package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;

/**
 * L3: tank drive. Left stick runs the left wheels, right stick the right.
 *
 * <p>Passes when: LessonsTest.l3_tankDrivesStraightAndTurns
 */
@TeleOp(name = "L3 Tank", group = "Lessons")
public class L3Tank extends CorbelsTeleOp {

    @Override
    protected void drive() {
        // TODO: call Drive.tank(follower, left, right).
        //       Both sticks need negating so that pushing away is forward.
    }
}
