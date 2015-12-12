package ie.cit.brian.taskreminder;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.Toolbar;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Created by briancoveney on 11/25/15.
 */
public class MainActivity extends FragmentActivity implements TopFragment.TaskSearcher {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createNotifications();



    }


    @Override
    public void refreshTaskList() {
        FragmentManager mgr = getFragmentManager();
        BottomFragment secondFragmentRef =
                (BottomFragment) mgr.findFragmentById(R.id.second_fragment);
        secondFragmentRef.refreshList();
    }


    //Inflate the ActionBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);

    }


    private String readFromFile() {

        String result = "";

        try {
            InputStream inputStream = openFileInput("myFile");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                result = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return result;
    }



    public void createNotifications()
    {
        android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.ic_add_dia);
        builder.setContentTitle("You have a message");
        builder.setContentText(readFromFile()); // adds Task from the File to the notification
        Notification myNotification = builder.build();

        NotificationManager nManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        nManager.notify(0, myNotification);
    }

}







