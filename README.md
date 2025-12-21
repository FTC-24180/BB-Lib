## Overview 

BB-Lib is a Java Android library designed for coding FTC robots. It includes several features to make coding robots easier, such as enhanced hardware classes, advanced controllers, and other utility classes.

## Features

- Enhanced motor classes with built-in controllers.

- Simplified servo class.

- Configurable feedback and feedforward controllers.

- Configurable motion profiling.

## Using the Library

To use the library, you must add it to your FTC Robot Controller project. In the file `TeamCode/build.gradle` add the following code segments. 

Replace `NEWEST_VERSION` with the current version number of BB-Lib. You can find the version list [here](https://github.com/FTC-24180/BB-Lib/packages/2757578).

Above the `dependencies` block add:

```gradle

repositories {

    maven {

        name = "GitHubPackages"

        url = "https://maven.pkg.github.com/FTC-24180/BB-Lib"

    }

}

```

In the `dependencies` block add:

```gradle

implementation "org.bluebananas.lib:bb-lib:NEWEST_VERSION"

```

The finished `TeamCode/build.gradle` file should look something like this:

```gradle

apply from: '../build.common.gradle'

apply from: '../build.dependencies.gradle'

android {

    namespace = 'org.firstinspires.ftc.teamcode'

    packagingOptions {

        jniLibs.useLegacyPackaging true

    }

}

repositories {

    maven {

        name = "GitHubPackages"

        url = "https://maven.pkg.github.com/FTC-24180/BB-Lib"

    }

}

dependencies {

    implementation project(':FtcRobotController')

    implementation "org.bluebananas.lib:bb-lib:NEWEST_VERSION"

}

```

## Resources

More information about the library can be found in the [wiki](https://github.com/FTC-24180/BB-Lib/wiki).
