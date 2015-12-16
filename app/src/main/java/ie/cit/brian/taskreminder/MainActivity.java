package ie.cit.brian.taskreminder;


import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;


/**
 * Created by briancoveney on 11/25/15.
 */
public class MainActivity extends FragmentActivity implements TopFragment.TaskSearcher {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotifications();
    }


    @Override
    public void refreshTaskList() {
        FragmentManager mgr = getFragmentManager();
        BottomFragment secondFragmentRef =
                (BottomFragment) mgr.findFragmentById(R.id.second_fragment);
        secondFragmentRef.refreshList();
    }


    //Inflate the ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }



    public void createNotifications()
    {
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

        builder.setSmallIcon(R.drawable.ic_add_dia);
        builder.setContentTitle("You have a message");
        builder.setContentText(UtilityClass.readFromFile(this)); // adds Task from the File to the notification
        builder.setContentIntent(resultPendingIntent);
        Notification myNotification = builder.build();

        nManager.notify(0, myNotification);
    }

}







