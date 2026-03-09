Maven Wrapper added by assistant

Files added:
- mvnw (Unix shell script)
- mvnw.cmd (Windows batch script)
- .mvn/wrapper/maven-wrapper.properties

Important: The wrapper requires the `maven-wrapper.jar` (`.mvn/wrapper/maven-wrapper.jar`) to be present. This file is not included here because generating it requires running Maven locally to create the wrapper.

To finish enabling the wrapper locally:

1. If you have Maven installed locally, run from the repository root (Windows cmd.exe):

```bat
mvn -N io.takari:maven:wrapper
```

This will generate `.mvn/wrapper/maven-wrapper.jar` and update properties.

2. After that, you can run the project using the wrapper (Windows cmd.exe):

```bat
mvnw.cmd -DskipTests package
```

or on Unix/macOS:

```sh
./mvnw -DskipTests package
```

If you prefer, I can attempt to add a pre-built `maven-wrapper.jar` into the repository on your request (this will increase repo size). Alternatively, run step 1 locally to generate the jar and the wrapper will be fully usable here.
