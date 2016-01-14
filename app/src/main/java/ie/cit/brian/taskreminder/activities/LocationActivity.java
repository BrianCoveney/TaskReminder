package ie.cit.brian.taskreminder.activities;


import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import ie.cit.brian.taskreminder.R;





/**
 * Created by briancoveney on 11/25/15.
 */
public class LocationActivity extends FragmentActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Button locationButton;
    private static String TAG = "ie.cit.brian.taskreminder";
    private final Calendar cal = Calendar.getInstance();
    private GoogleApiClient mGoogleApiClient;

    protected Location mCurrentLocation;
    protected LocationRequest mLocationRequest;
    protected TextView mLatitudeText,mLongitudeText,mLastUpdateTimeTextView;
    protected Boolean mRequestingLocationUpdates = true;
    protected String mLastUpdateTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);


        createLocationRequest();
        buildGoogleApiClient();

        mLatitudeText = (TextView)findViewById(R.id.mLatitudeText);
        mLongitudeText = (TextView)findViewById(R.id.mLongitudeText);
        mLastUpdateTimeTextView = (TextView) findViewById(R.id.mLastUpdateTimeTextView);
        locationButton = (Button) findViewById(R.id.location_btn);

        //toggle periodice updates
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                togglePeriodicLocUpdates();
            }
        });
    }



    private void togglePeriodicLocUpdates() {
        if(!mRequestingLocationUpdates){
            locationButton.setText(R.string.stop_loc_updates);

            mRequestingLocationUpdates = true;

            //Starting the location updates
            startLocationUpdates();
        }else{
            locationButton.setText(R.string.start_loc_updates);

            mRequestingLocationUpdates = false;

            //Stop the location updates
            stopLocationUpdates();
        }

    }


    protected synchronized void buildGoogleApiClient()
    {
        //create an instance of GoogleApiClient
        GoogleApiClient.Builder builder =
                new GoogleApiClient.Builder(this);

        //Add the location api
        builder.addApi(LocationServices.API);
        //tell it what to call back to when it has connected to the Google Service
        builder.addConnectionCallbacks(this);
        // connection error checking
        builder.addOnConnectionFailedListener(this);

        mGoogleApiClient = builder.build();
    }



    @Override
    public void onConnected(Bundle bundle) {

        // when connected with google api, get the location
        updateUI();

        if(mRequestingLocationUpdates) {
            startLocationUpdates();
        }
    }

    protected void createLocationRequest()
    {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(50000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    protected void startLocationUpdates()
    {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates()
    {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
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
        // get Last Known Location
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);

        if(mCurrentLocation != null) {
            mLatitudeText.setText(String.valueOf(mCurrentLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mCurrentLocation.getLongitude()));
            mLongitudeText.setVisibility(View.VISIBLE);
        }else {
            mLatitudeText.setText("Connection failed. Is location enabled?");
            mLongitudeText.setVisibility(View.INVISIBLE);
        }

        mLastUpdateTimeTextView.setText(mLastUpdateTime);
    }





    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + connectionResult.getErrorCode());
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
