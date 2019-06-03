package sg.com.ehealthassist.ehealthassist.Home;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 6/30/2017.
 */

public class HomeMenuAdapter extends BaseAdapter {
    private final List<String> menuValues;
    Context mContext;
    int medcount = 0;

    public HomeMenuAdapter(Context context, List<String> Values,int remindercount) {
        mContext = context;
        menuValues = Values;
        medcount = remindercount;
    }

    @Override
    public int getCount() {
        return menuValues.size();
    }

    @Override
    public String getItem(int position) {
        return menuValues.get(position);
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
            convertView = inflater.inflate(R.layout.row_home_gridview, parent, false);
            holder = new ViewHolder();
            holder.text = (TextView)convertView.findViewById(R.id.txtgridview_text) ;
            holder.imgview = (ImageView)convertView.findViewById(R.id.gridview_image) ;
            holder.textcount = (TextView) convertView.findViewById(R.id.txtremindercount) ;
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textcount.setVisibility(View.GONE);

        switch (position){
            case 0:
                holder.imgview.setImageResource(R.drawable.circle_findclinic_1);
                break;
            case 1:
                holder.imgview.setImageResource(R.drawable.circle_appt_1);
                break;
            case 2:
                holder.imgview.setImageResource(R.drawable.circle_event_1);
                break;
            case 3:
                holder.imgview.setImageResource(R.drawable.circle_receipt_1);
                break;
            case 4:
                holder.imgview.setImageResource(R.drawable.circle_haze_orange_1);
                break;
            case 5:
                holder.imgview.setImageResource(R.drawable.circle_reminder_1);
                Log.e("menu adapter",medcount+"");
                if(medcount>0){

                    holder.textcount.setVisibility(View.VISIBLE);

                }else{
                    holder.textcount.setVisibility(View.GONE);
                }

                break;
            case 6:
                holder.imgview.setImageResource(R.drawable.circle_message_1);
                break;
            case 7:
                holder.imgview.setImageResource(R.drawable.shopping2);
                break;
            default:
                break;

        }
        holder.text.setText(menuValues.get(position));
        holder.textcount.setText(medcount+"");
        return convertView;
    }

    static class ViewHolder {
        TextView text;
        ImageView imgview;
        TextView textcount;
    }
}
