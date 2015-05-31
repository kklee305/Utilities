package com.kklee.utilities.Logger;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.TextView;

/**
 * Created by Keith on 31/05/2015.
 */
public class ViewLogDialogFactory {

    public static void showDialog(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, 2);
        builder.setTitle("Log")
                .setMessage(activity.getSharedPreferences(Logger.LOGGER_SHARED_PREF, Context.MODE_PRIVATE).getString(Logger.LOGGER_SHARED_PREF, ""));
        Dialog dialog = builder.create();
        dialog.show();
        TextView tv = (TextView) dialog.findViewById(android.R.id.message);
        tv.setTextSize(8);
    }

}
