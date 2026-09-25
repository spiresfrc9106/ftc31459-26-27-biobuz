package org.firstinspires.ftc.teamcode.base;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.VoltageSensor;

import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * Every device this code touches, looked up once.
 *
 * <p>Built in {@code init()}, before the match starts, so a name that doesn't
 * match the Robot Controller configuration fails where someone can read the
 * error -- not halfway through a match. Nothing else calls
 * {@link HardwareMap#get} afterwards, and no device name appears outside
 * {@link Constants}.
 *
 * <p>The drivetrain motors are here for reading encoders. Pedro drives them:
 * it has its own handles, made from the same names in
 * {@link Constants#drivetrainConfig}.
 */
public final class RobotHardware {

    public final DcMotorEx frontLeft;
    public final DcMotorEx frontRight;
    public final DcMotorEx backLeft;
    public final DcMotorEx backRight;
    public final IMU imu;

    /**
     * What the battery is giving. Not looked up by name: the SDK offers every
     * voltage sensor as a group, and the first is the Control Hub's own.
     */
    public final VoltageSensor battery;

    /** Looks up every device by the names in {@link Constants}. */
    public RobotHardware(HardwareMap map) {
        this(map.get(DcMotorEx.class, Constants.frontLeftName),
                map.get(DcMotorEx.class, Constants.frontRightName),
                map.get(DcMotorEx.class, Constants.backLeftName),
                map.get(DcMotorEx.class, Constants.backRightName),
                map.get(IMU.class, Constants.imuName),
                map.voltageSensor.iterator().next());
    }

    /** For tests, which supply their own fakes. */
    public RobotHardware(DcMotorEx frontLeft, DcMotorEx frontRight,
                         DcMotorEx backLeft, DcMotorEx backRight, IMU imu) {
        this(frontLeft, frontRight, backLeft, backRight, imu, null);
    }

    /** For tests, which supply their own fakes. */
    public RobotHardware(DcMotorEx frontLeft, DcMotorEx frontRight,
                         DcMotorEx backLeft, DcMotorEx backRight, IMU imu,
                         VoltageSensor battery) {
        this.battery = battery;
        this.frontLeft = frontLeft;
        this.frontRight = frontRight;
        this.backLeft = backLeft;
        this.backRight = backRight;
        this.imu = imu;
    }

    /** What the battery is giving, or a nominal 12 V if there is no sensor. */
    public double batteryVolts() {
        return battery == null ? 12.0 : battery.getVoltage();
    }
}
