package nl.yrck.mprog_to_dolist.dialogs;


import android.content.Context;
import android.support.v7.app.AlertDialog;

public class RemoveListDialog {

    private ArgumentListener argumentListener;

    public RemoveListDialog(ArgumentListener argumentListener) {
        this.argumentListener = argumentListener;
    }

    public void show(long list_id, Context context) {
        new AlertDialog.Builder(context)
                .setTitle("Delete list")
                .setMessage("Remove this list?")
                .setPositiveButton("Delete", ((dialogInterface, i) -> argumentListener.onClick(list_id)))
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    public interface ArgumentListener {
        void onClick(long list_id);
    }
}

