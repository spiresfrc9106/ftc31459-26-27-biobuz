package org.firstinspires.ftc.teamcode.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;

import org.junit.Test;

import java.io.File;

/**
 * Every run writes a WPILOG file beside the live Panels view. These check that
 * it happens, that it carries what a lesson logged, and that it is closed.
 */
public class FlightLogTest {

    private static final PoseFactory POSES = PoseFactory.degrees();

    /** A lesson that logs one of everything. */
    public static class SampleTeleOp extends CorbelsTeleOp {
        @Override
        protected void drive() {
            data("number", 1.5);
            data("flag", true);
            data("text", "hello");
            data("Robot/Pose", POSES.of(24, 48, 90));
        }
    }

    public static class SampleAuto extends CorbelsAuto {
        @Override protected Pose startPose() { return POSES.of(12, 12, 0); }
        @Override protected com.pedropathing.ivy.Command routine() {
            return com.pedropathing.ivy.commands.Commands.instant(() -> { });
        }
    }

    @Test
    public void aTeleopRunWritesOneFlightLog() {
        OpModeHarness h = new OpModeHarness(new SampleTeleOp());
        assertEquals("nothing before init", 0, h.logs().length);
        h.init();
        assertEquals("a file from init, so setting-up values are kept", 1, h.logs().length);

        h.start();
        assertEquals("and still just the one", 1, h.logs().length);
        SampleTeleOp opMode = (SampleTeleOp) h.opMode();
        assertTrue(opMode.flight.status(), opMode.flight.isRecording());

        h.loops(5, 0);
        h.stop();
        assertFalse("closed when the OpMode ends", opMode.flight.isRecording());
        assertTrue("and has something in it", h.logs()[0].length() > 0);
    }

    @Test
    public void anAutoWritesOneToo() {
        OpModeHarness h = new OpModeHarness(new SampleAuto());
        h.init();
        h.start();
        h.loops(3, 0);
        h.stop();
        assertEquals(1, h.logs().length);
        assertFalse(((SampleAuto) h.opMode()).flight.isRecording());
    }

    @Test
    public void whatALessonLogsGoesToPanelsAndTheFileBoth() {
        OpModeHarness h = new OpModeHarness(new SampleTeleOp());
        h.init();
        h.start();
        h.loop();
        SampleTeleOp opMode = (SampleTeleOp) h.opMode();

        // Panels, as before.
        assertEquals(1.5, (Double) opMode.values().get("number"), 1e-9);
        assertEquals(true, opMode.values().get("flag"));
        assertEquals("hello", opMode.values().get("text"));
        assertEquals(24.0, (Double) opMode.values().get("Robot/Pose/x_in"), 1e-9);

        h.stop();

        // And the file, which by now is closed and readable.
        File file = h.logs()[0];
        assertTrue("the file has content", file.length() > 200);
    }

    @Test
    public void somethingLoggedDuringInitIsKept() {
        OpModeHarness h = new OpModeHarness(new InitLogging());
        h.init();
        InitLogging opMode = (InitLogging) h.opMode();
        assertEquals("init values reach the file before Panels exists",
                7.5, (Double) opMode.values().get("setup/value"), 1e-9);
        h.start();
        h.stop();
        assertTrue(h.logs()[0].length() > 0);
    }

    /** Logs during init, when Panels does not exist yet. */
    public static class InitLogging extends CorbelsTeleOp {
        @Override protected void onInit() { data("setup/value", 7.5); }
        @Override protected void drive() { }
    }

    @Test
    public void theFileNameSaysWhichOpModeItWas() {
        OpModeHarness h = new OpModeHarness(new SampleTeleOp());
        h.init();
        h.start();
        h.stop();
        assertTrue(h.logs()[0].getName(), h.logs()[0].getName().startsWith("SampleTeleOp-"));
    }
}
