package ie.cit.brian.taskreminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toolbar;


/**
 * Created by briancoveney on 11/29/15.
 */
public class TopFragment extends Fragment {

    private Button addBtn;
    private Spinner taskSpinner;
    private TextView spinnerTitle;


    private TaskSearcher searcher;

    //The interface which this fragment uses to communicate up to its Activity
    public interface TaskSearcher
    {
        public void refreshTaskList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {


        setUpListeners();
//        myDialogListener();

        //hiding spinner until user adds a task from the Taskbar
        taskSpinner = (Spinner) getActivity().findViewById(R.id.spinner);
        spinnerTitle = (TextView) getActivity().findViewById(R.id.spinnerTitle);
        spinnerTitle.setVisibility(View.GONE);
        taskSpinner.setVisibility(View.GONE);

        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public void onAttach(Activity activity) {
        searcher = (TaskSearcher) activity;
        super.onAttach(activity);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        return inflater.inflate(R.layout.fragment_first, container, false);
    }







    private void setUpListeners()
    {

        spinnerTitle = (TextView) getActivity().findViewById(R.id.spinnerTitle);
        addBtn = (Button) getActivity().findViewById(R.id.closeAddBtn);


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Spinner taskSpinner = (Spinner) getActivity().findViewById(R.id.spinner);
                String selected = taskSpinner.getSelectedItem().toString();

                //Calls out to the controller to put the data into the model,
                //then tells the bottom fragment to refresh itself from the updated model
                TaskController.getInstance().addTask(selected);
                searcher.refreshTaskList();

            }
        });
    }


    public void myDialogListener()
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

//                Button myButton = (Button) getActivity().findViewById(R.id.addButton);
//                myButton.setText(inputField.getText().toString());

                String tasks = inputField.getText().toString();

                TaskController.getInstance().addTask(tasks);
                searcher.refreshTaskList();


            }
        })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        //Create the AlertDialog and return it
        AlertDialog alertDialog = builder.create();

        alertDialog.show();

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {



        switch (item.getItemId()) {
            case R.id.action_add_dialog:

                myDialogListener();


            default:
                break;
        }
        return true;
    }






}



