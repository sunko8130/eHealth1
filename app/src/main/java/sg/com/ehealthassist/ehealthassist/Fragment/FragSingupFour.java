package sg.com.ehealthassist.ehealthassist.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
import sg.com.ehealthassist.ehealthassist.ActivitySetting;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 12/4/2017.
 */

public class FragSingupFour extends Fragment {

    Button btnsubmit;
    TextView txt_resend, txt_register;
    EditText editText_code_one, editText_code_two, editText_code_three, editText_code_four, editText_code_five, editText_code_six;
    String from = "";
    String memberid,usertoken = "",userphone="",signup_pwd="",gcmid="",nationality="";
    int userhpcode = 0;
    SharedPreferences preferences = null;
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_singup_four, container, false);

        preferences = getActivity().getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        memberid = preferences.getString(this.getString(R.string.pref_login_memberId),"");
        userphone = preferences.getString(this.getString(R.string.pref_main_user_data_hp),"");
        signup_pwd = preferences.getString(getString(R.string.pref_signup_password), "");
        userhpcode = preferences.getInt(getString(R.string.pref_main_user_data_hp_code),0);
        usertoken = preferences.getString(this.getString(R.string.pref_login_Access_token),"");
        gcmid = preferences.getString(getString(R.string.pref_gcm_property_reg_id), "");
        nationality=preferences.getString(getString(R.string.pref_main_user_data_hp_nationality),"");

        callsmsvolleyapi(from);
        findViewById();
        setEvent();
        return view;
    }

    private void findViewById() {
        txt_resend = view.findViewById(R.id.txt_resend);
        editText_code_one = view.findViewById(R.id.editText_activate_code_one);
        editText_code_two = view.findViewById(R.id.editText_activate_code_two);
        editText_code_three = view.findViewById(R.id.editText_activate_code_three);
        editText_code_four = view.findViewById(R.id.editText_activate_code_four);
        editText_code_five = view.findViewById(R.id.editText_activate_code_five);
        editText_code_six = view.findViewById(R.id.editText_activate_code_six);
        btnsubmit = view.findViewById(R.id.btn_verify_submit);
    }

    public void setEvent() {

        setEditTextAction();

        txt_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_resend.setTextColor(Color.RED);
                callsmsvolleyapi(from);
            }
        });

        btnsubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Boolean hasError = false;
                Log.d("verifytoken_check", "onclick");
                if (editText_code_one.getText().toString().length() <= 0) {
                    editText_code_one.setError(getString(R.string.error_response_otp_empty));
                    hasError = true;
                }
                if (editText_code_two.getText().toString().length() <= 0) {
                    editText_code_two.setError(getString(R.string.error_response_otp_empty));
                    hasError = true;
                }
                if (editText_code_three.getText().toString().length() <= 0) {
                    editText_code_three.setError(getString(R.string.error_response_otp_empty));
                    hasError = true;
                }
                if (editText_code_four.getText().toString().length() <= 0) {
                    editText_code_four.setError(getString(R.string.error_response_otp_empty));
                    hasError = true;
                }
                if (editText_code_five.getText().toString().length() <= 0) {
                    editText_code_five.setError(getString(R.string.error_response_otp_empty));
                    hasError = true;
                }
                if (editText_code_six.getText().toString().length() <= 0) {
                    editText_code_six.setError(getString(R.string.error_response_otp_empty));
                    hasError = true;
                }
                if (hasError) {
                    Log.d("verifytoken_check", "has error");
                    return;
                } else {
                    // call api link
                    if (Utils.hasInternetConnection(getContext())) {
                        Log.d("verifytoken_check", "has internet");
                        String otp_token = editText_code_one.getText().toString() + editText_code_two.getText().toString()
                                + editText_code_three.getText().toString() + editText_code_four.getText().toString()
                                + editText_code_five.getText().toString() + editText_code_six;
                        RequestSmsOTP obj_Json = new RequestSmsOTP(memberid, otp_token);
                        Log.d("verifytoken_check", otp_token + " ## otp");
                        Log.d("verifytoken_check", obj_Json.toString() + "##This is json");

                        VolleySmsOTP v_otp = new VolleySmsOTP(getContext());
                        Log.d("verifytoken_check", v_otp + " $$v_otp");
                        v_otp.GetSmsOTPJsonData(obj_Json.StringtoJsonObject(obj_Json.objecttoJson()), new VolleySmsOTP.VolleyCallback() {
                            @Override
                            public void onSuccess(String message) {
                                VolleyAuthenticationService jwt_token = new VolleyAuthenticationService(getContext());
                                jwt_token.GetStringRequest(userhpcode + userphone, signup_pwd, new VolleyAuthenticationService.VolleyCallback() {
                                    @Override
                                    public void onSuccess(JSONObject message) {
                                        try {
                                            String acctesstoken = message.getString("access_token");
                                            String tokey_type = message.getString("token_type");
                                            String expires_in = message.getString("expires_in");
                                            usertoken = tokey_type + " " + acctesstoken;
                                            preferences.edit().putBoolean(getString(R.string.pref_is_account_verified), true)
                                                    .putString(getString(R.string.pref_login_Access_token), usertoken)
                                                    .apply();
                                            Log.d("verifytoken_check", "success one");
                                            volleyinsertMemberprofile(memberid, usertoken);
                                            clearSignupPreference();
                                            Intent intent = new Intent(getContext(), ActivityHome1.class);
                                            getContext().startActivity(intent);
                                            Log.d("verifytoken_check", "success three");
                                            //finish();

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
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }

                                    @Override
                                    public void onFail(String errorcode, String errormessage) {
                                        Log.e("jwt", errormessage);
                                        showAlertDialog(getContext(), errorcode, errormessage);
                                        Log.d("verifytoken_check",  errormessage + " ### fail one");
                                    }
                                });
                            }

                            @Override
                            public void onFail(String errorcode, String errormessage) {
                                showAlertDialog(getActivity(), errorcode, errormessage);
                                Log.d("verifytoken_check",  errormessage + " ### fail final");
                            }
                        });
                    } else {
                        Utils.showInternetRequiredDialog(getContext(), getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                        return;
                    }
                }
            }
        });
    }

    private void setEditTextAction() {
        editText_code_one.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1)
                {
                    editText_code_two.requestFocus();
                }
            }
        });

        editText_code_two.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1)
                {
                    editText_code_three.requestFocus();
                }
            }
        });

        editText_code_three.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1)
                {
                    editText_code_four.requestFocus();
                }
            }
        });

        editText_code_four.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1)
                {
                    editText_code_five.requestFocus();
                }
            }
        });

        editText_code_five.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() == 1)
                {
                    editText_code_six.requestFocus();
                }
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

    public void volleyinsertMemberprofile(final String memberid, String accesstoken) {
        String nric = preferences.getString(getString(R.string.pref_signup_nric_number), "");
        String name = preferences.getString(getString(R.string.pref_signup_name), "");
        String dob = preferences.getString(getString(R.string.pref_signup_dob), "");
        String nricType = preferences.getInt(getString(R.string.pref_signup_nric_type), -1)+"";

        String mainaccount = "Y";
        if (Utils.hasInternetConnection(getContext())) {
            Requestinsertprofilejson param_obj = new Requestinsertprofilejson(memberid,nric,nricType,name,dob,mainaccount);
            JSONObject param_json = param_obj.StringtoJsonObject(param_obj.objecttoJson());
            Log.e("insertparam",param_json.toString());
            VolleyInsertMemberListProfile _vinsertprofile = new VolleyInsertMemberListProfile(getContext());
            _vinsertprofile.GetInsertMemberprofileRequest(param_json, accesstoken, new VolleyInsertMemberListProfile.VolleyCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.e("insertProfile",message);
                    Log.d("verifytoken_check", message + " ### success two");
                }
                @Override
                public void onFail(String errorcode, String errormessage) {
                    Log.e("insertPrfole", errorcode + " " + errormessage);
                    Log.d("verifytoken_check", errormessage + " ### fail two");
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

    public void callsmsvolleyapi(String from){
        if(Utils.hasInternetConnection(getContext())){
            if(from.equals("ActivitySetting")){
                VolleyReVerifyMember v_sms = new VolleyReVerifyMember(getContext());
                v_sms.PostReVerifyMemberJson(memberid,"Sms");
            }
            else{
                VolleyVerifyMember v_sms = new VolleyVerifyMember(getContext());
                v_sms.GetSMSRequest(memberid,"Sms");
            }
        }
        else {
            Utils.showInternetRequiredDialog(getContext(), getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
    }

    public void receiveFrom(String str){
        from = str;
        Log.d("fromfrom", from);
    }
}
