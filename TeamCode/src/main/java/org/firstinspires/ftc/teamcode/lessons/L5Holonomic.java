package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;

/**
 * L5: holonomic arcade. Now it can strafe -- the third number.
 *
 * <p>Passes when: LessonsTest.l5_holonomicCanStrafe
 */
@TeleOp(name = "L5 Holonomic", group = "Lessons")
public class L5Holonomic extends CorbelsTeleOp {

    @Override
    protected void drive() {
        // TODO: Drive.holonomic(follower, forward, left, turn)
        //       LEFT is positive, and the stick's x axis is positive to the
        //       right, so that one needs negating too.
    }
}
