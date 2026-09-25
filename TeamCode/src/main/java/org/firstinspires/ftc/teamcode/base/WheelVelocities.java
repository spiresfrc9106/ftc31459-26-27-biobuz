package org.firstinspires.ftc.teamcode.base;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * How fast each wheel is actually turning, in inches per second.
 *
 * <p>The SDK reports encoder ticks per second; {@link Constants#ticksPerInch}
 * turns that into inches per second, the same constant the encoder localizer
 * uses. Measured, not calculated -- push the robot a known distance and divide.
 *
 * <p>This is the measurement half of velocity control. The command half is
 * whatever a lesson computes, and the two meet in a controller.
 */
public class WheelVelocities {

    private final DcMotorEx frontLeft, frontRight, backLeft, backRight;

    public WheelVelocities(RobotHardware hardware) {
        frontLeft = hardware.frontLeft;
        frontRight = hardware.frontRight;
        backLeft = hardware.backLeft;
        backRight = hardware.backRight;
    }

    public double frontLeftInchesPerSecond() {
        return frontLeft.getVelocity() / Constants.ticksPerInch;
    }

    public double frontRightInchesPerSecond() {
        return frontRight.getVelocity() / Constants.ticksPerInch;
    }

    public double backLeftInchesPerSecond() {
        return backLeft.getVelocity() / Constants.ticksPerInch;
    }

    public double backRightInchesPerSecond() {
        return backRight.getVelocity() / Constants.ticksPerInch;
    }

    /** All four, in the order the drivetrain uses. */
    public double[] all() {
        return new double[]{frontLeftInchesPerSecond(), frontRightInchesPerSecond(),
                backLeftInchesPerSecond(), backRightInchesPerSecond()};
    }
}
