package sg.com.ehealthassist.ehealthassist.Account;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestSmsOTP;
import sg.com.ehealthassist.ehealthassist.API.Request.Requestinsertprofilejson;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyAuthenticationService;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyInsertMemberListProfile;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyReVerifyMember;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleySmsOTP;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyVerifyMember;
import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;
import sg.com.ehealthassist.ehealthassist.ActivitySetting;

public class ActivitySMSToken extends AppCompatActivity {
    Context mContext;
    Button btnsubmit;
    String from = "";
    String memberid,usertoken = "",userphone="",signup_pwd="",gcmid="",nationality="";
    int userhpcode = 0;
    SharedPreferences preferences = null;
    ImageButton toolbar_imgbutton_back;
    TextView txtactivation,main_toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_activity_smstoken);

        main_toolbar_title = (TextView)findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_account_verfication));
        mContext = this;
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        memberid = preferences.getString(this.getString(R.string.pref_login_memberId),"");
        userphone = preferences.getString(this.getString(R.string.pref_main_user_data_hp),"");
        signup_pwd = preferences.getString(getString(R.string.pref_signup_password), "");
        userhpcode = preferences.getInt(getString(R.string.pref_main_user_data_hp_code),0);
        usertoken = preferences.getString(this.getString(R.string.pref_login_Access_token),"");
         gcmid = preferences.getString(getString(R.string.pref_gcm_property_reg_id), "");
        nationality=preferences.getString(getString(R.string.pref_main_user_data_hp_nationality),"");
        Intent in = getIntent();
        from = in.getStringExtra("from");
        callsmsvolleyapi(from);
        findviewbyid();
        setEvent();
    }
    public void findviewbyid(){
        txtactivation = (TextView)findViewById(R.id.edit_activatecode);
        btnsubmit =(Button)findViewById(R.id.btnsubmit);
        toolbar_imgbutton_back = (ImageButton)findViewById(R.id.toolbar_imgbackbutton);
    }

    public void setEvent(){
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            Boolean hasError = false;
            @Override
            public void onClick(View v) {
                if (txtactivation.getText().toString().length() <= 0) {
                    txtactivation.setError(getString(R.string.error_response_otp_empty));
                    hasError = true;
                }
                if(hasError){
                    return;
                }
                else{
                    // call api link
                    if(Utils.hasInternetConnection(mContext)){
                        String otp_token = txtactivation.getText().toString();
                        RequestSmsOTP obj_Json = new RequestSmsOTP(memberid,otp_token);

                        VolleySmsOTP v_otp  = new VolleySmsOTP(mContext);
                        v_otp.GetSmsOTPJsonData(obj_Json.StringtoJsonObject(obj_Json.objecttoJson()), new VolleySmsOTP.VolleyCallback() {
                            @Override
                            public void onSuccess(String message) {
                                VolleyAuthenticationService jwt_token = new VolleyAuthenticationService(mContext);
                                jwt_token.GetStringRequest(userhpcode+userphone,signup_pwd,new VolleyAuthenticationService.VolleyCallback() {
                                    @Override
                                    public void onSuccess(JSONObject message) {
                                        try{
                                            String acctesstoken = message.getString("access_token");
                                            String tokey_type = message.getString("token_type");
                                            String expires_in = message.getString("expires_in");
                                            usertoken = tokey_type+ " " + acctesstoken;
                                            preferences.edit().putBoolean(getString(R.string.pref_is_account_verified), true)
                                                    .putString(getString(R.string.pref_login_Access_token),usertoken)
                                                    .apply();
                                            volleyinsertMemberprofile(memberid,usertoken);
                                            clearSignupPreference();
                                            Intent intent = new Intent(mContext, ActivityHome1.class);
                                            mContext.startActivity(intent);
                                            finish();

                                          /*  RequestJsonAuth requestauth = new RequestJsonAuth(gcmid);
                                            JSONObject authjson = requestauth.StringtoJsonObject(requestauth.ObjectJsonforLoginView(gcmid));
                                            VolleyLoginView _vloginview = new VolleyLoginView(mContext,nationality);
                                            _vloginview.GetLoginViewJsonData(usertoken, authjson, new VolleyLoginView.VolleyCallback() {
                                                @Override
                                                public void onSuccess(String name,String nric,String nrictype,String dob) {

                                                    volleyinsertMemberprofile(memberid,usertoken,nric,name,dob,nrictype);
                                                    clearSignupPreference();
                                                    Intent intent = new Intent(mContext, ActivityHome1.class);
                                                    mContext.startActivity(intent);
                                                    finish();
                                                }

                                                @Override
                                                public void onFail(String errorcode, String errormessage) {
                                                  //  ActivityLogIn.tverrormsg.setText(errormessage);
                                                   // ActivityLogIn.tverrormsg.setVisibility(View.VISIBLE);
                                                    showAlertDialog(mContext, errorcode, errormessage);
                                                }
                                            });
*/
                                        }catch (Exception ex){
                                            ex.printStackTrace();
                                        }
                                    }
                                    @Override
                                    public void onFail(String errorcode, String errormessage) {
                                        Log.e("jwt",errormessage);
                                        showAlertDialog(mContext, errorcode, errormessage);
                                    }
                                });
                            }
                            @Override
                            public void onFail(String errorcode, String errormessage) {
                                showAlertDialog(mContext, errorcode, errormessage);
                            }
                        });
                    }
                    else{
                        Utils.showInternetRequiredDialog(ActivitySMSToken.this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                        return;
                    }
                }
            }
        });

        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });
    }
    public static void showAlertDialog(final Context context, final String title, String message) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(title.equals("InvalidOTP")){
                    dialog.dismiss();
                }
                else if (title.equals("ExpiredOTP")){
                    Intent intent = new Intent(context,ActivitySetting.class);
                    context.startActivity(intent);
                }
            }
        });
        alertDialog.show();
    }
    public void callsmsvolleyapi(String from){
        if(Utils.hasInternetConnection(mContext)){
            if(from.equals("ActivitySetting")){
                VolleyReVerifyMember v_sms = new VolleyReVerifyMember(mContext);
                v_sms.PostReVerifyMemberJson(memberid,"Sms");
            }
            else{
                VolleyVerifyMember v_sms = new VolleyVerifyMember(mContext);
                v_sms.GetSMSRequest(memberid,"Sms");
            }
        }
        else {
            Utils.showInternetRequiredDialog(ActivitySMSToken.this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void returnback(){
        Intent intent = new Intent(getApplicationContext(), ActivitySetting.class);
        startActivity(intent);
        finish();
    }
    //endregion

    public void volleyinsertMemberprofile(final String memberid, String accesstoken) {
        String nric = preferences.getString(getString(R.string.pref_signup_nric_number), "");
        String name = preferences.getString(getString(R.string.pref_signup_name), "");
        String dob = preferences.getString(getString(R.string.pref_signup_dob), "");
        String nricType = preferences.getInt(getString(R.string.pref_signup_nric_type), -1)+"";

        String mainaccount = "Y";
        if (Utils.hasInternetConnection(mContext)) {
            Requestinsertprofilejson param_obj = new Requestinsertprofilejson(memberid,nric,nricType,name,dob,mainaccount);
            JSONObject param_json = param_obj.StringtoJsonObject(param_obj.objecttoJson());
            Log.e("insertparam",param_json.toString());
            VolleyInsertMemberListProfile _vinsertprofile = new VolleyInsertMemberListProfile(mContext);
            _vinsertprofile.GetInsertMemberprofileRequest(param_json, accesstoken, new VolleyInsertMemberListProfile.VolleyCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.e("insertProfile",message);
                }
                @Override
                public void onFail(String errorcode, String errormessage) {
                    Log.e("insertPrfole", errorcode + " " + errormessage);
                }
            });
        }
    }

    public void clearSignupPreference() {
        preferences.edit()
                .putString(getString(R.string.pref_signup_name), "")
                .putString(getString(R.string.pref_signup_nric_number), "")
                .putString(getString(R.string.pref_signup_dob), "")
                .putInt(getString(R.string.pref_signup_gender), -1)
                .putInt(getString(R.string.pref_signup_marrital_status), -1)
                .putInt(getString(R.string.pref_signup_nric_type), -1)
                .putString(getString(R.string.pref_signup_mobile), "")
                .putString(getString(R.string.pref_signup_password), "")
                .putString(getString(R.string.pref_signup_email), "")
                .putBoolean(getString(R.string.pref_signup_cbagree), false)
                .putString(getString(R.string.pref_signup_postalcode), "")
                .putString(getString(R.string.pref_signup_blockno), "")
                .putString(getString(R.string.pref_signup_unitno), "")
                .putString(getString(R.string.pref_signup_street), "")
                .putString(getString(R.string.pref_signup_building), "")
                .putInt(getString(R.string.pref_signup_integer_nationalty), -1)
                .putString(getString(R.string.pref_signup_nationalty), "")
                .putString(getString(R.string.pref_signup_address1), "")
                .putString(getString(R.string.pref_signup_address2), "")
                .putString(getString(R.string.pref_signup_address3), "")
                .putString(getString(R.string.pref_signup_address4), "")
                .putString(getString(R.string.pref_signup_mobile_iso), "")
                .putInt(getString(R.string.pref_signup_mobile_code), 0)
                .apply();
    }
}
