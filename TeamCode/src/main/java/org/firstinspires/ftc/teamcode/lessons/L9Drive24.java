package org.firstinspires.ftc.teamcode.lessons;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.base.CorbelsAuto;

/** L9: the first autonomous -- drive 24 inches forward, and stop there. */
@Autonomous(name = "L9 Drive 24", group = "Lessons")
public class L9Drive24 extends CorbelsAuto {

    private static final PoseFactory POSES = PoseFactory.degrees();

    private final Pose start = POSES.of(72, 72, 0);
    private final Pose end = POSES.of(96, 72, 0);

    @Override
    protected Pose startPose() {
        return start;
    }

    @Override
    protected Command routine() {
        return sequential(
                follow(follower, line(start, end).constant(0))
        );
    }
}
