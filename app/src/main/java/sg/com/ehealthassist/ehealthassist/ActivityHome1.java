package sg.com.ehealthassist.ehealthassist;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;

import java.util.Arrays;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyMedicalDispenseCount;
import sg.com.ehealthassist.ehealthassist.Account.ActivityPIN;
import sg.com.ehealthassist.ehealthassist.Clinic.ActivityPreferredClinic;
import sg.com.ehealthassist.ehealthassist.Clinic.ActivitySearchClinicDetailed;
import sg.com.ehealthassist.ehealthassist.CustomUI.CustomGridView;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Events.ActivityEvents;
import sg.com.ehealthassist.ehealthassist.Clinic.ActivityNearby_panel;
import sg.com.ehealthassist.ehealthassist.GDAA.GDAAConnect;
import sg.com.ehealthassist.ehealthassist.GDAA.GDAAService;
import sg.com.ehealthassist.ehealthassist.GDAA.GDAAUT;
import sg.com.ehealthassist.ehealthassist.Haze.ActivityHazeInfo;
import sg.com.ehealthassist.ehealthassist.Home.HomeMenuAdapter;
import sg.com.ehealthassist.ehealthassist.Medication.ActivityMedicalDispense;
import sg.com.ehealthassist.ehealthassist.Messages.ActivityMessageList;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Queue_Appt.ActivityQueue_Appoint;
import sg.com.ehealthassist.ehealthassist.eDocument.ActivityEDocument;

public class ActivityHome1 extends AppCompatActivity implements GDAAConnect.ConnectCBs {

    SharedPreferences preferences;
    ImageView bottomTabHome, bottomTabProfile, bottomTabClinic, bottomTabAppointment, bottomTabAbout;
    private boolean doubleBackToExitPressedOnce;
    private Handler mHandler = new Handler();
    Context context;
    public static Activity homeactivity;
    public static boolean activity_detect = false;
    CustomGridView menu_grid;
    HomeMenuAdapter homeadpter;
    String[] arry_homemenu;
    DBClinicInfo databaseHandlerClinicInfo;
   // private ViewPager home_images_pager;
  /*  private int[] mImageResources = {
            R.drawable.promotion1
    };*/
    private static final int REQ_ACCPICK = 1;
    private static final int REQ_CONNECT = 2;
    boolean backup_drive = true;
    String usertoken = "";int remindercount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            getSupportActionBar().hide();
        setContentView(R.layout.activity_home);
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        context = getApplicationContext();
        usertoken = preferences.getString(getString(R.string.pref_login_Access_token),"");
        callremindercount();

        preferences.edit().putBoolean(getString(R.string.pref_gdaa_connect_sync),false).apply();
        homeactivity = this;
        activity_detect = true;
        databaseHandlerClinicInfo = new DBClinicInfo(this);
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

        findViewById();
        // getRegisterId();
        loadmenu();
        Intent intent = getIntent();
        int recordUpdatedCount = intent.getIntExtra("successRecord", 0);
        if (recordUpdatedCount > 0) {
            // Toast.makeText(getApplicationContext(), recordUpdatedCount + " clinic info updated.", Toast.LENGTH_LONG).show();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setEvents();
            }
        },500);
       // LoadImageToVPager();

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
    //region findViewById()
    public void findViewById() {
        bottomTabHome = (ImageView) findViewById(R.id.iconHome);
        bottomTabProfile = (ImageView) findViewById(R.id.iconProfile);
        bottomTabClinic = (ImageView) findViewById(R.id.iconClinic);
        bottomTabAppointment = (ImageView) findViewById(R.id.iconAppt);
        bottomTabAbout = (ImageView) findViewById(R.id.iconAbout);
       // home_images_pager = (ViewPager) findViewById(R.id.pager_home);
        menu_grid = (CustomGridView) findViewById(R.id.menu_gridview);
    }

    //endregion
    //region ViewPager()
  /*  public void LoadImageToVPager() {
        mAdapter = new HomeViewPager(this, mImageResources);
        home_images_pager.setAdapter(mAdapter);
        home_images_pager.setCurrentItem(0);
        home_images_pager.setOnPageChangeListener(this);
        setTimer(home_images_pager);
    }

    final Handler handler = new Handler();
    public Timer swipeTimer;

    public void setTimer(final ViewPager myPager) {
        final int size = mImageResources.length;
        final Runnable Update = new Runnable() {
            int NUM_PAGES = size;
            int currentPage = 0;

            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                myPager.setCurrentItem(currentPage++, true);
            }
        };
        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 4000, 5000);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }*/
    //endregion

    //region setEvents()
    private void setEvents() {
        menu_grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = null;
                switch (position) {
                    case 0:
                        intent = new Intent(getApplicationContext(), ActivityNearby_panel.class);
                        break;
                    case 1:
                        intent = new Intent(getApplicationContext(), ActivityQueue_Appoint.class);
                        intent.putExtra("from", "ActivityHome1");
                        break;
                    case 2:
                        intent = new Intent(getApplicationContext(), ActivityEvents.class);
                        break;
                    case 3:
                        // Toast.makeText(getApplicationContext(), "COMING SOON.", Toast.LENGTH_SHORT).show();
                        //intent = new Intent(getApplicationContext(), ActivityEReceiptLists.class);
                        intent = new Intent(getApplicationContext(), ActivityEDocument.class);
                        intent.putExtra("from","ActivityHome1");
                        break;
                    case 4:
                        intent = new Intent(getApplicationContext(), ActivityHazeInfo.class);
                        break;
                    case 5:
                        intent = new Intent(getApplicationContext(), ActivityMedicalDispense.class);
                        intent.putExtra("from_activity","ActivityHome1");
                        // Toast.makeText(getApplicationContext(), "COMING SOON.", Toast.LENGTH_SHORT).show();
                        break;
                    case 6:
                        intent = new Intent(getApplicationContext(), ActivityMessageList.class);
                        intent.putExtra("from","ActivityHome1");
                        // Toast.makeText(getApplicationContext(), "COMING SOON.", Toast.LENGTH_SHORT).show();
                        break;
                    case 7:
                        Toast.makeText(getApplicationContext(), "COMING SOON.", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                    finish();
                }
            }
        });

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                String caller = v.getResources().getResourceEntryName(v.getId());
                switch (caller) {
                    case "iconProfile":
                        intent = new Intent(getApplicationContext(), ActivityPIN.class);
                        break;
                    case "iconClinic":
                        intent = new Intent(getApplicationContext(), ActivityPreferredClinic.class);
                        break;
                    case "iconAppt":
                        intent = new Intent(getApplicationContext(), ActivityQueue_Appoint.class);
                        intent.putExtra("from", "ActivityHome1");
                        break;
                    case "iconAbout":
                        intent = new Intent(getApplicationContext(), ActivitySetting.class);
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                    finish();
                    if (!caller.equals("iconProfile")) {
                        finish();
                    }
                }
            }
        };
        bottomTabHome.setOnClickListener(onClickListener);
        bottomTabProfile.setOnClickListener(onClickListener);
        bottomTabClinic.setOnClickListener(onClickListener);
        bottomTabAppointment.setOnClickListener(onClickListener);
        bottomTabAbout.setOnClickListener(onClickListener);
    }
    //endregion
    //region volley Reminder count
    public void callremindercount(){
        if(Utils.hasInternetConnection(context)){
            if(!usertoken.equals("")){
                VolleyMedicalDispenseCount v_medcount = new VolleyMedicalDispenseCount(context);
                v_medcount.GetMedDispensCount(usertoken, new VolleyMedicalDispenseCount.VolleyCallback() {
                    @Override
                    public void onSuccess(int count) {
                        remindercount = count;
                        Log.d("usertoken", count + " count");
                        loadmenu();
                    }
                    @Override
                    public void onFail(String errorcode, String errormessage) {
                        Log.e(errorcode,errormessage);
                        Log.d("usertoken", errormessage + " message");
                    }
                });
            }
        }
    }
    //endregion

    public boolean onCreateOptionsMenu(Menu menu) {
        Typeface face = Typeface.createFromAsset(this.getAssets(),"fonts/domine_regular.ttf");
        SpannableStringBuilder title = new SpannableStringBuilder(getBaseContext().getString(R.string.nav_clinic));
        title.setSpan(face,0,title.length(),0);
        menu.add(Menu.NONE,R.id.nav_find_clinic,0,title);
        return super.onCreateOptionsMenu(menu);
    }

    public void loadmenu(){
        arry_homemenu = getResources().getStringArray(R.array.array_menu);
        List<String> lst_menu = Arrays.asList(arry_homemenu);
        homeadpter = new HomeMenuAdapter(this, lst_menu,remindercount);
        menu_grid.setAdapter(homeadpter);
    }
    // region Exit()
    private final Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce = false;
        }
    };

    @Override
    protected void onDestroy() {
      //  Log.e("enter","destory");
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnable);
        }
    }

    public void onBackPressed() {
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

    public static void close() {
        if (ActivitySearchClinicDetailed.detect_act) {
            ActivitySearchClinicDetailed.mactivity.finish();
        }
        if (ActivityQueue_Appoint.activity_detect) {
            ActivityQueue_Appoint.mactivity.finish();
        }
    }
    //endregion/**/

}
