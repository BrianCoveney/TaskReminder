package ie.cit.brian.taskreminder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by briancoveney on 2/16/16.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    public CustomAdapter(Context context, String[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.drawer_list_item, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.mNavDrawerListTV);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.mNavDrawerListIcons);
        textView.setText(values[position]);

        // add Icons to the Nav Drawer Items
        switch (position){
            case 0:
                imageView.setImageResource(R.drawable.ic_settings_applications_48dp);
                break;
            case 1:
                imageView.setImageResource(R.drawable.ic_satellite_48dp);
                break;
            case 2:
                imageView.setImageResource(R.drawable.ic_map_48dp);
                break;
            default:
        }

        return rowView;
    }
}
