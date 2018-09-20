package ie.cit.brian.taskreminder.activities;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import ie.cit.brian.taskreminder.R;
import ie.cit.brian.taskreminder.ShareTaskDialog;
import ie.cit.brian.taskreminder.Task;
import ie.cit.brian.taskreminder.UtilityClass;


/**
 * Created by briancoveney on 11/28/15.
 */
public class TaskActivity extends BaseActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    private Task theTask;
    private TextView taskTime, taskDate, taskName, taskDesc;
    private FloatingActionButton saveFabBtn, shareBtn;
    protected ImageView deleteDateIcon, deleteTimeIcon;
    String selectedDate;
    String selectedTime;


    final Calendar cal = Calendar.getInstance();

    public interface taskReader {
        public void readTasks();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /* We will not use setContentView in this activity
           Rather than we will use layout inflater to add view in FrameLayout of our base activity layout**/
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

        taskName.setText(theTask.getTaskName());
        taskDesc.setText(theTask.getTaskDescription());

        taskTime.setText(theTask.getTaskTime());
        taskDate.setText(theTask.getTaskDate().toString());
    }


    public void shareTaskDialog() {
        try {

            ImageView emailIcon = (ImageView) findViewById(R.id.imageView_taskEmail);
            emailIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    DialogFragment dialogFragment = new ShareTaskDialog();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("taskObject", theTask);
                    dialogFragment.setArguments(bundle);
                    dialogFragment.show(getFragmentManager(), "share_key");
                }
            });
        } catch (NullPointerException np) {
            np.printStackTrace();
        }
    }


    //get Date
    public void dateOnClick(View view) {
        new DatePickerDialog(this, date,
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))
                .show();
    }


    //get Time
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


    public void clearDateOnClick(View view) {
        deleteDateIcon = (ImageView) findViewById(R.id.imageView_taskDate_cancel);
        deleteDateIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                taskDate.setText(null);
            }
        });
    }

    public void clearTimeOnClick(View view) {
        deleteTimeIcon = (ImageView) findViewById(R.id.imageView_taskTime_cancel);
        deleteTimeIcon.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                taskTime.setText(null);
            }
        });
    }


    //Date and Notifications changed onClick the Change Edit Date button
    public void setCurrentDate() {
        DateFormat dateFormatDay = new SimpleDateFormat("F EEEE, dd/MM/yyyy");
        String mDate = dateFormatDay.format(cal.getTime());
        String subMyDate = mDate.substring(0, 1);
        String a = "st Week, ";
        String b = "nd Week, ";
        String c = "rd Week, ";
        String d = "th Week, ";

        if (subMyDate.equals("1")) {
            StringBuilder str = new StringBuilder(mDate);
            str.insert(1, a).toString();
            mDate = str.toString();
        } else if (subMyDate.equals("2")) {
            StringBuilder str = new StringBuilder(mDate);
            str.insert(1, b).toString();
            mDate = str.toString();
        } else if (subMyDate.equals("3")) {
            StringBuilder str = new StringBuilder(mDate);
            str.insert(1, c).toString();
            mDate = str.toString();
        } else if (subMyDate.equals("4")
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

        saveFabBtn = (FloatingActionButton) findViewById(R.id.fab_save);
        saveFabBtn.setOnClickListener(new OnClickListener() {
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


    private void loadSavedPreferences() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        taskDate.setText(sharedPref.getString("date_pref", ""));
        taskTime.setText(sharedPref.getString("time_pref", ""));
    }

    private void savedPreferences(String key, String value) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.apply();
    }


    public void saveData() {
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


    private void createNotifications() {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_DEFAULT);

            // Configure the notification channel.
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Intent resultIntent = new Intent(this, NotificationActivity.class);
        PendingIntent resultPendingIntent = PendingIntent
                .getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT);

        String dummyText = "is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.";
        String message = UtilityClass.readFromFile(getApplicationContext())+ dummyText;
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.profile_pic);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);

        notificationBuilder.setAutoCancel(true)
                .setWhen(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.speak_bubble2)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentTitle("You have a new message")
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message))
                .setContentText(message)
                .setContentIntent(resultPendingIntent);

        notificationManager.notify(/*notification id*/1, notificationBuilder.build());

    }
}


