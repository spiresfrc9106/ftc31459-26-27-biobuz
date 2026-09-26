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
 *
 * <p>Passes when: LessonDriveTrainTest -- one test for each of the five blanks,
 * so a wrong wheel says which wheel.
 */
public class LessonDriveTrain extends CorbelsDriveTrain {

    public LessonDriveTrain(RobotHardware hardware) {
        super(hardware);
    }

    /**
     * The follower's three numbers, as four wheel powers.
     *
     * <p>{@code powers.strafe()} is positive towards the robot's left and
     * {@code powers.turn()} is positive counter-clockwise, the same as L5. See
     * {@link CorbelsDriveTrain} for where those directions come from.
     */
    @Override
    protected double[] mix(DrivePowers powers) {
        double forwardSpeed = powers.forward();
        double strafeLeftSpeed = powers.strafe();
        double turnCcwSpeed = powers.turn();

        return new double[]{
                // TODO 1: front left
                0,
                // TODO 2: front right
                0,
                // TODO 3: back left
                0,
                // TODO 4: back right
                0};
    }

    /** Sends each of the four powers to its own motor, in that same order. */
    @Override
    protected void writeWheels(double[] wheels) {
        // TODO 5: send each of the four powers to its own motor, in the same
        //         order mix() put them in:
        //         frontLeft.setPower(wheels[0]);  and so on for the other three.
    }
}
