# Rich Surface Starter Knowledge

The **RichSurfaceTerms** starter knowledge: a ledger-form content module for the
chronology-backed rich interaction surface and the Scriptorium
([IKE-Network/ike-issues#806](https://github.com/IKE-Network/ike-issues/issues/806)–#812).

The authoritative artifact is the **ledger** — the Java source in
`rich-surface-terms`: a time-major, append-only record organized in functional sections — stamps
declared inline, the edits under each following — authored with the tinkar
chronology builders
([#822](https://github.com/IKE-Network/ike-issues/issues/822)). Building replays
the ledger; the released artifact is a **change set**. A starter set is a change
set whose base is the empty store.

## Modules

| Module | Role |
|--------|------|
| `rich-surface-terms` | The ledger: functional sections (ConceptSet, MeaningAndPurposeSet, PatternSet), inline-stamped and time-major; replay verified over an ephemeral store |
| `rich-surface-bindings` | Generated `RichSurfaceTerms` bindings (ike:knowledge-bindings): resolved UUIDs, definitions as javadoc |
| `rich-surface-doc` | The guide: ledger model, content inventory, build and release discipline |

## Building

```bash
./mvnw clean verify
```

**Interim constraint:** the ledger depends on
`dev.ikm.tinkar:entity:1.127.2-chronology-builder-SNAPSHOT` — the chronology-builder
authoring tier, currently on the `ike-komet-wsr꞉chronology-builder` feature sibling.
Build that reactor first (`./mvnw clean install -DskipTests` from the sibling) so the
dependency resolves from your local repository. The version flips to the main line
when the feature merges.

## Content

The inventory this set realizes is the `dev-rich-surface-terms-inventory` topic in
the Kompendium (Part IV): element-kind taxonomy, property keys, the journal manifest
and element patterns (wave 1, `#807`); role kinds, module policies, path classes
(wave 2, `#809`–`#811`).

Bootstrap tracked in
[IKE-Network/ike-issues#823](https://github.com/IKE-Network/ike-issues/issues/823).
<!-- BEGIN ike-managed: developer-setup -->

## Developer Setup

New to IKE development? The
[Developer Environment guide](https://ike.network/ike-tooling/ike-build-standards/developer-environment.html)
covers IDE configuration, JDK 25 setup, and the tooling conventions
every IKE workspace expects — start there before your first build.
<!-- END ike-managed: developer-setup -->
