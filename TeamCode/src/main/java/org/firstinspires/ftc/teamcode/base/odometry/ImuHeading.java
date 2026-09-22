package org.firstinspires.ftc.teamcode.base.odometry;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

/** The Control Hub's built-in IMU, as a heading in radians. */
public class ImuHeading implements HardwareWheelSource.HeadingSource {

    private final IMU imu;

    public ImuHeading(HardwareMap map) {
        this(map, "imu");
    }

    public ImuHeading(HardwareMap map, String name) {
        this.imu = map.get(IMU.class, name);
    }

    @Override
    public double radians() {
        return imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
    }
}
