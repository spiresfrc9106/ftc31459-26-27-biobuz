package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.base.CorbelsDriveTrain;
import org.firstinspires.ftc.teamcode.base.RobotHardware;
import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * The drivetrain for L4: one number to go, one number to turn.
 *
 * <p>Tank asked the driver to keep two sticks matched to go straight, which is
 * harder than it sounds. Arcade asks for what the robot should do instead: how
 * fast forward, and how fast to spin. {@link #sticks} works out what each side
 * has to do.
 *
 * <p>Turning counter-clockwise -- to the driver's left -- means the left wheels
 * go backwards while the right wheels go forwards. So the turn number is
 * subtracted from the left side and added to the right.
 */
public class L4ArcadeDriveTrain {

    public final DcMotorEx frontLeft;
    public final DcMotorEx frontRight;
    public final DcMotorEx backLeft;
    public final DcMotorEx backRight;

    public L4ArcadeDriveTrain(RobotHardware hardware) {
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
     * Arcade drive. {@code forwardSpeed} is how fast to drive,
     * {@code turnCcwSpeed} is how fast to spin counter-clockwise, in the
     * directions {@link CorbelsDriveTrain} sets out. Both are -1 to 1.
     *
     * <p>Full forward and full turn together want more than a motor can give, so
     * the four powers are scaled down together. Scaling them together keeps the
     * robot going where the driver asked; clipping each one on its own would not.
     */
    public void sticks(double forwardSpeed, double turnCcwSpeed) {
        double leftSpeed = forwardSpeed - turnCcwSpeed;
        double rightSpeed = forwardSpeed + turnCcwSpeed;

        double max = Math.max(1.0, Math.max(Math.abs(leftSpeed), Math.abs(rightSpeed)));
        leftSpeed /= max;
        rightSpeed /= max;

        frontLeft.setPower(leftSpeed);
        backLeft.setPower(leftSpeed);
        frontRight.setPower(rightSpeed);
        backRight.setPower(rightSpeed);
    }
}
