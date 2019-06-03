package sg.com.ehealthassist.ehealthassist;

import android.Manifest;
import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestVersionJson;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyClinic;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyClinicServices;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyPublicHoliday;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyVersionChanges;
import sg.com.ehealthassist.ehealthassist.Account.ActivityCreateAccount;
import sg.com.ehealthassist.ehealthassist.DB.DBPublicHoliday;
import sg.com.ehealthassist.ehealthassist.DB.DBService_items;
import sg.com.ehealthassist.ehealthassist.DB.DBServices;
import sg.com.ehealthassist.ehealthassist.DB.DatabaseHandler;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Other.Utils;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.GET_ACCOUNTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.VIBRATE;
import static android.Manifest.permission.WAKE_LOCK;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission_group.CAMERA;

public class ActivitySplashScreen extends AppCompatActivity {

    RelativeLayout rl_splashscreen;

    public static ImageView iv_head_arrow;
    String lastclinicinfoupdate = "";
    public static String currentClinicInfoRowVersion = "";
    public static String newversion = "";
    public static Context _mcontext;
    public static Activity _activity;

    SharedPreferences pref_constant = null;
    SharedPreferences pref_name = null;

    DatabaseHandler db;
    DBPublicHoliday dbpublicholiday;
    DBServices dbservices;
    DBService_items dbservice_item;

    public static Boolean download_flag = false;
    public static String download_count = "";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    String SENDER_ID = "", regid;
    static final String TAG = "GCM";
    GoogleCloudMessaging gcm;
    public static final int RequestPermissionCode = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        _mcontext = this;
        _activity = this;
        db = new DatabaseHandler(this);
        dbpublicholiday = new DBPublicHoliday(this);
        dbservices = new DBServices(this);
        dbservice_item = new DBService_items(this);
        SENDER_ID = getResources().getString(R.string.gcm_sender_id);
        pref_constant = getSharedPreferences(getString(R.string.preference_constant), MODE_PRIVATE);
        pref_name = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        iv_head_arrow = (ImageView) findViewById(R.id.iv_head_arrow);
        rl_splashscreen=(RelativeLayout)findViewById(R.id.layout_splashscreen);
        download_flag = pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false);
        Animation animation= AnimationUtils.loadAnimation(ActivitySplashScreen.this,R.anim.bottom_to_center);
        rl_splashscreen.startAnimation(animation);

        VersionChecked();
    }

    //region download Clinc list & public holiday
    public void StartDownloadData() {
        RotateImage();
        GetpublicHoliday();
        GetClinicServices();
        if (Utils.hasInternetConnection(getApplicationContext())) {
            currentClinicInfoRowVersion = pref_constant.getString(getString(R.string.pref_permanent_clinic_info_row_version), "150101000000000");

            lastclinicinfoupdate = pref_constant.getString(getString(R.string.pref_lastupdate_time), "");
            try {
                Log.e(TAG, "RowVersion: " + currentClinicInfoRowVersion);
                if (findtimeInterval(Utils.getcurrenttime("dd-MM-yy hh:mm a"), lastclinicinfoupdate)) {
                    pref_constant.edit().putBoolean(getString(R.string.pref_permanent_clinic_download_flag), false).apply();
                    Log.e("update make", "Time Interval" + lastclinicinfoupdate);
                    VolleyClinic volley_clinic = new VolleyClinic(_mcontext);
                    volley_clinic.GetClinicJson( Constant.URL_CLINICINFO+currentClinicInfoRowVersion, new VolleyClinic.VolleyCallback() {
                        @Override
                        public void onSuccess(boolean flag) {
                            if (flag) {
                                download_flag = true;
                                editpreferencedownload_flag();
                            }
                        }
                        @Override
                        public void onFail(String errorcode, String message) {
                            download_flag = true;
                            editpreferencedownload_flag();
                        }
                    });
                    Log.e("ActivityNewVersion>>", newversion);
                    pref_constant.edit().
                            putString(getString(R.string.pref_lastupdate_time), lastclinicinfoupdate)
                            .apply();
                    gotohomepage();
                } else {
                    download_flag = true;
                    editpreferencedownload_flag();
                    gotohomepage();

                }
            } catch (Exception ex) {
                download_flag = true;
                editpreferencedownload_flag();
                Log.i(TAG, "Exception: " + ex.getMessage());
            }
        } else {
            try {
                download_flag = true;
                editpreferencedownload_flag();
                gotohomepage();
            } catch (Exception ex) {
                download_flag = true;
                editpreferencedownload_flag();
                Log.i(TAG, "No internet: " + ex.getMessage());
            }
        }
    }

    //endregion

    // region Animation rotate Image()
    public static void RotateImage() {
        RotateAnimation anim = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(300);
        iv_head_arrow.startAnimation(anim);
    }

    //endregion
    //region Home()
    public void gotohomepage() {
        getRegisterId();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                iv_head_arrow.setAnimation(null);
                ActivitySplashScreen._activity.overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                boolean isLoggedIn = pref_name.getBoolean(getString(R.string.pref_is_logged_in), false);
                Log.e("login checked", isLoggedIn + "");
                if(!checkPermission()){
                    requestPermission();
                }
                else{
                    if (!isLoggedIn) {
                        Intent intent = new Intent(_mcontext, ActivityCreateAccount.class);
                        intent.putExtra("from", "splash_screen");
                        ActivitySplashScreen._activity.startActivity(intent);
                        ActivitySplashScreen._activity.finish();
                    } else {
                        Intent intent = new Intent(_mcontext, ActivityHome.class);
                        intent.putExtra("successRecord", 0);
                        ActivitySplashScreen._activity.startActivity(intent);
                        ActivitySplashScreen._activity.finish();
                    }
                }
            }
        }, 1000);
    }

    // endregion
    // region Check Update Time() --> >15minute
    public boolean findtimeInterval(String currentdate, String lastupdatedate) {
        Log.e("date compare", "currentdate :" + currentdate + "lastupdate :" + lastupdatedate);
        boolean updatecall = false;
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yy hh:mm a");
        Date convertedcurrentdate = new Date();
        Date convertedlastdate = new Date();
        try {
            if (!lastupdatedate.equals("")) {
                convertedcurrentdate = dateFormatter.parse(currentdate);
                convertedlastdate = dateFormatter.parse(lastupdatedate);
                if (convertedlastdate.compareTo(convertedcurrentdate) < 0) {
                    long mills = convertedcurrentdate.getTime() - convertedlastdate.getTime();
                    long Hours = mills / (1000 * 60 * 15);
                    if (Hours > 0) {
                        updatecall = true;
                        lastclinicinfoupdate = currentdate;
                    }
                } else {
                    updatecall = false;
                }
            } else {
                updatecall = true;
                lastclinicinfoupdate = currentdate;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        Log.e("result compare", updatecall + "");
        return updatecall;
    }

    public void GetpublicHoliday() {
        Log.e("public holiday", "volley");
        try {
            if (Utils.hasInternetConnection(getApplicationContext())) {
                dbpublicholiday.deleteallHoliday();
                VolleyPublicHoliday v_public = new VolleyPublicHoliday(this);
                v_public.GetPublicholidayJsonData(new VolleyPublicHoliday.VolleyCallback() {
                    @Override
                    public void onSuccess(String pubholidaylists) {
                        Log.e("holiday", pubholidaylists);
                    }

                    @Override
                    public void onFail(String errorcode, String errormessage) {

                    }
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void VersionChecked() {
        RotateImage();
        try {
            RequestVersionJson version_json = new RequestVersionJson();
            JSONObject param_json = version_json.StringtoJsonObject(version_json.ObjecttoJson());
            if (Utils.hasInternetConnection(getApplicationContext())) {
                VolleyVersionChanges v_versioncheck = new VolleyVersionChanges(this);
                v_versioncheck.GetVersionRequest(param_json, new VolleyVersionChanges.VolleyCallback() {
                    @Override
                    public void onSuccess(String version, String version_url, boolean forceupdate) {
                        if (!version.equals(getAppVersionName(getApplicationContext()))) {
                            if (forceupdate) {
                                // goto url
                                iv_head_arrow.setAnimation(null);
                                /*finish();
                                GotoPlaystorePage();*/
                                versionDialogbox();

                            } else {
                                //show alert box
                                iv_head_arrow.setAnimation(null);
                                versionDialogbox();
                            }
                        } else {
                            // flase
                            iv_head_arrow.setAnimation(null);
                            StartDownloadData();
                        }
                    }

                    @Override
                    public void onFail(String errorcode, String errormessage) {
                        Log.e(errorcode, errormessage);
                        iv_head_arrow.setAnimation(null);
                        StartDownloadData();
                    }
                });
            } else {
                iv_head_arrow.setAnimation(null);
                StartDownloadData();
            }
        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }
    public void GetClinicServices(){
        try {
            if (Utils.hasInternetConnection(getApplicationContext())) {
                dbservices.deleteAllServices();
                dbservice_item.deleteAllServicesItem();
                VolleyClinicServices v_services= new VolleyClinicServices(this);
                v_services.GetClinicServicesJson();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    // endregion
    //region AppVersionName
    public static String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    //endregion
    //region versionDialogbox
    public void versionDialogbox() {
       /* android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        alertDialog.setTitle("Version Changes");
        alertDialog.setMessage("Do you want to update latest version?");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
                GotoPlaystorePage();

            }

        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                StartDownloadData();
            }
        });
        alertDialog.show();*/
        /*android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(this);
        final android.app.AlertDialog dialog = alertDialog.create();
        View view = LayoutInflater.from(this).inflate(R.layout.custom_alert_version_change, null);
        dialog.setView(view);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.getWindow().setLayout(50,100);
        Button btn_ok = view.findViewById(R.id.btn_alert_version_ok);
        Button btn_no = view.findViewById(R.id.btn_alert_version_no);
        dialog.setCancelable(false);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                GotoPlaystorePage();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                StartDownloadData();
            }
        });
        dialog.show();*/

        final Dialog dialog = new Dialog(_mcontext,R.style.YourCustomStyle);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.custom_alert_version_change);
     /*   View view = LayoutInflater.from(this).inflate(R.layout.custom_alert_version_change, null);
        dialog.setContentView(view);*/


        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER);
        Button btn_ok = dialog.findViewById(R.id.btn_alert_version_ok);
        Button btn_no = dialog.findViewById(R.id.btn_alert_version_no);
        dialog.setCancelable(false);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                GotoPlaystorePage();
            }
        });
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                StartDownloadData();
            }
        });
        dialog.show();

    }

    //endregion
    //region GoTo playstorePage
    public void GotoPlaystorePage() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
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
        String registrationId = pref_name.getString(getString(R.string.pref_gcm_property_reg_id), "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        int registeredVersion = pref_name.getInt(getString(R.string.pref_gcm_appversion), Integer.MIN_VALUE);
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
        pref_name.edit()
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

    public void editpreferencedownload_flag(){
        pref_constant.edit().putBoolean(getString(R.string.pref_permanent_clinic_download_flag),download_flag).apply();
    }
    //endregion

    //region check permission
    public boolean checkPermission() {

        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA);
       // int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_CONTACTS);
        int ThirdPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        int fourthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int fifthPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        int writestorageResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int readstorageResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        int getaccountResult = ContextCompat.checkSelfPermission(getApplicationContext(), GET_ACCOUNTS);
        int wakelockResult = ContextCompat.checkSelfPermission(getApplicationContext(), WAKE_LOCK);
        int vibrateResult = ContextCompat.checkSelfPermission(getApplicationContext(), VIBRATE);
     //   int notificationResult = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_NOTIFICATION_POLICY);

        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ThirdPermissionResult == PackageManager.PERMISSION_GRANTED &&
                fourthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                fifthPermissionResult == PackageManager.PERMISSION_GRANTED &&
                writestorageResult == PackageManager.PERMISSION_GRANTED &&
                readstorageResult == PackageManager.PERMISSION_GRANTED &&
                getaccountResult == PackageManager.PERMISSION_GRANTED &&
                wakelockResult == PackageManager.PERMISSION_GRANTED &&
                vibrateResult == PackageManager.PERMISSION_GRANTED
             //   notificationResult == PackageManager.PERMISSION_GRANTED
                ;
    }
    private void requestPermission() {

        ActivityCompat.requestPermissions(ActivitySplashScreen.this, new String[]
                {
                        Manifest.permission.CAMERA,
                        READ_PHONE_STATE,
                        ACCESS_COARSE_LOCATION,
                        ACCESS_FINE_LOCATION,
                        WRITE_EXTERNAL_STORAGE,
                        READ_EXTERNAL_STORAGE,
                        GET_ACCOUNTS,
                        WAKE_LOCK,
                        VIBRATE
                     //   Manifest.permission.ACCESS_NOTIFICATION_POLICY

                }, RequestPermissionCode);

    }
    //endregion
    @Override
    public void onRequestPermissionsResult(final int requestCode, @NonNull final String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    final boolean CameraPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                  //  boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessCoarsePermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                    boolean AccessFinePermission = grantResults[3] == PackageManager.PERMISSION_GRANTED;
                    boolean writestoragePermission = grantResults[4] == PackageManager.PERMISSION_GRANTED;
                    boolean readstoragePermission = grantResults[5] == PackageManager.PERMISSION_GRANTED;
                    boolean getaccountPermission = grantResults[6] == PackageManager.PERMISSION_GRANTED;
                    boolean wakelockPermission = grantResults[7] == PackageManager.PERMISSION_GRANTED;
                    boolean vibratePermission = grantResults[8] == PackageManager.PERMISSION_GRANTED;
                  //  boolean notificationPermission = grantResults[9] == PackageManager.PERMISSION_GRANTED;
                    Log.e("permission","camera :" + CameraPermission +", phonestate :" + ReadPhoneStatePermission);
                    Log.e("permission","accesscoarse :" + AccessCoarsePermission +", accessfine :" + AccessFinePermission);
                    Log.e("permission","writestorge :" + writestoragePermission +", readstorage :" + readstoragePermission);
                    Log.e("permission","getaccount :" + getaccountPermission +", wakelock :" + wakelockPermission);
                    Log.e("permission","vibrate :" + vibratePermission +", notification :"   );

                    if (CameraPermission  && ReadPhoneStatePermission && AccessCoarsePermission && AccessFinePermission &&
                            writestoragePermission && readstoragePermission && getaccountPermission && wakelockPermission &&
                            vibratePermission  ) {
                     //   Toast.makeText(ActivitySplashScreen.this, "Permission Granted", Toast.LENGTH_LONG).show();
                        boolean isLoggedIn = pref_name.getBoolean(getString(R.string.pref_is_logged_in), false);
                        if (!isLoggedIn) {
                            Intent intent = new Intent(_mcontext, ActivityCreateAccount.class);
                            intent.putExtra("from", "splash_screen");
                            ActivitySplashScreen._activity.startActivity(intent);
                            ActivitySplashScreen._activity.finish();
                        } else {
                            Intent intent = new Intent(_mcontext, ActivityHome.class);
                            intent.putExtra("successRecord", 0);
                            ActivitySplashScreen._activity.startActivity(intent);
                            ActivitySplashScreen._activity.finish();
                        }
                    }
                    else {
                       // requestPermission();
                        String message = "You need to allow access all permission";
                        showMessageOKCancel(message, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               requestPermission();
                            }
                        });

                     //   Toast.makeText(ActivitySplashScreen.this,"Permission Denied",Toast.LENGTH_LONG).show();
                    }
                }

                break;
        }
    }
    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ActivitySplashScreen.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

}
