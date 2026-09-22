package org.firstinspires.ftc.teamcode.base;

import com.pedropathing.follower.Follower;

/**
 * Stick mappings. Each one turns joystick numbers into the three numbers a
 * mecanum base takes: forward, lateral (left is positive) and turn.
 *
 * <p>All of these are OPEN LOOP. {@code follower.manual(...)} applies no
 * tuning and no feedback, so nothing here corrects heading drift --
 * see {@link HeadingHold} for that.
 */
public final class Drive {

    private Drive() {
    }

    /** Tank: one stick per side. No strafing. */
    public static void tank(Follower follower, double left, double right) {
        follower.manual((left + right) / 2, 0, (left - right) / 2);
    }

    /** Arcade without strafing: one stick drives, the other turns. */
    public static void arcade(Follower follower, double forward, double turn) {
        follower.manual(forward, 0, turn);
    }

    /** Arcade with strafing, relative to the robot's own front. */
    public static void holonomic(Follower follower, double forward, double lateral, double turn) {
        follower.manual(forward, lateral, turn);
    }

    /**
     * Arcade with strafing, relative to the FIELD: pushing the stick away from
     * the driver moves the robot away from the driver, whichever way it faces.
     * Rotates the stick vector by minus the robot's heading.
     */
    public static void fieldRelative(Follower follower, double fieldX, double fieldY, double turn) {
        double h = follower.pose().heading();
        double cos = Math.cos(h);
        double sin = Math.sin(h);
        follower.manual(fieldX * cos + fieldY * sin, -fieldX * sin + fieldY * cos, turn);
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
