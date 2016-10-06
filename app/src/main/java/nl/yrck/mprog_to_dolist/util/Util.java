package nl.yrck.mprog_to_dolist.util;

import android.content.Context;
import android.support.v7.app.AlertDialog;

public class Util {
    public static void aboutDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("By Yorick de Boer")
                .setTitle("About")
                .setCancelable(true)
                .setNeutralButton("DISMISS", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}

