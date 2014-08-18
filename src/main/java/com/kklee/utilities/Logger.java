package com.kklee.utilities;

import android.util.Log;

/**
 * Created by Keith on 10/06/2014.
 */
public class Logger {

    private static boolean IS_LOGGING = true;

    public static void d(String tag, String message) {
        if (!IS_LOGGING) return;
        Log.d(tag, message);
    }

    public static void e(String message) {
        Log.e("ERROR", message);
        logToFile("ERROR", message);
    }

    private static void logToFile(String tag, String message) {

    }

}