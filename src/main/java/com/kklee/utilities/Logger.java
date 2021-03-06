package com.kklee.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.TextView;

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

    public static void wtf(String message, Object... objects) {
        Log.wtf(getCallingInfo(), format(message, objects));
        logToFile("WTF/", message, objects);
    }

    public static void e(String message, Object... objects) {
        Log.e(getCallingInfo(), format(message, objects));
        logToFile("E/", message, objects);
    }

    public static void e(String message, Exception e, Object... objects) {
        Log.e(getCallingInfo(), format(message, objects) + " | Exception: " + e.getMessage());
        logToFile("E/", message + " | Exception: " + e.getMessage(), objects);
    }

    public static void w(String message, Object... objects) {
        if (!IS_LOGGING) return;
        Log.w(getCallingInfo(), format(message, objects));
        logToFile("W/", message, objects);
    }

    public static void i(String message, Object... objects) {
        if (!IS_LOGGING) return;
        Log.i(getCallingInfo(), format(message, objects));
    }

    public static void d(String message, Object... objects) {
        if (!IS_LOGGING) return;
        Log.d(getCallingInfo(), format(message, objects));
    }

    public static void v(String message, Object... objects) {
        if (!IS_LOGGING) return;
        Log.v(getCallingInfo(), format(message, objects));
    }

    private static void logToFile(String tag, String message, Object... objects) {
        if (context == null) return;
        String toLog = "[" + getTimeStamp() + "] " + tag + getCallingInfo() + ":  " + format(message, objects) + "\n";
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

    private static String format(String message, Object... objects) {
        String content;
        if (objects.length == 0) {
            content = message;
        } else {
            try {
                content = String.format(message, objects);
            }catch (java.util.UnknownFormatConversionException e) {
                Logger.e("Bad formatting! Check syntax", e);
                content = message;
            }
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

    public static void showDialog(final Activity activity) {
        if (activity == null || !IS_LOGGING) return;

        SharedPreferences pref = activity.getSharedPreferences(LOGGER_SHARED_PREF, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        AlertDialog.Builder builder = new AlertDialog.Builder(activity, 2);
        builder.setTitle("Log")
                .setMessage(pref.getString(Logger.LOGGER_SHARED_PREF, ""))
                .setNeutralButton("CLear", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editor.putString(LOGGER_SHARED_PREF, "");
                        editor.apply();
                    }
                });
        Dialog dialog = builder.create();
        dialog.show();
        TextView tv = (TextView) dialog.findViewById(android.R.id.message);
        tv.setTextSize(8);
    }

}