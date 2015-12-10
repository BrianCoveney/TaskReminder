package ie.cit.brian.taskreminder;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
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
import java.util.TimeZone;


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
        getActivity().getActionBar().setDisplayShowTitleEnabled(false);

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
//                final EditText taskTime = new EditText(getActivity());
//                final EditText taskDate = new EditText(getActivity());


                //Custom Dialog
                LinearLayout dialogLayout = new LinearLayout(getActivity().getApplicationContext());
                dialogLayout.setOrientation(LinearLayout.VERTICAL);
                dialogLayout.addView(taskName);
                dialogLayout.addView(taskDesc);
//                dialogLayout.addView(taskTime);
//                dialogLayout.addView(taskDate);



                alertDialog.setView(dialogLayout);

                alertDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {


                        String name = taskName.getText().toString();
                        String description = taskDesc.getText().toString();


                        TimeZone timeZone = TimeZone.getTimeZone("GMT");
                        DateFormat myTimeFormat = new SimpleDateFormat("HH:mm, a");
                        myTimeFormat.setTimeZone(timeZone);
                        String time = myTimeFormat.format(Calendar.getInstance(timeZone).getTime());



                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        String date = dateFormat.format(Calendar.getInstance().getTime());






//                        String date = "";

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

//            case R.id.action_add_spinner:
//                // Add preferences later...
//
//                break;
//
//            case R.id.action_remove_spinner:
//                // Add something else later...

            default:
                break;
        }
        return true;
    }

//    public void myCalendar()
//    {
//        Calendar cal = Calendar.getInstance();
//        Intent intent = new Intent(Intent.ACTION_EDIT);
//        intent.setType("vnd.android.cursor.item/event");
//        intent.putExtra("beginTime", cal.getTimeInMillis());
//        intent.putExtra("allDay", true);
//        intent.putExtra("rrule", "FREQ=YEARLY");
//        intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
//        intent.putExtra("title", "A Test Event from android app");
//        startActivity(intent);
//    }


}




