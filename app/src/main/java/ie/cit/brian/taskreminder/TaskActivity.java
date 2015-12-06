package ie.cit.brian.taskreminder;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by briancoveney on 11/28/15.
 */
public class TaskActivity extends FragmentActivity {

    private Task theTask;
    private TextView taskName;

    static TextView myDateTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        myDateTextView = (TextView) findViewById(R.id.date_display);



        //get the Task from the bottom fragment and display it in a new activity's textview
        theTask = (Task) getIntent().getSerializableExtra("selectedTask");
        taskName = (TextView) findViewById(R.id.task_name);
        taskName.setText("Task Name: " + theTask.getTaskName() + "\n\n" +
                "Task Description: " + theTask.getTaskDescription() + "\n\n" );
//                + "Task Date: " + theTask.getTaskDueDate());

    }


    public void setDate(View view)
    {
        PickDialogs pickDialogs = new PickDialogs();
        pickDialogs.show(getFragmentManager(), "date_picker");
    }






}
