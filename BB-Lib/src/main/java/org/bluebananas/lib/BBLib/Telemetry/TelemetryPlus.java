package org.bluebananas.lib.BBLib.Telemetry;

import java.util.ArrayList;
import java.util.Comparator;
import org.firstinspires.ftc.robotcore.external.Telemetry;


/** {@link Telemetry} wrapper adding various improvements including organization and filtering features. */
@SuppressWarnings("UnusedDeclaration") // This is a library file
public class TelemetryPlus {
    /*---------- Nested Classes ----------*/
    // Public Classes
    public enum VerbosityLevel {
        CRITICAL, // Required to maintain correct ordinal numbering
        ERROR,
        COMPETITION,
        WARNING,
        VERBOSE,
        DEBUG
    }

    // Internal Classes
    /** Data structure containing the information needed to add a telemetry entry to the log via a
     * {@link Telemetry} interface.
     * */
    private static class TelemetryPacket {
        public String caption;
        public Object value;
        public String format;
        public Object[] args;
        /** The priority used to determine the display order of the telemetry entries. */
        public int displayPriority;

        // Constructors
        /** Constructs a {@link TelemetryPacket}.
         * @param caption The caption of the telemetry entry.
         * @param displayPriority The {@link #displayPriority} of the telemetry entry.
         * */
        public TelemetryPacket(String caption, int displayPriority) {
            this.caption = caption;
            this.displayPriority = displayPriority;
        }

        /** Constructs a {@link TelemetryPacket}.
         * @param caption The caption of the telemetry entry.
         * @param value The data of the telemetry entry.
         * @param displayPriority The {@link #displayPriority} of the telemetry entry.
         * */
        public TelemetryPacket(String caption, Object value, int displayPriority) {
            this.caption = caption;
            this.value = value;
            this.displayPriority = displayPriority;
        }

        /** Constructs a {@link TelemetryPacket}.
         * @param caption The caption of the telemetry entry.
         * @param format The formating string of the telemetry entry. See {@link Telemetry#addData(String, String, Object...)}.
         * @param displayPriority The {@link #displayPriority} of the telemetry entry.
         * @param args The data of the telemetry entry.
         * */
        public TelemetryPacket(String caption, String format, int displayPriority, Object... args) {
            this.caption = caption;
            this.format = format;
            this.args = args;
            this.displayPriority = displayPriority;
        }
    }

    /*---------- Private Members ----------*/
    // Source Instances
    Telemetry telemetry;

    // Internal vars
    /** Current verbosity level of the output telemetry. */
    VerbosityLevel verbosityLevel;
    /** Stores the filtered telemetry packets for sorting. */
    ArrayList<TelemetryPacket> packetBuffer;

    /*---------- Constructors ----------*/
    /** Constructs a {@link TelemetryPlus}
     * @param verbosityLevel The desired verbosity level of the output telemetry.
     * @param telemetry OpMode {@link Telemetry} instance.
     * */
    public TelemetryPlus(VerbosityLevel verbosityLevel, Telemetry telemetry) {
        this.telemetry = telemetry;
        this.verbosityLevel = verbosityLevel;
        packetBuffer = new ArrayList<>();
    }

    /*---------- Public Functions ----------*/
    /** Updates the telemetry output. */
    public void update() {
        packetBuffer.sort(Comparator.comparingInt(TelemetryPacket -> TelemetryPacket.displayPriority));

        for (TelemetryPacket packet : packetBuffer) {
            if (packet.caption != null && packet.format != null && packet.args != null) {
                telemetry.addData(packet.caption, packet.format, packet.args);
            } else if (packet.caption != null && packet.value != null) {
                telemetry.addData(packet.caption, packet.value);
            } else if (packet.caption != null) {
                telemetry.addLine(packet.caption);
            } else {
                telemetry.addLine("TELEMETRY ERROR: Invalid Packet");
            }
        }

        telemetry.update();

        packetBuffer.clear();
    }

    /** Sets the verbosity level of the output telemetry.
     * @param verbosityLevel The desired verbosity level. */
    public void setVerbosityLevel(VerbosityLevel verbosityLevel) {
        this.verbosityLevel = verbosityLevel;
    }

    /** Adds a critical error message to the telemetry log.
     * @param caption The message sent to the log.
     * @param displayPriority The {@link TelemetryPacket#displayPriority} of the telemetry entry.
     * */
    public void logCritical(String caption, int displayPriority) {
        caption = "CRITICAL ERROR: " + caption;
        packetBuffer.add(new TelemetryPacket(caption, displayPriority));
    }

    /** Adds an error message to the telemetry log if {@link TelemetryPlus#verbosityLevel} is at
     * least {@link TelemetryPlus.VerbosityLevel#ERROR}.
     * @param caption The message sent to the log.
     * @param displayPriority The {@link TelemetryPacket#displayPriority} of the telemetry entry.
     * */
    public void logError(String caption, int displayPriority) {
        if (this.verbosityLevel.ordinal() >= TelemetryPlus.VerbosityLevel.ERROR.ordinal()) {
            caption = "ERROR: " + caption;
            packetBuffer.add(new TelemetryPacket(caption, displayPriority));
        }
    }

    /** Adds a warning message to the telemetry log if {@link TelemetryPlus#verbosityLevel} is at
     * least {@link TelemetryPlus.VerbosityLevel#WARNING}
     * @param caption The message sent to the log.
     * @param displayPriority The {@link TelemetryPacket#displayPriority} of the telemetry entry.
     * */
    public void logWarning(String caption, int displayPriority) {
        if (this.verbosityLevel.ordinal() >= TelemetryPlus.VerbosityLevel.WARNING.ordinal()) {
            caption = "WARNING: " + caption;
            packetBuffer.add(new TelemetryPacket(caption, displayPriority));
        }
    }

    /** Adds data to the telemetry log if {@link TelemetryPlus#verbosityLevel} is at
     * least {@link TelemetryPlus.VerbosityLevel#COMPETITION}.
     * @param caption The caption to use.
     * @param format The string by which the arguments are to be formatted.
     * @param displayPriority The {@link TelemetryPacket#displayPriority} of the telemetry entry.
     * @param args The arguments to format.
     * */
    public void logCompetition(String caption, String format, int displayPriority, Object... args) {
        if (this.verbosityLevel.ordinal() >= TelemetryPlus.VerbosityLevel.COMPETITION.ordinal()) {
            packetBuffer.add(new TelemetryPacket(caption, format, displayPriority, args));
        }
    }

    /** Adds data to the telemetry log if {@link TelemetryPlus#verbosityLevel} is at
     * least {@link TelemetryPlus.VerbosityLevel#COMPETITION}.
     * @param caption The caption to use.
     * @param value The value to display.
     * @param displayPriority The {@link TelemetryPacket#displayPriority} of the telemetry entry.
     * */
    public void logCompetition(String caption, Object value, int displayPriority) {
        if (this.verbosityLevel.ordinal() >= TelemetryPlus.VerbosityLevel.COMPETITION.ordinal()) {
            packetBuffer.add(new TelemetryPacket(caption, value, displayPriority));
        }
    }

    /** Adds a message to the telemetry log if {@link TelemetryPlus#verbosityLevel} is at
     * least {@link TelemetryPlus.VerbosityLevel#COMPETITION}.
     * @param caption The message to use.
     * @param displayPriority The {@link TelemetryPacket#displayPriority} of the telemetry entry.
     * */
    public void logCompetition(String caption, int displayPriority) {
        if (this.verbosityLevel.ordinal() >= TelemetryPlus.VerbosityLevel.COMPETITION.ordinal()) {
            packetBuffer.add(new TelemetryPacket(caption, displayPriority));
        }
    }

    /** Adds data to the telemetry log if {@link TelemetryPlus#verbosityLevel} is at
     * least {@link TelemetryPlus.VerbosityLevel#VERBOSE}.
     * @param caption The caption to use.
     * @param format The string by which the arguments are to be formatted.
     * @param displayPriority The {@link TelemetryPacket#displayPriority} of the telemetry entry.
     * @param args The arguments to format.
     * */
    public void logVerbose(String caption, String format, int displayPriority, Object... args) {
        if (this.verbosityLevel.ordinal() >= TelemetryPlus.VerbosityLevel.VERBOSE.ordinal()) {
            packetBuffer.add(new TelemetryPacket(caption, format, displayPriority, args));
        }
    }

    /** Adds data to the telemetry log if {@link TelemetryPlus#verbosityLevel} is at
     * least {@link TelemetryPlus.VerbosityLevel#VERBOSE}.
     * @param caption The caption to use.
     * @param value The value to display.
     * @param displayPriority The {@link TelemetryPacket#displayPriority} of the telemetry entry.
     * */
    public void logVerbose(String caption, Object value, int displayPriority) {
        if (this.verbosityLevel.ordinal() >= TelemetryPlus.VerbosityLevel.VERBOSE.ordinal()) {
            packetBuffer.add(new TelemetryPacket(caption, value, displayPriority));
        }
    }

    /** Adds a message to the telemetry log if {@link TelemetryPlus#verbosityLevel} is at
     * least {@link TelemetryPlus.VerbosityLevel#VERBOSE}.
     * @param caption The message to use.
     * @param displayPriority The {@link TelemetryPacket#displayPriority} of the telemetry entry.
     * */
    public void logVerbose(String caption, int displayPriority) {
        if (this.verbosityLevel.ordinal() >= TelemetryPlus.VerbosityLevel.VERBOSE.ordinal()) {
            packetBuffer.add(new TelemetryPacket(caption, displayPriority));
        }
    }

    /** Adds data to the telemetry log if {@link TelemetryPlus#verbosityLevel} is at
     * least {@link TelemetryPlus.VerbosityLevel#DEBUG}.
     * @param caption The caption to use.
     * @param format The string by which the arguments are to be formatted.
     * @param displayPriority The {@link TelemetryPacket#displayPriority} of the telemetry entry.
     * @param args The arguments to format.
     * */
    public void logDebug(String caption, String format, int displayPriority, Object... args) {
        if (this.verbosityLevel.ordinal() >= TelemetryPlus.VerbosityLevel.DEBUG.ordinal()) {
            packetBuffer.add(new TelemetryPacket(caption, format, displayPriority, args));
        }
    }

    /** Adds data to the telemetry log if {@link TelemetryPlus#verbosityLevel} is at
     * least {@link TelemetryPlus.VerbosityLevel#DEBUG}.
     * @param caption The caption to use.
     * @param value The value to display.
     * @param displayPriority The {@link TelemetryPacket#displayPriority} of the telemetry entry.
     * */
    public void logDebug(String caption, Object value, int displayPriority) {
        if (this.verbosityLevel.ordinal() >= TelemetryPlus.VerbosityLevel.DEBUG.ordinal()) {
            packetBuffer.add(new TelemetryPacket(caption, value, displayPriority));
        }
    }

    /** Adds a message to the telemetry log if {@link TelemetryPlus#verbosityLevel} is at
     * least {@link TelemetryPlus.VerbosityLevel#DEBUG}.
     * @param caption The message to use.
     * @param displayPriority The {@link TelemetryPacket#displayPriority} of the telemetry entry.
     * */
    public void logDebug(String caption, int displayPriority) {
        if (this.verbosityLevel.ordinal() >= TelemetryPlus.VerbosityLevel.DEBUG.ordinal()) {
            packetBuffer.add(new TelemetryPacket(caption, displayPriority));
        }
    }
}