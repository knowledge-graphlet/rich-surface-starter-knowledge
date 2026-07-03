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

import dev.ikm.tinkar.entity.builder.Namespace;
import dev.ikm.tinkar.terms.EntityProxy;

/**
 * The RichSurfaceTerms bindings: the namespace — the content set's single identity
 * literal — and the reference handles for its concepts. Every identity in this set is
 * {@code T5(NAMESPACE, fullyQualifiedNameAtBirth)}; the constants here are handles
 * derived from birth FQNs, never independent identity sources, so renaming a constant
 * changes nothing and a handle can be reconstructed from the ledger alone.
 * <p>
 * Handles are declared for content other declarations reference (parents in stated
 * axioms, the module concept in stamps). The full inventory this set realizes is the
 * {@code dev-rich-surface-terms-inventory} topic in the Kompendium (Part IV), tracked
 * by {@code IKE-Network/ike-issues#807}.
 */
public final class RichSurfaceTerms {

    private RichSurfaceTerms() {
    }

    /** The content set's identity root — the only identity literal in this module. */
    public static final Namespace NAMESPACE =
            Namespace.of("eaa95241-1a1b-4c48-8266-4be1fe9124b0");

    /**
     * The tinkar module concept that scopes every stamp in this set — the STAMP
     * dimension by which this content is exported, in whole or in part. Declared in
     * {@link Wave1}; self-referentially cited by the stamps that version it, which is
     * well-defined because references resolve by derived identity.
     */
    public static final EntityProxy.Concept RICH_SURFACE_MODULE =
            NAMESPACE.conceptRef("RichSurfaceTerms module (RichSurfaceTerms)");

    /** The root concept under which the element-kind taxonomy classifies. Declared in {@link Wave1}. */
    public static final EntityProxy.Concept RICH_SURFACE_ROOT =
            NAMESPACE.conceptRef("RichSurfaceTerms root (RichSurfaceTerms)");

    /** Root kind of the blocks a conversation journal orders. Declared in {@link Wave1}. */
    public static final EntityProxy.Concept JOURNAL_ELEMENT =
            NAMESPACE.conceptRef("Journal element (RichSurfaceTerms)");
}
