package sg.com.ehealthassist.ehealthassist.Clinic;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */

public class ClinicInfoAdapter extends BaseAdapter {

    public Activity activity;
    private List<ClinicInfo> clinicInfos;
    String from_activity;

    public ClinicInfoAdapter(Activity a, List<ClinicInfo> clinicInfoList, String f_act) {
        activity = a;
        from_activity = f_act;
        clinicInfos = clinicInfoList;
    }

    @Override
    public int getCount() {
        return clinicInfos.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = activity.getLayoutInflater()
                    .inflate(R.layout.list_clinic_single, null);
            holder = new ViewHolder();
            holder.clinicId = (TextView) convertView.findViewById(R.id.textViewClinicId);
            holder.clinicName = (TextView) convertView.findViewById(R.id.textViewClinicName);
            holder.icon24Hours = (ImageView) convertView.findViewById(R.id.imageViewClinicListAdaptServiceIcon24Hours);
            holder.iconChas = (ImageView) convertView.findViewById(R.id.imageViewClinicListAdaptServiceIconChas);
            holder.iconQueue = (ImageView) convertView.findViewById(R.id.imageViewClinicListAdaptServiceIconQueue);
            holder.iconappointment = (ImageView) convertView.findViewById(R.id.imageViewClinicListAdaptServiceIconAppt);
            holder.iconfavourite = (ImageView) convertView.findViewById(R.id.img_favourite);
            holder.blockno = (TextView) convertView.findViewById(R.id.textViewClinicDetailsBlockNo);
            holder.street = (TextView) convertView.findViewById(R.id.textViewClinicDetailsStreet);
            holder.unitno = (TextView) convertView.findViewById(R.id.textViewClinicDetailsUnitNo);
            holder.buildingname = (TextView) convertView.findViewById(R.id.textViewClinicDetailsBuildingName);
            holder.postalcode = (TextView) convertView.findViewById(R.id.textViewClinicDetailsPostalCode);
            holder.image_clinictype = (ImageView) convertView.findViewById(R.id.image_clinictype);
            holder.imgbtn_direction = (ImageButton) convertView.findViewById(R.id.imgbtn_direction);
            holder.txt_driection_text = (TextView) convertView.findViewById(R.id.txt_driection_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final ClinicInfo clinicInfo = clinicInfos.get(position);
        if (convertView != null) {
            if (clinicInfo.is_isBookmarked()) {
                holder.clinicName.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.colorred));
                holder.iconfavourite.setImageResource(R.drawable.ic_action_toggle_star);
            } else {
                holder.clinicName.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.colorblack));
                holder.iconfavourite.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
        }
        if (clinicInfo.getClinicType().equals("GP")) {
            holder.image_clinictype.setImageResource(R.drawable.clinicgp_35);
        } else if (clinicInfo.getClinicType().equals("SP")) {
            holder.image_clinictype.setImageResource(R.drawable.clinicsp_35);
        } else {
            holder.image_clinictype.setImageResource(R.drawable.clinicdt_35);
        }
        holder.clinicId.setText(String.valueOf(clinicInfo.get_id()));
        holder.clinicName.setText(clinicInfo.get_name());
        holder.blockno.setText(clinicInfo.getClinic_address().getBlockno());
        holder.street.setText(clinicInfo.getClinic_address().getStreet());
        holder.unitno.setText(clinicInfo.getClinic_address().getUnitno());
        holder.buildingname.setText(clinicInfo.getClinic_address().getBuilding());
        holder.postalcode.setText(clinicInfo.getClinic_address().getPostal());

        holder.icon24Hours.setVisibility(View.GONE);
        holder.iconChas.setVisibility(View.GONE);
        holder.iconQueue.setVisibility(View.GONE);
        holder.iconappointment.setVisibility(View.GONE);
        if (clinicInfo.is_is24Hours()) {
            holder.icon24Hours.setVisibility(View.VISIBLE);
        }
        if (clinicInfo.is_isChas()) {
            holder.iconChas.setVisibility(View.VISIBLE);
        }
        if (clinicInfo.is_isQueueEnabled()) {
            holder.iconQueue.setVisibility(View.VISIBLE);
        }
        if (clinicInfo.is_isapptEnabled()) {
            holder.iconappointment.setVisibility(View.VISIBLE);
        }


        holder.icon24Hours.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showIcondefinition(activity, "24HC", from_activity);
            }
        });
        holder.iconChas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showIcondefinition(activity, "CHAS", from_activity);
            }
        });
        holder.iconQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showIcondefinition(activity, "QNow", from_activity);
            }
        });
        holder.iconfavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int colorId = holder.clinicName.getCurrentTextColor();
                boolean isFavourite = (colorId == activity.getResources().getColor(R.color.colorred));
                DBClinicInfo dbHandler = new DBClinicInfo(activity);
                int rowAffected = dbHandler.toggleClinicAsFavourite(clinicInfo.get_id(), isFavourite);
                if (rowAffected > 0) {
                    if (!isFavourite) {
                        clinicInfos.get(position).set_isBookmarked(!isFavourite);
                        holder.iconfavourite.setImageResource(R.drawable.ic_action_toggle_star);
                        holder.clinicName.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.colorred));
                    } else {
                        clinicInfos.get(position).set_isBookmarked(!isFavourite);
                        holder.iconfavourite.setImageResource(R.drawable.ic_star_border_black_24dp);
                        holder.clinicName.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.colorblack));
                    }
                }
            }
        });
        if (clinicInfo.getClinic_location().get_lat().equals("0") && clinicInfo.getClinic_location().get_lng().equals("0")) {
            holder.imgbtn_direction.setVisibility(View.GONE);
            holder.txt_driection_text.setVisibility(View.GONE);
        } else {
            holder.imgbtn_direction.setVisibility(View.VISIBLE);
            holder.txt_driection_text.setVisibility(View.VISIBLE);
        }
        holder.imgbtn_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String map_package = activity.getResources().getString(R.string.google_maps_package_name);
                    ApplicationInfo mapinfo = activity.getApplication().getPackageManager().getApplicationInfo(map_package, 0);
                    if (!mapinfo.enabled) {
                        Utils.getGoogleMapsDialog(activity);
                        return;
                    }
                    String url;
                    if (clinicInfo.getClinic_location().get_lat().equals("") || clinicInfo.getClinic_location().get_lng().equals(""))
                        url = "geo:0,0?q=SINGAPORE+" + clinicInfo.getClinic_address().getPostal();
                    else {
                        String clinic_name = clinicInfo.get_name().replace(" ", "+");
                        url = "geo:0,0?q=" + clinicInfo.getClinic_location().get_lat() + "," + clinicInfo.getClinic_location().get_lng() + "(" + clinic_name + ")";
                    }
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setPackage(map_package);
                    activity.startActivity(intent);
                } catch (Exception e) {
                    Utils.getGoogleMapsDialog(activity);
                }
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView clinicId, clinicName, blockno, street, unitno, buildingname, postalcode, txt_driection_text;
        ImageView icon24Hours, iconChas, iconQueue, iconfavourite, iconappointment, image_clinictype;
        ImageButton imgbtn_direction;
    }
}

