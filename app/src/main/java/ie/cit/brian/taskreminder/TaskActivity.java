package ie.cit.brian.taskreminder;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;


/**
 * Created by briancoveney on 11/28/15.
 */
public class TaskActivity extends FragmentActivity {

    private Task theTask;
    private TextView taskName;
    private TextView taskDesc;
    private TextView taskTime;

    static TextView taskDate;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        //get the Task from the bottom fragment and display it in a new activity's textview
        theTask = (Task) getIntent().getSerializableExtra("selectedTask");
        taskName = (TextView) findViewById(R.id.task_name);
        taskDesc = (TextView) findViewById(R.id.task_desc);
        taskTime = (TextView) findViewById(R.id.task_time);


        taskName.setText("Task Name: " + theTask.getTaskName());
        taskDesc.setText("Task Description: " + theTask.getTaskDescription());
        taskTime.setText("Task Time: " + theTask.getTaskTime());

        taskDate = (TextView) findViewById(R.id.task_date);
        taskDate.setText("Task Date: " + theTask.getTaskDate());

    }


    public void setDate(View view)
    {
        PickDialogs pickDialogs = new PickDialogs();
        pickDialogs.show(getFragmentManager(), "date_picker");
    }

}
