package sg.com.ehealthassist.ehealthassist.Medication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalDispense;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 8/4/2017.
 */

public class ListSlotTimeAdapter extends BaseAdapter {
    private Context _mcont;
    private List<String> medicationlist;
    private String takestr="";

    public ListSlotTimeAdapter(Context context, List<String> lstmedication,String take) {
        this._mcont = context;
        this.medicationlist = lstmedication;
        this.takestr =take;
    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return medicationlist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder v = null;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_remindertimeslot, null);
            v.reminderslottime = (TextView) convertView.findViewById(R.id.timeslot);
            v.timeslot_take = (TextView) convertView.findViewById(R.id.timeslot_take);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        String slottime = medicationlist.get(position);
        String takes = "Time "+(position+1) + "("+takestr+")";
        v.reminderslottime.setText(slottime);
        v.timeslot_take.setText(takes);
        return convertView;
    }

    class ViewHolder {
        TextView reminderslottime,timeslot_take;

    }

}
