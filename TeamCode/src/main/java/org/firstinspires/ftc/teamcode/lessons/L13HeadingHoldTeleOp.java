package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;
import org.firstinspires.ftc.teamcode.base.HeadingHold;

/**
 * L13: stop the drift. When the turn stick is released, hold the heading the
 * robot was left at -- using the SAME controller the autonomous uses.
 *
 * <p>Passes when: LessonsTest.l13_theRobotHoldsItsHeadingWhenTheStickIsReleased
 */
@TeleOp(name = "L13 Heading Hold", group = "Lessons")
public class L13HeadingHoldTeleOp extends CorbelsTeleOp {

    private HeadingHold heading;

    @Override
    protected void bindings() {
        // TODO 1: build a HeadingHold from the tuned controller:
        //         new HeadingHold(Constants.foresightConfig.headingFeedback.get())
    }

    @Override
    protected void drive() {
        // TODO 2: ask heading.turn(follower, gamepad1.right_stick_x) for the
        //         turn power, then drive field relative with it.
    }
}
