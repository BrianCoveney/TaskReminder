package ie.cit.brian.taskreminder;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by briancoveney on 12/22/15.
 */
public class MyIntentService extends IntentService {


    private static String TAG = "ie.cit.brian.taskreminder";



    public MyIntentService() {

        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent workIntent)
    {

        String dayOfWeek;
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.UK);
        Calendar cal = Calendar.getInstance();
        dayOfWeek = dayFormat.format(cal.getTime());

        Intent i = new Intent("myBroadcast");
        i.putExtra("myBroadcastMessage", dayOfWeek);
        LocalBroadcastManager.getInstance(this).sendBroadcast(i);

        //Test passed
        Log.i(TAG, "The service has started from MyIntentService");


        int day = cal.get(Calendar.DAY_OF_WEEK);

        switch (day){
            case Calendar.SUNDAY:
                Log.i(TAG, "Today is Sunday");
                break;

            case Calendar.MONDAY:
                Log.i(TAG, "Today is Monday");
                break;

            case Calendar.TUESDAY:
                Log.i(TAG, "Today is Tuesday");
                break;

            case Calendar.WEDNESDAY:
                Log.i(TAG, "Today is Wednesday");
                break;

            case Calendar.THURSDAY:
                Log.i(TAG, "Today is Thursday");
                break;

            case Calendar.FRIDAY:
                Log.i(TAG, "Today is Friday");
                break;

            case Calendar.SATURDAY:
                Log.i(TAG, "Today is Saturday");
                break;
        }
    }
}
