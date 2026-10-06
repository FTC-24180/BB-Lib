package org.bluebananas.lib.BBLib.Utils;

import java.util.function.DoubleSupplier;

/** An object for storing a 2D pose(a 2D vector and yaw). Note: {@link Pose2} is not generic and can only handle doubles. */
@SuppressWarnings("UnusedDeclaration") // This is a library file
public class Pose2 {
    /*---------- Public Members ----------*/
    public Vector2 vector;
    public DoubleSupplier t;

    /*---------- Constructors ----------*/
    /** Constructs a {@link Pose2}.
     * @param vector The 2D vector of the pose.
     * @param t Theta, the yaw rotation of the pose.
     * */
    public Pose2(Vector2 vector, DoubleSupplier t) {
        this.vector = vector;
        this.t = t;
    }

    /** Constructs a {@link Pose2}.
     * @param vector The 2D vector of the pose.
     * @param t Theta, the yaw rotation of the pose.
     * */
    public Pose2(Vector2 vector, double t) {
        this.vector = vector;
        this.t = ()-> t;
    }
}