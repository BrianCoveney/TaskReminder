package ie.cit.brian.taskreminder;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by brian on 12/10/2015.
 */
public class NotificationActivity extends Activity {

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
        notificationResult = (TextView) findViewById(R.id.text_notifications);
        notificationResult.setText(UtilityClass.readFromFile(this));
    }
}
