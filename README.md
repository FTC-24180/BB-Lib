# BB-Lib

## Overview

BB-Lib is a Java Android library designed for coding FTC robots. It includes several features to make coding robots easier, such as enhanced telemetry, advanced controllers, and other utility classes.

## Features

- Enhanced telemetry with filterering and sorting.
- Configurable feedback controllers.

## Using the Library

In your `TeamCode/build.gradle`, add the JitPack repository:

```gradle
repositories {
    maven { url = "https://jitpack.io" }
}
```

Then add the BB-Lib dependency, replacing `TAG` with a released version (a Git tag on this
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

## Resources

More information about the library can be found in the [wiki](https://github.com/FTC-24180/BB-Lib/wiki).

See [CONTRIBUTING.md](CONTRIBUTING.md) for contributing information.

## License

Avalible under MIT license, see [LICENSE](LICENSE) for more information.
