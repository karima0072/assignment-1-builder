package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MonitorTest {
    @Test
    void storesMonitorValues() {
        Monitor monitor = new Monitor("Dell", 60, 24);
        assertEquals("Dell", monitor.getModel());
        assertEquals(60, monitor.getRefreshRateHz());
        assertEquals(24, monitor.getSizeInches());
    }

    @Test
    void rejectsNullOrBlankModel() {
        assertThrows(IllegalArgumentException.class, () -> new Monitor(null, 60, 24));
        assertThrows(IllegalArgumentException.class, () -> new Monitor(" ", 60, 24));
    }

    @Test
    void rejectsNonPositiveRefreshRate() {
        assertThrows(IllegalArgumentException.class, () -> new Monitor("Dell", 0, 24));
        assertThrows(IllegalArgumentException.class, () -> new Monitor("Dell", -1, 24));
    }

    @Test
    void rejectsNonPositiveSize() {
        assertThrows(IllegalArgumentException.class, () -> new Monitor("Dell", 60, 0));
        assertThrows(IllegalArgumentException.class, () -> new Monitor("Dell", 60, -1));
    }
}
