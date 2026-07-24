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
import dev.ikm.tinkar.entity.builder.KnowledgeSetSource;

/**
 * The {@link KnowledgeSetSource} this ledger module provides: composes every functional
 * section into the RichSurface session. Discovered by build tooling
 * ({@code ike:knowledge-bindings}, change-set export, the release verifier) via
 * {@link java.util.ServiceLoader}.
 */
public final class RichSurfaceSource implements KnowledgeSetSource {

    /**
     * Creates the source. Public no-arg constructor for {@link java.util.ServiceLoader}.
     */
    public RichSurfaceSource() {
    }

    /**
     * Composes the full RichSurface ledger — every functional section, in reading
     * order. Sections are independent (references resolve by derived identity); the
     * order here is for the reader.
     *
     * @return the composed knowledge set
     */
    @Override
    public KnowledgeSet compose() {
        // The IKE foundation is this set's declared external dependency (ike-terms in the
        // POM and module-info, IKE-Network/ike-issues#937) — it is NOT composed here.
        // Composing it would write the foundation into whatever store this set is
        // replayed into, and ike:knowledge-export exports the whole store: the change set
        // would absorb the foundation instead of remaining a delta atop it. Whoever
        // replays this set supplies the foundation first — the consumer at load time, and
        // {@code LedgerReplayTest} in its fixture, where closure is verified.
        MeaningAndPurposeSet.compose();
        ConceptSet.compose();
        PatternSet.compose();
        return RichSurface.RICH_SURFACE;
    }
}
