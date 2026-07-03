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

import static network.ike.richsurface.terms.RichSurfaceStamps.INCEPTION;
import static network.ike.richsurface.terms.RichSurfaceTerms.NAMESPACE;
import static network.ike.richsurface.terms.RichSurfaceTerms.RICH_SURFACE_ROOT;

/**
 * Wave 1 of the RichSurfaceTerms ledger — the journal chronology model of
 * {@code IKE-Network/ike-issues#807}, per the {@code dev-rich-surface-terms-inventory}
 * topic: the module and root concepts, the element-kind taxonomy, the property keys,
 * and the manifest and element patterns.
 * <p>
 * This file is an append-only version ledger: declarations are appended and revised
 * through new {@code .at(stamp)} scopes, never edited in place once released. Identity
 * derives from each declaration's fully qualified name at birth; the birth FQN is
 * therefore permanent (revising the FQN display text later is fine — the seed is
 * recorded by the ledger's history).
 */
public final class Wave1 {

    private Wave1() {
    }

    /**
     * Replays the wave-1 declarations into the open datastore. Requires a started
     * {@code PrimitiveData} store; deterministic — replaying again writes the same
     * identities, stamps, and versions.
     */
    public static void compose() {
        // The module concept every stamp in this set cites. Its own versions carry
        // those stamps — self-reference is well-defined under derived identity.
        NAMESPACE.concept("RichSurfaceTerms module (RichSurfaceTerms)")
                .at(INCEPTION)
                    .synonym("RichSurfaceTerms module")
                    .definition("The tinkar module scoping every stamp of the RichSurfaceTerms"
                            + " content set; the export dimension for this knowledge, in whole"
                            + " or in part.")
                .build();

        // The taxonomy root for this content set.
        NAMESPACE.concept("RichSurfaceTerms root (RichSurfaceTerms)")
                .at(INCEPTION)
                    .synonym("RichSurfaceTerms root")
                    .definition("Root concept of the RichSurfaceTerms starter knowledge for"
                            + " the chronology-backed rich interaction surface.")
                .build();

        // Root of the element kinds (inventory: element-kind taxonomy, wave 1).
        NAMESPACE.concept("Journal element (RichSurfaceTerms)")
                .at(INCEPTION)
                    .synonym("Journal element")
                    .definition("Root kind of the blocks a conversation journal orders.")
                    .statedAxioms(leb -> leb.NecessarySet(leb.And(
                            leb.ConceptAxiom(RICH_SURFACE_ROOT))))
                .build();
    }
}
