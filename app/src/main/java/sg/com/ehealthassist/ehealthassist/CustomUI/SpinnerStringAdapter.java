package sg.com.ehealthassist.ehealthassist.CustomUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 6/30/2017.
 */

public class SpinnerStringAdapter extends ArrayAdapter {
    private String[] object_string;
    private Context context;

    public SpinnerStringAdapter(Context context, int resourceid, String[] objects) {
        super(context, resourceid, objects);
        this.object_string = objects;
        this.context = context;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.spinner_profile_selected, parent, false);
        TextView txtname = (TextView) row.findViewById(R.id.txtspinnerprofile);
        txtname.setText(object_string[position]);

        return row;
    }
}
