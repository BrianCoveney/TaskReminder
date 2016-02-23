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
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import ie.cit.brian.taskreminder.MyIntentService;
import ie.cit.brian.taskreminder.R;
import ie.cit.brian.taskreminder.UtilityClass;
import ie.cit.brian.taskreminder.fragments.SecondFragment;
import ie.cit.brian.taskreminder.fragments.FirstFragment;



/**
 * Created by briancoveney on 11/25/15.
 */
public class MainActivity extends BaseActivity implements FirstFragment.TaskSearcher {


    private static String TAG = "ie.cit.brian.taskreminder";
    private final Calendar cal = Calendar.getInstance();


    private FirstFragment.TaskSearcher searcher;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /* We will not use setContentView in this activty
           Rather than we will use layout inflater to add view in FrameLayout of our base activity layout*/
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);
//        setContentView(R.layout.activity_main);





        createNotifications();
        settingsChangedNotification();

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
            //Test passed
            Log.i(TAG, "The service has started from Activity");
        }
    };


    // SecondFragments interface
    @Override
    public void refreshTaskList() {
        FragmentManager mgr = getFragmentManager();
        SecondFragment secondFragmentRef =
                (SecondFragment) mgr.findFragmentById(R.id.second_fragment);
        secondFragmentRef.refreshList();
    }



    public void createNotifications() {
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
                builder.setContentTitle("You have a new message");
                builder.setContentText(UtilityClass.readFromFile(getApplicationContext())); // adds Task from the File to the notification
                builder.setContentIntent(resultPendingIntent);
                Notification myNotification = builder.build();

                nManager.notify(uniqueNumber, myNotification);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void settingsChangedNotification() {
        //retrieved from PreferenceActivity
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        String changedSettings = sPref.getString("myPrefKey", "");

        if (changedSettings.contains("day")) {
            //Date retrieved from TaskActivity
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String myDate = sharedPreferences.getString("date_pref", "");


            //get todays date
            DateFormat dateFormatDay = new SimpleDateFormat("F EEEE, dd/MM/yyyy");
            String mDate = dateFormatDay.format(cal.getTime());


            //compare and display toast - if task is due today
            if (myDate.equals(myDateFormat())) {
                Toast.makeText(this, "Task due today: " + myDateFormat(), Toast.LENGTH_LONG).show();
            }


        } else if (changedSettings.contains("week")) {
            SharedPreferences shPref = PreferenceManager.getDefaultSharedPreferences(this);
            String mDate = shPref.getString("date_pref", "");
            String mSubString = mDate.substring(0, 1); //get first charAT of Date - the week of month

            //'F' -> Day of week in month(1-5)
            DateFormat weekFormat = new SimpleDateFormat("F");
            String dayOfMonth = weekFormat.format(cal.getTime());


            // if Task is due in the current week of the month - display a toast
            if(dayOfMonth.equals(mSubString)){
                Toast.makeText(this, "Task due this week: "+ myDateFormat().substring(0, 3) +" of the month",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    public String myDateFormat()
    {
        DateFormat dateFormatDay = new SimpleDateFormat("F EEEE, dd/MM/yyyy");
        String mDate = dateFormatDay.format(cal.getTime());
        String subMyDate = mDate.substring(0, 1);

        String a = "st Week, ";
        String b = "nd Week, ";
        String c = "rd Week, ";
        String d = "th Week, ";

        StringBuilder str = new StringBuilder(mDate);


        switch (subMyDate){
            case "1":
                str.insert(1, a);
                mDate = str.toString();
                break;
            case "2":
                str.insert(1, b);
                mDate = str.toString();
                break;
            case "3":
                str.insert(1, c);
                mDate = str.toString();
                break;
            case "4":
            case "5":
                str.insert(1, d);
                mDate = str.toString();
                break;
        }

        return mDate;
    }



}


