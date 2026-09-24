package org.firstinspires.ftc.teamcode.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.math.Pose;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Teleops and autos now share one parent. These check that both halves still
 * get what they used to: hardware at init, the scheduler, the shadow
 * localizers, Panels, and a follower that is stopped at the end.
 */
public class SharedLifecycleTest {

    private static final PoseFactory POSES = PoseFactory.degrees();

    /** A minimal teleop: records when each hook ran. */
    public static class SampleTeleOp extends CorbelsTeleOp {
        public final List<String> calls = new ArrayList<>();

        @Override protected void bindings() { calls.add("bindings"); }
        @Override protected void shadows() { calls.add("shadows"); }
        @Override protected void drive() {
            calls.add("drive");
            data("teleop/loops", loops());
        }
    }

    /** A minimal auto. */
    public static class SampleAuto extends CorbelsAuto {
        public final List<String> calls = new ArrayList<>();
        public boolean ran;

        @Override protected Pose startPose() {
            calls.add("startPose");
            return POSES.of(24, 24, 45);
        }

        @Override protected Command routine() {
            calls.add("routine");
            return Commands.instant(() -> ran = true);
        }

        @Override protected void shadows() { calls.add("shadows"); }
    }

    @Test
    public void aTeleopGetsHardwareAtInitAndItsHooksInOrder() {
        SampleTeleOp opMode = new SampleTeleOp();
        OpModeHarness h = new OpModeHarness(opMode);

        h.init();
        assertNotNull("hardware resolved at init", opMode.hardware);
        assertEquals("no hooks yet", 0, opMode.calls.size());

        h.start();
        assertEquals("bindings before shadows", "[bindings, shadows]", opMode.calls.toString());
        assertNotNull(opMode.buttons);

        h.loop();
        assertTrue(opMode.calls.contains("drive"));
        assertEquals(1, opMode.loops());
        assertEquals(1.0, (Double) opMode.values().get("teleop/loops"), 1e-9);

        h.stop();
        assertEquals("stopped", 0.0, h.forward(), 1e-9);
    }

    @Test
    public void anAutoGetsTheSameTreatmentPlusItsStartPoseAndRoutine() {
        SampleAuto opMode = new SampleAuto();
        OpModeHarness h = new OpModeHarness(opMode);

        h.init();
        assertNotNull("hardware resolved at init, for autos too", opMode.hardware);
        assertTrue("the start pose is set during init", opMode.calls.contains("startPose"));
        assertEquals("placed where it said", 24.0, h.robot.follower.pose().x(), 1e-6);

        h.start();
        assertTrue(opMode.calls.contains("routine"));
        assertTrue(opMode.calls.contains("shadows"));

        h.loop();
        assertTrue("the scheduler ran the routine", opMode.ran);
        assertEquals(1, opMode.loops());

        h.stop();
        assertEquals(0.0, h.forward(), 1e-9);
    }

    @Test
    public void bothLookUpTheHardwareExactlyOnce() {
        OpModeHarness teleop = new OpModeHarness(new SampleTeleOp());
        teleop.init();
        teleop.start();
        teleop.loops(5, 0);
        assertEquals("four motors and an IMU", 5, teleop.lookups);

        OpModeHarness auto = new OpModeHarness(new SampleAuto());
        auto.init();
        auto.start();
        auto.loops(5, 0);
        assertEquals("and the same for an auto", 5, auto.lookups);
    }
}
