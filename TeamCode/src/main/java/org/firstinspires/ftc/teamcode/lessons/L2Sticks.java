package org.firstinspires.ftc.teamcode.lessons;

import com.pedropathing.ivy.commands.Commands;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;

/** L2: what do the sticks actually say? The robot does not move yet. */
@TeleOp(name = "L2 Sticks", group = "Lessons")
public class L2Sticks extends CorbelsTeleOp {

    @Override
    protected void drive() {
        data("stick/leftY", -gamepad1.left_stick_y);
        data("stick/leftX", gamepad1.left_stick_x);
        data("stick/rightY", -gamepad1.right_stick_y);
        data("stick/rightX", gamepad1.right_stick_x);
    }

    @Override
    protected void bindings() {
        buttons.whenPressed(() -> gamepad1.a,
                Commands.instant(() -> data("driver pressed A", true)));
    }
}
