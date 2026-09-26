package org.firstinspires.ftc.teamcode.base;

import com.pedropathing.drivetrain.DrivePowers;

/**
 * A finished mecanum drivetrain, for code that is not a lesson.
 *
 * <p>Everything a drivetrain does is in {@link CorbelsDriveTrain}. This fills in
 * the two parts that class leaves open, with the mixing Pedro's own
 * {@code Mecanum} uses, so autonomous behaves exactly as it did before we
 * replaced Pedro's drivetrain with our own.
 *
 * <p>It is deliberately a separate file from the lessons' own drivetrain, and
 * deliberately the same four lines. A lesson's copy has blanks in it for a
 * student to fill, so the tuners and the system identification tools cannot
 * depend on it.
 */
public class CorbelsMecanum extends CorbelsDriveTrain {

    public CorbelsMecanum(RobotHardware hardware) {
        super(hardware);
    }

    /** Pedro's mixing, unchanged: forward, strafe and turn into four wheels. */
    @Override
    protected double[] mix(DrivePowers powers) {
        double forward = powers.forward();
        double strafe = powers.strafe();
        double turn = powers.turn();
        return new double[]{
                forward - strafe - turn,
                forward + strafe + turn,
                forward + strafe - turn,
                forward - strafe + turn};
    }

    @Override
    protected void writeWheels(double[] wheels) {
        frontLeft.setPower(wheels[0]);
        frontRight.setPower(wheels[1]);
        backLeft.setPower(wheels[2]);
        backRight.setPower(wheels[3]);
    }
}
