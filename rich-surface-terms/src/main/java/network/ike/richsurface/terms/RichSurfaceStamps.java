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

/**
 * The stamp file: the append-only ledger of declared stamps for this content set.
 * <p>
 * Discipline: stamps are appended, never edited or deleted. A stamp is an explicit
 * (status, time, author, module, path) tuple with a declared literal time; its identity
 * derives from the tuple, so restating the same tuple is the same stamp. Retiring
 * content means appending a new inactive stamp, never mutating an existing one.
 * Stamps are named for the release or curation event they stamp.
 */
public final class RichSurfaceStamps {

    private RichSurfaceStamps() {
    }

    /**
     * The set's inception: wave-1 authoring per the inventory
     * ({@code dev-rich-surface-terms-inventory}, {@code #807}).
     * Author is the generic user concept pending a curator-identity decision.
     */
    public static final ActiveStamp INCEPTION = Stamp.active("2026-07-03T00:00:00Z",
            TinkarTerm.USER, RichSurfaceTerms.RICH_SURFACE_MODULE, TinkarTerm.DEVELOPMENT_PATH);
}
