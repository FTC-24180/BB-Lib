package org.bluebananas.lib.BBLib.Utils;

import org.junit.Test;

import java.util.function.DoubleSupplier;

import static org.junit.Assert.*;

public class Vector2Test {

    // --- Constructor Tests ---

    @Test
    public void testConstructorWithSuppliers() {
        double valX = 0.0;
        double valY = 0.0;
        DoubleSupplier supplierX = () -> valX;
        DoubleSupplier supplierY = () -> valY;

        Vector2 vector = new Vector2(supplierX, supplierY);

        assertEquals(0.0, vector.x.getAsDouble(), 0.0);
        assertEquals(0.0, vector.y.getAsDouble(), 0.0);
    }

    @Test
    public void testConstructorWithDoubles() {
        // Testing with 0 as requested
        Vector2 vector = new Vector2(0.0, 0.0);

        assertEquals(0.0, vector.x.getAsDouble(), 0.0);
        assertEquals(0.0, vector.y.getAsDouble(), 0.0);
    }

    // --- Public Field Tests ---

    @Test
    public void testPublicFieldAccess() {
        Vector2 vector = new Vector2(0.0, 0.0);

        assertNotNull(vector.x);
        assertNotNull(vector.y);
    }

    // --- Magnitude Tests ---

    @Test
    public void testMagnitudeZero() {
        Vector2 vector = new Vector2(0.0, 0.0);
        assertEquals(0.0, vector.magnitude(), 0.0);
    }

    @Test
    public void testMagnitudeOnYAxis() {
        Vector2 vector = new Vector2(3.0, 0.0);
        assertEquals(3.0, vector.magnitude(), 0.0);
    }

    @Test
    public void testMagnitudeOnXAxis() {
        Vector2 vector = new Vector2(0.0, 3.0);
        assertEquals(3.0, vector.magnitude(), 0.0);
    }

    @Test
    public void testMagnitudePythagorean() {
        Vector2 vector = new Vector2(3.0, 4.0);
        assertEquals(5.0, vector.magnitude(), 0.0);
    }

    // --- Angle Tests ---

    @Test
    public void testAngleZero() {
        Vector2 vector = new Vector2(0.0, 0.0);
        // Math.atan2(0.0, 0.0) returns 0.0
        assertEquals(0.0, vector.angle(), 0.0);
    }

    @Test
    public void testAnglePositiveX() {
        Vector2 vector = new Vector2(1.0, 0.0);
        assertEquals(0.0, vector.angle(), 0.0);
    }

    @Test
    public void testAnglePositiveY() {
        Vector2 vector = new Vector2(0.0, 1.0);
        assertEquals(Math.PI / 2.0, vector.angle(), 0.0);
    }

    @Test
    public void testAngleNegativeX() {
        Vector2 vector = new Vector2(-1.0, 0.0);
        assertEquals(Math.PI, vector.angle(), 0.0);
    }

    @Test
    public void testAngleNegativeY() {
        Vector2 vector = new Vector2(0.0, -1.0);
        assertEquals(-Math.PI / 2.0, vector.angle(), 0.0);
    }

    @Test
    public void testDynamicSuppliers() {
        double[] valX = {0.0};
        double[] valY = {0.0};

        Vector2 vector = new Vector2(() -> valX[0], () -> valY[0]);

        // Start at (0,0)
        assertEquals(0.0, vector.x.getAsDouble(), 0.0);
        assertEquals(0.0, vector.y.getAsDouble(), 0.0);

        // Move to (1, 1)
        valX[0] = 1.0;
        valY[0] = 1.0;
        assertEquals(1.0, vector.x.getAsDouble(), 0.0);
        assertEquals(1.0, vector.y.getAsDouble(), 0.0);
    }
}