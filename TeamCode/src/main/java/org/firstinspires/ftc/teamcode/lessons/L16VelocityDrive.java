package org.firstinspires.ftc.teamcode.lessons;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.base.CorbelsTeleOp;
import org.firstinspires.ftc.teamcode.base.Drive;
import org.firstinspires.ftc.teamcode.base.WheelTargets;
import org.firstinspires.ftc.teamcode.base.WheelVelocities;
import org.firstinspires.ftc.teamcode.pedro.Constants;

/**
 * L16: drive in real units.
 *
 * <p>Until now a stick has meant "power": push it half way and the motor gets
 * 0.5, whatever that turns out to be. The robot goes slower on a full battery
 * at the end of a match, slower again up a ramp, and faster on blocks with its
 * wheels off the ground. Nothing in the code knows how fast the robot is going.
 *
 * <p>Here a stick means a <b>speed</b>. Full forward asks for
 * {@link Constants#maxWheelInchesPerSecond} inches per second, and the robot
 * goes that fast whether the battery is full or flat -- because the code
 * measures what the wheels are doing and corrects.
 *
 * <p>Two parts to that, and both are in {@link #drive}:
 *
 * <ul>
 *   <li><b>Feedforward</b> -- a guess at the power needed for a wanted speed,
 *       from the fact that power and speed are roughly proportional. Gets most
 *       of the way there immediately.</li>
 *   <li><b>Feedback</b> -- a correction proportional to the error between the
 *       speed asked for and the speed measured. Cleans up what the guess got
 *       wrong: battery, friction, carpet, a ramp.</li>
 * </ul>
 *
 * <p>Feedforward alone is always a little off. Feedback alone has to build up
 * error before it does anything, so it lags. Together they are how nearly every
 * velocity controller works.
 */
@TeleOp(name = "L16 Velocity Drive", group = "Lessons")
public class L16VelocityDrive extends CorbelsTeleOp {

    /** How fast full stick asks for, forward and sideways. Inches per second. */
    private static final double MAX_IPS = 40;

    /** How fast full stick asks for in turn. Radians per second. */
    private static final double MAX_TURN_RADPS = Math.PI;

    /** Power per inch per second. The feedforward guess, measured by AutoTune. */
    private static final double kV = Constants.powerPerInchPerSecond;

    /** Power per inch per second of error. The feedback correction. */
    private static final double kP = 0.008;

    private WheelVelocities wheels;

    @Override
    protected void bindings() {
        wheels = new WheelVelocities(hardware);
    }

    @Override
    protected void drive() {
        // TODO 1. Read the sticks as a SPEED, not a power. Full forward should
        //         ask for MAX_IPS inches per second; full turn, MAX_TURN_RADPS
        //         radians per second. Use Drive.deadband(value, 0.05) as usual.
        double forwardIps = 0;
        double leftIps = 0;
        double turnRadps = 0;

        // TODO 2. Work out how fast each wheel has to travel for the robot to
        //         move like that. WheelTargets.forMecanum does the mixing;
        //         Constants.turnRadiusInches says how far a wheel is from
        double[] target = new double[4];

        // TODO 3. Ask the motors how fast their wheels are actually going.
        //         WheelVelocities was made for you in bindings().
        double[] actual = new double[4];

        // TODO 4. For each wheel, guess the power from the target (kV), then
        //         correct it by the error (kP), and clamp the result to -1..1.
        //         power = kV * target + kP * (target - actual)
        double[] power = new double[4];

        drivetrain.driveWheels(power[0], power[1], power[2], power[3]);

        String[] names = {"frontLeft", "frontRight", "backLeft", "backRight"};
        for (int i = 0; i < 4; i++) {
            data("wheel/" + names[i] + "/target_ips", target[i]);
            data("wheel/" + names[i] + "/actual_ips", actual[i]);
            data("wheel/" + names[i] + "/error_ips", target[i] - actual[i]);
            data("wheel/" + names[i] + "/power", power[i]);
        }
        data("command/forward_ips", forwardIps);
        data("command/left_ips", leftIps);
        data("command/turn_radps", turnRadps);
    }

    private static double clamp(double v) {
        return Math.max(-1, Math.min(1, v));
    }
}
