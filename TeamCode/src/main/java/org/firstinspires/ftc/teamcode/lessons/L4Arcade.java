package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;
import org.firstinspires.ftc.teamcode.base.Drive;

/** L4: arcade drive, still without strafing. One stick drives, one turns. */
@TeleOp(name = "L4 Arcade", group = "Lessons")
public class L4Arcade extends CorbelsTeleOp {

    @Override
    protected void drive() {
        Drive.arcade(follower, -gamepad1.left_stick_y, gamepad1.right_stick_x);
    }
}
