package org.example.before;

import org.example.Monitor;
import org.example.OperatingSystem;

/** Historical constructor design for comparison; not used by the final client. */
public final class ConstructorComputerConfiguration {
    private final String processor;
    private final int ramGb;
    private final int storageGb;
    private final OperatingSystem operatingSystem;
    private final String graphicsCard;
    private final int powerSupplyWatts;
    private final boolean wifiEnabled;
    private final boolean bluetoothEnabled;
    private final boolean activeCooling;
    private final boolean rgbLighting;
    private final Monitor monitor;

    public ConstructorComputerConfiguration(
            String processor, int ramGb, int storageGb, OperatingSystem operatingSystem,
            String graphicsCard, int powerSupplyWatts, boolean wifiEnabled,
            boolean bluetoothEnabled, boolean activeCooling, boolean rgbLighting, Monitor monitor) {
        if (processor == null || processor.isBlank()) {
            throw new IllegalArgumentException("Processor must not be null or blank");
        }
        if (ramGb <= 0 || storageGb <= 0) {
            throw new IllegalArgumentException("RAM and storage must be greater than 0");
        }
        if (operatingSystem == null || powerSupplyWatts < 300) {
            throw new IllegalArgumentException("Operating system is required and PSU must be at least 300W");
        }
        if (graphicsCard != null && graphicsCard.isBlank()) {
            throw new IllegalArgumentException("Graphics card must not be blank");
        }
        boolean dedicated = graphicsCard != null;
        if (dedicated && powerSupplyWatts < 650) {
            throw new IllegalArgumentException("Dedicated graphics card requires at least 650W");
        }
        if (dedicated && !activeCooling) {
            throw new IllegalArgumentException("Dedicated graphics card requires active cooling");
        }
        if (monitor != null && monitor.getRefreshRateHz() > 120 && !dedicated) {
            throw new IllegalArgumentException("Monitor above 120 Hz requires a dedicated graphics card");
        }
        this.processor = processor;
        this.ramGb = ramGb;
        this.storageGb = storageGb;
        this.operatingSystem = operatingSystem;
        this.graphicsCard = graphicsCard;
        this.powerSupplyWatts = powerSupplyWatts;
        this.wifiEnabled = wifiEnabled;
        this.bluetoothEnabled = bluetoothEnabled;
        this.activeCooling = activeCooling;
        this.rgbLighting = rgbLighting;
        this.monitor = monitor;
    }

    public static ConstructorComputerConfiguration example() {
        return new ConstructorComputerConfiguration(
                "Intel Core i7", 32, 1000, OperatingSystem.WINDOWS,
                "RTX 4070", 750, true, true, true, false,
                new Monitor("Samsung", 144, 27));
    }
}

