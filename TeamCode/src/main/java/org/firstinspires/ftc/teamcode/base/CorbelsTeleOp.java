package org.firstinspires.ftc.teamcode.base;

/**
 * A teleop: the driver's sticks and buttons, on top of {@link CorbelsOpMode}.
 *
 * <p>A lesson writes {@code init}, {@code start}, {@code loop} and {@code stop}
 * around the framework's {@code Before} and {@code After} hooks, and may
 * override {@link #bindings()} to connect buttons to commands. A lesson that
 * has no reason to write {@code loop()} may implement {@link #drive()} instead.
 * Everything else (the hardware, the follower, the scheduler, Panels) is
 * handled for it.
 */
public abstract class CorbelsTeleOp extends CorbelsOpMode {

    protected Buttons buttons;

    /**
     * Stick code, for a lesson that leaves {@link #loop()} alone. Runs every
     * loop, before the follower updates. A lesson that writes its own
     * {@code loop()} puts the stick code there instead and leaves this empty.
     */
    protected void drive() {
    }

    /** Connect buttons to commands. Runs once, when the OpMode starts. */
    protected void bindings() {
    }

    @Override
    protected final void onStart() {
        buttons = new Buttons();
        bindings();
        // Park the follower where it will pass the sticks through. Its own mode
        // starts IDLE, and in IDLE it stops the drivetrain every loop instead of
        // driving it.
        follower.manual(0, 0, 0);
    }

    @Override
    protected final void onLoop() {
        drive();
    }

    @Override
    protected void pollInputs() {
        if (buttons != null) buttons.poll();
    }
}
