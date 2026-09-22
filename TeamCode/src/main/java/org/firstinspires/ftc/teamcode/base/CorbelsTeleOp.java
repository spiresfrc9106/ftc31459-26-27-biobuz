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
 * Everything a teleop needs, so a lesson can be a few lines.
 *
 * <p>A lesson subclass implements {@link #drive()} -- the stick code -- and may
 * override {@link #bindings()} to connect buttons to commands. The follower,
 * the Ivy scheduler and Panels are handled here.
 *
 * <p>Logging is Panels, live: {@link PanelsLogger} draws the robot, its aim
 * point, the path and a trail on the Panels field and sends the loop timing,
 * pose and velocity as telemetry. Values a lesson logs with {@link #data} go
 * out with them.
 */
public abstract class CorbelsTeleOp extends OpMode {

    protected Follower follower;
    protected PanelsLogger log;
    protected Buttons buttons;
    protected Shadow shadow;

    /** Panels' telemetry, if a lesson wants it directly. */
    protected TelemetryManager panels;

    private final Map<String, Object> values = new LinkedHashMap<>();
    private long loops;

    /** Stick code. Runs every loop, before the scheduler. */
    protected abstract void drive();

    /** Connect buttons to commands. Runs once, when the OpMode starts. */
    protected void bindings() {
    }

    /** Extra localizers to watch. Runs once, when the OpMode starts. */
    protected void shadows() {
    }

    /**
     * Sends a number to Panels, where it appears as telemetry and can be
     * plotted on a graph. Call it from {@link #drive()}.
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

    @Override
    public final void init() {
        follower = RobotFactory.follower.apply(hardwareMap);
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
        buttons = new Buttons();
        shadow = new Shadow();
        loops = 0;
        bindings();
        shadows();
        shadow.setPose(follower.pose());
    }

    @Override
    public final void loop() {
        loops++;
        buttons.poll();
        drive();
        follower.update();
        Scheduler.execute();
        shadow.update(this::data);
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

    public long loops() {
        return loops;
    }
}
