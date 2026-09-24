package org.firstinspires.ftc.teamcode.base;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.panels.PanelsLogger;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * What every Corbels OpMode does, whether a driver is holding the controller or
 * not: find the hardware, make the follower, run the scheduler, keep Panels and
 * the Driver Station fed.
 *
 * <p>Teleops and autos differ in four places, and those are the hooks below:
 * {@link #onInit}, {@link #onStart}, {@link #onLoop} and {@link #afterLoop}.
 * {@link CorbelsTeleOp} and {@link CorbelsAuto} fill them in; a lesson extends
 * one of those, not this.
 *
 * <p>The lifecycle methods are final on purpose. Everything a lesson needs to
 * change has a hook, and an OpMode that forgets to call {@code super.init()} is
 * a bad afternoon.
 */
public abstract class CorbelsOpMode extends OpMode {

    /** Every device on the robot, resolved in init(). */
    protected RobotHardware hardware;

    protected Follower follower;
    protected PanelsLogger log;
    protected Shadow shadow;

    /** Panels' telemetry, if a lesson wants it directly. */
    protected TelemetryManager panels;

    private final Map<String, Object> values = new LinkedHashMap<>();
    private long loops;

    // ------------------------------------------------------------ hooks

    /** After the hardware and follower exist, before the first update. */
    protected void onInit() {
    }

    /** Once, when the OpMode starts, before {@link #shadows()}. */
    protected void onStart() {
    }

    /** Every loop, before the follower updates. */
    protected void onLoop() {
    }

    /** Every loop, after the scheduler and the shadow localizers. */
    protected void afterLoop() {
    }

    /** Extra localizers to watch. Runs once, when the OpMode starts. */
    protected void shadows() {
    }

    // ------------------------------------------------------------ logging

    /**
     * Sends a number to Panels, where it appears as telemetry and can be
     * plotted on a graph.
     */
    protected void data(String key, double value) {
        panels.addData(key, value);
        values.put(key, value);
    }

    protected void data(String key, boolean value) {
        panels.addData(key, value);
        values.put(key, value);
    }

    protected void data(String key, String value) {
        panels.addData(key, value);
        values.put(key, value);
    }

    /** A pose, as three graphable numbers: key/x_in, key/y_in, key/heading_deg. */
    protected void data(String key, Pose pose) {
        if (pose == null) return;
        data(key + "/x_in", pose.x());
        data(key + "/y_in", pose.y());
        data(key + "/heading_deg", Math.toDegrees(pose.heading()));
    }

    /** What was last sent, for tests and for the Driver Station. */
    public Map<String, Object> values() {
        return values;
    }

    public long loops() {
        return loops;
    }

    protected static String describe(Pose p) {
        return String.format("x %.1f  y %.1f  h %.0f deg", p.x(), p.y(), Math.toDegrees(p.heading()));
    }

    // ------------------------------------------------------------ lifecycle

    @Override
    public final void init() {
        // Every device, looked up once, before the match starts. A name that
        // doesn't match the configuration fails here, where it can be read --
        // not halfway through a match.
        hardware = RobotFactory.hardware.apply(hardwareMap);
        follower = RobotFactory.follower.apply(hardwareMap);
        onInit();
        follower.update();
        telemetry.addLine("Panels: http://192.168.43.1:8001");
        telemetry.update();
    }

    @Override
    public final void start() {
        Scheduler.reset();
        panels = PanelsTelemetry.INSTANCE.getTelemetry();
        log = new PanelsLogger();
        follower = follower.withLogger(followerLog -> log.pedro(followerLog.toString()));
        log.start();
        shadow = new Shadow();
        loops = 0;
        onStart();
        shadows();
        shadow.setPose(follower.pose());
    }

    @Override
    public final void loop() {
        loops++;
        onLoop();
        follower.update();
        Scheduler.execute();
        shadow.update(this::data);
        afterLoop();
        // PanelsLogger does the rest: loop timing, pose, mode, velocity, the
        // field drawing, and one update() that flushes Panels and the
        // Driver Station together.
        log.update(follower, telemetry);
    }

    @Override
    public final void stop() {
        follower.manual(0, 0, 0);
        follower.update();
    }
}
