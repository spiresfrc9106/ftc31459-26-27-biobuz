package org.firstinspires.ftc.teamcode.example;

import com.bylazar.telemetry.PanelsTelemetry;
import com.bylazar.telemetry.TelemetryManager;
import com.pedropathing.follower.Follower;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.panels.PanelsLogger;
import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * Driver-controlled OpMode with Panels logging.
 *
 * <p>Same drive behaviour as ExampleTeleop; the only additions are the
 * PanelsLogger and the Pedro follower log hookup.
 *
 * <p>While this runs, open http://192.168.43.1:8001 on a device joined to the
 * robot's Wi-Fi. Loop rate, pose and velocity all plot live.
 */
@TeleOp(name = "TeleOp + Panels", group = "Corbels")
public class TeleopWithPanels extends OpMode {

    private Follower follower;
    private final PanelsLogger log = new PanelsLogger();

    @Override
    public void init() {
        // withLogger() attaches a Consumer<FollowerLog> that Pedro runs on every
        // follower.update(). Pedro does NOT flush it for you -- PanelsLogger does.
        follower = Constants.create(hardwareMap)
                .withLogger(followerLog -> log.pedro(followerLog.toString()));
        follower.update();

        telemetry.update();
    }

    @Override
    public void start() {
        log.start();
    }

    @Override
    public void loop() {
        // Field-relative manual drive, unchanged from ExampleTeleop.
        follower.manual(
                -gamepad1.left_stick_y*gamepad1.left_stick_y*gamepad1.left_stick_y,
                -gamepad1.left_stick_x*gamepad1.left_stick_x*gamepad1.left_stick_x,
                -gamepad1.right_stick_x*gamepad1.right_stick_x*gamepad1.right_stick_x
        );

        follower.update();

        // Logs to Panels and the Driver Station. Do not call telemetry.update()
        // after this -- PanelsLogger already did.
        log.update(follower, telemetry);
    }

    @Override
    public void stop() {
        follower.manual(0, 0, 0);
        follower.update();
    }
}
