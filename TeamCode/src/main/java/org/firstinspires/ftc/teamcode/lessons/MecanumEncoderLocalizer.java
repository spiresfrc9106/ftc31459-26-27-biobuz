package org.firstinspires.ftc.teamcode.lessons;

import com.pedropathing.localization.Localizer;
import com.pedropathing.localization.MotionState;
import com.pedropathing.math.Pose;
import com.pedropathing.math.Twist;
import com.pedropathing.api.PoseFactory;

import org.firstinspires.ftc.teamcode.base.HeadingHold;
import org.firstinspires.ftc.teamcode.base.odometry.WheelSource;

import java.util.function.LongSupplier;

/**
 * L8: work out where the robot is from the drive wheels and the IMU.
 *
 * <p>Pedro 3 has no localizer like this -- Pinpoint and dead wheels only -- so
 * this is ours. Every loop it asks how far each wheel rolled, turns that into
 * "forward" and "left" for the robot, rotates it by the heading, and adds it to
 * where it thought it was. That is dead reckoning: no wheel ever admits to
 * slipping, so the error only grows.
 */
public class MecanumEncoderLocalizer implements Localizer {

    private static final PoseFactory POSES = PoseFactory.radians();

    private final WheelSource source;
    private final LongSupplier clockNanos;

    private double x;
    private double y;
    private double headingOffset;
    private double[] lastWheels;
    private double lastRawHeading;
    private long lastNanos;
    private MotionState state = MotionState.zero();

    public MecanumEncoderLocalizer(WheelSource source) {
        this(source, System::nanoTime);
    }

    public MecanumEncoderLocalizer(WheelSource source, LongSupplier clockNanos) {
        this.source = source;
        this.clockNanos = clockNanos;
    }

    @Override
    public void update() {
        double[] wheels = source.wheelInches();
        double rawHeading = source.headingRadians();
        long now = clockNanos.getAsLong();

        if (lastWheels == null) {                       // first loop: nothing to compare with
            lastWheels = wheels.clone();
            lastRawHeading = rawHeading;
            lastNanos = now;
            publish(0, 0, 0);
            return;
        }

        double dFrontLeft = wheels[0] - lastWheels[0];
        double dFrontRight = wheels[1] - lastWheels[1];
        double dBackLeft = wheels[2] - lastWheels[2];
        double dBackRight = wheels[3] - lastWheels[3];

        double forwardIn = (dFrontLeft + dFrontRight + dBackLeft + dBackRight) / 4;
        double strafeLeftIn = (-dFrontLeft + dFrontRight + dBackLeft - dBackRight) / 4;

        double dHeading = HeadingHold.wrap(rawHeading - lastRawHeading);
        double midHeading = lastRawHeading + headingOffset + dHeading / 2;   // halfway through the step

        x += forwardIn * Math.cos(midHeading) - strafeLeftIn * Math.sin(midHeading);
        y += forwardIn * Math.sin(midHeading) + strafeLeftIn * Math.cos(midHeading);

        double seconds = (now - lastNanos) / 1e9;
        lastWheels = wheels.clone();
        lastRawHeading = rawHeading;
        lastNanos = now;

        if (seconds > 0) {
            publish(forwardIn / seconds, strafeLeftIn / seconds, dHeading / seconds);
        } else {
            publish(0, 0, 0);
        }
    }

    private void publish(double forwardSpeedInPerS, double strafeLeftSpeedInPerS,
                         double turnCcwSpeedRadPerS) {
        state = MotionState.ofTwist(
                POSES.of(x, y, lastRawHeading + headingOffset),
                new Twist(forwardSpeedInPerS, strafeLeftSpeedInPerS, turnCcwSpeedRadPerS));
    }

    @Override
    public void setPose(Pose pose) {
        x = pose.x();
        y = pose.y();
        headingOffset = pose.heading() - lastRawHeading;
        publish(0, 0, 0);
    }

    @Override
    public MotionState state() {
        return state;
    }

    @Override
    public void reset() {
        x = 0;
        y = 0;
        headingOffset = -lastRawHeading;
        lastWheels = null;
        publish(0, 0, 0);
    }
}
