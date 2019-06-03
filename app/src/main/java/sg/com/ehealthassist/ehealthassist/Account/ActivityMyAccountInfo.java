package sg.com.ehealthassist.ehealthassist.Account;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import org.json.JSONObject;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestAccountInfo;
import sg.com.ehealthassist.ehealthassist.API.Request.RequestChangePassword;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyChangePassword;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyCountryCode;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyMemberQuery;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyUpdateAccountInfo;
import sg.com.ehealthassist.ehealthassist.CountryCode.CountryCodeAdapter;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Profile.CountryCode;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;
import sg.com.ehealthassist.ehealthassist.ActivitySetting;

public class ActivityMyAccountInfo extends AppCompatActivity {

    SharedPreferences preferences = null;
    Context mContext = null;
    //EditText txtphone,txtemail,edtit_oldpwd,edit_newpwd,edit_confirmpwd;
    RelativeLayout rdl_forgotpwd, rdl_edit_user_info, rdl_pwd;
    String ph_number,email,country_isocode;
    int country_code;
    //Button btn_accinfo_update,btn_pwdchange,btn_accoinfodropdownccode,btn_accoinfocountrycode;
    ImageButton imagbtnresetpwd,imagbtnresetemail,img_btn_close,toolbar_imgbackbutton;
    TextView txt_chgpwd_error,main_toolbar_title;
    Button btn_accoinfodropdownccode, btn_update, btn_cancel , btn_pwd_cancel, btn_pwd_update;
    EditText txtphone, txtemail, edtit_oldpwd, edit_newpwd;
    String usertoken="";
    Dialog dialog;
    DBMedProfile dbmedprofile;
    ListView lvcountrycode;CountryCodeAdapter adpt_countrycode;
    ArrayList<CountryCode> lst_countrycode = new ArrayList<CountryCode>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_my_account_info);
        main_toolbar_title = (TextView)findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_account_setting));
        mContext = this;
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        usertoken = preferences.getString(getString(R.string.pref_login_Access_token),"");
        dbmedprofile = new DBMedProfile(mContext);
        findviewById();
        //callvolleyCountryCode();
        //volleyMemberQuery();
        setEvent();

    }
    //region findViewById()
    public void findviewById(){
        toolbar_imgbackbutton = (ImageButton)findViewById(R.id.toolbar_imgbackbutton);
        /*txtphone =(EditText)findViewById(R.id.txtphone);
        txtemail = (EditText)findViewById(R.id.txtemail);
        btn_accinfo_update = (Button)findViewById(R.id.btn_accinfo_update);
        btn_accoinfodropdownccode = (Button)findViewById(R.id.btn_accoinfodropdownccode);
        btn_accoinfocountrycode = (Button)findViewById(R.id.btn_accoinfocountrycode);*/
        /*imagbtnresetemail =(ImageButton)findViewById(R.id.imagbtnresetemail);
        imagbtnresetpwd =(ImageButton)findViewById(R.id.imagbtnresetpwd);*/
        rdl_pwd = (RelativeLayout)findViewById(R.id.rdl_pwd);
        rdl_forgotpwd=(RelativeLayout)findViewById(R.id.rdl_forgotpwd);
        rdl_edit_user_info = (RelativeLayout)findViewById(R.id.rdl_edit_user_info);

    }
    //endregion
    //region LoadData()
    public void LoadData(){
        ph_number = preferences.getString(getString(R.string.pref_main_user_data_hp),"");
        email = preferences.getString(getString(R.string.pref_main_user_data_email),"");
        country_code = preferences.getInt(getString(R.string.pref_main_user_data_hp_code),0);
        country_isocode = preferences.getString(getString(R.string.pref_main_user_data_hp_iso),"");
        txtemail.setText(email);
        txtphone.setText(ph_number);
        //btn_accoinfocountrycode.setText("+"+country_code);
        btn_accoinfodropdownccode.setText(country_isocode + "+"+country_code);
    }
    //endregion
    //region setEvent()
    public void setEvent(){
        //region toolbar back
        toolbar_imgbackbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });
        //endregion

        rdl_edit_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callEditUserDialog();
            }
        });

        //region Change forgot pwd
        rdl_forgotpwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = new Dialog(mContext);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_forgot_password);
                Button btn_forgotpwd_cancel = dialog.findViewById(R.id.btn_alert_forgotpwd_cancel);
                Button btn_forgotpwd_reset = dialog.findViewById(R.id.btn_alert_forgotpwd_reset);
                btn_forgotpwd_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                btn_forgotpwd_reset.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intent = new Intent(getApplicationContext(), ActivityForgotPwd.class);
                        intent.putExtra("from","accountinfo");
                        startActivity(intent);
                        finish();
                    }
                });
                dialog.show();
            }
        });
        //endregion
        //region Change password
        rdl_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePassword();
            }
        });
        //endregion
    }

    private void callEditUserDialog() {

        dialog = new Dialog(ActivityMyAccountInfo.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alert_edit_user_info);


        /*AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMyAccountInfo.this);
        final AlertDialog dialog = builder.create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_edit_user_info, null);
        dialog.setView(view);*/

        btn_accoinfodropdownccode = dialog.findViewById(R.id.btn_alert_edit_userinfo_dropdowncode);
        btn_update = dialog.findViewById(R.id.btn_alert_edit_userinfo_update);
        btn_cancel = dialog.findViewById(R.id.btn_alert_edit_userinfo_cancel);
        txtphone = dialog.findViewById(R.id.et_alert__edit_userinfo_login_mobileId);
        txtemail = dialog.findViewById(R.id.et_alert_edit_userinfo_login_emial);
        callvolleyCountryCode();
        volleyMemberQuery();
        btn_accoinfodropdownccode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogCountryCode();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkedittext()) {
                    if(Utils.hasInternetConnection(mContext)){
                        final String memberid = preferences.getString(getString(R.string.pref_login_memberId),"");
                        final String phone = txtphone.getText().toString();
                        final String email = txtemail.getText().toString();
                        /*final String isocode = btn_accoinfodropdownccode.getText().toString();
                        final String countrycode = btn_accoinfocountrycode.getText().toString();
                        final String substrcountrycode = countrycode.substring(1);*/
                        final String isocode_test = btn_accoinfodropdownccode.getText().toString();
                        final String isocode = isocode_test.substring(0, 2);
                        final String substrcountrycode = isocode_test.substring(3);
                        Log.d("testphph", isocode + " isocode" + " ##" + substrcountrycode);
                        RequestAccountInfo request_info = new RequestAccountInfo(memberid,substrcountrycode+phone,email,isocode);
                        JSONObject param = request_info.StringtoJsonObject(request_info.objecttoJson());

                        VolleyUpdateAccountInfo account_update = new VolleyUpdateAccountInfo(mContext);
                        account_update.GetAccountUpdateRequest(param, new VolleyUpdateAccountInfo.VolleyCallback() {
                            @Override
                            public void onSuccess(String message) {
                                preferences.edit()
                                        .putString(getString(R.string.pref_main_user_data_email),email)
                                        .putString(getString(R.string.pref_main_user_data_hp), phone)
                                        .putString(getString(R.string.pref_main_user_data_hp_iso),isocode)
                                        .putInt(getString(R.string.pref_main_user_data_hp_code),Integer.parseInt(substrcountrycode))
                                        .apply();

                                MedicalProfile medicalProfile = dbmedprofile.getMedProfilebyflag(memberid);
                                medicalProfile.setEmail(email);
                                medicalProfile.setTel1(phone);
                                medicalProfile.setTel1_iso(isocode);
                                medicalProfile.setTel1_code(Integer.parseInt(substrcountrycode));
                                dbmedprofile.updateMedicalProfile(medicalProfile);
                                Utils.showAlertDialog(mContext,"Account Update","Your account info have been updated");
                            }
                            @Override
                            public void onFail(String errorcode, String errormessage) {
                                Utils.showAlertDialog(mContext,errorcode,errormessage);
                            }
                        });
                    }
                    else{
                        Utils.showInternetRequiredDialog(mContext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                        return;
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.show();


        /*AlertDialog.Builder builder = new AlertDialog.Builder(ActivityMyAccountInfo.this);
        final AlertDialog dialog = builder.create();
        View view = LayoutInflater.from(mContext).inflate(R.layout.custom_alert_edit_user_info, null);
        dialog.setView(view);
        btn_accoinfodropdownccode = view.findViewById(R.id.btn_alert_edit_userinfo_dropdowncode);
        btn_update = view.findViewById(R.id.btn_alert_edit_userinfo_update);
        btn_cancel = view.findViewById(R.id.btn_alert_edit_userinfo_cancel);
        txtphone = view.findViewById(R.id.et_alert__edit_userinfo_login_mobileId);
        txtemail = view.findViewById(R.id.et_alert_edit_userinfo_login_emial);
        callvolleyCountryCode();
        volleyMemberQuery();
        btn_accoinfodropdownccode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogCountryCode();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkedittext()) {
                    if(Utils.hasInternetConnection(mContext)){
                        final String memberid = preferences.getString(getString(R.string.pref_login_memberId),"");
                        final String phone = txtphone.getText().toString();
                        final String email = txtemail.getText().toString();
                        *//*final String isocode = btn_accoinfodropdownccode.getText().toString();
                        final String countrycode = btn_accoinfocountrycode.getText().toString();
                        final String substrcountrycode = countrycode.substring(1);*//*
                        final String isocode_test = btn_accoinfodropdownccode.getText().toString();
                        final String isocode = isocode_test.substring(0, 2);
                        final String substrcountrycode = isocode_test.substring(3);
                        Log.d("testphph", isocode + " isocode" + " ##" + substrcountrycode);
                        RequestAccountInfo request_info = new RequestAccountInfo(memberid,substrcountrycode+phone,email,isocode);
                        JSONObject param = request_info.StringtoJsonObject(request_info.objecttoJson());

                        VolleyUpdateAccountInfo account_update = new VolleyUpdateAccountInfo(mContext);
                        account_update.GetAccountUpdateRequest(param, new VolleyUpdateAccountInfo.VolleyCallback() {
                            @Override
                            public void onSuccess(String message) {
                                preferences.edit()
                                        .putString(getString(R.string.pref_main_user_data_email),email)
                                        .putString(getString(R.string.pref_main_user_data_hp), phone)
                                        .putString(getString(R.string.pref_main_user_data_hp_iso),isocode)
                                        .putInt(getString(R.string.pref_main_user_data_hp_code),Integer.parseInt(substrcountrycode))
                                        .apply();

                                MedicalProfile medicalProfile = dbmedprofile.getMedProfilebyflag(memberid);
                                medicalProfile.setEmail(email);
                                medicalProfile.setTel1(phone);
                                medicalProfile.setTel1_iso(isocode);
                                medicalProfile.setTel1_code(Integer.parseInt(substrcountrycode));
                                dbmedprofile.updateMedicalProfile(medicalProfile);
                                Utils.showAlertDialog(mContext,"Account Update","Your account info have been updated");
                            }
                            @Override
                            public void onFail(String errorcode, String errormessage) {
                                Utils.showAlertDialog(mContext,errorcode,errormessage);
                            }
                        });
                    }
                    else{
                        Utils.showInternetRequiredDialog(mContext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                        return;
                    }
                }
                dialog.dismiss();
            }
        });
        dialog.show();*/
    }
    //endregion
    //region valid Edittext view
    public boolean checkedittext(){
        Boolean hasError = false;
        if (txtemail.getText().toString().length() <= 0) {
            txtemail.setError("Required");
            hasError = true;
        }
        if (txtphone.getText().toString().length() <= 0) {
            txtphone.setError("Required");
            hasError = true;
        }
        if (hasError) {
            return hasError;
        }
        if (!Utils.isValidEmail(txtemail.getText())) {
            txtemail.setError(getText(R.string.err_invalid_email));
            hasError = true;
        }

        if (!Utils.isValidSGMobile(txtphone.getText().toString())) {
            txtphone.setError(getText(R.string.err_invalid_mobile));
            hasError = true;
        }
        if (hasError) {
            return hasError;
        }
        return hasError;

    }
    //endregion

    //region Dialog ChangePassword
    public void changePassword(){
        dialog = new Dialog(ActivityMyAccountInfo.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_changepassword);

        edtit_oldpwd = dialog.findViewById(R.id.editold_pwd);
        edit_newpwd = dialog.findViewById(R.id.editnew_pwd);
        txt_chgpwd_error = dialog.findViewById(R.id.txt_chgpwd_error);
        btn_pwd_cancel = dialog.findViewById(R.id.btn_alert_edit_pwd_cancel);
        btn_pwd_update = dialog.findViewById(R.id.btn_alert_edit_pwd_update);
        btn_pwd_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        btn_pwd_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                volleychangepwd();
            }
        });
        dialog.show();
    }

    //endregion
    //region volley api
    public void volleychangepwd(){
        Boolean hasError = false;
        if (edtit_oldpwd.getText().toString().length() <= 0) {
            edtit_oldpwd.setError("Required");
            hasError = true;
        }
        if (edit_newpwd.getText().toString().length() <= 0) {
            edit_newpwd.setError("Required");
            hasError = true;
        }
        if(edit_newpwd.getText().toString().length() < 5) {
            edit_newpwd.setError("password too short");
            hasError = true;
        }
        if (hasError) {
            return;
        }
        /*if (edit_confirmpwd.getText().toString().length() <= 0) {
            edit_confirmpwd.setError("Required");
            hasError = true;
        }
        if(edit_confirmpwd.getText().toString().length() < 5){
            edit_confirmpwd.setError("password too short");
        }
        if (hasError) {
            return;
        }
        if(!edit_newpwd.getText().toString().equals(edit_confirmpwd.getText().toString())){
            edit_confirmpwd.setError("Password do not match");
            hasError = true;
        }
        if(hasError){
            return;
        }*/
        if(Utils.hasInternetConnection(mContext)){
            final String memberid = preferences.getString(getString(R.string.pref_login_memberId),"");
            String oldpassword = edtit_oldpwd.getText().toString();
            String newpassword = edit_newpwd.getText().toString();

            RequestChangePassword request_info = new RequestChangePassword(memberid,oldpassword,newpassword);
            JSONObject param = request_info.StringtoJsonObject(request_info.objecttoJson());
            VolleyChangePassword account_update = new VolleyChangePassword(mContext);
            account_update.GetChangePwdRequest(param, new VolleyChangePassword.VolleyCallback() {
                @Override
                public void onSuccess(String message) {
                    txt_chgpwd_error.setText(message);
                    txt_chgpwd_error.setVisibility(View.VISIBLE);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Thread.sleep(1500);
                                dialog.dismiss();
                            } catch (Exception ex) {

                            }
                        }
                    }).start();
                }
                @Override
                public void onFail(String errorcode, String errormessage) {
                    txt_chgpwd_error.setText(errormessage);
                    txt_chgpwd_error.setVisibility(View.VISIBLE);
                }
            });
        }
        else{
            Utils.showInternetRequiredDialog(mContext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
    }
    //endregion
    //region call api q
    public void volleyMemberQuery(){
        if(Utils.hasInternetConnection(mContext)){
            VolleyMemberQuery _v_query = new VolleyMemberQuery(mContext);
            _v_query.GetMemberQueryJson(usertoken, new VolleyMemberQuery.VolleyCallback() {
                @Override
                public void onSuccess(String message) {
                    LoadData();
                }

                @Override
                public void onFail(String errorcode, String errormessage) {
                    Utils.showAlertDialog(mContext,errorcode,errormessage);
                }
            });
        }else{
            Utils.showInternetRequiredDialog(mContext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
    }
    //endregion
    // region Exit from App()
    public void returnback(){
        Intent intent = new Intent(getApplicationContext(), ActivitySetting.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }
    //endregion
    //region CountryCode

    public void DialogCountryCode(){
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogcountrycode);

        lvcountrycode = (ListView) dialog.findViewById(R.id.lvcountrycode);
        LoadCountryCode();
        lvcountrycode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CountryCode code = lst_countrycode.get(position);
                //btn_accoinfocountrycode.setText("+" +code.getCountryCode());
                btn_accoinfodropdownccode.setText(code.getISOCode() + "+" +code.getCountryCode());
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void callvolleyCountryCode(){
        if (Utils.hasInternetConnection(mContext)) {
            VolleyCountryCode v_countryCode = new VolleyCountryCode(mContext);
            v_countryCode.GetCountryCodeJsonData(new VolleyCountryCode.VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<CountryCode> success) {
                    lst_countrycode = success;
                }

                @Override
                public void onFail(String errorcode, String errormessage) {
                    btn_accoinfodropdownccode.setText(getResources().getString(R.string.default_countryisocode) + "+"+getResources().getString(R.string.default_countrycode));
                    //btn_accoinfocountrycode.setText("+"+getResources().getString(R.string.default_countrycode));
                }
            });
        }else{
            btn_accoinfodropdownccode.setText(getResources().getString(R.string.default_countryisocode) + "+"+getResources().getString(R.string.default_countrycode));
            //btn_accoinfocountrycode.setText("+"+getResources().getString(R.string.default_countrycode));
        }
    }
    public void LoadCountryCode(){
        adpt_countrycode = new CountryCodeAdapter(mContext, lst_countrycode);
        adpt_countrycode.notifyDataSetChanged();
        lvcountrycode.setAdapter(adpt_countrycode);
    }
    //endregion

}
