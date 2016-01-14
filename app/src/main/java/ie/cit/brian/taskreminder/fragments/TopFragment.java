package ie.cit.brian.taskreminder.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toolbar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import ie.cit.brian.taskreminder.activities.PreferenceActivity;
import ie.cit.brian.taskreminder.R;
import ie.cit.brian.taskreminder.TaskController;


/**
 * Created by briancoveney on 11/29/15.
 */
public class TopFragment extends Fragment {

    private TaskSearcher searcher;

    //The interface which this fragment uses to communicate up to its Activity
    public interface TaskSearcher
    {
        public void refreshTaskList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        Toolbar myToolBar = (Toolbar)getActivity().findViewById(R.id.my_toolbar);
        getActivity().setActionBar(myToolBar);
        getActivity().getActionBar().setDisplayShowTitleEnabled(true);


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


    //Inflate ActionBar
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }



    //Action bar options
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Dialog user entries
        switch (item.getItemId()) {
            case R.id.action_add_dialog:

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                alertDialog.setTitle(R.string.dialog_task_title);
                alertDialog.setMessage(R.string.dialog_task_message);

                //EditText to get user input
                final EditText taskName = new EditText(getActivity());
                final EditText taskDesc = new EditText(getActivity());


                //Custom Dialog
                LinearLayout dialogLayout = new LinearLayout(getActivity().getApplicationContext());
                dialogLayout.setOrientation(LinearLayout.VERTICAL);
                dialogLayout.addView(taskName);
                dialogLayout.addView(taskDesc);


                alertDialog.setView(dialogLayout);

                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
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

                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled by default.
                    }
                });

                alertDialog.show();

                break;

            case R.id.action_settings_pref:

                Intent i = new Intent(getActivity(), PreferenceActivity.class);
                startActivity(i);

                break;

            default:
                break;
        }
        return true;
    }

}




