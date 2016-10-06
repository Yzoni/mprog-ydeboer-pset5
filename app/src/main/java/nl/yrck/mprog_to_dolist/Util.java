package nl.yrck.mprog_to_dolist;

import android.content.Context;
import android.support.v4.util.LongSparseArray;
import android.support.v7.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

class Util {
    static void aboutDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("By Yorick de Boer")
                .setTitle("About")
                .setCancelable(true)
                .setNeutralButton("DISMISS", (dialog, which) -> dialog.dismiss());
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    static <C> List<C> convertLongParseArrayToList(LongSparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<>(sparseArray.size());

        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }
}

