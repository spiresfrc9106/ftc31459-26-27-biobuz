package org.firstinspires.ftc.teamcode.base;

import com.pedropathing.follower.Follower;

/**
 * Stick mappings. Each one turns joystick numbers into the three numbers a
 * mecanum base takes: {@code forwardSpeed}, {@code strafeLeftSpeed} and
 * {@code turnCcwSpeed}, in the directions {@link CorbelsDriveTrain} sets out.
 *
 * <p>All of these are OPEN LOOP. {@code follower.manual(...)} applies no
 * tuning and no feedback, so nothing here corrects heading drift --
 * see {@link HeadingHold} for that.
 */
public final class Drive {

    private Drive() {
    }

    /**
     * Tank: one stick per side. No strafing.
     *
     * <p>The left side ahead of the right turns the robot clockwise, which is a
     * negative {@code turnCcwSpeed}.
     */
    public static void tank(Follower follower, double leftSpeed, double rightSpeed) {
        follower.manual((leftSpeed + rightSpeed) / 2, 0, (rightSpeed - leftSpeed) / 2);
    }

    /** Arcade without strafing: one stick drives, the other turns. */
    public static void arcade(Follower follower, double forwardSpeed, double turnCcwSpeed) {
        follower.manual(forwardSpeed, 0, turnCcwSpeed);
    }

    /** Arcade with strafing, relative to the robot's own front. */
    public static void holonomic(Follower follower, double forwardSpeed,
                                 double strafeLeftSpeed, double turnCcwSpeed) {
        follower.manual(forwardSpeed, strafeLeftSpeed, turnCcwSpeed);
    }

    /**
     * Arcade with strafing, relative to the FIELD: pushing the stick away from
     * the driver moves the robot away from the driver, whichever way it faces.
     * Rotates the stick vector by minus the robot's heading.
     */
    public static void fieldRelative(Follower follower, double fieldXSpeed, double fieldYSpeed,
                                     double turnCcwSpeed) {
        double h = follower.pose().heading();
        double cos = Math.cos(h);
        double sin = Math.sin(h);
        follower.manual(fieldXSpeed * cos + fieldYSpeed * sin,
                -fieldXSpeed * sin + fieldYSpeed * cos, turnCcwSpeed);
    }

    /** Ignores stick values smaller than {@code band}. */
    public static double deadband(double value, double band) {
        return Math.abs(value) < band ? 0 : value;
    }

    /** Squares the stick, keeping its sign: fine control near the middle. */
    public static double squared(double value) {
        return value * Math.abs(value);
    }
}
