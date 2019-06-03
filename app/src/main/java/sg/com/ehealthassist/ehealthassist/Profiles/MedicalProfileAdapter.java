package sg.com.ehealthassist.ehealthassist.Profiles;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */

public class MedicalProfileAdapter extends BaseAdapter {
    private Context _mcont;
    private List<MedicalProfile> list_medprofile;
    DBMedProfile dbmedprofile = null;

    public MedicalProfileAdapter(Context context, List<MedicalProfile> medlist) {
        this._mcont = context;
        this.list_medprofile = medlist;
        this.dbmedprofile = new DBMedProfile(context);
    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list_medprofile.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder v = null;
        MedicalProfile obj_med = list_medprofile.get(position);
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_med_profile, null);
            v.profile_name = (TextView) convertView.findViewById(R.id.txt_med_profile_name);
            v.profile_nric = (TextView) convertView.findViewById(R.id.txt_med_profile_nric);
            v.txt_med_error = (TextView)convertView.findViewById(R.id.txt_med_error);
            v.nested_card_view = (CardView) convertView.findViewById(R.id.nested_card_view);
            v.imageupload = (ImageView)convertView.findViewById(R.id.imgupload) ;
            v.imgv_profile_view = (ImageView)convertView.findViewById(R.id.imgv_profile_view);
            v.imageupload.setVisibility(View.INVISIBLE);
            v.profile_nric.setVisibility(View.INVISIBLE);
            v.profile_name.setVisibility(View.INVISIBLE);
            v.imgv_profile_view.setVisibility(View.INVISIBLE);
            v.txt_med_error.setVisibility(View.GONE);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        v.profile_name.setText(obj_med.getNric_name());
        v.profile_nric.setText("(" +obj_med.getNric() +")");

        if(obj_med.getMember()> 0){
            v.nested_card_view.setCardBackgroundColor(_mcont.getResources().getColor(R.color.cas_default_bg_coreColorDark));
            if(obj_med.getNric()== null || obj_med.getNric().equals("")){
                v.imageupload.setVisibility(View.VISIBLE);
                v.txt_med_error.setVisibility(View.VISIBLE);
            }
            else{
                v.profile_nric.setVisibility(View.VISIBLE);
                v.profile_name.setVisibility(View.VISIBLE);
                v.imgv_profile_view.setVisibility(View.VISIBLE);
                v.imageupload.setVisibility(View.GONE);
                v.txt_med_error.setVisibility(View.GONE);
            }
        }
        else{
            v.profile_nric.setVisibility(View.VISIBLE);
            v.profile_name.setVisibility(View.VISIBLE);
            v.imgv_profile_view.setVisibility(View.VISIBLE);
            v.nested_card_view.setCardBackgroundColor(_mcont.getResources().getColor(R.color.colorwhite));
        }
        int age = 0;
        if(!obj_med.getDob().equals("")){
            age = Utils.calculate_userage(obj_med.getDob());
        }

        Log.e("profile",age+"");
        if(age>=60){
            if(obj_med.getGender()>0){
                v.imgv_profile_view.setImageResource(R.drawable.profile_m_elder);
            }else{
                v.imgv_profile_view.setImageResource(R.drawable.profile_f_elder);
            }
        }
        else if(age < 60 && age >=30){
            if(obj_med.getGender()>0){
                v.imgv_profile_view.setImageResource(R.drawable.profile_m_adult);
            }else{
                v.imgv_profile_view.setImageResource(R.drawable.profile_f_adult);
            }
        }
        else if(age < 30 && age>=17){
            if(obj_med.getGender()>0){
                v.imgv_profile_view.setImageResource(R.drawable.profile_m_child);
            }else{
                v.imgv_profile_view.setImageResource(R.drawable.profile_f_child);
            }
        }else{
            if(obj_med.getGender()>0){
                v.imgv_profile_view.setImageResource(R.drawable.profile_m_child);
            }else{
                v.imgv_profile_view.setImageResource(R.drawable.profile_f_child);
            }
        }
        /*if(obj_med.getFlag_upload()> 0){
            v.imageupload.setVisibility(View.GONE);
        }
        else{
            v.imageupload.setVisibility(View.VISIBLE);
        }*/

        return convertView;
    }

    class ViewHolder {
        TextView profile_name;
        TextView profile_nric;
        TextView txt_med_error;
        ImageView imageupload,imgv_profile_view;
        CardView nested_card_view;

    }
}
