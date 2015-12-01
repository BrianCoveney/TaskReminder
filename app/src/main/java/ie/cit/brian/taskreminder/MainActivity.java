package ie.cit.brian.taskreminder;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
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
public class MainActivity extends AppCompatActivity implements TopFragment.TaskSearcher {

    String[] spinnerValues = { "Running", "Working", "Cycling","Shopping", "Swimming", };
    String[] spinnerSubs = { "On the track", "In the office", "On the road", "In the shop", "At the pool" };
    int total_images[] = { R.drawable.running, R.drawable.office, R.drawable.biking, R.drawable.shopping_cart, R.drawable.swimming};



    private ArrayAdapter adapter;
    private ArrayList<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar myToolBar = (Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(myToolBar);
        getActionBar().setDisplayShowTitleEnabled(false);


        Task t1 = new Task("Shopping");
        Task t2 = new Task("Running");

        taskList = new ArrayList<Task>();
        taskList.add(t1);
        taskList.add(t2);


        //Old Spinner
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, taskList);


        //New Custom Spinner
        Spinner mySpinner = (Spinner) findViewById(R.id.spinner);
        mySpinner.setAdapter(new MyAdapter(this, R.layout.custom_spinner, spinnerValues));


    }//end onCreate



    @Override
    public void refreshTaskList()
    {
        FragmentManager mgr = getFragmentManager();
        BottomFragment secondFragmentRef =
                (BottomFragment)mgr.findFragmentById(R.id.second_fragment);

        secondFragmentRef.refreshList();
    }


    //ActionBar Write to File
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);

        //Info Icon
        ImageButton imgButton = (ImageButton) menu.findItem(R.id.action_info).getActionView();
        imgButton.setImageResource(R.drawable.ic_text);
        imgButton.setBackgroundResource(0); //hides background
        imgButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                TextView tv = (TextView) findViewById(R.id.textView);


                Spinner taskSpinner = (Spinner) findViewById(R.id.spinner);
                String selected = taskSpinner.getSelectedItem().toString();

                //Calls out to the controller to put the data into the model,
                //then tells the bottom fragment to refresh itself from the updated model
                TaskController.getInstance().addTask(selected);


                //Write and Read from file
                writeToFile(selected.toString());
                tv.setText(readFromFile());


                tv.setVisibility(View.VISIBLE);
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    private String readFromFile() {

        String ret = "";

        try {
            InputStream inputStream = openFileInput("config.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Spinner taskSpinner = (Spinner)findViewById(R.id.spinner);
        TextView spinnerTitle = (TextView) findViewById(R.id.spinnerTitle);

        switch (item.getItemId()) {
            case R.id.action_add_dialog:



                AddTaskDialogFragment dialogFragment = new AddTaskDialogFragment();
                dialogFragment.show(getFragmentManager(), "yesNoDialog");


                break;

            case R.id.action_add_spinner:
                spinnerTitle.setVisibility(View.VISIBLE);
                taskSpinner.setVisibility(View.VISIBLE);
                break;

            case R.id.action_remove_spinner:
                spinnerTitle.setVisibility(View.GONE);
                taskSpinner.setVisibility(View.GONE);

            default:
                break;
        }
        return true;
    }



    //Spinner Custom ************

    public class MyAdapter extends ArrayAdapter<String>
    {
        public MyAdapter(Context ctx, int txtViewResourceId, String[] objects) {
            super(ctx, txtViewResourceId, objects);
        }

        @Override public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
            return getCustomView(position, cnvtView, prnt);
        }

        @Override public View getView(int pos, View cnvtView, ViewGroup prnt) {
            return getCustomView(pos, cnvtView, prnt);
        }

        public View getCustomView(int position, View convertView, ViewGroup parent)
        {
            LayoutInflater inflater = getLayoutInflater();

            View mySpinner = inflater.inflate(R.layout.custom_spinner, parent, false);

            TextView main_text = (TextView) mySpinner .findViewById(R.id.text_main_seen);

            main_text.setText(spinnerValues[position]);

            TextView subSpinner = (TextView) mySpinner .findViewById(R.id.sub_text_seen);

            subSpinner.setText(spinnerSubs[position]);

            ImageView left_icon = (ImageView) mySpinner .findViewById(R.id.left_pic);

            left_icon.setImageResource(total_images[position]);
            left_icon.setBackgroundResource(0);


            return mySpinner;
        }
    }

}







