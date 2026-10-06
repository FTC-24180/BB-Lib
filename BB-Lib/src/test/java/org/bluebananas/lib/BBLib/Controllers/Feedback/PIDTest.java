package org.bluebananas.lib.BBLib.Controllers.Feedback;

import org.junit.Test;

import java.util.function.DoubleSupplier;

import static org.junit.Assert.*;

public class PIDTest {

    // --- Constructor Tests ---

    @Test
    public void testConstructorWithSuppliers() {
        double kpVal = 0.0;
        double kiVal = 0.0;
        double kdVal = 0.0;
        double kivdVal = 0.0;

        DoubleSupplier kp = () -> kpVal;
        DoubleSupplier ki = () -> kiVal;
        DoubleSupplier kd = () -> kdVal;
        DoubleSupplier kivd = () -> kivdVal;

        PID pid = new PID(kp, ki, kd, kivd);

        // We cannot access private fields directly, but we can verify construction didn't crash
        // and that the object exists. The behavior is tested in calc().
        assertNotNull(pid);
    }

    @Test
    public void testConstructorWithDoubles() {
        PID pid = new PID(0.0, 0.0, 0.0, 0.0);
        assertNotNull(pid);
    }

    // --- setCoefficients Tests ---
    @Test
    public void testSetCoefficientsWithSuppliers() {
        PID pid = new PID(0.0, 0.0, 0.0, 0.0);

        pid.setCoefficients(() -> 2.0,() -> 0.0,() -> 0.0,()-> 0.0);

        // Verify the change by running a calc that isolates P term
        // error = 10. Output = 10 * 2.0 = 20
        double output = pid.calc(0.0, 10, 1.0);
        assertEquals(20.0, output, 0.0);
    }

    @Test
    public void testSetCoefficientsWithDoubles() {
        PID pid = new PID(0.0, 0.0, 0.0, 0.0);

        pid.setCoefficients(2.0, 0.0, 0.0, 0.0);

        // error = 10. Output = 10 * 2.0 = 20
        double output = pid.calc(0.0, 10.0, 1.0);
        assertEquals(20.0, output, 0.0);
    }

    // --- calc() Logic Tests ---

    @Test
    public void testCalcWithZeroInputs() {
        // All gains 0, current 0, ref 0
        PID pid = new PID(0.0, 0.0, 0.0, 0.0);
        double output = pid.calc(0.0, 0.0, 1.0);
        assertEquals(0.0, output, 0.0);
    }

    @Test
    public void testProportionalTermOnly() {
        // Ki, Kd, Kivd = 0. Only Kp matters.
        PID pid = new PID(2.0, 0.0, 0.0, 1.0);

        // Error = 10 - 5 = 5. Output = 5 * 2 = 10
        double output = pid.calc(5.0, 10.0, 1.0);
        assertEquals(10.0, output, 0.0);
    }

    @Test
    public void testIntegralAccumulation() {
        // Kp=0, Kd=0, Ki=0.
        PID pid = new PID(0.0, 1.0, 0.0, 0.0);

        // Step 1: Error = 10. Integral += (10/1)*1 = 10. Output = 10.
        double out1 = pid.calc(0.0, 10.0, 1.0);
        assertEquals(10.0, out1, 0.0);

        // Step 2: Error still 10 (assuming state hasn't changed).
        // Integral += (10/1)*1 = 10. Total Int = 20. Output = 20.
        double out2 = pid.calc(0.0, 10.0, 1.0);
        assertEquals(20.0, out2, 0.0);
    }

    @Test
    public void testDerivativeTerm() {
        // Kp=0, Ki=0, Kd=2.
        PID pid = new PID(0.0, 0.0, 2.0, 1.0);

        // Step 1: Error = 10. Deriv = (10-0)/1 = 10. Output = 20.
        double out1 = pid.calc(0.0, 10.0, 1.0);
        assertEquals(20.0, out1, 0.0);

        // Step 2: Error still 10. Deriv = (10-10)/1 = 0. Output = 0.
        double out2 = pid.calc(0.0, 10.0, 1.0);
        assertEquals(0.0, out2, 0.0);
    }

    @Test
    public void testIntegralWindUpResetOnSignChange() {
        // Kp=0, Ki=1, Kd=0, Kivd=0
        PID pid = new PID(0.0, 1.0, 0.0, 0.0);

        // Accumulate positive error
        pid.calc(0.0, 10.0, 1.0); // Int = 10
        pid.calc(0.0, 10.0, 1.0); // Int = 20

        // Switch to negative error (sign flip)
        // Error = -10. Previous was +10. Signum differs -> Integral resets to 0
        // and the new error is added to the integral.
        double out = pid.calc(20.0, 10.0, 1.0);

        // Output should be purely P term (0) + I term (-10) + D term (0) = 0?
        assertEquals(-10.0, out, 0.0);

        // Verify next step doesn't carry old integral
        double outNext = pid.calc(20.0, 10.0, 1.0); // Error = -10. Int accumulates to -20.
        // Output = 0 + (-20 * 1) + 0 = -20
        assertEquals(-20.0, outNext, 0.0);
    }

    @Test
    public void testVelocityRelativeWindUpDampening() {
        // High derivative reduces integral accumulation factor.
        // Formula: error / max(kivd * |deriv|, 1)
        // If deriv is huge, divisor is huge, integral grows slowly.

        PID pid = new PID(0.0, 1.0, 0.0, 10.0); // High kivd

        // Step 1: Error 10. Deriv 10. Divisor = max(10*10, 1) = 100.
        // Int += (10 / 100) * 1 = 0.1. Output = 0.1.
        double out1 = pid.calc(0.0, 10.0, 1.0);
        assertEquals(0.1, out1, 0.0);

        // Step 2: Error 10. Deriv 0. Divisor = max(0, 1) = 1.
        // Int += (10 / 1) * 1 = 10. Total Int = 10.1. Output = 10.1.
        double out2 = pid.calc(0.0, 10.0, 1.0);
        assertEquals(10.1, out2, 0.0);
    }

    @Test
    public void testCalcWithZeroDeltaTimeHandling() {
        // Although dt=0 causes div by zero in derivative, the code uses Math.max(..., 1) for integral.
        // But derivative calculation: (error - prev) / dt. If dt=0, this throws Infinity or NaN.
        // The code does not guard dt=0 in the derivative line.
        // Assuming valid dt > 0 for normal operation, but testing 0.0 input as requested.
        // If dt=0, derivative becomes Infinity.
        // Let's test with a very small dt to avoid crash, or accept Infinity behavior if expected.
        // Given the prompt asks for 0 inputs, we test dt=0.0.

        PID pid = new PID(0.0, 0.0, 1.0, 1.0); // Only Kd active

        // First call: error 10, prev 0. deriv = 10/0 = Infinity.
        // Output = Infinity * 1 = Infinity.
        double out = pid.calc(0.0, 10.0, 0.0);
        assertTrue(Double.isInfinite(out));
    }

    // --- resetIntegral Tests ---

    @Test
    public void testResetIntegral() {
        PID pid = new PID(0.0, 1.0, 0.0, 1.0);

        // Accumulate
        pid.calc(0.0, 10.0, 1.0); // Int = 10
        pid.calc(0.0, 10.0, 1.0); // Int = 20

        pid.resetIntegral();

        // Next calc should start fresh integral
        // Error 10. Int += 10. Output = 10.
        double out = pid.calc(0.0, 10.0, 1.0);
        assertEquals(10.0, out, 0.0);
    }

    @Test
    public void testDynamicGainReference() {
        PID pid = new PID(0.0, 0.0, 0.0, 0.0);

        double newKp = 2.0;
        DoubleSupplier kp = () -> newKp;
        DoubleSupplier ki = () -> 0.0;
        DoubleSupplier kd = () -> 0.0;
        DoubleSupplier kivd = () -> 0.0;

        pid.setCoefficients(kp, ki, kd, kivd);

        // Verify the change by running a calc that isolates P term
        // error = 10 - 0 = 10. Output = 10 * kp = 20
        double output = pid.calc(0.0, 10.0, 1.0);
        assertEquals(20.0, output, 0.0);
    }
}