package ie.cit.brian.taskreminder.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import ie.cit.brian.taskreminder.R;
import ie.cit.brian.taskreminder.TaskController;
import ie.cit.brian.taskreminder.UtilityClass;
import ie.cit.brian.taskreminder.activities.MainActivity;


/**
 * Created by briancoveney on 11/29/15.
 */
public class FirstFragment extends Fragment {

    protected FloatingActionButton floatingBtn, floatingBtnLogin;
    private EditText taskName, taskDesc;


    private TaskSearcher searcher;

    //The interface which this fragment uses to communicate up to its Activity
    public interface TaskSearcher {
        public void refreshTaskList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // Moving login button to top menu bar
        addTaskFloatingButton();
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


    public void addTaskFloatingButton() {

        floatingBtn = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        floatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCustomAddTaskDiaglog();
            }
        });
    }


    // display customer dialog to add tasks
    public void myCustomAddTaskDiaglog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(
                getContext());

        builder.setTitle("Create Task");
        builder.setMessage("... and description");

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_alert, null);
        builder.setView(dialogView);

        taskName = (EditText) dialogView.findViewById(R.id.edT_name);
        taskDesc = (EditText) dialogView.findViewById(R.id.edT_desc);

        taskName.setHint("Task name");
        taskDesc.setHint("Task description");


        builder.setPositiveButton("Ok", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String name = taskName.getText().toString();
                String description = taskDesc.getText().toString();



                TimeZone timeZone = TimeZone.getTimeZone("GMT");
                DateFormat myTimeFormat = new SimpleDateFormat("HH:mm, a");
                myTimeFormat.setTimeZone(timeZone);
                String time = myTimeFormat.format(Calendar.getInstance(timeZone).getTime());

                Date date = Calendar.getInstance().getTime();
                //Populate the task details
                TaskController.getInstance().addTask(name, description, time, date);

                searcher.refreshTaskList();
            }
        });

        builder.setNegativeButton("Cancel", new AlertDialog.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled by default.
            }
        });


        final AlertDialog alertDialog = builder.create();

        alertDialog.show();
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setSaveEnabled(false);
        taskName.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                //Catches when user enter a number instead of a string
                String regexStr = "^[0-9]*$";

                if(editable == taskName.getEditableText()) {
                    if (!(editable.toString().trim().matches(regexStr))) {
                        taskName.setError(null);
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    } else {
                        taskName.setError("Computer says no!");
                        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);

                    }
                    // removes error message when numbers are deleted
                    if (editable.toString().equals("")) {
                        taskName.setError(null);
                    }
                }
            }
        });
    }
}





