package org.firstinspires.ftc.teamcode.base;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.panels.PanelsLogger;

/**
 * Everything an autonomous needs, so a lesson is just its routine.
 *
 * <p>A lesson subclass supplies {@link #startPose()} -- where the robot is
 * placed -- and {@link #routine()}, the commands to run.
 */
public abstract class CorbelsAuto extends OpMode {

    protected Follower follower;
    protected PanelsLogger log;
    protected Shadow shadow;
    protected TelemetryManager panels;

    private long loops;

    /** Where the robot is placed before the match. */
    protected abstract Pose startPose();

    /** What the robot should do. */
    protected abstract Command routine();

    /** Extra localizers to watch. Optional. */
    protected void shadows() {
    }

    protected void data(String key, double value) {
        panels.addData(key, value);
    }

    @Override
    public final void init() {
        Scheduler.reset();
        follower = RobotFactory.follower.apply(hardwareMap);
        follower.setPose(startPose());
        follower.update();
        telemetry.addLine("Panels: http://192.168.43.1:8001");
        telemetry.update();
    }

    @Override
    public final void start() {
        panels = PanelsTelemetry.INSTANCE.getTelemetry();
        log = new PanelsLogger();
        log.start();
        shadow = new Shadow();
        loops = 0;
        shadows();
        shadow.setPose(follower.pose());
        Scheduler.schedule(routine());
    }

    @Override
    public final void loop() {
        loops++;
        follower.update();
        Scheduler.execute();
        shadow.update(this::data);
        telemetry.addData("Pose", describe(follower.pose()));
        log.update(follower, telemetry);
    }

    @Override
    public final void stop() {
        follower.manual(0, 0, 0);
        follower.update();
    }

    protected static String describe(Pose p) {
        return String.format("x %.1f  y %.1f  h %.0f deg", p.x(), p.y(), Math.toDegrees(p.heading()));
    }

    public long loops() {
        return loops;
    }
}
