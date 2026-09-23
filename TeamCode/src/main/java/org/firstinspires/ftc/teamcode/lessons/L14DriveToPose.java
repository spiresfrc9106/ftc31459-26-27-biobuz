package org.firstinspires.ftc.teamcode.lessons;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.commands.Commands;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;
import org.firstinspires.ftc.teamcode.base.Drive;

/**
 * L14: press Y and the robot drives itself to a pose and stays there.
 *
 * <p>The catch worth teaching: follower.manual() throws away whatever the
 * follower was doing. So while the command is running, the stick code must keep
 * its hands off -- unless the driver moves a stick, which cancels the command.
 */
@TeleOp(name = "L14 Drive To Pose", group = "Lessons")
public class L14DriveToPose extends CorbelsTeleOp {

    private static final PoseFactory POSES = PoseFactory.degrees();
    private static final Pose TARGET = POSES.of(120, 72, 90);

    private boolean drivingItself;

    @Override
    protected void bindings() {
        // PedroCommands.hold() is an INSTANT command: it tells the follower to
        // hold the pose and finishes straight away. The follower stays in HOLD
        // until something calls manual() -- so we track that ourselves.
        buttons.whenPressed(() -> gamepad1.y, Commands.instant(() -> {
            follower.hold(TARGET);
            drivingItself = true;
        }));
    }

    @Override
    protected void drive() {
        double forward = -gamepad1.left_stick_y;
        double left = -gamepad1.left_stick_x;
        double turn = gamepad1.right_stick_x;
        boolean driverWantsControl = Math.abs(forward) > 0.1 || Math.abs(left) > 0.1 || Math.abs(turn) > 0.1;

        if (drivingItself) {
            if (!driverWantsControl) {
                data("drive/mode", "AUTO");
                return;                       // leave the follower holding
            }
            drivingItself = false;            // the driver takes over
        }
        data("drive/mode", "DRIVER");
        Drive.fieldRelative(follower, forward, left, turn);
    }
}
