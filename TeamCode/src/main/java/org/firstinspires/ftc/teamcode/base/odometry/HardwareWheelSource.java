package org.firstinspires.ftc.teamcode.base.odometry;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * Reads the four drive motors' encoders and the Control Hub's IMU.
 *
 * <p>The only part of the encoder localizer that touches hardware, so the
 * maths can be tested on a laptop. Motor names must match the robot
 * configuration, and TICKS_PER_INCH comes from measuring: push the robot a
 * known distance and divide.
 */
public class HardwareWheelSource implements WheelSource {

    /** Measure this: drive a known distance, divide ticks by inches. */
    public static double TICKS_PER_INCH = 45.0;

    private final DcMotorEx frontLeft;
    private final DcMotorEx frontRight;
    private final DcMotorEx backLeft;
    private final DcMotorEx backRight;
    private final HeadingSource imu;

    /** Where the heading comes from; separated so tests can supply one. */
    public interface HeadingSource {
        double radians();
    }

    public HardwareWheelSource(HardwareMap map) {
        this(map, Constants.motorNames[0],
                Constants.motorNames[1],
                Constants.motorNames[2],
                Constants.motorNames[3], new ImuHeading(map));
    }

    public HardwareWheelSource(HardwareMap map, String fl, String fr, String bl, String br,
                               HeadingSource imu) {
        this.frontLeft = map.get(DcMotorEx.class, fl);
        this.frontRight = map.get(DcMotorEx.class, fr);
        this.backLeft = map.get(DcMotorEx.class, bl);
        this.backRight = map.get(DcMotorEx.class, br);
        this.imu = imu;
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
        return imu.radians();
    }
}
