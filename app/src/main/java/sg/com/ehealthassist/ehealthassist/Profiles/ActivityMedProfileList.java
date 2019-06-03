package sg.com.ehealthassist.ehealthassist.Profiles;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thazin.swipelibrary.SwipeMenu;
import com.thazin.swipelibrary.SwipeMenuCreator;
import com.thazin.swipelibrary.SwipeMenuListView;

import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyGetallMemberListingProfile;
import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.ActivitySplashScreen;
import sg.com.ehealthassist.ehealthassist.Clinic.ActivityPreferredClinic;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Queue_Appt.ActivityQueue_Appoint;
import sg.com.ehealthassist.ehealthassist.R;
import sg.com.ehealthassist.ehealthassist.ActivitySetting;

public class ActivityMedProfileList extends AppCompatActivity {

    ImageButton btncreate;
    public static SwipeMenuListView lvlstmed_profile;
    public static Context _mcontext;
    public static TextView txtchkprofile;
    TextView main_toolbar_title,txtdownloadaccount;
    ImageView bottomTabHome, bottomTabProfile, bottomTabClinic, bottomTabAppointment, bottomTabAbout;
    private Handler mHandler = new Handler();
    public boolean doubleBackToExitPressedOnce,accountverification;
    public static List<MedicalProfile> med_profile = new ArrayList<MedicalProfile>();
    public static DBMedProfile medRQL;
    DBClinicInfo db;
    public static MedicalProfileAdapter medical_adapter;
    public SharedPreferences pref = null;
    String usertoken,mobile,isocode,nationality;int countrycode;
    String array_relation[];
    ImageButton toolbar_imgbutton_back;
    RelativeLayout loadingPanel;
    final Handler handler = new Handler();
    public static String login_member_id="";
    boolean backup_drive_detect = false;
    SharedPreferences pref_constant = null;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_med_profile_list);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_activity_med_profile_list));
        init();
        findViewById();
        setEvent();
        SwipeMenuCreator();
        MenuTouchEvent();

        callvolleyallMemberListProfile();

        if(accountverification){
            btncreate.setVisibility(View.VISIBLE);
        }else{
            btncreate.setVisibility(View.GONE);
        }
        handler.postDelayed(r, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
      /*  if (accountverification) {
            if (backup_drive_detect) {
                if (pref.getBoolean(getString(R.string.pref_gdaa_connect_sync), false)) {
                    med_profile = medRQL.getMedProfilebyMemberid(login_member_id);
                    if (med_profile.size() > 0 && med_profile.size() == 1) {
                        if (med_profile.get(0).getNric().equals("") && med_profile.get(0).getNric_name().equals("")) {
                            Log.e("med profilelist", "arrived");
                            GDAAConnect.connect();
                            GDAAConnect.ReadContentFile("profilelist", this);
                        }
                    }
                }
            }
        }*/
    }

    @Override
    protected void onResume() {
        super.onResume();
       /* if (accountverification) {
            if (backup_drive_detect) {
                if (pref.getBoolean(getString(R.string.pref_gdaa_connect_sync), false)) {
                    med_profile = medRQL.getMedProfilebyMemberid(login_member_id);
                    if (med_profile.size() > 0 && med_profile.size() == 1) {
                        if (med_profile.get(0).getNric().equals("") && med_profile.get(0).getNric_name().equals("")) {
                            Log.e("med profilelist", "arrived");
                            GDAAConnect.connect();
                            GDAAConnect.ReadContentFile("profilelist", this);
                        }
                    }
                }
            }
        }*/
    }

    //region init()
    public void init() {
        _mcontext = this;
        medRQL = new DBMedProfile(this);
        pref = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        pref_constant = getSharedPreferences(getString(R.string.preference_constant), MODE_PRIVATE);
        login_member_id = pref.getString(getString(R.string.pref_login_memberId), "");
        usertoken = pref.getString(getString(R.string.pref_login_Access_token), "");
        mobile = pref.getString(getString(R.string.pref_main_user_data_hp),"");
        isocode = pref.getString(getString(R.string.pref_main_user_data_hp_iso),"");
        countrycode = pref.getInt(getString(R.string.pref_main_user_data_hp_code),0);
        nationality = pref.getString(getString(R.string.pref_main_user_data_hp_nationality),"");
        backup_drive_detect = pref.getBoolean(getString(R.string.pref_backup_drive_checked),true);
        accountverification = pref.getBoolean(getString(R.string.pref_is_account_verified), false);
        array_relation = getResources().getStringArray(R.array.array_relation);
        db = new DBClinicInfo(_mcontext);
        pDialog = new ProgressDialog(_mcontext);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
    }

    //endregion
    //region findviewbyid()
    public void findViewById() {
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        lvlstmed_profile = (SwipeMenuListView) findViewById(R.id.med_profile_lst);
        txtchkprofile = (TextView) findViewById(R.id.txtcheckprofile);
        txtdownloadaccount = (TextView)findViewById(R.id.txtdownloadaccount);
        btncreate = (ImageButton) findViewById(R.id.btncreate);
        bottomTabHome = (ImageView) findViewById(R.id.iconHome);
        bottomTabProfile = (ImageView) findViewById(R.id.iconProfile);
        bottomTabClinic = (ImageView) findViewById(R.id.iconClinic);
        bottomTabAppointment = (ImageView) findViewById(R.id.iconAppt);
        bottomTabAbout = (ImageView) findViewById(R.id.iconAbout);
        toolbar_imgbutton_back.setVisibility(View.GONE);
        loadingPanel = (RelativeLayout) findViewById(R.id.med_profile_loadingPanel);
    }

    //endregion
    //region setEvent()
    public void setEvent() {
        bottomTabHome.setOnClickListener(bottomTabOnClickListener);
        bottomTabProfile.setOnClickListener(bottomTabOnClickListener);
        bottomTabClinic.setOnClickListener(bottomTabOnClickListener);
        bottomTabAppointment.setOnClickListener(bottomTabOnClickListener);
        bottomTabAbout.setOnClickListener(bottomTabOnClickListener);
        btncreate.setOnClickListener(bottomTabOnClickListener);
        lvlstmed_profile.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // if (ActivitySplashScreen.download_flag) {
                if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                    MedicalProfile med_object = med_profile.get(position);
                    changepreference(med_object);
                    Intent intent = new Intent(getApplicationContext(), ActivityMedicalProfile.class);
                    intent.putExtra("from", "list");
                    intent.putExtra("clinic_id", med_object.getClinic_id());
                    intent.putExtra("member", med_object.getMember());
                    intent.putExtra("id", med_object.getId());
                    startActivity(intent);
                    finish();
                }
            }
        });
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityQueue_Appoint.close();
                finish();
            }
        });
    }

    final Runnable r = new Runnable() {
        public void run() {
            if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                loadingPanel.setVisibility(View.GONE);
                handler.removeCallbacks(r);

            } else {
                loadingPanel.setVisibility(View.VISIBLE);
                txtdownloadaccount.setText(ActivitySplashScreen.download_count);
            }
            handler.postDelayed(this, 1000);
        }
    };

    //region BottomTabOnClickListener()
    View.OnClickListener bottomTabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            String caller = v.getResources().getResourceEntryName(v.getId());
            switch (caller) {
                case "iconHome":
                    intent = new Intent(getApplicationContext(), ActivityHome1.class);
                    break;
                case "iconAbout":
                    intent = new Intent(getApplicationContext(), ActivitySetting.class);
                    break;
                case "iconClinic":
                    intent = new Intent(getApplicationContext(), ActivityPreferredClinic.class);
                    break;
                case "iconAppt":
                    intent = new Intent(getApplicationContext(), ActivityQueue_Appoint.class);
                    intent.putExtra("from", "ActivityMedicalProfileList");
                    break;
                case "btncreate":
                    if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                        clearpreference();
                        if (med_profile.size() < 10) {
                            intent = new Intent(getApplicationContext(), ActivityMedicalProfile.class);
                            intent.putExtra("from", "create");
                            intent.putExtra("clinic_id", 0);
                            intent.putExtra("member",0);
                            intent.putExtra("id",0);
                        } else {
                            Utils.showAlertDialog(_mcontext, "Medical Profile", getString(R.string.response_10patients));
                        }
                    }
                    break;
            }
            if (intent != null) {
                startActivity(intent);
                if (!caller.equals("iconProfile")) {
                    finish();
                }
            }
        }
    };

    //endregion
    //endregion
    //region swipemenucreator()
    public void SwipeMenuCreator() {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                /*SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.ic_delete_black_24dp);
                menu.addMenuItem(deleteItem);*/
            }
        };
        lvlstmed_profile.setMenuCreator(creator);
    }

    public void MenuTouchEvent() {
        lvlstmed_profile.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                MedicalProfile map = med_profile.get(position);
                switch (index) {
                    case 0:
                        //if(map.getMember() < 0){
                        // delete(map);
                        // med_profile.remove(position);
                        //}
                        //medical_adapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });
        // set SwipeListener
        lvlstmed_profile.setOnSwipeListener(new SwipeMenuListView.OnSwipeListener() {
            @Override
            public void onSwipeStart(int position) {// swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    //endregion
    //region loadData()
    public void delete(MedicalProfile _med) {
        medRQL.deleteMedProfile(_med.getId());
    }


  /*  public RequestJsonMedicalProfileUpload prepareJsonProfile(MedicalProfile obj_flag) {
        RequestJsonMedicalProfileUpload new_profile = new RequestJsonMedicalProfileUpload();
        new_profile.setClinicId(obj_flag.getClinic_id());
        new_profile.setNric(obj_flag.getNric());
        new_profile.setNricType(obj_flag.getNric_type());
        new_profile.setFullName(obj_flag.getNric_name());
        new_profile.setSex(obj_flag.getGender());
        new_profile.setNationality(obj_flag.getNationality());
        new_profile.setDateOfBirth(obj_flag.getDob());
        new_profile.createAddress(
                obj_flag.getBlock_no(), obj_flag.getStreet(), obj_flag.getBuilding_name(), obj_flag.getUnit_no(), obj_flag.getPostal_code());
        new_profile.createContact(obj_flag.getTel1(), obj_flag.getTel2(), obj_flag.getEmail());

        new_profile.setAllergy(obj_flag.getAllergy());
        //region medical profile object
        if (obj_flag.getRelation() == -1) {
            new_profile.setRelationship(getResources().getString(R.string.mp_myself));
        } else {
            if (obj_flag.getRelation() > -1) {
                new_profile.setRelationship(array_relation[obj_flag.getRelation() - 1]);
            }
        }
        return new_profile;
    }*/

    public static void LoadData() {
        med_profile = medRQL.getMedProfilebyMemberid(login_member_id);
        Log.e("medprofile size",med_profile.size() + "");
        if (med_profile.size() > 0) {
            medical_adapter = new MedicalProfileAdapter(_mcontext, med_profile);
            medical_adapter.notifyDataSetChanged();
            lvlstmed_profile.setAdapter(medical_adapter);
            txtchkprofile.setVisibility(View.GONE);
        } else {
            txtchkprofile.setVisibility(View.VISIBLE);
        }
    }

    //endregion
    //region Edit preference
    public void clearpreference() {
        pref.edit().putString(getString(R.string.pref_mp_nric), "")
                .putInt(getString(R.string.pref_mp_integer_nric_type), -1)
                .putString(getString(R.string.pref_mp_nric_type), "")
                .putString(getString(R.string.pref_mp_name), "")
                .putInt(getString(R.string.pref_mp_sex), -1)
                .putString(getString(R.string.pref_mp_relation), "")
                .putInt(getString(R.string.pref_mp_integer_relation), -1)
                .putString(getString(R.string.pref_mp_nationality_name), "")
                .putString(getString(R.string.pref_mp_dob), "")
                .putString(getString(R.string.pref_mp_block_no), "")
                .putString(getString(R.string.pref_mp_street), "")
                .putString(getString(R.string.pref_mp_building), "")
                .putString(getString(R.string.pref_mp_unit_no), "")
                .putString(getString(R.string.pref_mp_postal_code), "")
                .putString(getString(R.string.pref_mp_mobile), "")
                .putInt(getString(R.string.pref_mp_mobile_code),0)
                .putString(getString(R.string.pref_mp_mobile_iso),"")
                .putString(getString(R.string.pref_mp_tel), "")
                .putString(getString(R.string.pref_mp_email), "")
                .putString(getString(R.string.pref_mp_allergy), "")
                .putInt(getString(R.string.pref_mp_profile_id), 0)
                .putInt(getString(R.string.pref_mp_member), 0)
                .putInt(getString(R.string.pref_mp_uploadflag), 0)
                .putString(getString(R.string.pref_mp_marriedstatus), "")
                .putInt(getString(R.string.pref_mp_integer_married_status), -1)
                .putInt(getString(R.string.pref_mp_language), -1)
                .putString(getString(R.string.pref_mp_address1),"")
                .putString(getString(R.string.pref_mp_address2),"")
                .putString(getString(R.string.pref_mp_address3),"")
                .putString(getString(R.string.pref_mp_address4),"")
                .apply();

    }

    public void changepreference(MedicalProfile profile) {
        pref.edit().putString(getString(R.string.pref_mp_nric), profile.getNric())
                .putString(getString(R.string.pref_mp_name), profile.getNric_name())
                .putString(getString(R.string.pref_mp_nationality_name), profile.getNationality())
                .putString(getString(R.string.pref_mp_dob), profile.getDob())
                .putInt(getString(R.string.pref_mp_integer_nric_type), profile.getNric_type())
                .putInt(getString(R.string.pref_mp_sex), profile.getGender())
                .putString(getString(R.string.pref_mp_block_no), profile.getBlock_no())
                .putString(getString(R.string.pref_mp_street), profile.getStreet())
                .putString(getString(R.string.pref_mp_building), profile.getBuilding_name())
                .putString(getString(R.string.pref_mp_unit_no), profile.getUnit_no())
                .putString(getString(R.string.pref_mp_postal_code), profile.getPostal_code())
                .putString(getString(R.string.pref_mp_mobile), profile.getTel1())
                .putString(getString(R.string.pref_mp_tel), profile.getTel2())
                .putString(getString(R.string.pref_mp_email), profile.getEmail())
                .putString(getString(R.string.pref_mp_allergy), profile.getAllergy())
                .putInt(getString(R.string.pref_mp_integer_relation), profile.getRelation())
                .putInt(getString(R.string.pref_mp_profile_id), profile.getId())
                .putInt(getString(R.string.pref_mp_member), profile.getMember())
                .putInt(getString(R.string.pref_mp_uploadflag), profile.getFlag_upload())
                .putInt(getString(R.string.pref_mp_integer_married_status), profile.getMarried_staus())
                .putInt(getString(R.string.pref_mp_language), profile.getLanguage())
                .putString(getString(R.string.pref_mp_address1),profile.getAddress1())
                .putString(getString(R.string.pref_mp_address2),profile.getAddress2())
                .putString(getString(R.string.pref_mp_address3),profile.getAddress3())
                .putString(getString(R.string.pref_mp_address4),profile.getAddress4())
                .putInt(getString(R.string.pref_mp_mobile_code),profile.getTel1_code())
                .putString(getString(R.string.pref_mp_mobile_iso),profile.getTel1_iso())
                .apply();
    }

    //endregion
    // region Exit from App()
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);

        }
        handler.removeCallbacks(r);
    }

    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            ActivityQueue_Appoint.close();
            db.deletetestclinic();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(mRunnable, 2000);
    }
    //endregion

    public void callvolleyallMemberListProfile(){
        if(Utils.hasInternetConnection(_mcontext)){
            if(accountverification){
                txtchkprofile.setVisibility(View.GONE);
                showpDialog();
                VolleyGetallMemberListingProfile _vmemberlist = new VolleyGetallMemberListingProfile(_mcontext,login_member_id,mobile,isocode,countrycode,nationality);
                _vmemberlist.GetallMemberprofileRequest(usertoken, new VolleyGetallMemberListingProfile.VolleyCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Log.e("profile download",message);
                        ArrayList<MedicalProfile> list_profile= medRQL.getMedProfilebyMemberid(login_member_id);

                       // GDAAConnect.uploadprofileToDrive(list_profile);
                        LoadData();
                        hidepDialog();
                    }

                    @Override
                    public void onFail(String errorcode, String errormessage) {
                        Log.e(errorcode,errormessage);
                        LoadData();
                        hidepDialog();
                    }
                });
            }
        }else{
            LoadData();
        }
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}

