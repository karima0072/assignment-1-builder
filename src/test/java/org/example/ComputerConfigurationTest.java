package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ComputerConfigurationTest {
    private ComputerConfiguration.Builder minimalBuilder() {
        return new ComputerConfiguration.Builder("Intel Core i3", 8, 256, OperatingSystem.WINDOWS);
    }

    @Test
    void minimalComputerUsesDefaults() {
        ComputerConfiguration computer = minimalBuilder().build();
        assertEquals("Intel Core i3", computer.getProcessor());
        assertEquals(8, computer.getRamGb());
        assertEquals(256, computer.getStorageGb());
        assertEquals(OperatingSystem.WINDOWS, computer.getOperatingSystem());
        assertEquals(500, computer.getPowerSupplyWatts());
        assertNull(computer.getGraphicsCard());
        assertNull(computer.getMonitor());
        assertFalse(computer.isWifiEnabled());
        assertFalse(computer.isBluetoothEnabled());
        assertFalse(computer.isActiveCooling());
        assertFalse(computer.isRgbLighting());
    }

    @Test
    void gamingPresetHasGamingComponents() {
        ComputerConfiguration computer = new ComputerConfigurationDirector().createGamingComputer();
        assertEquals("RTX 4070", computer.getGraphicsCard());
        assertEquals(32, computer.getRamGb());
        assertEquals(750, computer.getPowerSupplyWatts());
        assertTrue(computer.isActiveCooling());
        assertTrue(computer.isWifiEnabled());
        assertTrue(computer.isBluetoothEnabled());
        assertTrue(computer.isRgbLighting());
        assertEquals(144, computer.getMonitor().getRefreshRateHz());
    }

    @Test
    void workstationPresetHasLargeMemoryAndLinux() {
        ComputerConfiguration computer = new ComputerConfigurationDirector().createWorkstationComputer();
        assertEquals(64, computer.getRamGb());
        assertEquals(2000, computer.getStorageGb());
        assertEquals(OperatingSystem.LINUX, computer.getOperatingSystem());
        assertEquals("RTX A4000", computer.getGraphicsCard());
        assertTrue(computer.isActiveCooling());
        assertFalse(computer.isRgbLighting());
    }

    @Test
    void officePresetUsesIntegratedGraphics() {
        ComputerConfiguration computer = new ComputerConfigurationDirector().createOfficeComputer();
        assertNull(computer.getGraphicsCard());
        assertEquals(8, computer.getRamGb());
        assertEquals(60, computer.getMonitor().getRefreshRateHz());
        assertTrue(computer.isWifiEnabled());
    }

    @Test
    void rejectsBlankProcessor() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> new ComputerConfiguration.Builder(" ", 8, 256, OperatingSystem.LINUX).build());
        assertEquals("Processor must not be null or blank", error.getMessage());
    }

    @Test
    void rejectsNullProcessor() {
        assertThrows(IllegalArgumentException.class,
                () -> new ComputerConfiguration.Builder(null, 8, 256, OperatingSystem.LINUX).build());
    }

    @Test
    void rejectsZeroRam() {
        assertThrows(IllegalArgumentException.class,
                () -> new ComputerConfiguration.Builder("CPU", 0, 256, OperatingSystem.LINUX).build());
    }

    @Test
    void rejectsZeroStorage() {
        assertThrows(IllegalArgumentException.class,
                () -> new ComputerConfiguration.Builder("CPU", 8, 0, OperatingSystem.LINUX).build());
    }

    @Test
    void rejectsMissingOperatingSystem() {
        assertThrows(IllegalArgumentException.class,
                () -> new ComputerConfiguration.Builder("CPU", 8, 256, null).build());
    }

    @Test
    void rejectsBlankGraphicsCard() {
        assertThrows(IllegalArgumentException.class, () -> minimalBuilder().withGraphicsCard(" ").build());
    }

    @Test
    void acceptsMinimumPowerSupply() {
        assertEquals(300, minimalBuilder().withPowerSupply(300).build().getPowerSupplyWatts());
    }

    @Test
    void rejectsPowerSupplyBelowMinimum() {
        assertThrows(IllegalArgumentException.class, () -> minimalBuilder().withPowerSupply(299).build());
    }

    @Test
    void acceptsMinimumRamAndStorage() {
        ComputerConfiguration computer =
                new ComputerConfiguration.Builder("CPU", 1, 1, OperatingSystem.LINUX).build();
        assertEquals(1, computer.getRamGb());
        assertEquals(1, computer.getStorageGb());
    }

    @Test
    void dedicatedGpuRejects649WattsEvenWithCooling() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> minimalBuilder().withGraphicsCard("RTX 4070").withPowerSupply(649)
                        .enableActiveCooling().build());
        assertEquals("Dedicated graphics card requires at least 650W", error.getMessage());
    }

    @Test
    void dedicatedGpuRejectsMissingCoolingEvenWithEnoughPower() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> minimalBuilder().withGraphicsCard("RTX 4070").withPowerSupply(650).build());
        assertEquals("Dedicated graphics card requires active cooling", error.getMessage());
    }

    @Test
    void dedicatedGpuAccepts650WattsWithCooling() {
        ComputerConfiguration computer = minimalBuilder().withGraphicsCard("RTX 4070")
                .withPowerSupply(650).enableActiveCooling().build();
        assertEquals(650, computer.getPowerSupplyWatts());
        assertTrue(computer.isActiveCooling());
    }

    @Test
    void integratedGraphicsAccepts120HzMonitor() {
        assertEquals(120, minimalBuilder().withMonitor(new Monitor("Office", 120, 24))
                .build().getMonitor().getRefreshRateHz());
    }

    @Test
    void integratedGraphicsRejects121HzMonitor() {
        IllegalArgumentException error = assertThrows(IllegalArgumentException.class,
                () -> minimalBuilder().withMonitor(new Monitor("Gaming", 121, 27)).build());
        assertEquals("Monitor above 120 Hz requires a dedicated graphics card", error.getMessage());
    }

    @Test
    void builderReuseDoesNotChangeFirstProduct() {
        Monitor firstMonitor = new Monitor("Office", 60, 24);
        ComputerConfiguration.Builder builder = minimalBuilder().withMonitor(firstMonitor);
        ComputerConfiguration first = builder.build();
        ComputerConfiguration second = builder.withGraphicsCard("RTX 4070").withPowerSupply(750)
                .enableActiveCooling().enableWiFi().enableBluetooth().enableRgbLighting()
                .withMonitor(new Monitor("Gaming", 144, 27)).build();
        assertNotSame(first, second);
        assertNull(first.getGraphicsCard());
        assertEquals(500, first.getPowerSupplyWatts());
        assertFalse(first.isActiveCooling());
        assertFalse(first.isWifiEnabled());
        assertFalse(first.isBluetoothEnabled());
        assertFalse(first.isRgbLighting());
        assertSame(firstMonitor, first.getMonitor());
        assertEquals("RTX 4070", second.getGraphicsCard());
        assertEquals(750, second.getPowerSupplyWatts());
        assertEquals(144, second.getMonitor().getRefreshRateHz());
    }

    @Test
    void fluentMethodsReturnSameBuilder() {
        ComputerConfiguration.Builder builder = minimalBuilder();
        assertSame(builder, builder.withGraphicsCard("RTX 4070"));
        assertSame(builder, builder.withPowerSupply(750));
        assertSame(builder, builder.enableWiFi());
        assertSame(builder, builder.enableBluetooth());
        assertSame(builder, builder.enableActiveCooling());
        assertSame(builder, builder.enableRgbLighting());
        assertSame(builder, builder.withMonitor(new Monitor("Gaming", 144, 27)));
        assertNotNull(builder.build());
    }

    @Test
    void failedBuildCanBeCorrected() {
        ComputerConfiguration.Builder builder = minimalBuilder().withGraphicsCard("RTX 4070");
        assertThrows(IllegalArgumentException.class, builder::build);
        assertNotNull(builder.withPowerSupply(650).enableActiveCooling().build());
    }
}
