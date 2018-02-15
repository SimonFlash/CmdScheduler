package com.mcsimonflash.sponge.cmdscheduler.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    private static final Pattern TIMESPAN_PATTERN = Pattern.compile("(?<w>([0-9]+)[w])?(?<d>([0-9]+)[d])?(?<h>([0-9]+)[h])?(?<m>([0-9]+)[m])?(?<s>([0-9]+)[s])?(?<ms>([0-9]+)[m][s])?");
    private static final int[] UNIT_CONVERSIONS = new int[] {604800000, 86400000, 3600000, 60000, 1000, 1};
    private static final String[] UNIT_ABBREVIATIONS = new String[] {"w", "d", "h", "m", "s", "ms"};

    public static long parseTime(String timespan) {
        try {
            return Long.parseLong(timespan);
        } catch (NumberFormatException e) {
            Matcher matcher = TIMESPAN_PATTERN.matcher(timespan);
            if (matcher.matches()) {
                long time = 0;
                for (int i = 0; i < 6; i++) {
                    if (matcher.group(UNIT_ABBREVIATIONS[i]) != null) {
                        time += Long.parseLong(matcher.group(UNIT_ABBREVIATIONS[i])) * UNIT_CONVERSIONS[i];
                    }
                }
                return time;
            }
            throw new IllegalArgumentException("Invalid timespan argument. Argument:[" + timespan + "]");
        }
    }

    public static String printTime(long time) {
        if (time <= 0) {
            return time + "s";
        }
        StringBuilder timespan = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            if (time / UNIT_CONVERSIONS[i] > 0) {
                timespan.append(time / UNIT_CONVERSIONS[i]).append(UNIT_ABBREVIATIONS[i]);
                time = time % UNIT_CONVERSIONS[i];
            }
        }
        return timespan.toString();
    }

}