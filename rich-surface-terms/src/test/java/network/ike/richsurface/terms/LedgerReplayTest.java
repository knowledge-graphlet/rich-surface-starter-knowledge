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
import dev.ikm.tinkar.entity.PatternEntity;
import dev.ikm.tinkar.entity.PatternEntityVersion;
import dev.ikm.tinkar.entity.EntityService;
import dev.ikm.tinkar.entity.StampEntity;
import dev.ikm.tinkar.entity.builder.Stamp;
import dev.ikm.tinkar.terms.State;
import network.ike.foundation.ike.terms.IkeSource;
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
 * Replays the full ledger — every functional section, via the set's
 * {@code KnowledgeSetSource} — into an ephemeral store and verifies the declared
 * chronologies, including that replaying twice is idempotent.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LedgerReplayTest {

    @BeforeAll
    static void beforeAll() {
        CachingService.clearAll();
        ServiceProperties.set(ServiceKeys.DATA_STORE_ROOT,
                Path.of(System.getProperty("user.dir"))
                        .resolve("target").resolve("LedgerReplayTest").resolve("datastore").toFile());
        PrimitiveData.selectControllerByName("Load Ephemeral Store");
        PrimitiveData.start();
        // The declared external dependency, supplied first: this set's references to
        // foundation components resolve within its closure only when the foundation is
        // present in the store (IKE-Network/ike-issues#937). The consumer does the same at
        // load time; RichSurfaceSource itself must not compose it, or the exported change
        // set would absorb the foundation rather than remain a delta atop it.
        new IkeSource().compose().write();
        new RichSurfaceSource().compose().write();
        RichSurface.RICH_SURFACE.write(); // idempotence: same identities, stamps, versions
    }

    @AfterAll
    static void afterAll() {
        PrimitiveData.stop();
    }

    @Test
    @DisplayName("ConceptSet concepts replay with inception stamps, once")
    void conceptsReplay() {
        ConceptEntity<?> journalElement =
                EntityHandle.get(RichSurface.JOURNAL_ELEMENT.nid()).expectConcept();
        assertNotNull(journalElement);
        assertEquals(1, journalElement.versions().size(), "replay must not duplicate versions");

        StampEntity<?> stamp = Entity.getStamp(journalElement.versions().get(0).stampNid());
        assertEquals(State.ACTIVE, stamp.state());
        // Restating the inception tuple derives the same stamp — tuple identity.
        assertEquals(Stamp.active("2026-07-03T00:00:00Z", TinkarTerm.USER,
                RichSurface.RICH_SURFACE_MODULE, TinkarTerm.DEVELOPMENT_PATH).time(),
                stamp.time());
        assertEquals(RichSurface.RICH_SURFACE_MODULE.nid(), stamp.moduleNid(),
                "stamps are scoped by the set's own module concept");
    }

    @Test
    @DisplayName("The module concept is self-scoped and carries its descriptions")
    void moduleConceptSelfScoped() {
        ConceptEntity<?> module =
                EntityHandle.get(RichSurface.RICH_SURFACE_MODULE.nid()).expectConcept();
        assertEquals(1, module.versions().size());
        assertEquals(RichSurface.RICH_SURFACE_MODULE.nid(),
                Entity.getStamp(module.versions().get(0).stampNid()).moduleNid(),
                "the module concept's own versions cite the module — well-defined under derived identity");

        int[] descriptions = EntityService.get().semanticNidsForComponentOfPattern(
                module.nid(), TinkarTerm.DESCRIPTION_PATTERN.nid());
        assertEquals(3, descriptions.length, "FQN + synonym + definition");
    }

    @Test
    @DisplayName("Wave-1 patterns replay: manifest + three element patterns, fields wired")
    void patternsReplay() {
        PatternEntity<?> manifest = EntityHandle.get(
                RichSurface.RICH_SURFACE.patternRef("Journal manifest pattern (RichSurfaceTerms)").nid())
                .expectPattern();
        assertEquals(1, manifest.versions().size());
        PatternEntityVersion manifestVersion = (PatternEntityVersion) manifest.versions().get(0);
        assertEquals(RichSurface.JOURNAL_MANIFEST.nid(), manifestVersion.semanticMeaningNid());
        assertEquals(RichSurface.ELEMENT_ORDER.nid(), manifestVersion.semanticPurposeNid());
        assertEquals(1, manifestVersion.fieldDefinitions().size());
        assertEquals(RichSurface.JOURNAL_ELEMENTS.nid(),
                manifestVersion.fieldDefinitions().get(0).meaningNid());
        assertEquals(TinkarTerm.COMPONENT_ID_LIST_FIELD.nid(),
                manifestVersion.fieldDefinitions().get(0).dataTypeNid());

        for (String fqn : new String[]{
                "Prose element pattern (RichSurfaceTerms)",
                "Component-list element pattern (RichSurfaceTerms)",
                "Reference element pattern (RichSurfaceTerms)"}) {
            PatternEntity<?> pattern = EntityHandle.get(
                    RichSurface.RICH_SURFACE.patternRef(fqn).nid()).expectPattern();
            PatternEntityVersion version = (PatternEntityVersion) pattern.versions().get(0);
            assertEquals(RichSurface.ELEMENT_CONTENT.nid(), version.semanticPurposeNid(),
                    "element patterns share the element-content purpose: " + fqn);
            assertEquals(1, version.fieldDefinitions().size(), fqn);
        }
    }

    @Test
    @DisplayName("Element kinds classify under Journal element")
    void elementKindsClassify() {
        for (var kind : new dev.ikm.tinkar.terms.EntityProxy.Concept[]{
                RichSurface.PROSE_ELEMENT, RichSurface.COMPONENT_LIST_ELEMENT,
                RichSurface.REFERENCE_ELEMENT}) {
            int[] axiomNids = EntityService.get().semanticNidsForComponentOfPattern(
                    kind.nid(), TinkarTerm.EL_PLUS_PLUS_STATED_AXIOMS_PATTERN.nid());
            assertEquals(1, axiomNids.length, kind.description());
        }
    }

    @Test
    @DisplayName("Purposes classify under the purpose sub-root, which is dual-parented (#846)")
    void purposesClassifyUnderPurposeSubRoot() {
        // The sub-root itself: one stated-axiom semantic, dual-parent
        // (set root + TinkarTerm.PURPOSE).
        int[] subRootAxioms = EntityService.get().semanticNidsForComponentOfPattern(
                RichSurface.RICH_SURFACE_PURPOSE.nid(),
                TinkarTerm.EL_PLUS_PLUS_STATED_AXIOMS_PATTERN.nid());
        assertEquals(1, subRootAxioms.length);

        // Each purpose classifies under the sub-root.
        for (var purpose : new dev.ikm.tinkar.terms.EntityProxy.Concept[]{
                RichSurface.ELEMENT_ORDER, RichSurface.ELEMENT_CONTENT}) {
            int[] axiomNids = EntityService.get().semanticNidsForComponentOfPattern(
                    purpose.nid(), TinkarTerm.EL_PLUS_PLUS_STATED_AXIOMS_PATTERN.nid());
            assertEquals(1, axiomNids.length, purpose.description());
        }
    }

    @Test
    @DisplayName("Journal element classifies under the RichSurfaceTerms root")
    void journalElementAxioms() {
        int[] axiomNids = EntityService.get().semanticNidsForComponentOfPattern(
                RichSurface.JOURNAL_ELEMENT.nid(),
                TinkarTerm.EL_PLUS_PLUS_STATED_AXIOMS_PATTERN.nid());
        assertEquals(1, axiomNids.length);
    }
}
