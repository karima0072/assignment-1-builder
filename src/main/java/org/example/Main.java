package org.example;

public class Main {
    public static void main(String[] args) {
        ComputerConfiguration custom = new ComputerConfiguration.Builder(
                "AMD Ryzen 5", 16, 512, OperatingSystem.LINUX)
                .enableWiFi()
                .enableBluetooth()
                .withMonitor(new Monitor("Dell", 75, 24))
                .build();

        ComputerConfigurationDirector director = new ComputerConfigurationDirector();
        ComputerConfiguration office = director.createOfficeComputer();
        ComputerConfiguration gaming = director.createGamingComputer();
        ComputerConfiguration workstation = director.createWorkstationComputer();

        // Construct all four examples, but print only the required GAMING preset.
        System.out.println(gaming);
    }
}
