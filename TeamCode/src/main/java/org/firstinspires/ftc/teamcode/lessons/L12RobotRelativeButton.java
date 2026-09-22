package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;

/**
 * L12: field relative normally; robot relative while the right bumper is held,
 * which is what a driver wants when lining up on a wall.
 *
 * <p>Passes when: LessonsTest.l12_theBumperSwitchesToRobotRelative
 */
@TeleOp(name = "L12 Robot Relative Button", group = "Lessons")
public class L12RobotRelativeButton extends CorbelsTeleOp {

    @Override
    protected void drive() {
        // TODO: if gamepad1.right_bumper is held, drive robot relative
        //       (Drive.holonomic); otherwise field relative.
        //       Held, not toggled -- ask a driver why.
    }
}
