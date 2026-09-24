package org.firstinspires.ftc.teamcode.base.odometry;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.teamcode.base.RobotHardware;

/**
 * Reads the four drive motors' encoders and the Control Hub's IMU.
 *
 * <p>The only part of the encoder localizer that touches hardware, so the maths
 * can be tested on a laptop. It looks nothing up: the devices come from
 * {@link RobotHardware}, resolved once at init.
 *
 * <p>TICKS_PER_INCH comes from measuring: push the robot a known distance and
 * divide.
 */
public class HardwareWheelSource implements WheelSource {

    /** Measure this: drive a known distance, divide ticks by inches. */
    public static double TICKS_PER_INCH = 45.0;

    private final DcMotorEx frontLeft;
    private final DcMotorEx frontRight;
    private final DcMotorEx backLeft;
    private final DcMotorEx backRight;
    private final HeadingSource heading;

    /** Where the heading comes from; separated so tests can supply one. */
    public interface HeadingSource {
        double radians();
    }

    /** The usual case: the robot's own motors and IMU. */
    public HardwareWheelSource(RobotHardware hardware) {
        this(hardware.frontLeft, hardware.frontRight, hardware.backLeft, hardware.backRight,
                () -> hardware.imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
    }

    public HardwareWheelSource(DcMotorEx frontLeft, DcMotorEx frontRight,
                               DcMotorEx backLeft, DcMotorEx backRight, HeadingSource heading) {
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.heading = heading;
    }

    @Override
    public double[] wheelInches() {
        return new double[]{
                frontLeft.getCurrentPosition() / TICKS_PER_INCH,
                frontRight.getCurrentPosition() / TICKS_PER_INCH,
                backLeft.getCurrentPosition() / TICKS_PER_INCH,
                backRight.getCurrentPosition() / TICKS_PER_INCH};
    }

    @Override
    public double headingRadians() {
        return heading.radians();
    }
}
