package org.firstinspires.ftc.teamcode.panels;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Shared Panels logging for the Corbels OpModes.
 *
 * <p><b>How Panels 1.0 graphs.</b> There is no graph() method. The Graph panel
 * parses telemetry text: any line containing {@code name: number} becomes a
 * plottable series. {@code addData(k, v)} writes exactly that format, so every
 * addData value appears in the Panels text panel AND the graph picker. Names
 * may contain anything except a colon.
 *
 * <p>A series appears in the picker only after it has been sent once, in the
 * order it was first sent. The picker is a flat list; the {@code loop/},
 * {@code pose/} and {@code vel/} prefixes are for readability only.
 *
 * <p><b>Sampling.</b> The robot sends to Panels every 75 ms by default and
 * discards lines from the loops in between, so graphs are sampled, not
 * per-loop. With a ~5 ms loop, roughly 1 loop in 15 reaches the graph.
 * {@code loop/max_ms} exists to catch spikes that sampling misses.
 * {@code panels.setUpdateInterval(ms)} lowers the interval at the cost of
 * more Wi-Fi traffic.
 *
 * <p><b>Output split.</b> Panels gets every series; the Driver Station gets a
 * three-line summary. ({@code TelemetryManager.update(telemetry)} would copy
 * every Panels line to the DS, which crowds its screen.) {@link #update}
 * flushes both, so OpModes using this class should not call
 * {@code telemetry.update()} themselves.
 */
public class PanelsLogger {

    /** Panels' telemetry handle. Panels itself is already running inside the
     *  Robot Controller app; there is nothing to start. PanelsTelemetry is a
     *  Kotlin object, hence INSTANCE from Java. */
    private final TelemetryManager panels = PanelsTelemetry.INSTANCE.getTelemetry();

    private long loops;
    private long lastNs;
    private long startNs;
    private double loopMs;
    private double maxLoopMs;

    /** Latest line Pedro pushed through its own logger, if wired up. */
    private String pedroLog = "";

    /** Call once from start(), so init-loop time is not counted. */
    public void start() {
        loops = 0;
        maxLoopMs = 0.0;
        loopMs = 0.0;
        lastNs = System.nanoTime();
        startNs = lastNs;
    }

    /**
     * Sink for Pedro's own follower log. Wire with:
     * {@code Constants.create(hardwareMap).withLogger(log -> panelsLogger.pedro(log.toString()))}
     */
    public void pedro(String line) {
        pedroLog = line;
    }

    /** Call once per loop, after follower.update(). */
    public void update(Follower follower, Telemetry driverStation) {
        // ---- loop counter and loop duration ----
        long now = System.nanoTime();
        if (lastNs != 0L) {
            loopMs = (now - lastNs) / 1_000_000.0;
        }
        lastNs = now;
        loops++;
        if (loopMs > maxLoopMs) {
            maxLoopMs = loopMs;
        }
        double hz = loopMs > 0.0 ? 1000.0 / loopMs : 0.0;
        double upSec = (now - startNs) / 1_000_000_000.0;

        panels.addData("loop/count", loops);
        panels.addData("loop/ms", loopMs);
        panels.addData("loop/max_ms", maxLoopMs);
        panels.addData("loop/hz", hz);
        panels.addData("loop/uptime_s", upSec);

        if (follower != null) {
            // ---- robot position ----
            // Pedro 3 uses follower.pose(), NOT getPose(), and the accessors are
            // x()/y()/heading() -- methods, not fields. Heading is in radians.
            double x = follower.pose().x();
            double y = follower.pose().y();
            double headingDeg = Math.toDegrees(follower.pose().heading());

            panels.addData("pose/x_in", x);
            panels.addData("pose/y_in", y);
            panels.addData("pose/heading_deg", headingDeg);

            // ---- robot velocity ----
            // Three flavours, all useful for different questions:
            //   velocity()           world frame -- where on the field is it going
            //   twist()              body frame  -- forward / strafe from the robot's view
            //   tangentialVelocity() scalar      -- speed along the current path
            // Velocity and Twist expose PUBLIC FIELDS vx/vy/omega, not getters.
            double vx = follower.velocity().vx;
            double vy = follower.velocity().vy;
            double omega = follower.velocity().omega;
            double forward = follower.twist().vx;
            double strafe = follower.twist().vy;
            double tangential = follower.tangentialVelocity();
            double speed = Math.hypot(vx, vy);

            panels.addData("vel/vx_ips", vx);
            panels.addData("vel/vy_ips", vy);
            panels.addData("vel/speed_ips", speed);
            panels.addData("vel/omega_radps", omega);
            panels.addData("vel/forward_ips", forward);
            panels.addData("vel/strafe_ips", strafe);
            panels.addData("vel/tangential_ips", tangential);

            driverStation.addData("Pose", "x %.1f  y %.1f  h %.0f", x, y, headingDeg);
            driverStation.addData("Speed", "%.1f in/s", speed);
        }

        if (!pedroLog.isEmpty()) {
            // Text only -- unless FollowerLog.toString() contains "name: number"
            // pairs, in which case those become extra graph series as well.
            panels.debug("pedro  " + pedroLog);
        }

        driverStation.addData("Loop", "#%d  %.1f ms (max %.1f)", loops, loopMs, maxLoopMs);

        panels.update();         // Panels: every series above (throttled to 75 ms)
        driverStation.update();  // Driver Station: summary lines only
    }

    public long loops() {
        return loops;
    }

    public double lastLoopMs() {
        return loopMs;
    }

    public double maxLoopMs() {
        return maxLoopMs;
    }
}
