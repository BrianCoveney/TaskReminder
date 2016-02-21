package ie.cit.brian.taskreminder.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

import ie.cit.brian.taskreminder.R;
import ie.cit.brian.taskreminder.Task;
import ie.cit.brian.taskreminder.UtilityClass;

/**
 * Created by brian on 12/10/2015.
 */
public class NotificationActivity extends BaseActivity {

    private Task theTask;
    private TextView notificationResult;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        populateNotificationTasks();
    }



    public void populateNotificationTasks()
    {
        notificationResult = (TextView) findViewById(R.id.text_notification1);
        notificationResult.setText(UtilityClass.readFromFile(this));
    }
}
