package ie.cit.brian.taskreminder.activities;


import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import java.io.FileNotFoundException;
import java.io.InputStream;
import ie.cit.brian.taskreminder.MyIntentService;
import ie.cit.brian.taskreminder.R;
import ie.cit.brian.taskreminder.UtilityClass;
import ie.cit.brian.taskreminder.fragments.BottomFragment;
import ie.cit.brian.taskreminder.fragments.TopFragment;



/**
 * Created by briancoveney on 11/25/15.
 */
public class MainActivity extends FragmentActivity implements TopFragment.TaskSearcher {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotifications();

        //Start Intentservice
        Intent intent = new Intent(this, MyIntentService.class);
        startService(intent);

    }


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



    // Broadcast receiver for receiving status updates from the IntentService
    private class ResponseReceiver extends BroadcastReceiver
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {



        }

    }




}