package org.bluebananas.lib.BBLib.Utils;

import org.junit.Test;

import java.util.function.DoubleSupplier;

import static org.junit.Assert.*;

public class Pose2Test {

    // --- Constructor Tests ---

    @Test
    public void testConstructorWithVectorAndSupplier() {
        Vector2 vec = new Vector2(0.0, 0.0);
        double thetaVal = 0.0;
        DoubleSupplier supplierT = () -> thetaVal;

        Pose2 pose = new Pose2(vec, supplierT);

        assertSame(vec, pose.vector);
        assertNotNull(pose.t);
        assertEquals(0.0, pose.t.getAsDouble(), 0.0);
    }

    @Test
    public void testConstructorWithVectorAndDouble() {
        Vector2 vec = new Vector2(0.0, 0.0);
        double theta = 0.0;

        Pose2 pose = new Pose2(vec, theta);

        assertSame(vec, pose.vector);
        assertNotNull(pose.t);
        assertEquals(0.0, pose.t.getAsDouble(), 0.0);
    }

    // --- Public Member Tests ---

    @Test
    public void testPublicMemberVector() {
        Vector2 vec = new Vector2(0.0, 0.0);
        Pose2 pose = new Pose2(vec, 0.0);

        assertNotNull(pose.vector);
        assertSame(vec, pose.vector);

        // Verify the vector inside the pose reflects the original
        assertEquals(0.0, pose.vector.x.getAsDouble(), 0.0);
        assertEquals(0.0, pose.vector.y.getAsDouble(), 0.0);
    }

    @Test
    public void testPublicMemberT() {
        Vector2 vec = new Vector2(0.0, 0.0);
        double theta = 0.0;
        Pose2 pose = new Pose2(vec, theta);

        assertNotNull(pose.t);
        assertEquals(0.0, pose.t.getAsDouble(), 0.0);
    }

    // --- Dynamic Behavior Tests ---

    @Test
    public void testDynamicThetaSupplier() {
        Vector2 vec = new Vector2(0.0, 0.0);
        double[] mutableTheta = {0.0};
        DoubleSupplier supplierT = () -> mutableTheta[0];

        Pose2 pose = new Pose2(vec, supplierT);

        // Initial state: 0.0
        assertEquals(0.0, pose.t.getAsDouble(), 0.0);

        // Update the underlying value
        mutableTheta[0] = Math.PI;

        // Verify the pose reflects the change immediately
        assertEquals(Math.PI, pose.t.getAsDouble(), 0.0);
    }

    @Test
    public void testDynamicVectorReference() {
        // Create a vector with 0s
        Vector2 vec = new Vector2(0.0, 0.0);
        Pose2 pose = new Pose2(vec, 0.0);

        // Verify initial state
        assertEquals(0.0, pose.vector.magnitude(), 0.0);

        // Modify the vector's internal supplier state (simulating external change)
        // Note: Vector2 stores suppliers, so we can't easily mutate the double directly
        // without recreating the Vector2 or accessing its private state if it existed.
        // However, since Vector2 takes suppliers, we can test if Pose2 holds the reference correctly.
        // We'll just verify the reference is the same object.
        assertSame(vec, pose.vector);

        // If we were to recreate the vector with new suppliers, the Pose2 wouldn't see it
        // because it holds the reference to the old Vector2 instance.
        // This test confirms the reference integrity.
    }
}
