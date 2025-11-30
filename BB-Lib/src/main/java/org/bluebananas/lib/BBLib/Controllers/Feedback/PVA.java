package org.bluebananas.lib.BBLib.Controllers.Feedback;

/** Feedback controller with proportional position, velocity, and acceleration terms. */
public class PVA {
    // Gains
    double kp; // Gain for the position term
    double kv; // Gain for the velocity term
    double ka; // Gain for the acceleration term



    // Constructors
    /** Constructs a {@link PVA}.
     * @param kp Gain for the position term.
     * @param kv Gain for the velocity term.
     * @param ka Gain for the acceleration term.
     * */
    public PVA(double kp, double kv, double ka) {
        this.kp = kp;
        this.kv = kv;
        this.ka = ka;
    }

    //Functions
    /** Runs the {@link PVA} calculation on the arguments and returns the result.
     * @param pos Current motor position.
     * @param refPos Desired motor position.
     * @param vel Current motor velocity.
     * @param refVel Desired motor velocity.
     * @param accel Current motor acceleration.
     * @param refAccel Desired motor acceleration.
     * @return Motor power.
     * */
    public double calc(double pos, double refPos, double vel, double refVel, double accel, double refAccel) {
        double posError = refPos - pos;
        double velError = refVel - vel;
        double accelError = refAccel - accel;
        return (kp * posError) + (kv * velError) + (ka * accelError); // Applies gains and adds the terms together
    }
}