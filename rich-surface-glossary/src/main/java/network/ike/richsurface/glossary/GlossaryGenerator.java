/*
 * Copyright © 2026 IKE Network (support@ike.network)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package network.ike.richsurface.glossary;

import dev.ikm.tinkar.common.id.PublicId;
import dev.ikm.tinkar.entity.builder.KnowledgeSet;
import dev.ikm.tinkar.entity.builder.KnowledgeSet.Declaration;
import dev.ikm.tinkar.entity.builder.KnowledgeSetSource;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * The doc projection of a knowledge set — beside the code projection (bindings) and the
 * KB projection (change set). From the ledger it produces two files the guide consumes
 * through the standard {@code koncept-asciidoc-extension}:
 * <ol>
 *   <li>a {@code koncepts.yml} — one entry per concept and pattern (label, definition,
 *       identity UUID, kind, and the taxonomy as a DL axiom) — the extension's definition
 *       source; and</li>
 *   <li>a Content Inventory fragment where every concept and pattern is a {@code k:}
 *       Koncept chip (the standard identicon-and-name badge, multi-backend, linked to the
 *       auto-generated glossary), with its parents and children rendered as chips too, so
 *       the whole set is browsable as a taxonomy of standard chips.</li>
 * </ol>
 * The identicons and the glossary of definitions are rendered by the extension at doc
 * build time (the same LifeHash badge Komet draws), so this generator needs no identicon
 * library — it only walks the ledger's {@code declarations()} read surface, store-free.
 * <p>
 * The knowledge set is discovered via {@link ServiceLoader} over {@link KnowledgeSetSource}.
 * When a second knowledge set needs a glossary this generalizes to a tinkar-side main
 * plus an {@code ike:knowledge-glossary} goal, the same shape as bindings and export.
 */
public final class GlossaryGenerator {

    private GlossaryGenerator() {
    }

    /**
     * Generates the koncepts YAML and the Content Inventory fragment.
     *
     * @param args {@code konceptsYmlFile fragmentAdocFile [sourceClassName]}
     * @throws Exception if discovery, composition, or writing fails
     */
    public static void main(String[] args) throws Exception {
        if (args.length < 2 || args.length > 3) {
            throw new IllegalArgumentException(
                    "Usage: GlossaryGenerator <konceptsYmlFile> <fragmentAdocFile> [sourceClassName]");
        }
        Path konceptsYml = Path.of(args[0]);
        Path fragmentAdoc = Path.of(args[1]);

        KnowledgeSetSource source;
        if (args.length == 3) {
            source = (KnowledgeSetSource) Class.forName(args[2], true,
                    GlossaryGenerator.class.getClassLoader()).getDeclaredConstructor().newInstance();
        } else {
            List<KnowledgeSetSource> found = new ArrayList<>();
            ServiceLoader.load(KnowledgeSetSource.class, GlossaryGenerator.class.getClassLoader())
                    .forEach(found::add);
            if (found.size() != 1) {
                throw new IllegalStateException("Expected exactly one KnowledgeSetSource on the classpath, found "
                        + found.size() + " — pass the implementation class name as the third argument");
            }
            source = found.getFirst();
        }

        KnowledgeSet knowledgeSet = source.compose();
        List<Declaration> declarations = knowledgeSet.declarations();
        Model model = new Model(declarations);

        Files.createDirectories(konceptsYml.toAbsolutePath().getParent());
        Files.writeString(konceptsYml, model.renderYaml());
        Files.createDirectories(fragmentAdoc.toAbsolutePath().getParent());
        Files.writeString(fragmentAdoc, model.renderFragment());

        System.out.println("Koncepts written: " + konceptsYml + " and " + fragmentAdoc
                + " — " + declarations.size() + " entries from " + source.getClass().getName());
    }

    /**
     * The resolved view of a set for rendering: a stable {@code k:} identifier per
     * declaration, identity/label/definition/kind, and the parent and child edges (an
     * out-of-set parent has no identifier and renders as plain text).
     */
    static final class Model {
        private final List<Declaration> byName;
        private final Map<PublicId, Declaration> byId = new LinkedHashMap<>();
        private final Map<PublicId, String> identifierById = new LinkedHashMap<>();
        private final Map<PublicId, List<Declaration>> childrenOf = new LinkedHashMap<>();

        Model(List<Declaration> declarations) {
            byName = new ArrayList<>(declarations);
            byName.sort(Comparator.comparing(Declaration::birthFqn));
            Map<String, String> identifierToFqn = new LinkedHashMap<>();
            for (Declaration declaration : byName) {
                byId.put(declaration.publicId(), declaration);
                String identifier = identifier(declaration.birthFqn());
                String prior = identifierToFqn.putIfAbsent(identifier, declaration.birthFqn());
                if (prior != null) {
                    throw new IllegalStateException("Koncept identifier collision: \"" + prior
                            + "\" and \"" + declaration.birthFqn() + "\" both reduce to " + identifier);
                }
                identifierById.put(declaration.publicId(), identifier);
            }
            for (Declaration declaration : byName) {
                for (PublicId parent : declaration.supertypes()) {
                    childrenOf.computeIfAbsent(parent, k -> new ArrayList<>()).add(declaration);
                }
            }
        }

        String renderYaml() {
            StringBuilder sb = new StringBuilder();
            sb.append("# Generated by rich-surface-glossary from the ledger — DO NOT EDIT.\n");
            sb.append("# Definition source for the koncept-asciidoc-extension (k: macros).\n\n");
            for (Declaration declaration : byName) {
                sb.append(identifierById.get(declaration.publicId())).append(":\n");
                sb.append("  label: ").append(yaml(label(declaration.birthFqn()))).append('\n');
                declaration.definition().ifPresent(def ->
                        sb.append("  definition: ").append(yaml(def)).append('\n'));
                sb.append("  kind: ").append(declaration.kind() == Declaration.Kind.PATTERN
                        ? "pattern" : "concept").append('\n');
                String axiom = axiom(declaration);
                if (axiom != null) {
                    sb.append("  axiom: ").append(yaml(axiom)).append('\n');
                }
                sb.append("  uuids:\n");
                sb.append("    - ").append(declaration.publicId().asUuidArray()[0]).append('\n');
                sb.append('\n');
            }
            return sb.toString();
        }

        String renderFragment() {
            StringBuilder sb = new StringBuilder();
            sb.append("// Generated by rich-surface-glossary — DO NOT EDIT.\n\n");
            renderGroup(sb, Declaration.Kind.CONCEPT, "Concepts");
            renderGroup(sb, Declaration.Kind.PATTERN, "Patterns");
            return sb.toString();
        }

        private void renderGroup(StringBuilder sb, Declaration.Kind kind, String heading) {
            List<Declaration> group = byName.stream().filter(d -> d.kind() == kind).toList();
            if (group.isEmpty()) {
                return;
            }
            sb.append('.').append(heading).append(" (").append(group.size()).append(")\n");
            sb.append("[glossary]\n");
            for (Declaration declaration : group) {
                sb.append(chip(declaration)).append("::\n");
                List<Declaration> children = childrenOf.getOrDefault(declaration.publicId(), List.of());
                boolean any = false;
                if (!declaration.supertypes().isEmpty()) {
                    sb.append("Parents: ").append(parentChips(declaration.supertypes())).append('\n');
                    any = true;
                }
                if (!children.isEmpty()) {
                    if (any) {
                        sb.append("+\n");
                    }
                    sb.append("Children: ").append(childChips(children)).append('\n');
                    any = true;
                }
                if (!any) {
                    sb.append("{empty}\n");
                }
                sb.append('\n');
            }
            sb.append('\n');
        }

        /** The standard Koncept chip for a declaration: {@code k:Identifier[]}. */
        private String chip(Declaration declaration) {
            return "k:" + identifierById.get(declaration.publicId()) + "[]";
        }

        private String parentChips(List<PublicId> parents) {
            List<String> parts = new ArrayList<>();
            for (PublicId parent : parents) {
                Declaration target = byId.get(parent);
                parts.add(target != null ? chip(target) : "__outside this set__");
            }
            return String.join(" ", parts);
        }

        private String childChips(List<Declaration> children) {
            List<String> parts = new ArrayList<>();
            for (Declaration child : children) {
                parts.add(chip(child));
            }
            return String.join(" ", parts);
        }

        /** The DL taxonomy axiom for a concept with in-set parents, or null. */
        private String axiom(Declaration declaration) {
            List<String> parentLabels = new ArrayList<>();
            for (PublicId parent : declaration.supertypes()) {
                Declaration target = byId.get(parent);
                if (target != null) {
                    parentLabels.add(label(target.birthFqn()));
                }
            }
            return parentLabels.isEmpty() ? null : "⊑ " + String.join(" ⊓ ", parentLabels);
        }
    }

    /**
     * The stable {@code k:} identifier for a birth FQN: the tag-stripped name in
     * PascalCase. FQNs are unique within a set, so identifiers are too (a collision is a
     * build error).
     *
     * @param birthFqn the fully qualified name at birth
     * @return the PascalCase identifier
     */
    static String identifier(String birthFqn) {
        String base = label(birthFqn);
        StringBuilder sb = new StringBuilder();
        for (String word : base.split("[^A-Za-z0-9]+")) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1));
            }
        }
        if (sb.length() == 0) {
            throw new IllegalStateException("Birth FQN reduces to an empty identifier: \"" + birthFqn + "\"");
        }
        if (Character.isDigit(sb.charAt(0))) {
            sb.insert(0, 'N');
        }
        return sb.toString();
    }

    /** The display label: the FQN with its trailing parenthesized semantic tag stripped. */
    static String label(String birthFqn) {
        return birthFqn.replaceAll("\\s*\\([^)]*\\)\\s*$", "").trim();
    }

    /** Renders a scalar as a double-quoted YAML string. */
    private static String yaml(String text) {
        return '"' + text.replace("\\", "\\\\").replace("\"", "\\\"") + '"';
    }
}
