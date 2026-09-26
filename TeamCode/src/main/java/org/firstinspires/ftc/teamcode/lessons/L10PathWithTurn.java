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
 *
 * <p>Passes when: LessonsTest.l10_autoDrivesTwoLegsAndEndsTurned
 */
@Autonomous(name = "L10 Path With Turn", group = "Lessons")
public class L10PathWithTurn extends CorbelsAuto {

    private static final PoseFactory POSES = PoseFactory.degrees();

    private final Pose start = POSES.of(72, 72, 0);
    private final Pose corner = POSES.of(96, 72, 0);
    private final Pose end = POSES.of(96, 96, 90);


    private LessonDriveTrain wheels;

    @Override
    public void init() {
        initBefore();
        wheels = new LessonDriveTrain(hardware);
        initAfter(wheels);
    }

    @Override
    public void start() {
        startBefore();
        startAfter();
    }

    /**
     * Nothing of this lesson's own goes here. An autonomous puts its work in
     * {@link #routine()}, and the scheduler runs it from inside
     * {@code loopAfter()}.
     */
    @Override
    public void loop() {
        loopBefore();
        loopAfter();
    }

    @Override
    public void stop() {
        stopAfter();
    }

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
