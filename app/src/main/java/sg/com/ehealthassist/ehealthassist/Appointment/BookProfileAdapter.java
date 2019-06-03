package sg.com.ehealthassist.ehealthassist.Appointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */

public class BookProfileAdapter extends BaseAdapter {
    private Context _mcont;
    private List<MedicalProfile> profileList;

    public BookProfileAdapter(Context context, List<MedicalProfile> lstprofile) {
        this._mcont = context;
        this.profileList = lstprofile;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return profileList.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.spinner_profile_selected, null);
            v.txt_profile_name = (TextView) convertView.findViewById(R.id.txtspinnerprofile);
            v.imgv_profile_view = (ImageView) convertView.findViewById(R.id.imgv_profile_view);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        String dr_name = profileList.get(position).getNric_name();
        v.txt_profile_name.setText(dr_name);
        int age = Utils.calculate_userage(profileList.get(position).getDob());

        if (age >= 60) {
            if (profileList.get(position).getGender() > 0) {
                v.imgv_profile_view.setImageResource(R.drawable.profile_m_elder);
            } else {
                v.imgv_profile_view.setImageResource(R.drawable.profile_f_elder);
            }
        } else if (age < 60 && age >= 30) {
            if (profileList.get(position).getGender() > 0) {
                v.imgv_profile_view.setImageResource(R.drawable.profile_m_adult);
            } else {
                v.imgv_profile_view.setImageResource(R.drawable.profile_f_adult);
            }
        } else if (age < 30 && age >= 17) {
            if (profileList.get(position).getGender() > 0) {
                v.imgv_profile_view.setImageResource(R.drawable.profile_m_child);
            } else {
                v.imgv_profile_view.setImageResource(R.drawable.profile_f_child);
            }
        } else {
            if (profileList.get(position).getGender() > 0) {
                v.imgv_profile_view.setImageResource(R.drawable.profile_m_child);
            } else {
                v.imgv_profile_view.setImageResource(R.drawable.profile_f_child);
            }
        }
        return convertView;
    }

    class ViewHolder {
        TextView txt_profile_name;
        ImageView imgv_profile_view;
    }
}

