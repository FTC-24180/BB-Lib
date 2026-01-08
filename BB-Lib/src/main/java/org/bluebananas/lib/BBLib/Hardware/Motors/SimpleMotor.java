package org.bluebananas.lib.BBLib.Hardware.Motors;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/** Provides basic control of a standard servo. */
public class SimpleMotor {
    // Config
    DcMotorSimple motorReference; // Stores the reference to the physical motor component

    // Properties
    double pow;

    // Property Getters
    public double getPow() {
        return pow;
    }



    // Constructors
    /** Constructs a {@link SimpleMotor}.
     * @param name The name of the motor in the hardware config.
     * @param opMode The running OpMode.
     */
    public SimpleMotor(String name, OpMode opMode) {
        motorReference = opMode.hardwareMap.get(DcMotorSimple.class, name);
    }

    // Functions
    /** Sets the power of the motor.
     * @param pow Set as a percentage of the motor power; value from 0 to 1.
     * */
    public void setPower(double pow) {
        this.pow = pow;
    }
    /** Updates the state of the servo. Must be called every loop for the servo to run properly. */
    public void update() {
        motorReference.setPower(pow);
    }
}
