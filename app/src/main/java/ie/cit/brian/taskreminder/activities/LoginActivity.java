package ie.cit.brian.taskreminder.activities;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import ie.cit.brian.taskreminder.ContactProvider;
import ie.cit.brian.taskreminder.R;

/**
 * Created by brian on 18/05/16.
 */
public class LoginActivity extends BaseActivity {

    EditText edTName, edTEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLayoutInflater().inflate(R.layout.activity_login, frameLayout);


        edTName = (EditText)findViewById(R.id.edT_login_name);
        edTEmail = (EditText)findViewById(R.id.edT_login_email);
        edTName.setHintTextColor(getResources().getColor(R.color.blue_grey_900));
        edTEmail.setHintTextColor(getResources().getColor(R.color.blue_grey_900));

    }


    // SQLite DB
    public void addUser(View view) {

        // Get the name supplied
        String name = edTName.getText().toString();
        String email = edTEmail.getText().toString();


        // Stores a key value pair
        ContentValues values = new ContentValues();
        values.put(ContactProvider.name, name);
        values.put(ContactProvider.email, email);

        String returnedName = (String) values.get(ContactProvider.name);
        String returnedEmail = (String) values.get(ContactProvider.email);


        // Provides access to other applications Content Providers
        Uri uri = getContentResolver().insert(ContactProvider.CONTENT_URL, values);

        Toast.makeText(getBaseContext(), "New Contact Added: "
                + returnedName + " " + returnedEmail,
                Toast.LENGTH_LONG)
                .show();
    }




}
