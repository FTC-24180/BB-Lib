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

        url = "https://jitpack.io"

    }

}

```

In the `dependencies` block add:

```gradle

implementation "com.github.FTC-24180:BB-Lib:NEWEST_VERSION"

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

        url = "https://jitpack.io"

    }

}

dependencies {

    implementation project(':FtcRobotController')

    implementation "com.github.FTC-24180:BB-Lib:NEWEST_VERSION"

}

```

## Resources

More information about the library can be found in the [wiki](https://github.com/FTC-24180/BB-Lib/wiki).
