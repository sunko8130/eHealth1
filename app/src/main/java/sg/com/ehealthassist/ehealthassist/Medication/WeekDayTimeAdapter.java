package sg.com.ehealthassist.ehealthassist.Medication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Medical.WeekSlot;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 8/4/2017.
 */

public class WeekDayTimeAdapter extends BaseAdapter {
    public Context _mcont;
    public ArrayList<WeekSlot> wslot;

    public WeekDayTimeAdapter(Context context, ArrayList<WeekSlot> lstmedication) {
        this._mcont = context;
        this.wslot = lstmedication;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return wslot.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder v = null;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_remindeweekslot, null);
            v.reminderslotweek = (TextView)convertView.findViewById(R.id.weekslot) ;
            v.chk_week = (CheckBox) convertView.findViewById(R.id.chk_week) ;

            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        final WeekSlot slottime = wslot.get(position);

        v.reminderslotweek.setText(slottime.getSlotname());
        v.chk_week.setChecked(slottime.getChecked());

        v.chk_week.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
               // slottime.setChecked(b);
                wslot.get(position).setChecked(b);

            }
        });
        return convertView;
    }

    class ViewHolder{
        TextView reminderslotweek;
        CheckBox chk_week;

    }

}
