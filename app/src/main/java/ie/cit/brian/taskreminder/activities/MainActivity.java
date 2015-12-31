package ie.cit.brian.taskreminder.activities;


import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import ie.cit.brian.taskreminder.MyIntentService;
import ie.cit.brian.taskreminder.R;
import ie.cit.brian.taskreminder.UtilityClass;
import ie.cit.brian.taskreminder.fragments.BottomFragment;
import ie.cit.brian.taskreminder.fragments.TopFragment;



/**
 * Created by briancoveney on 11/25/15.
 */
public class MainActivity extends FragmentActivity implements TopFragment.TaskSearcher {

    private static String TAG = "ie.cit.brian.taskreminder";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotifications();


//      reterived from PreferenceActivity
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        String changedSettings = sPref.getString("myPrefKey", "");

        if(changedSettings.contains("day")) {
            Toast toast = Toast.makeText(this, changedSettings, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }else if(changedSettings.contains("week")) {
            Toast toast = Toast.makeText(this, changedSettings, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }


        //retreived from TaskActivity
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String myDate = sharedPreferences.getString("date_pref", "");



        Calendar cal = Calendar.getInstance();
        DateFormat myDateFormat = new SimpleDateFormat("EEEE");
        String today = myDateFormat.format(cal.getTime());

        if (myDate.contains(today) ){
            Toast.makeText(this, "Task due today - " + today, Toast.LENGTH_SHORT).show();

        }


        //TaskActivity's SharedPreferences - Date, passed to MainActvity
//        if(myDate.contains("Mon")){
//            Toast.makeText(this, "Task due today - Monday", Toast.LENGTH_SHORT).show();
//        }else if(myDate.contains("Tue")){
//            Toast.makeText(this, "Task due today - Tuesday", Toast.LENGTH_SHORT).show();
//        }else if(myDate.contains("Wed")){
//            Toast.makeText(this, "Task due today - Wednesday", Toast.LENGTH_SHORT).show();
//        }else if(myDate.contains("Thu")){
//            Toast.makeText(this, "Task due today - Thursday", Toast.LENGTH_SHORT).show();
//        }else if(myDate.contains("Fri")){
//            Toast.makeText(this, "Task due today - Friday", Toast.LENGTH_SHORT).show();
//        }else if(myDate.contains("Sat")){
//            Toast.makeText(this, "Task due today - Saturday", Toast.LENGTH_SHORT).show();
//        }else if(myDate.contains("Sun")){
//            Toast.makeText(this, "Task due today - Sunday", Toast.LENGTH_SHORT).show();
//        }


        //Test passed
//        TextView tv = (TextView) findViewById(R.id.testTV);
//        tv.setText(myDate);



        //Services
        Intent i = new Intent(this, MyIntentService.class);

        //Create an IntentFilter for it
        IntentFilter myIntentFilter = new IntentFilter("myBroadcast");

        //Register the BroadcastReceiver and its filter with the Android OS
        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadcastReceiver, myIntentFilter);
        startService(i);

    }

    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String messageFromBroadcast = intent.getStringExtra("myBroadcastMessage");

            //Test passed
//            Toast.makeText(getApplicationContext(), messageFromBroadcast, Toast.LENGTH_LONG).show();

            //Test passed
            Log.i(TAG, "The service has started from Activity");

        }
    };



    @Override
    public void refreshTaskList() {
        FragmentManager mgr = getFragmentManager();
        BottomFragment secondFragmentRef =
                (BottomFragment) mgr.findFragmentById(R.id.second_fragment);
        secondFragmentRef.refreshList();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        //Inflate the ActionBar 
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void createNotifications()
    {

        //unique ID used for multiple notifications
        int uniqueNumber = (int) System.currentTimeMillis();

        try {
            InputStream inputStream = openFileInput("myFile");

            //When app is first launched, or there's no saved tasks - a notification is not displayed
            if (inputStream != null) {

                android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(this);
                Intent resultIntent = new Intent(this, NotificationActivity.class);

                PendingIntent resultPendingIntent =
                        PendingIntent.getActivity(
                                this,   // Context
                                0,
                                resultIntent,
                                PendingIntent.FLAG_UPDATE_CURRENT);

                NotificationManager nManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                builder.setSmallIcon(R.drawable.speak_bubble2);
                builder.setContentTitle("You have a message");
                builder.setContentText(UtilityClass.readFromFile(getApplicationContext())); // adds Task from the File to the notification
                builder.setContentIntent(resultPendingIntent);
                Notification myNotification = builder.build();

                nManager.notify(uniqueNumber, myNotification);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}