package org.example;

/** Reusable recipes; each call creates a fresh builder and product. */
public final class ComputerConfigurationDirector {
    public ComputerConfiguration createOfficeComputer() {
        return new ComputerConfiguration.Builder("Intel Core i3", 8, 256, OperatingSystem.WINDOWS)
                .enableWiFi()
                .withMonitor(new Monitor("Dell Office", 60, 24))
                .build();
    }

    public ComputerConfiguration createGamingComputer() {
        return new ComputerConfiguration.Builder("Intel Core i7", 32, 1000, OperatingSystem.WINDOWS)
                .withGraphicsCard("RTX 4070")
                .withPowerSupply(750)
                .enableWiFi()
                .enableBluetooth()
                .enableActiveCooling()
                .enableRgbLighting()
                .withMonitor(new Monitor("Samsung Gaming", 144, 27))
                .build();
    }

    public ComputerConfiguration createWorkstationComputer() {
        return new ComputerConfiguration.Builder("AMD Ryzen 9", 64, 2000, OperatingSystem.LINUX)
                .withGraphicsCard("RTX A4000")
                .withPowerSupply(850)
                .enableActiveCooling()
                .withMonitor(new Monitor("Dell Professional", 60, 32))
                .build();
    }
}
