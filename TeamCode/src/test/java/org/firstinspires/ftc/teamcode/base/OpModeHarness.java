package org.firstinspires.ftc.teamcode.base;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Runs a real OpMode on a laptop: simulated drivetrain, fake motors and a
 * fake IMU. Panels telemetry is captured through the OpMode's data(...)
 * values rather than read back from a file.
 */
public final class OpModeHarness {

    /** A motor whose encoder the test drives by hand. */
    public static final class FakeMotor implements DcMotorEx {
        public int ticks;
        public double power;

        @Override
        public int getCurrentPosition() {
            return ticks;
        }

        @Override
        public void setPower(double power) {
            this.power = power;
        }

        @Override
        public double getVelocity() {
            return 0;
        }

        @Override
        public void setVelocity(double ticksPerSecond) {
        }
    }

    public static final class FakeImu implements IMU {
        public double yawRadians;

        @Override
        public YawPitchRollAngles getRobotYawPitchRollAngles() {
            return new YawPitchRollAngles(yawRadians);
        }

        @Override
        public void resetYaw() {
            yawRadians = 0;
        }
    }

    public final SimRobot robot = new SimRobot();
    public final Gamepad gamepad1 = new Gamepad();
    public final Gamepad gamepad2 = new Gamepad();
    public final Map<String, String> driverStation = SimRobot.newCapture();
    public final Map<String, FakeMotor> motors = new HashMap<>();
    public final FakeImu imu = new FakeImu();
    public final HardwareMap hardwareMap = new HardwareMap();

    private final OpMode opMode;

    public OpModeHarness(OpMode opMode) {
        this.opMode = opMode;
        for (String name : Constants.motorNames) {
            motors.put(name, new FakeMotor());
        }
        hardwareMap.devices = (type, name) -> type == IMU.class ? imu : motors.get(name);
        opMode.hardwareMap = hardwareMap;
        opMode.telemetry = SimRobot.telemetry(driverStation);
        opMode.gamepad1 = gamepad1;
        opMode.gamepad2 = gamepad2;
        RobotFactory.follower = map -> robot.follower;
    }

    /** Sets all four encoders, in ticks. */
    public void setWheelTicks(int frontLeft, int frontRight, int backLeft, int backRight) {
        motors.get(Constants.motorNames[0]).ticks = frontLeft;
        motors.get(Constants.motorNames[1]).ticks = frontRight;
        motors.get(Constants.motorNames[2]).ticks = backLeft;
        motors.get(Constants.motorNames[3]).ticks = backRight;
    }

    /** The OpMode under test, for reading what it logged. */
    public OpMode opMode() {
        return opMode;
    }

    public void init() {
        opMode.init();
    }

    public void start() {
        opMode.start();
    }

    public void loop() {
        opMode.loop();
    }

    public void loops(int count, long sleepMs) {
        for (int i = 0; i < count; i++) {
            opMode.loop();
            sleep(sleepMs);
        }
    }

    public void stop() {
        opMode.stop();
    }

    /** init, start, n loops, stop. */
    public void run(int loops, long sleepMs) {
        init();
        start();
        loops(loops, sleepMs);
        stop();
    }

    /** The powers the follower last commanded: forward, strafe, turn. */
    public double forward() {
        return robot.drive.last.forward();
    }

    public double strafe() {
        return robot.drive.last.strafe();
    }

    public double turn() {
        return robot.drive.last.turn();
    }

    public static void sleep(long ms) {
        if (ms <= 0) return;
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void restoreFactories() {
        RobotFactory.follower = org.firstinspires.ftc.teamcode.pedro.Constants::create;
    }
}
