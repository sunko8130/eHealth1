package sg.com.ehealthassist.ehealthassist.Doctors;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Doctor.Doctors;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */



public class DoctorAdapter extends BaseAdapter {
    private Context _mcont;
    private List<Doctors> doctorsList;

    public DoctorAdapter(Context context, List<Doctors> lstdoctors) {
        this._mcont = context;
        this.doctorsList = lstdoctors;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return doctorsList.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.doctor_row, null);
            v.txtdrname = (TextView) convertView.findViewById(R.id.doctor_name);
            v.txtnumber = (TextView) convertView.findViewById(R.id.txtnumber);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        String dr_name = doctorsList.get(position).getDr_name();
        v.txtdrname.setText(dr_name);
        v.txtnumber.setText(position + 1 + ". ");

        return convertView;
    }

    class ViewHolder {
        TextView txtdrname;
        TextView txtnumber;
    }

}


