package sg.com.ehealthassist.ehealthassist.Account;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestJsonAuth;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyAuth;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyAuthenticationService;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyCountryCode;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyLoginView;
import sg.com.ehealthassist.ehealthassist.ActivityHome;
import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.ActivitySetting;
import sg.com.ehealthassist.ehealthassist.CountryCode.CountryCodeAdapter;
import sg.com.ehealthassist.ehealthassist.Models.Profile.CountryCode;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 12/5/2017.
 */

@TargetApi(Build.VERSION_CODES.M)
public class FingerprintHandler extends FingerprintManager.AuthenticationCallback {

    // You should use the CancellationSignal method whenever your app can no longer process user input, for example when your app goes
    // into the background. If you don’t use this method, then other apps will be unable to access the touch sensor, including the lockscreen!//

    EditText et_alert_login_mobileId, et_alert_login_password;
    Button btn_alert_dropdowncode;
    ListView lvcountrycode;
    CountryCodeAdapter adpt_countrycode;
    private ProgressDialog pDialog;
    private CancellationSignal cancellationSignal;
    private Context context;
    ArrayList<CountryCode> lst_countrycode = new ArrayList<CountryCode>();
    private SharedPreferences preferences, preferencesFP;
    String nationality = "";

    public FingerprintHandler(Context mcontext) {
        context = mcontext;
        preferences = context.getSharedPreferences(context.getString(R.string.preference_name), MODE_PRIVATE);
        preferencesFP = context.getSharedPreferences("FingerPrintSP", MODE_PRIVATE);
    }

    //Implement the startAuth method, which is responsible for starting the fingerprint authentication process//

    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {

        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //Toast.makeText(context, "Start", Toast.LENGTH_SHORT).show();
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);

        Log.d("fingerprinttest", "start");
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        Log.d("fingerprinttest", preferences.getString("test", ""));
    }

    @Override
    //onAuthenticationError is called when a fatal error has occurred. It provides the error code and error message as its parameters//

    public void onAuthenticationError(int errMsgId, CharSequence errString) {

        //I’m going to display the results of fingerprint authentication as a series of toasts.
        //Here, I’m creating the message that’ll be displayed if an error occurs//

        //Toast.makeText(context, "Authentication error\n" + errString, Toast.LENGTH_LONG).show();
    }

    @Override

    //onAuthenticationFailed is called when the fingerprint doesn’t match with any of the fingerprints registered on the device//

    public void onAuthenticationFailed() {
        Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show();
    }

    @Override

    //onAuthenticationHelp is called when a non-fatal error has occurred. This method provides additional information about the error,
    //so to provide the user with as much feedback as possible I’m incorporating this information into my toast//
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        //Toast.makeText(context, "Authentication help\n" + helpString, Toast.LENGTH_LONG).show();
    }@Override

    //onAuthenticationSucceeded is called when a fingerprint has been successfully matched to one of the fingerprints stored on the user’s device//
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {

        preferences.edit().putString("test", "test");

        Log.d("finalfinalfinal", preferencesFP.getString("fingerprint_check" , "") + "check");
        if (preferencesFP.getString("fingerprint_check", "").equals("fingerprint_success"))
        {
            Log.d("finalfinalfinal", "come");
            checkLoginAuth();
            Log.d("fingerprinttest", "ok");
            /*Intent intent = new Intent(context, ActivityHome.class);
            context.startActivity(intent);*/
        }
        else {
            Log.d("finalfinalfinal", "come 2");
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Register with Fingerprint");
            builder.setMessage("Do you want to register with fingerprint");
            builder.setIcon(R.drawable.ic_info_white_24dp);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callDialog();
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();
        }
        Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show();
    }

    private void callDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder.create();
        View view = LayoutInflater.from(context).inflate(R.layout.custom_alert_fingerprint, null);
        dialog.setView(view);
        btn_alert_dropdowncode = view.findViewById(R.id.btn_alert_dropdowncode);
        et_alert_login_mobileId = view.findViewById(R.id.et_alert_login_mobileId);
        final EditText et_alert_login_password = view.findViewById(R.id.et_alert_login_password);
        Button btnCancel = view.findViewById(R.id.btn_alert_cancel);
        Button btnOk = view.findViewById(R.id.btn_alert_ok);
        callvolleyCountryCode();

        btn_alert_dropdowncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogCountryCode();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Boolean hasError = false;
                if (et_alert_login_mobileId.length() <= 0) {
                    et_alert_login_mobileId.setError(context.getText(R.string.err_invalid_pwd_length));
                    hasError = true;
                }
                if (et_alert_login_password.length() <= 0) {
                    et_alert_login_password.setError(context.getText(R.string.err_invalid_pwd_length));
                    hasError = true;
                }
                if (hasError) {
                    return;
                }
                showpDialog();
                final String phone_number = et_alert_login_mobileId.getText().toString();
                final String password = et_alert_login_password.getText().toString();
                final String gcmid = preferences.getString(context.getString(R.string.pref_gcm_property_reg_id), "");
                final String prefix = btn_alert_dropdowncode.getText().toString().substring(3);
                Log.d("prefixprefix", prefix);
                if (Utils.hasInternetConnection(context)) {
                    try {
                        VolleyAuthenticationService jwt_token = new VolleyAuthenticationService(context);
                        jwt_token.GetStringRequest(prefix + phone_number, password, new VolleyAuthenticationService.VolleyCallback() {
                            @Override
                            public void onSuccess(JSONObject message) {
                                // ActivityLogIn.tverrormsg.setVisibility(View.GONE);
                                try {
                                    String acctesstoken = message.getString("access_token");
                                    String tokey_type = message.getString("token_type");
                                    String expires_in = message.getString("expires_in");
                                    String usertoken = tokey_type + " " + acctesstoken;
                                    preferences.edit().putBoolean(context.getString(R.string.pref_is_account_verified), true)
                                            .putString(context.getString(R.string.pref_login_Access_token), usertoken)
                                            .apply();

                                    RequestJsonAuth requestauth = new RequestJsonAuth(gcmid);
                                    JSONObject authjson = requestauth.StringtoJsonObject(requestauth.ObjectJsonforLoginView(gcmid));
                                    VolleyLoginView _vloginview = new VolleyLoginView(context, nationality);
                                    _vloginview.GetLoginViewJsonData(usertoken, authjson, new VolleyLoginView.VolleyCallback() {
                                        @Override
                                        public void onSuccess(String name, String nric, String nrictype, String dob) {

                                            preferencesFP.edit().putString("fingerprint_phone_number", phone_number)
                                                    .putString("fingerprint_password", password)
                                                    .putString("fingerprint_prefix", prefix)
                                                    .apply();
                                            preferencesFP.edit().putString("fingerprint_check", "fingerprint_success").apply();
                                            Log.d("fingerprinttest", preferences.getString("fingerprint_check", "") + "success");
                                            Toast.makeText(context, "You can Login with fingerprint Now", Toast.LENGTH_SHORT).show();
                                            /*Intent intent = new Intent(context, ActivityHome1.class);
                                            context.startActivity(intent);
                                            //ActivityLogIn.activity.finish();
                                            getActivity().finish();*/
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

                                RequestJsonAuth requestauth = new RequestJsonAuth(prefix + phone_number, password, gcmid);
                                JSONObject authjson = requestauth.StringtoJsonObject(requestauth.ObjecttoJson(requestauth));

                                VolleyAuth _vauth = new VolleyAuth(context, nationality);
                                _vauth.GetClinicJson(authjson, new VolleyAuth.VolleyCallback() {
                                    @Override
                                    public void onSuccess(String message) {
                                        hidepDialog();
                                        preferences.edit().putString(context.getString(R.string.pref_signup_password), password)
                                                .putString(context.getString(R.string.pref_main_user_data_hp_nationality), nationality)
                                                .apply();
                                        Intent intent = new Intent(context, ActivitySetting.class);
                                        context.startActivity(intent);
                                        //ActivityLogIn.activity.finish();
                                        //getActivity().finish();
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
                        Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    hidepDialog();
                    Utils.showInternetRequiredDialog(context, context.getString(R.string.title_internet_require), context.getString(R.string.msg_no_internet_connection_setup));
                    return;
                }
                dialog.dismiss();
            }

            });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void checkLoginAuth(){

        final String check_phone_number = preferencesFP.getString("fingerprint_phone_number", "");
        final String check_prefix = preferencesFP.getString("fingerprint_prefix", "");
        final String check_password = preferencesFP.getString("fingerprint_password", "");

        if (!check_phone_number.equals("") && !check_prefix.equals("") && !check_password.equals(""))
        {
            Log.d("finalfinalfinal", "have");
            final String gcmid = preferences.getString(context.getString(R.string.pref_gcm_property_reg_id), "");
            if (Utils.hasInternetConnection(context)) {
                try {
                    VolleyAuthenticationService jwt_token = new VolleyAuthenticationService(context);
                    jwt_token.GetStringRequest(check_prefix + check_phone_number, check_password, new VolleyAuthenticationService.VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject message) {
                            // ActivityLogIn.tverrormsg.setVisibility(View.GONE);
                            try {
                                String acctesstoken = message.getString("access_token");
                                String tokey_type = message.getString("token_type");
                                String expires_in = message.getString("expires_in");
                                String usertoken = tokey_type + " " + acctesstoken;
                                preferences.edit().putBoolean(context.getString(R.string.pref_is_account_verified), true)
                                        .putString(context.getString(R.string.pref_login_Access_token), usertoken)
                                        .apply();
                                Log.d("finalfinalfinal", "token success");

                                RequestJsonAuth requestauth = new RequestJsonAuth(gcmid);
                                JSONObject authjson = requestauth.StringtoJsonObject(requestauth.ObjectJsonforLoginView(gcmid));
                                VolleyLoginView _vloginview = new VolleyLoginView(context,nationality);
                                _vloginview.GetLoginViewJsonData(usertoken, authjson, new VolleyLoginView.VolleyCallback() {
                                    @Override
                                    public void onSuccess(String name,String nric,String nrictype,String dob) {

                                       /* preferences.edit().putString("fingerprint_phone_number", phone_number)
                                                .putString("fingerprint_password", password)
                                                .putString("fingerprint_prefix", prefix)
                                                .putString("fingerprint_check", "fingerprint_success").apply();
                                        Log.d("fingerprinttest", preferences.getString("fingerprint_check", "") + "success");
                                        Toast.makeText(context, "You can Login with fingerprint Now", Toast.LENGTH_SHORT).show();
*/
                                            /*Intent intent = new Intent(context, ActivityHome1.class);
                                            context.startActivity(intent);
                                            //ActivityLogIn.activity.finish();
                                            getActivity().finish();*/
                                        Intent intent = new Intent(context, ActivityHome.class);
                                        context.startActivity(intent);
                                        Log.d("finalfinalfinal", "final success");
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

                            RequestJsonAuth requestauth = new RequestJsonAuth(check_prefix + check_phone_number,check_password,gcmid);
                            JSONObject authjson = requestauth.StringtoJsonObject(requestauth.ObjecttoJson(requestauth));

                            VolleyAuth _vauth = new VolleyAuth(context,nationality);
                            _vauth.GetClinicJson(authjson, new VolleyAuth.VolleyCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    hidepDialog();
                                    preferences.edit().putString(context.getString(R.string.pref_signup_password) ,check_password)
                                            .putString(context.getString(R.string.pref_main_user_data_hp_nationality) ,nationality)
                                            .apply();
                                    Intent intent = new Intent(context, ActivitySetting.class);
                                    context.startActivity(intent);
                                    //ActivityLogIn.activity.finish();
                                    //getActivity().finish();
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
                    Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                hidepDialog();
                Utils.showInternetRequiredDialog(context, context.getString(R.string.title_internet_require), context.getString(R.string.msg_no_internet_connection_setup));
                return;
            }
        }
        else {
            /*Boolean hasError = false;
            if (et_alert_login_mobileId.length() <= 0) {
                et_alert_login_mobileId.setError(context.getText(R.string.err_invalid_pwd_length));
                hasError = true;
            }
            if (et_alert_login_password.length() <= 0) {
                et_alert_login_password.setError(context.getText(R.string.err_invalid_pwd_length));
                hasError = true;
            }
            if (hasError) {
                return;
            }
            showpDialog();
            final String phone_number = et_alert_login_mobileId.getText().toString();
            final String password = et_alert_login_password.getText().toString();
            final String gcmid = preferences.getString(context.getString(R.string.pref_gcm_property_reg_id), "");
            final String prefix = btn_alert_dropdowncode.getText().toString().substring(3);
            Log.d("prefixprefix", prefix);
            if (Utils.hasInternetConnection(context)) {
                try {
                    VolleyAuthenticationService jwt_token = new VolleyAuthenticationService(context);
                    jwt_token.GetStringRequest(prefix + phone_number, password, new VolleyAuthenticationService.VolleyCallback() {
                        @Override
                        public void onSuccess(JSONObject message) {
                            // ActivityLogIn.tverrormsg.setVisibility(View.GONE);
                            try {
                                String acctesstoken = message.getString("access_token");
                                String tokey_type = message.getString("token_type");
                                String expires_in = message.getString("expires_in");
                                String usertoken = tokey_type + " " + acctesstoken;
                                preferences.edit().putBoolean(context.getString(R.string.pref_is_account_verified), true)
                                        .putString(context.getString(R.string.pref_login_Access_token), usertoken)
                                        .apply();

                                RequestJsonAuth requestauth = new RequestJsonAuth(gcmid);
                                JSONObject authjson = requestauth.StringtoJsonObject(requestauth.ObjectJsonforLoginView(gcmid));
                                VolleyLoginView _vloginview = new VolleyLoginView(context, nationality);
                                _vloginview.GetLoginViewJsonData(usertoken, authjson, new VolleyLoginView.VolleyCallback() {
                                    @Override
                                    public void onSuccess(String name, String nric, String nrictype, String dob) {

                                        preferences.edit().putString("fingerprint_phone_number", phone_number)
                                                .putString("fingerprint_password", password)
                                                .putString("fingerprint_prefix", prefix)
                                                .putString("fingerprint_check", "fingerprint_success").apply();
                                        Log.d("fingerprinttest", preferences.getString("fingerprint_check", "") + "success");
                                        Toast.makeText(context, "You can Login with fingerprint Now", Toast.LENGTH_SHORT).show();

                                            *//*Intent intent = new Intent(context, ActivityHome1.class);
                                            context.startActivity(intent);
                                            //ActivityLogIn.activity.finish();
                                            getActivity().finish();*//*
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

                            RequestJsonAuth requestauth = new RequestJsonAuth(prefix + phone_number, password, gcmid);
                            JSONObject authjson = requestauth.StringtoJsonObject(requestauth.ObjecttoJson(requestauth));

                            VolleyAuth _vauth = new VolleyAuth(context, nationality);
                            _vauth.GetClinicJson(authjson, new VolleyAuth.VolleyCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    hidepDialog();
                                    preferences.edit().putString(context.getString(R.string.pref_signup_password), password)
                                            .putString(context.getString(R.string.pref_main_user_data_hp_nationality), nationality)
                                            .apply();
                                    Intent intent = new Intent(context, ActivitySetting.class);
                                    context.startActivity(intent);
                                    //ActivityLogIn.activity.finish();
                                    //getActivity().finish();
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
                    Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show();
                }
            } else {
                hidepDialog();
                Utils.showInternetRequiredDialog(context, context.getString(R.string.title_internet_require), context.getString(R.string.msg_no_internet_connection_setup));
                return;
            }*/
        }
    }

    public void DialogCountryCode() {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogcountrycode);

        lvcountrycode = (ListView) dialog.findViewById(R.id.lvcountrycode);
        LoadCountryCode();
        lvcountrycode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CountryCode code = lst_countrycode.get(position);
                btn_alert_dropdowncode.setText(code.getISOCode() + " +" + code.getCountryCode());
                //btn_logincountrycode.setText("+" + code.getCountryCode());
                //  nationality = code.getCountryName();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void LoadCountryCode() {
        adpt_countrycode = new CountryCodeAdapter(context, lst_countrycode);
        adpt_countrycode.notifyDataSetChanged();
        lvcountrycode.setAdapter(adpt_countrycode);
    }

    public void callvolleyCountryCode() {
        if (Utils.hasInternetConnection(context)) {
            VolleyCountryCode v_countryCode = new VolleyCountryCode(context);
            v_countryCode.GetCountryCodeJsonData(new VolleyCountryCode.VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<CountryCode> success) {
                    lst_countrycode = success;
                    if (lst_countrycode.size() > 0) {
                        for (int i = 0; i < lst_countrycode.size(); i++) {
                            if (lst_countrycode.get(i).getISOCode().equals(context.getResources().getString(R.string.default_countryisocode))) {
                                btn_alert_dropdowncode.setText(lst_countrycode.get(i).getISOCode() + " +" + lst_countrycode.get(i).getCountryCode());
                                //btn_logincountrycode.setText("+" + lst_countrycode.get(i).getCountryCode());
                                // nationality = lst_countrycode.get(i).getCountryName();
                                break;
                            }
                        }
                    }
                }
                @Override
                public void onFail(String errorcode, String errormessage) {
                    btn_alert_dropdowncode.setText(context.getResources().getString(R.string.default_countryisocode) + " +" + context.getResources().getString(R.string.default_countrycode));
                    //btn_logincountrycode.setText("+" + getResources().getString(R.string.default_countrycode));
                }
            });
        } else {
            btn_alert_dropdowncode.setText(context.getResources().getString(R.string.default_countryisocode) + " +" + context.getResources().getString(R.string.default_countrycode));
            //btn_logincountrycode.setText("+" + getResources().getString(R.string.default_countrycode));
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
