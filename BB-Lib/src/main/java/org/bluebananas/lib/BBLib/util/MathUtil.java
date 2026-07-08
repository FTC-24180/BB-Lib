package org.bluebananas.lib.BBLib.util;

/** Pure-Java numeric helpers with no FTC SDK dependency. */
public final class MathUtil {

    private MathUtil() {
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    /** Returns 0 when {@code Math.abs(value) < threshold}, otherwise returns value unchanged. */
    public static double applyDeadband(double value, double threshold) {
        return Math.abs(value) < threshold ? 0.0 : value;
    }
}
