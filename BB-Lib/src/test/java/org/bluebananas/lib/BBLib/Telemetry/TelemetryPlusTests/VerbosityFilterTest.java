package org.bluebananas.lib.BBLib.Telemetry.TelemetryPlusTests;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.bluebananas.lib.BBLib.Telemetry.TelemetryPlus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collection;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(Parameterized.class)
public class VerbosityFilterTest {

    @FunctionalInterface
    public interface LogAction {
        void log(TelemetryPlus helper);
    }

    @Parameterized.Parameter()
    public TelemetryPlus.VerbosityLevel verbosity;

    @Parameterized.Parameter(1)
    public LogAction action;

    @Parameterized.Parameter(2)
    public boolean shouldLog;

    @Parameterized.Parameter(3)
    public String expectedMessage;

    @Parameterized.Parameters(name = "{index}: {0} -> {3}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{

                // ---------- ERROR ----------
                {TelemetryPlus.VerbosityLevel.CRITICAL,
                        (LogAction) h -> h.logError("Test", 1),
                        false,
                        "ERROR: Test"},

                {TelemetryPlus.VerbosityLevel.ERROR,
                        (LogAction) h -> h.logError("Test", 1),
                        true,
                        "ERROR: Test"},

                // ---------- WARNING ----------
                {TelemetryPlus.VerbosityLevel.ERROR,
                        (LogAction) h -> h.logWarning("Test", 1),
                        false,
                        "WARNING: Test"},

                {TelemetryPlus.VerbosityLevel.WARNING,
                        (LogAction) h -> h.logWarning("Test", 1),
                        true,
                        "WARNING: Test"},

                // ---------- COMPETITION ----------
                {TelemetryPlus.VerbosityLevel.ERROR,
                        (LogAction) h -> h.logCompetition("Competition", 1),
                        false,
                        "Competition"},

                {TelemetryPlus.VerbosityLevel.COMPETITION,
                        (LogAction) h -> h.logCompetition("Competition", 1),
                        true,
                        "Competition"},

                // ---------- VERBOSE ----------
                {TelemetryPlus.VerbosityLevel.WARNING,
                        (LogAction) h -> h.logVerbose("Verbose", 1),
                        false,
                        "Verbose"},

                {TelemetryPlus.VerbosityLevel.VERBOSE,
                        (LogAction) h -> h.logVerbose("Verbose", 1),
                        true,
                        "Verbose"},

                // ---------- DEBUG ----------
                {TelemetryPlus.VerbosityLevel.VERBOSE,
                        (LogAction) h -> h.logDebug("Debug", 1),
                        false,
                        "Debug"},

                {TelemetryPlus.VerbosityLevel.DEBUG,
                        (LogAction) h -> h.logDebug("Debug", 1),
                        true,
                        "Debug"},
        });
    }

    private Telemetry telemetry;

    private TelemetryPlus helper;

    @Before
    public void setUp() {
        telemetry = Mockito.mock(Telemetry.class);

        helper = new TelemetryPlus(TelemetryPlus.VerbosityLevel.COMPETITION, telemetry);
    }

    @Test
    public void verbosityFilteringWorks() {

        helper.setVerbosityLevel(verbosity);

        action.log(helper);

        helper.update();

        if (shouldLog) {
            verify(telemetry).addLine(expectedMessage);
            verify(telemetry).update();
        } else {
            verify(telemetry).update();
            verifyNoMoreInteractions(telemetry);
        }
    }
}