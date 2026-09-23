package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;
import org.firstinspires.ftc.teamcode.base.Drive;

/** L3: tank drive. Left stick drives the left wheels, right stick the right. */
@TeleOp(name = "L3 Tank", group = "Lessons")
public class L3Tank extends CorbelsTeleOp {

    @Override
    protected void drive() {
        Drive.tank(follower, -gamepad1.left_stick_y, -gamepad1.right_stick_y);
    }
}
