package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;
import org.firstinspires.ftc.teamcode.base.Drive;

/**
 * L12: field relative normally, robot relative while the right bumper is held
 * -- which is what a driver wants when lining up against a wall.
 */
@TeleOp(name = "L12 Robot Relative Button", group = "Lessons")
public class L12RobotRelativeButton extends CorbelsTeleOp {

    @Override
    protected void drive() {
        double forward = -gamepad1.left_stick_y;
        double left = -gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;
        if (gamepad1.right_bumper) {
            Drive.holonomic(follower, forward, left, turn);
        } else {
            Drive.fieldRelative(follower, forward, left, turn);
        }
        data("drive/robotRelative", gamepad1.right_bumper);
    }
}
