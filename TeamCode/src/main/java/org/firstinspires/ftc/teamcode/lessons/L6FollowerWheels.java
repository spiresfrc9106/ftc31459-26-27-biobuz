package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;

/**
 * L6: hand the wheels to the path follower.
 *
 * <p>L5 drove exactly as well as this does, and the sticks feel the same. What
 * changes is who calls {@code setPower}. Instead of the loop working out four
 * wheel powers, it tells the follower what the robot should do --
 * {@code follower.manual(forward, left, turn)} -- and the follower asks
 * {@link LessonDriveTrain} for the four powers when it updates.
 *
 * <p>That is worth doing because the follower can do more than pass the sticks
 * through. It knows where the robot is, it can hold a heading, and it can drive
 * a path. None of that is possible while the loop owns the motors, and all of it
 * is what L8 onwards is about.
 *
 * <p>Two things to notice. The drivetrain is handed to {@code initAfter}, which
 * is how the follower gets it. And the loop no longer touches a motor: the last
 * line of every loop is {@code loopAfter()}, and inside it
 * {@code follower.update()} is what moves the robot.
 *
 * <p>Nothing to fill in here. The work is in {@link LessonDriveTrain}, and it
 * passes when LessonDriveTrainTest does.
 */
@TeleOp(name = "L6 Follower Wheels", group = "Lessons")
public class L6FollowerWheels extends CorbelsTeleOp {

    private LessonDriveTrain wheels;

    @Override
    public void init() {
        initBefore();
        wheels = new LessonDriveTrain(hardware);
        initAfter(wheels);
    }

    @Override
    public void start() {
        startBefore();
        startAfter();
    }

    @Override
    public void loop() {
        loopBefore();

        double forward = -gamepad1.left_stick_y;
        double left = -gamepad1.left_stick_x;
        double turn = -gamepad1.right_stick_x;
        follower.manual(forward, left, turn);

        data("command/forward", forward);
        data("command/left", left);
        data("command/turn_ccw", turn);

        loopAfter();
    }

    @Override
    public void stop() {
        stopAfter();
    }
}
