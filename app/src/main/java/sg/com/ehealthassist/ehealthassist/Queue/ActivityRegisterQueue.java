package sg.com.ehealthassist.ehealthassist.Queue;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestJsonMedicalProfileUpload;
import sg.com.ehealthassist.ehealthassist.API.Request.RequestJsonProfileDownload;
import sg.com.ehealthassist.ehealthassist.API.Request.RequestJsonQueueRegister;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyPatientProfileDownloadStatus;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyQueueRegister;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyUploadMedicalProfile;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Models.Profile.ResponseDownloadStatus;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Profiles.Activitydownloadprofilestatus;
import sg.com.ehealthassist.ehealthassist.Queue_Appt.ActivityQueue_Appoint;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityRegisterQueue extends AppCompatActivity {
    TextView main_toolbar_title, txtprofilename_value;
    ImageButton toolbar_imgbutton_back;
    RelativeLayout rlprofilename;
    Context _mcontext;
    String hecode = "", memberid = "", from_activity = "", clinic_name = "", usertoken = "";
    int clinicid = 0;
    SharedPreferences preference = null;
    RelativeLayout rlv_existing, rlv_pending, rlv_unkown, rlv_notexist;
    TextView txt_queue_unknown1, txt_queue_unknown2, txt_pending, txt_rl_existing, txt_notexisting,
            txtnrictype_value, txtnric_value, txtdate_value, txtage_value, txt_clinicname;
    DBMedProfile dbmedprofile;
    String array_relation[];
    Boolean account_verify;
    ResponseDownloadStatus response_object = new ResponseDownloadStatus();
    Button btn_existing_regqueue_req, btn_notexist_submitto_clinic, btn_unknow_profile_submit_clinic, btn_unkown_queue_request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register_queue);
        toolbar();
        _mcontext = this;
        preference = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        memberid = preference.getString(getString(R.string.pref_login_memberId), "");
        account_verify = preference.getBoolean(getString(R.string.pref_is_account_verified), false);
        usertoken = preference.getString(getString(R.string.pref_login_Access_token), "");
        Intent get_param = getIntent();
        clinicid = get_param.getIntExtra("clinicid", 0);
        hecode = get_param.getStringExtra("hecode");
        clinic_name = get_param.getStringExtra("clinicname");
        from_activity = get_param.getStringExtra("from_activity");
        dbmedprofile = new DBMedProfile(_mcontext);
        array_relation = getResources().getStringArray(R.array.array_myself_relation);
        findviewbyid();
        setEvent();
        loadtextview();
        if (from_activity.equals("activitysearchclinicdetail")) {
            clear();
            checklayout("");
        } else if (from_activity.equals("downloadprofilestatus")) {
            response_object = get_param.getParcelableExtra("download_status");
            LoadData();

        }
    }

    public void toolbar() {
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_activity_select_profile));
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
    }

    public void findviewbyid() {
        rlprofilename = (RelativeLayout) findViewById(R.id.rlprofilename);
        txtprofilename_value = (TextView) findViewById(R.id.txtprofilename_value);
        rlv_existing = (RelativeLayout) findViewById(R.id.rlv_existing);
        rlv_notexist = (RelativeLayout) findViewById(R.id.rlv_notexisting);
        rlv_pending = (RelativeLayout) findViewById(R.id.rlv_pending);
        rlv_unkown = (RelativeLayout) findViewById(R.id.rlv_unknown);
        txt_pending = (TextView) findViewById(R.id.txt_rl_pending);
        txt_queue_unknown1 = (TextView) findViewById(R.id.txt_queue_unknown1);
        txt_queue_unknown2 = (TextView) findViewById(R.id.txt_queue_unknown2);
        txt_rl_existing = (TextView) findViewById(R.id.txt_rl_existing);
        txt_notexisting = (TextView) findViewById(R.id.txt_notexisting);
        txtnrictype_value = (TextView) findViewById(R.id.txtnrictype_value);
        txtnric_value = (TextView) findViewById(R.id.txtnric_value);
        txtdate_value = (TextView) findViewById(R.id.txtdate_value);
        txtage_value = (TextView) findViewById(R.id.txtage_value);
        txt_clinicname = (TextView) findViewById(R.id.txt_clinicname);
        txt_clinicname.setText(clinic_name);
        btn_existing_regqueue_req = (Button)findViewById(R.id.btn_existing_regqueue_req);
        btn_notexist_submitto_clinic = (Button)findViewById(R.id.btn_notexist_submitto_clinic);
        btn_unknow_profile_submit_clinic = (Button)findViewById(R.id.btn_unknow_profile_submit_clinic);
        btn_unkown_queue_request = (Button)findViewById(R.id.btn_unkown_queue_request);
    }

    public void setEvent() {
        rlprofilename.setOnClickListener(rlprofilenameOnClickListener);
        txtprofilename_value.setOnClickListener(rlprofilenameOnClickListener);
        btn_existing_regqueue_req.setOnClickListener(btnregisterQueue_OnClickListener);
        btn_notexist_submitto_clinic.setOnClickListener(btn_submitProfiletoclinic);
        btn_unknow_profile_submit_clinic.setOnClickListener(btn_submitProfiletoclinic);
        btn_unkown_queue_request.setOnClickListener(btnregisterQueue_OnClickListener);
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });
    }

    public void checklayout(String status) {
        if (status.equals("Y")) { //existing
            rlv_pending.setVisibility(View.GONE);
            rlv_notexist.setVisibility(View.GONE);
            rlv_unkown.setVisibility(View.GONE);
            rlv_existing.setVisibility(View.VISIBLE);
        } else if (status.equals("P")) {//peinding
            rlv_pending.setVisibility(View.VISIBLE);
            rlv_notexist.setVisibility(View.GONE);
            rlv_unkown.setVisibility(View.GONE);
            rlv_existing.setVisibility(View.GONE);
        } else if (status.equals("N")) {//notexisting
            rlv_pending.setVisibility(View.GONE);
            rlv_notexist.setVisibility(View.VISIBLE);
            rlv_unkown.setVisibility(View.GONE);
            rlv_existing.setVisibility(View.GONE);
        } else if (status.equals("U")) {//unknown
            rlv_pending.setVisibility(View.GONE);
            rlv_notexist.setVisibility(View.GONE);
            rlv_unkown.setVisibility(View.VISIBLE);
            rlv_existing.setVisibility(View.GONE);
        } else {
            rlv_pending.setVisibility(View.GONE);
            rlv_notexist.setVisibility(View.GONE);
            rlv_unkown.setVisibility(View.GONE);
            rlv_existing.setVisibility(View.GONE);
        }
    }

    public void loadtextview() {
        //pending
        String str_pending1 = getResources().getString(R.string.msg_queue_info_pending1);
        String str_pending2 = getResources().getString(R.string.msg_queue_info_pending2);
        String str_pending_middle = getResources().getString(R.string.msg_queue_45mins);
        String pending = str_pending1 + "<b><font color=\"#0D47A1\"> " + str_pending_middle + " </font></b>" + str_pending2;
        txt_pending.setText(Html.fromHtml(pending));
        //Unknown
        String str_unknown1 = getResources().getString(R.string.msg_queue_info1_part1);
        String str_unknown1_1 = getResources().getString(R.string.msg_queue_info_part3);
        String unknown1 = str_unknown1 + "<b><font color=\"#0D47A1\"> " + str_pending_middle + " </font></b>" + str_unknown1_1;
       // txt_queue_unknown1.setText(Html.fromHtml(unknown1));
        txt_queue_unknown1.setVisibility(View.GONE);
        String str_unknown2 = getResources().getString(R.string.msg_queue_unknown1);
        String str_unknown2_1 = getResources().getString(R.string.msg_queue_unknown2);
        String str_unknown2_middle = getResources().getString(R.string.msg_queue_10mins);
        String str_unknown2_2 = getResources().getString(R.string.msg_queue_unknown3);
        String str_unknown2_3 = getResources().getString(R.string.msg_queue_unknown4);
        String unknown2 = str_unknown2 + "<br/><br/>" + "2." + str_unknown2_1 + "<b><font color=\"#0D47A1\"> " + str_unknown2_middle + " </font></b>" +
                str_unknown2_2 + "<br/><br/>" + "3." + str_unknown2_3;
        txt_queue_unknown2.setText(Html.fromHtml(unknown2));
        //existing
        String existing = "1." + str_unknown2_1 + "<b><font color=\"#0D47A1\"> " + str_unknown2_middle + " </font></b>" +
                str_unknown2_2 + "<br/><br/>" + "2." + str_unknown2_3;
        txt_rl_existing.setText(Html.fromHtml(existing));
        //not existing
        String notexisting = str_unknown1 + "<b><font color=\"#0D47A1\"> " + str_pending_middle + " </font></b>" + str_unknown1_1;
        txt_notexisting.setText(Html.fromHtml(notexisting));
    }

    View.OnClickListener rlprofilenameOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RequestJsonProfileDownload obj_profile = new RequestJsonProfileDownload(memberid, clinicid, hecode);
            JSONObject json_profile = obj_profile.StringtoJsonObject(obj_profile.ObjecttoJson(obj_profile));
            Log.e("json download param", json_profile.toString());
            if (Utils.hasInternetConnection(getApplicationContext())) {
                VolleyPatientProfileDownloadStatus _vprofilestatus = new VolleyPatientProfileDownloadStatus(_mcontext);
                _vprofilestatus.PostPatientProfileDownloadStatusJson(usertoken,json_profile, new VolleyPatientProfileDownloadStatus.VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList<ResponseDownloadStatus> response_list) {
                        Log.e("profile status list", response_list.size() + "");

                        Intent intent_download = new Intent(_mcontext, Activitydownloadprofilestatus.class);
                        intent_download.putParcelableArrayListExtra("profile_status", response_list);
                        intent_download.putExtra("memberid", memberid);
                        intent_download.putExtra("hecode", hecode);
                        intent_download.putExtra("clinicid", clinicid);
                        intent_download.putExtra("clinicname", clinic_name);
                        startActivity(intent_download);
                        finish();
                    }
                    @Override
                    public void onFail(String errorcode, String errormessage) {
                        Log.e(errorcode, errormessage);
                        // Utils.showAlertDialog(_mcontext,errorcode,errormessage);
                        Intent intent_download = new Intent(_mcontext, Activitydownloadprofilestatus.class);
                        intent_download.putParcelableArrayListExtra("profile_status", new ArrayList<ResponseDownloadStatus>());
                        intent_download.putExtra("memberid", memberid);
                        intent_download.putExtra("hecode", hecode);
                        intent_download.putExtra("clinicid", clinicid);
                        intent_download.putExtra("clinicname", clinic_name);
                        startActivity(intent_download);
                        finish();
                    }
                });
            }else{
                Utils.showInternetRequiredDialog(ActivityRegisterQueue.this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                return;
            }
        }
    };
    View.OnClickListener btnregisterQueue_OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setRequestQueue();
        }
    };
    View.OnClickListener btn_submitProfiletoclinic = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showDialogProfileSubmit();
        }
    };
    public void clear() {
        txtage_value.setText("---");
        txtdate_value.setText("---");
        txtnric_value.setText("---");
        txtnrictype_value.setText("---");
        txtprofilename_value.setText("None");
    }

    public void LoadData() {
        txtnric_value.setText(response_object.getNric());
        txtdate_value.setText(response_object.getDob());
        txtage_value.setText(Utils.calculate_userage(response_object.getDob()) + "");
        txtprofilename_value.setText(response_object.getNricname());
        checklayout(response_object.getDownloaded());
        int nric_type = Integer.parseInt(response_object.getNrictype());
        Log.e("nric type", nric_type + "******");

        if (nric_type == 0) {
            txtnrictype_value.setText("OTHER");
        } else if (nric_type == 1) {
            txtnrictype_value.setText("PINK");
        } else if (nric_type == 2) {
            txtnrictype_value.setText("BLUE");
        } else if (nric_type == 3) {
            txtnrictype_value.setText("FIN");
        }
        else if(nric_type == 4) {
            txtnrictype_value.setText("PASSPORT");
        }
    }

    public void setRequestQueue() {
        if (!usertoken.equals("")) {
            if (Utils.hasInternetConnection(_mcontext)) {
                if (!account_verify) {
                    Utils.showInternetRequiredDialog(_mcontext, getString(R.string.error_response_invalidtoken_message), getString(R.string.error_response_upload_profile_status_InvalidUserToken));
                } else {
                    RequestJsonQueueRegister queueRegister = new RequestJsonQueueRegister(response_object.getNric(),response_object.getDob(),Integer.parseInt(response_object.getNrictype()));

                    VolleyQueueRegister _vqueuregister = new VolleyQueueRegister(_mcontext,response_object.getNricname(),clinicid,clinic_name,response_object.getDob(),response_object.getNric(),usertoken);
                    _vqueuregister.PostQueueRegisterJson(clinicid, usertoken, queueRegister.StringtoJsonObject(queueRegister.ObjecttoJson(queueRegister)), new VolleyQueueRegister.VolleyCallback() {
                        @Override
                        public void onSuccess(String status) {
                            Intent intent = new Intent(_mcontext, ActivityQueue_Appoint.class);
                            intent.putExtra("from", "ActivityQueueDialog");
                            _mcontext.startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFail(String errorcode, String errormessage) {
                            Utils.showAlertDialog(_mcontext, errorcode, errormessage);
                        }
                    });

                }
            } else {
                Utils.showInternetRequiredDialog(_mcontext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                return;
            }
        }

    }

    public void showDialogProfileSubmit(){
        AlertDialog.Builder dialog = new AlertDialog.Builder(_mcontext);
        dialog.setTitle("Submit Profile");
        dialog.setMessage("Are you sure to submit your profile (" + response_object.getNricname() +
                ") to clinic ("+clinic_name+")?");
        dialog.setPositiveButton("OK",new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                submitProfiletoClinic();
                dialog.dismiss();
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void submitProfiletoClinic() {
        try {
            if (Utils.hasInternetConnection(_mcontext)) {
                MedicalProfile upload_medobj = dbmedprofile.getMedProfilebyNRIC_memberid(Integer.parseInt(response_object.getNrictype()), response_object.getNric(), memberid);
                RequestJsonMedicalProfileUpload obj = prepareJsonProfile(upload_medobj);
                JSONObject json = obj.StringtoJsonObject(obj.ObjecttoJson(obj));
                if (account_verify && (usertoken.length() > 0)) {
                    VolleyUploadMedicalProfile _vupload = new VolleyUploadMedicalProfile(_mcontext, "", upload_medobj, "rq");
                    _vupload.PostUploaddProfileJson(preference.getString(getString(R.string.pref_login_Access_token), ""), json, new VolleyUploadMedicalProfile.VolleyCallback() {
                        @Override
                        public void onSuccess(String status) {
                            preference.edit().putBoolean(getString(R.string.pref_update_profile_flag), true).apply();
                            finish();
                        }

                        @Override
                        public void onFail(String errorcode, String errormsg) {
                            Utils.showAlertDialog(_mcontext, errorcode, errormsg);
                        }
                    });
                } else {
                    Utils.showAlertDialog(_mcontext, "Profile Submission Fail", getString(R.string.error_response_upload_profile_status_Failed), "ActivityMedicalProfileList");
                }
            } else {
                Utils.showInternetRequiredDialog(_mcontext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public RequestJsonMedicalProfileUpload prepareJsonProfile(MedicalProfile medicalobj) {
        RequestJsonMedicalProfileUpload new_profile = new RequestJsonMedicalProfileUpload();
        new_profile.setClinicId(clinicid);
        new_profile.setHecode(hecode);

        new_profile.setNric(medicalobj.getNric());
        new_profile.setNricType(medicalobj.getNric_type());
        new_profile.setFullName(medicalobj.getNric_name());

        if(medicalobj.getGender()>-1){
            new_profile.setSex(medicalobj.getGender());
        }
        else{
            new_profile.setSex(1);
        }

        String nationality = medicalobj.getNationality();
        if(!nationality.equals("")){
            String nationcode = Utils.getnationalitycode(_mcontext,nationality);
            new_profile.setNationality(nationcode);
        }


        String pro_dob = medicalobj.getDob();
        new_profile.setDateOfBirth(pro_dob);
        new_profile.createAddress(
                medicalobj.getBlock_no(), medicalobj.getStreet(),
                medicalobj.getBuilding_name(), medicalobj.getUnit_no(),
                medicalobj.getPostal_code(),medicalobj.getAddress1(),medicalobj.getAddress2(),
                medicalobj.getAddress3(),medicalobj.getAddress4());
        new_profile.createContact(
                medicalobj.getTel1_code()+medicalobj.getTel1(), medicalobj.getTel2(), medicalobj.getEmail(),medicalobj.getTel1_iso());
        new_profile.setAllergy(medicalobj.getAllergy());
        //region medical profile object
        int relation = medicalobj.getRelation();
        int marriedstatus = medicalobj.getMarried_staus();
        int language = medicalobj.getLanguage();

        if (medicalobj.getMember() > 0) {
            new_profile.setRelationship(getResources().getString(R.string.mp_myself));
        } else {
            if (relation > -1) {
                new_profile.setRelationship(array_relation[relation - 1]);
            }
        }
        if (marriedstatus > -1) {
            new_profile.setMarried_status(marriedstatus);
        }
        if (language > -1) {
            new_profile.setLanguage(language+1);
        }
        else{
            new_profile.setLanguage(3);
        }
        return new_profile;
    }

    public void returnback(){
        Intent intent_flow = new Intent(_mcontext,ActivityQueueflow.class);
        intent_flow.putExtra("clinicid", clinicid);
        intent_flow.putExtra("clinicname", clinic_name);
        intent_flow.putExtra("hecode", hecode);
        intent_flow.putExtra("from_activity", "activitysearchclinicdetail");
        _mcontext.startActivity(intent_flow);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }
}

