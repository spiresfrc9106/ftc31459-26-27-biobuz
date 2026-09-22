package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;

/**
 * L11: field relative. Push the stick away from you and the robot goes away
 * from you, whichever way it is facing.
 *
 * <p>Passes when: LessonsTest.l11_fieldRelativeIgnoresWhichWayTheRobotFaces
 */
@TeleOp(name = "L11 Field Relative", group = "Lessons")
public class L11FieldRelative extends CorbelsTeleOp {

    @Override
    protected void drive() {
        // TODO: same three numbers as lesson 5, but through
        //       Drive.fieldRelative(...) instead of Drive.holonomic(...).
        //       Try lesson 5's version with the robot turned 180 degrees first,
        //       so you can feel the difference.
    }
}
