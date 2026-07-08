# CLAUDE.md

Guidance for working in this repository: what BB-Lib is, how it's structured, the rules
that keep it safe to depend on, and how to extend it.

## What this is

BB-Lib is an Android library of common utilities for INSPIRE FTC team 24180. It is not
an app and does not run on its own — it's compiled into a `TeamCode` module alongside
[FTCRobotController](https://github.com/FIRST-Tech-Challenge/FTCRobotController).
Distributed via JitPack so any team can add one dependency line (see [README.md](README.md)).

BB-Lib is the actively-consumed artifact: BioBuzz and State-Bot both depend on
`com.github.FTC-24180:BB-Lib:<tag>`. A sibling project (`BBLib`, no hyphen) was a
from-scratch rewrite used to validate the fixes described below before applying them
here; treat this repo, not that one, as the source of truth going forward.

## Architecture

```
BB-Lib/
├── build.gradle              root build script (AGP version, group = com.github.FTC-24180)
├── settings.gradle            includes ':BB-Lib'
├── jitpack.yml                 pins the JDK JitPack builds with
└── BB-Lib/                    the only published module
    ├── build.gradle
    └── src/
        ├── main/java/org/bluebananas/lib/BBLib/
        │   ├── Controllers/
        │   │   ├── Feedback/      PID.java, PVA.java - pure-Java feedback control
        │   │   └── Feedforward/   VAS.java - pure-Java feedforward control
        │   ├── Hardware/
        │   │   ├── Motors/        Motor.java, SimpleMotor.java, DualModeMotor.java -
        │   │   │                  FTC SDK-facing motor wrappers
        │   │   └── StandardServo.java - FTC SDK-facing servo wrapper
        │   └── util/               MathUtil.java - pure-Java, no-Android-dependency helpers
        └── test/java/...          JVM unit tests, mirrors main's package layout
```

Single-module on purpose: there's one artifact (`BB-Lib`) and one audience (TeamCode
authors). Don't split into multiple published modules until there's a concrete reason.

Package casing note: `Controllers`/`Hardware` use PascalCase per this repo's own naming
conventions (see `CONTRIBUTING.md`), but `util` is lowercase - it was carried over
as-is from BBLib rather than renamed to match. This repo currently has both casing
styles; don't "fix" the mismatch as a drive-by change, and match whichever convention
the specific package you're touching already uses.

### The FTC SDK dependency: `compileOnly`, always

`BB-Lib/build.gradle` depends on FTC SDK artifacts (`org.firstinspires.ftc:RobotCore` —
a real, Maven Central/Google-hosted coordinate published by FIRST) as **`compileOnly`**,
never `implementation`. This is the single most important rule in this codebase and
every new hardware-facing class must follow it.

**Why:** a consuming team's `TeamCode/build.gradle` already declares the FTC SDK
modules directly (`implementation 'org.firstinspires.ftc:RobotCore:11.1.0'`, `Hardware`,
`Vision`, ...) at whatever version that team is running this season — TeamCode and
FtcRobotController are sibling app modules that each depend on these coordinates
directly, not via `project(':FtcRobotController')`. This repo's own history is the
incident that established the rule: every FTC SDK module (`RobotCore`, `Blocks`,
`Inspection`, `OnBotJava`, `Hardware`, `FtcCommon`, `Vision`) was previously declared
`implementation`, alongside unrelated AndroidX UI libraries (`appcompat`, `material`)
that no class in this library even used. Every consumer ended up with **two
declarations of the same FTC SDK coordinate** in their build: theirs, and BB-Lib's
transitive one. Gradle doesn't error on that — it silently resolves same-coordinate
conflicts to one version (normally the higher one) across the whole build. That's not
a hard failure, but it's a silent one: the version that wins might not be the one
either side was actually built and tested against, and the team loses the ability to
control their own SDK version. This is exactly what caused the Robot Controller app to
crash-loop before reaching any op mode code when this library was consumed in
production — from the Control Hub's perspective, the hub never finished booting, and
the only trace was in the system log, not the team's own debug output. That symptom
reads nothing like a dependency-version problem, which is what led to it originally
being misdiagnosed as a minSdk/Java-version issue. The commit that switched every FTC
SDK dependency to `compileOnly` and dropped the unused modules/AndroidX libs entirely
fixed this.

`compileOnly` gives BB-Lib's own code the types it needs to compile against
(`HardwareMap`, `DcMotor`, `Servo`, ...) without shipping a second copy of them. At
runtime, the classes come from the host app's own `FtcRobotController` module, which is
exactly what you want.

**Checklist when adding a class that touches FTC SDK types:**
1. Add the specific FTC SDK artifact as `compileOnly` (and `testCompileOnly` if tests
   reference the type) in `BB-Lib/build.gradle` — only add the module you actually need
   (currently only `RobotCore` is used), not the whole set speculatively.
2. Never add it as `implementation` or `api`.
3. Pin the FTC SDK artifact version (`fTCRobotControllerVersion` in
   `gradle/libs.versions.toml`) to whatever the current season's SDK uses (check
   `FtcRobotController/build.dependencies.gradle` in the upstream repo, or a real
   TeamCode project like BioBuzz) purely so this repo compiles — it has zero effect on
   what a consumer actually runs against.

### Alternative considered: vendoring FtcRobotController as a submodule

An earlier draft vendored the full `FtcRobotController` source as a git submodule and
depended on it via `compileOnly project(':FtcRobotController')`. That works, but it's
unnecessary weight: FIRST already publishes the individual SDK modules (`RobotCore`,
`Hardware`, `Vision`, etc.) to Maven Central/Google under `org.firstinspires.ftc:*`, so
a plain `compileOnly` Maven coordinate gets the same compile-time types without cloning
~100MB of upstream source into every checkout of this repo. Only reach for the
submodule/vendoring approach if FIRST ever stops publishing those artifacts, or if you
need upstream source for local debugging.

## Public API design

- **Split by dependency, not just by feature.** `Controllers/` and `util/` must stay
  pure Java with zero FTC SDK or Android imports — that's what makes them fast to unit
  test on the JVM with no emulator/device. `Hardware/` is where FTC SDK types are
  allowed; keep those classes thin (adapt/guard the SDK, don't reimplement it).
- **Prefer returning null/defaults over throwing** for hardware lookups. `HardwareMap
  .get()` throwing on a missing/misconfigured device takes down the whole op mode's
  init; a library helper should let the caller decide how to degrade. No helper for
  this exists yet in `Hardware/` - add one before any class calls `hardwareMap.get()`
  directly.
- **No static/global mutable state.** Every utility should be instantiable per-op-mode
  (e.g. a `PID` instance per motor) so nothing leaks state across op mode runs or
  between subsystems.
- **Javadoc every public class and method** with the *why*, not a restatement of the
  signature — this is the only documentation most consuming teams will ever read.
  Existing `Controllers/`/`Hardware/` classes are inconsistent about this; new code
  should not add to that gap.
- **Package by capability** (`Controllers`, `Hardware`, `util`, and new top-level
  packages as new capability areas appear), not by FTC season or team.

## Build configuration

- `compileSdk 36`, `minSdk 24`, `targetSdk 28` — `minSdk`/`targetSdk` match what
  `FtcRobotController` itself declares (Control/Driver Hub firmware is Android 7–9).
  `compileSdk` is newer only so the toolchain has a currently-supported platform to
  compile against; it does not change what devices the library runs on.
- `sourceCompatibility` / `targetCompatibility` = `JavaVersion.VERSION_1_8`, matching
  `FtcRobotController`'s own setting — TeamCode and this library must target the same
  bytecode level or teams hit compile errors mixing modules.
- AGP itself requires JDK 17 to *run* the build (see `jitpack.yml`); that's separate
  from the Java 8 bytecode level the library's own classes compile to.

## Testing strategy

- **`Controllers/` and `util/`**: full JVM unit tests (`src/test`), no mocks needed —
  see `MathUtilTest`. This is where most test coverage should live. **Gap:** `PID.java`,
  `PVA.java`, and `VAS.java` have no unit tests yet even though they're pure Java and
  could be tested exactly like `MathUtilTest` - add them opportunistically rather than
  all at once.
- **`Hardware/`**: `compileOnly` means the FTC SDK classes exist for compilation but
  have no real implementation (no Robot/Driver Hub, no real `HardwareMap`) at test
  time. Use `testCompileOnly` + Mockito to mock the SDK interfaces for what can be
  tested this way; anything that fundamentally needs real hardware behavior (timing,
  actual motor response) is validated manually by dogfooding — depend on a locally
  published snapshot (`./gradlew publishToMavenLocal`, then `mavenLocal()` +
  `implementation "com.github.FTC-24180:BB-Lib:local"` in a real TeamCode project) and
  run it on an actual robot before tagging a release.
- Run `./gradlew :BB-Lib:test` before every release tag.

## Release / distribution (JitPack)

- **Why JitPack** over Maven Central or GitHub Packages: zero account/credential setup
  (push a tag, JitPack builds on demand), which matters for a student-maintained repo
  with no dedicated release infra. Maven Central needs a Sonatype OSSRH account + GPG
  signing (more setup, faster/more "official" once done); GitHub Packages requires
  consumers to authenticate even for public read, which is a bad fit for teams who just
  want to add a dependency line. Revisit this if the library ever needs guaranteed
  uptime independent of JitPack or a more "official" presence.
- **Maven groupId vs Java package**: the Maven `groupId` is `com.github.FTC-24180`
  (JitPack's required convention: `com.github.<GitHub owner>`) — unrelated to the Java
  package name `org.bluebananas.lib.BBLib` used in source. Don't try to make these match.
- **Versioning**: releases are Git tags (`MAJOR.MINOR.PATCH`, SemVer). **This repo uses
  no `v` prefix** (`0.2.0`, not `v0.2.0`) — existing tags are `0.1.0`, `0.1.1`, `0.1.2`,
  `0.1.3`, `0.2.0`. Stay consistent with that convention. JitPack reads the version from
  a `VERSION` env var it sets during the build — `BB-Lib/build.gradle` reads that
  (`System.getenv('VERSION')`), not `git describe`, which is a JitPack-Android specific
  requirement easy to miss.
- **Publication coordinates are pinned explicitly** in `BB-Lib/build.gradle`'s
  `afterEvaluate` block (`artifactId = 'BB-Lib'`) rather than left to JitPack's
  defaults, so the existing `com.github.FTC-24180:BB-Lib:<tag>` coordinate that BioBuzz
  and State-Bot already reference keeps resolving after any future refactor.
- **First resolve after a new tag is slow** (JitPack builds cold); this is expected,
  not a break.

## Project guidelines

- Adding a utility: decide `util`/`Controllers` (pure Java) vs `Hardware` (FTC SDK)
  first — it determines the dependency and test approach above. Add a unit test in the
  mirrored `src/test` package before opening a PR.
- Don't add a new compileOnly FTC SDK module until a class actually needs a type from
  it.
- Bump the version by tagging, not by editing a version file — there isn't one anymore;
  JitPack derives it from the tag. `CONTRIBUTING.md` documents the same branch/tagging
  process.
- Keep `BB-Lib` as the only published module unless there's a concrete reason to split.
