package org.firstinspires.ftc.teamcode.lessons;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;
import org.firstinspires.ftc.teamcode.base.HeadingHold;

/**
 * L15: everything at once -- field relative with heading hold, robot relative
 * on the bumper, drive-to-pose on Y, and your localizer running alongside.
 *
 * <p>Passes when: LessonsTest.l15_everythingTogether
 */
@TeleOp(name = "L15 Combined", group = "Lessons")
public class L15Combined extends CorbelsTeleOp {

    private static final PoseFactory POSES = PoseFactory.degrees();
    private static final Pose TARGET = POSES.of(120, 72, 90);

    private HeadingHold heading;
    private boolean drivingItself;

    @Override
    protected void shadows() {
        // TODO 1: add your encoder localizer as "encoders", as in lesson 8.
    }

    @Override
    protected void bindings() {
        // TODO 2: heading hold from lesson 13, and the Y binding from lesson 14.
    }

    @Override
    protected void drive() {
        // TODO 3: lesson 14's hand-off, then lesson 13's heading hold, then
        //         lesson 12's bumper switch. Deadband and square the sticks
        //         with Drive.deadband(...) and Drive.squared(...) so the robot
        //         is calmer to drive.
    }
}
