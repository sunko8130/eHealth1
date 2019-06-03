package sg.com.ehealthassist.ehealthassist.Medication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalDispense;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */

public class MedicationclinicAdapter extends BaseAdapter {
    private Context _mcont;
    private List<MedicalDispense> lstdispense;
    MedicalDispenseAdapter adapter;
    String preclinicid="";

    public MedicationclinicAdapter(Context context, List<MedicalDispense> lstdispense) {
        this._mcont = context;
        this.lstdispense = lstdispense;

    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return lstdispense.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder v = null;
        MedicalDispense medobj = lstdispense.get(position);
        if (convertView == null) {
            LayoutInflater inflater=(LayoutInflater)_mcont.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.row_medicalclinic, parent, false);
            v = new ViewHolder();

            v.txt_clinicname = (TextView) convertView.findViewById(R.id.txtdispenseclinicname);
            v.txt_visitno = (TextView) convertView.findViewById(R.id.txtvisitno);
            v.txt_visitdate = (TextView) convertView.findViewById(R.id.txtvisitdate);
            v.txtpatient = (TextView) convertView.findViewById(R.id.txtpatient);
            v.llheader =(LinearLayout) convertView.findViewById(R.id.llheader);

            v.llheader.setVisibility(View.GONE);

            if(position ==0){
                v.llheader.setVisibility(View.VISIBLE);
                preclinicid = medobj.getClinicId();
            }
            else{
                if (preclinicid.equals(lstdispense.get(position).getClinicId())) {
                    v.llheader.setVisibility(View.GONE);

                }
                else{
                    v.llheader.setVisibility(View.VISIBLE);
                    preclinicid = medobj.getClinicId();
                }
            }
            convertView.setTag(v);
            convertView.setTag(R.id.llheader,v.llheader);

        } else {
            v = (ViewHolder) convertView.getTag();
        }

        String clinicname = medobj.getClinicName();
        String visitno = medobj.getVisitNo();
        String visitdate = medobj.getVisitDate();
        String patientname = medobj.getPatientName();
        String chgfrmdate = Utils.GridviewChangefromat(visitdate,"yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");

        v.txt_visitno.setText(visitno);
        v.txt_visitdate.setText(chgfrmdate);
        v.txtpatient.setText(patientname);
        v.txt_clinicname.setText(clinicname);

        return convertView;
    }

    private static class ViewHolder {
        TextView txt_clinicname;
        LinearLayout llheader;
        TextView txt_visitno;
        TextView txt_visitdate;
        TextView txtpatient;
    }
}

