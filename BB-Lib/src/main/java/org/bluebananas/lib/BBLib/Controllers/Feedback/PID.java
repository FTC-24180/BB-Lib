package org.bluebananas.lib.BBLib.Controllers.Feedback;

/** Feedback controller with a proportional term, integral term, derivative term, and velocity relative integral wind up dampening. */
@SuppressWarnings("UnusedDeclaration") // This is a library file
public class PID {
    // Gains
    double kp; // Gain for proportional term
    double ki; // Gain for integral term
    double kd; // Gain for derivative term
    double kivd; // Gain for velocity relative integral wind up dampening.

    // Vars for calc
    double integral = 0; // Stores the current integral
    double previousError = 0; // Stores the position error from the previous loop



    // Constructors
    /** Constructs a {@link PID}.
     * @param kp Gain for the proportional term.
     * @param ki Gain for the integral term.
     * @param kd Gain for the derivative term.
     * @param kivd Gain for velocity relative integral wind up dampening.
     * */
    public PID(double kp, double ki, double kd, double kivd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.kivd = kivd;
    }

    // Functions
    /** Runs the {@link PID} calculation on the arguments and returns the result.
     * @param currentValue Current value of process variable.
     * @param reference Desired value.
     * @param dt Delta time.
     * @return Control effort, most often motor power.
     * */
    public double calc(double currentValue, double reference, double dt) {
        double error = reference - currentValue;
        double derivative = (error - previousError) / dt;
        integral += (error / Math.max((kivd * Math.abs(derivative)), 1)) * dt;
        if (Math.signum(error) != Math.signum(previousError)) {integral = 0;}
        previousError = error;
        return (error * kp) + (integral * ki) + (derivative * kd);
    }
    /** Resets the integral. */
    public void resetIntegral() {
        integral = 0;
    }
    /** Resets the {@link PID}'s gains to the provided values.
     * @param kp Gain for the proportional term.
     * @param ki Gain for the integral term.
     * @param kd Gain for the derivative term.
     * @param kivd Gain for velocity relative integral wind up dampening.
     * */
    public void setCoefficients(double kp, double ki, double kd, double kivd){
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        this.kivd = kivd;
    }
}