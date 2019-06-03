package sg.com.ehealthassist.ehealthassist.Clinic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Clinic.Clinic_Services;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */


public class Clinic_ServicesAdapter extends BaseAdapter {
    private Context _mcont;
    private List<Clinic_Services> clinic_services;

    public Clinic_ServicesAdapter(Context context, List<Clinic_Services> lstclinicservices) {
        this._mcont = context;
        this.clinic_services = lstclinicservices;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return clinic_services.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.clinic_services_row, null);
            v.txtdrname = (TextView) convertView.findViewById(R.id.clinictxtnumber);

            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        String dr_name = clinic_services.get(position).getSerivices_name();

        v.txtdrname.setText(". " + dr_name);

        return convertView;
    }

    class ViewHolder {
        TextView txtdrname;

    }
}


