package org.firstinspires.ftc.teamcode.lessons;

import static com.pedropathing.api.Paths.line;
import static com.pedropathing.ivy.groups.Groups.sequential;
import static com.pedropathing.ivy.pedro.PedroCommands.follow;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.base.CorbelsAuto;

/**
 * L9: the first autonomous -- drive 24 inches forward, and stop there.
 *
 * <p>Passes when: LessonsTest.l9_autoDrives24InchesForwardAndStops
 */
@Autonomous(name = "L9 Drive 24", group = "Lessons")
public class L9Drive24 extends CorbelsAuto {

    private static final PoseFactory POSES = PoseFactory.degrees();

    private final Pose start = POSES.of(72, 72, 0);
    private final Pose end = POSES.of(96, 72, 0);


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
        // TODO: return a sequence with one step: follow a straight line from
        //       start to end, holding heading 0.
        //           return sequential(follow(follower, line(start, end).constant(0)));
        //       Use .constant(), not .linear() -- Pedro 3.0.1 issues #176 and #181.
        return Command.NOOP;
    }
}
