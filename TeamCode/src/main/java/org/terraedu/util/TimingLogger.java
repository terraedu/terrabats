package org.terraedu.util;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class TimingLogger {
    private static final String FILE_PATH = "/sdcard/FIRST/timing_log.csv";
    private static boolean initialized = false;

    public static void log(String phase, String subsystem, double timeMs) {
        try {
            initializeIfNeeded();

            try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH, true))) {
                writer.printf("%s,%s,%.2f%n", phase, subsystem, timeMs);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void initializeIfNeeded() throws IOException {
        if (initialized) return;

        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            writer.println("phase,subsystem,time_ms");
        }

        initialized = true;
    }
}
