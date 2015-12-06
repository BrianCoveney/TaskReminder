package ie.cit.brian.taskreminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toolbar;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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


//        setUpListeners();


        Toolbar myToolBar = (Toolbar)getActivity().findViewById(R.id.my_toolbar);
        getActivity().setActionBar(myToolBar);
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);


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


    //ActionBar Write to File
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater)
    {
        super.onCreateOptionsMenu(menu, inflater);
    }



//    private void setUpListeners()
//    {
//
//        spinnerTitle = (TextView) getActivity().findViewById(R.id.spinnerTitle);
//        addBtn = (Button) getActivity().findViewById(R.id.closeAddBtn);
//
//
//        addBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Spinner taskSpinner = (Spinner) getActivity().findViewById(R.id.spinner);
//                String selected = taskSpinner.getSelectedItem().toString();
//
//                //Calls out to the controller to put the data into the model,
//                //then tells the bottom fragment to refresh itself from the updated model
//                TaskController.getInstance().addTask(selected);
//                searcher.refreshTaskList();
//
//            }
//        });
//    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Spinner taskSpinner = (Spinner)getActivity().findViewById(R.id.spinner);
        TextView spinnerTitle = (TextView)getActivity().findViewById(R.id.spinnerTitle);




        //Dialog user entries
        switch (item.getItemId()) {
            case R.id.action_add_dialog:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle(R.string.dialog_task_title);
                alertDialog.setMessage(R.string.dialog_task_message);

                //EditText to get user input
                final EditText taskName = new EditText(getActivity());
                final EditText taskDesc = new EditText(getActivity());
                final EditText taskTime = new EditText(getActivity());
                final EditText taskDate = new EditText(getActivity());


                //Custom Dialog
                LinearLayout dialogLayout = new LinearLayout(getActivity().getApplicationContext());
                dialogLayout.setOrientation(LinearLayout.VERTICAL);
                dialogLayout.addView(taskName);
                dialogLayout.addView(taskDesc);
                dialogLayout.addView(taskTime);
                dialogLayout.addView(taskDate);



                alertDialog.setView(dialogLayout);

                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String name = taskName.getText().toString();
                        String description = taskDesc.getText().toString();

                        DateFormat dateTimeFormat = new SimpleDateFormat("HH:mm");
                        String time = dateTimeFormat.format(Calendar.getInstance().getTime());

                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String date = dateFormat.format(Calendar.getInstance().getTime());

                        TaskController.getInstance().addTask(name, description, time, date);
                        searcher.refreshTaskList();
                    }
                });

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

                alertDialog.show();

                break;

            case R.id.action_add_spinner:
                spinnerTitle.setVisibility(View.VISIBLE);
                taskSpinner.setVisibility(View.VISIBLE);
                break;

            case R.id.action_remove_spinner:
                spinnerTitle.setVisibility(View.GONE);
                taskSpinner.setVisibility(View.GONE);

            default:
                break;
        }
        return true;
    }

    public void myCalendar()
    {
        Calendar cal = Calendar.getInstance();
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra("beginTime", cal.getTimeInMillis());
        intent.putExtra("allDay", true);
        intent.putExtra("rrule", "FREQ=YEARLY");
        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
        intent.putExtra("title", "A Test Event from android app");
        startActivity(intent);
    }


}




