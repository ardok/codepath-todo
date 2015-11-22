package com.codepath.simpletodo.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Formatter {
    public static String formatCalendar(Calendar calendar) {
        final Calendar now = Calendar.getInstance();
        final int nowYear = now.get(Calendar.YEAR);
        final int year = calendar.get(Calendar.YEAR);

        SimpleDateFormat format;
        if (nowYear == year) {
            format = new SimpleDateFormat("MMM d, HH:mm", Locale.US);
        } else {
            format = new SimpleDateFormat("MMM d, yyyy HH:mm", Locale.US);
        }

        return format.format(calendar.getTime());
    }
}
