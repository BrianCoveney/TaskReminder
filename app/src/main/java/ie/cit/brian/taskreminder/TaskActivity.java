package ie.cit.brian.taskreminder;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


/**
 * Created by briancoveney on 11/28/15.
 */
public class TaskActivity extends FragmentActivity {

    private Task theTask;
    private TextView taskName, taskDesc, taskTime, taskDate;
    private Button shareBtn, saveBtn;
    String selectedDate;


    final Calendar cal = Calendar.getInstance();

    public interface taskReader
    {
        public void readTasks();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        populateTasks();
        shareTaskDialog();
        saveOnClick();
    }


    public void populateTasks()
    {
        //get the Task from the bottom fragment and display it in a new activity's textview
        theTask  = (Task) getIntent().getSerializableExtra("selectedTask");
        taskName = (TextView) findViewById(R.id.task_name);
        taskDesc = (TextView) findViewById(R.id.task_desc);
        taskTime = (TextView) findViewById(R.id.task_time);
        taskDate = (TextView) findViewById(R.id.task_date);

        taskName.setText("Task: " + theTask.getTaskName());
        taskDesc.setText("Description: " + theTask.getTaskDescription());
        taskTime.setText("Time: " + theTask.getTaskTime());
        taskDate.setText("Date: " + theTask.getTaskDate());
    }


    public void shareTaskDialog()
    {
        shareBtn = (Button) findViewById(R.id.share_btn);
        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment dialogFragment = new ShareTaskDialog();
                Bundle bundle = new Bundle();
                bundle.putSerializable("taskObject", theTask);
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "share_key");

            }
        });
    }



    /**
     * Date & Time picker Dialogs. Save task button.
     */

    DatePickerDialog.OnDateSetListener date = (new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setCurrentDate();
        }
    });


    //Date and Notifications changed on clicking Change Date button
    public void setCurrentDate()
    {
        DateFormat myDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        taskDate.setText("Date: " + myDateFormat.format(cal.getTime()));

        selectedDate = myDateFormat.format(cal.getTime());
    }


    //Save task by clicking button - write it to file and set it in the notification
    public void saveOnClick()
    {
        setCurrentDate();
        saveBtn = (Button) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UtilityClass.writeToFile(TaskActivity.this,
                        theTask.getTaskName() + " - " +
                                theTask.getTaskDescription() + " - " +
                                theTask.getTaskTime() + " - " +
                                selectedDate);

                createNotifications();
            }
        });
    }


    public void dateOnClick(View view)
    {
        new DatePickerDialog(this, date,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))
                .show();
    }


    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet( TimePicker view, int hourOfDay, int minute ) {
            cal.set( Calendar.HOUR_OF_DAY, hourOfDay );
            cal.set( Calendar.MINUTE, minute );
            setCurrentTime();
        }
    };

    public void setCurrentTime()
    {
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        DateFormat myTimeFormat = new SimpleDateFormat("HH:mm a");
        myTimeFormat.setTimeZone(timeZone);
        taskTime.setText("Task Time: " + myTimeFormat.format(cal.getTime()));
    }


    //Change Task Time - selected in activity_task.xml
    public void timeOnClick(View view)
    {
        new TimePickerDialog(this, time,
                cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE),
                false)
                .show();
    }

    /**
     * When user rotated the screen - to Landscape/Portrait,
     * the Date data preserved and still displays in its TextView
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        String savedTask =
                taskName.getText().toString() +" - "+
                        taskDate.getText().toString();

        UtilityClass.writeToFile(this, savedTask);
        savedInstanceState.putString("savedKey", savedTask);
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onRestoreInstanceState(Bundle restoredInstanceState) {
        String restoredSate = restoredInstanceState.getString("savedKey");
        taskDate.setText(restoredSate);
        super.onRestoreInstanceState(restoredInstanceState);
    }




    public void createNotifications()
    {
        android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(this);
        Intent resultIntent = new Intent(this, NotificationActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,   // Context
                        0,      //
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager nManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        builder.setSmallIcon(R.drawable.ic_add_dia);
        builder.setContentTitle("You have a message");
        builder.setContentText(UtilityClass.readFromFile(this)); // adds Task from the File to the notification
        builder.setContentIntent(resultPendingIntent);
        Notification myNotification = builder.build();

        nManager.notify(0, myNotification);
    }

}
