package com.kklee.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.text.DateFormat;
import java.util.Objects;

/**
 * Created by Keith on 10/06/2014.
 * wtf,e,w,i,d,v
 */
public class Logger {

    public static final String LOGGER_SHARED_PREF = "LOGGER_SHARED_PREF";
    private static boolean IS_LOGGING = false;
    private static Context context = null;
    private static Logger instance;

    private Logger() {
    }

    public static void setIsLogging(Boolean set) {
        IS_LOGGING = set;
    }

    public static void setLogToFile(Context appContext) {
        context = appContext;
    }

    public static void wtf(String message, Objects... objects) {
        Log.wtf(getCallingInfo(), format(message, objects));
        logToFile("WTF/", message, objects);
    }

    public static void e(String message, Objects... objects) {
        Log.e(getCallingInfo(), format(message, objects));
        logToFile("E/", message, objects);
    }

    public static void e(String message, Exception e, Objects... objects) {
        Log.e(getCallingInfo(), format(message, objects) + " | Exception: " + e.getMessage());
        logToFile("E/", message + " | Exception: " + e.getMessage(), objects);
    }

    public static void w(String message, Objects... objects) {
        if (!IS_LOGGING) return;
        Log.w(getCallingInfo(), format(message, objects));
        logToFile("W/", message, objects);
    }

    public static void i(String message, Objects... objects) {
        if (!IS_LOGGING) return;
        Log.i(getCallingInfo(), format(message, objects));
    }

    public static void d(String message, Objects... objects) {
        if (!IS_LOGGING) return;
        Log.d(getCallingInfo(), format(message, objects));
    }

    public static void v(String message, Objects... objects) {
        if (!IS_LOGGING) return;
        Log.v(getCallingInfo(), format(message, objects));
    }

    private static void logToFile(String tag, String message, Objects... objects) {
        if (context == null) return;
        String toLog = "[" + getTimeStamp() + "] " + tag + getCallingInfo() + "--> " + format(message, objects) + "\n";
        //TODO temp writes to shared pref FOR NOW. should write to file
        SharedPreferences pref = context.getSharedPreferences(LOGGER_SHARED_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        String oldLog = pref.getString(LOGGER_SHARED_PREF, "");
        if (oldLog.length() > 2000) {
            oldLog = oldLog.substring(oldLog.length() / 2, oldLog.length());
        }
        String log = oldLog + toLog;
        editor.putString(LOGGER_SHARED_PREF, log);
        editor.apply();
    }

    private static String getTimeStamp() {
        return DateFormat.getDateTimeInstance().format(System.currentTimeMillis());
    }

    private static String format(String message, Objects... objects) {
        String content;
        if (objects.length == 0) {
            content = message;
        } else {
            content = String.format(message, objects);
        }
        return content;
    }

    private static String getCallingInfo() {
        StackTraceElement s = getCallingStackTraceElement();
        return s.getClassName() + "." + s.getMethodName() + "@" + s.getLineNumber();
    }

    private static StackTraceElement getCallingStackTraceElement() {
        StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
        String loggerClassName = getInstance().getClass().getName();

        boolean found = false;
        for (int i = 0; i < stackTraceElements.length; i++) {
            StackTraceElement s = stackTraceElements[i];

            if (!found) {
                if (s.getClassName().equalsIgnoreCase(loggerClassName)) {
                    found = true;
                }
                continue;
            }

            if (found) {
                if (!s.getClassName().equalsIgnoreCase(loggerClassName)) {
                    return s;
                }
            }
        }

        return null;
    }

    private static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

}