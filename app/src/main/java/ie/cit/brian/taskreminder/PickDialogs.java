package ie.cit.brian.taskreminder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by briancoveney on 12/6/15.
 */
public class PickDialogs extends DialogFragment{


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {


        //create a object of the DateSetting class
        DateSettings dateSettings = new DateSettings(getActivity());


        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog dialog;
        dialog = new DatePickerDialog(getActivity(), dateSettings, year, month, day);


        return dialog;
    }


}
