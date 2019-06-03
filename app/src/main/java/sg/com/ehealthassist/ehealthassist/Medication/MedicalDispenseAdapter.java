package sg.com.ehealthassist.ehealthassist.Medication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalDispense;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */

public class MedicalDispenseAdapter extends BaseAdapter {
    private Context _mcont;
    private List<MedicalDispense> medicationlist;


    public MedicalDispenseAdapter(Context context, List<MedicalDispense> lstmedication) {
        this._mcont = context;
        this.medicationlist = lstmedication;
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_medicaldispense, null);
            v.txt_visitno = (TextView) convertView.findViewById(R.id.txtvisitno);
            v.txt_visitdate = (TextView) convertView.findViewById(R.id.txtvisitdate);
            v.txtpatient = (TextView) convertView.findViewById(R.id.txtpatient);

            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        String clinicname = medicationlist.get(position).getClinicName();
        String visitno = medicationlist.get(position).getVisitNo();
        String visitdate = medicationlist.get(position).getVisitDate();
        String patientname = medicationlist.get(position).getPatientName();
        String chgfrmdate = Utils.GridviewChangefromat(visitdate,"yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
        v.txt_visitno.setText(visitno);
        v.txt_visitdate.setText(chgfrmdate);
        v.txtpatient.setText(patientname);
        return convertView;
    }

    class ViewHolder {

        TextView txt_visitno;
        TextView txt_visitdate;
        TextView txtpatient;

    }

}

