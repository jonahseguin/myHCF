/*
 * Copyright (c) Jonah Seguin (Shawckz) 2016.  You may not copy, re-sell, distribute, modify, or use any code contained in this document or file, collection of documents or files, or project.  Thank you.
 */

package com.shawckz.myhcf.util;

public class TimeUtil {

    public static String formatTime(long time) {
        int seconds = (int) (time / 1000) % 60;
        int minutes = (int) ((time / (1000 * 60)) % 60);
        int hours = (int) ((time / (1000 * 60 * 60)) % 24);

        String h = (hours > 0 ? hours + "h" : "0h");
        String m = (minutes > 0 ? minutes + "m" : "0m");
        String s = (seconds > 0 ? seconds + "s" : "0s");

        //1h, 20m, 30s
        return h + ", " + m + ", " + s;
    }

    public static String formatTimeClockStyle(long time) {
        //int seconds = (int) (time / 1000) % 60;
        int minutes = (int) ((time / (1000 * 60)) % 60);
        int hours = (int) ((time / (1000 * 60 * 60)) % 24);

        //12:30
        return hours+":" + minutes;
    }

}
