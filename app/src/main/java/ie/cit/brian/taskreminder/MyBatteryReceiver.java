package ie.cit.brian.taskreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.widget.Toast;

/**
 * Created by briancoveney on 12/18/15.
 */
public class MyBatteryReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        int status = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        String message = "Device is Plugged in.";


        if(status == BatteryManager.BATTERY_PLUGGED_USB ||
                status == BatteryManager.BATTERY_PLUGGED_AC) {
        }

        //If running this app with Android Studio, the toast will only appear
        //when cable is plugged out and back in again.
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();

    }


}
