package org.firstinspires.ftc.teamcode.base.odometry;

/**
 * Where wheel travel and heading come from.
 *
 * <p>Splitting this out means a localizer can be tested on a laptop with made
 * up numbers, instead of only on the robot.
 */
public interface WheelSource {

    /**
     * How far each wheel has rolled since the robot turned on, in inches, as
     * {@code {frontLeft, frontRight, backLeft, backRight}}. Forward is positive.
     */
    double[] wheelInches();

    /** Which way the robot faces, in radians, counterclockwise positive. */
    double headingRadians();
}
