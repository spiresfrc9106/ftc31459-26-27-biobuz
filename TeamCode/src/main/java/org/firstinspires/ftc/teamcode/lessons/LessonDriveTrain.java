package org.firstinspires.ftc.teamcode.lessons;

import com.pedropathing.drivetrain.DrivePowers;

import org.firstinspires.ftc.teamcode.base.CorbelsDriveTrain;
import org.firstinspires.ftc.teamcode.base.RobotHardware;

/**
 * The drivetrain the path follower drives, from L6 onwards.
 *
 * <p>Up to L5 the OpMode called {@code setPower} itself, every loop. From here
 * the path follower does the asking: it works out how fast the robot should go
 * forward, sideways and around, and hands those three numbers to this class
 * every time {@code follower.update()} runs. Everything the follower needs is
 * already written in {@link CorbelsDriveTrain}. Two things are not, and they are
 * the two below.
 *
 * <p>{@link #mix} is the L5 mixing again, and {@link #writeWheels} is the four
 * {@code setPower} calls again. Nothing new to learn; what changes is who calls
 * them. That is the whole point of L6: the same four lines that drove the robot
 * by hand now drive it along a path.
 */
public class LessonDriveTrain extends CorbelsDriveTrain {

    public LessonDriveTrain(RobotHardware hardware) {
        super(hardware);
    }

    /**
     * The follower's three numbers, as four wheel powers.
     *
     * <p>{@code powers.strafe()} is positive towards the robot's left and
     * {@code powers.turn()} is positive counter-clockwise, the same as L5.
     */
    @Override
    protected double[] mix(DrivePowers powers) {
        double forward = powers.forward();
        double left = powers.strafe();
        double turn = powers.turn();

        return new double[]{
                forward - left - turn,      // front left
                forward + left + turn,      // front right
                forward + left - turn,      // back left
                forward - left + turn};     // back right
    }

    /** Sends each of the four powers to its own motor, in that same order. */
    @Override
    protected void writeWheels(double[] wheels) {
        frontLeft.setPower(wheels[0]);
        frontRight.setPower(wheels[1]);
        backLeft.setPower(wheels[2]);
        backRight.setPower(wheels[3]);
    }
}
