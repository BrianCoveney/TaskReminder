package ie.cit.brian.taskreminder;


import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;


/**
 * Created by briancoveney on 11/28/15.
 */
public class TaskActivity extends FragmentActivity {

    private Task theTask;
    private TextView taskName, taskDesc, taskTime, taskDate, fileTextView;
    private Button shareBtn;


    final Calendar cal = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        populateTasks();
        shareTaskDialog();

    }



    public void populateTasks()
    {
        //get the Task from the bottom fragment and display it in a new activity's textview
        theTask = (Task) getIntent().getSerializableExtra("selectedTask");
        taskName = (TextView) findViewById(R.id.task_name);
        taskDesc = (TextView) findViewById(R.id.task_desc);
        taskTime = (TextView) findViewById(R.id.task_time);
        taskDate = (TextView) findViewById(R.id.task_date);

        taskName.setText("Name: " + theTask.getTaskName());
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



    /*=== Date and Time picker Dialogs*/

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
        taskDate.setText("Task Date: " +myDateFormat.format(cal.getTime()));

        //Write selected task name and date to file
        String selectedDate = myDateFormat.format(cal.getTime());
        writeToFile(theTask.toString() +" - "+ selectedDate);

        //Notification to display result
        createNotifications();
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

    public void timeOnClick( View view ) {
        new TimePickerDialog(this, time,
                cal.get(Calendar.HOUR),
                cal.get(Calendar.MINUTE),
                false)
                .show();


    }/*=== End - Date and Time picker Dialogs*/






    // When user rotated the screen - to Landscape/Portrait,
    // the Date data preserved and still displays in its TextView
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState)
    {
        String savedTask = taskDate.getText().toString();
        writeToFile(savedTask);
        savedInstanceState.putString("savedKey", savedTask);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle restoredInstanceState)
    {
        String restoredSate = restoredInstanceState.getString("savedKey");
        taskDate.setText(restoredSate);
        super.onRestoreInstanceState(restoredInstanceState);
    }






    private void writeToFile(String data) {

        try {
            FileOutputStream outputStreamWriter;

            outputStreamWriter = getApplication().openFileOutput("myFile",
                    Context.MODE_PRIVATE);
            outputStreamWriter.write(data.getBytes());
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private String readFromFile() {

        String result = "";

        try {
            InputStream inputStream = openFileInput("myFile");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                result = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return result;
    }


    public void createNotifications()
    {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_add_dia);
        builder.setContentTitle("You have a message");
        builder.setContentText(readFromFile()); // adds Task from the File to the notification
        Notification myNotification = builder.build();

        NotificationManager nManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        nManager.notify(0, myNotification);
    }



// Writing Task to file - used in the MainActivity
//    @Override
//    public void onPause()
//    {
//        super.onPause();
//        String savedTask = taskName.getText().toString() +" - "
//                + taskTime.getText().toString() +" - "+ taskDate.getText().toString();
//        writeToFile(savedTask);
//    }
//
//    @Override
//    protected void onRestart()
//    {
//        super.onRestart();
//        taskDate.setText(readFromFile());
//    }



}
