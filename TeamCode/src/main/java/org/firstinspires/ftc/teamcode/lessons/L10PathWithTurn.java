package org.firstinspires.ftc.teamcode.lessons;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;
import static com.pedropathing.ivy.pedro.PedroCommands.hold;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.base.CorbelsAuto;

/**
 * L10: two moves in a row, and the second one turns.
 *
 * <p>Each leg holds a constant heading, so the robot turns while driving the
 * second leg. (Pedro 3.0.1 has a bug in .linear() heading interpolation --
 * issues #176 and #181 -- so we use .constant().)
 */
@Autonomous(name = "L10 Path With Turn", group = "Lessons")
public class L10PathWithTurn extends CorbelsAuto {

    private static final PoseFactory POSES = PoseFactory.degrees();

    private final Pose start = POSES.of(72, 72, 0);
    private final Pose corner = POSES.of(96, 72, 0);
    private final Pose end = POSES.of(96, 96, 90);

    @Override
    protected Pose startPose() {
        return start;
    }

    @Override
    protected Command routine() {
        return sequential(
                follow(follower, line(start, corner).constant(0)),
                follow(follower, line(corner, end).constant(Math.toRadians(90))),
                hold(follower, end)
        );
    }
}
