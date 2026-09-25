package org.firstinspires.ftc.teamcode.pedro;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.drivetrain.Drivetrain;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.MecanumConfig;

import org.firstinspires.ftc.teamcode.base.CorbelsMecanum;
import org.firstinspires.ftc.teamcode.base.RobotHardware;
import com.pedropathing.revhub.localizers.PinpointConfig;
import com.pedropathing.revhub.localizers.PinpointLocalizer;
import com.pedropathing.tuning.autotune.Procedure;
import com.pedropathing.tuning.autotune.Tuner;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.pedro.procedures.ForesightTuner;
import org.firstinspires.ftc.teamcode.pedro.procedures.PinpointTuner;
import org.firstinspires.ftc.teamcode.pedro.procedures.Tests;


public class Constants {

    // ---- Device names. These must match the Robot Controller configuration,
    // ---- and nothing outside this file should name a device. ----------------

    public static String frontLeftName = "Front Left";
    public static String frontRightName = "Front Right";
    public static String backLeftName = "Back Left";
    public static String backRightName = "Back Right";
    public static String imuName = "imu";

    // ---- Drivetrain. One source of truth, read by both the Pedro config and
    // ---- our own CorbelsMecanum. -------------------------------------------

    public static DcMotorSimple.Direction frontLeftDirection = DcMotorSimple.Direction.FORWARD;
    public static DcMotorSimple.Direction frontRightDirection = DcMotorSimple.Direction.REVERSE;
    public static DcMotorSimple.Direction backLeftDirection = DcMotorSimple.Direction.FORWARD;
    public static DcMotorSimple.Direction backRightDirection = DcMotorSimple.Direction.REVERSE;

    /** Brake rather than coast when a driver releases the sticks. */
    public static boolean manualBrakeMode = true;

    /**
     * Ticks per inch of wheel travel, and the fastest a wheel actually goes.
     * MEASURE BOTH on your robot: push it a known distance for the first, and
     * drive flat out and read the velocity for the second.
     */
    public static double ticksPerInch = 45.0;          // MEASURE: run L17a

    /** How far a wheel sits from the middle, inches. MEASURE: run L17b. */
    public static double turnRadiusInches = 8.0;

    /**
     * The fastest the robot goes, inches per second, and the power it takes to
     * ask for one inch per second. Both come from AutoTune:
     * maxAchievableForwardVelocity, maxAchievableStrafeVelocity, and the coast
     * feedforward. Strafing is slower than driving because mecanum rollers
     * waste some of it sideways.
     */
    public static double maxForwardInchesPerSecond = 64.43298446564829;
    public static double maxStrafeInchesPerSecond = 43.595132915165195;
    public static double powerPerInchPerSecond = 0.016695978563625216;

    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set(frontLeftName);
        c.frontRightName.set(frontRightName);
        c.backLeftName.set(backLeftName);
        c.backRightName.set(backRightName);
        c.frontLeftDirection.set(frontLeftDirection);
        c.frontRightDirection.set(frontRightDirection);
        c.backLeftDirection.set(backLeftDirection);
        c.backRightDirection.set(backRightDirection);

        c.manualBrakeMode.set(manualBrakeMode);
    });

    public static PinpointConfig localizerConfig = new PinpointConfig(c -> {
        c.name.set("pinpoint");
        c.podType.set(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);
        c.xPodOffset.set(1.0581908639021746);
        c.yPodOffset.set(3.359022666150191);
        c.xPodDirection.set(GoBildaPinpointDriver.EncoderDirection.REVERSED );
        c.yPodDirection.set(GoBildaPinpointDriver.EncoderDirection.FORWARD);
        c.globalDistanceUnit.set(DistanceUnit.INCH);
        c.offsetUnits.set(DistanceUnit.INCH);
    });

    public static ForesightConfig foresightConfig = new ForesightConfig(
            c -> {
                Controller primaryTranslationalForward = Controller.proportional(0.368944499316663);
                Controller secondaryTranslationalForward = Controller.proportional(0.13631513411892088);
                Controller primaryTranslationalLateral = Controller.proportional(0.28643896916380074);
                Controller secondaryTranslationalLateral = Controller.proportional(0.10583154531580646);

                c.forwardTranslational.set(Controller.piecewise(secondaryTranslationalForward).put(2.5, primaryTranslationalForward));
                c.strafeTranslational.set(Controller.piecewise(secondaryTranslationalLateral).put(2.5, primaryTranslationalLateral));

                c.coast.set(Controller.proportionalFeedforward(0.016695978563625216));
                c.brake.set(Controller.proportionalFeedforward(0.014191581779081433));

                c.headingFeedback.set(Controller.proportional(4.443284774829611));
                c.headingBrakeCoefficients.set(Vector2D.cartesian(0.20442551239984175, -0.004974765042140602));

                c.linearBrakeCoefficients.set(Matrix.diag(0.04993877265618792, 0.08334033043867983));
                c.quadraticBrakeCoefficients.set(Matrix.diag(0.0011536551239685066, 6.430819946483606E-4));

                c.maxAchievableForwardVelocity.set(64.43298446564829);
                c.maxAchievableStrafeVelocity.set(43.595132915165195);
                c.naturalForwardDeceleration.set(46.50217822427653);
                c.naturalStrafeDeceleration.set(62.02176070971554);

                c.translationalConstraint.set(0.5);              // hold tolerance in inches
                c.headingConstraint.set(Math.toRadians(1));      // hold tolerance degrees
                c.velocityConstraint.set(0.1);                   // hold tolerance inches/sec
                c.holdPointTranslationalScaling.set(0.45);       // default=0.45
                c.holdPointHeadingScaling.set(0.35);             // default=0.35
            }
    );


    /**
     * For code that has no RobotHardware of its own -- the standalone examples.
     * Prefer the two-argument form, which shares the already-resolved devices.
     */
    public static Follower create(HardwareMap h) {
        return create(h, new CorbelsMecanum(new RobotHardware(h)));
    }

    public static Follower create(HardwareMap h, Drivetrain drivetrain) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig),   // Your Localizer object
                drivetrain,                                  // Your Drivetrain object
                new Foresight(foresightConfig)               // Your Foresight algorithm object
        );
    }


}

