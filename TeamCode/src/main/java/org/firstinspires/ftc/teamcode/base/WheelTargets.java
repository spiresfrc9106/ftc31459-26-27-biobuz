package org.firstinspires.ftc.teamcode.base;

/**
 * Turns a command for the whole robot into a target for each wheel.
 *
 * <p>Everything here is in inches per second, and radians per second for the
 * turn. A mecanum robot moving forward at {@code f}, sideways at {@code s} and
 * turning at {@code omega} needs its wheels to travel at
 *
 * <pre>
 *   front left  = f - s - omega * radius
 *   front right = f + s + omega * radius
 *   back left   = f + s - omega * radius
 *   back right  = f - s + omega * radius
 * </pre>
 *
 * where {@code radius} is half the sum of the track width and the wheelbase --
 * how far a wheel is from the middle, along the diagonal it pushes.
 *
 * <p>Note what this is not: a power. Nothing here has been divided by a maximum
 * or clamped to 1. These are speeds the wheels should actually travel at, and
 * turning them into motor power is a separate job.
 */
public final class WheelTargets {

    private WheelTargets() {
    }

    /**
     * @param forwardIps how fast the robot should go forward, inches/second
     * @param leftIps    how fast it should go left, inches/second
     * @param turnRadps  how fast it should turn counter-clockwise, radians/second
     * @param turnRadiusIn how far a wheel sits from the centre, inches
     * @return front left, front right, back left, back right -- inches/second
     */
    public static double[] forMecanum(double forwardIps, double leftIps, double turnRadps,
                                      double turnRadiusIn) {
        double strafe = -leftIps;               // the mixing below is in terms of right
        double turn = turnRadps * turnRadiusIn;
        return new double[]{
                forwardIps - strafe - turn,
                forwardIps + strafe + turn,
                forwardIps + strafe - turn,
                forwardIps - strafe + turn};
    }
}
