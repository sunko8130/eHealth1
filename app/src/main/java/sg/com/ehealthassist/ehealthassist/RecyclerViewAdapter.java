package sg.com.ehealthassist.ehealthassist;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Other.Utils;

/**
 * Created by Wunna Tun on 22-Dec-17.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder>{
    final List<ClinicInfo> horizontallist;
    Activity activity;
    String from_activity;
    public RecyclerViewAdapter(Activity activity, List<ClinicInfo> horizontallist, String from_activity) {
        this.activity=activity;
        this.horizontallist=horizontallist;
        this.from_activity=from_activity;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.list_clinic_single,parent,false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position) {
       /* holder.txtview.setText(horizontallist.get(position));
        holder.txtview.setTextColor(res.getColor(R.color.theme_primary));
        holder.txtview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,holder.txtview.getText().toString(),Toast.LENGTH_SHORT).show();

            }
        });*/
        final ClinicInfo clinicInfo=horizontallist.get(position);
      /*  if (holder != null) {
            if (clinicInfo.is_isBookmarked()) {
                holder.clinicName.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.colorred));
                holder.iconfavourite.setImageResource(R.drawable.ic_action_toggle_star);
            } else {
                holder.clinicName.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.colorblack));
                holder.iconfavourite.setImageResource(R.drawable.ic_star_border_black_24dp);
            }
        }*/
        holder.clinicName.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.colorblack));
        holder.iconfavourite.setImageResource(R.drawable.ic_star_border_black_24dp);
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
                        horizontallist.get(position).set_isBookmarked(!isFavourite);
                        holder.iconfavourite.setImageResource(R.drawable.ic_action_toggle_star);
                        holder.clinicName.setTextColor(activity.getApplicationContext().getResources().getColor(R.color.colorred));
                    } else {
                        horizontallist.get(position).set_isBookmarked(!isFavourite);
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
    }

    @Override
    public int getItemCount() {
        return horizontallist.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView clinicId, clinicName, blockno, street, unitno, buildingname, postalcode, txt_driection_text;
        ImageView icon24Hours, iconChas, iconQueue, iconfavourite, iconappointment, image_clinictype;
        ImageButton imgbtn_direction;

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            clinicId=(TextView)itemView.findViewById(R.id.textViewClinicId);
            clinicName=(TextView)itemView.findViewById(R.id.textViewClinicName);
            blockno=(TextView)itemView.findViewById(R.id.textViewClinicDetailsBlockNo);
            street=(TextView)itemView.findViewById(R.id.textViewClinicDetailsStreet);
            unitno=(TextView)itemView.findViewById(R.id.textViewClinicDetailsUnitNo);
            buildingname=(TextView)itemView.findViewById(R.id.textViewClinicDetailsBuildingName);
            postalcode=(TextView)itemView.findViewById(R.id.textViewClinicDetailsPostalCode);
            txt_driection_text=(TextView)itemView.findViewById(R.id.txt_driection_text);
            icon24Hours=(ImageView) itemView.findViewById(R.id.imageViewClinicListAdaptServiceIcon24Hours);
            iconChas=(ImageView) itemView.findViewById(R.id.imageViewClinicListAdaptServiceIconChas);
            iconQueue=(ImageView) itemView.findViewById(R.id.imageViewClinicListAdaptServiceIconQueue);
            iconfavourite=(ImageView) itemView.findViewById(R.id.img_favourite);
            iconappointment=(ImageView) itemView.findViewById(R.id.imageViewClinicListAdaptServiceIconAppt);
            image_clinictype=(ImageView) itemView.findViewById(R.id.image_clinictype);
            imgbtn_direction=(ImageButton) itemView.findViewById(R.id.imgbtn_direction);

        }
    }
}
