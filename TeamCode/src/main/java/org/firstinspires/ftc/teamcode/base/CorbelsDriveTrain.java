package org.firstinspires.ftc.teamcode.base;

import com.pedropathing.drivetrain.DrivePowers;
import com.pedropathing.drivetrain.Drivetrain;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.pedro.Constants;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Everything a drivetrain the follower drives has to do, except the two parts a
 * lesson writes.
 *
 * <p>Pedro takes any {@link Drivetrain} in its Follower constructor, so this is
 * the seam Pedro provides rather than a patch. The four motors come from
 * {@link RobotHardware}, so they are the same objects the encoder localizer
 * reads.
 *
 * <p><b>The two a lesson writes.</b> {@link #mix} turns the three numbers the
 * follower asks for -- forward, strafe and turn -- into four wheel powers.
 * {@link #writeWheels} sends those four powers to the motors. Everything else
 * here is done.
 *
 * <p><b>Who writes to the motors.</b> Only {@link #writeWheels}, and only
 * {@link #drive} and {@link #stop} call it. {@code drive} is final so that
 * brake mode cannot be missed: a subclass that replaced it would replace that
 * line too.
 *
 * <p>The saturation maths in {@link #maxScaling} follows Pedro's {@code Mecanum}
 * (BSD 3-Clause, Pedro Pathing).
 */
public abstract class CorbelsDriveTrain implements Drivetrain {

    protected static final int FL = 0, FR = 1, BL = 2, BR = 3;

    protected final DcMotorEx frontLeft, frontRight, backLeft, backRight;

    private final DcMotorEx[] motors;
    private final double[] wheelPowers = new double[4];

    /** Set by {@link #setCommandedWheels}, cleared by {@link #releaseCommandedWheels}. */
    private double[] commandedWheels;

    protected CorbelsDriveTrain(RobotHardware hardware) {
        frontLeft = hardware.frontLeft;
        frontRight = hardware.frontRight;
        backLeft = hardware.backLeft;
        backRight = hardware.backRight;
        motors = new DcMotorEx[]{frontLeft, frontRight, backLeft, backRight};
        frontLeft.setDirection(Constants.frontLeftDirection);
        frontRight.setDirection(Constants.frontRightDirection);
        backLeft.setDirection(Constants.backLeftDirection);
        backRight.setDirection(Constants.backRightDirection);
    }

    // ------------------------------------------------------- what a lesson writes

    /**
     * The three numbers the follower asks for, as four wheel powers: front left,
     * front right, back left, back right.
     */
    protected abstract double[] mix(DrivePowers powers);

    /** Sends each of the four powers to its motor. */
    protected abstract void writeWheels(double[] wheels);

    // ------------------------------------------------------- wheel commands

    /**
     * Drives each wheel at the power given, from the next follower update until
     * {@link #releaseCommandedWheels}. Powers are -1 to 1, as the SDK uses them.
     *
     * <p>While wheels are commanded this way, whatever the follower computes is
     * ignored -- so only do it while the follower is in manual mode.
     */
    public void setCommandedWheels(double frontLeftPower, double frontRightPower,
                                   double backLeftPower, double backRightPower) {
        commandedWheels = new double[]{frontLeftPower, frontRightPower,
                backLeftPower, backRightPower};
    }

    /** Hands the wheels back to the follower. */
    public void releaseCommandedWheels() {
        commandedWheels = null;
    }

    /** True while a lesson is driving the wheels itself. */
    public boolean commandedWheelsAreSet() {
        return commandedWheels != null;
    }

    // ------------------------------------------------------- reading back

    /** What each wheel was last told to do: front left, front right, back left, back right. */
    public double[] wheelPowers() {
        return wheelPowers.clone();
    }

    /** What each encoder has counted, in ticks, in the same order. */
    public int[] wheelTicks() {
        return new int[]{frontLeft.getCurrentPosition(), frontRight.getCurrentPosition(),
                backLeft.getCurrentPosition(), backRight.getCurrentPosition()};
    }

    // ------------------------------------------------------- Drivetrain

    @Override
    public final void drive(DrivePowers powers, boolean manual) {
        applyBrakeMode(manual);
        double[] wheels = commandedWheels != null
                ? commandedWheels.clone()
                : normalized(mix(powers));
        System.arraycopy(wheels, 0, wheelPowers, 0, 4);
        writeWheels(wheels);
    }

    /**
     * BRAKE while a driver has the sticks, so letting go stops the robot; FLOAT
     * while the follower is running a path, so its own control is not fighting
     * the wheels.
     */
    protected final void applyBrakeMode(boolean manual) {
        setZeroPowerBehavior(manual && Constants.manualBrakeMode
                ? DcMotor.ZeroPowerBehavior.BRAKE : DcMotor.ZeroPowerBehavior.FLOAT);
    }

    /** Scales everything down together if any wheel would exceed full power. */
    protected static double[] normalized(double[] powers) {
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
        double[] currentPowers = mix(current);
        double[] deltaPowers = mix(delta);
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
        double[] zeros = new double[4];
        System.arraycopy(zeros, 0, wheelPowers, 0, 4);
        writeWheels(zeros);
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
