package ie.cit.brian.taskreminder;

import android.app.Activity;
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

/**
 * Created by briancoveney on 11/28/15.
 */
public class TaskActivity extends FragmentActivity{

    private Task theTask;
    private Task theTask2;

    private TextView taskName;
    private TextView taskDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //get the Task from the bottom fragment and display it in a new activity's textview
        theTask = (Task) getIntent().getSerializableExtra("selectedTask");
        taskName = (TextView) findViewById(R.id.task_name);
        taskName.setText("Task Name: " + theTask.getTaskName() +"\n\n"+
                "Task Description: " + theTask.getTaskDescription() +"\n\n"+
                "Task Date: " + theTask.getTaskDueDate());


        //does not work
//        theTask2 = (Task) getIntent().getSerializableExtra("selectedTask");
//        taskDesc = (TextView) findViewById(R.id.task_desc);
//        taskDesc.setText(theTask2.toString());


    }

}
