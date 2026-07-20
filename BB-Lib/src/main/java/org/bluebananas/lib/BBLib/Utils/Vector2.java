package org.bluebananas.lib.BBLib.Utils;

/** An object for storing a 2D vector. Note: {@link Vector2} is not generic and can only handle doubles. */
@SuppressWarnings("UnusedDeclaration") // This is a library file
public class Vector2 {
    public double x;
    public double y;

    /** Constructs a {@link Vector2}.
     * @param x The x value of the vector.
     * @param y The y value of the vector.
     * */
    public Vector2(double x,double y) {
        this.x = x;
        this.y = y;
    }

    /** Returns the magnitude of the vector. */
    public double magnitude() {
        return Math.hypot(x, y);
    }

    /** Returns the angle of the vector in radians. Range: (-π, π) */
    public double angle() {
        return Math.atan2(y, x);
    }
}