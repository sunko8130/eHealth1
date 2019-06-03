package sg.com.ehealthassist.ehealthassist.eDocument;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 9/14/2017.
 */

public class ClinicRateAdapter extends BaseAdapter {
    private Context _mcont;
    private List<ClinicInfo> list_clinic;

    public ClinicRateAdapter(Context _mcontext, List<ClinicInfo> lst_info) {

        this._mcont = _mcontext;
        this.list_clinic = lst_info;
    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list_clinic.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_ratingclinic, null);
            v.txtrateclinic = (TextView)convertView.findViewById(R.id.txtrateclinic) ;

            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        ClinicInfo clinicobj = list_clinic.get(position);
        v.txtrateclinic.setText(clinicobj.get_name());

        return convertView;
    }

    class ViewHolder {
        TextView txtrateclinic;
    }

}
