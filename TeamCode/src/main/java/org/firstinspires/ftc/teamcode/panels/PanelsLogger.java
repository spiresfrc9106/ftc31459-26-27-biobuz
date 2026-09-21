package org.firstinspires.ftc.teamcode.panels;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Shared Panels logging for the Corbels OpModes.
 *
 * <p>Two kinds of output, and they are separate calls:
 * <ul>
 *   <li>{@code debug(String)} and {@code addData(k, v)} produce TEXT, shown in
 *       the Panels Telemetry panel.</li>
 *   <li>{@code graph(String, double)} produces a PLOTTED SERIES, shown in the
 *       Panels Graph panel. A value only appears in the graph picker after it
 *       has been sent at least once, so run the OpMode before looking for it.</li>
 * </ul>
 *
 * <p>{@code update(telemetry)} flushes to Panels AND the Driver Station in one
 * call, so OpModes using this class should not call {@code telemetry.update()}
 * themselves.
 *
 * <p>Series are named {@code group/name} so Panels groups them in the picker.
 */
public class PanelsLogger {

    /** Panels' telemetry handle. Panels itself is already running inside the
     *  Robot Controller app; there is nothing to start. */
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

        panels.graph("loop/count", (double) loops);
        panels.graph("loop/ms", loopMs);
        panels.graph("loop/hz", hz);

        if (follower != null) {
            // ---- robot position ----
            // Pedro 3 uses follower.pose(), NOT getPose(), and the accessors are
            // x()/y()/heading() -- methods, not fields. Heading is in radians.
            double x = follower.pose().x();
            double y = follower.pose().y();
            double headingDeg = Math.toDegrees(follower.pose().heading());

            panels.graph("pose/x_in", x);
            panels.graph("pose/y_in", y);
            panels.graph("pose/heading_deg", headingDeg);

            // ---- robot velocity ----
            // Three flavours, all useful for different questions:
            //   velocity()          world frame  -- where on the field is it going
            //   twist()             body frame   -- forward / strafe from the robot's view
            //   tangentialVelocity() scalar      -- speed along the current path
            // Velocity and Twist expose PUBLIC FIELDS vx/vy/omega, not getters.
            double vx = follower.velocity().vx;
            double vy = follower.velocity().vy;
            double omega = follower.velocity().omega;
            double forward = follower.twist().vx;
            double strafe = follower.twist().vy;
            double tangential = follower.tangentialVelocity();
            double speed = Math.hypot(vx, vy);

            panels.graph("vel/vx_ips", vx);
            panels.graph("vel/vy_ips", vy);
            panels.graph("vel/speed_ips", speed);
            panels.graph("vel/omega_radps", omega);
            panels.graph("vel/forward_ips", forward);
            panels.graph("vel/strafe_ips", strafe);
            panels.graph("vel/tangential_ips", tangential);

            panels.debug(String.format(
                    "pose   x=%6.2f in   y=%6.2f in   h=%6.1f deg", x, y, headingDeg));
            panels.debug(String.format(
                    "vel    speed=%5.2f  fwd=%5.2f  strafe=%5.2f  tang=%5.2f in/s",
                    speed, forward, strafe, tangential));

            driverStation.addData("Pose", "x %.1f  y %.1f  h %.0f", x, y, headingDeg);
            driverStation.addData("Speed", "%.1f in/s", speed);
        }

        panels.debug(String.format(
                "loop   #%d   %.2f ms (max %.2f)   %.0f Hz   up %.1fs",
                loops, loopMs, maxLoopMs, hz, upSec));

        if (!pedroLog.isEmpty()) {
            panels.debug("pedro  " + pedroLog);
        }

        driverStation.addData("Loop", "#%d  %.1f ms", loops, loopMs);

        // One call reaches Panels and the Driver Station. Do not also call
        // driverStation.update() -- this does it.
        panels.update(driverStation);
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
