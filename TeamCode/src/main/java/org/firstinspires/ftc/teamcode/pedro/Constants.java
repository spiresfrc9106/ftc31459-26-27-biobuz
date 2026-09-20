package org.firstinspires.ftc.teamcode.pedro;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.pedropathing.algorithm.Foresight;
import com.pedropathing.algorithm.ForesightConfig;
import com.pedropathing.controllers.Controller;
import com.pedropathing.follower.Follower;
import com.pedropathing.math.Matrix;
import com.pedropathing.math.Vector2D;
import com.pedropathing.revhub.drivetrains.Mecanum;
import com.pedropathing.revhub.drivetrains.MecanumConfig;
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
    public static MecanumConfig drivetrainConfig = new MecanumConfig(c -> {
        c.frontLeftName.set("Front Left");
        c.frontRightName.set("Front Right");
        c.backLeftName.set("Back Left");
        c.backRightName.set("Back Right");
        c.frontLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.frontRightDirection.set(DcMotorSimple.Direction.REVERSE);
        c.backLeftDirection.set(DcMotorSimple.Direction.FORWARD);
        c.backRightDirection.set(DcMotorSimple.Direction.REVERSE);
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
            }
    );


    public static Follower create(HardwareMap h) {
        return new Follower(
                new PinpointLocalizer(h, localizerConfig), // Your Localizer object
                new Mecanum(h, drivetrainConfig),       // Your Drivetrain object
                new Foresight(foresightConfig)          // Your Foresight algorithm object
        );
    }


}

