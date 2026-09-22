package org.firstinspires.ftc.teamcode.base;

import com.pedropathing.localization.Localizer;
import com.pedropathing.math.Pose;


import java.util.ArrayList;
import java.util.List;

/**
 * Runs extra localizers alongside the real one and logs what each of them
 * thinks. Nothing here steers the robot -- they only watch.
 *
 * <p>Each shadow sends {@code Localizer/<name>/x_in}, {@code /y_in} and
 * {@code /heading_deg} to Panels, so its numbers can be graphed next to the
 * follower's own pose while you drive.
 */
public final class Shadow {

    private static final class Entry {
        final String name;
        final Localizer localizer;

        Entry(String name, Localizer localizer) {
            this.name = name;
            this.localizer = localizer;
        }
    }

    private final List<Entry> entries = new ArrayList<>();

    public Shadow add(String name, Localizer localizer) {
        entries.add(new Entry(name, localizer));
        return this;
    }

    /** Puts every shadow at the same starting pose as the real localizer. */
    public void setPose(Pose pose) {
        for (Entry e : entries) e.localizer.setPose(pose);
    }

    /** Call once per loop: updates each shadow and logs its pose. */
    /** Where a shadow's numbers go. */
    public interface Recorder {
        void record(String key, double value);
    }

    /** Call once per loop: updates each shadow and sends its pose to Panels. */
    public void update(Recorder recorder) {
        for (Entry e : entries) {
            e.localizer.update();
            Pose p = e.localizer.pose();
            recorder.record("Localizer/" + e.name + "/x_in", p.x());
            recorder.record("Localizer/" + e.name + "/y_in", p.y());
            recorder.record("Localizer/" + e.name + "/heading_deg", Math.toDegrees(p.heading()));
        }
    }

    /** How far a shadow is from a reference pose, in inches. */
    public static double distance(Pose a, Pose b) {
        return Math.hypot(a.x() - b.x(), a.y() - b.y());
    }

    public int size() {
        return entries.size();
    }
}
