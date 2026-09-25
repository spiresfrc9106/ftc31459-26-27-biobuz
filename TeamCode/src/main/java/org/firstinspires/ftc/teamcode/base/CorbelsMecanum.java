package org.firstinspires.ftc.teamcode.base;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.drivetrain.Drivetrain;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Our mecanum drivetrain, in place of Pedro's own.
 *
 * <p>Pedro takes any {@link Drivetrain} in its Follower constructor, so nothing
 * here is a patch or a subclass -- it is the seam Pedro provides. Two reasons to
 * use it:
 *
 * <ul>
 *   <li>the four motors come from {@link RobotHardware}, so they are the same
 *       objects the encoder localizer reads, looked up once at init;</li>
 *   <li>a lesson can command the wheels directly, in the SDK's own terms,
 *       rather than only through a chassis-level power triple.</li>
 * </ul>
 *
 * <p><b>Who writes to the motors.</b> Still only Pedro: it calls
 * {@link #drive} every {@code follower.update()}, and that is the single place
 * anything reaches {@code setPower}. {@link #driveWheels} does not write -- it
 * records what the next {@code drive} should use. So a lesson can own the
 * control law without racing the follower.
 *
 * <p>The mixing and the saturation maths follow Pedro's {@code Mecanum}
 * (BSD 3-Clause, Pedro Pathing), so autonomous behaves exactly as before.
 */
public class CorbelsMecanum implements Drivetrain {

    private static final int FL = 0, FR = 1, BL = 2, BR = 3;

    private final DcMotorEx[] motors;
    private final double[] wheelPowers = new double[4];

    /** Set by {@link #driveWheels}, cleared by {@link #releaseWheels}. */
    private double[] commandedWheels;

    public CorbelsMecanum(RobotHardware hardware) {
        motors = new DcMotorEx[]{hardware.frontLeft, hardware.frontRight,
                hardware.backLeft, hardware.backRight};
        motors[FL].setDirection(Constants.frontLeftDirection);
        motors[FR].setDirection(Constants.frontRightDirection);
        motors[BL].setDirection(Constants.backLeftDirection);
        motors[BR].setDirection(Constants.backRightDirection);
    }

    // ------------------------------------------------------- wheel commands

    /**
     * Drives each wheel at the power given, from the next follower update until
     * {@link #releaseWheels}. Powers are -1 to 1, as the SDK uses them.
     *
     * <p>While wheels are commanded this way, whatever the follower computes is
     * ignored -- so only do it while the follower is in manual mode.
     */
    public void driveWheels(double frontLeft, double frontRight,
                            double backLeft, double backRight) {
        commandedWheels = new double[]{frontLeft, frontRight, backLeft, backRight};
    }

    /** Hands the wheels back to the follower. */
    public void releaseWheels() {
        commandedWheels = null;
    }

    /** True while a lesson is driving the wheels itself. */
    public boolean wheelsAreCommanded() {
        return commandedWheels != null;
    }

    /** What each wheel was last told to do: front left, front right, back left, back right. */
    public double[] wheelPowers() {
        return wheelPowers.clone();
    }

    // ------------------------------------------------------- Drivetrain

    @Override
    public void drive(DrivePowers powers, boolean manual) {
        setZeroPowerBehavior(manual && Constants.manualBrakeMode
                ? DcMotor.ZeroPowerBehavior.BRAKE : DcMotor.ZeroPowerBehavior.FLOAT);

        double[] next = commandedWheels != null
                ? commandedWheels.clone()
                : normalized(computeWheelPowersUnnormalized(powers));

        for (int i = 0; i < 4; i++) {
            wheelPowers[i] = next[i];
            motors[i].setPower(next[i]);
        }
    }

    /** Pedro's mixing, unchanged: forward, strafe and turn into four wheels. */
    public double[] computeWheelPowersUnnormalized(DrivePowers powers) {
        double forward = powers.forward();
        double strafe = powers.strafe();
        double turn = powers.turn();
        return new double[]{
                forward - strafe - turn,
                forward + strafe + turn,
                forward + strafe - turn,
                forward - strafe + turn};
    }

    /** Scales everything down together if any wheel would exceed full power. */
    private static double[] normalized(double[] powers) {
        double max = 1.0;
        for (double p : powers) max = Math.max(max, Math.abs(p));
        if (max == 1.0) return powers;
        for (int i = 0; i < powers.length; i++) powers[i] /= max;
        return powers;
    }

    /**
     * How much of {@code delta} can be added to {@code current} before a wheel
     * saturates. Pedro's algorithm uses this to avoid asking for more than the
     * drivetrain can give; the maths is Pedro's.
     */
    @Override
    public double maxScaling(DrivePowers current, DrivePowers delta) {
        double lambda = 1.0;
        double[] currentPowers = computeWheelPowersUnnormalized(current);
        double[] deltaPowers = computeWheelPowersUnnormalized(delta);
        for (int i = 0; i < 4; i++) {
            double a = currentPowers[i];
            double b = deltaPowers[i];
            if (Math.abs(b) < 1e-9) continue;
            double t1 = (1.0 - a) / b;
            double t2 = (-1.0 - a) / b;
            if (t1 >= 0.0 && t1 < lambda) lambda = t1;
            if (t2 >= 0.0 && t2 < lambda) lambda = t2;
        }
        return Math.max(0.0, Math.min(1.0, lambda));
    }

    @Override
    public void stop() {
        stop(Constants.manualBrakeMode);
    }

    @Override
    public void stop(boolean brake) {
        commandedWheels = null;
        setZeroPowerBehavior(brake ? DcMotor.ZeroPowerBehavior.BRAKE : DcMotor.ZeroPowerBehavior.FLOAT);
        for (int i = 0; i < 4; i++) {
            wheelPowers[i] = 0;
            motors[i].setPower(0);
        }
    }

    @Override
    public double interpolateVelocity(double xRadius, double yRadius, double theta) {
        return 1.0 / (Math.abs(Math.cos(theta)) / xRadius + Math.abs(Math.sin(theta)) / yRadius);
    }

    @Override
    public Map<String, Object> debug() {
        Map<String, Object> out = new LinkedHashMap<>();
        out.put("frontLeft", wheelPowers[FL]);
        out.put("frontRight", wheelPowers[FR]);
        out.put("backLeft", wheelPowers[BL]);
        out.put("backRight", wheelPowers[BR]);
        out.put("wheelsCommanded", commandedWheels != null);
        return out;
    }

    private void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior) {
        for (DcMotorEx motor : motors) motor.setZeroPowerBehavior(behavior);
    }
}
