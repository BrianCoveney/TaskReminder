package ie.cit.brian.taskreminder.activities;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import ie.cit.brian.taskreminder.ContactProvider;
import ie.cit.brian.taskreminder.R;
import ie.cit.brian.taskreminder.ShareTaskDialog;
import ie.cit.brian.taskreminder.Task;
import ie.cit.brian.taskreminder.UtilityClass;


/**
 * Created by briancoveney on 11/28/15.
 */
public class TaskActivity extends BaseActivity {

    private Task theTask;
    private TextView taskName, taskDesc, taskTime, taskDate;
    private FloatingActionButton saveFabBtn, shareBtn;
    String selectedDate;
    String selectedTime;


    final Calendar cal = Calendar.getInstance();

    public interface taskReader {
        public void readTasks();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* We will not use setContentView in this activty
           Rather than we will use layout inflater to add view in FrameLayout of our base activity layout*/
        getLayoutInflater().inflate(R.layout.activity_task, frameLayout);

        populateTasks();
        shareTaskDialog();
        saveOnClick();

        loadSavedPreferences();

    }


    public void populateTasks() {
        //get the Task from the bottom fragment and display it in a new activity's textview
        theTask = (Task) getIntent().getSerializableExtra("selectedTask");
        taskName = (TextView) findViewById(R.id.task_name);
        taskDesc = (TextView) findViewById(R.id.task_desc);
        taskTime = (TextView) findViewById(R.id.task_time);
        taskDate = (TextView) findViewById(R.id.task_date);

        taskName.setText("Task: " + theTask.getTaskName());
        taskDesc.setText("Description: " + theTask.getTaskDescription());
        taskTime.setText(theTask.getTaskTime());
        taskDate.setText(theTask.getTaskDate().toString());
    }


    public void shareTaskDialog() {
        shareBtn = (FloatingActionButton) findViewById(R.id.fab_send);
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


    //get Calendar Date
    public void dateOnClick(View view) {
        new DatePickerDialog(this, date,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))
                .show();
    }


    //get Calendar Time
    public void timeOnClick(View view) {
        new TimePickerDialog(this, time,
                cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE),
                false)
                .show();
    }

    //set Calendar
    DatePickerDialog.OnDateSetListener date = (new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int myYear, int myMonthOfYear, int myDayOfMonth) {
            cal.set(Calendar.YEAR, myYear);
            cal.set(Calendar.MONTH, myMonthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, myDayOfMonth);

            setCurrentDate();
        }
    });

    TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);

            setCurrentTime();
        }
    };


    //Date and Notifications changed onClick the Change Edit Date button
    public void setCurrentDate() {


        DateFormat dateFormatDay = new SimpleDateFormat("F EEEE, dd/MM/yyyy");
        String mDate = dateFormatDay.format(cal.getTime());
        String subMyDate = mDate.substring(0, 1);
        String a = "st Week, ";
        String b = "nd Week, ";
        String c = "rd Week, ";
        String d = "th Week, ";

        if(subMyDate.equals("1")){
            StringBuilder str = new StringBuilder(mDate);
            str.insert(1, a).toString();
            mDate = str.toString();
        }else if(subMyDate.equals("2")) {
            StringBuilder str = new StringBuilder(mDate);
            str.insert(1, b).toString();
            mDate = str.toString();
        }else if(subMyDate.equals("3")) {
            StringBuilder str = new StringBuilder(mDate);
            str.insert(1, c).toString();
            mDate = str.toString();
        }else if(subMyDate.equals("4")
                 || subMyDate.equals("5")) {
            StringBuilder str = new StringBuilder(mDate);
            str.insert(1, d).toString();
            mDate = str.toString();
        }

        taskDate.setText(mDate);
        selectedDate = mDate;

    }


    //Time and Notifications changed onClick the Change Edit Time button
    public void setCurrentTime() {
        //Save button - updates file and the TextView in this activity
        DateFormat myTimeFormat = new SimpleDateFormat("HH:mm a");
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        myTimeFormat.setTimeZone(timeZone);
        taskTime.setText(myTimeFormat.format(cal.getTime()));

        //String selectedTime passed into the saveOnClick() to save to file
        selectedTime = myTimeFormat.format(cal.getTime());
    }


    //Save task by clicking button - write it to file and set it in the notification
    public void saveOnClick() {

        setCurrentDate();
        setCurrentTime();

        saveFabBtn = (FloatingActionButton)findViewById(R.id.fab_save);
        saveFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilityClass.writeToFile(TaskActivity.this,
                        theTask.getTaskName() + " - " +
                                theTask.getTaskDescription() + " - " +
                                selectedTime + " - " +
                                selectedDate);

                createNotifications();

            }
        });
    }


    /**
     * When the phone is flipped between orientations,
     * the Date and Timestamp data is preserved.
     */
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        String savedDate = taskDate.getText().toString();
        String savedTime = taskTime.getText().toString();

        savedInstanceState.putString("saved_date", savedDate);
        savedInstanceState.putString("saved_time", savedTime);

        super.onSaveInstanceState(savedInstanceState);

    }


    @Override
    public void onRestoreInstanceState(Bundle restoredInstanceState) {
        String restoredDate = restoredInstanceState.getString("saved_date");
        String restoredTime = restoredInstanceState.getString("saved_time");

        taskDate.setText(restoredDate);
        taskTime.setText(restoredTime);

        super.onRestoreInstanceState(restoredInstanceState);
    }


    private void loadSavedPreferences()
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        taskDate.setText(sharedPref.getString("date_pref", ""));
        taskTime.setText(sharedPref.getString("time_pref", ""));
    }

    private void savedPreferences(String key, String value)
    {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }


    public void saveData()
    {
        savedPreferences("date_pref", taskDate.getText().toString());
        savedPreferences("time_pref", taskTime.getText().toString());
    }


    //adding savedData() to onBackPressed() to saved Date change between activities,
    //and display notification change in MainActivity as a toast
    @Override
    public void onBackPressed() {
        saveData();
        Intent intent = new Intent(TaskActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }



    public void createNotifications() {

        //unique ID used for multiple notifications
        int uniqueNumber = (int) System.currentTimeMillis();
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


        builder.setSmallIcon(R.drawable.speak_bubble2);
        builder.setContentTitle("You have a new message");
        builder.setContentText(UtilityClass.readFromFile(getApplicationContext()));
        builder.setContentIntent(resultPendingIntent);
        Notification myNotification = builder.build();

        nManager.notify(uniqueNumber, myNotification);
    }




}


