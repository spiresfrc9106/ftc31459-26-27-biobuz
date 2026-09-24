package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;
import org.firstinspires.ftc.teamcode.base.Drive;
import org.firstinspires.ftc.teamcode.base.odometry.HardwareWheelSource;

/**
 * L8: drive around while two localizers disagree.
 *
 * <p>Panels draws the follower's pose, its aim point and the path on the
 * field. Your localizer arrives as telemetry -- Localizer/encoders/x_in
 * and friends -- so graph it next to pose/x_in, then measure the robot
 * with a tape and see which one was right.
 */
@TeleOp(name = "L8 Compare Localizers", group = "Lessons")
public class L8CompareLocalizers extends CorbelsTeleOp {

    @Override
    protected void shadows() {
        shadow.add("encoders", new MecanumEncoderLocalizer(new HardwareWheelSource(hardware)));
    }

    @Override
    protected void drive() {
        Drive.holonomic(follower,
                -gamepad1.left_stick_y, -gamepad1.left_stick_x, gamepad1.right_stick_x);
    }
}
