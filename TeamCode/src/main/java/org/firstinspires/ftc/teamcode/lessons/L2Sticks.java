package org.firstinspires.ftc.teamcode.lessons;

import com.pedropathing.ivy.commands.Commands;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;
import org.firstinspires.ftc.teamcode.base.Tracker;

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

        double left = -gamepad1.left_stick_y;
        double right = -gamepad1.right_stick_y;
        tank.sticks(left, right);

        Tracker.publish("stick/leftY", left);
        Tracker.publish("stick/leftX", gamepad1.left_stick_x);
        Tracker.publish("stick/rightY", right);
        Tracker.publish("stick/rightX", gamepad1.right_stick_x);

        loopAfter();
    }

    @Override
    public void stop() {
        tank.sticks(0, 0);
        stopAfter();
    }

    @Override
    protected void bindings() {
        buttons.whenPressed(() -> gamepad1.a,
                Commands.instant(() -> Tracker.publish("driver pressed A", true)));
    }
}
