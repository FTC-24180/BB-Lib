package org.bluebananas.lib.BBLib.Hardware;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.Servo;

/** Provides basic control of a standard servo. */
public class StandardServo {
    // Config
    Servo servoReference; // Stores the reference to the physical servo component

    // Properties
    double pos;

    // Property Getters
    public double getPos() {
        return pos;
    }



    // Constructors
    /** Constructs a {@link StandardServo}.
     * @param name The name of the servo in the hardware config.
     * @param opMode The running OpMode.
     */
    public StandardServo(String name, OpMode opMode) {
        servoReference = opMode.hardwareMap.get(Servo.class, name);
    }

    // Functions
    /** Sets the position of the servo.
     * @param pos Set as a percentage of the servos range of motion; value from 0 to 1.
     * */
    public void moveToPosition(double pos) {
        this.pos = pos;
    }
    /** Updates the state of the servo. Must be called every loop for the servo to run properly. */
    public void update() {
        servoReference.setPosition(pos);
    }
}
