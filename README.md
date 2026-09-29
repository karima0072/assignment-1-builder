# Assignment 1 - Builder Pattern: Design Under Changing Requirements

A small Java project for Software Design Patterns. It builds immutable computer
configurations with readable optional choices and validation at build time.

**Individual variant:** Computer Configuration. A dedicated GPU requires a power
supply of at least 650W and active cooling. Presets: OFFICE, GAMING, WORKSTATION.

**Technologies:** Java 17+, Maven, JUnit 5, PlantUML. No application frameworks.

## Structure

```text
src/main/java/org/example/
  ComputerConfiguration.java         Product and nested Builder
  ComputerConfigurationDirector.java Three preset recipes
  Main.java                          Client (prints only GAMING)
  Monitor.java                       Immutable value object
  OperatingSystem.java               Enum
  before/ConstructorComputerConfiguration.java
src/test/java/org/example/            25 JUnit tests
docs/builder-uml.puml                 Editable UML source
docs/builder-uml.png                  Rendered UML
docs/DEFENSE_NOTES.md                 Explanation and live-change guide
.gitignore                           Build and IDE exclusions
pom.xml
report.md
```

## Run

With JDK 17+ and Maven on PATH, from the project root:

```sh
mvn -q compile
java -cp target/classes org.example.Main
```

Run tests:

```sh
mvn test
```

On the current Windows computer, Java and Maven are installed but not on PATH.
These PowerShell commands use the installed tools:

```powershell
$env:JAVA_HOME='C:\Users\omen\.jdks\temurin-17.0.17'
& 'C:\Program Files\JetBrains\IntelliJ IDEA 2025.3\plugins\maven\lib\maven3\bin\mvn.cmd' -q compile
& "$env:JAVA_HOME\bin\java.exe" -cp target/classes org.example.Main
# Tests:
& 'C:\Program Files\JetBrains\IntelliJ IDEA 2025.3\plugins\maven\lib\maven3\bin\mvn.cmd' test
```

The Builder constructor takes processor, RAM, storage, and OS. Fluent methods
select optional parts. Each returns the same Builder. `build()` validates the
combined choices and copies them into a new immutable Product. The Director
stores reusable recipes. The preserved `before` class shows the old constructor.

See [report.md](report.md) and [defense notes](docs/DEFENSE_NOTES.md).

## Connect to GitHub

Create an empty GitHub repository, then replace the placeholder below:

```sh
git remote add origin <YOUR_REPOSITORY_URL>
git push -u origin master
```

Add the real URL to the report. Existing local IDE files are not part of the
assignment commits.
