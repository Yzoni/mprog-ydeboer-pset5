package nl.yrck.mprog_to_dolist.dialogs;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.widget.EditText;

public class AddListDialog {

    private ArgumentListener argumentListener;

    public AddListDialog(ArgumentListener argumentListener) {
        this.argumentListener = argumentListener;
    }

    public void show(Context context) {
        final EditText listName = new EditText(context);
        listName.setSingleLine();
        new AlertDialog.Builder(context)
                .setTitle("New list")
                .setMessage("Create a new list")
                .setView(listName)
                .setPositiveButton("Create", ((dialogInterface, i) ->
                        argumentListener.onClick(listName.getText().toString())))
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }

    public interface ArgumentListener {
        void onClick(String argument);
    }
}

