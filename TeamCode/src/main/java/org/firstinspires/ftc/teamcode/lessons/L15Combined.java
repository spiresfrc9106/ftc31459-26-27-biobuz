package org.firstinspires.ftc.teamcode.lessons;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;
import org.firstinspires.ftc.teamcode.base.Drive;
import org.firstinspires.ftc.teamcode.base.HeadingHold;
import org.firstinspires.ftc.teamcode.base.odometry.HardwareWheelSource;
import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * L15: everything at once.
 *
 * <p>Field relative with heading hold; right bumper for robot relative;
 * Y drives to a pose; and the encoder localizer runs alongside so the field
 * view still shows both answers.
 */
@TeleOp(name = "L15 Combined", group = "Lessons")
public class L15Combined extends CorbelsTeleOp {

    private static final PoseFactory POSES = PoseFactory.degrees();
    private static final Pose TARGET = POSES.of(120, 72, 90);

    private HeadingHold heading;
    private boolean drivingItself;

    @Override
    protected void shadows() {
        shadow.add("encoders", new MecanumEncoderLocalizer(new HardwareWheelSource(hardwareMap)));
    }

    @Override
    protected void bindings() {
        heading = new HeadingHold(Constants.foresightConfig.headingFeedback.get());
        buttons.whenPressed(() -> gamepad1.y, Commands.instant(() -> {
            follower.hold(TARGET);
            drivingItself = true;
        }));
    }

    @Override
    protected void drive() {
        double forward = Drive.squared(Drive.deadband(-gamepad1.left_stick_y, 0.05));
        double left = Drive.squared(Drive.deadband(-gamepad1.left_stick_x, 0.05));
        double stick = Drive.deadband(gamepad1.right_stick_x, 0.05);
        boolean driverWantsControl = forward != 0 || left != 0 || stick != 0;

        if (drivingItself) {
            if (!driverWantsControl) {
                data("drive/mode", "AUTO");
                return;
            }
            drivingItself = false;
            heading.release();
        }

        double turn = heading.turn(follower, stick);
        if (gamepad1.right_bumper) {
            data("drive/mode", "ROBOT");
            Drive.holonomic(follower, forward, left, turn);
        } else {
            data("drive/mode", "FIELD");
            Drive.fieldRelative(follower, forward, left, turn);
        }
    }
}
