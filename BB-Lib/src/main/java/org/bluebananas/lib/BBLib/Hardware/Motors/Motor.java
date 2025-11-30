package org.bluebananas.lib.BBLib.Hardware.Motors;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/** Motor template with configurable feedback, feedforward, and motion profile controllers for either position or velocity.
 *  The controllers can be configured by overriding the corresponding functions.
 *  Note: Switching between position and velocity drive modes is not supported in {@link Motor}.
 *  Use {@link DualModeMotor} if that functionality is desired. */
public abstract class Motor {
    // Config
    DcMotor motorReference; // Stores the reference to the physical motor component

    // Properties
    double pos;
    double vel;
    double accel;
    protected double refPos;
    protected double refVel;
    protected double refAccel;
    double target; // Final desired position or velocity
    boolean isIdle = true;

    // Property geters
    public double getPos() {
        return pos;
    }
    public double getVel() {
        return vel;
    }
    public double getAccel() {
        return accel;
    }
    public double getTarget() {
        return target;
    }

    // Other vars
    ElapsedTime timer = new ElapsedTime();
    double previousPos = 0;
    double previousVel = 0;



    // Constructors
    /** Constructs a {@link Motor}.
     * @param name The name of the motor in the hardware config.
     * @param opMode The running OpMode.
     */
    public Motor(String name, OpMode opMode) {
        motorReference = opMode.hardwareMap.get(DcMotor.class, name);
        motorReference.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // Functions
    /** Sets the target for the motor.
     * @param target Measured in ticks or tps.
     * */
    public void setTarget(double target) {
        this.target = target;
        isIdle = false;
    }
    public void idle() {
        isIdle = true;
    }
    public void resetEncoder() {
        motorReference.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motorReference.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    /** Updates the state of the motor. Must be called every loop for the motor to run properly. */
    public void update() {
        pos = motorReference.getCurrentPosition();
        vel = (pos - previousPos) / timer.seconds();
        accel = (vel - previousVel) / timer.seconds();

        if (!isIdle) {
            motionProfile();
            motorReference.setPower(feedforward() + feedback());
        } else {
            motorReference.setPower(0);
        }

        previousPos = pos;
        previousVel = vel;
    }

    // Configurable Controllers
    protected abstract double feedback();
    double feedforward() {
        return  0;
    }
    void motionProfile() {
        refPos = target;
        refVel = target;
    }
}
