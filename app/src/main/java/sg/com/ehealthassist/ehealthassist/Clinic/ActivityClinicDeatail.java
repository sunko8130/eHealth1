package sg.com.ehealthassist.ehealthassist.Clinic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestJsonMedicalProfileUpload;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyUploadMedicalProfile;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Profiles.ActivityMedicalProfile;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityClinicDeatail extends AppCompatActivity {
    TextView txtclinicid, txtclinicName;
    SharedPreferences pref = null;
    Button btnconfirm;
    private int clinicId;
    private String hecode;
    Context mContext;
    Activity activity;
    MedicalProfile medical_profile;
    TextView txtBlockNo, txtStreet, txtUnitNo, txtBuildingName,
            txtPostalCode,main_toolbar_title;
    ImageButton toolbar_imgbutton_back;
    String array_relation[];
    DBMedProfile dbmedprofile;
    ClinicInfo ci = new ClinicInfo();
    boolean backup_drive_detect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_clinic_deatail);
        main_toolbar_title = (TextView)findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_qrclinicDetail));
        pref = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        backup_drive_detect = pref.getBoolean(getString(R.string.pref_backup_drive_checked),true);
        mContext = this;
        activity = this;
        dbmedprofile = new DBMedProfile(mContext);

        Intent intent = getIntent();
        hecode = intent.getStringExtra("hecode");
        if(dbmedprofile.existmemberprofile(pref.getString(getString(R.string.pref_login_memberId),""))!= 0){
            array_relation = getResources().getStringArray(R.array.array_relation);
        }
        else{
            array_relation = getResources().getStringArray(R.array.array_myself_relation);
        }
        findViewById();
        bindClinicInfo();
        setEvent();
    }
    //region findViewById()
    public void findViewById() {
        txtclinicid = (TextView) findViewById(R.id.txtvalueclinicid);
        txtclinicName = (TextView) findViewById(R.id.txtvalueclinicname);
        btnconfirm = (Button) findViewById(R.id.btnconfirm);
        txtBlockNo = (TextView) findViewById(R.id.textViewClinicDetailsBlockNo);
        txtStreet = (TextView) findViewById(R.id.textViewClinicDetailsStreet);
        txtUnitNo = (TextView) findViewById(R.id.textViewClinicDetailsUnitNo);
        txtBuildingName = (TextView) findViewById(R.id.textViewClinicDetailsBuildingName);
        txtPostalCode = (TextView) findViewById(R.id.textViewClinicDetailsPostalCode);
        toolbar_imgbutton_back = (ImageButton)findViewById(R.id.toolbar_imgbackbutton);
    }
    //endregion

    //region setEvent()
    public void setEvent() {
        btnconfirm.setOnClickListener(btnConFirmOnClickListener);
        toolbar_imgbutton_back.setOnClickListener(imgtoolbarbackOnClickListener);
    }

    View.OnClickListener imgtoolbarbackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            finish();
        }
    };

    View.OnClickListener btnConFirmOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // ************* CALL API ************
            try {
                if (Utils.hasInternetConnection(mContext)) {
                    RequestJsonMedicalProfileUpload obj = prepareJsonProfile();
                    JSONObject json = obj.StringtoJsonObject(obj.ObjecttoJson(obj));
                    createMedicalprofile();
                    if ((pref.getBoolean(getString(R.string.pref_is_account_verified), false)) && (pref.getString(getString(R.string.pref_login_Access_token), "").length() > 0)) {
                        VolleyUploadMedicalProfile _vupload = new VolleyUploadMedicalProfile(mContext, txtclinicName.getText().toString(),medical_profile,"");
                        _vupload.PostUploaddProfileJson(pref.getString(getString(R.string.pref_login_Access_token), ""), json, new VolleyUploadMedicalProfile.VolleyCallback() {
                            @Override
                            public void onSuccess(String status) {
                                pref.edit().putBoolean(getString(R.string.pref_update_profile_flag),true).apply();
                            }
                            @Override
                            public void onFail(String errorcode, String errormsg) {
                                Utils.showAlertDialog(mContext,errorcode,errormsg,"ActivityMedicalProfileList");
                            }
                        });
                    } else {
                        ActivityMedicalProfile._mactivity.finish();
                        Utils.showAlertDialog(mContext, "Profile Submission Fail", getString(R.string.error_response_upload_profile_status_Failed), "ActivityMedicalProfileList");
                    }
                } else {
                    Utils.showInternetRequiredDialog(ActivityClinicDeatail.this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    //endregion
    //region BindClinicInfo()
    private void bindClinicInfo() {
        Clear();
        DBClinicInfo db = new DBClinicInfo(mContext);
        ci = db.getClinicInfobyHECODE(hecode);
        if (ci != null) {
            txtclinicid.setText(ci.getHecode());
            txtclinicName.setText(ci.get_name());
            clinicId = ci.get_id();
            txtBlockNo.setText(String.valueOf(ci.getClinic_address().getBlockno()));
            txtStreet.setText(ci.getClinic_address().getStreet());
            txtUnitNo.setText(ci.getClinic_address().getUnitno());
            txtBuildingName.setText(ci.getClinic_address().getBuilding());
            txtPostalCode.setText(ci.getClinic_address().getPostal());
        }
    }
    //endregion
    //region prepareProfile()
    public RequestJsonMedicalProfileUpload prepareJsonProfile() {
        RequestJsonMedicalProfileUpload new_profile = new RequestJsonMedicalProfileUpload();
        new_profile.setClinicId(clinicId);
        new_profile.setHecode(hecode);
        new_profile.setNric(pref.getString(getString(R.string.pref_mp_nric), ""));
        new_profile.setNricType(pref.getInt(getString(R.string.pref_mp_integer_nric_type), -1));
        new_profile.setFullName(pref.getString(getString(R.string.pref_mp_name), ""));
        // new_profile.setSex(pref.getInt(getString(R.string.pref_mp_sex), -1));

        String nationality = pref.getString(getString(R.string.pref_mp_nationality_name),"");

        if(!nationality.equals("")){
            String nationcode = Utils.getnationalitycode(mContext,nationality);
            new_profile.setNationality(nationcode);
        }
       // new_profile.setNationality(pref.getString(getString(R.string.pref_mp_nationality_name), ""));
        String pro_dob = pref.getString(getString(R.string.pref_mp_dob),"");
        new_profile.setDateOfBirth(pro_dob);
        new_profile.createAddress(
                pref.getString(getString(R.string.pref_mp_block_no), ""),
                pref.getString(getString(R.string.pref_mp_street), ""),
                pref.getString(getString(R.string.pref_mp_building), ""),
                pref.getString(getString(R.string.pref_mp_unit_no), ""),
                pref.getString(getString(R.string.pref_mp_postal_code), "0"),
                pref.getString(getString(R.string.pref_mp_address1),""),
                pref.getString(getString(R.string.pref_mp_address2),""),
                pref.getString(getString(R.string.pref_mp_address3),""),
                pref.getString(getString(R.string.pref_mp_address4),"")

        );
        String phoneno = pref.getInt(getString(R.string.pref_mp_mobile_code),0)+
                pref.getString(getString(R.string.pref_mp_mobile), "");
        new_profile.createContact(phoneno,
                pref.getString(getString(R.string.pref_mp_tel), "0"),
                pref.getString(getString(R.string.pref_mp_email), ""),
                pref.getString(getString(R.string.pref_mp_mobile_iso),""));

        new_profile.setAllergy(pref.getString(getString(R.string.pref_mp_allergy), ""));
        //region medical profile object
        int relation = pref.getInt(getString(R.string.pref_mp_integer_relation),-1);
        int marriedstatus = pref.getInt(getString(R.string.pref_mp_integer_married_status),-1);
        int language = pref.getInt(getString(R.string.pref_mp_language),-1);

        if(language > -1){
            new_profile.setLanguage(language);
        }
        else{
            new_profile.setLanguage(2);
        }

        if(pref.getInt(getString(R.string.pref_mp_member),-1) > 0){
            new_profile.setRelationship(getResources().getString(R.string.mp_myself));
        }else{
            if(relation > -1){
                new_profile.setRelationship(array_relation[relation-1]);
            }else{
                new_profile.setRelationship("Unknown");
            }
        }
        if(marriedstatus>-1){
            new_profile.setMarried_status(marriedstatus);
        }
        int gender = pref.getInt(getString(R.string.pref_mp_sex), -1);
        if(gender == -1){
            new_profile.setSex(1);
        }else{
            new_profile.setSex(gender);
        }

        return new_profile;
    }

    //endregion
    //region create object()
    public void createMedicalprofile(){
        medical_profile = new MedicalProfile();
        medical_profile.setId(pref.getInt(getString(R.string.pref_mp_profile_id), 0));
        medical_profile.setClinic_id(clinicId);
        medical_profile.setNric(pref.getString(getString(R.string.pref_mp_nric), ""));
        medical_profile.setNric_type(pref.getInt(getString(R.string.pref_mp_integer_nric_type), -1));
        medical_profile.setNric_name(pref.getString(getString(R.string.pref_mp_name), ""));
        medical_profile.setGender(pref.getInt(getString(R.string.pref_mp_sex), -1));
        medical_profile.setLanguage(pref.getInt(getString(R.string.pref_mp_language), -1));
        medical_profile.setNationality(pref.getString(getString(R.string.pref_mp_nationality_name), ""));
        medical_profile.setDob(pref.getString(getString(R.string.pref_mp_dob),""));
        medical_profile.setBlock_no(pref.getString(getString(R.string.pref_mp_block_no), ""));
        medical_profile.setBuilding_name(pref.getString(getString(R.string.pref_mp_building), ""));
        medical_profile.setStreet(pref.getString(getString(R.string.pref_mp_street), ""));
        medical_profile.setUnit_no(pref.getString(getString(R.string.pref_mp_unit_no), ""));
        medical_profile.setPostal_code(pref.getString(getString(R.string.pref_mp_postal_code), ""));
        medical_profile.setTel1(pref.getString(getString(R.string.pref_mp_mobile), ""));
        medical_profile.setTel1_code(pref.getInt(getString(R.string.pref_mp_mobile_code),0));
        medical_profile.setTel1_iso(pref.getString(getString(R.string.pref_mp_mobile_iso),""));
        medical_profile.setTel2(pref.getString(getString(R.string.pref_mp_tel), ""));
        medical_profile.setEmail(pref.getString(getString(R.string.pref_mp_email), ""));
        medical_profile.setAllergy(pref.getString(getString(R.string.pref_mp_allergy), ""));
        medical_profile.setMemberid(pref.getString(getString(R.string.pref_login_memberId),""));
        medical_profile.setRelation(pref.getInt(getString(R.string.pref_mp_integer_relation), -1));
        medical_profile.setMember(pref.getInt(getString(R.string.pref_mp_member),0));
        medical_profile.setLanguage(pref.getInt(getString(R.string.pref_mp_language), -1));
        medical_profile.setMarried_staus(pref.getInt(getString(R.string.pref_mp_integer_married_status),-1));
        medical_profile.setAddress1(pref.getString(getString(R.string.pref_mp_address1),""));
        medical_profile.setAddress2(pref.getString(getString(R.string.pref_mp_address2),""));
        medical_profile.setAddress3(pref.getString(getString(R.string.pref_mp_address3),""));
        medical_profile.setAddress4(pref.getString(getString(R.string.pref_mp_address4),""));

    }
    //endregion
    //region Clear()
    public void Clear() {
        txtclinicid.setText("");
        txtclinicName.setText("");
    }
    //endregion

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


}
