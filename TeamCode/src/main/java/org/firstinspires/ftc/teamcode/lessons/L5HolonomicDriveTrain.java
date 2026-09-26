package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.base.CorbelsDriveTrain;
import org.firstinspires.ftc.teamcode.base.RobotHardware;
import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * The drivetrain for L5: all four wheels mixed separately, so the robot can
 * slide sideways.
 *
 * <p>Mecanum wheels have rollers set at 45 degrees, and the four wheels are
 * handed so the rollers make an X across the robot. Spin all four forwards and
 * the sideways pushes cancel, so the robot drives forward. Spin one diagonal
 * pair forwards and the other pair backwards and the forward pushes cancel
 * instead, so the robot slides.
 *
 * <p>That is the whole trick, and it is these four lines:
 *
 * <pre>
 *   front left  = forwardSpeed - strafeLeftSpeed - turnCcwSpeed
 *   front right = forwardSpeed + strafeLeftSpeed + turnCcwSpeed
 *   back left   = forwardSpeed + strafeLeftSpeed - turnCcwSpeed
 *   back right  = forwardSpeed - strafeLeftSpeed + turnCcwSpeed
 * </pre>
 *
 * <p>Those are the same four lines the path follower uses, so a robot that
 * strafes correctly here will follow a path correctly later.
 */
public class L5HolonomicDriveTrain {

    public final DcMotorEx frontLeft;
    public final DcMotorEx frontRight;
    public final DcMotorEx backLeft;
    public final DcMotorEx backRight;

    public L5HolonomicDriveTrain(RobotHardware hardware) {
        frontLeft = hardware.frontLeft;
        frontRight = hardware.frontRight;
        backLeft = hardware.backLeft;
        backRight = hardware.backRight;

        frontLeft.setDirection(Constants.frontLeftDirection);
        frontRight.setDirection(Constants.frontRightDirection);
        backLeft.setDirection(Constants.backLeftDirection);
        backRight.setDirection(Constants.backRightDirection);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    /**
     * Holonomic drive, relative to the robot's own front.
     * {@code forwardSpeed} drives, {@code strafeLeftSpeed} slides towards the
     * robot's left, {@code turnCcwSpeed} spins counter-clockwise. All -1 to 1,
     * in the directions {@link CorbelsDriveTrain} sets out.
     *
     * <p>Asking for all three at once wants more than a motor can give, so the
     * four powers are scaled down together.
     */
    public void sticks(double forwardSpeed, double strafeLeftSpeed, double turnCcwSpeed) {
        double[] wheels = {
                forwardSpeed - strafeLeftSpeed - turnCcwSpeed,
                forwardSpeed + strafeLeftSpeed + turnCcwSpeed,
                forwardSpeed + strafeLeftSpeed - turnCcwSpeed,
                forwardSpeed - strafeLeftSpeed + turnCcwSpeed};

        double max = 1.0;
        for (double wheel : wheels) max = Math.max(max, Math.abs(wheel));

        frontLeft.setPower(wheels[0] / max);
        frontRight.setPower(wheels[1] / max);
        backLeft.setPower(wheels[2] / max);
        backRight.setPower(wheels[3] / max);
    }
}
