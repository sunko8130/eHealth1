package sg.com.ehealthassist.ehealthassist.Account;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestJsonRegister;
import sg.com.ehealthassist.ehealthassist.API.Request.RequestPcodejson;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyCountryCode;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyPCodeAddress;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyRegisterMember;
import sg.com.ehealthassist.ehealthassist.CountryCode.CountryCodeAdapter;
import sg.com.ehealthassist.ehealthassist.CustomUI.FixedHoloDatePickerDialog;
import sg.com.ehealthassist.ehealthassist.CustomUI.NothingSelectedSpinnerAdapter;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Fragment.FragSingupOne;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Profile.CountryCode;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Privacy.ActivityTNCWebView;
import sg.com.ehealthassist.ehealthassist.R;
import sg.com.ehealthassist.ehealthassist.ActivitySetting;

public class ActivitySingup extends AppCompatActivity {

    FragSingupOne fragSingupOne;
    ImageButton toolbar_imgback_button;

    /*SharedPreferences preferences = null;
    String fromactivity = "", mEmail = "", mMobile = "", mImei = "", mGcmid = "",
            mPassword = "",mMobileISO="";int mMobileCode=0;
    TextView main_toolbar_title, txtlink, txtprivacy, txtlink_con;
    ImageButton toolbar_imgback_button, img_postal_search;
    EditText editText_nric, editText_name, edtdob, editText_Mobile, TextViewEmail, Textviewpostalcode,
            editTextBlockNo, editTextStreet, editTextBuildingName, editTextUnitNo, editext_Password,
            edt_address1,edt_address2,edt_address3,edt_address4;
    ImageView imgdobright_arrow;
    Spinner spinner_nric, spinner_married, spinner_gender,spinner_nationalty;
    Button btn_signup,btn_dropdowncCode,btn_countrycode;
    CheckBox cbAgree;
    Context _mcontext;
    DBMedProfile dbmedprofile;
    String[] nric_type,nric_type_passport, marrital_status,arr_nationality;
    ArrayAdapter<CharSequence> adapterMarried, adapterNricType, adapterGender,adapterNationality;
    LinearLayout linearLayout,llmultiaddressView;
    RelativeLayout rldoblayout,rlpostalcode;
    String SENDER_ID = "", regid;
    static final String TAG = "GCM";
    GoogleCloudMessaging gcm;
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    ArrayList<CountryCode> lst_countrycode = new ArrayList<CountryCode>();
    ListView lvcountrycode;CountryCodeAdapter adpt_countrycode;
    Context themedContext;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_singup);

        toolbar_imgback_button = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        TextView main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(R.string.title_activity_signup);

        toolbar_imgback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySingup.this, ActivityCreateAccount.class);
                intent.putExtra("from", "singup");
                startActivity(intent);
                finish();
            }
        });

        callFragSingupOne();

        /*SENDER_ID = getResources().getString(R.string.gcm_sender_id);
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        themedContext = new ContextThemeWrapper(this, android.R.style.Theme_Holo_Light_Dialog);
        dbmedprofile = new DBMedProfile(this);
        _mcontext = this;
        Intent i = getIntent();
        fromactivity = i.getStringExtra("fromactivity");
        getRegisterId();
        callvolleyCountryCode();
        findViewById();
        setEvent();
        setControlsEvent();

        LoadData();
        LoadSignupPreference();*/

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ActivitySingup.this, ActivityCreateAccount.class);
        intent.putExtra("from", "singup");
        startActivity(intent);
        finish();
        //super.onBackPressed();
    }

    private void callFragSingupOne() {
        fragSingupOne = (FragSingupOne) getSupportFragmentManager().findFragmentByTag("FragSingupOne");
        if (fragSingupOne == null)
        {
            fragSingupOne = new FragSingupOne();
        }
        getSupportFragmentManager().beginTransaction().add(R.id.frag_signup_holder, fragSingupOne, "FragSingupOne").commit();
    }

   /* //region FindViewById
    public void findViewById() {
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(R.string.title_activity_signup);
        toolbar_imgback_button = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        linearLayout = (LinearLayout) findViewById(R.id.llContactView);
        llmultiaddressView =(LinearLayout)findViewById(R.id.llmultiaddressView);
        rldoblayout = (RelativeLayout) findViewById(R.id.rldoblayout);
        rlpostalcode = (RelativeLayout)findViewById(R.id.rlpostalcode);
        editText_nric = (EditText) findViewById(R.id.editText_nric);
        editText_name = (EditText) findViewById(R.id.editText_name);
        edtdob = (EditText) findViewById(R.id.edtdob);
        editText_Mobile = (EditText) findViewById(R.id.editText_Mobile);
        TextViewEmail = (EditText) findViewById(R.id.TextViewEmail);
        Textviewpostalcode = (EditText) findViewById(R.id.Textviewpostalcode);
        editTextBlockNo = (EditText) findViewById(R.id.editTextBlockNo);
        editTextStreet = (EditText) findViewById(R.id.editTextStreet);
        editTextBuildingName = (EditText) findViewById(R.id.editTextBuildingName);
        editTextUnitNo = (EditText) findViewById(R.id.editTextUnitNo);
        editext_Password = (EditText) findViewById(R.id.editext_Password);
        imgdobright_arrow = (ImageView) findViewById(R.id.imgdobright_arrow);
        spinner_nric = (Spinner) findViewById(R.id.spinner_nric);
        spinner_married = (Spinner) findViewById(R.id.spinner_married);
        spinner_gender = (Spinner) findViewById(R.id.spinner_gender);
        btn_signup = (Button) findViewById(R.id.btn_signup);
        cbAgree = (CheckBox) findViewById(R.id.CheckBoxAgree);
        txtlink = (TextView) findViewById(R.id.txtlink);
        txtlink_con = (TextView) findViewById(R.id.txtlink_con);
        txtprivacy = (TextView) findViewById(R.id.txtprivacy);
        img_postal_search = (ImageButton) findViewById(R.id.img_postal_search);
        editText_name.setFilters(new InputFilter[]{new InputFilter.LengthFilter(66), new InputFilter.AllCaps()});
        editText_nric.setFilters(new InputFilter[]
                {new InputFilter.LengthFilter(15), new InputFilter.AllCaps()});


        nric_type = getResources().getStringArray(R.array.array_nric_type);
        nric_type_passport= getResources().getStringArray(R.array.array_nric_type_passport);
        spinnerNricTypeLoad(R.array.array_nric_type);

        adapterMarried = ArrayAdapter.createFromResource(this, R.array.array_marriedStaus, R.layout.custom_spinner_style);
        adapterMarried.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marrital_status = getResources().getStringArray(R.array.array_marriedStaus);
        spinner_nationalty = (Spinner) findViewById(R.id.spinner_nationalty);
        arr_nationality = getResources().getStringArray( R.array.country_code);
        adapterNationality = ArrayAdapter.createFromResource(this, R.array.country_code, R.layout.custom_spinner_style);
        adapterNationality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_nationalty.setAdapter(new NothingSelectedSpinnerAdapter(adapterNationality, R.layout.signup_spinner_row_nothing_selected, this));
        spinner_married.setAdapter(new NothingSelectedSpinnerAdapter(adapterMarried, R.layout.signup_spinner_row_nothing_selected, this));
        adapterGender = ArrayAdapter.createFromResource(this, R.array.array_gender, R.layout.custom_spinner_style);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(new NothingSelectedSpinnerAdapter(adapterGender, R.layout.signup_spinner_row_nothing_selected, this));
        btn_dropdowncCode = (Button)findViewById(R.id.btn_dropdownccode);
        btn_countrycode = (Button)findViewById(R.id.btn_countrycode);
        edt_address1 = (EditText)findViewById(R.id.edt_address1);
        edt_address2 = (EditText)findViewById(R.id.edt_address2);
        edt_address3 = (EditText)findViewById(R.id.edt_address3);
        edt_address4 = (EditText)findViewById(R.id.edt_address4);
    }
    //endregion

    public void spinnerNricTypeLoad(int nric_type){
        adapterNricType = ArrayAdapter.createFromResource(this, nric_type, R.layout.custom_spinner_style);
        adapterNricType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_nric.setAdapter(new NothingSelectedSpinnerAdapter(adapterNricType, R.layout.signup_spinner_row_nothing_selected, this));
    }

    //region Event()
    public void setEvent() {
        toolbar_imgback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });
        edtdob.setOnClickListener(datetimepickerOnClickListener);
        rldoblayout.setOnClickListener(datetimepickerOnClickListener);
        imgdobright_arrow.setOnClickListener(datetimepickerOnClickListener);
        editText_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String controlKey = getString(R.string.pref_signup_name);
                String controlValue = editText_name.getText().toString();
                preferences.edit().putString(controlKey, controlValue).apply();
                editText_name.setError(null);
            }
        });
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
        editText_nric.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String controlValue = editText_nric.getText().toString();
                String controlKey = getString(R.string.pref_signup_nric_number);
                preferences.edit().putString(controlKey, controlValue).apply();
            }
        });
        editText_Mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String controlKey = getString(R.string.pref_signup_mobile);
                String controlValue = editText_Mobile.getText().toString();
                if (!Utils.isValidSGMobile(controlValue) && controlValue.length() > 0) {

                } else {
                    preferences.edit().putString(controlKey, controlValue).apply();
                }
            }
        });
        spinner_married.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    preferences.edit().putInt(getString(R.string.pref_signup_marrital_status), position - 1)
                            .apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                preferences.edit().putInt(getString(R.string.pref_signup_marrital_status), -1).apply();
            }
        });
        spinner_nric.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String controlKey = getString(R.string.pref_signup_nric_type);
                if (position > 0) {
                    preferences.edit().putInt(controlKey, position - 1)
                            .apply();
                    if (position > 1 && position!=5) {
                        if (!editText_nric.getText().toString().equals("")) {
                            if (!Utils.isValidNRIC(editText_nric.getText().toString(), nric_type[position - 1])) {
                                editText_nric.setError(getString(R.string.err_invalid_nric));
                            } else {
                                editText_nric.setError(null);
                            }
                        }
                    } else {
                        editText_nric.setError(null);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                preferences.edit().putInt(getString(R.string.pref_signup_nric_type), -1).apply();
            }
        });
        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    preferences.edit().putInt(getString(R.string.pref_signup_gender), position - 1)
                            .apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                preferences.edit().putInt(getString(R.string.pref_signup_gender), -1).apply();
            }
        });

        spinner_nationalty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    preferences.edit().putString(getString(R.string.pref_signup_nationalty),arr_nationality[position-1])
                            .putInt(getString(R.string.pref_signup_integer_nationalty),position-1).apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                preferences.edit()
                        .putInt(getString(R.string.pref_signup_integer_nationalty), -1)
                        .apply();
            }
        });
    }

    public void setControlsEvent() {

        btn_dropdowncCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCountryCode();
            }
        });

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //region Create Register
                if (!validateallData()) if (cbAgree.isChecked()) {
                    mEmail = TextViewEmail.getText().toString();
                    mPassword = preferences.getString(getString(R.string.pref_signup_password), "");
                    mImei = Utils.getImei(_mcontext);
                    mMobile = preferences.getString(getString(R.string.pref_signup_mobile), "");
                    mMobileCode = preferences.getInt(getString(R.string.pref_signup_mobile_code), 0);
                    mMobileISO = preferences.getString(getString(R.string.pref_signup_mobile_iso), "");
                    mGcmid = preferences.getString(getString(R.string.pref_gcm_property_reg_id), "");
                    String nric = editText_nric.getText().toString();
                    // ******* Call To API **********
                    if (Utils.hasInternetConnection(_mcontext)) {
                        RequestJsonRegister newuser = new RequestJsonRegister(mEmail, mMobileCode + mMobile, mMobileISO, mPassword, mGcmid, 1, mImei,nric);
                        String json = newuser.ObjecttoJson(newuser);
                        Log.e("register ",json);
                        VolleyRegisterMember v_register = new VolleyRegisterMember(ActivitySingup.this, newuser);
                        v_register.GetRegisterJson(newuser.StringtoJsonObject(json), new VolleyRegisterMember.VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject data_json) {
                                try {
                                    String memberid = data_json.getString(Constant.NODE_MemberId);
                                    String accesstoken = data_json.getString(Constant.NODE_AccessToken);
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

                                    Dialog(getString(R.string.sign_verification),
                                            getString(R.string.sign_verification_message1) + " " + "<font color=red><b>" + mMobileCode + mMobile + "</b></font>"
                                                    + " " + getString(R.string.sign_verification_message2));
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }

                            @Override
                            public void onFail(String errorcode, String errormessage) {
                                Utils.showAlertDialog(_mcontext, errorcode, errormessage);
                            }
                        });

                    } else {
                        Utils.showInternetRequiredDialog(_mcontext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                        return;
                    }
                } else {
                    showMessageDialog("Agreement", getString(R.string.msgbox_signup_agree_to_continue));
                    return;
                }
                else{
                    Utils.showAlertDialog(_mcontext,"Alerts",getString(R.string.alerts_profile));
                }
                //endregion
            }

        });

        //region postalcode textview action listener
        Textviewpostalcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_GO) {
                    getAddress();
                }
                return false;
            }

        });
        //endregion

        //region image postal search action listener
        img_postal_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddress();
            }
        });
        //endregion

        //region view Address Text onFocus Change Listener
        View.OnFocusChangeListener txtFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String controlKey = null;
                String controlValue = null;
                boolean isValid = true;
                switch (v.getId()) {
                    case R.id.editTextBlockNo:
                        controlKey = getString(R.string.pref_signup_blockno);
                        controlValue = editTextBlockNo.getText().toString();
                        break;
                    case R.id.editTextStreet:
                        controlKey = getString(R.string.pref_signup_street);
                        controlValue = editTextStreet.getText().toString();
                        break;
                    case R.id.editTextBuildingName:
                        controlKey = getString(R.string.pref_signup_building);
                        controlValue = editTextBuildingName.getText().toString();
                        break;
                    case R.id.editTextUnitNo:
                        controlKey = getString(R.string.pref_signup_unitno);
                        controlValue = editTextUnitNo.getText().toString();
                        break;
                    case R.id.edt_address1 :
                        controlKey = getString(R.string.pref_signup_address1);
                        controlValue = edt_address1.getText().toString();
                        break;
                    case R.id.edt_address2 :
                        controlKey = getString(R.string.pref_signup_address2);
                        controlValue = edt_address2.getText().toString();
                        break;
                    case R.id.edt_address3 :
                        controlKey = getString(R.string.pref_signup_address3);
                        controlValue = edt_address3.getText().toString();
                        break;
                    case R.id.edt_address4 :
                        controlKey = getString(R.string.pref_signup_address4);
                        controlValue = edt_address4.getText().toString();
                        break;
                    case R.id.TextViewEmail:
                        controlKey = getString(R.string.pref_signup_email);
                        controlValue = TextViewEmail.getText().toString();
                        if (!controlValue.equals("")) {
                            if (!Utils.isValidEmail(controlValue)) {
                                isValid = false;
                                TextViewEmail.setError(getString(R.string.err_invalid_email));
                            }
                        }
                        break;
                }
                if (!hasFocus && isValid) {
                    preferences.edit().putString(controlKey, controlValue).commit();
                }
            }
        };
        //endregion

        //region TextChange Lisetner
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = linearLayout.getChildAt(i);
            if (v instanceof EditText) {
                final EditText txt = (EditText) v;
                txt.setOnFocusChangeListener(txtFocusChangeListener);
            }
        }
        int count_address = llmultiaddressView.getChildCount();
        for(int i =0 ;i<count_address;i++){
            View v = llmultiaddressView.getChildAt(i);
            if(v instanceof EditText){
                final  EditText edt = (EditText) v;
                edt.setOnFocusChangeListener(txtFocusChangeListener);
            }
        }
        TextViewEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String controlKey = getString(R.string.pref_signup_email);
                String controlValue = TextViewEmail.getText().toString();
               // if (!controlValue.equals("")) {
                    if (!Utils.isValidEmail(controlValue)) {

                    } else {
                        preferences.edit().putString(controlKey, controlValue).commit();
                    }
                //}
            }
        });
        Textviewpostalcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                img_postal_search.setVisibility(View.VISIBLE);
                String controlKey = getString(R.string.pref_signup_postalcode);
                String controlValue = Textviewpostalcode.getText().toString();
                preferences.edit().putString(controlKey, controlValue).commit();
            }
        });
        txtlink.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gotoTerms_Conditions("tnc");
                return false;
            }
        });
        txtlink_con.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                gotoTerms_Conditions("tnc");
                return false;
            }
        });
        txtprivacy.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                gotoTerms_Conditions("privacy");
                return false;
            }
        });
        //endregion
    }
    //endregion

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
        int integernricType = preferences.getInt(getString(R.string.pref_signup_nric_type), -1);
     //   String email = preferences.getString(getString(R.string.pref_signup_email), "");

        if (nric.equals("")) {
            editText_nric.setError("Required");
            hasError = true;
        }
        if (integernricType > 0 && integernricType <4) {
            String nstrtype = nric_type[integernricType];
            if (!Utils.isValidNRIC(nric, nstrtype)) {
                editText_nric.setError("Invalid NRIC");
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
                editText_nric.setError("Invalid");
                hasError = true;
            }
        }
        if (name.equals("")) {
            editText_name.setError("Required");
            hasError = true;
        }
        if (dob == "") {
            hasError = true;
        }
        if (telph.length() < 8) {
            editText_Mobile.setError("Invalid Mobile");
            hasError = true;
        }
        if (password.length() < 5) {
            editext_Password.setError("Password is too short");
            hasError = true;
        }
        if (email.equals("")) {
            TextViewEmail.setError("Required");
            hasError = true;
        }
        return hasError;
    }

    public void LoadData() {
        String name = preferences.getString(getString(R.string.pref_signup_name), "");
        String nric_number = preferences.getString(getString(R.string.pref_signup_nric_number), "");
        String dob = preferences.getString(getString(R.string.pref_signup_dob), "");
        String phone = preferences.getString(getString(R.string.pref_signup_mobile), "");
        int phone_code = preferences.getInt(getString(R.string.pref_signup_mobile_code),0);
        String phone_iso = preferences.getString(getString(R.string.pref_signup_mobile_iso),"");
        String pwd = preferences.getString(getString(R.string.pref_signup_password), "");
        int married_status = preferences.getInt(getString(R.string.pref_signup_marrital_status), -1);
        int nricType = preferences.getInt(getString(R.string.pref_signup_nric_type), -1);
        int sex = preferences.getInt(getString(R.string.pref_signup_gender), -1);
        String nation = preferences.getString(getString(R.string.pref_signup_nationalty),"");
        edtdob.setText(dob);
        editText_Mobile.setText(phone);
        btn_countrycode.setText("+"+ phone_code);
        btn_dropdowncCode.setText(phone_iso);
        editText_nric.setText(nric_number);
        editText_name.setText(name);
        editext_Password.setText(pwd);

        if (nricType != -1) {
            int position = nricType;
            if(position==4){
                spinnerNricTypeLoad(R.array.array_nric_type_passport);
            }else{
                spinnerNricTypeLoad(R.array.array_nric_type);
            }
            spinner_nric.setSelection(position + 1);
        }

        if (married_status != -1) {
            int position = married_status;
            spinner_married.setSelection(position + 1);
        }
        if (sex != -1) {
            int postion = sex;
            spinner_gender.setSelection(postion + 1);
        }else{
           spinner_gender.setSelection(2);
            preferences.edit()
                    .putInt(getString(R.string.pref_mp_sex), 2)
                    .apply();
        }
        if (dob.equals("")) {
            edtdob.setText("Unknown");
            edtdob.setTextColor(getResources().getColor(R.color.colorblack));
        } else {
            edtdob.setTextColor(getResources().getColor(R.color.colorlightblue));
        }
        if(!nation.equals("")){
            int spinnerPosition = adapterNationality.getPosition(nation);
            spinner_nationalty.setSelection(spinnerPosition+1);
        }
        else{
            int spinnerPosition = adapterNationality.getPosition(_mcontext.getResources().getString(R.string.defaultnational));
            spinner_nationalty.setSelection(spinnerPosition+1);

            // spinner_nationalty.setSelection(177);
            preferences.edit().putString(getString(R.string.pref_signup_nationalty),arr_nationality[spinnerPosition+1])
                    .putInt(getString(R.string.pref_signup_integer_nationalty), spinnerPosition)
                    .apply();
        }
        if(phone_iso.equals(getResources().getString(R.string.default_countryisocode)) || phone_iso.equals("")){
            linearLayout.setVisibility(View.VISIBLE);
            rlpostalcode.setVisibility(View.VISIBLE);
            llmultiaddressView.setVisibility(View.GONE);
            spinner_nric.setEnabled(true);
            editText_nric.setHint(getResources().getString(R.string.input_nric));
        }
        else{
            llmultiaddressView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            rlpostalcode.setVisibility(View.GONE);
            spinner_nric.setEnabled(false);
            editText_nric.setHint(getResources().getString(R.string.passportno));
        }
        // Log.e("name" , "++" + phone_iso + "," + btn_dropdowncCode.getText().toString());
    }

    public void LoadSignupPreference() {
        String blockno = preferences.getString(getString(R.string.pref_signup_blockno), "");
        String unitno = preferences.getString(getString(R.string.pref_signup_unitno), "");
        String street = preferences.getString(getString(R.string.pref_signup_street), "");
        String building = preferences.getString(getString(R.string.pref_signup_building), "");
        String email = preferences.getString(getString(R.string.pref_signup_email), "");
        String postalcode = preferences.getString(getString(R.string.pref_signup_postalcode), "");
        String address1 = preferences.getString(getString(R.string.pref_signup_address1),"");
        String address2 = preferences.getString(getString(R.string.pref_signup_address2),"");
        String address3 = preferences.getString(getString(R.string.pref_signup_address3),"");
        String address4 = preferences.getString(getString(R.string.pref_signup_address4),"");

        Textviewpostalcode.setText(postalcode);
        editTextUnitNo.setText(unitno);
        editTextStreet.setText(street);
        editTextBlockNo.setText(blockno);
        editTextBuildingName.setText(building);
        TextViewEmail.setText(email);
        cbAgree.setChecked(preferences.getBoolean(getString(R.string.pref_signup_cbagree), false));
        edt_address1.setText(address1);
        edt_address2.setText(address2);
        edt_address3.setText(address3);
        edt_address4.setText(address4);
    }
    //endregion

    //region Postal Code API
    public void getAddress() {
        String pcode_str = Textviewpostalcode.getText().toString();
        if (pcode_str.length() == 6) {
            img_postal_search.setVisibility(View.VISIBLE);
            RequestPcodejson pcode_json = new RequestPcodejson(pcode_str);
            JSONObject json = pcode_json.StringtoJsonObject(pcode_json.objecttoJson());
            if (Utils.hasInternetConnection(getApplicationContext())) {
                VolleyPCodeAddress v_pcode = new VolleyPCodeAddress(this);
                v_pcode.GetPCodeAddressRequest(json, new VolleyPCodeAddress.VolleyCallback() {
                    @Override
                    public void onSuccess(String blockno, String street, String building) {
                        editTextBlockNo.setText(blockno);
                        editTextStreet.setText(street);
                        editTextBuildingName.setText(building);
                        editTextBlockNo.setError(null);
                        editTextStreet.setError(null);
                        editTextBuildingName.setError(null);
                        preferences.edit().putString(getString(R.string.pref_signup_blockno), blockno)
                                .putString(getString(R.string.pref_signup_street), street)
                                .putString(getString(R.string.pref_signup_building), building)
                                .apply();
                    }
                    @Override
                    public void onFail(String errorcode, String errormessage) {
                        Utils.showAlertDialog(_mcontext, errorcode, errormessage);
                    }
                });
            } else {
                Utils.showInternetRequiredDialog(this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                return;
            }
        } else {
            Textviewpostalcode.setError("Required");
            img_postal_search.setVisibility(View.GONE);
        }
    }
    //endregion

    //region OTP request Verify Dialog
    public void Dialog(String title, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder
                .setTitle(title)
                .setMessage(Html.fromHtml(message))
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(_mcontext, ActivitySetting.class);
                        _mcontext.startActivity(intent);
                        finish();
                    }
                })
                .setPositiveButton("Verify", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(_mcontext, ActivitySMSToken.class);
                                intent.putExtra("from", "ActivitySignUp");
                                _mcontext.startActivity(intent);
                                finish();
                            }
                        }
                ).show();
    }
    //endregion

    //region Datetiem picker
    View.OnClickListener datetimepickerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String dob = preferences.getString(getString(R.string.pref_signup_dob), "");
            Calendar newCalendar = Calendar.getInstance();
            if (dob.equals("")) {
                newCalendar.set(Calendar.YEAR, 1980);
            } else {
                Date date = Utils.reminderTimeFormat(dob, "dd-MMM-yyyy", "dd-MMM-yyyy");
                newCalendar.set(Calendar.YEAR, date.getYear() + 1900);
                newCalendar.set(Calendar.MONTH, date.getMonth());
                newCalendar.set(Calendar.DATE, date.getDate());
            }

            DatePickerDialog dialog = new DatePickerDialog(ActivitySingup.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                            edtdob.setText(simpleDateFormat.format(newDate.getTime()));
                            edtdob.setTextColor(getResources().getColor(R.color.colorlightblue));
                            //  rderror_dob.setVisibility(View.GONE);
                            preferences.edit().putString(getString(R.string.pref_signup_dob), edtdob.getText().toString()).apply();
                        }
                    },
                    newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setTitle("Date of Birth");
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog.show();


            final DatePickerDialog dialog = new FixedHoloDatePickerDialog(
                    themedContext,
                    new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                            edtdob.setText(simpleDateFormat.format(newDate.getTime()));
                            edtdob.setTextColor(getResources().getColor(R.color.colorlightblue));
                            //  rderror_dob.setVisibility(View.GONE);
                            preferences.edit().putString(getString(R.string.pref_signup_dob), edtdob.getText().toString()).apply();
                        }
                    },
                    newCalendar.get(java.util.Calendar.YEAR),
                    newCalendar.get(java.util.Calendar.MONTH),
                    newCalendar.get(java.util.Calendar.DAY_OF_MONTH)
            );

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setTitle("Date of Birth");
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog.show();

        }
    };

    //endregion

    //region showMessageDialog()
    private void showMessageDialog(String title, String message) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    //endregion

    //region Country Code
   *//* public void DialogCountryCode(){
        final Dialog dialog = new Dialog(ActivitySingup.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogcountrycode);

        lvcountrycode = (ListView) dialog.findViewById(R.id.lvcountrycode);
        LoadCountryCode();
        lvcountrycode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CountryCode code = lst_countrycode.get(position);
                btn_dropdowncCode.setText(code.getISOCode());
                btn_countrycode.setText("+" +code.getCountryCode());

                if(code.getISOCode().equals(getResources().getString(R.string.default_countryisocode))){
                    linearLayout.setVisibility(View.VISIBLE);
                    rlpostalcode.setVisibility(View.VISIBLE);
                    llmultiaddressView.setVisibility(View.GONE);
                    spinner_nric.setEnabled(true);
                    editText_nric.setHint(getResources().getString(R.string.input_nric));
                    spinner_nric.setSelection(1);

                    preferences.edit()
                            .putInt(getString(R.string.pref_signup_nric_type),0)
                            //  .putString(getString(R.string.pref_signup_nationalty),code.getCountryName())
                            .apply();
                    //String nation = preferences.getString(getString(R.string.pref_signup_nationalty),"");
                    if(!code.getCountryName().equals("")){
                        int spinnerPosition = adapterNationality.getPosition(code.getCountryName());
                        spinner_nationalty.setSelection(spinnerPosition+1);
                    }
                }
                else{
                    spinner_nric.setSelection(5);
                    llmultiaddressView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    rlpostalcode.setVisibility(View.GONE);
                    spinner_nric.setEnabled(false);
                    editText_nric.setHint(getResources().getString(R.string.passportno));
                    preferences.edit()
                            .putInt(getString(R.string.pref_signup_nric_type),4)
                            //  .putString(getString(R.string.pref_signup_nationalty),code.getCountryName())
                            .apply();

                    if(!code.getCountryName().equals("")){
                        int spinnerPosition = adapterNationality.getPosition(code.getCountryName());
                        spinner_nationalty.setSelection(spinnerPosition+1);
                    }
                }
                preferences.edit()
                        .putString(getString(R.string.pref_signup_mobile_iso),code.getISOCode())
                        .putInt(getString(R.string.pref_signup_mobile_code),code.getCountryCode())
                        .putString(getString(R.string.pref_signup_nationalty),code.getCountryName())
                        .apply();

                if(!code.getCountryName().equals("")){
                    int spinnerPosition = adapterNationality.getPosition(code.getCountryName());
                    spinner_nationalty.setSelection(spinnerPosition+1);
                }
                Log.e("isocode_code",code.getISOCode() + "," + code.getCountryCode());

                dialog.dismiss();
            }
        });
        dialog.show();
    }*//*
    public void DialogCountryCode(){
        final Dialog dialog = new Dialog(ActivitySingup.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogcountrycode);

        lvcountrycode = (ListView) dialog.findViewById(R.id.lvcountrycode);

        LoadCountryCode();

        lvcountrycode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CountryCode code = lst_countrycode.get(position);
                btn_dropdowncCode.setText(code.getISOCode());
                btn_countrycode.setText("+" +code.getCountryCode());

                if(code.getISOCode().equals(getResources().getString(R.string.default_countryisocode))){
                    linearLayout.setVisibility(View.VISIBLE);
                    rlpostalcode.setVisibility(View.VISIBLE);
                    llmultiaddressView.setVisibility(View.GONE);
                    spinnerNricTypeLoad(R.array.array_nric_type);
                    spinner_nric.setEnabled(true);
                    editText_nric.setHint(getResources().getString(R.string.input_nric));
                    spinner_nric.setSelection(1);

                    preferences.edit()
                            .putInt(getString(R.string.pref_signup_nric_type),0)
                            .apply();
                }
                else{
                    spinnerNricTypeLoad(R.array.array_nric_type_passport);
                    spinner_nric.setSelection(5);
                    llmultiaddressView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    rlpostalcode.setVisibility(View.GONE);
                    spinner_nric.setEnabled(false);
                    editText_nric.setHint(getResources().getString(R.string.passportno));
                    preferences.edit()
                            .putInt(getString(R.string.pref_signup_nric_type),4)
                            .apply();
                }
                preferences.edit()
                        .putString(getString(R.string.pref_signup_mobile_iso),code.getISOCode())
                        .putInt(getString(R.string.pref_signup_mobile_code),code.getCountryCode())
                        .putString(getString(R.string.pref_signup_nationalty),code.getCountryName())
                        .apply();

               *//* if(!code.getCountryName().equals("")){
                    int spinnerPosition = adapterNationality.getPosition(code.getCountryName());
                    spinner_nationalty.setSelection(spinnerPosition+1);
                }
                Log.e("isocode_code",code.getISOCode() + "," + code.getCountryCode());*//*

                dialog.dismiss();
            }
        });
        dialog.show();
    }
    public void callvolleyCountryCode(){
        if (Utils.hasInternetConnection(_mcontext)) {
            VolleyCountryCode v_countryCode = new VolleyCountryCode(_mcontext);
            v_countryCode.GetCountryCodeJsonData(new VolleyCountryCode.VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<CountryCode> success) {
                    lst_countrycode = success;
                    if(lst_countrycode.size()>0){
                        String isocode = preferences.getString(getString(R.string.pref_signup_mobile_iso), "");
                        int phone_code = preferences.getInt(getString(R.string.pref_signup_mobile_code),0);
                        if(isocode.equals("")){
                            btn_dropdowncCode.setText(getResources().getString(R.string.default_countryisocode));
                            btn_countrycode.setText("+"+getResources().getString(R.string.default_countrycode));
                        }else{
                            btn_dropdowncCode.setText(isocode);
                            btn_countrycode.setText("+"+phone_code);
                        }
                        String code = btn_countrycode.getText().toString().substring(1);
                        preferences.edit()
                                .putString(getString(R.string.pref_signup_mobile_iso),btn_dropdowncCode.getText().toString())
                                .putInt(getString(R.string.pref_signup_mobile_code),Integer.parseInt(code)).apply();

                        Log.e("call api",btn_dropdowncCode.getText().toString()+ "," + code);
                    }

                }

                @Override
                public void onFail(String errorcode, String errormessage) {
                    btn_dropdowncCode.setText(getResources().getString(R.string.default_countryisocode));
                    btn_countrycode.setText("+"+getResources().getString(R.string.default_countrycode));
                }
            });
        }else{
            btn_dropdowncCode.setText(getResources().getString(R.string.default_countryisocode));
            btn_countrycode.setText("+"+getResources().getString(R.string.default_countrycode));
        }
    }
    public void LoadCountryCode(){
        adpt_countrycode = new CountryCodeAdapter(this, lst_countrycode);
        adpt_countrycode.notifyDataSetChanged();
        lvcountrycode.setAdapter(adpt_countrycode);
    }
    //endregion

    //region GCM Service()
    private void getRegisterId() {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(_mcontext);
            if (regid.isEmpty()) {
                registerInBackground();
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    private String getRegistrationId(Context context) {
        String registrationId = preferences.getString(getString(R.string.pref_gcm_property_reg_id), "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = preferences.getInt(getString(R.string.pref_gcm_appversion), Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(_mcontext);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = regid;
                    sendRegistrationIdToBackend();
                    storeRegistrationId(_mcontext, regid);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.e("GCM Register Id : ", msg);
            }
        }.execute(null, null, null);
    }

    private void sendRegistrationIdToBackend() {
    }

    private void storeRegistrationId(Context context, String regId) {
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        preferences.edit()
                .putString(getString(R.string.pref_gcm_property_reg_id), regId)
                .putInt(getString(R.string.pref_gcm_appversion), appVersion)
                .apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
    //endregion

    //region Terms_conditions()
    public void gotoTerms_Conditions(String link) {
        Intent intent = new Intent(this, ActivityTNCWebView.class);
        intent.putExtra("url", link);
        intent.putExtra("from_activity", "ActivitySignup");
        startActivity(intent);
        finish();
    }

    //endregion

    //region onKeyDown()
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void returnback() {
        String memberid = preferences.getString(getString(R.string.pref_login_memberId), "");
        Intent intent = null;
        switch (fromactivity) {
            case "ActivityCreateAccount":
                intent = new Intent(ActivitySingup.this, ActivityCreateAccount.class);
                break;
            case "ActivityLogIn":
                intent = new Intent(ActivitySingup.this, ActivityLogIn.class);
                intent.putExtra("from", "");
                intent.putExtra("CID", 0);
                break;
            default:
                intent = new Intent(ActivitySingup.this, ActivityCreateAccount.class);
                break;
        }
        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }*/

}
