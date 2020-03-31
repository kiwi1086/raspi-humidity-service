# raspi-humidity-service (rhs) project
[![CircleCI Build Status](https://circleci.com/gh/kiwi1086/raspi-humidity-service/tree/master.svg?style=shield)](https://circleci.com/gh/kiwi1086/raspi-humidity-service/tree/master)

Figure

Motivation


![Raspberry Pi GPIO](documentation/raspberry-pi-pinout.png "Raspberry Pi GPIO")
Source: https://pinout.xyz/

## Information
This project uses Quarkus, the Supersonic Subatomic Java Framework, if you want to know more please visit its website: https://quarkus.io/ .

## Running the application in dev mode
You can run the application in dev mode that enables live coding using:
```
./gradlew quarkusDev
```

## Packaging and running the application

The application can be packaged using `./gradlew quarkusBuild`.
It produces the `garage-door-service-0.1.0-SNAPSHOT-runner.jar` file in the `build` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `build/lib` directory.

The application is now runnable using `java -jar build/garage-door-service-0.1.0-SNAPSHOT-runner.jar`.

If you want to build an _über-jar_, just add the `--uber-jar` option to the command line:
```
./gradlew quarkusBuild --uber-jar
```

## Creating a native executable

You can create a native executable using: `./gradlew buildNative`.

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: `./gradlew buildNative --docker-build=true`.

You can then execute your native executable with: `./build/garage-door-service-0.1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult https://quarkus.io/guides/gradle-tooling#building-a-native-executable.