package ie.cit.brian.taskreminder.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.Objects;

import ie.cit.brian.taskreminder.R;

/**
 * Created by briancoveney on 12/21/15.
 */
public class PreferenceActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(
                android.R.id.content, new MyPreferenceFragment()).commit();
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(PreferenceActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }


    public static class MyPreferenceFragment extends PreferenceFragment
            implements SharedPreferences.OnSharedPreferenceChangeListener {
        @Override
        public void onCreate(final Bundle savedInstanceState) {

            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.preferences);

        }


        @Override
        public void onResume() {
            super.onResume();
            getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onPause() {
            super.onPause();
            getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        }


        //Configure CheckboxPreferences to work like RadioButtons
        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {

            String key = preference.getKey();
            CheckBoxPreference wkPref = (CheckBoxPreference) findPreference("week_preference");
            CheckBoxPreference dayPref = (CheckBoxPreference) findPreference("day_preference");



            if(key.equals(null)){
                String none = "not changed";

                Toast.makeText(getActivity(), none, Toast.LENGTH_SHORT).show();
                wkPref.setChecked(false);
                dayPref.setChecked(false);

            }

            else if (key.equals("day_preference")) {
                String mDay = "Changed to day";

                Toast.makeText(getActivity(), mDay, Toast.LENGTH_SHORT).show();
                wkPref.setChecked(false);

                SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sPref.edit();
                editor.putString("myPrefKey", mDay);
                editor.commit();


            } else if (key.equals("week_preference")) {
                String mWeek = "Changed to week";
                Toast.makeText(getActivity(), mWeek, Toast.LENGTH_SHORT).show();
                dayPref.setChecked(false);

                SharedPreferences sPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
                SharedPreferences.Editor editor = sPref.edit();
                editor.putString("myPrefKey", mWeek);
                editor.commit();
            }

            return super.onPreferenceTreeClick(preferenceScreen, preference);
        }


    }
}