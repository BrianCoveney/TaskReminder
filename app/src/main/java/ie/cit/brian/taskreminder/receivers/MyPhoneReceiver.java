package ie.cit.brian.taskreminder.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.Toast;

/**
 * Created by briancoveney on 12/18/15.
 */
public class MyPhoneReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        if(extras != null) {
            String state = extras.getString(TelephonyManager.EXTRA_STATE);
            String message = "Phone state changed to " + state;
            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                String phoneNumner = extras.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
                message += "Incoming number is" + phoneNumner;
            }
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}
