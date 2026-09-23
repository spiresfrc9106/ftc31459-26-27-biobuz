package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;
import org.firstinspires.ftc.teamcode.base.Drive;

/** L5: holonomic arcade -- now it can strafe. Still relative to the robot. */
@TeleOp(name = "L5 Holonomic", group = "Lessons")
public class L5Holonomic extends CorbelsTeleOp {

    @Override
    protected void drive() {
        Drive.holonomic(follower,
                -gamepad1.left_stick_y,      // forward
                -gamepad1.left_stick_x,      // left is positive
                gamepad1.right_stick_x);     // turn
    }
}
