package sg.com.ehealthassist.ehealthassist.Account;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestJsonAuth;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyAuth;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyAuthenticationService;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyCountryCode;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyLoginView;
import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.ActivitySetting;
import sg.com.ehealthassist.ehealthassist.Appointment.ActivityApptFlow;
import sg.com.ehealthassist.ehealthassist.CountryCode.CountryCodeAdapter;
import sg.com.ehealthassist.ehealthassist.Models.Profile.CountryCode;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Queue.ActivityQueueflow;
import sg.com.ehealthassist.ehealthassist.R;
import sg.com.ehealthassist.ehealthassist.eDocument.ActivityEDocument;

public class ActivityLogIn extends AppCompatActivity {
    SharedPreferences preferences = null;
    EditText editTextLogInMobileId, editTextLogInPassword;
    RelativeLayout rllogin, rlforgotPwd, rlnewaccount;
    TextView main_toolbar_title, txtforgotpwd, txt_buttonLogin, txt_btnNewSignUp;
    ImageButton toolbar_imgback_button;
    Button btn_logindropdownccode, btn_logincountrycode;
    String from_activity = "";
    Context mContext = null;
    int clinic_id = 0;int position = 0;
    public static TextView tverrormsg;
    public static Activity activity;
    String SENDER_ID = "", hedcode = "", clinic_name = "", appt_mode = "", regid;
    static final String TAG = "GCM";
    GoogleCloudMessaging gcm;
    ListView lvcountrycode;
    CountryCodeAdapter adpt_countrycode;
    ArrayList<CountryCode> lst_countrycode = new ArrayList<CountryCode>();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private ProgressDialog pDialog;
    String nationality = "";

    //@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_log_in);
        //windowTransition();
        mContext = this;
        activity = this;
        SENDER_ID = getResources().getString(R.string.gcm_sender_id);
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        Intent i_from = getIntent();
        from_activity = i_from.getStringExtra("from");
        clinic_id = i_from.getIntExtra("CID", 0);
        if (from_activity.equals("Queueflow")) {
            hedcode = i_from.getStringExtra("hecode");
            clinic_name = i_from.getStringExtra("clinic_name");
        } else if (from_activity.equals("Apptflow")) {
            clinic_name = i_from.getStringExtra("clinic_name");
            appt_mode = i_from.getStringExtra("appt_mode");
        }

        callvolleyCountryCode();
        getRegisterId();
        findViewsById();
        setEvent();
    }

    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return super.onSupportNavigateUp();
    }*/

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void windowTransition() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.setDuration(500);
        slide.setInterpolator(new LinearInterpolator());
        getWindow().setEnterTransition(slide);

        /*Fade fade = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            fade = new Fade();
            fade.setDuration(500);
            getWindow().setEnterTransition(fade);
        }*/

        //fade.excludeTarget(R.id.tvName, true);
    }

    //region findViewsById()
    private void findViewsById() {
        editTextLogInMobileId = (EditText) findViewById(R.id.editTextLogInMobileId);
        editTextLogInPassword = (EditText) findViewById(R.id.editTextLogInPassword);
        txt_buttonLogin = (TextView) findViewById(R.id.txt_buttonLogin);
        txt_btnNewSignUp = (TextView) findViewById(R.id.txt_btnNewSignUp);
        txtforgotpwd = (TextView) findViewById(R.id.txtforgotpwd);
        /*rllogin = (RelativeLayout) findViewById(R.id.rllogin);
        rlforgotPwd = (RelativeLayout) findViewById(R.id.rlforgotPwd);
        rlnewaccount = (RelativeLayout) findViewById(R.id.rlnewaccount);
        tverrormsg = (TextView) findViewById(R.id.tverrormsg);*/
        //tverrormsg.setVisibility(View.INVISIBLE);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(R.string.title_activity_login);
        toolbar_imgback_button = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        //btn_logincountrycode = (Button) findViewById(R.id.btn_logincountrycode);
        btn_logindropdownccode = (Button) findViewById(R.id.btn_logindropdownccode);
    }

    //endregion
    //region Event()
    public void setEvent() {
        //region Login
        View.OnClickListener rl_LoginOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean hasError = false;
                if (editTextLogInMobileId.length() <= 0) {
                    editTextLogInMobileId.setError(getText(R.string.err_invalid_pwd_length));
                    hasError = true;
                }
                if (editTextLogInPassword.length() <= 0) {
                    editTextLogInPassword.setError(getText(R.string.err_invalid_pwd_length));
                    hasError = true;
                }
                if (hasError) {
                    return;
                }
                showpDialog();
                final String phone_number = editTextLogInMobileId.getText().toString();
                final String password = editTextLogInPassword.getText().toString();
                final String gcmid = preferences.getString(getString(R.string.pref_gcm_property_reg_id), "");
                final String prefix = btn_logincountrycode.getText().toString().substring(1);
                if (Utils.hasInternetConnection(mContext)) {
                    try {
                        VolleyAuthenticationService jwt_token = new VolleyAuthenticationService(mContext);
                        jwt_token.GetStringRequest(prefix + phone_number, password, new VolleyAuthenticationService.VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject message) {
                                // ActivityLogIn.tverrormsg.setVisibility(View.GONE);
                                try {
                                    String acctesstoken = message.getString("access_token");
                                    String tokey_type = message.getString("token_type");
                                    String expires_in = message.getString("expires_in");
                                    String usertoken = tokey_type + " " + acctesstoken;
                                    preferences.edit().putBoolean(getString(R.string.pref_is_account_verified), true)
                                            .putString(getString(R.string.pref_login_Access_token), usertoken)
                                            .apply();

                                    RequestJsonAuth requestauth = new RequestJsonAuth(gcmid);
                                    JSONObject authjson = requestauth.StringtoJsonObject(requestauth.ObjectJsonforLoginView(gcmid));
                                    VolleyLoginView _vloginview = new VolleyLoginView(mContext,nationality);
                                    _vloginview.GetLoginViewJsonData(usertoken, authjson, new VolleyLoginView.VolleyCallback() {
                                        @Override
                                        public void onSuccess(String name,String nric,String nrictype,String dob) {
                                            Intent intent = new Intent(mContext, ActivityHome1.class);
                                            mContext.startActivity(intent);
                                            ActivityLogIn.activity.finish();
                                        }

                                        @Override
                                        public void onFail(String errorcode, String errormessage) {
                                            ActivityLogIn.tverrormsg.setText(errormessage);
                                            ActivityLogIn.tverrormsg.setVisibility(View.VISIBLE);
                                        }
                                    });
                                    hidepDialog();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                    hidepDialog();
                                }
                            }

                            @Override
                            public void onFail(String errorcode, String errormessage) {

                                RequestJsonAuth requestauth = new RequestJsonAuth(prefix + phone_number,password,gcmid);
                                JSONObject authjson = requestauth.StringtoJsonObject(requestauth.ObjecttoJson(requestauth));

                                VolleyAuth _vauth = new VolleyAuth(mContext,nationality);
                                _vauth.GetClinicJson(authjson, new VolleyAuth.VolleyCallback() {
                                    @Override
                                    public void onSuccess(String message) {
                                        hidepDialog();
                                        preferences.edit().putString(mContext.getString(R.string.pref_signup_password) ,password)
                                                .putString(mContext.getString(R.string.pref_main_user_data_hp_nationality) ,nationality)
                                                .apply();
                                        Intent intent = new Intent(mContext, ActivitySetting.class);
                                        mContext.startActivity(intent);
                                        ActivityLogIn.activity.finish();
                                    }

                                    @Override
                                    public void onFail(String errorcode, String errormessage) {
                                        hidepDialog();
                                        ActivityLogIn.tverrormsg.setText(errormessage);
                                        ActivityLogIn.tverrormsg.setVisibility(View.VISIBLE);
                                    }
                                });
                            }
                        });

                    } catch (Exception ex) {
                        hidepDialog();
                        Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    hidepDialog();
                    Utils.showInternetRequiredDialog(ActivityLogIn.this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                    return;
                }
            }
        };
        //endregion
        //region create new account
        View.OnClickListener rl_newaccountOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivitySingup.class);
                intent.putExtra("fromactivity", "ActivityLogIn");
                startActivity(intent);
                finish();
            }
        };
        //endregion
        //region forgot password
        View.OnClickListener rl_forgotpwdOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ActivityForgotPwd.class);
                intent.putExtra("from", "login");
                startActivity(intent);
                finish();
            }
        };
        //endregion
        //region toolbar Back ImageButton
        View.OnClickListener toolbarimgOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        };
        //endregion
        //region countrycode
        btn_logindropdownccode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCountryCode();
            }
        });
        //endregion
        rllogin.setOnClickListener(rl_LoginOnClickListener);
        rlforgotPwd.setOnClickListener(rl_forgotpwdOnClickListener);
        rlnewaccount.setOnClickListener(rl_newaccountOnClickListener);

        txt_buttonLogin.setOnClickListener(rl_LoginOnClickListener);
        txtforgotpwd.setOnClickListener(rl_forgotpwdOnClickListener);
        txt_btnNewSignUp.setOnClickListener(rl_newaccountOnClickListener);
        toolbar_imgback_button.setOnClickListener(toolbarimgOnClickListener);
    }

    //endregion
    //region dialog countrycode
    public void DialogCountryCode() {
        final Dialog dialog = new Dialog(ActivityLogIn.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogcountrycode);

        lvcountrycode = (ListView) dialog.findViewById(R.id.lvcountrycode);
        LoadCountryCode();
        lvcountrycode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CountryCode code = lst_countrycode.get(position);
                btn_logindropdownccode.setText(code.getISOCode() + " +" + code.getCountryCode());
                //btn_logincountrycode.setText("+" + code.getCountryCode());
              //  nationality = code.getCountryName();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void callvolleyCountryCode() {
        if (Utils.hasInternetConnection(mContext)) {
            VolleyCountryCode v_countryCode = new VolleyCountryCode(mContext);
            v_countryCode.GetCountryCodeJsonData(new VolleyCountryCode.VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<CountryCode> success) {
                    lst_countrycode = success;
                    if (lst_countrycode.size() > 0) {
                        for (int i = 0; i < lst_countrycode.size(); i++) {
                            if (lst_countrycode.get(i).getISOCode().equals(getResources().getString(R.string.default_countryisocode))) {
                                btn_logindropdownccode.setText(lst_countrycode.get(i).getISOCode());
                                btn_logincountrycode.setText("+" + lst_countrycode.get(i).getCountryCode());
                               // nationality = lst_countrycode.get(i).getCountryName();
                                break;
                            }
                        }
                    }
                }
                @Override
                public void onFail(String errorcode, String errormessage) {
                    btn_logindropdownccode.setText(getResources().getString(R.string.default_countryisocode));
                    btn_logincountrycode.setText("+" + getResources().getString(R.string.default_countrycode));
                }
            });
        } else {
            btn_logindropdownccode.setText(getResources().getString(R.string.default_countryisocode));
            btn_logincountrycode.setText("+" + getResources().getString(R.string.default_countrycode));
        }
    }

    public void LoadCountryCode() {
        adpt_countrycode = new CountryCodeAdapter(this, lst_countrycode);
        adpt_countrycode.notifyDataSetChanged();
        lvcountrycode.setAdapter(adpt_countrycode);
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
        Intent intent = null;
        switch (from_activity) {
            case "ActivityHome1":
                intent = new Intent(ActivityLogIn.this, ActivityHome1.class);
                break;
            case "ActivityCreateAccount":
                intent = new Intent(ActivityLogIn.this, ActivityCreateAccount.class);
                break;
            case "Queueflow":
                intent = new Intent(ActivityLogIn.this, ActivityQueueflow.class);
                intent.putExtra("clinicid", clinic_id);
                intent.putExtra("hecode", hedcode);
                intent.putExtra("clinicname", clinic_name);
                intent.putExtra("from_activity", from_activity);
                break;
            case "Apptflow":
                intent = new Intent(ActivityLogIn.this, ActivityApptFlow.class);
                intent.putExtra("CID", clinic_id);
                intent.putExtra("CName", clinic_name);
                intent.putExtra("appt_mode", appt_mode);
                break;
            case "eDocument":
                intent = new Intent(this, ActivityEDocument.class);
                intent.putExtra("from",from_activity);
                break;
            default:
                boolean isLoggedIn = preferences.getBoolean(getString(R.string.pref_is_logged_in), false);
                if (!isLoggedIn) {
                    intent = new Intent(ActivityLogIn.this, ActivityCreateAccount.class);
                } else {
                    intent = new Intent(ActivityLogIn.this, ActivityHome1.class);
                }
        }
        if (intent != null) {
            finish();
            startActivity(intent);
        }
    }

    //endregion
    //region GCM Service()
    private void getRegisterId() {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this);
            regid = getRegistrationId(mContext);
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
                        gcm = GoogleCloudMessaging.getInstance(mContext);
                    }
                    regid = gcm.register(SENDER_ID);
                    msg = regid;
                    sendRegistrationIdToBackend();
                    storeRegistrationId(mContext, regid);
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
    //region loading progress
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    //endregion
}
