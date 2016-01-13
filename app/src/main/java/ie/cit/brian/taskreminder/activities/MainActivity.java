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
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ie.cit.brian.taskreminder.MyIntentService;
import ie.cit.brian.taskreminder.R;
import ie.cit.brian.taskreminder.UtilityClass;
import ie.cit.brian.taskreminder.fragments.BottomFragment;
import ie.cit.brian.taskreminder.fragments.TopFragment;



/**
 * Created by briancoveney on 11/25/15.
 */
public class MainActivity extends FragmentActivity implements TopFragment.TaskSearcher,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static String TAG = "ie.cit.brian.taskreminder";
    private final Calendar cal = Calendar.getInstance();
    private GoogleApiClient mGoogleApiClient;


    protected Location mCurrentLocation;
    protected LocationRequest mLocationRequest;
    protected TextView mLatitudeText,mLongitudeText,mLastUpdateTimeTextView;
    protected Boolean mRequestingLocationUpdates;
    protected String mLastUpdateTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotifications();
        settingsChangedNotification();
        createLocationRequest();

        //create an instance of GoogleApiClient
        GoogleApiClient.Builder builder =
                new GoogleApiClient.Builder(this);

        //Add the location api
        builder.addApi(LocationServices.API);
        //tell it what to call back to when it has connected to the Google Service
        builder.addConnectionCallbacks(this);

        mGoogleApiClient = builder.build();


        mLatitudeText = (TextView)findViewById(R.id.mLatitudeText);
        mLongitudeText = (TextView)findViewById(R.id.mLongitudeText);
        mLastUpdateTimeTextView = (TextView) findViewById(R.id.mLastUpdateTimeTextView);


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
        //reterived from PreferenceActivity
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        String changedSettings = sPref.getString("myPrefKey", "");

        if (changedSettings.contains("day")) {

            //Display toast if preference setting has been changed to Day
            Toast toast = Toast.makeText(this, changedSettings, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();


            //Date retreived from TaskActivity
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

            //Display toast if preference setting has been changed to Week
            Toast toast = Toast.makeText(this, changedSettings, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();


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

        if (subMyDate.equals("1")) {
            StringBuilder str = new StringBuilder(mDate);
            str.insert(1, a).toString();
            mDate = str.toString();
        } else if (subMyDate.equals("2")) {
            StringBuilder str = new StringBuilder(mDate);
            str.insert(1, b).toString();
            mDate = str.toString();
        } else if (subMyDate.equals("3")) {
            StringBuilder str = new StringBuilder(mDate);
            str.insert(1, c).toString();
            mDate = str.toString();
        } else if (subMyDate.equals("4")
                || subMyDate.equals("5")) {
            StringBuilder str = new StringBuilder(mDate);
            str.insert(1, d).toString();
            mDate = str.toString();
        }
        return mDate;
    }


    //** Google Play Services & Location API **//

    @Override
    public void onConnected(Bundle bundle) {

        // get Last Known Location - display current Lat Log in a textview
//        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
//                mGoogleApiClient);
//        if (mCurrentLocation != null) {
//            mLatitudeText.setText(String.valueOf(mCurrentLocation.getLatitude()));
//            mLongitudeText.setText(String.valueOf(mCurrentLocation.getLongitude()));
//        }else{
//            Toast.makeText(this, "Location test failed", Toast.LENGTH_LONG).show();
//        }


        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
            startLocationUpdates();

    }

    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(50000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates() {

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }


    // define the LocationListener's interface method to display the current Lat, Long and Timestamp
    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        updateUI();
    }

    private void updateUI()
    {
        mLatitudeText.setText(String.valueOf(mCurrentLocation.getLatitude()));
        mLongitudeText.setText(String.valueOf(mCurrentLocation.getLongitude()));
        mLastUpdateTimeTextView.setText(mLastUpdateTime);
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
}