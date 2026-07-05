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

import static network.ike.richsurface.terms.RichSurface.COMPONENT_LIST_ELEMENT;
import static network.ike.richsurface.terms.RichSurface.ELEMENT_CONTENT;
import static network.ike.richsurface.terms.RichSurface.ELEMENT_ORDER;
import static network.ike.richsurface.terms.RichSurface.JOURNAL_ELEMENTS;
import static network.ike.richsurface.terms.RichSurface.JOURNAL_MANIFEST;
import static network.ike.richsurface.terms.RichSurface.LISTED_COMPONENTS;
import static network.ike.richsurface.terms.RichSurface.PROSE_CONTENT;
import static network.ike.richsurface.terms.RichSurface.PROSE_ELEMENT;
import static network.ike.richsurface.terms.RichSurface.REFERENCED_CHRONOLOGY;
import static network.ike.richsurface.terms.RichSurface.REFERENCE_ELEMENT;
import static network.ike.richsurface.terms.RichSurface.RICH_SURFACE;
import static network.ike.richsurface.terms.RichSurface.RICH_SURFACE_MODULE;

/**
 * The pattern section of the RichSurfaceTerms ledger: the journal manifest and element
 * patterns of the chronology model ({@code IKE-Network/ike-issues#807}), wiring the
 * {@link MeaningAndPurposeSet} vocabulary to existing datatype concepts — Component id
 * list, String, and Component; the set mints no datatypes.
 * <p>
 * Same ledger discipline as every section: time-major, inline stamps, append-only,
 * birth-FQN identity. A pattern version (meaning, purpose, field definitions) restates
 * as a whole; a fieldless version is a membership pattern.
 */
public final class PatternSet {

    private PatternSet() {
    }

    /**
     * Composes the pattern declarations into the knowledge-set session.
     */
    public static void compose() {

        // ============================================================ 2026-07-03
        ActiveStamp inception = Stamp.active("2026-07-03T00:00:00Z",
                TinkarTerm.USER, RICH_SURFACE_MODULE, TinkarTerm.DEVELOPMENT_PATH);

        // One manifest semantic per conversation, on the journal-anchor concept:
        // an ordered component-id list of the conversation's elements. Reorder or
        // insert = a new manifest version; replay = walking versions in time.
        RICH_SURFACE.pattern("Journal manifest pattern (RichSurfaceTerms)").at(inception)
                .meaning(JOURNAL_MANIFEST).purpose(ELEMENT_ORDER)
                .field(JOURNAL_ELEMENTS, ELEMENT_ORDER, TinkarTerm.COMPONENT_ID_LIST_FIELD)
                .synonym("Journal manifest pattern")
                .definition("Holds the ordered elements of one conversation journal; a new"
                        + " version per insert or reorder, so replay walks manifest versions"
                        + " in time.");

        // Element patterns: the structural kinds. The pattern IS the structural
        // classification — a block's kind is carried by which pattern its element
        // semantic uses.
        RICH_SURFACE.pattern("Prose element pattern (RichSurfaceTerms)").at(inception)
                .meaning(PROSE_ELEMENT).purpose(ELEMENT_CONTENT)
                .field(PROSE_CONTENT, ELEMENT_CONTENT, TinkarTerm.STRING)
                .synonym("Prose element pattern")
                .definition("An embedded prose block: markdown with id-bearing k: tokens as"
                        + " the interchange form.");

        RICH_SURFACE.pattern("Component-list element pattern (RichSurfaceTerms)").at(inception)
                .meaning(COMPONENT_LIST_ELEMENT).purpose(ELEMENT_CONTENT)
                .field(LISTED_COMPONENTS, ELEMENT_CONTENT, TinkarTerm.COMPONENT_ID_LIST_FIELD)
                .synonym("Component-list element pattern")
                .definition("An embedded ad-hoc component list the journal owns.");

        RICH_SURFACE.pattern("Reference element pattern (RichSurfaceTerms)").at(inception)
                .meaning(REFERENCE_ELEMENT).purpose(ELEMENT_CONTENT)
                .field(REFERENCED_CHRONOLOGY, ELEMENT_CONTENT, TinkarTerm.COMPONENT_FIELD)
                .synonym("Reference element pattern")
                .definition("A block over live knowledge: a component reference resolving"
                        + " latest-on-coordinate — reference, never copy.");
    }
}
