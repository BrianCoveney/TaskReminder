package ie.cit.brian.taskreminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.ListFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by briancoveney on 11/26/15.
 */
public class AddTaskDialogFragment extends DialogFragment {


    private ArrayAdapter adapter;
    private ArrayList<Task> taskList;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dialog_task_title);
        builder.setMessage(R.string.dialog_task_message);
        // Set an EditText view to get user input
        final EditText inputField = new EditText(getActivity());
        builder.setView(inputField);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {



                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                        Button myButton = (Button) getActivity().findViewById(R.id.addButton);
                        myButton.setText(inputField.getText().toString());


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        //Create the AlertDialog and return it
        return builder.create();


    }

}
