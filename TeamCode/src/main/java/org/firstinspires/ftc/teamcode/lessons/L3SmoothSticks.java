package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;
import org.firstinspires.ftc.teamcode.base.Drive;

/**
 * L3: make the sticks feel good.
 *
 * <p>Same tank drive as L2, same {@link L2TankDriveTrain}. What changes is what
 * happens to the stick number on its way there. Two small jobs, and drivers
 * notice both.
 *
 * <p><b>Deadband.</b> A stick that has been let go rarely reads exactly 0. It
 * reads 0.02, or -0.03, and the robot creeps across the field on its own. So
 * anything smaller than {@link #DEADBAND} counts as 0.
 *
 * <p><b>Squaring.</b> Half stick gives quarter power, not half. Near the middle
 * the stick becomes gentle, which is where a driver lines up on a target; out at
 * the end it still reaches full power, which is where a driver crosses the
 * field. The sign is kept, so pulling back still goes backwards.
 *
 * <p>Both numbers go to Panels, raw and shaped, so the change is visible while
 * driving.
 *
 * <p>Passes when: LessonsTest.l3_aLetGoStickIsIgnoredAndHalfStickIsQuarterPower
 */
@TeleOp(name = "L3 Smooth Sticks", group = "Lessons")
public class L3SmoothSticks extends CorbelsTeleOp {

    /** Anything smaller than this counts as a stick that was let go. */
    private static final double DEADBAND = 0.05;

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

        double leftRaw = -gamepad1.left_stick_y;
        double rightRaw = -gamepad1.right_stick_y;

        // TODO: shape each stick before it reaches the wheels. Drive.deadband
        //       throws away anything smaller than DEADBAND, and Drive.squared
        //       squares it while keeping its sign. Deadband first, then square.
        double left = 0;
        double right = 0;
        tank.sticks(left, right);

        data("stick/left_raw", leftRaw);
        data("stick/left_shaped", left);
        data("stick/right_raw", rightRaw);
        data("stick/right_shaped", right);

        loopAfter();
    }

    @Override
    public void stop() {
        tank.sticks(0, 0);
        stopAfter();
    }
}
