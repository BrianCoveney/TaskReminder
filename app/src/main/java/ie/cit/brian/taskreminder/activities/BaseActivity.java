package ie.cit.brian.taskreminder.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import ie.cit.brian.taskreminder.CustomAdapter;
import ie.cit.brian.taskreminder.R;
import ie.cit.brian.taskreminder.UtilityClass;

/**
 * Created by brian on 20/02/16.
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected String[] mTaskItems;
    protected DrawerLayout mDrawerLayout;
    protected FrameLayout frameLayout;
    protected ListView mDrawerList;
    protected ImageView navImages;
    protected ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_base);
        mDrawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.activity_base, null);
        frameLayout = (FrameLayout)findViewById(R.id.content_frame);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        addDrawerItems();
        setupDrawer();

        assert getSupportActionBar() != null;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
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

        // when items in Actionbar are pressed (not the Hamburger menu)
        switch (item.getItemId()) {
            case R.id.action_location:
                UtilityClass.launchLocationActivity(this);
                break;
            case R.id.action_googlemaps:
                UtilityClass.launchGoogleMapsActivity(this);
                break;
            case R.id.action_login:
                UtilityClass.launchLoginActivity(this);
            default:
                break;
        }

        // Activate the navigation drawer toggle (Hamburger menu)
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

//       add a Header to the Nav Drawer
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
                        UtilityClass.launchMainActivity(getApplicationContext());
                        break;
                    case 2:
                        UtilityClass.launchSettingsActivity(getApplicationContext());
                        break;
                    case 3:
                        UtilityClass.launchGoogleMapsActivity(getApplicationContext());
                        break;
                    case 4:
                        UtilityClass.launchLocationActivity(getApplicationContext());
                    default:
                }

            }
        });
    } /*** end Navigation Drawer and ActionBar ***/

}
