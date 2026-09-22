package org.firstinspires.ftc.teamcode.lessons;

import com.pedropathing.api.PoseFactory;
import com.pedropathing.math.Pose;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;

/**
 * L14: press Y and the robot drives itself to a pose and stays there.
 *
 * <p>The catch: follower.manual() throws away whatever the follower was doing.
 * So while the robot is driving itself, your stick code has to keep its hands
 * off -- until the driver moves a stick, which takes control back.
 *
 * <p>Passes when: LessonsTest.l14_pressingYDrivesToAPoseAndTheDriverCanTakeOver
 */
@TeleOp(name = "L14 Drive To Pose", group = "Lessons")
public class L14DriveToPose extends CorbelsTeleOp {

    private static final PoseFactory POSES = PoseFactory.degrees();
    private static final Pose TARGET = POSES.of(120, 72, 90);

    private boolean drivingItself;

    @Override
    protected void bindings() {
        // TODO 1: when gamepad1.y is pressed, run an instant command that
        //         calls follower.hold(TARGET) and sets drivingItself = true.
        //         (PedroCommands.hold() is instant: it sets the mode and ends.
        //         The follower stays in HOLD until something calls manual().)
    }

    @Override
    protected void drive() {
        // TODO 2: if drivingItself and the sticks are near zero, return without
        //         calling any Drive method -- let the follower hold.
        // TODO 3: if the driver DOES move a stick, set drivingItself = false
        //         and drive field relative as usual.
    }
}
