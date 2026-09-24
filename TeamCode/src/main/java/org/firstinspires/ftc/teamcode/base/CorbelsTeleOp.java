package org.firstinspires.ftc.teamcode.base;

/**
 * A teleop: the driver's sticks and buttons, on top of {@link CorbelsOpMode}.
 *
 * <p>A lesson implements {@link #drive()} -- the stick code -- and may override
 * {@link #bindings()} to connect buttons to commands. Everything else (the
 * hardware, the follower, the scheduler, Panels) is handled for it.
 */
public abstract class CorbelsTeleOp extends CorbelsOpMode {

    protected Buttons buttons;

    /** Stick code. Runs every loop, before the follower updates. */
    protected abstract void drive();

    /** Connect buttons to commands. Runs once, when the OpMode starts. */
    protected void bindings() {
    }

    @Override
    protected final void onStart() {
        buttons = new Buttons();
        bindings();
    }

    @Override
    protected final void onLoop() {
        buttons.poll();
        drive();
    }
}
