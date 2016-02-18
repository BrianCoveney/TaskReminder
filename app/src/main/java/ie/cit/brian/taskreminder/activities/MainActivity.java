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
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import ie.cit.brian.taskreminder.CustomAdapter;
import ie.cit.brian.taskreminder.MyIntentService;
import ie.cit.brian.taskreminder.R;
import ie.cit.brian.taskreminder.UtilityClass;
import ie.cit.brian.taskreminder.fragments.SecondFragment;
import ie.cit.brian.taskreminder.fragments.FirstFragment;



/**
 * Created by briancoveney on 11/25/15.
 */
public class MainActivity extends AppCompatActivity implements FirstFragment.TaskSearcher {


    private static String TAG = "ie.cit.brian.taskreminder";
    private final Calendar cal = Calendar.getInstance();

    private FirstFragment.TaskSearcher searcher;


    private String[] mTaskItems;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ImageView navImages;
    private ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotifications();
        settingsChangedNotification();


        addDrawerItems();
        setupDrawer();


        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setTitle(null);



        //Services
        Intent i = new Intent(this, MyIntentService.class);

        //Create an IntentFilter for it
        IntentFilter myIntentFilter = new IntentFilter("myBroadcast");

        //Register the BroadcastReceiver and its filter with the Android OS
        LocalBroadcastManager.getInstance(this).registerReceiver(myBroadcastReceiver, myIntentFilter);
        startService(i);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //Inflate the ActionBar
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }



   /*** Navigation Drawer and ActionBar ***/
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // when items in Actionbar are pressed
        switch (item.getItemId()) {
            case R.id.action_location:
                UtilityClass.launchLocationActivity(this);
                break;
            case R.id.action_googlemaps:
                UtilityClass.launchGoogleMapsActivity(this);
                break;
            default:
                break;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return true;
    }


    private void addDrawerItems(){
        mTaskItems = getResources().getStringArray(R.array.menu_items);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navImages = (ImageView) findViewById(R.id.mNavDrawerListIcons);

        // add a Header to the Nav Drawer
        View headerView = View.inflate(this, R.layout.nav_header, null);
        mDrawerList.addHeaderView(headerView);

        CustomAdapter customAdapter = new CustomAdapter(this, mTaskItems);
        mDrawerList.setAdapter(customAdapter);

        // when items in Navigation Drawer are pressed
        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 1:
                        UtilityClass.launchSettingsActivity(getApplicationContext());
                        break;
                    case 2:
                        UtilityClass.launchGoogleMapsActivity(getApplicationContext());
                        break;
                    case 3:
                        UtilityClass.launchLocationActivity(getApplicationContext());
                    default:
                }

            }
        });
    } /*** end Navigation Drawer and ActionBar ***/



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
        //reterived from PreferenceActivity
        SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(this);
        String changedSettings = sPref.getString("myPrefKey", "");

        if (changedSettings.contains("day")) {
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


