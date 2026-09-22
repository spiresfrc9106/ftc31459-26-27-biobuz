package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;
import org.firstinspires.ftc.teamcode.base.Drive;

/**
 * L8: drive around while two localizers disagree.
 *
 * <p>Panels draws the follower's pose, its aim point and the path on the
 * field. Your localizer arrives as telemetry -- Localizer/encoders/x_in
 * and friends -- so graph it next to pose/x_in, then measure the robot
 * with a tape and see which one was right.
 *
 * <p>Passes when: LessonsTest.l8_theEncoderLocalizerRunsAlongsideAndIsLogged
 */
@TeleOp(name = "L8 Compare Localizers", group = "Lessons")
public class L8CompareLocalizers extends CorbelsTeleOp {

    @Override
    protected void shadows() {
        // TODO 1: run your localizer alongside the real one, named "encoders":
        //         shadow.add("encoders",
        //                 new MecanumEncoderLocalizer(new HardwareWheelSource(hardwareMap)));
    }

    @Override
    protected void drive() {
        // TODO 2: holonomic driving, same as lesson 5.
    }
}
