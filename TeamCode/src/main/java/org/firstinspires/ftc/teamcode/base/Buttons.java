package org.firstinspires.ftc.teamcode.base;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Runs Ivy commands when gamepad buttons are pressed.
 *
 * <p>Ivy has commands, groups and a scheduler, but nothing that connects a
 * button to a command, so this fills that gap. Call {@link #poll()} once per
 * loop.
 */
public final class Buttons {

    private interface Binding {
        void poll();
    }

    private final List<Binding> bindings = new ArrayList<>();

    /** Runs the command once, each time the button goes from up to down. */
    public Buttons whenPressed(BooleanSupplier button, Command command) {
        bindings.add(new Binding() {
            private boolean was;

            @Override
            public void poll() {
                boolean now = button.getAsBoolean();
                if (now && !was) Scheduler.schedule(command);
                was = now;
            }
        });
        return this;
    }

    /** Runs the command once, each time the button is let go. */
    public Buttons whenReleased(BooleanSupplier button, Command command) {
        bindings.add(new Binding() {
            private boolean was;

            @Override
            public void poll() {
                boolean now = button.getAsBoolean();
                if (!now && was) Scheduler.schedule(command);
                was = now;
            }
        });
        return this;
    }

    /** Runs while the button is held, and cancels it on release. */
    public Buttons whileHeld(BooleanSupplier button, Command command) {
        bindings.add(new Binding() {
            private boolean was;

            @Override
            public void poll() {
                boolean now = button.getAsBoolean();
                if (now && !was) Scheduler.schedule(command);
                if (!now && was) Scheduler.cancel(command);
                was = now;
            }
        });
        return this;
    }

    /** Call once per loop, before the scheduler runs. */
    public void poll() {
        for (int i = 0; i < bindings.size(); i++) bindings.get(i).poll();
    }

    public int size() {
        return bindings.size();
    }
}
