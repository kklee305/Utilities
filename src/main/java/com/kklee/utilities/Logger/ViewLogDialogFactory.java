package com.kklee.utilities.Logger;

import android.app.Activity;
import android.app.Dialog;
import android.support.v7.app.AlertDialog;

/**
 * Created by Keith on 31/05/2015.
 */
public class ViewLogDialogFactory {

    public static Dialog createDialog(final Activity activity, String log) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, 2);
        builder.setTitle("Log")
                .setMessage(log);
        return builder.create();
    }

}
