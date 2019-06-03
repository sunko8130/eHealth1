package sg.com.ehealthassist.ehealthassist.Fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestJsonRegister;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyRegisterMember;
import sg.com.ehealthassist.ehealthassist.Account.ActivitySMSToken;
import sg.com.ehealthassist.ehealthassist.ActivitySetting;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Privacy.ActivityTNCWebView;
import sg.com.ehealthassist.ehealthassist.R;

import static android.content.Context.LOCATION_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 12/4/2017.
 */

public class FragSingupThree extends Fragment {

    TextView txt_terms_and_conditions, txt_privacy_policies;
    EditText editext_Password;
    Button btn_singup;
    CheckBox cbAgree;

    SharedPreferences preferences = null;
    DBMedProfile dbmedprofile;
    String[] nric_type,nric_type_passport, marrital_status,arr_nationality;
    String fromactivity = "", mEmail = "", mMobile = "", mImei = "", mGcmid = "",
            mPassword = "",mMobileISO="";int mMobileCode=0;

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_singup_three, container, false);
        preferences = getActivity().getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        dbmedprofile = new DBMedProfile(getContext());
        findViewById();
        setUnderLineText();
        setEvent();
        setControlEvent();
        return view;
    }

    private void findViewById() {
        txt_privacy_policies = view.findViewById(R.id.txt_fragthree_privacy_policies);
        txt_terms_and_conditions = view.findViewById(R.id.txt_fragthree_terms_and_condition);
        btn_singup = view.findViewById(R.id.btn_fragthree_sign_up);
        editext_Password = view.findViewById(R.id.editext_Password);
        cbAgree = view.findViewById(R.id.cbAgree);

        nric_type = getResources().getStringArray(R.array.array_nric_type);
        arr_nationality = getResources().getStringArray( R.array.country_code);
    }

    private void setEvent() {
        btn_singup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //region Create Register
                if (!validateallData()) if (cbAgree.isChecked()) {
                    //mEmail = TextViewEmail.getText().toString();
                    mEmail = preferences.getString(getString(R.string.pref_signup_email), "");
                    mPassword = preferences.getString(getString(R.string.pref_signup_password), "");
                    mImei = Utils.getImei(getContext());
                    mMobile = preferences.getString(getString(R.string.pref_signup_mobile), "");
                    mMobileCode = preferences.getInt(getString(R.string.pref_signup_mobile_code), 0);
                    mMobileISO = preferences.getString(getString(R.string.pref_signup_mobile_iso), "");
                    mGcmid = preferences.getString(getString(R.string.pref_gcm_property_reg_id), "");
                    //String nric = editText_nric.getText().toString();
                    String nric = preferences.getString(getString(R.string.pref_signup_nric_number), "");
                    Log.d("finaltest", mMobile + "=Mobile");
                    Log.d("finaltest", mMobileCode + "=MobileCode");
                    Log.d("finaltest", mMobileISO + "=MobileISO");
                    Log.d("finaltest", nric + "=Nric");
                    // ******* Call To API **********
                    if (Utils.hasInternetConnection(getContext())) {
                        RequestJsonRegister newuser = new RequestJsonRegister(mEmail, mMobileCode + mMobile, mMobileISO, mPassword, mGcmid, 1, mImei,nric);
                        Log.d("jsontest", newuser.toString());
                        String json = newuser.ObjecttoJson(newuser);
                        Log.e("register ",json);
                        VolleyRegisterMember v_register = new VolleyRegisterMember(getActivity(), newuser);
                        v_register.GetRegisterJson(newuser.StringtoJsonObject(json), new VolleyRegisterMember.VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject data_json) {
                                Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                                try {
                                    String memberid = data_json.getString(Constant.NODE_MemberId);
                                    String accesstoken = data_json.getString(Constant.NODE_AccessToken);
                                    Log.d("memberididid", memberid);
                                    preferences.edit()
                                            .putBoolean(getString(R.string.pref_is_logged_in), true)
                                            .putBoolean(getString(R.string.pref_is_account_verified), false)
                                            .putString(getString(R.string.pref_login_Access_token), "")
                                            .putString(getString(R.string.pref_login_memberId), memberid)
                                            .putString(getString(R.string.pref_main_user_data_hp), mMobile)
                                            .putString(getString(R.string.pref_main_user_data_hp_iso), mMobileISO)
                                            .putInt(getString(R.string.pref_main_user_data_hp_code), mMobileCode)
                                            .putString(getString(R.string.pref_main_user_data_email), mEmail)
                                            .apply();
                                    createMedicalprofile();

                                    Log.d("testtesttest","dialog");
                                    Dialog(getString(R.string.sign_verification),
                                            getString(R.string.sign_verification_message1) + " " + "<font color=red><b>" + mMobileCode + mMobile + "</b></font>"
                                                    + " " + getString(R.string.sign_verification_message2));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }

                            @Override
                            public void onFail(String errorcode, String errormessage) {
                                Utils.showAlertDialog(getContext(), errorcode, errormessage);
                            }
                        });

                    } else {
                        Utils.showInternetRequiredDialog(getContext(), getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                        return;
                    }
                } else {
                    showMessageDialog("Agreement", getString(R.string.msgbox_signup_agree_to_continue));
                    return;
                }
                else{
                    Utils.showAlertDialog(getContext(),"Alerts",getString(R.string.alerts_profile));
                }
                //endregion
            }
        });
    }

    private void setControlEvent(){

        editext_Password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String controlKey = getString(R.string.pref_signup_password);
                String controlValue = editext_Password.getText().toString();
                preferences.edit().putString(controlKey, controlValue).apply();
                editext_Password.setError(null);
            }
        });

        txt_terms_and_conditions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gotoTerms_Conditions("tnc");
                return false;
            }
        });

        txt_privacy_policies.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                gotoTerms_Conditions("privacy");
                return false;
            }
        });
        //endregion
    }

    //region Data Create,Load,Valid ()
    public void createMedicalprofile() {
        MedicalProfile membermedical = new MedicalProfile();
        membermedical.setNric_type(preferences.getInt(getString(R.string.pref_signup_nric_type), -1));
        membermedical.setNric(preferences.getString(getString(R.string.pref_signup_nric_number), ""));
        membermedical.setNric_name(preferences.getString(getString(R.string.pref_signup_name), ""));
        membermedical.setGender(preferences.getInt(getString(R.string.pref_signup_gender), -1));
        membermedical.setDob(preferences.getString(getString(R.string.pref_signup_dob), ""));
        membermedical.setBlock_no(preferences.getString(getString(R.string.pref_signup_blockno), ""));
        membermedical.setStreet(preferences.getString(getString(R.string.pref_signup_street), ""));
        membermedical.setBuilding_name(preferences.getString(getString(R.string.pref_signup_building), ""));
        membermedical.setUnit_no(preferences.getString(getString(R.string.pref_signup_unitno), ""));
        membermedical.setPostal_code(preferences.getString(getString(R.string.pref_signup_postalcode), ""));
        membermedical.setTel1(preferences.getString(getString(R.string.pref_signup_mobile), "0"));
        membermedical.setTel1_code(preferences.getInt(getString(R.string.pref_signup_mobile_code), 0));
        membermedical.setTel1_iso(preferences.getString(getString(R.string.pref_signup_mobile_iso), ""));
        membermedical.setEmail(preferences.getString(getString(R.string.pref_signup_email), ""));
        membermedical.setMemberid(preferences.getString(getString(R.string.pref_login_memberId), ""));
        membermedical.setNationality(preferences.getString(getString(R.string.pref_signup_nationalty),""));
        membermedical.setAddress1(preferences.getString(getString(R.string.pref_signup_address1),""));
        membermedical.setAddress2(preferences.getString(getString(R.string.pref_signup_address2),""));
        membermedical.setAddress3(preferences.getString(getString(R.string.pref_signup_address3),""));
        membermedical.setAddress4(preferences.getString(getString(R.string.pref_signup_address4),""));

        membermedical.setRelation(1);
        membermedical.setMarried_staus(preferences.getInt(getString(R.string.pref_signup_marrital_status), -1));
        if (dbmedprofile.existmemberprofile(membermedical.getMemberid()) < 1) {
            membermedical.setMember(1);
            membermedical.setFlag_upload(0);
        }
        dbmedprofile.updateMedicalProfile(membermedical);
    }

    public boolean validateallData() {
        Boolean hasError = false;
        String password = preferences.getString(getString(R.string.pref_signup_password), "");
        String nric = preferences.getString(getString(R.string.pref_signup_nric_number), "");
        String name = preferences.getString(getString(R.string.pref_signup_name), "");
        String dob = preferences.getString(getString(R.string.pref_signup_dob), "");
        String telph = preferences.getString(getString(R.string.pref_signup_mobile), "");
        Log.d("teltesttest", telph + "no");
        int integernricType = preferences.getInt(getString(R.string.pref_signup_nric_type), -1);
        //   String email = preferences.getString(getString(R.string.pref_signup_email), "");

        if (nric.equals("")) {
            //editText_nric.setError("Required");
            hasError = true;
        }
        if (integernricType > 0 && integernricType <4) {
            String nstrtype = nric_type[integernricType];
            if (!Utils.isValidNRIC(nric, nstrtype)) {
                //editText_nric.setError("Invalid NRIC");
                hasError = true;
            }
        }
        if (integernricType < 0) {
            hasError = true;
        }

        if (preferences.getInt(getString(R.string.pref_signup_nric_type), 0) > 0 &&
                preferences.getInt(getString(R.string.pref_signup_nric_type),0)<4) {
            String nstrtype = nric_type[preferences.getInt(getString(R.string.pref_signup_nric_type),0)];
            if (!Utils.isValidNRIC(nric,nstrtype)) {
                //editText_nric.setError("Invalid");
                hasError = true;
            }
        }
        if (name.equals("")) {
            //editText_name.setError("Required");
            hasError = true;
        }
        if (dob == "") {
            hasError = true;
        }
        if (telph.length() < 8) {
            //editText_Mobile.setError("Invalid Mobile");
            hasError = true;
        }
        if (password.length() < 5) {
            editext_Password.setError("Password is too short");
            hasError = true;
        }
        /*if (email.equals("")) {
            TextViewEmail.setError("Required");
            hasError = true;
        }*/
        return hasError;
    }

    //region OTP request Verify Dialog
    public void Dialog(String title, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder
                .setTitle(title)
                .setMessage(Html.fromHtml(message))
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(getContext(), ActivitySetting.class);
                        getActivity().startActivity(intent);
                        getActivity().finish();
                    }
                })
                .setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                /*Intent intent = new Intent(getContext(), ActivitySMSToken.class);
                                intent.putExtra("from", "ActivitySignUp");
                                getActivity().startActivity(intent);
                                getActivity().finish();*/

                                FragSingupFour fragSingupFour = (FragSingupFour) getFragmentManager().findFragmentByTag("FragSingupFour");
                                if (fragSingupFour == null)
                                {
                                    fragSingupFour = new FragSingupFour();
                                }
                                fragSingupFour.receiveFrom("ActivitySignUp");
                                getFragmentManager().beginTransaction().replace(R.id.frag_signup_holder, fragSingupFour, "FragSingupFour").commit();
                                Log.d("fragthreepreferences", preferences.getString("signup_pwd", ""));
                                            }
                                        }
                                ).show();
    }
    //endregion

    //region showMessageDialog()
    private void showMessageDialog(String title, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        alertBuilder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }


    //region Terms_conditions()
    public void gotoTerms_Conditions(String link) {
        Intent intent = new Intent(getContext(), ActivityTNCWebView.class);
        intent.putExtra("url", link);
        intent.putExtra("from_activity", "ActivitySignup");
        startActivity(intent);
        getActivity().finish();
    }

    private void setUnderLineText() {
        txt_terms_and_conditions.setPaintFlags(txt_terms_and_conditions.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_privacy_policies.setPaintFlags(txt_privacy_policies.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }



}
