package org.firstinspires.ftc.teamcode.lessons;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;


import org.firstinspires.ftc.teamcode.base.OpModeHarness;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.junit.After;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * One test per lesson: what the robot should do once the student's code is
 * right. Each of these FAILS against the starter file and PASSES against the
 * finished lesson -- so a student knows when they're done.
 */
public class LessonsTest {

    private static final PoseFactory POSES = PoseFactory.degrees();
    private static final double EPS = 1e-6;

    @After
    public void tearDown() {
        OpModeHarness.restoreFactories();
        Scheduler.reset();
    }

    /** A number the lesson sent to Panels, whatever its Java type was. */
    private static double number(Map<String, Object> values, String key) {
        Object v = values.get(key);
        assertTrue("nothing was logged as \"" + key + "\"; got " + values.keySet(), v instanceof Number);
        return ((Number) v).doubleValue();
    }

    // -------------------------------------------------------------- L2

    @Test
    public void l2_logsEveryStickAndTheAButton() {
        OpModeHarness h = new OpModeHarness(new L2Sticks());
        h.init();
        h.start();
        h.gamepad1.left_stick_y = -1.0f;      // pushed away from the driver
        h.gamepad1.right_stick_x = 0.5f;
        h.gamepad1.a = true;
        h.loop();
        h.gamepad1.a = false;
        h.loop();
        h.stop();

        Map<String, Object> values = ((L2Sticks) h.opMode()).values();
        assertEquals("forward is positive after negating the stick",
                1.0, number(values, "stick/leftY"), EPS);
        assertEquals(0.5, number(values, "stick/rightX"), EPS);
        assertTrue(values.containsKey("stick/leftX"));
        assertTrue(values.containsKey("stick/rightY"));
        assertTrue("pressing A sends something to Panels: " + values.keySet(),
                values.keySet().stream().anyMatch(k -> k.contains("pressed A")));
    }

    // -------------------------------------------------------------- L3

    @Test
    public void l3_tankDrivesStraightAndTurns() {
        OpModeHarness h = new OpModeHarness(new L3Tank());
        h.init();
        h.start();

        h.gamepad1.left_stick_y = -1.0f;      // both sticks forward
        h.gamepad1.right_stick_y = -1.0f;
        h.loop();
        assertEquals(1.0, h.forward(), EPS);
        assertEquals("tank never strafes", 0.0, h.strafe(), EPS);
        assertEquals(0.0, h.turn(), EPS);

        h.gamepad1.right_stick_y = 1.0f;      // sticks opposed
        h.loop();
        assertEquals(0.0, h.forward(), EPS);
        assertEquals(1.0, h.turn(), EPS);
        h.stop();
    }

    // -------------------------------------------------------------- L4

    @Test
    public void l4_arcadeUsesOneStickToDriveAndOneToTurn() {
        OpModeHarness h = new OpModeHarness(new L4Arcade());
        h.init();
        h.start();
        h.gamepad1.left_stick_y = -0.8f;
        h.gamepad1.right_stick_x = 0.3f;
        h.loop();
        assertEquals(0.8, h.forward(), EPS);
        assertEquals(0.3, h.turn(), EPS);
        assertEquals("arcade at this stage still cannot strafe", 0.0, h.strafe(), EPS);
        h.stop();
    }

    // -------------------------------------------------------------- L5

    @Test
    public void l5_holonomicCanStrafe() {
        OpModeHarness h = new OpModeHarness(new L5Holonomic());
        h.init();
        h.start();
        h.gamepad1.left_stick_x = -1.0f;      // stick pushed left
        h.loop();
        assertEquals("now it strafes", 1.0, h.strafe(), EPS);
        assertEquals(0.0, h.forward(), EPS);
        h.stop();
    }

    // -------------------------------------------------------------- L8

    @Test
    public void l8_theEncoderLocalizerRunsAlongsideAndIsLogged() {
        OpModeHarness h = new OpModeHarness(new L8CompareLocalizers());
        h.init();
        h.start();
        h.loop();
        // 45 inches of wheel travel on every wheel = 45 inches forward
        h.setWheelTicks(2025, 2025, 2025, 2025);
        h.loop();
        h.stop();

        Map<String, Object> values = ((L8CompareLocalizers) h.opMode()).values();
        assertTrue("the shadow localizer is logged: " + values.keySet(),
                values.containsKey("Localizer/encoders/x_in"));
        assertEquals("the shadow must not steer the robot", 0.0, h.forward(), EPS);
        assertNotEquals("it moved in its own estimate",
                0.0, number(values, "Localizer/encoders/x_in"), 0.1);
    }

    // -------------------------------------------------------------- L9

    @Test
    public void l9_autoDrives24InchesForwardAndStops() {
        OpModeHarness h = new OpModeHarness(new L9Drive24());
        Follower follower = h.robot.follower;
        h.init();
        assertEquals("placed at the start pose", 72.0, follower.pose().x(), EPS);
        h.start();
        runUntilDone(h, follower, 3.0);
        h.stop();

        assertEquals("ends 24 inches further along x", 96.0, follower.pose().x(), 1.0);
        assertEquals("and does not wander in y", 72.0, follower.pose().y(), 1.0);
    }

    // -------------------------------------------------------------- L10

    @Test
    public void l10_autoDrivesTwoLegsAndEndsTurned() {
        OpModeHarness h = new OpModeHarness(new L10PathWithTurn());
        Follower follower = h.robot.follower;
        h.init();
        h.start();
        runUntilDone(h, follower, 6.0);
        h.stop();

        Pose end = follower.pose();
        assertEquals(96.0, end.x(), 2.0);
        assertEquals(96.0, end.y(), 2.0);
        assertEquals("finishes facing +y", 90.0, Math.toDegrees(end.heading()), 15.0);
    }

    // -------------------------------------------------------------- L11

    @Test
    public void l11_fieldRelativeIgnoresWhichWayTheRobotFaces() {
        OpModeHarness h = new OpModeHarness(new L11FieldRelative());
        Follower follower = h.robot.follower;
        h.init();
        h.start();

        follower.setPose(POSES.of(0, 0, 0));
        h.gamepad1.left_stick_y = -1.0f;          // away from the driver
        h.loop();
        assertEquals(1.0, h.forward(), 1e-3);

        follower.setPose(POSES.of(0, 0, 90));     // robot now faces +y
        h.loop();
        assertEquals("same stick, still moves away from the driver",
                0.0, h.forward(), 1e-3);
        assertEquals(-1.0, h.strafe(), 1e-3);
        h.stop();
    }

    // -------------------------------------------------------------- L12

    @Test
    public void l12_theBumperSwitchesToRobotRelative() {
        OpModeHarness h = new OpModeHarness(new L12RobotRelativeButton());
        Follower follower = h.robot.follower;
        h.init();
        h.start();
        follower.setPose(POSES.of(0, 0, 90));
        h.gamepad1.left_stick_y = -1.0f;

        h.loop();
        assertEquals("field relative by default", 0.0, h.forward(), 1e-3);

        h.gamepad1.right_bumper = true;
        h.loop();
        assertEquals("robot relative while held", 1.0, h.forward(), 1e-3);

        h.gamepad1.right_bumper = false;
        h.loop();
        assertEquals("and back again on release", 0.0, h.forward(), 1e-3);
        h.stop();
    }

    // -------------------------------------------------------------- L13

    @Test
    public void l13_theRobotHoldsItsHeadingWhenTheStickIsReleased() {
        OpModeHarness h = new OpModeHarness(new L13HeadingHoldTeleOp());
        Follower follower = h.robot.follower;
        h.init();
        h.start();

        follower.setPose(POSES.of(0, 0, 0));
        h.gamepad1.right_stick_x = 0.5f;
        h.loop();
        assertEquals("while steering, the stick wins", 0.5, h.turn(), 1e-3);

        h.gamepad1.right_stick_x = 0.0f;
        h.loop();                                  // releases: captures heading 0
        assertEquals("on target, no correction", 0.0, h.turn(), 1e-3);

        follower.setPose(POSES.of(0, 0, -10));     // the robot drifts
        h.loop();
        assertTrue("it steers back", h.turn() > 0.01);
        h.stop();
    }

    // -------------------------------------------------------------- L14

    @Test
    public void l14_pressingYDrivesToAPoseAndTheDriverCanTakeOver() {
        OpModeHarness h = new OpModeHarness(new L14DriveToPose());
        Follower follower = h.robot.follower;
        h.init();
        h.start();
        follower.setPose(POSES.of(72, 72, 0));

        h.gamepad1.y = true;
        h.loop();
        h.gamepad1.y = false;
        h.loop();
        assertEquals("the command is driving, not the sticks",
                Follower.Mode.HOLD, follower.mode());

        h.gamepad1.left_stick_y = -1.0f;           // the driver grabs the stick
        h.loop();
        assertEquals("manual control returns", Follower.Mode.MANUAL, follower.mode());
        assertEquals(1.0, h.forward(), 1e-3);
        h.stop();
    }

    // -------------------------------------------------------------- L15

    @Test
    public void l15_everythingTogether() {
        OpModeHarness h = new OpModeHarness(new L15Combined());
        Follower follower = h.robot.follower;
        h.init();
        h.start();
        follower.setPose(POSES.of(72, 72, 90));

        h.gamepad1.left_stick_y = -1.0f;
        h.loop();
        assertEquals("field relative with nothing held", 0.0, h.forward(), 1e-3);

        h.gamepad1.right_bumper = true;
        h.loop();
        assertEquals("robot relative on the bumper", 1.0, h.forward(), 1e-3);
        h.gamepad1.right_bumper = false;

        h.gamepad1.left_stick_y = 0.0f;
        h.gamepad1.y = true;
        h.loop();
        h.gamepad1.y = false;
        h.loop();
        assertEquals("Y hands the robot to the follower", Follower.Mode.HOLD, follower.mode());
        h.stop();

        assertTrue("the shadow localizer is still running",
                ((L15Combined) h.opMode()).values().containsKey("Localizer/encoders/x_in"));
    }

    @Test
    public void l15_aPointsAt45DegreesWhileTheDriverKeepsDriving() {
        OpModeHarness h = new OpModeHarness(new L15Combined());
        Follower follower = h.robot.follower;
        h.init();
        h.start();
        follower.setPose(POSES.of(72, 72, 0));       // facing 0, wants 45

        h.gamepad1.a = true;
        h.loop();
        h.gamepad1.a = false;
        h.gamepad1.left_stick_y = -1.0f;             // still translating
        h.loop();

        L15Combined opMode = (L15Combined) h.opMode();
        assertEquals("aiming", true, opMode.values().get("drive/aiming"));
        assertEquals("at 45 degrees", 45.0, (Double) opMode.values().get("drive/target_deg"), 1e-6);
        assertTrue("turning toward it", h.turn() > 0);
        assertTrue("and still driving", Math.abs(h.forward()) + Math.abs(h.strafe()) > 0);
    }

    @Test
    public void l15_theTurnStickTakesAimingBack() {
        OpModeHarness h = new OpModeHarness(new L15Combined());
        h.robot.follower.setPose(POSES.of(72, 72, 0));
        h.init();
        h.start();

        h.gamepad1.a = true;
        h.loop();
        h.gamepad1.a = false;
        h.gamepad1.right_stick_x = 0.8f;             // the driver steers
        h.loop();

        assertEquals("the stick wins", 0.8, h.turn(), 1e-3);
        assertEquals("not aiming any more", false,
                ((L15Combined) h.opMode()).values().get("drive/aiming"));
    }

    @Test
    public void l15_yDrivesToTheStatedPoseAndAStickTakesItBack() {
        OpModeHarness h = new OpModeHarness(new L15Combined());
        Follower follower = h.robot.follower;
        h.init();
        h.start();
        follower.setPose(POSES.of(72, 72, 90));

        h.gamepad1.y = true;
        h.loop();
        h.gamepad1.y = false;
        h.loop();
        assertEquals(Follower.Mode.HOLD, follower.mode());
        assertEquals("AUTO", ((L15Combined) h.opMode()).values().get("drive/mode"));
        // Pedro normalises headings to [0, 360), so -45 comes back as 315.
        assertEquals("the pose it was told to go to", 315.0,
                (Double) ((L15Combined) h.opMode()).values().get("drive/target_deg"), 1e-6);

        h.gamepad1.left_stick_y = -1.0f;
        h.loop();
        assertEquals("a stick takes it back", "FIELD",
                ((L15Combined) h.opMode()).values().get("drive/mode"));
    }

    @Test
    public void l16_theSticksCommandASpeedAndTheWheelsAreCorrectedTowardsIt() {
        OpModeHarness h = new OpModeHarness(new L16VelocityDrive());
        h.init();
        h.start();

        h.gamepad1.left_stick_y = -1.0f;            // full forward
        h.loop();
        L16VelocityDrive opMode = (L16VelocityDrive) h.opMode();

        assertEquals("full stick asks for a speed, in inches per second",
                40.0, (Double) opMode.values().get("command/forward_ips"), 1e-6);
        assertEquals("and every wheel must travel at it",
                40.0, (Double) opMode.values().get("wheel/frontLeft/target_ips"), 1e-6);
        assertEquals("the wheels are not moving yet, so the error is the whole target",
                40.0, (Double) opMode.values().get("wheel/frontLeft/error_ips"), 1e-6);

        // the measured feedforward for 40 in/s, plus the feedback on a 40 in/s error
        assertEquals(Constants.powerPerInchPerSecond * 40 + 0.008 * 40,
                (Double) opMode.values().get("wheel/frontLeft/power"), 1e-6);
    }

    @Test
    public void l16_whenTheWheelsAreUpToSpeedOnlyTheFeedforwardRemains() {
        OpModeHarness h = new OpModeHarness(new L16VelocityDrive());
        h.init();
        h.start();
        // 40 in/s at the measured ticks per inch
        int ticks = (int) Math.round(40 * Constants.ticksPerInch);
        h.velocities(ticks, ticks, ticks, ticks);

        h.gamepad1.left_stick_y = -1.0f;
        h.loop();
        L16VelocityDrive opMode = (L16VelocityDrive) h.opMode();

        assertEquals("measured speed matches the command", 40.0,
                (Double) opMode.values().get("wheel/frontLeft/actual_ips"), 1e-6);
        assertEquals("so no correction is needed", 0.0,
                (Double) opMode.values().get("wheel/frontLeft/error_ips"), 1e-6);
        assertEquals("and the power is the feedforward alone",
                Constants.powerPerInchPerSecond * 40,
                (Double) opMode.values().get("wheel/frontLeft/power"), 1e-6);
    }

    @Test
    public void l16_turningAskesEachSideForOppositeSpeeds() {
        OpModeHarness h = new OpModeHarness(new L16VelocityDrive());
        h.init();
        h.start();
        h.gamepad1.right_stick_x = -1.0f;           // full counter-clockwise
        h.loop();
        L16VelocityDrive opMode = (L16VelocityDrive) h.opMode();

        double left = (Double) opMode.values().get("wheel/frontLeft/target_ips");
        double right = (Double) opMode.values().get("wheel/frontRight/target_ips");
        assertEquals("opposite", -left, right, 1e-6);
        assertTrue("turning counter-clockwise drives the left side backwards", left < 0);
    }

    // ---------------------------------------------------------- helpers

    /** Runs loops until the follower stops following, or the time runs out. */
    private static void runUntilDone(OpModeHarness h, Follower follower, double seconds) {
        long deadline = System.nanoTime() + (long) (seconds * 1e9);
        boolean followed = false;
        while (System.nanoTime() < deadline) {
            h.loop();
            if (follower.mode() == Follower.Mode.FOLLOW) followed = true;
            if (followed && follower.mode() != Follower.Mode.FOLLOW && follower.currentPath() == null) {
                h.loop();
                break;
            }
            OpModeHarness.sleep(5);
        }
        assertTrue("the robot never started following a path", followed);
    }
}
