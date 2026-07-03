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
package network.ike.richsurface.terms;

import dev.ikm.tinkar.entity.builder.KnowledgeSet;
import dev.ikm.tinkar.terms.EntityProxy;

/**
 * The RichSurface knowledge set — its UUID is the content set's single identity literal —
 * and the ledger-internal reference handles. Every identity in this set is
 * {@code T5(setUuid, fullyQualifiedNameAtBirth)}; the handles here are derived from
 * birth FQNs, never independent identity sources, so renaming one changes nothing.
 * <p>
 * Handles are declared only for content the ledger itself references (parents in stated
 * axioms, the module concept in stamps). Downstream consumers do not use this class:
 * they depend on the <em>generated</em> {@code RichSurfaceTerms} bindings artifact
 * ({@code rich-surface-bindings}), produced from this ledger by
 * {@code ike:knowledge-bindings}. The inventory this set realizes is the
 * {@code dev-rich-surface-terms-inventory} topic in the Kompendium (Part IV), tracked
 * by {@code IKE-Network/ike-issues#807}.
 */
public final class RichSurface {

    private RichSurface() {
    }

    /** The knowledge set — its UUID is the only identity literal in this module. */
    public static final KnowledgeSet RICH_SURFACE =
            KnowledgeSet.of("eaa95241-1a1b-4c48-8266-4be1fe9124b0");

    /**
     * The tinkar module concept that scopes every stamp in this set — the STAMP
     * dimension by which this content is exported, in whole or in part. Declared in
     * {@link ConceptSet}; self-referentially cited by the stamps that version it, which is
     * well-defined because references resolve by derived identity.
     */
    public static final EntityProxy.Concept RICH_SURFACE_MODULE =
            RICH_SURFACE.conceptRef("RichSurfaceTerms module (RichSurfaceTerms)");

    /** The root concept under which the element-kind taxonomy classifies. Declared in {@link ConceptSet}. */
    public static final EntityProxy.Concept RICH_SURFACE_ROOT =
            RICH_SURFACE.conceptRef("RichSurfaceTerms root (RichSurfaceTerms)");

    /** Root kind of the blocks a conversation journal orders. Declared in {@link ConceptSet}. */
    public static final EntityProxy.Concept JOURNAL_ELEMENT =
            RICH_SURFACE.conceptRef("Journal element (RichSurfaceTerms)");
}
