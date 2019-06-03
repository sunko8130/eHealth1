package sg.com.ehealthassist.ehealthassist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.kyleduo.switchbutton.SwitchButton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestLogout;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyLogout;
import sg.com.ehealthassist.ehealthassist.Account.ActivityCreateAccount;
import sg.com.ehealthassist.ehealthassist.Account.ActivityLogIn;
import sg.com.ehealthassist.ehealthassist.Account.ActivityMyAccountInfo;
import sg.com.ehealthassist.ehealthassist.Account.ActivityPIN;
import sg.com.ehealthassist.ehealthassist.Account.ActivitySMSToken;
import sg.com.ehealthassist.ehealthassist.Account.ActivitySetupPIN;
import sg.com.ehealthassist.ehealthassist.Account.ActivitySingup;
import sg.com.ehealthassist.ehealthassist.Clinic.ActivityPreferredClinic;
import sg.com.ehealthassist.ehealthassist.DB.DBBookInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.DB.DBQueueRequest;
import sg.com.ehealthassist.ehealthassist.DB.DBTestClinic;
import sg.com.ehealthassist.ehealthassist.GDAA.GDAAConnect;
import sg.com.ehealthassist.ehealthassist.GDAA.GDAAService;
import sg.com.ehealthassist.ehealthassist.Models.Appointment.BookInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Privacy.ActivityTNCWebView;
import sg.com.ehealthassist.ehealthassist.Queue_Appt.ActivityQueue_Appoint;

public class ActivitySetting extends AppCompatActivity {

    SharedPreferences preferences = null;
    SharedPreferences pref_constant = null;
    SwitchButton sb_pin_listener,drive_backup_pin_listener;
    /*ImageView bottomTabHome, bottomTabProfile, bottomTabClinic, bottomTabAppointment, bottomTabSetting;
    TextView txtEmail, txtVersion, txtPendingVerification, txtaccountinfo, main_toolbar_title,txtdownloadaccount;
    ImageButton btnimageaccountinfo, toolbar_imgbutton_back, imgterm_conditon, imgprivacy;
    RelativeLayout rlaccountinfo, rlterm_conditions, rlprivacy;
    Button btnLogin, btn_hidden;
    CardView card_view2;*/

    Toolbar toolbar;
    ImageView toolbar_imgbutton_back;
    TextView main_toolbar_title;

    TextView txt_account_setting, txt_lock_profile, txt_backup_profile, txt_term_condition, txt_privacy_policies, txt_phno, txt_verify, txt_version;
    ImageView img_account_setting, img_term_condition, img_privacy_policies;
    View space_one;

    Context mContext = null;
    public boolean accountverification;
    public static boolean activity_detect = false;
    public static Activity _activity;
    private boolean isPinEnabled = false;
    private String pin, user_phone = "",user_phone_iso="",usertoken ="",gcmid= "";int user_phone_code = 0;
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    final Handler handler = new Handler();
    RelativeLayout setting_loadingpanel;
    DBTestClinic dbHandler_testclinic;
    DBClinicInfo dbHandler_clinicinfo;
    DBMedProfile dbHandler_medprofile;
    public static List<MedicalProfile> med_profile = new ArrayList<MedicalProfile>();
    List<ClinicInfo> get_test_clinic;
    boolean backup_drive = true;
    public  String login_member_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_setting);
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        pref_constant = getSharedPreferences(getString(R.string.preference_constant), MODE_PRIVATE);
        accountverification = preferences.getBoolean(getString(R.string.pref_is_account_verified), false);
        mContext = this;_activity = this;
        ActivityHome1.close();
        pin = preferences.getString(getString(R.string.pref_main_user_data_pin), "");
        backup_drive = preferences.getBoolean(getString(R.string.pref_backup_drive_checked),true);
        usertoken  = preferences.getString(getString(R.string.pref_login_Access_token),"");
        gcmid = preferences.getString(getString(R.string.pref_gcm_property_reg_id), "");
        isPinEnabled = !pin.equals("");
        activity_detect = true;
        user_phone = preferences.getString(getString(R.string.pref_main_user_data_hp), "");
        user_phone_code = preferences.getInt(getString(R.string.pref_main_user_data_hp_code),0);
        user_phone_iso = preferences.getString(getString(R.string.pref_main_user_data_hp_iso),"");
        login_member_id = preferences.getString(getString(R.string.pref_login_memberId), "");

        dbHandler_clinicinfo = new DBClinicInfo(this);
        dbHandler_testclinic = new DBTestClinic(this);
        dbHandler_medprofile = new DBMedProfile(this);
        get_test_clinic = dbHandler_testclinic.getallclinicinfo();
        findsViewById();
        loadData();
        setEvents();
        /*if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
            setting_loadingpanel.setVisibility(View.GONE);
            sb_pin_listener.setEnabled(true);
            drive_backup_pin_listener.setEnabled(true);
        } else {
            setting_loadingpanel.setVisibility(View.VISIBLE);
            txtdownloadaccount.setText(ActivitySplashScreen.download_count);
        }*/
        //handler.postDelayed(r, 1000);
    }
    //region handler
    /*final Runnable r = new Runnable() {
        public void run() {
            if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                setting_loadingpanel.setVisibility(View.GONE);
                handler.removeCallbacks(r);
                sb_pin_listener.setEnabled(true);
                drive_backup_pin_listener.setEnabled(true);
            } else {
                setting_loadingpanel.setVisibility(View.VISIBLE);
                txtdownloadaccount.setText(ActivitySplashScreen.download_count);
            }
            handler.postDelayed(this, 1000);
        }
    };*/
    //endregion
    //region onStop & on Resume
    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(accountverification){
            if(backup_drive){
                if(preferences.getBoolean(getString(R.string.pref_gdaa_connect_sync),false)){
                    /*if(btnLogin.getText().equals(getString(R.string.action_logout)))
                        med_profile = dbHandler_medprofile.getMedProfilebyMemberid(login_member_id);*/
                    if(med_profile.size() > 0 && med_profile.size() == 1){
                        if(med_profile.get(0).getNric().equals("") && med_profile.get(0).getNric_name().equals("")){
                            GDAAConnect.connect();
                            GDAAConnect.ReadContentFile("setting",this);
                        }
                    }
                }
            }
        }
    }
    //endregion
    // region findsViewByid()
    public void findsViewById() {

        txt_version = findViewById(R.id.txt_version_setting);
        txt_phno = findViewById(R.id.txt_phno_setting);
        txt_verify = findViewById(R.id.txt_verify_setting);
        txt_account_setting = findViewById(R.id.txt_account_setting);
        txt_lock_profile = findViewById(R.id.txt_lock_profile_setting);
        txt_backup_profile = findViewById(R.id.txt_backup_profile_setting);
        txt_term_condition = findViewById(R.id.txt_term_condition_setting);
        txt_privacy_policies = findViewById(R.id.txt_privacy_policies_setting);
        img_account_setting = findViewById(R.id.img_account_setting_go);
        img_term_condition = findViewById(R.id.img_term_condition_continue);
        img_privacy_policies = findViewById(R.id.img_privacy_policies_continue);
        space_one = findViewById(R.id.space_one);

        /*bottomTabHome = (ImageView) findViewById(R.id.iconHome);
        bottomTabProfile = (ImageView) findViewById(R.id.iconProfile);
        bottomTabClinic = (ImageView) findViewById(R.id.iconClinic);
        bottomTabAppointment = (ImageView) findViewById(R.id.iconAppt);
        bottomTabSetting = (ImageView) findViewById(R.id.iconAbout);
        btnLogin = (Button) findViewById(R.id.button_login);
        txtEmail = (TextView) findViewById(R.id.editTextSettingsEmail);
        txtVersion = (TextView) findViewById(R.id.editTextSettingsVersion);
        txtPendingVerification = (TextView) findViewById(R.id.textViewSettingsVerifyEmail);*/
        sb_pin_listener = (SwitchButton) findViewById(R.id.sb_pin_listener);
        sb_pin_listener.setEnabled(true);
        drive_backup_pin_listener = (SwitchButton)findViewById(R.id.drive_backup_pin_listener);
        drive_backup_pin_listener.setChecked(backup_drive);
        drive_backup_pin_listener.setEnabled(true);

        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        //toolbar_imgbutton_back.setVisibility(View.GONE);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(R.string.title_setting);
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySetting.this, ActivityHome.class);
                startActivity(intent);
                finish();
            }
        });

        /*rlaccountinfo = (RelativeLayout) findViewById(R.id.rlmyaccount);
        rlprivacy = (RelativeLayout) findViewById(R.id.rlprivacy);
        rlterm_conditions = (RelativeLayout) findViewById(R.id.rlterm_conditions);
        imgprivacy = (ImageButton) findViewById(R.id.imgprivacy);
        imgterm_conditon = (ImageButton) findViewById(R.id.imgterm_conditon);
        btnimageaccountinfo = (ImageButton) findViewById(R.id.imgaccountinfo);
        txtaccountinfo = (TextView) findViewById(R.id.txtaccountinfo);
        txtdownloadaccount = (TextView)findViewById(R.id.txtdownloadaccount);
        card_view2 = (CardView) findViewById(R.id.card_view2);
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        toolbar_imgbutton_back.setVisibility(View.GONE);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(R.string.title_setting);*/
        /*setting_loadingpanel = (RelativeLayout) findViewById(R.id.setting_loadingPanel);
        btn_hidden = (Button) findViewById(R.id.btn_hidden);*/

    }
    //endregion
    // region setEvents ()
    public void setEvents() {
        /*bottomTabHome.setOnClickListener(bottomTabOnClickListener);
        bottomTabProfile.setOnClickListener(bottomTabOnClickListener);
        bottomTabClinic.setOnClickListener(bottomTabOnClickListener);
        bottomTabAppointment.setOnClickListener(bottomTabOnClickListener);
        bottomTabSetting.setOnClickListener(bottomTabOnClickListener);
        btnLogin.setOnClickListener(btnLoginOnClickListener);*/
        txt_verify.setOnClickListener(pendingVerificationOnClickListener);
        //rlaccountinfo.setOnClickListener(myaccountinfoOnClickListener);
        img_account_setting.setOnClickListener(myaccountinfoOnClickListener);
        txt_account_setting.setOnClickListener(myaccountinfoOnClickListener);
        txt_term_condition.setOnClickListener(term_conditionOnClickListener);
        txt_privacy_policies.setOnClickListener(privacyOnClinckListener);
        img_term_condition.setOnClickListener(term_conditionOnClickListener);
        img_privacy_policies.setOnClickListener(privacyOnClinckListener);
        //btn_hidden.setOnClickListener(btn_hiddenOnClinicListener);
        sb_pin_listener.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(getApplicationContext(), ActivityPIN.class);
                    intent.putExtra("CallNext", "ActivitySetupPIN");
                    intent.putExtra("CallBack", "ActivitySetting");
                    startActivityForResult(intent, 1);
                } else {
                    Intent intent = new Intent(getApplicationContext(), ActivitySetupPIN.class);
                    startActivityForResult(intent, 1);
                }
                finish();
            }
        });

        drive_backup_pin_listener.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    backup_drive = true;
                    startService(new Intent(mContext, GDAAService.class));
                }else{
                    backup_drive = false;
                    stopService(new Intent(mContext, GDAAService.class));
                }
                preferences.edit().putBoolean(getString(R.string.pref_backup_drive_checked),backup_drive).apply();
            }
        });
    }

    //region ButtonLoginOnClickListener()
   /* View.OnClickListener btnLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                if (btnLogin.getText().toString().equals(getText(R.string.msg_account_login))) {
                    Intent intent = new Intent(getApplicationContext(), ActivityLogIn.class);
                    intent.putExtra("ActivityCallBack", "ActivitySetting");
                    intent.putExtra("from", "");
                    intent.putExtra("CID", 0);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                } else {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ActivitySetting.this);
                    alertBuilder.setMessage(getString(R.string.msgbox_sign_out_confirm_msg))
                            .setTitle("Confirm Sign Out?")
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    return;
                                }
                            })
                            .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // if (editTextInput.getText().toString().equals("SIGNOUT")) {
                                    try{
                                        close();
                                        if(Utils.hasInternetConnection(mContext)){
                                            RequestLogout  reqjson = new RequestLogout("1",gcmid);
                                            JSONObject jsonparam = reqjson.StringtoJsonObject(reqjson.ObjecttoJson());
                                            VolleyLogout v_logout = new VolleyLogout(mContext);
                                            v_logout.PostLogOutJsonData(usertoken,jsonparam);
                                        }
                                        preferences.edit().clear().commit();
                                        DBBookInfo deletebook = new DBBookInfo(getApplicationContext());
                                        ArrayList<BookInfo> booklist = deletebook.getRequestLog();
                                        for (int d = 0; d < booklist.size(); d++) {
                                            Utils.CancleAlarm(mContext, booklist.get(d).getId());
                                        }
                                        deletebook.deleteAllBooks();
                                        DBQueueRequest deleterequest = new DBQueueRequest(getApplicationContext());
                                        String currentdate = Utils.getcurrenttime("dd-MMM-yyyy hh:mm a");
                                        deleterequest.deleteQueuebyCurrentDate(currentdate);
                                    for(int cl = 0;cl <get_test_clinic.size();cl++){
                                        dbHandler_clinicinfo.deleteclinicbyId(get_test_clinic.get(cl).get_id());
                                    }
                                        dbHandler_clinicinfo.deletetestclinic();

                                        Intent intent = new Intent(getApplicationContext(), ActivityHome1.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "Signed Out Successfully", Toast.LENGTH_LONG).show();
                                        finish();

                                    }catch (Exception ex){
                                        ex.printStackTrace();
                                    }
                                }
                            });
                    alertBuilder.create().show();
                    return;
                }
            }
        }
    };*/
    //endregion
    //region BottomTabOnClickListener()
    /*View.OnClickListener bottomTabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            String caller = v.getResources().getResourceEntryName(v.getId());
            switch (caller) {
                case "iconHome":
                    intent = new Intent(getApplicationContext(), ActivityHome1.class);
                    break;
                case "iconProfile":
                    intent = new Intent(getApplicationContext(), ActivityPIN.class);
                    break;
                case "iconClinic":
                    intent = new Intent(getApplicationContext(), ActivityPreferredClinic.class);
                    break;
                case "iconAppt":
                    intent = new Intent(getApplicationContext(), ActivityQueue_Appoint.class);
                    intent.putExtra("from", "ActivitySetting");
                    break;
            }
            if (intent != null) {
                startActivity(intent);
                finish();
            }
        }
    };*/
    //endregion
    //region PendingVerificationOnClickListener
    View.OnClickListener pendingVerificationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // **************** CALL To API *******************
            showResendEmailDialog();
        }
    };
    //endregion
    //region myaccountinfoOnClickListener
    View.OnClickListener myaccountinfoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, ActivityMyAccountInfo.class);
            startActivity(intent);
            finish();
        }
    };
    //endregion
    //region term&conditon and privacy
    View.OnClickListener term_conditionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goto_privacy("tnc");
        }
    };
    View.OnClickListener privacyOnClinckListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            goto_privacy("privacy");
        }
    };

    public void goto_privacy(String link) {
        Intent intent = new Intent(this, ActivityTNCWebView.class);
        intent.putExtra("url", link);
        intent.putExtra("from_activity", "ActivitySetting");
        startActivity(intent);
        finish();
    }

    //endregion
    //region Hidden Test Clinic Dialog
    /*View.OnClickListener btn_hiddenOnClinicListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // if (ActivitySplashScreen.download_flag) {
            if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                final Dialog dialog = new Dialog(ActivitySetting.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_password_layout);
                final EditText textmsg = (EditText) dialog.findViewById(R.id.edit_hidden_pwd);
                Button dialog_yes = (Button) dialog.findViewById(R.id.btnpwd_Yes);
                Button dialog_no = (Button) dialog.findViewById(R.id.btnpwd_No);

                dialog_yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final String password = textmsg.getText().toString();
                        if (password.equals(getString(R.string.testpwd))) {
                            int successrecord = 0;
                            if(get_test_clinic.size() > 0){
                                for(int i =0; i< get_test_clinic.size();i++){
                                    if(dbHandler_clinicinfo.updateClinicInfos(get_test_clinic.get(i))>0){
                                        successrecord++;
                                    }
                                }
                            }
                            dialog.dismiss();

                        } else {
                            Toast.makeText(getApplicationContext(),
                                    "Wrong Password!" + password, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialog_no.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }
    };*/
    //endregion
    // endregion
    //region LoadData()
    private void loadData() {
        if (preferences != null) {
            pin = preferences.getString(getString(R.string.pref_main_user_data_pin), "");

            isPinEnabled = !pin.equals("");
            boolean isLoggedIn = preferences.getBoolean(getString(R.string.pref_is_logged_in), false);
            if (isLoggedIn) {
                togglePendingVerificationMessage();
                /*rlaccountinfo.setVisibility(View.VISIBLE);
                card_view2.setVisibility(View.VISIBLE);*/
                txt_account_setting.setVisibility(View.VISIBLE);
                img_account_setting.setVisibility(View.VISIBLE);
                space_one.setVisibility(View.VISIBLE);
                if (!user_phone.equals("")) {
                    //txtEmail.setText("+"+user_phone_code+user_phone);
                    txt_phno.setText("+" + user_phone_code + " " + user_phone);
                    if(user_phone.length()>8){
                        if(user_phone.charAt(0)== '6' & user_phone.charAt(1) == '5'){
                            //txtEmail.setText("+" + user_phone);
                            txt_phno.setText("+" + user_phone);
                        }
                    }else{
                        //txtEmail.setText(user_phone);
                        txt_phno.setText(user_phone);
                    }
                } else {
                    //txtEmail.setText(user_phone);
                    txt_phno.setText(user_phone);
                }
                /*btnLogin.setTextColor(getResources().getColor(R.color.colorred));
                btnLogin.setText(getString(R.string.action_logout));*/
                if (accountverification) {
                    //txtPendingVerification.setVisibility(View.GONE);
                    txt_verify.setVisibility(View.GONE);
                } else {
                    //txtPendingVerification.setVisibility(View.VISIBLE);
                    txt_verify.setVisibility(View.VISIBLE);
                }
            } else {
                txt_verify.setVisibility(View.GONE);
                //txtPendingVerification.setVisibility(View.GONE);
                /*btnLogin.setTextColor(getResources().getColor(R.color.cas_color_green));
                btnLogin.setText(getString(R.string.msg_account_login));
                rlaccountinfo.setVisibility(View.GONE);
                card_view2.setVisibility(View.GONE);*/
                txt_account_setting.setVisibility(View.GONE);
                img_account_setting.setVisibility(View.GONE);
                space_one.setVisibility(View.GONE);
            }
            if (isPinEnabled) {
                sb_pin_listener.setChecked(true);
            } else {
                sb_pin_listener.setChecked(false);
            }
        }
        //txtVersion.setText(appversion());
        txt_version.setText(appversion());
    }

    private void togglePendingVerificationMessage() {
        boolean isAccountVerified = preferences.getBoolean(getString(R.string.pref_is_account_verified), false);
        if (isAccountVerified) {
            if (txt_verify.getVisibility() == View.VISIBLE) {
                txt_verify.setVisibility(View.GONE);
            }
        } else {
            if (txt_verify.getVisibility() == View.GONE) {
                txt_verify.setVisibility(View.VISIBLE);
            }
        }
    }

    //endregion
    //region ShowResendEmailDialog()
    private void showResendEmailDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
        alertDialog.setTitle("Pending Verification");
        //  String user_phone = preferences.getString(getString(R.string.pref_main_user_data_hp),"");
        String message = getString(R.string.error_response_sms_reverify) +
                " " + "<font color=red><b>" + user_phone_code+user_phone + "</b></font>" + " " +
                getString(R.string.sign_verification_message2);

        alertDialog.setMessage(Html.fromHtml(message));
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("Re-Verify", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(mContext, ActivitySMSToken.class);
                        intent.putExtra("from", "ActivitySetting");
                        mContext.startActivity(intent);
                        finish();
                        //   ActivitySignUp.activity.finish();
                    }
                }
        );
        alertDialog.show();
    }

    //endregion
    //region onActivityResult() & onKeyDown ()
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        loadData();
    }
    public String appversion() {
        String version = "";
        try {
            PackageManager manager = mContext.getPackageManager();
            PackageInfo info = manager.getPackageInfo(
                    mContext.getPackageName(), 0);
            version = info.versionName;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return version;
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
        //handler.removeCallbacks(r);
    }
    /*public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finish();
            dbHandler_clinicinfo.deletetestclinic();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(mRunnable, 2000);
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        dbHandler_clinicinfo.deletetestclinic();
    }

    public static void close() {
        if (ActivityQueue_Appoint.activity_detect) {
            ActivityQueue_Appoint.mactivity.finish();
        }
    }
    //endregion*/


}
