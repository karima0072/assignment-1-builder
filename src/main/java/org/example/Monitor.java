package org.example;

/** Immutable monitor shared safely between configurations. */
public final class Monitor {
    private final String model;
    private final int refreshRateHz;
    private final int sizeInches;

    public Monitor(String model, int refreshRateHz, int sizeInches) {
        if (model == null || model.isBlank()) {
            throw new IllegalArgumentException("Monitor model must not be null or blank");
        }
        if (refreshRateHz <= 0) {
            throw new IllegalArgumentException("Monitor refresh rate must be greater than 0");
        }
        if (sizeInches <= 0) {
            throw new IllegalArgumentException("Monitor size must be greater than 0");
        }
        this.model = model;
        this.refreshRateHz = refreshRateHz;
        this.sizeInches = sizeInches;
    }

    public String getModel() { return model; }
    public int getRefreshRateHz() { return refreshRateHz; }
    public int getSizeInches() { return sizeInches; }

    @Override
    public String toString() {
        return model + " (" + refreshRateHz + " Hz, " + sizeInches + " inches)";
    }
}
