package sg.com.ehealthassist.ehealthassist.Clinic;

/**
 * Created by thazinhlaing on 2/7/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 8/3/2016.
 */
public class OpenHourGridview extends BaseAdapter {
    private Context mContext;
    private final List<String> mMobileValues;

    public OpenHourGridview(Context context, List<String> mobileValues) {
        mContext = context;
        mMobileValues = mobileValues;

    }

    @Override
    public int getCount() {
        return mMobileValues.size();
    }

    @Override
    public String getItem(int position) {
        return mMobileValues.get(position);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.grid_item_openhr, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.label);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.text.setText(mMobileValues.get(position));
        return convertView;
    }

    static class ViewHolder {
        TextView text;
    }
}

