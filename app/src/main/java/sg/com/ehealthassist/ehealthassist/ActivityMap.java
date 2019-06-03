package sg.com.ehealthassist.ehealthassist;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.GCM.GPSTracker;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Other.Utils;

public class ActivityMap extends AppCompatActivity {
    private GoogleMap googleMap; // Might be null if Google Play services APK is not available.
    Context mcontext;
    List<ClinicInfo> lstclinicfo;
    Location loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_map);
        mcontext = this;
        Intent i = getIntent();
        lstclinicfo = i.getParcelableArrayListExtra("clinic");

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    //region setUpMapIfNeed()
    private void setUpMapIfNeeded() {
        if (googleMap == null) {
            // googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
            if (googleMap != null) {
                googleMap.setMyLocationEnabled(true);
                startMap();
            }
        }
    }

    //endregion
    //region startMap()
    private void startMap() {
        googleMap.clear();
        GPSTracker tracker = new GPSTracker(this);
        if (!Utils.hasInternetConnection(getApplicationContext())) {
            Utils.showInternetRequiredDialog(this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
        if (tracker.canGetLocation()) {
            loc = tracker.getLocation();
        } else {
            tracker.showSettingsAlert();
        }
        LatLng userPosition;
       /* if (lstclinicfo.size() > 0) {
            Log.e("lat value", lstclinicfo.get(0).getClinic_location().get_lat() + "" + lstclinicfo.get(0).getClinic_location().get_lng());
            userPosition = new LatLng(Double.valueOf(lstclinicfo.get(0).getClinic_location().get_lat() == "null" ? 0.0 : Double.valueOf(lstclinicfo.get(0).getClinic_location().get_lat())),
                    Double.valueOf(lstclinicfo.get(0).getClinic_location().get_lng() == "null" ? 0.0 : Double.valueOf(lstclinicfo.get(0).getClinic_location().get_lng())));
        }*/
        //  else {
        userPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
        // }


        for (ClinicInfo marker_clinic : lstclinicfo) {

            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng
                            (Double.valueOf(marker_clinic.getClinic_location().get_lat()), Double.valueOf(marker_clinic.getClinic_location().get_lng())))
                    .title(marker_clinic.get_name()));

        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userPosition) // Sets the center of the map to
                .zoom(17) // Sets the zoom
                .tilt(30) // Sets the tilt of the camera to 30 degrees
                .build(); // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));

    }

    //endregion
    //region onKeyDown()
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            /*Intent intent = new Intent(getApplicationContext(), ActivitySearchClinic.class);
            intent.putExtra("fromactivity", getString(R.string.ActivityMap));
            intent.putExtra("FindBy", getString(R.string.search_bynearme));
            startActivity(intent);*/
        }
        return super.onKeyDown(keyCode, event);
    }
    //endregion

}
