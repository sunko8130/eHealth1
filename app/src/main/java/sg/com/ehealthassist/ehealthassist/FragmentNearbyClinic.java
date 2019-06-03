package sg.com.ehealthassist.ehealthassist;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import sg.com.ehealthassist.ehealthassist.Clinic.ActivitySearchClinicDetailed;
import sg.com.ehealthassist.ehealthassist.Clinic.CardPagerAdapter;
import sg.com.ehealthassist.ehealthassist.Clinic.ClinicInfoAdapter;
import sg.com.ehealthassist.ehealthassist.Clinic.ClinicTypeAdapter;
import sg.com.ehealthassist.ehealthassist.Clinic.SpecialistClinicAdapter;
import sg.com.ehealthassist.ehealthassist.CustomUI.CustomListView;
import sg.com.ehealthassist.ehealthassist.CustomUI.ShadowTransformer;
import sg.com.ehealthassist.ehealthassist.CustomUI.SlidingUpPanelLayout;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBService_items;
import sg.com.ehealthassist.ehealthassist.DB.DBServices;
import sg.com.ehealthassist.ehealthassist.GCM.GPSTracker;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Services.Services;
import sg.com.ehealthassist.ehealthassist.Models.Services.Services_Items;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Service.Services_ItemsAdapter;

/**
 * Created by User on 11/21/2016.
 */

public class FragmentNearbyClinic extends Fragment implements SlidingUpPanelLayout.PanelSlideListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener {

    RecyclerView clinic_detail;
    RecyclerViewAdapter recyclerViewAdapter;
    private List<ClinicInfo> arrayList;


    private GoogleMap googleMap;
    private SlidingUpPanelLayout mSlidingUpPanelLayout;
    private CustomListView mListView;
    EditText editsearch;
    Context _mcontext;
    List<ClinicInfo> find_dist = new ArrayList<ClinicInfo>();
    List<ClinicInfo> search_list = new ArrayList<ClinicInfo>();
    List<ClinicInfo> search_list_click = new ArrayList<ClinicInfo>();
    Location loc;
    LatLng userPosition;
    DBClinicInfo dbClinicInfo;
    Marker previousmarker;
    String previous_clinictype = null;
    private ViewPager mViewPager;
    public ClinicInfoAdapter clinicInfoAdapter;
    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;
    private SupportMapFragment mMapFragment;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    boolean is_collapse = false;
    SpecialistClinicAdapter adpter_speciallist;
    Services_ItemsAdapter adapter_itmes;
    RelativeLayout rlclinic_type, rlspecilty, rlclinic_service, loadingPanel,
            rl_description, rlclinicbottom, rlsearch_edittext, rlnearby_img, rlgp, rlsp, rldt;
    TextView txtservices_value, txtspecialty_value, txtclinictype_value, txt_dialog_title, txttotalclinic,
            txt_clinic_value, txtViewEmptyLabel, txt_clinic_24hr_value, txt_clinic_chas_value,txtdownloadaccount;
    ImageButton imgbtn_cross, imgbtn_maprefresh, imgtype_close, imgspecial_close, imgservice_close;
    RecyclerView recyclerView;
    ListView lvclinictype, lvspecialist;
    List<Services> list_clinicspec;
    List<Services_Items> list_servicesitems;
    DBServices db_services;
    DBService_items db_services_item;
    final Handler handler = new Handler();
    String search_type = "", _specialty = "", services = "";
    String search_str = "";
    String search_type_click = "";
    boolean callback_check = false;
    SharedPreferences pref_constant = null;

    public FragmentNearbyClinic() {
    }

    public static FragmentNearbyClinic newInstance() {
        FragmentNearbyClinic f = new FragmentNearbyClinic();
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_nearbypanel, container, false);
        _mcontext = getActivity();
        pref_constant = _mcontext.getSharedPreferences(getString(R.string.preference_constant), _mcontext.MODE_PRIVATE);
        dbClinicInfo = new DBClinicInfo(_mcontext);
        db_services = new DBServices(_mcontext);
        db_services_item = new DBService_items(_mcontext);
        mListView = (CustomListView) rootView.findViewById(R.id.list2);
        editsearch = (EditText) rootView.findViewById(R.id.edit_clinic_search);
        /*OVER_SCROLL_NEVER*/
        mListView.setOverScrollMode(ListView.SCROLL_AXIS_HORIZONTAL);
        mViewPager = (ViewPager) rootView.findViewById(R.id.viewPager_panel);

        rlclinic_type = (RelativeLayout) rootView.findViewById(R.id.rlclinic_type);
        rlspecilty = (RelativeLayout) rootView.findViewById(R.id.rlspecilty);
        rlclinic_service = (RelativeLayout) rootView.findViewById(R.id.rlclinic_service);
        rlnearby_img = (RelativeLayout) rootView.findViewById(R.id.rlnearby_img);
        rlclinicbottom = (RelativeLayout) rootView.findViewById(R.id.rlclinicbottom);
        rlsearch_edittext = (RelativeLayout) rootView.findViewById(R.id.rlsearch_edittext);



        clinic_detail=(RecyclerView)rootView.findViewById(R.id.clinic_detail);





       /* adding to the pager*/

        rlgp = (RelativeLayout) rootView.findViewById(R.id.rlgp);
        rlsp = (RelativeLayout) rootView.findViewById(R.id.rlsp);
        rldt = (RelativeLayout) rootView.findViewById(R.id.rldt);
        //rlsc = (RelativeLayout)rootView.findViewById(R.id.txt_selected_clinic);

        rlgp.setOnClickListener(this);
        rlsp.setOnClickListener(this);
        rldt.setOnClickListener(this);
        //rlsc.setOnClickListener(this);

        //MyCustomRecyclerAdapter adapter = new MyCustomRecyclerAdapter(getActivity(),)

        loadingPanel = (RelativeLayout) rootView.findViewById(R.id.panel_loadingPanel);

        rl_description = (RelativeLayout) rootView.findViewById(R.id.rl_description);
        txtservices_value = (TextView) rootView.findViewById(R.id.txtservices_value);
        txtspecialty_value = (TextView) rootView.findViewById(R.id.txtspecialty_value);
        txtclinictype_value = (TextView) rootView.findViewById(R.id.txtclinictype_value);
        txttotalclinic = (TextView) rootView.findViewById(R.id.txttotalclinic);
        txt_clinic_value = (TextView) rootView.findViewById(R.id.txt_clinic_value);
        imgbtn_cross = (ImageButton) rootView.findViewById(R.id.imgbtn_cross);
        txtViewEmptyLabel = (TextView) rootView.findViewById(R.id.textViewEmptyListSearchClinic);
        txt_clinic_24hr_value = (TextView) rootView.findViewById(R.id.txtclinic_24hrvalue);
        txt_clinic_chas_value = (TextView) rootView.findViewById(R.id.txtclinic_chasvalue);
        txtdownloadaccount = (TextView)rootView.findViewById(R.id.txtdownloadaccount);
        imgbtn_maprefresh = (ImageButton) rootView.findViewById(R.id.imgbtn_maprefresh);
        mSlidingUpPanelLayout = (SlidingUpPanelLayout) rootView.findViewById(R.id.slidingLayout2);
        mSlidingUpPanelLayout.setPanelSlideListener(this);
/*
        mSlidingUpPanelLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Animation moveup=AnimationUtils.loadAnimation(_mcontext,R.anim.slide_up);
                mSlidingUpPanelLayout.startAnimation(moveup);
                return true;
            }
        });*/

        mSlidingUpPanelLayout.setEnableDragViewTouchEvents(true);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if(find_dist.size()>0){
                    if (slidingpanellayoutheight() < 200) {
                        mSlidingUpPanelLayout.setPanelHeight(slidingpanellayoutheight() + 200);
                    } else {
                        mSlidingUpPanelLayout.setPanelHeight(slidingpanellayoutheight() * 2);
                    }
                }else{
                    mSlidingUpPanelLayout.setPanelHeight(slidingpanellayoutheight());
                }
                //  mSlidingUpPanelLayout.setPanelHeight(slidingpanellayoutheight());
                 //mSlidingUpPanelLayout.setPanelHeight(270);
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });

        /*arrayList=new ArrayList<String>();
        arrayList.add("User 1");
        arrayList.add("User 2");
        arrayList.add("User 3");
        arrayList.add("User 4");
        arrayList.add("User 5");
        arrayList.add("User 6");
        arrayList.add("User 7");
        arrayList.add("User 8");
        arrayList.add("User 9");
        arrayList.add("User 10");
        arrayList.add("User 11");
        arrayList.add("User 12");
        arrayList.add("User 13");
        arrayList.add("User 14");
        arrayList.add("User 15");
        arrayList.add("User 16");
        arrayList.add("User 17");*/

/*        public void SearchByclinic() {
            String searchString = editsearch.getText().toString();
            if (searchString.matches("^\\d{6}$")) {///postal
                //search_list = dbClinicInfo.getClinicInfoBytype_Services(services, search_type, _specialty, searchString, false, false, false);
                search_list = dbClinicInfo.getClinicInfos(false, false, false, Integer.parseInt(searchString));

            } else {// if (!searchString.equals("")) { // town
                //search_list = dbClinicInfo.getClinicInfoBytype_Services(services, search_type, _specialty, searchString, false, false, false);
                search_list = dbClinicInfo.getClinicInfos(false, false, false, searchString);
            }
            txtclinictype_value.setText("All");
            txtspecialty_value.setText("All");
            txtservices_value.setText("All");
            LoadSearchClinic(search_list);
        }*/

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMapFragment = SupportMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.mapContainer, mMapFragment, "map");
        fragmentTransaction.commit();
    //    int height = slidingpanellayoutheight();
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        setTounchListener();
        setEvent();
        handler.postDelayed(r, 1000);
    }

    public int slidingpanellayoutheight() {
        int rlnearby = rlnearby_img.getHeight();
        int rlsearch = rlsearch_edittext.getHeight();
        int rlclinic_detail=clinic_detail.getHeight();
        return rlnearby + rlsearch + rlclinic_detail+30;
    }


    final Runnable r = new Runnable() {
        public void run() {
           // if (ActivitySplashScreen.download_flag) {
            if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                if (!callback_check) {
                    Log.e("runnable flag" ,"true arrived");
                    loadingPanel.setVisibility(View.GONE);
                  //  LoadViewPager();
                   if (mSlidingUpPanelLayout.getPanelHeight() == 0) {
                        mSlidingUpPanelLayout.setPanelHeight(slidingpanellayoutheight());
                        Log.e("sliding panel size" ," arrived");
                    }

                    setUpMapIfNeeded();
                    imgbtn_maprefresh.setVisibility(View.VISIBLE);
                    rl_description.setVisibility(View.VISIBLE);
                    handler.removeCallbacksAndMessages(null);
                    callback_check = true;
                }
            } else {
                loadingPanel.setVisibility(View.VISIBLE);
                txtdownloadaccount.setText(ActivitySplashScreen.download_count);

                mSlidingUpPanelLayout.setPanelHeight(0);

              //  mSlidingUpPanelLayout.setPanelHeight(slidingpanellayoutheight());
                imgbtn_maprefresh.setVisibility(View.GONE);
                rl_description.setVisibility(View.GONE);
                callback_check = false;
            }
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Fragment Nearby Clinic" ,"onResume");
        setUpMapIfNeeded();
    }

    //region Slide Panel
    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        rlnearby_img.setVisibility(View.VISIBLE);
        mViewPager.setVisibility(View.GONE);
        is_collapse = false;
        mSlidingUpPanelLayout.setPanelHeight(slidingpanellayoutheight());
    }

    @Override
    public void onPanelCollapsed(View panel) {
        if (find_dist.size() > 0) {
            LoadSearchClinic(find_dist);
        } else {
            LoadSearchClinic(search_list);
        }
        is_collapse = false;
        rlnearby_img.setVisibility(View.VISIBLE);
        mSlidingUpPanelLayout.setPanelHeight(slidingpanellayoutheight());
    }
    @Override
    public void onPanelExpanded(View panel) {
        is_collapse = true;
        rlnearby_img.setVisibility(View.GONE);
    }

    @Override
    public void onPanelAnchored(View panel) {
    }

    //endregion
    //region Google Map
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        Log.e("setupmapifneeded","arrived");
        //if (ActivitySplashScreen.download_flag) {
        if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
            if (googleMap == null) {
                // Try to obtain the map from the SupportMapFragment.

                googleMap = mMapFragment.getMap();
                // Check if we were successful in obtaining the map.
                if (googleMap != null) {
                    googleMap.setMyLocationEnabled(true);
                    startMap();
                }
            }
        }
    }

    private void startMap() {
        googleMap.clear();
        GPSTracker tracker = new GPSTracker(_mcontext);
        if (!Utils.hasInternetConnection(_mcontext)) {
            mSlidingUpPanelLayout.setPanelHeight(slidingpanellayoutheight());
            Utils.showInternetRequiredDialog(_mcontext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
        if (tracker.canGetLocation()) {
            loc = tracker.getLocation();
        } else {
            tracker.showSettingsAlert();
            return;
        }
        if (loc != null) {
            userPosition = new LatLng(loc.getLatitude(), loc.getLongitude());
            getNearestClinicInfo();
            //  LatLngBounds bounds = this.googleMap.getProjection().getVisibleRegion().latLngBounds;
            for (ClinicInfo marker_clinic : find_dist) {
                LatLng markerPoint = new LatLng
                        (Double.valueOf(marker_clinic.getClinic_location().get_lat()),
                                Double.valueOf(marker_clinic.getClinic_location().get_lng()));
                MarkerOptions marker = new MarkerOptions().position(markerPoint)
                        .title(marker_clinic.get_name());
                if (marker_clinic.getClinicType().equals("GP")) {
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.clinicgp_35));
                } else if (marker_clinic.getClinicType().equals("SP")) {
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.clinicsp_35));
                } else if (marker_clinic.getClinicType().equals("DT")) {
                    marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.clinicdt_35));
                }
                // if(bounds.contains(markerPoint)){
                googleMap.addMarker(marker);
                //  }
            }
        } else {
            return;
        }
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(userPosition) // Sets the center of the map to
                .zoom(15) // Sets the zoom
               /* .tilt(30)*/// Sets the tilt of the camera to 30 degrees
                .build(); // Creates a CameraPosition from the builder
        googleMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(cameraPosition));
        txt_clinic_value.setText(find_dist.size() + "");
        LoadViewPager();
        LoadSearchClinic(find_dist);
        markerOnClinckEvent();
    }

    @Override
    public void onLocationChanged(Location location) {
    }

    @Override
    public void onConnected(Bundle bundle) {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setNumUpdates(1);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    public void markerOnClinckEvent() {
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                setMarker(marker);
                return true;
            }
        });
    }

    public void setMarker(Marker marker) {
        int position = 0;
        for (ClinicInfo check : find_dist) {
            if (check.get_name().equals(marker.getTitle())) {
                position = find_dist.indexOf(check);
                mCardShadowTransformer.onPageSelected(position);
                if (is_collapse) {
                    mViewPager.setVisibility(View.GONE);
                    rlnearby_img.setVisibility(View.GONE);
                } else {
                    mViewPager.setVisibility(View.VISIBLE);
                    rlnearby_img.setVisibility(View.VISIBLE);
                    if (slidingpanellayoutheight() < 200) {
                        //mSlidingUpPanelLayout.setPanelHeight(slidingpanellayoutheight()+300);
                        mSlidingUpPanelLayout.setPanelHeight(slidingpanellayoutheight() + 200);
                    } else {
                        mSlidingUpPanelLayout.setPanelHeight(slidingpanellayoutheight() * 2);
                    }
                }
                break;
            }
        }
        CameraUpdate center =
                CameraUpdateFactory.newLatLng(marker.getPosition());
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(20);
        marker.getTitle();
        marker.showInfoWindow();
        if (previousmarker != null) {
            if (previous_clinictype != null) {
                if (previous_clinictype.equals("GP")) {
                    previousmarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.clinicgp_35));
                } else if (previous_clinictype.equals("SP")) {
                    previousmarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.clinicsp_35));
                } else if (previous_clinictype.equals("DT")) {
                    previousmarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.clinicdt_35));
                }
            }
        }
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.cliniccurrent_35_sec));
        previousmarker = marker;
        previous_clinictype = find_dist.get(position).getClinicType();
        googleMap.moveCamera(center);
        googleMap.animateCamera(zoom);
        googleMap.getUiSettings().setMapToolbarEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
    }

    public void getNearestClinicInfo() {
        find_dist = new ArrayList<ClinicInfo>();
        List<ClinicInfo> all_clinic = dbClinicInfo.getAllClinicInfosbyNearest(loc);
        double preNearbyclinic_lat = loc.getLatitude();
        double preNearbyclinic_lng = loc.getLongitude();
        for (int i = 0; i < all_clinic.size(); i++) {
            Double latitude = Double.parseDouble(all_clinic.get(i).getClinic_location().get_lat());
            Double longitude = Double.parseDouble(all_clinic.get(i).getClinic_location().get_lng());
            Location compare_loc = new Location("point");
            compare_loc.setLatitude(latitude);
            compare_loc.setLongitude(longitude);
            double dist = distance(preNearbyclinic_lat, preNearbyclinic_lng, latitude, longitude, "K");
            if (dist < 0.25) {
                find_dist.add(all_clinic.get(i));
            }
        }
        String first_lat = "";
        String first_lng = "";
        int samelatlong_count = 0;
        double COORDINATE_OFFSET = 0.00007;
        for (int k = 0; k < find_dist.size(); k++) {
            String temp_lat = find_dist.get(k).getClinic_location().get_lat();
            String temp_lng = find_dist.get(k).getClinic_location().get_lng();
            if (first_lat.equals(temp_lat) && first_lng.equals(temp_lng)) {
                samelatlong_count += 1;
                int module = samelatlong_count % 4;
                double orders = (Math.abs(samelatlong_count / 4)) / 2.0 + 0.5;

                switch (module) {
                    case 0:
                        double lat = Double.parseDouble(temp_lat) + (orders * COORDINATE_OFFSET);
                        find_dist.get(k).getClinic_location().set_lat(lat + "");
                        break;
                    case 1:
                        double lng = Double.parseDouble(temp_lng) + (orders * COORDINATE_OFFSET);
                        find_dist.get(k).getClinic_location().set_lng(lng + "");
                        break;
                    case 2:
                        double lats = Double.parseDouble(temp_lat) + (orders * -COORDINATE_OFFSET);
                        find_dist.get(k).getClinic_location().set_lat(lats + "");
                        break;
                    case 3:
                        double lngs = Double.parseDouble(temp_lng) + (orders * -COORDINATE_OFFSET);
                        find_dist.get(k).getClinic_location().set_lng(lngs + "");
                        break;
                    default:
                        break;
                }
            } else {
                samelatlong_count = 0;
            }
            first_lat = temp_lat;
            first_lng = temp_lng;
        }
    }

    private static double distance(double lat1, double lon1, double lat2, double lon2, String unit) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        if (unit == "K") {
            dist = dist * 1.609344;
        } else if (unit == "N") {
            dist = dist * 0.8684;
        }
        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    //endregion
    //region Search Area
    public void setEvent() {
        rlclinic_type.setOnClickListener(ByClinicTypeOnClickListener);
        rlspecilty.setOnClickListener(BySpecialtyOnClinicListener);
        rlclinic_service.setOnClickListener(ByServiceOnClinicListener);
        txtclinictype_value.setOnClickListener(ByClinicTypeOnClickListener);
        txtspecialty_value.setOnClickListener(BySpecialtyOnClinicListener);
        txtservices_value.setOnClickListener(ByServiceOnClinicListener);
        imgbtn_cross.setOnClickListener(ImageCrossOnClinicListener);

        editsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if (i == EditorInfo.IME_ACTION_DONE) {
                    SearchByclinic();
                    //DetailClinic();
                    //txtclinictype_value.setText("All");
                    //txtspecialty_value.setText("All");
                    //txtservices_value.setText("All");
                }
                return false;
            }
        });
        editsearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSlidingUpPanelLayout.expandPane();
                editsearch.requestFocus();
                //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

                return false;
            }
        });
     /*   editsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSlidingUpPanelLayout.expandPane();
            }
        });*/

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int clinicId = Integer.parseInt(((TextView) view.findViewById(R.id.textViewClinicId)).getText().toString());
                Intent intent = new Intent(getActivity(), ActivitySearchClinicDetailed.class);
                intent.putExtra("CID", clinicId);
                intent.putExtra("Caller", getString(R.string.Activityclinicdetailed));
                getActivity().startActivity(intent);
            }
        });
        mViewPager.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = mViewPager.getCurrentItem();
                int clinicId = find_dist.get(position).get_id();

                Intent intent = new Intent(getActivity(), ActivitySearchClinicDetailed.class);
                intent.putExtra("CID", clinicId);
                intent.putExtra("Caller", getString(R.string.Activityclinicdetailed));
                getActivity().startActivity(intent);

            }
        });

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ClinicInfo marker_clinic = find_dist.get(position);
                LatLng ll = new LatLng(Double.valueOf(marker_clinic.getClinic_location().get_lat()),
                        Double.valueOf(marker_clinic.getClinic_location().get_lng()));
                Marker marker;
                MarkerOptions options = new MarkerOptions()
                        .title(marker_clinic.get_name())
                        .position(ll);
                marker = googleMap.addMarker(options);
                setMarker(marker);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        imgbtn_maprefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (googleMap != null) {
                    googleMap.setMyLocationEnabled(true);
                    previous_clinictype = null;
                    previousmarker = null;
                    startMap();
                }
            }
        });
    }

   /* private void DetailClinic() {

        String searchString = editsearch.getText().toString();
        if (searchString.matches("^\\d{6}$")) {///postal
            //search_list = dbClinicInfo.getClinicInfoBytype_Services(services, search_type, _specialty, searchString, false, false, false);
            search_list = dbClinicInfo.getClinicInfos(false, false, false, Integer.parseInt(searchString));

        } else {// if (!searchString.equals("")) { // town
            //search_list = dbClinicInfo.getClinicInfoBytype_Services(services, search_type, _specialty, searchString, false, false, false);
            search_list = dbClinicInfo.getClinicInfos(false, false, false, searchString);
        }
        txtclinictype_value.setText("All");
        txtspecialty_value.setText("All");
        txtservices_value.setText("All");
        LoadSearchClinic(search_list);
    }*/

    View.OnClickListener ByClinicTypeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            searchclinicDialog();
        }
    };
    View.OnClickListener BySpecialtyOnClinicListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            searchspecialtyDialog();
        }
    };

    View.OnClickListener ByServiceOnClinicListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            searchservicesDialog();
        }
    };

    View.OnClickListener ImageCrossOnClinicListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            editsearch.setText("");
            txtclinictype_value.setText("All");
            txtspecialty_value.setText("All");
            txtservices_value.setText("All");
            mSlidingUpPanelLayout.collapsePane();
            mSlidingUpPanelLayout.setPanelHeight(slidingpanellayoutheight());
        }
    };

    public void LoadViewPager() {
        mCardAdapter = new CardPagerAdapter(find_dist, getActivity(),loc);
        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mCardShadowTransformer.enableScaling(true);
        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);

       // mViewPager.setOffscreenPageLimit(3);
    }

    public void LoadSearchClinic(List<ClinicInfo> load_list) {

        if (load_list.size() > 0) {
            txtViewEmptyLabel.setVisibility(View.GONE);
        } else {
            txtViewEmptyLabel.setVisibility(View.VISIBLE);
        }
        clinicInfoAdapter = new ClinicInfoAdapter(getActivity(), load_list, getString(R.string.ActivitySearchClinic));
        txttotalclinic.setText(load_list.size() + " clinic found(s)");
        Load24_chas_count(load_list);
    }

    public void Load24_chas_count(List<ClinicInfo> infolist) {
        int _24hr_count = 0, _chas_count = 0;
        for (ClinicInfo agr0 : infolist) {
            if (agr0.is_is24Hours()) {
                _24hr_count++;
            }
            if (agr0.is_isChas()) {
                _chas_count++;
            }
        }
        txt_clinic_chas_value.setText(_chas_count + "");
        txt_clinic_24hr_value.setText(_24hr_count + "");
    }

    public void setTounchListener() {
        mListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
               // if (ActivitySplashScreen.download_flag) {
                if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                    int action = motionEvent.getAction();
                    switch (action) {
                        case MotionEvent.ACTION_DOWN:
                            // Disallow ScrollView to intercept touch events.
                            view.getParent().requestDisallowInterceptTouchEvent(true);
                            break;
                        case MotionEvent.ACTION_UP:
                            // Allow ScrollView to intercept touch events.
                            view.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    // Handle ListView touch events.
                    view.onTouchEvent(motionEvent);
                }
                return true;
            }
        });
    }

    public void SearchByclinic() {
        String searchString = editsearch.getText().toString();
        if (searchString.matches("^\\d{6}$")) {///postal
            //search_list = dbClinicInfo.getClinicInfoBytype_Services(services, search_type, _specialty, searchString, false, false, false);
            search_list = dbClinicInfo.getClinicInfos(false, false, false, Integer.parseInt(searchString));

        } else {// if (!searchString.equals("")) { // town
            //search_list = dbClinicInfo.getClinicInfoBytype_Services(services, search_type, _specialty, searchString, false, false, false);
            search_list = dbClinicInfo.getClinicInfos(false, false, false, searchString);
        }
        txtclinictype_value.setText("All");
        txtspecialty_value.setText("All");
        txtservices_value.setText("All");
        LoadSearchClinic(search_list);
    }

    //endregion
    //region clinic Dialog
    public void searchclinicDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_specialist_clinic_lists);
        txt_dialog_title = (TextView) dialog.findViewById(R.id.tool);
        txt_dialog_title.setText("Clinic Type");
        txt_dialog_title.setTextColor(Color.BLACK);
        lvclinictype = (ListView) dialog.findViewById(R.id.lvspecialist);
        imgtype_close = (ImageButton) dialog.findViewById(R.id.img_close);

        final List<String> type = Arrays.asList(getResources().getStringArray(R.array.clinictype));

        ClinicTypeAdapter adapter_clinic = new ClinicTypeAdapter(getActivity(), type);
        lvclinictype.setAdapter(adapter_clinic);
        search_str = editsearch.getText().toString();
        lvclinictype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                switch (i) {
                    case 0:
                        search_type = "";
                        txtclinictype_value.setText(type.get(i));
                        break;
                    case 1:
                        search_type = "GP";
                        txtclinictype_value.setText(type.get(i));
                        break;
                    case 2:
                        search_type = "SP";
                        txtclinictype_value.setText(type.get(i));
                        break;
                    case 3:
                        search_type = "DT";
                        txtclinictype_value.setText(type.get(i));
                        break;
                    default:
                        break;
                }

                search_list = dbClinicInfo.getNearClinicInfoByType(search_type, search_str, false, false, false);
                txtspecialty_value.setText("All");
                txtservices_value.setText("All");
                LoadSearchClinic(search_list);

                dialog.dismiss();
            }
        });

        imgtype_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    public void searchspecialtyDialog() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_specialist_clinic_lists);
        txt_dialog_title = (TextView) dialog.findViewById(R.id.tool);
        txt_dialog_title.setText(getString(R.string.title_activity_activity_specialist_clinic_lists));
        txt_dialog_title.setTextColor(Color.BLACK);
        lvspecialist = (ListView) dialog.findViewById(R.id.lvspecialist);
        imgspecial_close = (ImageButton) dialog.findViewById(R.id.img_close);
        final String search_str = editsearch.getText().toString();
        list_clinicspec = new ArrayList<Services>();
        Services obj = new Services();
        obj.setSpeciality("All");
        list_clinicspec = db_services.getspeciltybyclinictype(search_type);
        list_clinicspec.add(0, obj);
        LoadAdapter();
        lvspecialist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                _specialty = list_clinicspec.get(position).getSpeciality();
                txtspecialty_value.setText(_specialty);
                search_list = dbClinicInfo.getNearClinicInfoByType(search_type, _specialty, search_str, false, false, false);

                LoadSearchClinic(search_list);
                txtservices_value.setText("All");
                dialog.dismiss();

            }
        });
        imgspecial_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void LoadAdapter() {
        adpter_speciallist = new SpecialistClinicAdapter(getActivity(), list_clinicspec);
        lvspecialist.setAdapter(adpter_speciallist);
    }

    public void searchservicesDialog() {
        final Dialog dialog = new Dialog(_mcontext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_specialist_clinic_lists);
        lvspecialist = (ListView) dialog.findViewById(R.id.lvspecialist);
        imgservice_close = (ImageButton) dialog.findViewById(R.id.img_close);
        txt_dialog_title = (TextView) dialog.findViewById(R.id.tool);
        txt_dialog_title.setText("Services");
        txt_dialog_title.setTextColor(Color.BLACK);

        final ArrayList<Services_Items> service_list = db_services_item.getallservicesname();
        list_servicesitems = service_list;
        Services_Items obj = new Services_Items();
        obj.setServices_name("All");
        list_servicesitems.add(0, obj);
        LoadServiceNameAdapter();

        final String search_str = editsearch.getText().toString();

        lvspecialist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dialog.dismiss();
                services = list_servicesitems.get(position).getServices_name();
                txtservices_value.setText(services);
                _specialty = txtspecialty_value.getText().toString();

                search_list = dbClinicInfo.getClinicInfoBytype_Services(services, search_type, _specialty, search_str, false, false, false);
                LoadSearchClinic(search_list);
            }
        });
        imgservice_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void LoadServiceNameAdapter() {
        adapter_itmes = new Services_ItemsAdapter(_mcontext, list_servicesitems);
        lvspecialist.setAdapter(adapter_itmes);
    }
    //endregion


    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(r);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rlgp:
                break;

            case R.id.rlsp:
                break;

            case R.id.rldt:
                break;
        }
    }

   /* @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.rlgp:
                search_type_click = "GP";
                break;

            case R.id.rlsp:
                search_type_click = "SP";
                break;

            case R.id.rldt:
                search_type_click = "DT";
                break;

            *//*case R.id.txt_selected_clinic:
                search_type_click = "SC";
                break;*//*
        }
        search_list_click = dbClinicInfo.getNearClinicInfoByType(search_type, search_str, false, false, false);
        LoadSearchClinicClick(search_list_click);
    }*/

    /*private void LoadSearchClinicClick(List<ClinicInfo> search_list_click) {
        MyCustomCliniAdapter adapter = new MyCustomCliniAdapter(getActivity(), search_list_click, getString(R.string.ActivitySearchClinic));
    }*/
}
