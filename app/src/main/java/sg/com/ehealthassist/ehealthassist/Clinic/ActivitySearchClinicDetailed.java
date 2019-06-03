package sg.com.ehealthassist.ehealthassist.Clinic;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableWeightLayout;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyClinicInfo;
import sg.com.ehealthassist.ehealthassist.Account.ActivityLogIn;
import sg.com.ehealthassist.ehealthassist.Appointment.ActivityApptFlow;
import sg.com.ehealthassist.ehealthassist.CustomUI.CustomListView;
import sg.com.ehealthassist.ehealthassist.CustomUI.PagerCircleDotAdapter;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBClinic_Services;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Doctors.ActivityDoctor_On_Duty;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.Clinic_Services;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.OpeningHours;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Queue.ActivityQueueflow;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivitySearchClinicDetailed extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    public static TextView txtNoOfPplInQueue;
   // private ExpandableWeightLayout mExpandLayout;
    private OpeningHourAdapter adapter_openinighr;
    private OpenHourListviewAdapter adapter_openhrlistview;
    private Clinic_ServicesAdapter adapter_clinicservice;
    public static boolean detect_act = false;
    public static Context mContext;
    public static Activity mactivity = null;
    boolean cbtemagree = false, isbookmarked = false, isfavorite = false;
    public int clinicId;
    private String from_callactivity = "";
    private String lat, lng, postalCode, note = "";
    public String clinicName, usertoken;
    protected View view;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount;
    private ImageView[] dots;
    private PagerCircleDotAdapter mAdapter;
    private int[] mImageResources = {
            R.drawable.images
    };
    SharedPreferences preferences = null;
    DBClinic_Services dbclinic_service;
    ImageView icon24Hours, iconCHAS;//iconQueue,imgappointment
    ScrollView scroll_vdetail;
    RelativeLayout rladdress, rl_image, rlnote, rlcontact, rlFax, rlEmail, rlwebsite, rlopeninghr,rlservices,rlnewopenhr;//rlcurrentqueue_no,rlophrexpandable;
    View v2_remark, v3_Address, v3_Contact, v4_Fax, v5_email, v3_website,view_services;//v2_current_queue_no,view_rlnewopenhr;
    Button btn_website;
    ImageButton btnRegisterQueue, btnDrDuty, btnApptSlot,//imgbtn_refresh,
            btnRefreshQueue, buttonmyclinic, imagebtn_openclose, image_map, toolbar_imgbutton_back;
    TextView lblClinicName, lblBlock, lblStreet, lblUnit, lblPostalCode, lblBuilding,
            lblContact1, lblopenhr, txtshowalltime, textviewclinicNote,
            txt_website_label, lblContactLabel2, main_toolbar_title,
            textview24Hr, textViewClinicFax_detail,
            textViewClinicEmail, txtqueue_appt_message;//txtcurrent_queue_no,lblOperatingHours;
    CustomListView lvclinic_services,lvcustomopenhrheader;//lvopeninghr
    ArrayList<OpeningHours> list_openhr;
    ArrayList<Clinic_Services> list_clinicservices;
    DBMedProfile medRQL;
    ArrayList<MedicalProfile> med_profile = new ArrayList<MedicalProfile>();
    ClinicInfo ci = new ClinicInfo();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_search_clinic_detailed);
        initialize();
        findViewsById();
        Intent intent = getIntent();
        clinicId = intent.getIntExtra("CID", 0);
        from_callactivity = intent.getStringExtra("Caller");
        medRQL = new DBMedProfile(this);
        dbclinic_service = new DBClinic_Services(this);
        callvolleyclinicbyId();
        bindClinicInfo();
        loadClinicServices();
        setEvents();
        setUiPageViewController();
    }

    //region init()
    private void initialize() {
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        cbtemagree = preferences.getBoolean(getString(R.string.pref_term_agree), false);
        usertoken = preferences.getString(getString(R.string.pref_login_Access_token), "");
        isfavorite = preferences.getBoolean(getString(R.string.pref_favorite), false);
        mContext = this;
        mactivity = this;
        detect_act = true;
    }

    //endregion
    //region findViewById()
    private void findViewsById() {
        //region toolbar
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_activity_search_clinic_detailed));
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        //endregion
        //region TextView
        lblClinicName = (TextView) findViewById(R.id.textViewClinicDetailsClinicName);
        lblBlock = (TextView) findViewById(R.id.textViewClinicDetailsBlockNo);
        lblStreet = (TextView) findViewById(R.id.textViewClinicDetailsStreet);
        lblUnit = (TextView) findViewById(R.id.textViewClinicDetailsUnitNo);
        lblBuilding = (TextView) findViewById(R.id.textViewClinicDetailsBuildingName);
        lblPostalCode = (TextView) findViewById(R.id.textViewClinicDetailsPostalCode);
        lblContactLabel2 = (TextView) findViewById(R.id.textViewClinicDetailsContact2);
        lblContact1 = (TextView) findViewById(R.id.textViewClinicDetailsContact1);
       // lblOperatingHours = (TextView) findViewById(R.id.textViewClinicDetailsOperatingHours);
       // lblopenhr = (TextView) findViewById(R.id.textViewClinicDetailsOperatingHoursLabel);
        txtNoOfPplInQueue = (TextView) findViewById(R.id.textViewClinicDetailsPeopleInQueueResult);
        txt_website_label = (TextView) findViewById(R.id.txt_website_label);
       // txtshowalltime = (TextView) findViewById(R.id.txtshowalltime);
        textviewclinicNote = (TextView) findViewById(R.id.textviewclinicNote);
        textview24Hr = (TextView) findViewById(R.id.textview24Hr);
        textViewClinicFax_detail = (TextView) findViewById(R.id.textViewClinicFax_detail);
        textViewClinicEmail = (TextView) findViewById(R.id.textViewClinicEmail);
        txtqueue_appt_message = (TextView) findViewById(R.id.queue_appt_message);
        //endregion
        //region icon
        icon24Hours = (ImageView) findViewById(R.id.imageViewClinicDetails24Hours);
        iconCHAS = (ImageView) findViewById(R.id.imageViewClinicDetailsCHAS);
        //endregion
        //region Button
        btn_website = (Button) findViewById(R.id.btn_website);
        //endregion
        //region ImageButton
        btnRefreshQueue = (ImageButton) findViewById(R.id.imageButtonRefreshQueueStatus);
        btnRegisterQueue = (ImageButton) findViewById(R.id.buttonRegisterQueue);
        btnDrDuty = (ImageButton) findViewById(R.id.buttonDrDuty);
        buttonmyclinic = (ImageButton) findViewById(R.id.buttonmyclinic);
        btnApptSlot = (ImageButton) findViewById(R.id.buttonApptSlot);
      //  imagebtn_openclose = (ImageButton) findViewById(R.id.imagebtn_openclose);
        image_map = (ImageButton) findViewById(R.id.image_map);
        //endregion
        //region CustomListView
       // lvopeninghr = (CustomListView) findViewById(R.id.lvopeninghr);
        lvclinic_services = (CustomListView) findViewById(R.id.lvclinic_services);
        //endregion
        //region ExpandableWeightLayout
       // mExpandLayout = (ExpandableWeightLayout) findViewById(R.id.expandableLayout);
        //endregion
        //region RelativeLayout
        rlservices = (RelativeLayout) findViewById(R.id.rlservices);
       // rlopeninghr = (RelativeLayout) findViewById(R.id.rlopeninghr);
        rladdress = (RelativeLayout) findViewById(R.id.rladdress);
        rlnote = (RelativeLayout) findViewById(R.id.rlnote);
        rlcontact = (RelativeLayout) findViewById(R.id.rlcontact);
        rlFax = (RelativeLayout) findViewById(R.id.rlFax);
        rlEmail = (RelativeLayout) findViewById(R.id.rlEmail);
        rlwebsite = (RelativeLayout) findViewById(R.id.rlwebsite);
      //  rlopeninghr = (RelativeLayout) findViewById(R.id.rlopeninghr);
       // rlophrexpandable = (RelativeLayout) findViewById(R.id.rlophrexpandable);
        //   rlcurrentqueue_no = (RelativeLayout)findViewById(R.id.rlcurrentqueue_no);
        rl_image = (RelativeLayout) findViewById(R.id.rl_image);
        rl_image.setVisibility(View.GONE);
        //endregion
        //region Scrollview
        scroll_vdetail = (ScrollView) findViewById(R.id.scroll_vdetail);
        scroll_vdetail.fullScroll(View.FOCUS_UP);
        scroll_vdetail.pageScroll(View.FOCUS_UP);
        //endregion
        //region view
        v2_remark = (View) findViewById(R.id.v2_remark);
        v3_Address = (View) findViewById(R.id.v3_Address);
        v3_Contact = (View) findViewById(R.id.v3_Contact);
        v4_Fax = (View) findViewById(R.id.v4_Fax);
        v5_email = (View) findViewById(R.id.v5_email);
        v3_website = (View) findViewById(R.id.v3_website);
        view_services = (View) findViewById(R.id.view_services);
      //  view_rlnewopenhr=(View)findViewById(R.id.view_rlnewopenhr);
        // v2_current_queue_no = (View)findViewById(R.id.v2_current_queue_no);
        //endregion
        // region image pager
        intro_images = (ViewPager) findViewById(R.id.pager_introduction);
        pager_indicator = (LinearLayout) findViewById(R.id.viewPagerCountDots);
        mAdapter = new PagerCircleDotAdapter(this, mImageResources);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        lvcustomopenhrheader = (CustomListView)findViewById(R.id.lvcustomopenhrheader);
        rlnewopenhr= (RelativeLayout)findViewById(R.id.rlnewopenhr);
        //endregion
    }

    //endregion
    //region image View Pager ()
    private void setUiPageViewController() {
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(this);
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(4, 0, 4, 0);
            pager_indicator.addView(dots[i], params);
        }
        dots[0].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(getResources().getDrawable(R.drawable.nonselecteditem_dot));
        }
        dots[position].setImageDrawable(getResources().getDrawable(R.drawable.selecteditem_dot));
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //endregion
    //region call queue watch
  /*  public void callqueuewatch(){
        Boolean account_verified = preferences.getBoolean(mContext.getString(R.string.pref_is_account_verified),false);
        if(!usertoken.equals("")){
            if(account_verified){
                if (Utils.hasInternetConnection(mContext)) {

                    VolleyQueueWatch queuewatch = new VolleyQueueWatch(mContext);
                    queuewatch.PostQueueWatchJson(clinicId, usertoken, new VolleyQueueWatch.VolleyCallback() {
                        @Override
                        public void onSuccess(int currentqueueno) {
                            if(currentqueueno>0){
                                txtcurrent_queue_no.setText(currentqueueno+"");
                                rlcurrentqueue_no.setVisibility(View.VISIBLE);
                                v2_current_queue_no.setVisibility(View.VISIBLE);
                            }
                            else{
                                rlcurrentqueue_no.setVisibility(View.GONE);
                                v2_current_queue_no.setVisibility(View.GONE);
                            }

                        }

                        @Override
                        public void onFail(String errorcode,String errormsg) {
                            rlcurrentqueue_no.setVisibility(View.GONE);
                        }
                    });
                }
                else{
                    rlcurrentqueue_no.setVisibility(View.GONE);
                }
            }

        }

    }*/
    //endregion
    //region clinic update
    public void callvolleyclinicbyId() {
        if (Utils.hasInternetConnection(mContext)) {
            VolleyClinicInfo _vinfo = new VolleyClinicInfo(mContext);
            _vinfo.GetClinicbyidJson(Constant.URL_CLINICDETAILINFO + clinicId + "/info", new VolleyClinicInfo.VolleyCallback() {
                @Override
                public void onSuccess(boolean flag) {
                }

                @Override
                public void onFail(String errorcode, String message) {
                }
            });
        }
    }

    //endregion
    //region setEvents()
    private void setEvents() {
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });
        btnRegisterQueue.setOnClickListener(btnRegisterQueueOnClickListener);
        btnDrDuty.setOnClickListener(btnDrDutoOnClickListener);
        btnApptSlot.setOnClickListener(btnApptSlotOnClickListener);
        buttonmyclinic.setOnClickListener(btnMyClinicOnClickListener);
        btn_website.setOnClickListener(btnWebsiteOnClickListener);
        lblContact1.setOnClickListener(btnContactOnClickListener);

      /*  txtshowalltime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandLayout.toggle();
                lvopeninghr.setExpanded(true);
                mExpandLayout.setVisibility(View.VISIBLE);

            }
        });
        imagebtn_openclose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mExpandLayout.toggle();
                mExpandLayout.setVisibility(View.GONE);
                lvopeninghr.setExpanded(false);
            }
        });*/

        image_map.setOnClickListener(btnLocateUsOnClickListener);
        rladdress.setOnClickListener(btnLocateUsOnClickListener);
      /*  imgbtn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callqueuewatch();
            }
        });*/

    }

    //region btnMyClinicOnClickListener()
    View.OnClickListener btnMyClinicOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            DBClinicInfo dbHandler = new DBClinicInfo(getApplicationContext());
            int rowAffected = dbHandler.toggleClinicAsFavourite(clinicId, isbookmarked);
            if (rowAffected > 0) {
                if (!isbookmarked) {
                    Toast.makeText(getApplicationContext(), "Added To My Clinic.", Toast.LENGTH_SHORT).show();
                    buttonmyclinic.setImageResource(R.drawable.ic_action_toggle_star);
                    isbookmarked = true;
                } else {
                    Toast.makeText(getApplicationContext(), "Removed From My Clinic.", Toast.LENGTH_SHORT).show();
                    buttonmyclinic.setImageResource(R.drawable.ic_star_border_black_24dp);
                    isbookmarked = false;
                }
            }
        }
    };
    //endregion
    //region btnLocateUsOnClickListener()
    View.OnClickListener btnLocateUsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                ApplicationInfo info = getPackageManager().getApplicationInfo(getString(R.string.google_maps_package_name), 0);
                if (!info.enabled) {
                    getGoogleMapsDialog();
                    return;
                }
                String url;
                if (lat.equals("") || lng.equals(""))
                    url = "geo:0,0?q=SINGAPORE+" + postalCode;
                else {
                    clinicName = clinicName.replace(" ", "+");
                    url = "geo:0,0?q=" + lat + "," + lng + "(" + clinicName + ")";
                }
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                intent.setPackage(getString(R.string.google_maps_package_name));
                startActivity(intent);
            } catch (PackageManager.NameNotFoundException e) {
                getGoogleMapsDialog();
            }
        }
    };
    //endregion
    //region btnDrDutoOnClickListener()
    View.OnClickListener btnDrDutoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (usertoken.equals("")) {
                callLogin();
            } else
                callDoctorOnDuty();
        }
    };
    //endregion
    //region btnApptSlotOnClickListener()
    View.OnClickListener btnApptSlotOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            preferences.edit()
                    .putString(getString(R.string.pref_main_user_selected_date), "")
                    .apply();

            Intent intent_flow = new Intent(mContext,ActivityApptFlow.class);
            intent_flow.putExtra("CID", Integer.valueOf(clinicId));
            intent_flow.putExtra("CName", ci.get_name());
            intent_flow.putExtra("appt_mode",ci.getAppt_mode());
            mContext.startActivity(intent_flow);
        }
    };
    //endregion

    //region btnRegisterQueueOnClickListener()
    View.OnClickListener btnRegisterQueueOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent_gotoflow = new Intent(mContext,ActivityQueueflow.class);
            intent_gotoflow.putExtra("clinicid",clinicId);
            intent_gotoflow.putExtra("hecode",ci.getHecode());
            intent_gotoflow.putExtra("clinicname",ci.get_name());
            intent_gotoflow.putExtra("from_activity","activitysearchclinicdetail");
            startActivity(intent_gotoflow);

        }
    };
    //endregion
    //region btnWebsiteOnClickListener()
    View.OnClickListener btnWebsiteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ActivityClinicWebsite.class);
            intent.putExtra("url", ci.getClinic_contact().get_website());
            startActivity(intent);
        }
    };
    //endregion
    //region btnContactOnClickListener()
    View.OnClickListener btnContactOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + lblContact1.getText().toString()));
            startActivity(callIntent);
        }
    };
    //endregion
    //endregion
    //region bindClinicInfo ()
    private void bindClinicInfo() {
        DBClinicInfo db = new DBClinicInfo(mContext);
        ci = db.getClinicInfo(clinicId);
        lat = ci.getClinic_location().get_lat();
        lng = ci.getClinic_location().get_lng();
        note = ci.getClinic_note();
        postalCode = String.valueOf(ci.getClinic_address().getPostal());
        clinicName = ci.get_name();
        lblClinicName.setText(clinicName);
        lblBlock.setText(String.valueOf(ci.getClinic_address().getBlockno()));
        lblStreet.setText(ci.getClinic_address().getStreet());
        lblUnit.setText(ci.getClinic_address().getUnitno());
        lblBuilding.setText(ci.getClinic_address().getBuilding());
        lblPostalCode.setText(postalCode);
        lblContactLabel2.setVisibility(View.GONE);
        txt_website_label.setVisibility(View.GONE);

        if (ci.getClinic_contact().get_tel1() > 0) {
            lblContact1.setText("" + ci.getClinic_contact().get_tel1());
        } else {
            lblContact1.setVisibility(View.GONE);
        }
        if (ci.getClinic_contact().get_tel2() > 0) {
            lblContactLabel2.setVisibility(View.VISIBLE);
            lblContactLabel2.setText("" + ci.getClinic_contact().get_tel2());
        } else {
            lblContactLabel2.setVisibility(View.GONE);
        }
        if (ci.getClinic_contact().get_tel1() > 0 || ci.getClinic_contact().get_tel2() > 0) {
            rlcontact.setVisibility(View.VISIBLE);
            v3_Contact.setVisibility(View.VISIBLE);
        } else {
            rlcontact.setVisibility(View.GONE);
            v3_Contact.setVisibility(View.GONE);
        }
        if (ci.getClinic_contact().get_fax() > 0) {
            rlFax.setVisibility(View.VISIBLE);
            v4_Fax.setVisibility(View.VISIBLE);
            textViewClinicFax_detail.setText(ci.getClinic_contact().get_fax() + "");
        } else {
            rlFax.setVisibility(View.GONE);
            v4_Fax.setVisibility(View.GONE);
        }
        btn_website.setVisibility(View.GONE);

        if (ci.getClinic_contact().get_website().length() > 0) {
            btn_website.setVisibility(View.VISIBLE);
            rlwebsite.setVisibility(View.VISIBLE);
            v3_website.setVisibility(View.VISIBLE);
            btn_website.setText(ci.getClinic_contact().get_website());
        } else {
            btn_website.setVisibility(View.GONE);
            rlwebsite.setVisibility(View.GONE);
            v3_website.setVisibility(View.GONE);
        }
      /*  if (ci.get_operatinghr().length() > 0 && !ci.get_operatinghr().equals("[\"\"]")) {
            txtshowalltime.setVisibility(View.VISIBLE);
            tokenizerhour(ci.get_operatinghr());
        //    loadOpenHour();
            //loadOpenHourNew();
            rlopeninghr.setVisibility(View.VISIBLE);
        } else {
            rlopeninghr.setVisibility(View.GONE);
        }*/
        if(ci.get_operatinghr().length()>0 && !ci.get_operatinghr().equals("[\"\"]")){
            tokenizerhour(ci.get_operatinghr());
            loadOpenHourNew();
            rlnewopenhr.setVisibility(View.VISIBLE);
          //  view_rlnewopenhr.setVisibility(View.VISIBLE);
            view_services.setVisibility(View.VISIBLE);
        }
        else{
            rlnewopenhr.setVisibility(View.GONE);
          //  view_rlnewopenhr.setVisibility(View.GONE);
            view_services.setVisibility(View.GONE);
        }

        if (note != "null") {
            if (!note.equals("")) {
                textviewclinicNote.setText(note);
                rlnote.setVisibility(View.VISIBLE);
                v2_remark.setVisibility(View.VISIBLE);
            } else {
                rlnote.setVisibility(View.GONE);
                v2_remark.setVisibility(View.GONE);
            }
        } else {
            rlnote.setVisibility(View.GONE);
            v2_remark.setVisibility(View.GONE);
        }
        if (ci.getClinic_contact().get_email().equals("")) {
            rlEmail.setVisibility(View.GONE);
            v5_email.setVisibility(View.GONE);
        } else {
            rlEmail.setVisibility(View.VISIBLE);
            rlEmail.setVisibility(View.VISIBLE);
        }
        if (ci.is_is24Hours()) {
            icon24Hours.setVisibility(View.VISIBLE);
            textview24Hr.setVisibility(View.VISIBLE);
        } else {
            icon24Hours.setVisibility(View.GONE);
            textview24Hr.setVisibility(View.GONE);
        }
        if (ci.is_isChas()) {
            iconCHAS.setVisibility(View.VISIBLE);
        } else {
            iconCHAS.setVisibility(View.GONE);
        }
        if (ci.is_isBookmarked()) {
            isbookmarked = true;
            buttonmyclinic.setImageResource(R.drawable.ic_action_toggle_star);
        }
        if (ci.getClinicType().equals("GP")) {
            btnDrDuty.setVisibility(View.VISIBLE);
            btnDrDuty.setEnabled(true);
        }
        else if (ci.getClinicType().equals("SP") || ci.getClinicType().equals("DT")) {
            btnDrDuty.setEnabled(false);
            btnDrDuty.setImageResource(R.drawable.dod_disable_sec_80);
        }
        if (ci.is_isQueueEnabled()) {
            btnRefreshQueue.setVisibility(View.VISIBLE);
            btnRegisterQueue.setEnabled(true);
            btnRegisterQueue.setVisibility(View.VISIBLE);
        } else {
            btnRegisterQueue.setImageResource(R.drawable.queue_disable_brighted_sec_80);
            btnRegisterQueue.setEnabled(false);
        }
        if (ci.is_isapptEnabled()) {
            btnApptSlot.setVisibility(View.VISIBLE);
            btnApptSlot.setEnabled(true);
        } else {
            btnApptSlot.setEnabled(false);
            btnApptSlot.setImageResource(R.drawable.appt_disable_sec_80);
        }
        if (!ci.is_isapptEnabled() && !ci.is_isQueueEnabled()) {
            txtqueue_appt_message.setVisibility(View.VISIBLE);
            btnRegisterQueue.setVisibility(View.GONE);
            btnApptSlot.setVisibility(View.GONE);
            btnDrDuty.setVisibility(View.GONE);
        } else {
            txtqueue_appt_message.setVisibility(View.GONE);
            btnRegisterQueue.setVisibility(View.VISIBLE);
            btnApptSlot.setVisibility(View.VISIBLE);
            btnDrDuty.setVisibility(View.VISIBLE);
        }
    }
    //endregion
    //region getallprofile()
    public boolean getallProfile() {
        boolean flag = false;
        med_profile = medRQL.getMedProfilebyMemberid(preferences.getString(getString(R.string.pref_login_memberId), ""));
        if (med_profile.size() > 0) {
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }
    //endregion
    //region GoogleMapDialog()
    private void getGoogleMapsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivitySearchClinicDetailed.this);
        builder.setMessage(R.string.msg_google_map_required);
        builder.setPositiveButton("Enable/Install", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_google_map_install)));
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //endregion
    public void callDoctorOnDuty() {
        Intent intent_scan = new Intent(mContext, ActivityDoctor_On_Duty.class);
        intent_scan.putExtra("CID", Integer.valueOf(clinicId));
        intent_scan.putExtra("CName", clinicName);
        mContext.startActivity(intent_scan);
    }
    public void callLogin() {
        Intent intent_scan = new Intent(getApplicationContext(), ActivityLogIn.class);
        intent_scan.putExtra("from", "");
        intent_scan.putExtra("CID", 0);
        startActivity(intent_scan);
    }
    //endregion
    //region Opening hr ()
    public void tokenizerhour(String time) {
        if (!time.equals("")) {
            try {
                list_openhr = new ArrayList<OpeningHours>();
                JSONArray json_open = new JSONArray(time);
                if (json_open.length() > 0) {
                    for (int i = 0; i < json_open.length(); i++) {
                        OpeningHours response_obj = getOpenhourlist(json_open.getString(i));

                        list_openhr.add(response_obj);
                    }
                }
              //  lblOperatingHours.setText(list_openhr.get(0).getOpen_day() + ":" + list_openhr.get(0).getOpen_time().get(0));
            } catch (Exception ex) {
                ex.toString();
            }
        }
    }
    public OpeningHours getOpenhourlist(String openhr) {
        OpeningHours obj_open = null;
        if (!openhr.equals("")) {
            String[] sub_day = openhr.split(":");
            String first_str = sub_day[0];
            String replace_str = openhr.replace(first_str + ":", "");
            String replace_second = replace_str.replace(" ","");
            String[] sub_time = replace_second.split(";");

            List<String> sub_list = Arrays.asList(sub_time);

            obj_open = new OpeningHours(first_str, sub_list);
        }
        return obj_open;
    }
   /* public void loadOpenHour() {
        adapter_openinighr = new OpeningHourAdapter(this, list_openhr);
        lvopeninghr.setAdapter(adapter_openinighr);

    }*/
    public void loadOpenHourNew() {
        adapter_openhrlistview = new OpenHourListviewAdapter(this, list_openhr);
        lvcustomopenhrheader.setAdapter(adapter_openhrlistview);
        lvcustomopenhrheader.setExpanded(true);

       /* adapter_openinighr = new OpeningHourAdapter(this, list_openhr);
        lvcustomopenhrheader.setAdapter(adapter_openinighr);
        lvcustomopenhrheader.setExpanded(true);*/

    }
    public void loadClinicServices() {
        list_clinicservices = new ArrayList<Clinic_Services>();
        list_clinicservices = dbclinic_service.getclinicservicesLog(clinicId);
        if (list_clinicservices.size() > 0) {
            rlservices.setVisibility(View.VISIBLE);
            adapter_clinicservice = new Clinic_ServicesAdapter(this, list_clinicservices);
            lvclinic_services.setAdapter(adapter_clinicservice);
            lvclinic_services.setExpanded(true);
        } else {
            rlservices.setVisibility(View.GONE);
        }
    }
    //endregion
    //region KeyDown & Returnback ()
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void returnback() {
        Intent intent = null;
        if (from_callactivity.equals(getString(R.string.Activityclinicdetailed))) {
            finish();
        } else if (from_callactivity.equals(getString(R.string.ActivityPreferredClinic))) {
            finish();
            intent = new Intent(getApplicationContext(), ActivityPreferredClinic.class);
            intent.putExtra("fromactivity", getString(R.string.ActivityPreferredClinic));
            startActivity(intent);
        }
    }
    //endregion

}
