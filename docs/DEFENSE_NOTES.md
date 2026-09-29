# Defense notes

## Explain the idea

**What problem does Builder solve here?**  
A computer has many optional parts. A single constructor call with eleven
arguments is hard to read. Builder gives names to the optional choices.

**Why is it better than the original constructor?**  
I can see what each choice means. I do not need to pass every default value.
Validation also has a clear final step. The four required arguments are still
positional, so I must still pay attention to their order.

**What is the Product?**  
ComputerConfiguration: the finished computer selection, with eleven properties.

**What is the Builder?**  
ComputerConfiguration.Builder: a temporary object that collects choices.
It is a static nested class, so I can create it without an existing Product.

**What does build() do?**  
It calls four validation helpers and then the private Product constructor.
If a rule fails, it throws IllegalArgumentException and returns no Product.
Otherwise, the constructor copies the Builder values into final fields.

**Why do option methods return this?**  
`this` is the current Builder. Returning it lets me immediately call another
method on the same object.

**What is method chaining?**  
Calling methods one after another:
`builder.withPowerSupply(750).enableActiveCooling().build()`.

**What is a fluent API?**  
An API that reads like a sequence of choices. Here the names describe computer
options. Fluent methods return Builder; build returns ComputerConfiguration.

## Data and rules

**Which fields are required?**  
processor, ramGb, storageGb, operatingSystem. They enter the Builder constructor.

**Which fields are optional?**  
graphicsCard, powerSupplyWatts, wifiEnabled, bluetoothEnabled, activeCooling,
rgbLighting, monitor.

**Where are defaults stored?**  
In the Builder fields. PSU starts at 500. Java initializes boolean fields to
false and reference fields to null. A null graphicsCard means integrated
graphics; a null monitor means no selected monitor.

**Where does validation happen, and why there?**  
Product rules run in build(), when all choices are available. This lets the
caller add parts in any order. Monitor validates its own values in its
constructor because they do not depend on other components.

**What are single-field validations?**  
Checks of one value alone: processor not null or blank, RAM and storage above
zero, OS not null, PSU at least 300, and a selected GPU model not blank.
Monitor also requires a nonblank model and positive refresh rate and size.

**What are cross-field validations?**  
Rules that depend on a combination of fields. A dedicated GPU needs enough
power and cooling. A monitor above 120 Hz needs a dedicated GPU in this model.

**Explain the individual GPU constraint.**  
Every non-null, nonblank graphicsCard string means a dedicated GPU. It needs
at least 650W AND active cooling. With 649W it fails even with cooling. At 650W
without cooling it also fails. At 650W with cooling it succeeds.
Use null, not the text "Integrated", to select integrated graphics.

**Is the monitor rule a universal hardware fact?**  
No. It is a simple extra compatibility rule for this assignment. It demonstrates
how the Builder checks values from the nested Monitor object.

## Classes, reuse, and UML

**What is the Director, and why use it?**  
ComputerConfigurationDirector stores OFFICE, GAMING, and WORKSTATION recipes.
Clients can request a preset without copying its construction steps. Each method
creates a fresh Builder, so calls do not share mutable Builder state.

**Why is Product immutable?**  
Setters could break its rules after validation. Final fields and no setters
preserve a valid result. A Product also never keeps a reference to its Builder.

**How does Builder reuse work?**  
Build A, change options on the same Builder, then build B. Each build creates
a new Product. A keeps its original values. Monitor can be shared safely because
it is immutable. Required Builder fields are final; use a new Builder to change
processor, RAM, storage, or OS.

**Explain each important class.**

| Class | Simple explanation |
| --- | --- |
| Main | Client; creates one manual computer and three presets, prints only GAMING |
| ComputerConfiguration | Immutable finished Product with getters and toString |
| ComputerConfiguration.Builder | Collects choices, checks rules, constructs Product |
| ComputerConfigurationDirector | Three reusable recipes |
| Monitor | Immutable model, refresh rate, and size |
| OperatingSystem | WINDOWS and LINUX choices |
| before.ConstructorComputerConfiguration | Preserved old design with eleven constructor arguments |
| ComputerConfigurationTest | Product, Builder, preset, dependency, and boundary tests |
| MonitorTest | Value storage and invalid Monitor input tests |

**Explain the UML arrows.**  
Dashed arrows show use: Main uses Builder and Director; Director uses Builder;
Builder creates the Product. Solid arrows show stored references to Monitor
and OperatingSystem. A Product has zero or one Monitor. Several Products can
share that immutable Monitor, so I used an association instead of exclusive
composition. Builder is nested in Product; the diagram labels that explicitly.
The old constructor class is not part of the final pattern diagram.

## Tests and Clean Code

**Explain the tests.**  
There are 25 JUnit tests. They check valid defaults and presets, invalid inputs,
300/299W and 650/649W boundaries, RAM/storage minimums, 120/121 Hz monitors,
both GPU requirements, fluent methods, and Builder reuse. Monitor has its own
four tests. assertEquals compares values, assertThrows checks rejection, and
assertNotSame confirms two different Products. The report lists every test.

**Explain the three refactorings.**

1. Old positional booleans became named methods such as enableWiFi. This removes
   unclear flag arguments and makes each option readable.
2. Checks inside the old constructor became small named validation helpers.
   build reads at one level of abstraction, and error messages are more specific.
3. Eleven constructor arguments became four required Builder arguments plus
   optional calls. Defaults have one home and callers need fewer arguments.

**Do you follow Command-Query Separation everywhere?**  
No. Fluent option methods change Builder state and return the Builder for
convenient chaining. Getters only read state. I can explain this tradeoff instead
of claiming strict separation.

## Short demonstration

1. Open the historical constructor and point to the boolean arguments.
2. Open Main and compare the named option calls.
3. Open Builder.build and show validation before construction.
4. Show private final Product fields and immutable Monitor.
5. Run Main: only one GAMING line appears.
6. Run the JUnit tests and explain the two separate GPU rejection tests.
7. Open builderReuseDoesNotChangeFirstProduct and explain A versus B.
8. Show the Director and UML. Show the development commits with git log.

The commands for this computer are in README.md.

## Likely live changes

### A. Add a new optional property

Example: a case color.

1. In ComputerConfiguration.java add `private final String caseColor;` to Product.
2. Add a mutable `caseColor` Builder field with a default such as "Black".
3. Add `withCaseColor(String caseColor)`, assign it, and return this.
4. Copy it in the private Product constructor and add a getter.
5. Add validation in validateOptionalFields if blank colors should be rejected.
6. Update toString if the new choice should appear in the output.
7. Add tests for the default, custom value, and independence after Builder reuse.
8. Update UML, report, and any affected sample output. Old callers still work.

### B. Add a new required property

Example: a motherboard model.

1. Add final fields to Product and Builder in ComputerConfiguration.java.
2. Add the argument to the Builder constructor and assign it.
3. Copy it to Product, add a getter, and check it in validateRequiredFields.
4. Update every Builder creation in Main, Director, and tests. Use the compiler
   or search for `new ComputerConfiguration.Builder` to find them.
5. Add a valid-value test and invalid-input test. Update UML and documentation.
6. Leave the preserved historical class as BEFORE evidence unless specifically
   asked to change the historical model too.

### C. Add a new validation rule

Example: RAM must not exceed 256 GB.

1. Add a clear check in Builder.validateRequiredFields.
2. Throw IllegalArgumentException with a message describing the maximum.
3. In ComputerConfigurationTest, check that 256 succeeds and 257 fails.
4. Check existing presets still satisfy the rule. Update the report's rule table.

### D. Add a new cross-field validation

Example: RGB lighting also requires active cooling as a new assignment rule.

1. Add a small named helper such as validateLightingCompatibility in Builder.
2. Check `rgbLighting && !activeCooling` and throw a clear exception.
3. Call the helper in build before new ComputerConfiguration.
4. Add tests that isolate the new dependency: RGB without cooling fails, RGB
   with cooling succeeds, and no RGB still works without cooling.
5. Update the UML method list and report. Keep the check out of
   enableRgbLighting so order of option calls remains flexible.

### E. Change a default value

Example: increase the default PSU from 500W to 550W.

1. Change only the Builder powerSupplyWatts initializer in ComputerConfiguration.
2. Update default-value expectations in minimalComputerUsesDefaults and the
   old Product value in builderReuseDoesNotChangeFirstProduct.
3. OFFICE uses the default, so update its documented PSU. GAMING and WORKSTATION
   explicitly select power and keep their selected values.
4. Run tests and update the report default table.

### F. Add a new preset

1. Add a descriptive method, such as createStudyComputer, to Director.
2. Use a new Builder with required arguments, optional choices, and build.
3. Add a JUnit test that checks the preset's distinguishing values.
4. Add a Main call only if the demonstration should include it. Keep exactly
   one printed configuration unless the output requirement changes.
5. Add the method to UML and document its values.

### G. Modify Builder reuse behavior

Current behavior: reuse is allowed and every build creates a fresh Product.
If asked to make Builder single-use:

1. Add a private boolean built flag in Builder.
2. At the start of build, throw IllegalStateException if built is true.
3. Validate and create the Product, then set built to true only after success.
   This lets a failed build still be corrected.
4. Replace the reuse-success test with a second-build rejection test, and keep
   a test proving changes to Builder fields cannot change the first Product.
5. Update the report and defense explanation to describe the new policy.
6. Do not make this change in the submitted version: the current assignment
   specifically requires successful Builder reuse and Product independence.

If instead asked to reset optional choices after a successful build, first store
the new Product in a local variable, reset only Builder options to their defaults,
then return that Product. Test that A retains its choices and the next build
uses defaults. Never modify a previously returned Product.

### H. Add a corresponding JUnit test

1. Choose ComputerConfigurationTest for Product rules or MonitorTest for Monitor.
2. Add a descriptive method with `@Test`.
3. Arrange a Builder with the relevant inputs.
4. For valid data, call build and assert exact values through getters.
5. For invalid data, use assertThrows with build inside the lambda; check the
   message when distinguishing a particular rule matters.
6. For a boundary, test both the allowed limit and the nearest rejected value.
7. Run `mvn test` and update the test table and count in the report.

Example for the proposed RAM maximum:

```java
@Test
void rejectsRamAboveMaximum() {
    assertThrows(IllegalArgumentException.class,
            () -> new ComputerConfiguration.Builder(
                    "CPU", 257, 256, OperatingSystem.LINUX).build());
}
```

This example is for a possible live change; the current implementation has
no 256 GB maximum.
