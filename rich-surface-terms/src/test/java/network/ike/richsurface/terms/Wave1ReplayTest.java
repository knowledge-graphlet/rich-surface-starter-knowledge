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

import dev.ikm.tinkar.common.service.CachingService;
import dev.ikm.tinkar.common.service.PrimitiveData;
import dev.ikm.tinkar.common.service.ServiceKeys;
import dev.ikm.tinkar.common.service.ServiceProperties;
import dev.ikm.tinkar.entity.ConceptEntity;
import dev.ikm.tinkar.entity.Entity;
import dev.ikm.tinkar.entity.EntityHandle;
import dev.ikm.tinkar.entity.EntityService;
import dev.ikm.tinkar.entity.StampEntity;
import dev.ikm.tinkar.terms.State;
import dev.ikm.tinkar.terms.TinkarTerm;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Replays the wave-1 ledger into an ephemeral store and verifies the declared
 * chronologies — including that replaying twice is idempotent.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Wave1ReplayTest {

    @BeforeAll
    static void beforeAll() {
        CachingService.clearAll();
        ServiceProperties.set(ServiceKeys.DATA_STORE_ROOT,
                Path.of(System.getProperty("user.dir"))
                        .resolve("target").resolve("Wave1ReplayTest").resolve("datastore").toFile());
        PrimitiveData.selectControllerByName("Load Ephemeral Store");
        PrimitiveData.start();
        Wave1.compose();
        Wave1.compose(); // idempotence: same identities, stamps, versions
    }

    @AfterAll
    static void afterAll() {
        PrimitiveData.stop();
    }

    @Test
    @DisplayName("Wave-1 concepts replay with inception stamps, once")
    void conceptsReplay() {
        ConceptEntity<?> journalElement =
                EntityHandle.get(RichSurfaceTerms.JOURNAL_ELEMENT.nid()).expectConcept();
        assertNotNull(journalElement);
        assertEquals(1, journalElement.versions().size(), "replay must not duplicate versions");

        StampEntity<?> stamp = Entity.getStamp(journalElement.versions().get(0).stampNid());
        assertEquals(State.ACTIVE, stamp.state());
        assertEquals(RichSurfaceStamps.INCEPTION.time(), stamp.time());
        assertEquals(RichSurfaceTerms.RICH_SURFACE_MODULE.nid(), stamp.moduleNid(),
                "stamps are scoped by the set's own module concept");
    }

    @Test
    @DisplayName("The module concept is self-scoped and carries its descriptions")
    void moduleConceptSelfScoped() {
        ConceptEntity<?> module =
                EntityHandle.get(RichSurfaceTerms.RICH_SURFACE_MODULE.nid()).expectConcept();
        assertEquals(1, module.versions().size());
        assertEquals(RichSurfaceTerms.RICH_SURFACE_MODULE.nid(),
                Entity.getStamp(module.versions().get(0).stampNid()).moduleNid(),
                "the module concept's own versions cite the module — well-defined under derived identity");

        int[] descriptions = EntityService.get().semanticNidsForComponentOfPattern(
                module.nid(), TinkarTerm.DESCRIPTION_PATTERN.nid());
        assertEquals(3, descriptions.length, "FQN + synonym + definition");
    }

    @Test
    @DisplayName("Journal element classifies under the RichSurfaceTerms root")
    void journalElementAxioms() {
        int[] axiomNids = EntityService.get().semanticNidsForComponentOfPattern(
                RichSurfaceTerms.JOURNAL_ELEMENT.nid(),
                TinkarTerm.EL_PLUS_PLUS_STATED_AXIOMS_PATTERN.nid());
        assertEquals(1, axiomNids.length);
    }
}
