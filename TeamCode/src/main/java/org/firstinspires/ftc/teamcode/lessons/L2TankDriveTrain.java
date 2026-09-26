package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.base.RobotHardware;
import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * The drivetrain for L2: two sticks, four wheels.
 *
 * <p>This is an ordinary Java class. It holds the four motors and it has one
 * job: turn the two stick numbers into four motor powers. Nothing else in the
 * robot writes to these motors while L2 is running, so what {@link #sticks}
 * sends is what the wheels do.
 *
 * <p>The motors are public, so a lesson can read a position or a velocity off
 * them any time.
 */
public class L2TankDriveTrain {

    public final DcMotorEx frontLeft;
    public final DcMotorEx frontRight;
    public final DcMotorEx backLeft;
    public final DcMotorEx backRight;

    /**
     * Takes the four motors and gets them ready: the right side spins the
     * opposite way to the left, because the two sides face opposite ways on the
     * robot, and every wheel brakes when its power goes to 0.
     */
    public L2TankDriveTrain(RobotHardware hardware) {
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
     * Tank drive: the left stick runs both left wheels, the right stick runs
     * both right wheels. Powers are -1 to 1.
     *
     * <p>Push both sticks forward and the robot goes straight. Push one forward
     * and one back and it spins. That is the whole idea, and it lives here in
     * one place so every lesson that drives this way says the same thing.
     */
    public void sticks(double leftSpeed, double rightSpeed) {
        frontLeft.setPower(leftSpeed);
        backLeft.setPower(leftSpeed);
        frontRight.setPower(rightSpeed);
        backRight.setPower(rightSpeed);
    }
}
