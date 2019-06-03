package sg.com.ehealthassist.ehealthassist.CustomUI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */

public class GridAdapter extends BaseAdapter {
    private Context mContext;
    private final List<String> mMobileValues;
    // private final AppointmentSlot app_slot_obj;
    //  String grouptitle;

    public GridAdapter(Context context, List<String> mobileValues) {
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
            convertView = inflater.inflate(R.layout.grid_item, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.label);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //   holder.text.setText(Utils.GridviewChangefromat(mMobileValues.get(position),"yyyy-MM-dd'T'HH:mm:ss","hh:mm aa"));

        String slot_time = Utils.BookDateFormat(mMobileValues.get(position), "yyyy-MM-dd'T'HH:mm:ss", "HH:mm a");
        holder.text.setText(slot_time);
      /*  holder.text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.text.setTextColor(mContext.getResources().getColor(R.color.cas_color_green));


            }
        });*/

        return convertView;
    }

    static class ViewHolder {
        TextView text;
    }


}

