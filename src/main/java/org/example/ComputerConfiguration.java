package org.example;

/** Immutable product. Use Builder to select optional components. */
public final class ComputerConfiguration {
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

    private ComputerConfiguration(Builder builder) {
        this.processor = builder.processor;
        this.ramGb = builder.ramGb;
        this.storageGb = builder.storageGb;
        this.operatingSystem = builder.operatingSystem;
        this.graphicsCard = builder.graphicsCard;
        this.powerSupplyWatts = builder.powerSupplyWatts;
        this.wifiEnabled = builder.wifiEnabled;
        this.bluetoothEnabled = builder.bluetoothEnabled;
        this.activeCooling = builder.activeCooling;
        this.rgbLighting = builder.rgbLighting;
        this.monitor = builder.monitor;
    }

    public String getProcessor() { return processor; }
    public int getRamGb() { return ramGb; }
    public int getStorageGb() { return storageGb; }
    public OperatingSystem getOperatingSystem() { return operatingSystem; }
    public String getGraphicsCard() { return graphicsCard; }
    public int getPowerSupplyWatts() { return powerSupplyWatts; }
    public boolean isWifiEnabled() { return wifiEnabled; }
    public boolean isBluetoothEnabled() { return bluetoothEnabled; }
    public boolean isActiveCooling() { return activeCooling; }
    public boolean isRgbLighting() { return rgbLighting; }
    public Monitor getMonitor() { return monitor; }

    @Override
    public String toString() {
        return "ComputerConfiguration{processor='" + processor + "', ramGb=" + ramGb
                + ", storageGb=" + storageGb + ", operatingSystem=" + operatingSystem
                + ", graphicsCard=" + (graphicsCard == null ? "Integrated" : graphicsCard)
                + ", powerSupplyWatts=" + powerSupplyWatts + ", wifiEnabled=" + wifiEnabled
                + ", bluetoothEnabled=" + bluetoothEnabled + ", activeCooling=" + activeCooling
                + ", rgbLighting=" + rgbLighting + ", monitor=" + monitor + "}";
    }

    public static final class Builder {
        private final String processor;
        private final int ramGb;
        private final int storageGb;
        private final OperatingSystem operatingSystem;
        private String graphicsCard;
        private int powerSupplyWatts = 500;
        private boolean wifiEnabled;
        private boolean bluetoothEnabled;
        private boolean activeCooling;
        private boolean rgbLighting;
        private Monitor monitor;

        public Builder(String processor, int ramGb, int storageGb, OperatingSystem operatingSystem) {
            this.processor = processor;
            this.ramGb = ramGb;
            this.storageGb = storageGb;
            this.operatingSystem = operatingSystem;
        }

        /** A non-null model means a dedicated GPU; null selects integrated graphics. */
        public Builder withGraphicsCard(String graphicsCard) {
            this.graphicsCard = graphicsCard;
            return this;
        }

        public Builder withPowerSupply(int powerSupplyWatts) {
            this.powerSupplyWatts = powerSupplyWatts;
            return this;
        }

        public Builder enableWiFi() {
            wifiEnabled = true;
            return this;
        }

        public Builder enableBluetooth() {
            bluetoothEnabled = true;
            return this;
        }

        public Builder enableActiveCooling() {
            activeCooling = true;
            return this;
        }

        public Builder enableRgbLighting() {
            rgbLighting = true;
            return this;
        }

        public Builder withMonitor(Monitor monitor) {
            this.monitor = monitor;
            return this;
        }

        public ComputerConfiguration build() {
            return new ComputerConfiguration(this);
        }
    }
}

