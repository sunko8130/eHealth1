package sg.com.ehealthassist.ehealthassist.eDocument;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 9/15/2017.
 */

public class ClinicSearchAdapter extends BaseAdapter {
    private Context _mcont;
    private List<ClinicInfo> list_clinic;

    public ClinicSearchAdapter(Context _mcontext, List<ClinicInfo> lst_clinic) {
        this._mcont = _mcontext;
        this.list_clinic = new ArrayList<ClinicInfo>();
        this.list_clinic.addAll(ActivityRatingClinic.arrCliniclist);

    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return ActivityRatingClinic.arrCliniclist.size();
    }

    @Override
    public ClinicInfo getItem(int position) {
        return ActivityRatingClinic.arrCliniclist.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder v = null;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_clinicsearch, null);
            v.txt_clinicname = (TextView)convertView.findViewById(R.id.txtsearchname) ;
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
      //  ClinicInfo labobject = list_clinic.get(position);
        v.txt_clinicname.setText(ActivityRatingClinic.arrCliniclist.get(position).get_name());
        return convertView;
    }

    class ViewHolder {
        TextView txt_clinicname;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        ActivityRatingClinic.arrCliniclist.clear();
        if (charText.length() == 0) {
            ActivityRatingClinic.arrCliniclist.addAll(list_clinic);
        } else {
            for (ClinicInfo wp : list_clinic) {
                if (wp.get_name().toLowerCase(Locale.getDefault()).contains(charText)) {
                    ActivityRatingClinic.arrCliniclist.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
