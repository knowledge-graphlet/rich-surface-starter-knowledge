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
 * The meaning-and-purpose section of the RichSurfaceTerms ledger: the field vocabulary —
 * property-key concepts (field meanings) and purpose concepts — the patterns in
 * {@link PatternSet} wire to datatypes. Purposes are shared deliberately across patterns
 * where semantics genuinely match; field meanings are unique within each pattern (the
 * field's knowledge-level address).
 * <p>
 * Same ledger discipline as every section: time-major, inline stamps, append-only,
 * birth-FQN identity.
 */
public final class MeaningAndPurposeSet {

    private MeaningAndPurposeSet() {
    }

    /**
     * Composes the field-vocabulary declarations into the knowledge-set session.
     */
    public static void compose() {

        // ============================================================ 2026-07-03
        ActiveStamp inception = Stamp.active("2026-07-03T00:00:00Z",
                TinkarTerm.USER, RICH_SURFACE_MODULE, TinkarTerm.DEVELOPMENT_PATH);

        // ---- Pattern meanings ------------------------------------------------

        RICH_SURFACE.concept("Journal manifest (RichSurfaceTerms)").at(inception)
                .synonym("Journal manifest")
                .definition("What a manifest semantic means: the ordered membership of one"
                        + " conversation journal. Inserting or reordering a block writes a"
                        + " new manifest version; replay walks manifest versions in time.")
                .isA(RICH_SURFACE_ROOT);

        // ---- Field meanings (property keys) ----------------------------------
        // Each is the knowledge-level address of one field: unique within its
        // pattern, addressable via getFieldWithMeaning.

        RICH_SURFACE.concept("Journal elements (RichSurfaceTerms)").at(inception)
                .synonym("Journal elements")
                .definition("The ordered elements of one conversation journal — the manifest"
                        + " pattern's single field.")
                .isA(RICH_SURFACE_ROOT);

        RICH_SURFACE.concept("Prose content (RichSurfaceTerms)").at(inception)
                .synonym("Prose content")
                .definition("A prose block's text: markdown whose inline concept references"
                        + " are id-bearing k: tokens — the interchange form.")
                .isA(RICH_SURFACE_ROOT);

        RICH_SURFACE.concept("Listed components (RichSurfaceTerms)").at(inception)
                .synonym("Listed components")
                .definition("The members of an embedded component-list block, in order.")
                .isA(RICH_SURFACE_ROOT);

        RICH_SURFACE.concept("Referenced chronology (RichSurfaceTerms)").at(inception)
                .synonym("Referenced chronology")
                .definition("The live knowledge a reference block resolves — reference,"
                        + " never copy: reading resolves latest-on-coordinate; updating"
                        + " writes a new version of the referenced chronology.")
                .isA(RICH_SURFACE_ROOT);

        // ---- Purposes ---------------------------------------------------------
        // Shared across patterns where the purpose genuinely matches — shared
        // purpose concepts are what make patterns queryable by purpose. The
        // purpose sub-root classifies them under TinkarTerm.PURPOSE as well as
        // the set root (#844 phase 2: purpose classifies; module/path enforce),
        // so a purpose predicate can select by classification, not enumeration.

        RICH_SURFACE.concept("Rich surface purpose (RichSurfaceTerms)").at(inception)
                .synonym("Rich surface purpose")
                .definition("The class of purposes the rich-surface knowledge set declares:"
                        + " a permitted, complementary classification key read at boundaries"
                        + " (filtering, navigation) — never an enforcement carrier, which"
                        + " stays with module and path.")
                .isA(RICH_SURFACE_ROOT, TinkarTerm.PURPOSE);

        RICH_SURFACE.concept("Element order (RichSurfaceTerms)").at(inception)
                .synonym("Element order")
                .definition("Ordering a conversation's elements: the purpose of the journal"
                        + " manifest and its field.")
                .isA(RichSurface.RICH_SURFACE_PURPOSE);

        RICH_SURFACE.concept("Element content (RichSurfaceTerms)").at(inception)
                .synonym("Element content")
                .definition("Carrying an element's content: the purpose shared by the"
                        + " element patterns and their fields.")
                .isA(RichSurface.RICH_SURFACE_PURPOSE);
    }
}
