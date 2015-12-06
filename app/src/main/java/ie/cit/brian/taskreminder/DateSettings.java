package ie.cit.brian.taskreminder;

import android.app.DatePickerDialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by briancoveney on 12/6/15.
 */
public class DateSettings extends FragmentActivity implements DatePickerDialog.OnDateSetListener {

    Context context;
    TextView tv;

    //Constructor
    public DateSettings(Context context) {
        this.context = context;
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        //find the TextView in TaskActivity and change it to the selected date, from the dialog
        TaskActivity.taskDate.setText("Task Date Changed: " + dayOfMonth + "/" + monthOfYear + "/" + year);

    }
}


