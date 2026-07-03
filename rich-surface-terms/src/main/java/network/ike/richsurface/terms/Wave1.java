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

import dev.ikm.tinkar.entity.builder.ActiveStamp;
import dev.ikm.tinkar.entity.builder.Stamp;
import dev.ikm.tinkar.terms.TinkarTerm;

import static network.ike.richsurface.terms.RichSurface.RICH_SURFACE;
import static network.ike.richsurface.terms.RichSurface.RICH_SURFACE_MODULE;
import static network.ike.richsurface.terms.RichSurface.RICH_SURFACE_ROOT;

/**
 * Wave 1 of the RichSurfaceTerms ledger — the journal chronology model of
 * {@code IKE-Network/ike-issues#807}, per the {@code dev-rich-surface-terms-inventory}
 * topic: the module and root concepts, the element-kind taxonomy, the property keys,
 * and the manifest and element patterns.
 * <p>
 * The file reads time-major, as a true ledger: a stamp is declared inline, the edits
 * under it follow — across any components — then the next stamp, and so on. Components
 * are pulled back up by their birth FQN and simply continue; nothing is restated.
 * Declarations are appended, never edited in place once released; identity derives from
 * each declaration's fully qualified name at birth, so the birth FQN is permanent
 * (revising its display text later is a new version, not a new identity).
 */
public final class Wave1 {

    private Wave1() {
    }

    /**
     * Composes the wave-1 declarations into the knowledge-set session. The caller writes the
     * session ({@link dev.ikm.tinkar.entity.builder.KnowledgeSet#write()}) when composition
     * is complete; writing is idempotent — replay produces the same identities, stamps,
     * and versions.
     */
    public static void compose() {

        // ============================================================ 2026-07-03
        // Inception: the set's own module, the taxonomy root, and the first
        // element kind. Author is the generic user concept pending a
        // curator-identity decision.
        ActiveStamp inception = Stamp.active("2026-07-03T00:00:00Z",
                TinkarTerm.USER, RICH_SURFACE_MODULE, TinkarTerm.DEVELOPMENT_PATH);

        // The module concept every stamp in this set cites — including its own
        // versions' stamps; self-reference is well-defined under derived identity.
        RICH_SURFACE.concept("RichSurfaceTerms module (RichSurfaceTerms)").at(inception)
                .synonym("RichSurfaceTerms module")
                .definition("The tinkar module scoping every stamp of the RichSurfaceTerms"
                        + " content set; the export dimension for this knowledge, in whole"
                        + " or in part.");

        RICH_SURFACE.concept("RichSurfaceTerms root (RichSurfaceTerms)").at(inception)
                .synonym("RichSurfaceTerms root")
                .definition("Root concept of the RichSurfaceTerms starter knowledge for"
                        + " the chronology-backed rich interaction surface.");

        RICH_SURFACE.concept("Journal element (RichSurfaceTerms)").at(inception)
                .synonym("Journal element")
                .definition("Root kind of the blocks a conversation journal orders.")
                .statedAxioms(leb -> leb.NecessarySet(leb.And(
                        leb.ConceptAxiom(RICH_SURFACE_ROOT))));
    }
}
