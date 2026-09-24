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
    public void eachDeviceIsAskedForOnlyOnce() {
        OpModeHarness h = new OpModeHarness(new L15Combined());
        h.init();
        h.start();
        h.loops(5, 0);
        // Four motors and an IMU. Pedro's drivetrain is a fake in tests, so
        // what is counted here is entirely our own.
        assertEquals("five devices, five lookups", 5, h.lookups);
    }

    @Test
    public void theHardwareIsTheOneTheLocalizerReads() {
        OpModeHarness h = new OpModeHarness(new L15Combined());
        h.init();
        RobotHardware hardware = new RobotHardware(h.hardwareMap);
        assertSame("the same motor object, not a second handle",
                h.motors.get(Constants.frontLeftName), hardware.frontLeft);
        assertSame(h.motors.get(Constants.frontRightName), hardware.frontRight);
        assertSame(h.motors.get(Constants.backLeftName), hardware.backLeft);
        assertSame(h.motors.get(Constants.backRightName), hardware.backRight);
        assertSame(h.imu, hardware.imu);
    }

    @Test
    public void noDeviceNameAppearsOutsideConstants() throws Exception {
        Path base = Paths.get("src/main/java/org/firstinspires/ftc/teamcode");
        if (!Files.isDirectory(base)) return;          // run from another folder

        String[] names = {Constants.frontLeftName, Constants.frontRightName,
                Constants.backLeftName, Constants.backRightName, Constants.imuName};
        List<String> offenders = new ArrayList<>();
        try (Stream<Path> files = Files.walk(base)) {
            for (Path file : files.filter(f -> f.toString().endsWith(".java"))
                    .collect(Collectors.toList())) {
                String name = file.getFileName().toString();
                if (name.equals("Constants.java")) continue;
                if (file.toString().contains("procedures")) continue;   // tuners ask the driver
                String text = new String(Files.readAllBytes(file), "UTF-8");
                for (String device : names) {
                    if (text.contains("\"" + device + "\"")) {
                        offenders.add(name + " hardcodes \"" + device + "\"");
                    }
                }
            }
        }
        if (!offenders.isEmpty()) fail(String.join("; ", offenders));
    }
}
