package ie.cit.brian.taskreminder.activities;


import android.app.FragmentManager;
import android.content.BroadcastReceiver;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.mikhaellopez.circularimageview.CircularImageView;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import ie.cit.brian.taskreminder.ContactProvider;
import ie.cit.brian.taskreminder.MyIntentService;
import ie.cit.brian.taskreminder.R;
import ie.cit.brian.taskreminder.fragments.SecondFragment;
import ie.cit.brian.taskreminder.fragments.FirstFragment;



/**
 * Created by briancoveney on 11/25/15.
 */
public class MainActivity extends BaseActivity implements FirstFragment.TaskSearcher {


    private static String TAG = "ie.cit.brian.taskreminder";
    private final Calendar cal = Calendar.getInstance();
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        /* We will not use setContentView in this activity
           Rather than we will use layout inflater to add view in FrameLayout of our base activity layout*/
        getLayoutInflater().inflate(R.layout.activity_main, frameLayout);

        settingsChangedNotification();

        // for the snackbar layout
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .coordinator_layout);


        CircularImageView circularImageView = (CircularImageView)findViewById(R.id.myCircularImageView);
        // Set Border
        circularImageView.setBorderColor(getResources().getColor(R.color.light_grey));
        circularImageView.setBorderWidth(10);
        // Add Shadow with default param
        circularImageView.addShadow();
        // or with custom param
        circularImageView.setShadowRadius(15);
        circularImageView.setShadowColor(Color.RED);


        //Services
        Intent intent = new Intent(this, MyIntentService.class);
        //Create an IntentFilter for it
        IntentFilter myIntentFilter = new IntentFilter("myBroadcast");
        //Register the BroadcastReceiver and its filter with the Android OS
        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadcastReceiver, myIntentFilter);
        startService(intent);

    }


    private BroadcastReceiver myBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Test passed
            Log.i(TAG, "The service has started from Activity");

            String messageFromBroadcast = intent.getStringExtra("myBroadcastMessage");

            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Today is " + messageFromBroadcast, Snackbar.LENGTH_LONG);
            snackbar.show();

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



    // listen for changes in the Settings Activity
    public void settingsChangedNotification() {
        //retrieved from PreferenceActivity
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        String changedSettings = sPref.getString("myPrefKey", "");

        if (changedSettings.contains("day")) {
            //Date retrieved from TaskActivity
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
            String myDate = sharedPreferences.getString("date_pref", "");


            //get todays date
            SimpleDateFormat dateFormatDay = new SimpleDateFormat("F EEEE, dd/MM/yyyy");
            String mDate = dateFormatDay.format(cal.getTime());


            //compare and display toast - if task is due today
            if (myDate.equals(myDateFormat())) {
                Toast.makeText(this, "Task due today: " + myDateFormat(), Toast.LENGTH_LONG).show();
            }


        } else if (changedSettings.contains("week")) {


            try {

                SharedPreferences shPref = PreferenceManager.getDefaultSharedPreferences(this);
                String mDate = shPref.getString("date_pref", "");
                String mSubString = mDate.substring(0, 1); //get first charAT of Date - the week of month


                //'F' -> Day of week in month(1-5)
                SimpleDateFormat weekFormat = new SimpleDateFormat("F");
                String dayOfMonth = weekFormat.format(cal.getTime());


                // if Task is due in the current week of the month - display a toast
                if (dayOfMonth.equals(mSubString)) {
                    Toast.makeText(this, "Task due this week: " + myDateFormat().substring(0, 3) + " of the month",
                            Toast.LENGTH_LONG).show();
                }
            }catch (StringIndexOutOfBoundsException e){
                Log.i(TAG, "Exception = " + e.getMessage());
            }

            }
        }



    public String myDateFormat()
    {
        SimpleDateFormat dateFormatDay = new SimpleDateFormat("F EEEE, dd/MM/yyyy");
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


