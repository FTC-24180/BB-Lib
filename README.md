# BB-Lib

## Overview

BB-Lib is a Java Android library designed for coding FTC robots. It includes several features to make coding robots easier, such as enhanced hardware classes, advanced controllers, and other utility classes.

## Features

- Enhanced motor classes with built-in controllers.
- Simplified servo class.
- Configurable feedback and feedforward controllers.
- Configurable motion profiling.

## Using the Library

In your `TeamCode/build.gradle`, add the JitPack repository:

```gradle
repositories {
    maven { url = "https://jitpack.io" }
}
```

Then add the dependency, replacing `TAG` with a released version (a Git tag on this
repo, e.g. `0.2.0`), alongside the FTC SDK modules your `TeamCode/build.gradle` already
lists:

```gradle
dependencies {
    implementation 'org.firstinspires.ftc:Inspection:11.1.0'
    implementation 'org.firstinspires.ftc:Blocks:11.1.0'
    implementation 'org.firstinspires.ftc:RobotCore:11.1.0'
    implementation 'org.firstinspires.ftc:RobotServer:11.1.0'
    implementation 'org.firstinspires.ftc:OnBotJava:11.1.0'
    implementation 'org.firstinspires.ftc:Hardware:11.1.0'
    implementation 'org.firstinspires.ftc:FtcCommon:11.1.0'
    implementation 'org.firstinspires.ftc:Vision:11.1.0'
    implementation 'androidx.appcompat:appcompat:1.2.0'

    implementation "com.github.FTC-24180:BB-Lib:TAG"
}
```

**That existing FTC SDK dependency list is required, not optional.** BB-Lib references
FTC SDK types like `DcMotor`, `Servo`, and `OpMode` that it does *not* bundle - they're
`compileOnly`, so BB-Lib's published artifact has no dependency metadata pointing to
them at all. Without those `org.firstinspires.ftc:*` lines already present, those
classes crash with `NoClassDefFoundError` at runtime; nothing warns you at build time.
Every real TeamCode project already has this list, so this is only a risk if you're
integrating BB-Lib into something unusual.

The first build after a new tag is pushed will be slower while JitPack builds it fresh;
subsequent builds are cached.

## Releasing a new version

Push a Git tag (e.g. `git tag 0.2.0 && git push origin 0.2.0`) and JitPack builds it the
first time someone requests that version. There is no separate publish step to run by hand.

## Resources

More information about the library can be found in the [wiki](https://github.com/FTC-24180/BB-Lib/wiki).
