package org.bluebananas.lib.BBLib.Hardware.Motors;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/** Motor template with configurable feedback, feedforward, and motion profile controllers for both position and velocity.
 *  The controllers can be configured by overriding the corresponding functions.
 *  This class supports switching between position and velocity drive modes. */
public abstract class DualModeMotor {
    // Config
    DcMotor motorReference; // Stores the reference to the physical motor component

    // Properties
    double pos;
    double vel;
    double accel;
    protected double refPos;
    protected double refVel;
    protected double refAccel;
    double targetPos; // Final desired position
    double targetVel; // Final desired velocity
    enum Mode {
        TO_POS, // Moving to target position
        TO_VEL, // Moving to target velocity
        IDLE
    }
    Mode mode = Mode.IDLE; // Run mode of the motor

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
    public double getTargetPos() {
        return targetPos;
    }
    public double getTargetVel() {
        return targetVel;
    }

    // Other vars
    ElapsedTime timer = new ElapsedTime();
    double previousPos = 0;
    double previousVel = 0;



    // Constructors
    /** Constructs a {@link DualModeMotor}.
     * @param name The name of the motor in the hardware config.
     * @param opMode The running OpMode.
     */
    public DualModeMotor(String name, OpMode opMode) {
        motorReference = opMode.hardwareMap.get(DcMotor.class, name);
        motorReference.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // Functions
    /** Sets the target position.
     * @param targetPos Measured in ticks.
     * */
    public void moveToPos(double targetPos) {
        mode = Mode.TO_POS;
        this.targetPos = targetPos;
    }
    /** Sets the target velocity.
     * @param targetVel Measured in tps.
     * */
    public void accelerateToVel(double targetVel) {
        mode = Mode.TO_VEL;
        this.targetVel = targetVel;
    }
    public void idle() {
        mode = Mode.IDLE;
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

        switch (mode) {
            case TO_POS:
                toPosMotionProfile();
                motorReference.setPower(toPosFeedforward() + toPosFeedback());
                break;
            case TO_VEL:
                toVelMotionProfile();
                motorReference.setPower(toVelFeedforward() + toVelFeedback());
                break;
            case IDLE:
                motorReference.setPower(0);
                break;
        }

        previousPos = pos;
        previousVel = vel;
    }

    // Configurable Controllers
    protected abstract double toPosFeedback();
    double toPosFeedforward() {
        return  0;
    }
    void toPosMotionProfile() {
        refPos = targetPos;
    }
    protected abstract double toVelFeedback();
    double toVelFeedforward() {
        return  0;
    }
    void toVelMotionProfile() {
        refVel = targetVel;
    }
}
