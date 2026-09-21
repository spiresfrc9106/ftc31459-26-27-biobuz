package org.firstinspires.ftc.teamcode.panels;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.Scheduler.schedule;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.follower.Follower;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.math.Pose;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * Minimal autonomous: drive 24 inches forward, holding heading, with Panels
 * logging throughout. Built with Ivy so adding a second step later is one more
 * line in the sequential() block.
 *
 * <p>Pedro's coordinate frame: +x is to the right of the field and heading 0
 * faces +x. Starting at (0, 0, 0) and ending at (24, 0, 0) is therefore
 * "24 inches in whatever direction the robot is pointing at start".
 *
 * <p>NOTE ON HEADING: this uses {@code .constant()}, not the {@code .linear()}
 * the Pedro docs show. Open issues #176 and #181 both report .linear()
 * interpolating heading BACKWARDS on line paths in Pedro 3.0.x. Since this path
 * does not change heading at all, constant interpolation is both correct and
 * avoids that code path. Do not "fix" this to .linear().
 */
@Autonomous(name = "Auto: Drive 24in", group = "Corbels")
public class DriveForward24 extends OpMode {

    /** How far to drive, in inches. */
    private static final double DISTANCE_IN = 24.0;

    private Follower follower;
    private final PanelsLogger log = new PanelsLogger();

    private final PoseFactory poses = PoseFactory.degrees();
    private final Pose startPose = poses.of(0, 0, 0);
    private final Pose endPose = poses.of(DISTANCE_IN, 0, 0);

    private Path driveForward() {
        return line(startPose, endPose).constant(startPose);
    }

    private Command routine() {
        return sequential(
                follow(follower, driveForward())
                // Add more steps here; each runs after the previous finishes.
        );
    }

    @Override
    public void init() {
        // Ivy keeps static scheduler state between OpMode runs. Without this
        // reset, a second run re-executes commands left over from the first.
        Scheduler.reset();

        follower = Constants.create(hardwareMap)
                .withLogger(followerLog -> log.pedro(followerLog.toString()));
        follower.setPose(startPose);
        follower.update();

        telemetry.addLine("Panels: http://192.168.43.1:8001");
        telemetry.addData("Will drive", "%.0f in forward", DISTANCE_IN);
        telemetry.update();
    }

    @Override
    public void start() {
        log.start();
        schedule(routine());
    }

    @Override
    public void loop() {
        follower.update();
        Scheduler.execute();

        telemetry.addData("Mode", follower.mode());
        telemetry.addData("Remaining", "%.1f in",
                DISTANCE_IN - follower.pose().x());

        log.update(follower, telemetry);
    }
}
