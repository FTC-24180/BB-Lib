package org.bluebananas.lib.BBLib.Controllers.Feedforward;

/** Feedforward controller with velocity term, acceleration term, and ks term to account for static friction. */
public class VAS {
    // Gains
    double kv;
    double ka;
    double ks;



    // Constructors
    /** Constructs a {@link VAS}.
     * @param kv Gain for the velocity term.
     * @param ka Gain for the acceleration term.
     * @param ks Gain for the static friction term.
     * */
    public VAS(double kv, double ka, double ks) {
        this.kv = kv;
        this.ka = ka;
        this.ks = ks;
    }

    // Functions
    /** Runs the {@link VAS} calculation on the arguments and returns the result.
     * @param refVel Reference velocity.
     * @param refAccel Reference acceleration.
     * @return Motor power.
     * */
    public double calc(double refVel, double refAccel) {
        return (refVel * kv) + (refAccel * ka) + (Math.max(1 - Math.floor(refVel), 0) * ks);
    }
}

