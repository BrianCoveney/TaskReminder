package ie.cit.brian.taskreminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by briancoveney on 12/10/15.
 */
public class ShareTaskDialog extends DialogFragment {

    private Task theTask;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        theTask = (Task) getArguments().getSerializable("taskObject");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //Build the dialof and set up the button click handlers
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Share Task")
                .setPositiveButton("Send", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");

                        intent.putExtra(Intent.EXTRA_EMAIL, getResources().getStringArray(R.array.intent_email));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Task Info");

                        // Concatenating 'message', to be placed in Intent.EXTRA_TEXT.
                        // Pass Serializable Flight object, as an extra to the Email Intent.
                        String message = "";
                        message += getString(R.string.share_message1);
                        message += "\n\n" + theTask.getTaskName() +", "+ theTask.getTaskDescription() +", "+
                                theTask.getTaskTime() +", "+  theTask.getTaskDate();
                        message += "\n\n" + getString(R.string.share_message2);

                        intent.putExtra(Intent.EXTRA_TEXT, message);

                        startActivity(intent);

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });



        return builder.create();
    }
}
