package com.example.ktbapi.common;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public final class TimeUtil {
    private TimeUtil() {}

    private static final ZoneId ZONE = ZoneId.of("Asia/Seoul");
    private static final DateTimeFormatter DISP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static String format(LocalDateTime time) {
        if (time == null) return null;
        return time.atZone(ZONE).format(DISP);
    }
}
