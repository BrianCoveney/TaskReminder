package ie.cit.brian.taskreminder;



import android.content.Context;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by briancoveney on 12/16/15.
 */
public class UtilityClass extends TaskActivity {




    public static String readFromFile(Context context)
    {
        String result = "";

        try {
            InputStream inputStream = context.openFileInput("myFile");

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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }


    public static void writeToFile(Context context, String data)
    {

        try {
            FileOutputStream outputStreamWriter;

            outputStreamWriter = context.getApplicationContext().openFileOutput("myFile",
                    Context.MODE_PRIVATE);
            outputStreamWriter.write(data.getBytes());
            outputStreamWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
