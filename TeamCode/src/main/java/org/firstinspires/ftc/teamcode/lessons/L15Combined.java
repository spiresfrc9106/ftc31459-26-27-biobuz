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
 * <table>
 *   <tr><td>nothing held</td><td>field relative, holding the heading you left</td></tr>
 *   <tr><td>right bumper</td><td>robot relative</td></tr>
 *   <tr><td>A</td><td>turn to 45 degrees and hold it; the sticks still drive</td></tr>
 *   <tr><td>Y</td><td>drive to (24, 24, -45); any stick takes control back</td></tr>
 * </table>
 *
 * <p>The encoder localizer runs alongside the real one, so the field view shows
 * both answers.
 */
@TeleOp(name = "L15 Combined", group = "Lessons")
public class L15Combined extends CorbelsTeleOp {

    private static final PoseFactory POSES = PoseFactory.degrees();

    /** Where Y drives to. */
    private static final Pose TARGET_POSE = POSES.of(24, 24, -45);

    /** Where A points. */
    private static final double TARGET_HEADING_DEGREES = 45;

    private HeadingHold heading;
    private boolean drivingItself;

    @Override
    protected void shadows() {
        shadow.add("encoders", new MecanumEncoderLocalizer(new HardwareWheelSource(hardware)));
    }

    @Override
    protected void bindings() {
        heading = new HeadingHold(Constants.foresightConfig.headingFeedback.get());

        // A: point at a fixed field heading. Translation stays with the driver.
        buttons.whenPressed(() -> gamepad1.a, Commands.instant(() -> {
            drivingItself = false;
            heading.aimAt(Math.toRadians(TARGET_HEADING_DEGREES));
        }));

        // Y: hand the whole robot to the follower until a stick moves.
        buttons.whenPressed(() -> gamepad1.y, Commands.instant(() -> {
            follower.hold(TARGET_POSE);
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
                data("drive/target_deg", Math.toDegrees(TARGET_POSE.heading()));
                return;
            }
            drivingItself = false;      // a stick moved: the driver has it back
            heading.release();
        }

        double turn = heading.turn(follower, stick);
        Double held = heading.target();
        data("drive/aiming", held != null);
        if (held != null) data("drive/target_deg", Math.toDegrees(held));

        if (gamepad1.right_bumper) {
            data("drive/mode", "ROBOT");
            Drive.holonomic(follower, forward, left, turn);
        } else {
            data("drive/mode", "FIELD");
            Drive.fieldRelative(follower, forward, left, turn);
        }
    }
}
