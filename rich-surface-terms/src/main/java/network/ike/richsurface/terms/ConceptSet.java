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
 * The concept section of the RichSurfaceTerms ledger: the set's own module and root,
 * and the element-kind taxonomy of the journal chronology model
 * ({@code IKE-Network/ike-issues#807}, per {@code dev-rich-surface-terms-inventory}).
 * <p>
 * The file reads time-major, as a true ledger: a stamp is declared inline, the edits
 * under it follow, then the next stamp. Components are pulled back up by their birth
 * FQN and simply continue; nothing is restated. Declarations are appended, never edited
 * in place once released; identity derives from each declaration's fully qualified name
 * at birth, so the birth FQN is permanent (revising its display text later is a new
 * version, not a new identity).
 */
public final class ConceptSet {

    private ConceptSet() {
    }

    /**
     * Composes the concept declarations into the knowledge-set session. The
     * {@link RichSurfaceSource} orders the sections; {@code KnowledgeSet.write()}
     * replays the session, idempotently.
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
        // Both anchor into the base (Tinkar starter set) taxonomy so a KB that
        // loads base + this set navigates to it from the root — no orphan forest.
        // MODEL_CONCEPT as the root's anchor is provisional; revisable pre-release.
        RICH_SURFACE.concept("RichSurfaceTerms module (RichSurfaceTerms)").at(inception)
                .synonym("RichSurfaceTerms module")
                .definition("The tinkar module scoping every stamp of the RichSurfaceTerms"
                        + " content set; the export dimension for this knowledge, in whole"
                        + " or in part.")
                .isA(TinkarTerm.MODULE);

        RICH_SURFACE.concept("RichSurfaceTerms root (RichSurfaceTerms)").at(inception)
                .synonym("RichSurfaceTerms root")
                .definition("Root concept of the RichSurfaceTerms starter knowledge for"
                        + " the chronology-backed rich interaction surface.")
                .isA(TinkarTerm.MODEL_CONCEPT);

        RICH_SURFACE.concept("Journal element (RichSurfaceTerms)").at(inception)
                .synonym("Journal element")
                .definition("Root kind of the blocks a conversation journal orders.")
                .isA(RICH_SURFACE_ROOT);

        // The structural element kinds: one per element pattern. A block's structural
        // kind is carried by which element pattern its semantic uses; these concepts
        // make the kinds classifiable and navigable like any terminology.
        RICH_SURFACE.concept("Prose element (RichSurfaceTerms)").at(inception)
                .synonym("Prose element")
                .definition("An embedded prose block: the journal owns the content, carried"
                        + " as text on the prose element pattern.")
                .isA(RichSurface.JOURNAL_ELEMENT);

        RICH_SURFACE.concept("Component-list element (RichSurfaceTerms)").at(inception)
                .synonym("Component-list element")
                .definition("An embedded ad-hoc component list: the journal owns the"
                        + " membership, carried as an ordered component-id list on the"
                        + " component-list element pattern.")
                .isA(RichSurface.JOURNAL_ELEMENT);

        RICH_SURFACE.concept("Reference element (RichSurfaceTerms)").at(inception)
                .synonym("Reference element")
                .definition("A block over live knowledge: holds a component reference to an"
                        + " existing chronology and resolves latest-on-coordinate —"
                        + " reference, never copy.")
                .isA(RichSurface.JOURNAL_ELEMENT);

        RICH_SURFACE.concept("Tombstone element (RichSurfaceTerms)").at(inception)
                .synonym("Tombstone element")
                .definition("A documented absence: how an excised element renders, so replay"
                        + " degrades from re-executable to verifiable rather than breaking.")
                .isA(RichSurface.JOURNAL_ELEMENT);
    }
}
