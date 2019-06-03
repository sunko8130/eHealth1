package sg.com.ehealthassist.ehealthassist.Profiles;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Models.Profile.ResponseDownloadStatus;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */

public class DownloadProfileStatusAdapter extends BaseAdapter {

    private Context _mcont;
    private ArrayList<ResponseDownloadStatus> lstdown_status;

    public DownloadProfileStatusAdapter(Context _mcontext, ArrayList<ResponseDownloadStatus> lst_status) {
        _mcont = _mcontext;
        lstdown_status = lst_status;
    }

    @Override
    public int getCount() {
        return lstdown_status.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder view = null;
        if (convertView == null) {
            view = new ViewHolder();
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_download_profilestatus, null);
            view.img_back = (ImageButton) convertView.findViewById(R.id.imgbtn_back);
            view.imgv_status = (ImageView) convertView.findViewById(R.id.imgv_status);
            view.txt_profilename = (TextView) convertView.findViewById(R.id.txtprofile_name);
            view.txtprofile_nric = (TextView) convertView.findViewById(R.id.txtprofile_nric);
            view.imgv_profile = (ImageView) convertView.findViewById(R.id.imgv_profile_view);
            convertView.setTag(view);
        } else {
            view = (ViewHolder) convertView.getTag();
        }
        ResponseDownloadStatus obj_response = lstdown_status.get(position);
        view.txt_profilename.setText(obj_response.getNricname());
        view.txtprofile_nric.setText(obj_response.getNric());
        if (obj_response.getDownloaded().equals("Y")) {
            view.imgv_status.setImageResource(R.drawable.v_tick_50);
        } else if (obj_response.getDownloaded().equals("N")) {
            view.imgv_status.setImageResource(R.drawable.v_close_50);
        } else if (obj_response.getDownloaded().equals("P")) {
            view.imgv_status.setImageResource(R.drawable.v_pending_50);
        } else {
            view.imgv_status.setImageResource(R.drawable.v_question_50);
        }
        int age = Utils.calculate_userage(obj_response.getDob());
        if (age >= 60) {
            if (obj_response.getGender() > 0) {
                view.imgv_profile.setImageResource(R.drawable.profile_m_elder);
            } else {
                view.imgv_profile.setImageResource(R.drawable.profile_f_elder);
            }
        } else if (age < 60 && age >= 30) {
            if (obj_response.getGender() > 0) {
                view.imgv_profile.setImageResource(R.drawable.profile_m_adult);
            } else {
                view.imgv_profile.setImageResource(R.drawable.profile_f_adult);
            }
        } else if (age < 30 && age >= 17) {
            if (obj_response.getGender() > 0) {
                view.imgv_profile.setImageResource(R.drawable.profile_m_child);
            } else {
                view.imgv_profile.setImageResource(R.drawable.profile_f_child);
            }
        } else {
            if (obj_response.getGender() > 0) {
                view.imgv_profile.setImageResource(R.drawable.profile_m_child);
            } else {
                view.imgv_profile.setImageResource(R.drawable.profile_f_child);
            }
        }
        return convertView;
    }

    class ViewHolder {
        ImageButton img_back;
        ImageView imgv_status;
        ImageView imgv_profile;
        TextView txt_profilename, txtprofile_nric;
    }
}
