package org.firstinspires.ftc.teamcode.lessons;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.base.CorbelsAuto;

/**
 * L10: two moves in a row, and the second one turns.
 *
 * <p>Passes when: LessonsTest.l10_autoDrivesTwoLegsAndEndsTurned
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
        // TODO 1: drive start -> corner holding heading 0.
        // TODO 2: then corner -> end holding heading 90 degrees, so the robot
        //         turns as it drives the second leg.
        // TODO 3: finish with hold(follower, end) so it stays put.
        return Command.NOOP;
    }
}
