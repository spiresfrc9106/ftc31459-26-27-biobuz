package org.firstinspires.ftc.teamcode.base;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.firstinspires.ftc.teamcode.lessons.L15Combined;
import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Three rules about hardware, which are easy to break by accident:
 * device names live in Constants, every device is looked up once, and the
 * lookups happen at init -- not after the match has started.
 */
public class HardwareRulesTest {

    @Test
    public void everyDeviceIsLookedUpDuringInitAndNotAfter() {
        OpModeHarness h = new OpModeHarness(new L15Combined());
        assertEquals("nothing before init", 0, h.lookups);

        h.init();
        int afterInit = h.lookups;
        assertTrue("init resolves the hardware", afterInit > 0);

        h.start();
        assertEquals("start looks nothing up", afterInit, h.lookups);

        h.loops(20, 0);
        assertEquals("neither does the loop", afterInit, h.lookups);

        h.stop();
        assertEquals("nor stopping", afterInit, h.lookups);
    }

    @Test
    public void theHardwareIsResolvedExactlyOnce() {
        OpModeHarness h = new OpModeHarness(new L15Combined());
        h.init();
        h.start();
        h.loops(5, 0);
        assertEquals("once, at init", 1, h.lookups);
    }

    @Test
    public void theLocalizerReadsTheDevicesTheOpModeWasGiven() {
        OpModeHarness h = new OpModeHarness(new L15Combined());
        h.init();
        L15Combined opMode = (L15Combined) h.opMode();
        assertSame("the same motor object, not a second handle",
                h.motors.get(Constants.frontLeftName).device, opMode.hardware.frontLeft);
        assertSame(h.motors.get(Constants.frontRightName).device, opMode.hardware.frontRight);
        assertSame(h.motors.get(Constants.backLeftName).device, opMode.hardware.backLeft);
        assertSame(h.motors.get(Constants.backRightName).device, opMode.hardware.backRight);
        assertSame(h.imu.device, opMode.hardware.imu);
    }

    @Test
    public void noDeviceNameAppearsOutsideConstants() throws Exception {
        Path base = sourceRoot();
        assertNotNull("could not find the sources to scan", base);

        String[] names = {Constants.frontLeftName, Constants.frontRightName,
                Constants.backLeftName, Constants.backRightName, Constants.imuName};
        List<String> offenders = new ArrayList<>();
        try (Stream<Path> files = Files.walk(base)) {
            for (Path file : files.filter(f -> f.toString().endsWith(".java"))
                    .collect(Collectors.toList())) {
                String name = file.getFileName().toString();
                if (name.equals("Constants.java")) continue;
                if (file.toString().contains("procedures")) continue;   // tuners ask the driver
                String code = withoutComments(new String(Files.readAllBytes(file), "UTF-8"));
                for (String device : names) {
                    if (code.contains("\"" + device + "\"")) {
                        offenders.add(name + " hardcodes \"" + device + "\"");
                    }
                }
            }
        }
        if (!offenders.isEmpty()) fail(String.join("; ", offenders));
    }

    /**
     * The sources, whether the tests run from the module folder (Gradle) or the
     * repository root (an IDE, or by hand). Returns null if neither is there --
     * and the test fails on that rather than passing silently, which is how a
     * real offender slipped through once.
     */
    private static Path sourceRoot() {
        for (String candidate : new String[]{
                "src/main/java/org/firstinspires/ftc/teamcode",
                "TeamCode/src/main/java/org/firstinspires/ftc/teamcode"}) {
            Path path = Paths.get(candidate);
            if (Files.isDirectory(path)) return path;
        }
        return null;
    }

    /** Strips // and /* *\/ comments, so documentation can say a name aloud. */
    static String withoutComments(String source) {
        return source
                .replaceAll("(?s)/\\*.*?\\*/", "")
                .replaceAll("(?m)//.*$", "");
    }

    @Test
    public void theCommentStripperLeavesCodeAlone() {
        // The whitespace before a trailing comment stays; only the comment goes.
        assertEquals("String a = \"keep\";   \n",
                withoutComments("String a = \"keep\";   // drop \"this\"\n"));
        assertEquals("\ncode();", withoutComments("/** doc with \"imu\" in it */\ncode();"));
    }
}
