package org.bluebananas.lib.BBLib.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class MathUtilTest {

    @Test
    public void clamp_withinRange_returnsValue() {
        assertEquals(0.5, MathUtil.clamp(0.5, -1.0, 1.0), 1e-9);
    }

    @Test
    public void clamp_aboveMax_returnsMax() {
        assertEquals(1.0, MathUtil.clamp(5.0, -1.0, 1.0), 1e-9);
    }

    @Test
    public void clamp_belowMin_returnsMin() {
        assertEquals(-1.0, MathUtil.clamp(-5.0, -1.0, 1.0), 1e-9);
    }

    @Test
    public void applyDeadband_belowThreshold_returnsZero() {
        assertEquals(0.0, MathUtil.applyDeadband(0.02, 0.05), 1e-9);
    }

    @Test
    public void applyDeadband_aboveThreshold_returnsValue() {
        assertEquals(0.3, MathUtil.applyDeadband(0.3, 0.05), 1e-9);
    }
}
