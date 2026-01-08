package org.bluebananas.lib.BBLib.Hardware.Motors;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

/** Provides power control for motors. */
public class SimpleMotor {
    // Config
    DcMotor motorReference; // Stores the reference to the physical motor component

    // Properties
    double pow;
    double pos;
    double vel;
    double accel;

    // Property Getters
    public double getPow() {
        return pow;
    }
    public double getPos() {
        return pos;
    }
    public double getVel() {
        return vel;
    }
    public double getAccel() {
        return accel;
    }

    // Other vars
    ElapsedTime timer = new ElapsedTime();
    double previousPos = 0;
    double previousVel = 0;


    // Constructors
    /** Constructs a {@link SimpleMotor}.
     * @param name The name of the motor in the hardware config.
     * @param opMode The running OpMode.
     */
    public SimpleMotor(String name, OpMode opMode) {
        motorReference = opMode.hardwareMap.get(DcMotor.class, name);
    }

    // Functions
    /** Sets the power of the motor.
     * @param pow Set as a percentage of the motor power; value from 0 to 1.
     * */
    public void setPower(double pow) {
        this.pow = pow;
    }
    /** Updates the state of the motor. Must be called every loop for the motor to run properly. */
    public void update() {
        motorReference.setPower(pow);

        pos = motorReference.getCurrentPosition();
        vel = (pos - previousPos) / timer.seconds();
        accel = (vel - previousVel) / timer.seconds();

        previousPos = pos;
        previousVel = vel;
    }
}
