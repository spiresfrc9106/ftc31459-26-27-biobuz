package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;

/**
 * L4: arcade drive -- one stick drives, the other turns. Still no strafing.
 *
 * <p>Passes when: LessonsTest.l4_arcadeUsesOneStickToDriveAndOneToTurn
 */
@TeleOp(name = "L4 Arcade", group = "Lessons")
public class L4Arcade extends CorbelsTeleOp {

    @Override
    protected void drive() {
        // TODO: Drive.arcade(follower, forward, turn)
        //       forward comes from the left stick's y axis (negated),
        //       turn comes from the right stick's x axis.
    }
}
