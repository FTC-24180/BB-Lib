package org.bluebananas.lib.BBLib.Telemetry.TelemetryPlusTests;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.bluebananas.lib.BBLib.Telemetry.TelemetryPlus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class BaseFunctionalityTest {

    @Mock
    private Telemetry telemetry;

    private TelemetryPlus helper;

    @Before
    public void setUp() {
        helper = new TelemetryPlus(TelemetryPlus.VerbosityLevel.COMPETITION, telemetry);

        // Set the highest verbosity so every logging method is enabled.
        helper.setVerbosityLevel(TelemetryPlus.VerbosityLevel.DEBUG);
    }

    @Test
    public void logCritical_logsCriticalMessage() {
        helper.logCritical("Motor Failure", 1);

        helper.update();

        verify(telemetry).addLine("CRITICAL ERROR: Motor Failure");
        verify(telemetry).update();
    }

    @Test
    public void logError_logsErrorMessage() {
        helper.logError("Encoder Failure", 1);

        helper.update();

        verify(telemetry).addLine("ERROR: Encoder Failure");
        verify(telemetry).update();
    }

    @Test
    public void logWarning_logsWarningMessage() {
        helper.logWarning("Battery Low", 1);

        helper.update();

        verify(telemetry).addLine("WARNING: Battery Low");
        verify(telemetry).update();
    }

    @Test
    public void logCompetition_messageOverload() {
        helper.logCompetition("Robot Ready", 1);

        helper.update();

        verify(telemetry).addLine("Robot Ready");
        verify(telemetry).update();
    }

    @Test
    public void logCompetition_objectOverload() {
        helper.logCompetition("Voltage", 12.5, 1);

        helper.update();

        verify(telemetry).addData("Voltage", 12.5);
        verify(telemetry).update();
    }

    @Test
    public void logCompetition_formattedOverload() {
        helper.logCompetition("Position", "(%d,%d)", 1, 10, 20);

        helper.update();

        verify(telemetry).addData(
                eq("Position"),
                eq("(%d,%d)"),
                any(Object[].class));

        verify(telemetry).update();
    }

    @Test
    public void logVerbose_messageOverload() {
        helper.logVerbose("Verbose Message", 1);

        helper.update();

        verify(telemetry).addLine("Verbose Message");
        verify(telemetry).update();
    }

    @Test
    public void logVerbose_objectOverload() {
        helper.logVerbose("Voltage", 12.5, 1);

        helper.update();

        verify(telemetry).addData("Voltage", 12.5);
        verify(telemetry).update();
    }

    @Test
    public void logVerbose_formattedOverload() {
        helper.logVerbose("Position", "(%d,%d)", 1, 10, 20);

        helper.update();

        verify(telemetry).addData(
                eq("Position"),
                eq("(%d,%d)"),
                any(Object[].class));

        verify(telemetry).update();
    }

    @Test
    public void logDebug_messageOverload() {
        helper.logDebug("Debug Message", 1);

        helper.update();

        verify(telemetry).addLine("Debug Message");
        verify(telemetry).update();
    }

    @Test
    public void logDebug_objectOverload() {
        helper.logDebug("Voltage", 12.5, 1);

        helper.update();

        verify(telemetry).addData("Voltage", 12.5);
        verify(telemetry).update();
    }

    @Test
    public void logDebug_formattedOverload() {
        helper.logDebug("Position", "(%d,%d)", 1, 10, 20);

        helper.update();

        verify(telemetry).addData(
                eq("Position"),
                eq("(%d,%d)"),
                any(Object[].class));

        verify(telemetry).update();
    }

    @Test
    public void packets_areSortedByPriority() {
        helper.logDebug("Third", 3);
        helper.logDebug("First", 1);
        helper.logDebug("Second", 2);

        helper.update();

        InOrder order = inOrder(telemetry);

        order.verify(telemetry).addLine("First");
        order.verify(telemetry).addLine("Second");
        order.verify(telemetry).addLine("Third");
        order.verify(telemetry).update();
    }

    @Test
    public void update_callsTelemetryUpdateWhenBufferIsEmpty() {
        helper.update();

        verify(telemetry).update();
        verifyNoMoreInteractions(telemetry);
    }

    @Test
    public void update_clearsPacketBuffer() {
        helper.logDebug("Hello", 1);

        helper.update();
        helper.update();

        verify(telemetry).addLine("Hello");
        verify(telemetry, times(2)).update();
        verifyNoMoreInteractions(telemetry);
    }
}