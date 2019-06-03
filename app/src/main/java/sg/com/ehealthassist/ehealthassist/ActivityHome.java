package sg.com.ehealthassist.ehealthassist;

import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;

import org.json.JSONObject;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestLogout;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyLogout;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyMedicalDispenseCount;
import sg.com.ehealthassist.ehealthassist.Account.ActivityCreateAccount;
import sg.com.ehealthassist.ehealthassist.Account.ActivityMyAccountInfo;
import sg.com.ehealthassist.ehealthassist.Account.ActivityPIN;
import sg.com.ehealthassist.ehealthassist.Account.ActivitySingup;
import sg.com.ehealthassist.ehealthassist.Clinic.ActivityNearby_panel;
import sg.com.ehealthassist.ehealthassist.Clinic.ActivitySearchClinicDetailed;
import sg.com.ehealthassist.ehealthassist.CustomUI.CustomGridView;
import sg.com.ehealthassist.ehealthassist.DB.DBBookInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBQueueRequest;
import sg.com.ehealthassist.ehealthassist.DB.DBTestClinic;
import sg.com.ehealthassist.ehealthassist.Events.ActivityEvents;
import sg.com.ehealthassist.ehealthassist.Fragment.FragLogIn;
import sg.com.ehealthassist.ehealthassist.GDAA.GDAAConnect;
import sg.com.ehealthassist.ehealthassist.GDAA.GDAAService;
import sg.com.ehealthassist.ehealthassist.GDAA.GDAAUT;
import sg.com.ehealthassist.ehealthassist.Haze.ActivityHazeInfo;
import sg.com.ehealthassist.ehealthassist.Home.HomeMenuAdapter;
import sg.com.ehealthassist.ehealthassist.Medication.ActivityMedicalDispense;
import sg.com.ehealthassist.ehealthassist.Messages.ActivityMessageList;
import sg.com.ehealthassist.ehealthassist.Models.Appointment.BookInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Profiles.ActivityMedicalProfile;
import sg.com.ehealthassist.ehealthassist.Queue_Appt.ActivityQueue_Appoint;
import sg.com.ehealthassist.ehealthassist.eDocument.ActivityEDocument;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by User on 12/7/2017.
 */

public class ActivityHome extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, GDAAConnect.ConnectCBs {

    ImageView img_user_profile;
    TextView txt_user_name;
    LinearLayout ll_user_profile_header;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView img_toolbar_back;
    TextView txt_toolbar_title;

    SharedPreferences preferences;
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    Context context;
    public static Activity homeactivity;
    public static boolean activity_detect = false;
    DBClinicInfo databaseHandlerClinicInfo;
    DBTestClinic dbHandler_testclinic;
    private static final int REQ_ACCPICK = 1;
    private static final int REQ_CONNECT = 2;
    boolean backup_drive = true;
    String usertoken = "";int remindercount = 0; String gcmid = "";
    DBClinicInfo dbHandler_clinicinfo;
    int recordUpdatedCount;
    List<ClinicInfo> get_test_clinic;
    Menu mMenu;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("font/domine_regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());


        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        context = getApplicationContext();
        usertoken = preferences.getString(getString(R.string.pref_login_Access_token),"");
        Log.d("usertoken", usertoken +" token");
        dbHandler_clinicinfo = new DBClinicInfo(this);
        databaseHandlerClinicInfo = new DBClinicInfo(this);
        dbHandler_testclinic = new DBTestClinic(this);
        get_test_clinic = dbHandler_testclinic.getallclinicinfo();


        //setSupportActionBar(toolbar);
        findsViewById();
        setDrawerToggle();
        checkDrawerOpenClose();
        callremindercount();

        /*img_toolbar_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(Gravity.START);
            }
        });*/

        preferences.edit().putBoolean(getString(R.string.pref_gdaa_connect_sync),false).apply();
        homeactivity = this;
        activity_detect = true;

        backup_drive = preferences.getBoolean(getString(R.string.pref_backup_drive_checked),true);
        close();
        if (savedInstanceState == null) {
            GDAAUT.init(this);
            if (!GDAAConnect.init(this)) {
                Log.e("arrived","GDAAConnect.init");
                startActivityForResult(AccountPicker.newChooseAccountIntent(null,
                        null, new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, true, null, null, null, null),
                        REQ_ACCPICK);
            }
        }

        Intent intent = getIntent();
        recordUpdatedCount = intent.getIntExtra("successRecord", 0);
        checkAccountLogin();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //setEvents();
            }
        },500);


    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    @Override
    protected void onStart() {
        callremindercount();
        super.onStart();
    }

    @Override
    protected void onResume() {
        callremindercount();
        super.onResume();
        GDAAConnect.connect();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        GDAAConnect.disconnect();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onConnOK() {
        preferences.edit()
                .putBoolean(getString(R.string.pref_gdaa_connect_sync),true).apply();
        GDAAConnect.onrequestSync();
        if(backup_drive) {
            startService(new Intent(this, GDAAService.class));
        }
    }

    @Override
    public void onConnFail(ConnectionResult connResult) {
        if (connResult != null  && connResult.hasResolution()) try {                    //UT.lg("connFail - has res");
            connResult.startResolutionForResult(this, REQ_CONNECT);
            return;  //++++++++++++++++++++++++++++++++++++++++++++++++++++++++>>>
        } catch (Exception e) { GDAAUT.le(e); }                                              //UT.lg("connFail - no res");
        suicide(R.string.err_author);  //---------------------------------->>>
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQ_ACCPICK:
                Log.e("arrived","REQ_ACCPICK");
                if (data != null && data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME) != null)
                    GDAAUT.AM.setEmail(data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME));
                if (!GDAAConnect.init(this))
                    suicide(R.string.err_author); //---------------------------------->>>
                break;
            case REQ_CONNECT:
                Log.e("arrived","REQ_CONNECT");
                if (requestCode == RESULT_OK)
                    GDAAConnect.connect();
                else
                    suicide(R.string.err_author);  //---------------------------------->>>
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void suicide(int rid) {
        GDAAUT.AM.setEmail(null);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkDrawerOpenClose() {
        if (drawer.isDrawerOpen(Gravity.START))
        {
            toolbar.setForegroundGravity(Gravity.CENTER_HORIZONTAL);
            toolbar.setTitle("Find");
        }
    }

    private void findsViewById() {
        img_user_profile = (ImageView)findViewById(R.id.img_user_profile);
        txt_user_name = (TextView)findViewById(R.id.txt_user_name);
        ll_user_profile_header = (LinearLayout)findViewById(R.id.ll_user_profile_header);
        toolbar = (Toolbar)findViewById(R.id.toolbar_main);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        gcmid = preferences.getString(getString(R.string.pref_gcm_property_reg_id), "");
        /*img_toolbar_back = findViewById(R.id.toolbar_imgbackbutton);
        txt_toolbar_title = findViewById(R.id.main_toolbar_title);*/
    }

    private void checkAccountLogin() {
        mMenu = navigationView.getMenu();
        if (!usertoken.equals("")) {
            mMenu.findItem(R.id.nav_account).setVisible(false);
            LinearLayout ll = (LinearLayout) navigationView.getHeaderView(0);
            ImageView mm = ll.findViewById(R.id.img_user_profile);
            TextView tt = ll.findViewById(R.id.txt_user_name);
            mm.setVisibility(View.VISIBLE);
            tt.setVisibility(View.VISIBLE);
            //ll_user_profile_header.setVisibility(View.VISIBLE);
            // Toast.makeText(getApplicationContext(), recordUpdatedCount + " clinic info updated.", Toast.LENGTH_LONG).show();

        }
        else {
            mMenu.findItem(R.id.nav_profile).setVisible(false);
            mMenu.findItem(R.id.nav_queue).setVisible(false);
            mMenu.findItem(R.id.nav_appointment).setVisible(false);
            mMenu.findItem(R.id.nav_medical_reminder).setVisible(false);
            mMenu.findItem(R.id.nav_edocument).setVisible(false);
            mMenu.findItem(R.id.nav_clinic_message).setVisible(false);
            mMenu.findItem(R.id.nav_log_out).setVisible(false);
        }
    }



    private void setDrawerToggle() {
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item))
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void callremindercount(){
        Log.d("usertoken", "ok");
        if(Utils.hasInternetConnection(context)){
            Log.d("usertoken", "internet");
            if(!usertoken.equals("")){
                Log.d("usertoken", "have");
                VolleyMedicalDispenseCount v_medcount = new VolleyMedicalDispenseCount(context);
                v_medcount.GetMedDispensCount(usertoken, new VolleyMedicalDispenseCount.VolleyCallback() {
                    @Override
                    public void onSuccess(int count) {
                        remindercount = count;
                        Log.d("usertoken", count + " count");
                        //loadmenu();
                    }
                    @Override
                    public void onFail(String errorcode, String errormessage) {
                        Log.e(errorcode,errormessage);
                        Log.d("usertoken", errormessage + " message");
                    }
                });
            }
            else {
                Log.d("usertoken", "not equal");
            }
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent = null;
        switch (item.getItemId())
        {
            case R.id.nav_find_clinic:
                intent = new Intent(getApplicationContext(), ActivityNearby_panel.class);
                break;

            case R.id.nav_add_favourite:
                intent=new Intent(getApplicationContext(),TestActivitty.class);
                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(Gravity.START);
                break;

            case R.id.nav_bio_data:
                //intent = new Intent(getApplicationContext(), ActivityMedicalProfile.class);
                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_LONG).show();
                drawer.closeDrawer(Gravity.START);
                break;

            case R.id.nav_vital_sign:
                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(Gravity.START);
                break;

            case R.id.nav_lab_result:
                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(Gravity.START);
                break;

            case R.id.nav_queue:
                intent = new Intent(getApplicationContext(), ActivityQueue_Appoint.class);
                intent.putExtra("from", "ActivityHome1");
                break;

            case R.id.nav_appointment:
                intent = new Intent(getApplicationContext(), ActivityQueue_Appoint.class);
                intent.putExtra("from", "ActivityHome1");
                break;

            case R.id.nav_medical_reminder:
                intent = new Intent(getApplicationContext(), ActivityMedicalDispense.class);
                intent.putExtra("from_activity","ActivityHome1");
                break;

            case R.id.nav_edocument:
                intent = new Intent(getApplicationContext(), ActivityEDocument.class);
                intent.putExtra("from","ActivityHome1");
                break;

            case R.id.nav_clinic_message:
                intent = new Intent(getApplicationContext(), ActivityMessageList.class);
                intent.putExtra("from","ActivityHome1");
                break;

            case R.id.nav_create_account:
                intent = new Intent(getApplicationContext(), ActivitySingup.class);
                break;

            case R.id.nav_login:
                intent = new Intent(getApplicationContext(), ActivityCreateAccount.class);
                intent.putExtra("from", "login");
                break;

            case R.id.nav_event:
                intent = new Intent(getApplicationContext(), ActivityEvents.class);
                break;

            case R.id.nav_haze_info:
                intent = new Intent(getApplicationContext(), ActivityHazeInfo.class);
                break;

            case R.id.nav_setting:
                intent = new Intent(getApplicationContext(), ActivitySetting.class);
                break;

            case R.id.nav_help_and_support:
                Toast.makeText(getApplicationContext(), "Coming Soon", Toast.LENGTH_SHORT).show();
                drawer.closeDrawer(Gravity.START);
                break;

            case R.id.nav_log_out:
                //intent = new Intent(getApplicationContext(), ActivitySetting.class);
                callLogoutDialog();
                break;
        }
        if (intent != null)
        {
            startActivity(intent);
            drawer.closeDrawer(Gravity.START);
        }
        return true;
    }

    private void callLogoutDialog() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ActivityHome.this);
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
                            if(Utils.hasInternetConnection(getApplicationContext())){
                                RequestLogout reqjson = new RequestLogout("1",gcmid);
                                JSONObject jsonparam = reqjson.StringtoJsonObject(reqjson.ObjecttoJson());
                                VolleyLogout v_logout = new VolleyLogout(getApplicationContext());
                                v_logout.PostLogOutJsonData(usertoken,jsonparam);
                            }
                            preferences.edit().clear().commit();
                            DBBookInfo deletebook = new DBBookInfo(getApplicationContext());
                            ArrayList<BookInfo> booklist = deletebook.getRequestLog();
                            for (int d = 0; d < booklist.size(); d++) {
                                Utils.CancleAlarm(getApplicationContext(), booklist.get(d).getId());
                            }
                            deletebook.deleteAllBooks();
                            DBQueueRequest deleterequest = new DBQueueRequest(getApplicationContext());
                            String currentdate = Utils.getcurrenttime("dd-MMM-yyyy hh:mm a");
                            deleterequest.deleteQueuebyCurrentDate(currentdate);
                            for(int cl = 0;cl <get_test_clinic.size();cl++){
                                dbHandler_clinicinfo.deleteclinicbyId(get_test_clinic.get(cl).get_id());
                            }
                            dbHandler_clinicinfo.deletetestclinic();

                            Intent intent = new Intent(getApplicationContext(), ActivityHome.class);
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
    }

    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    protected void onDestroy() {
        //  Log.e("enter","destory");
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(Gravity.START))
        {
            drawer.closeDrawer(Gravity.START);
        }
        else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finish();
                databaseHandlerClinicInfo.deletetestclinic();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();
            mHandler.postDelayed(mRunnable, 2000);
        }
    }

    public static void close() {
        if (ActivitySearchClinicDetailed.detect_act) {
            ActivitySearchClinicDetailed.mactivity.finish();
        }
        if (ActivityQueue_Appoint.activity_detect) {
            ActivityQueue_Appoint.mactivity.finish();
        }
    }
}
