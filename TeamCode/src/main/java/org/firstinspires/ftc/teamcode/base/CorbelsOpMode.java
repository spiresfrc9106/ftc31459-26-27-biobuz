package org.firstinspires.ftc.teamcode.base;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import io.github.mikestitt.corbelsflightlog.FlightLog;
import io.github.mikestitt.corbelsflightlog.ftc.FtcFlightLog;
import io.github.mikestitt.corbelsflightlog.pedro.PedroFlightLog;

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
 * <p>Every run also writes a WPILOG file, openable in AdvantageScope
 * afterwards: everything sent to {@link #data}, the robot's pose and path from
 * Pedro, and the shadow localizers. It opens at init, so setting-up values are
 * in it as well as the run. Panels shows the run live; the file keeps
 * it. Files land in {@code /sdcard/corbelsflightlog} and can be downloaded from
 * {@code http://192.168.43.1:8080/corbelsflightlog}.
 *
 * <p>The lifecycle methods are final on purpose. Everything a lesson needs to
 * change has a hook, and an OpMode that forgets to call {@code super.init()} is
 * a bad afternoon.
 */
public abstract class CorbelsOpMode extends OpMode {

    /** Every device on the robot, resolved in init(). */
    protected RobotHardware hardware;

    protected Follower follower;

    /**
     * The drivetrain, for a lesson that drives the wheels itself. The follower
     * drives it every update; see {@link CorbelsMecanum#driveWheels}.
     */
    protected CorbelsMecanum drivetrain;
    protected PanelsLogger log;

    /**
     * The run's WPILOG file, for opening in AdvantageScope afterwards. Panels
     * shows what is happening now; this keeps what happened. Everything sent to
     * {@link #data} goes to both.
     */
    protected FlightLog flight;

    private PedroFlightLog pedro;
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
        if (panels != null) panels.addData(key, value);   // null until start
        values.put(key, value);
        if (flight != null) flight.recordOutput(key, value);
    }

    protected void data(String key, boolean value) {
        if (panels != null) panels.addData(key, value);   // null until start
        values.put(key, value);
        if (flight != null) flight.recordOutput(key, value);
    }

    protected void data(String key, String value) {
        if (panels != null) panels.addData(key, value);   // null until start
        values.put(key, value);
        if (flight != null) flight.recordOutput(key, value);
    }

    /** A pose, as three graphable numbers: key/x_in, key/y_in, key/heading_deg. */
    protected void data(String key, Pose pose) {
        if (pose == null) return;
        // As a struct too, so AdvantageScope can draw it on the field rather
        // than only graph the three numbers.
        if (flight != null) PedroFlightLog.recordOutput(flight, key, pose);
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
        // From init, so anything logged while setting up -- a starting pose, a
        // sensor reading, a configuration problem -- is in the file too. The
        // Robot Controller closes it if the OpMode never runs.
        flight = FtcFlightLog.open(this);
        pedro = new PedroFlightLog(flight, "Robot");
        drivetrain = RobotFactory.drivetrain.apply(hardware);
        follower = RobotFactory.follower.apply(hardwareMap, drivetrain);
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
        if (pedro != null) pedro.record(follower);
        if (flight != null) flight.endLoop();
        // PanelsLogger does the rest: loop timing, pose, mode, velocity, the
        // field drawing, and one update() that flushes Panels and the
        // Driver Station together.
        log.update(follower, telemetry);
    }

    @Override
    public final void stop() {
        follower.manual(0, 0, 0);
        follower.update();
        if (flight != null) flight.close();
    }
}
