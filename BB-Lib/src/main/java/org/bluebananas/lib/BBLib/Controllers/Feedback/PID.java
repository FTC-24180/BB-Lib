package org.bluebananas.lib.BBLib.Controllers.Feedback;

import com.qualcomm.robotcore.util.ElapsedTime;
/** Feedback controller with position based proportional, integral and derivative terms. */
public class PID {
    // Gains
    double kp; // Gain for proportional term
    double ki; // Gain for integral term
    double kd; // Gain for derivative term

    // Vars for calc
    ElapsedTime timer; // Timer used for calculating derivative and integral
    double integral; // Stores the current integral
    double previousError; // Stores the position error from the previous loop



    // Constructors
    /** Constructs a {@link PID}.
     * @param kp Gain for the proportional term.
     * @param ki Gain for the integral term.
     * @param kd Gain for the derivative term.
     * */
    public PID(double kp, double ki, double kd) {
        this.kp = kp;
        this.ki = ki;
        this.kd = kd;
        timer = new ElapsedTime();
    }

    // Functions
    /** Runs the {@link PID} calculation on the arguments and returns the result.
     * @param position Current motor position.
     * @param reference Desired motor position.
     * @return Motor power.
     * */
    public double calc(double position, double reference) {
        double error = reference - position;
        integral += error * timer.seconds();
        double derivative = (error - previousError) / timer.seconds();
        timer.reset();
        return (error * kp) + (integral * ki) + (derivative * kd);
    }
    /** Resets the integral and timer. */
    public void reset() {
        integral = 0;
        timer.reset();
    }
}