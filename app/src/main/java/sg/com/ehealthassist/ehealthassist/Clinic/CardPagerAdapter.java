package sg.com.ehealthassist.ehealthassist.Clinic;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.location.Location;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.CustomUI.CardAdapter;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */

public class CardPagerAdapter extends PagerAdapter implements CardAdapter {
    private List<CardView> mViews;
    private List<ClinicInfo> mData = new ArrayList<ClinicInfo>();
    private float mBaseElevation;
    Activity _mactivity;
    Location current_loc;

    public CardPagerAdapter(List<ClinicInfo> viewlist, Activity activity, Location _loc) {
        mData = viewlist;
        mViews = new ArrayList<>();
        _mactivity = activity;
        current_loc = _loc;
        for (int i = 0; i < mData.size(); i++) {
            mViews.add(null);
        }
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.cardadapter_row, container, false);
        container.addView(view);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }
        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);

        TextView clinicId = (TextView) cardView.findViewById(R.id.textViewClinicId);
        final TextView clinicName = (TextView) cardView.findViewById(R.id.textViewClinicName);
        TextView blockno = (TextView) cardView.findViewById(R.id.textViewClinicDetailsBlockNo);
        TextView street = (TextView) cardView.findViewById(R.id.textViewClinicDetailsStreet);
        TextView unitno = (TextView) cardView.findViewById(R.id.textViewClinicDetailsUnitNo);
        TextView buildingname = (TextView) cardView.findViewById(R.id.textViewClinicDetailsBuildingName);
        TextView postalcode = (TextView) cardView.findViewById(R.id.textViewClinicDetailsPostalCode);
        TextView txt_distance_value = (TextView) cardView.findViewById(R.id.txt_distance_value);
        ImageView image_clinic_type = (ImageView) cardView.findViewById(R.id.image_clinictype);
        ImageButton imgbtn_direction = (ImageButton) cardView.findViewById(R.id.imgbtn_direction);
        ImageView icon24Hours = (ImageView) cardView.findViewById(R.id.imageViewClinicListAdaptServiceIcon24Hours);
        ImageView iconChas = (ImageView) cardView.findViewById(R.id.imageViewClinicListAdaptServiceIconChas);
        ImageView iconQueue = (ImageView) cardView.findViewById(R.id.imageViewClinicListAdaptServiceIconQueue);
        ImageView iconappointment = (ImageView) cardView.findViewById(R.id.imageViewClinicListAdaptServiceIconAppt);
        ImageView iconfavourite = (ImageView) cardView.findViewById(R.id.img_favourite);

        final ClinicInfo _clinicinfo = mData.get(position);
        clinicName.setText(_clinicinfo.get_name());
        blockno.setText(_clinicinfo.getClinic_address().getBlockno());
        street.setText(_clinicinfo.getClinic_address().getStreet());
        unitno.setText(_clinicinfo.getClinic_address().getUnitno());
        buildingname.setText(_clinicinfo.getClinic_address().getBuilding());
        postalcode.setText(_clinicinfo.getClinic_address().getPostal());
        Float latitude = Float.parseFloat(_clinicinfo.getClinic_location().get_lat());
        Float longitude = Float.parseFloat(_clinicinfo.getClinic_location().get_lng());
        Float currentlatitude = (float) current_loc.getLatitude();
        Float currentlongitude = (float) current_loc.getLongitude();

        float meter = distFrom(currentlatitude, currentlongitude, latitude, longitude);
        if (meter > 0) {
            txt_distance_value.setText(new DecimalFormat("##.##").format(meter) + "m");
        }
        switch (_clinicinfo.getClinicType()) {
            case "GP":
                image_clinic_type.setImageResource(R.drawable.clinicgp_35);
                break;
            case "DT":
                image_clinic_type.setImageResource(R.drawable.clinicdt_35);
                break;
            case "SP":
                image_clinic_type.setImageResource(R.drawable.clinicsp_35);
                break;
            default:
                break;
        }
        if (_clinicinfo.is_isBookmarked()) {
            iconfavourite.setImageResource(R.drawable.ic_action_toggle_star);
        } else {
            iconfavourite.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
        icon24Hours.setVisibility(View.GONE);
        iconChas.setVisibility(View.GONE);
        iconQueue.setVisibility(View.GONE);
        iconappointment.setVisibility(View.GONE);
        if (_clinicinfo.is_is24Hours()) {
            icon24Hours.setVisibility(View.VISIBLE);
        }
        if (_clinicinfo.is_isChas()) {
            iconChas.setVisibility(View.VISIBLE);
        }
        if (_clinicinfo.is_isQueueEnabled()) {
            iconQueue.setVisibility(View.VISIBLE);
        }
        if (_clinicinfo.is_isapptEnabled()) {
            iconappointment.setVisibility(View.VISIBLE);
        }
        imgbtn_direction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String map_package = _mactivity.getResources().getString(R.string.google_maps_package_name);
                    //ApplicationInfo mapinfo = _mactivity.getApplication().getPackageManager().getApplicationInfo(map_package,0);
                    ApplicationInfo mapinfo = _mactivity.getPackageManager().getApplicationInfo(map_package, 0);

                    if (!mapinfo.enabled) {
                        Utils.getGoogleMapsDialog(_mactivity);
                        return;
                    }
                    String url;
                    if (_clinicinfo.getClinic_location().get_lat().equals("") || _clinicinfo.getClinic_location().get_lng().equals(""))
                        url = "geo:0,0?q=SINGAPORE+" + _clinicinfo.getClinic_address().getPostal();
                    else {
                        String clinic_name = _clinicinfo.get_name().replace(" ", "+");
                        url = "geo:0,0?q=" + _clinicinfo.getClinic_location().get_lat() + "," + _clinicinfo.getClinic_location().get_lng() + "(" + clinic_name + ")";
                    }

                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                    intent.setPackage(map_package);
                    _mactivity.startActivity(intent);
                } catch (Exception e) {
                    Utils.getGoogleMapsDialog(_mactivity);
                }

            }
        });

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int clinicId = _clinicinfo.get_id();
                Intent intent = new Intent(_mactivity, ActivitySearchClinicDetailed.class);
                intent.putExtra("CID", clinicId);
                intent.putExtra("Caller", _mactivity.getResources().getString(R.string.Activityclinicdetailed));
                _mactivity.startActivity(intent);
            }
        });

        mViews.set(position, cardView);

        return view;
    }

    public static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng / 2) * Math.sin(dLng / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        float dist = (float) (earthRadius * c);

        return dist;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

}

