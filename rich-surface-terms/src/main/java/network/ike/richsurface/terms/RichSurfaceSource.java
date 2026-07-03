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
import dev.ikm.tinkar.entity.builder.KnowledgeSource;

/**
 * The {@link KnowledgeSource} this ledger module provides: composes every wave into the
 * RichSurface session. Discovered by build tooling ({@code ike:knowledge-bindings},
 * change-set export, the release verifier) via {@link java.util.ServiceLoader}.
 */
public final class RichSurfaceSource implements KnowledgeSource {

    /**
     * Creates the source. Public no-arg constructor for {@link java.util.ServiceLoader}.
     */
    public RichSurfaceSource() {
    }

    /**
     * Composes the full RichSurface ledger — every wave, in order.
     *
     * @return the composed knowledge set
     */
    @Override
    public KnowledgeSet compose() {
        Wave1.compose();
        return RichSurface.RICH_SURFACE;
    }
}
