package org.firstinspires.ftc.teamcode.lessons;

import com.pedropathing.ivy.commands.Commands;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;

/**
 * L2: the sticks drive the wheels.
 *
 * <p>Two sticks, tank drive. The left stick runs the left wheels and the right
 * stick runs the right wheels, and {@link L2TankDriveTrain#sticks} is where
 * that happens. Push both forward and the robot goes straight; push one each
 * way and it spins in place.
 *
 * <p>This is also the first look at the four methods every OpMode has.
 * {@code init} runs once when INIT is pressed, {@code start} once when PLAY is
 * pressed, {@code loop} over and over until STOP, and {@code stop} once at the
 * end. The {@code Before} and {@code After} calls are the robot's own
 * housekeeping -- hardware, the flight log, Panels -- and the code between them
 * is this lesson's.
 *
 * <p>A stick pushed away from the driver reads negative, which is backwards
 * from how anyone thinks about it, so every stick gets a minus sign on the way
 * in. After that, forward is positive.
 *
 * <p>Passes when: LessonsTest.l2_logsEveryStickAndTheAButton and
 * LessonsTest.l2_theSticksDriveTheWheelsLikeATank
 */
@TeleOp(name = "L2 Sticks", group = "Lessons")
public class L2Sticks extends CorbelsTeleOp {

    private L2TankDriveTrain tank;

    @Override
    public void init() {
        initBefore();
        tank = new L2TankDriveTrain(hardware);
        initAfter();
    }

    @Override
    public void start() {
        startBefore();
        startAfter();
    }

    @Override
    public void loop() {
        loopBefore();

        // TODO 1: read both sticks' y axes, negating each one so that pushing
        //         away from the driver is a positive number.
        double left = 0;
        double right = 0;

        // TODO 2: hand them to the drivetrain: tank.sticks(left, right);

        // TODO 3: log all four stick axes, so Panels can draw them:
        //         data("stick/leftY", left);
        //         then stick/leftX, stick/rightY and stick/rightX.

        loopAfter();
    }

    @Override
    public void stop() {
        tank.sticks(0, 0);
        stopAfter();
    }

    @Override
    protected void bindings() {
        // TODO 4: when gamepad1.a is pressed, send "driver pressed A" to Panels:
        //         buttons.whenPressed(() -> gamepad1.a,
        //                 Commands.instant(() -> data("driver pressed A", true)));
    }
}
