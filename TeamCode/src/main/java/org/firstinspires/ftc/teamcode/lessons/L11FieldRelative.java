package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;
import org.firstinspires.ftc.teamcode.base.Drive;

/**
 * L11: field relative. Push the stick away from you and the robot goes away
 * from you, whichever way it happens to be facing.
 */
@TeleOp(name = "L11 Field Relative", group = "Lessons")
public class L11FieldRelative extends CorbelsTeleOp {

    @Override
    protected void drive() {
        Drive.fieldRelative(follower,
                -gamepad1.left_stick_y,      // away from the driver
                -gamepad1.left_stick_x,      // to the driver's left
                gamepad1.right_stick_x);
    }
}
