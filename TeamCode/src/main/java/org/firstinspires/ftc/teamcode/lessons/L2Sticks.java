package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;

/**
 * L2: what do the sticks actually say?
 *
 * <p>The robot does not move in this lesson. Log all four stick axes, then
 * look at them in Panels while you wiggle the sticks.
 *
 * <p>Passes when: LessonsTest.l2_logsEveryStickAndTheAButton
 */
@TeleOp(name = "L2 Sticks", group = "Lessons")
public class L2Sticks extends CorbelsTeleOp {

    @Override
    protected void drive() {
        // TODO 1: log the left stick's y axis as "stick/leftY".
        //         data("stick/leftY", ...);
        //         Pushing the stick AWAY from you gives a NEGATIVE number,
        //         so negate it to make forward positive.
        // TODO 2: do the same for stick/leftX, stick/rightY and stick/rightX.
    }

    @Override
    protected void bindings() {
        // TODO 3: when gamepad1.a is pressed, send "driver pressed A" to Panels:
        //         buttons.whenPressed(() -> gamepad1.a,
        //                 Commands.instant(() -> data("driver pressed A", true)));
    }
}
