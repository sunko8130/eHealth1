package sg.com.ehealthassist.ehealthassist.Fragment;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.KeyguardManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestJsonAuth;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyAuth;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyAuthenticationService;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyCountryCode;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyLoginView;
import sg.com.ehealthassist.ehealthassist.Account.ActivityCreateAccount;
import sg.com.ehealthassist.ehealthassist.Account.ActivityForgotPwd;
import sg.com.ehealthassist.ehealthassist.Account.ActivityLogIn;
import sg.com.ehealthassist.ehealthassist.Account.ActivitySingup;
import sg.com.ehealthassist.ehealthassist.Account.FingerprintHandler;
import sg.com.ehealthassist.ehealthassist.ActivitySetting;
import sg.com.ehealthassist.ehealthassist.CountryCode.CountryCodeAdapter;
import sg.com.ehealthassist.ehealthassist.ActivityHome;
import sg.com.ehealthassist.ehealthassist.Models.Profile.CountryCode;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

import static android.content.Context.FINGERPRINT_SERVICE;
import static android.content.Context.KEYGUARD_SERVICE;
import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 12/1/2017.
 */

public class FragLogIn extends Fragment {

    SharedPreferences preferences = null;
    EditText editTextLogInMobileId, editTextLogInPassword;
    RelativeLayout rllogin, rlforgotPwd, rlnewaccount;
    TextView main_toolbar_title, txtforgotpwd, txt_buttonLogin, txt_btnNewSignUp;
    ImageButton toolbar_imgback_button;
    Button btn_logindropdownccode, btn_logincountrycode, btn_loginskip;
    String from_activity = "";
    Context mContext = null;
    int clinic_id = 0;int position = 0;
    public static TextView tverrormsg;
    public static Activity activity;
    String SENDER_ID = "", hedcode = "", clinic_name = "", appt_mode = "", regid;
    static final String TAG = "GCM";
    GoogleCloudMessaging gcm;
    ListView lvcountrycode;
    CardView cardView;
    CountryCodeAdapter adpt_countrycode;
    ArrayList<CountryCode> lst_countrycode = new ArrayList<CountryCode>();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private ProgressDialog pDialog;
    String nationality = "";

    private static final String KEY_NAME = "yourKey";
    private Cipher cipher;
    private KeyStore keyStore;
    private KeyGenerator keyGenerator;
    private TextView textView;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_login, container, false);

        //getActivity().getSupportActionBar().hide();
        //windowTransition();
        mContext = getContext();
        activity = getActivity();
        SENDER_ID = getResources().getString(R.string.gcm_sender_id);
        preferences = getActivity().getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        pDialog = new ProgressDialog(mContext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        /*Intent i_from = getIntent();
        from_activity = i_from.getStringExtra("from");
        clinic_id = i_from.getIntExtra("CID", 0);
        if (from_activity.equals("Queueflow")) {
            hedcode = i_from.getStringExtra("hecode");
            clinic_name = i_from.getStringExtra("clinic_name");
        } else if (from_activity.equals("Apptflow")) {
            clinic_name = i_from.getStringExtra("clinic_name");
            appt_mode = i_from.getStringExtra("appt_mode");
        }*/

        callvolleyCountryCode();
        getRegisterId();
        findViewsById();
        setEvent();
        setFingerPrint();

        //findViewsById();
        return view;
    }

    /*@Override
    public void onStart() {
        super.onStart();
        setFingerPrint();
    }*/

    //region findViewsById()
    private void findViewsById() {
        editTextLogInMobileId = (EditText)view.findViewById(R.id.editTextLogInMobileId);
        editTextLogInPassword = (EditText)view.findViewById(R.id.editTextLogInPassword);
        txt_buttonLogin = (TextView)view.findViewById(R.id.txt_buttonLogin);
        txt_btnNewSignUp = (TextView)view.findViewById(R.id.txt_btnNewSignUp);
        txtforgotpwd = (TextView)view.findViewById(R.id.txtforgotpwd);
        btn_loginskip = (Button) view.findViewById(R.id.btn_login_skip);
        cardView = view.findViewById(R.id.card_frag_one);
        /*rllogin = (RelativeLayout)view.findViewById(R.id.rllogin);
        rlforgotPwd = (RelativeLayout)view.findViewById(R.id.rlforgotPwd);
        rlnewaccount = (RelativeLayout)view.findViewById(R.id.rlnewaccount);
        tverrormsg = (TextView)view.findViewById(R.id.tverrormsg);*/
        //tverrormsg.setVisibility(View.INVISIBLE);
        /*main_toolbar_title = (TextView)view.findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(R.string.title_activity_login);
        toolbar_imgback_button = (ImageButton)view.findViewById(R.id.toolbar_imgbackbutton);*/
        //btn_logincountrycode = (Button)view.findViewById(R.id.btn_logincountrycode);
        btn_logindropdownccode = (Button)view.findViewById(R.id.btn_logindropdownccode);
    }

    private void callAnimation() {
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.right_to_left);
        cardView.startAnimation(animation);
    }

    private void setFingerPrint() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("versionCheck", "ok");
            //Get an instance of KeyguardManager and FingerprintManager//
            keyguardManager =
                    (KeyguardManager) getActivity().getSystemService(KEYGUARD_SERVICE);
            fingerprintManager =
                    (FingerprintManager) getActivity().getSystemService(FINGERPRINT_SERVICE);

            //tvMessage = (TextView) findViewById(R.id.tvMessage);

            //Check whether the device has a fingerprint sensor//
            if (!fingerprintManager.isHardwareDetected()) {
                Log.d("versionCheck", "fingerprint doesn't support");
                // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//
                //tvMessage.setText("Your device doesn't support fingerprint authentication");
            }
            //Check whether the user has granted your app the USE_FINGERPRINT permission//
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
                // If your app doesn't have this permission, then display the following text//
                Log.d("versionCheck", "permission");
                //tvMessage.setText("Please enable the fingerprint permission");
            }else {
                Log.d("versionCheck", "permission ok");
            }

            //Check that the user has registered at least one fingerprint//
            if (!fingerprintManager.hasEnrolledFingerprints()) {
                Log.d("versionCheck", "Enter");
                // If the user hasn’t configured any fingerprints, then display the following message//
                //tvMessage.setText("No fingerprint configured. Please register at least one fingerprint in your device's Settings");
            }

            //Check that the lockscreen is secured//
            if (!keyguardManager.isKeyguardSecure()) {
                Log.d("versionCheck", "keyguard");
                // If the user hasn’t secured their lockscreen with a PIN password or pattern, then display the following text//
                //tvMessage.setText("Please enable lockscreen security in your device's Settings");
            }
            else {
                try{
                    generateKey();
                } catch (FingerprintException e) {
                    e.printStackTrace();
                }

                if (initCipher()) {
                    //If the cipher is initialized successfully, then create a CryptoObject instance//
                    cryptoObject = new FingerprintManager.CryptoObject(cipher);

                    // Here, I’m referencing the FingerprintHandler class that we’ll create in the next section. This class will be responsible
                    // for starting the authentication process (via the startAuth method) and processing the authentication process events//
                    FingerprintHandler helper = new FingerprintHandler(getContext());
                    helper.startAuth(fingerprintManager, cryptoObject);
                }
            }
        }
    }

//Create the generateKey method that we’ll use to gain access to the Android keystore and generate the encryption key//

    @TargetApi(Build.VERSION_CODES.M)
    private void generateKey() throws FingerprintException {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            //Generate the key//
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            //Initialize an empty KeyStore//
            keyStore.load(null);

            //Initialize the KeyGenerator//
            keyGenerator.init(new

                    //Specify the operation(s) this key can be used for//
                    KeyGenParameterSpec.Builder(KEY_NAME,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                    //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(
                            KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());

            //Generate the key//
            keyGenerator.generateKey();

        } catch (KeyStoreException
                | NoSuchAlgorithmException
                | NoSuchProviderException
                | InvalidAlgorithmParameterException
                | CertificateException
                | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }
    }

    //Create a new method that we’ll use to initialize our cipher//
    @TargetApi(Build.VERSION_CODES.M)
    public boolean initCipher() {
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(KEY_NAME,
                    null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //Return true if the cipher has been initialized successfully//
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {

            //Return false if cipher initialization failed//
            return false;
        } catch (KeyStoreException | CertificateException
                | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    public void receiveDatafromFragCreateAccount(String fromactivity, int cid)
    {
        from_activity = fromactivity;
        clinic_id = cid;
        Log.d("testtest", from_activity + " " + clinic_id);
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
                final String prefix = btn_logindropdownccode.getText().toString().substring(3);
                Log.d("prefixprefix", prefix);
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
                                            Intent intent = new Intent(mContext, ActivityHome.class);
                                            intent.putExtra("successRecord", 1);
                                            mContext.startActivity(intent);
                                            //ActivityLogIn.activity.finish();
                                            getActivity().finish();
                                        }

                                        @Override
                                        public void onFail(String errorcode, String errormessage) {
                                            /*ActivityLogIn.tverrormsg.setText(errormessage);
                                            ActivityLogIn.tverrormsg.setVisibility(View.VISIBLE);*/
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
                                        //ActivityLogIn.activity.finish();
                                        getActivity().finish();
                                    }

                                    @Override
                                    public void onFail(String errorcode, String errormessage) {
                                        hidepDialog();
                                        /*ActivityLogIn.tverrormsg.setText(errormessage);
                                        ActivityLogIn.tverrormsg.setVisibility(View.VISIBLE);*/
                                    }
                                });
                            }
                        });

                    } catch (Exception ex) {
                        hidepDialog();
                        Toast.makeText(getContext(), ex.toString(), Toast.LENGTH_LONG).show();
                    }
                } else {
                    hidepDialog();
                    Utils.showInternetRequiredDialog(getContext(), getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                    return;
                }
            }
        };
        //endregion
        //region create new account
        View.OnClickListener rl_newaccountOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivitySingup.class);
                intent.putExtra("fromactivity", "ActivityLogIn");
                startActivity(intent);
                getActivity().finish();
            }
        };
        //endregion
        //region forgot password
        View.OnClickListener rl_forgotpwdOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityForgotPwd.class);
                intent.putExtra("from", "login");
                startActivity(intent);
                //getActivity().finish();
            }
        };

        View.OnClickListener rl_skipOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, ActivityHome.class);
                intent.putExtra("successRecord", 0);
                mContext.startActivity(intent);
                getActivity().finish();
            }
        };

        //endregion
        //region toolbar Back ImageButton
        /*View.OnClickListener toolbarimgOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        };*/

        //endregion
        //region countrycode
        btn_logindropdownccode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCountryCode();
            }
        });
        //endregion
        /*rllogin.setOnClickListener(rl_LoginOnClickListener);
        rlforgotPwd.setOnClickListener(rl_forgotpwdOnClickListener);
        rlnewaccount.setOnClickListener(rl_newaccountOnClickListener);*/

        txt_buttonLogin.setOnClickListener(rl_LoginOnClickListener);
        txtforgotpwd.setOnClickListener(rl_forgotpwdOnClickListener);
        txt_btnNewSignUp.setOnClickListener(rl_newaccountOnClickListener);
        btn_loginskip.setOnClickListener(rl_skipOnClickListener);

        //toolbar_imgback_button.setOnClickListener(toolbarimgOnClickListener);
    }

    //endregion
    //region dialog countrycode
    public void DialogCountryCode() {
        final Dialog dialog = new Dialog(getContext());
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
                                btn_logindropdownccode.setText(lst_countrycode.get(i).getISOCode() + " +" + lst_countrycode.get(i).getCountryCode());
                                //btn_logincountrycode.setText("+" + lst_countrycode.get(i).getCountryCode());
                                // nationality = lst_countrycode.get(i).getCountryName();
                                break;
                            }
                        }
                    }
                }
                @Override
                public void onFail(String errorcode, String errormessage) {
                    btn_logindropdownccode.setText(getResources().getString(R.string.default_countryisocode) + " +" + getResources().getString(R.string.default_countrycode));
                    //btn_logincountrycode.setText("+" + getResources().getString(R.string.default_countrycode));
                }
            });
        } else {
            btn_logindropdownccode.setText(getResources().getString(R.string.default_countryisocode) + " +" + getResources().getString(R.string.default_countrycode));
            //btn_logincountrycode.setText("+" + getResources().getString(R.string.default_countrycode));
        }
    }

    public void LoadCountryCode() {
        adpt_countrycode = new CountryCodeAdapter(getContext(), lst_countrycode);
        adpt_countrycode.notifyDataSetChanged();
        lvcountrycode.setAdapter(adpt_countrycode);
    }

    //endregion
    //region onKeyDown()
    /*@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }*/

    public void returnback() {
        Intent intent = null;
        switch (from_activity) {
            /*case "ActivityHome1":
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
                }*/
        }
        if (intent != null) {
            getActivity().finish();
            startActivity(intent);
        }
    }

    //endregion
    //region GCM Service()
    private void getRegisterId() {
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(getContext());
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

    /*@Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }*/

    @Override
    public void onResume() {
        super.onResume();
        checkPlayServices();
        setFingerPrint();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                getActivity().finish();
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

private class FingerprintException extends Exception {
    public FingerprintException(Exception e) {
        super(e);
    }
}
}
