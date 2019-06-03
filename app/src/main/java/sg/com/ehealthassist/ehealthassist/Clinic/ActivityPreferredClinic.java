package sg.com.ehealthassist.ehealthassist.Clinic;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Account.ActivityPIN;
import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.ActivitySplashScreen;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Queue_Appt.ActivityQueue_Appoint;
import sg.com.ehealthassist.ehealthassist.R;
import sg.com.ehealthassist.ehealthassist.ActivitySetting;

public class ActivityPreferredClinic extends AppCompatActivity {
    //   Toolbar toolbar;
    ImageView bottomTabHome, bottomTabProfile, bottomTabClinic, bottomTabAppointment, bottomTabAbout;
    Button btnSearchClinic;
    ImageButton toolbar_imgbutton_back;
    private Handler mHandler=new Handler();
    private boolean doubleBackToExitPressedOnce;
    public static Activity activity;
    public static boolean activity_detect = false;
    public static ListView listView;
    public static List<ClinicInfo> clinicInfos = null;
    TextView main_toolbar_title,txtdownloadaccount;
    RelativeLayout loadingPanel;
    final Handler   handler = new Handler();
    DBClinicInfo databaseHandlerClinicInfo ;
    SharedPreferences pref_constant = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_preferred_clinic);

        main_toolbar_title = (TextView)findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_activity_preferred_clinic));
        pref_constant = getSharedPreferences(getString(R.string.preference_constant), MODE_PRIVATE);
        activity_detect=true;
        ActivityHome1.close();
        activity=this;
        databaseHandlerClinicInfo = new DBClinicInfo(this);
        findViewById();
        setEvents();
        bindClinicInfoListView();

        handler.postDelayed(r, 1000);

    }
    //region findViewById()
    public void findViewById(){
        toolbar_imgbutton_back = (ImageButton)findViewById(R.id.toolbar_imgbackbutton);
        bottomTabHome = (ImageView) findViewById(R.id.iconHome);
        bottomTabProfile = (ImageView) findViewById(R.id.iconProfile);
        bottomTabClinic = (ImageView) findViewById(R.id.iconClinic);
        bottomTabAppointment = (ImageView) findViewById(R.id.iconAppt);
        bottomTabAbout = (ImageView) findViewById(R.id.iconAbout);
        listView = (ListView) findViewById(R.id.listViewPreferredClinic);
        btnSearchClinic = (Button) findViewById(R.id.btnSearchClinic);
        toolbar_imgbutton_back.setVisibility(View.GONE);
        loadingPanel = (RelativeLayout)findViewById(R.id.preferred_loadingPanel);
        txtdownloadaccount = (TextView)findViewById(R.id.txtdownloadaccount);
    }
    //endregion

    final Runnable r = new Runnable() {
        public void run() {
            //if(ActivitySplashScreen.download_flag){
            if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                loadingPanel.setVisibility(View.GONE);
                handler.removeCallbacks(r);
            }
            else {
                loadingPanel.setVisibility(View.VISIBLE);
                txtdownloadaccount.setText(ActivitySplashScreen.download_count);
            }
            handler.postDelayed(this, 1000);
        }
    };

    //region setEvent()
    public void setEvents(){
        bottomTabHome.setOnClickListener(bottomTabOnClickListener);
        bottomTabProfile.setOnClickListener(bottomTabOnClickListener);
        bottomTabClinic.setOnClickListener(bottomTabOnClickListener);
        bottomTabAppointment.setOnClickListener(bottomTabOnClickListener);
        bottomTabAbout.setOnClickListener(bottomTabOnClickListener);
        btnSearchClinic.setOnClickListener(bottomTabOnClickListener);

    }
    //endregion

    //region BottomTabOnClickListener()
    View.OnClickListener bottomTabOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = null;
            String caller = v.getResources().getResourceEntryName(v.getId());
            switch (caller) {
                case "iconHome":
                    intent = new Intent(getApplicationContext(), ActivityHome1.class);
                    break;
                case "iconProfile":
                    intent = new Intent(getApplicationContext(), ActivityPIN.class);
                    break;
                case "iconAbout":
                    intent = new Intent(getApplicationContext(), ActivitySetting.class);
                    break;
                case "iconAppt":
                    intent = new Intent(getApplicationContext(), ActivityQueue_Appoint.class);
                    intent.putExtra("from","ActivityPreferredClinic");
                    break;
                case "btnSearchClinic":
                    if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                        //if(ActivitySplashScreen.download_flag){
                        loadingPanel.setVisibility(View.GONE);
                        intent = new Intent(getApplicationContext(), ActivityNearby_panel.class);

                    }else{
                        loadingPanel.setVisibility(View.VISIBLE);
                        txtdownloadaccount.setText(ActivitySplashScreen.download_count);
                    }

                    break;
            }
            if (intent != null) {
                startActivity(intent);
                if (!caller.equals("iconProfile")) {
                    finish();
                }
            }
        }
    };
    //endregion
    //region bindClinicInfoListView()
    private void bindClinicInfoListView() {

        clinicInfos = databaseHandlerClinicInfo.getFavoriteClinicInfos();
        if (clinicInfos != null) {
            PreferredClinicAdapter clinicInfoAdapter = new PreferredClinicAdapter(this, clinicInfos, getString(R.string.ActivityPreferredClinic));
            listView.setAdapter(clinicInfoAdapter);
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    //  if(ActivitySplashScreen.download_flag){
                    if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                        int clinicId = Integer.parseInt(((TextView) view.findViewById(R.id.textViewClinicId)).getText().toString());
                        int colorId = ((TextView) view.findViewById(R.id.textViewClinicName)).getCurrentTextColor();
                        boolean isFavourite = (colorId == view.getResources().getColor(R.color.colorred));
                        DBClinicInfo dbHandler = new DBClinicInfo(activity);
                        int rowAffected = dbHandler.toggleClinicAsFavourite(clinicId, isFavourite);
                        if (rowAffected > 0) {
                            TextView txtClinicName = (TextView) view.findViewById(R.id.textViewClinicName);
                            if (!isFavourite) {
                                Toast.makeText(activity, "Added To My Clinic.", Toast.LENGTH_SHORT).show();
                                txtClinicName.setTextColor(view.getResources().getColor(R.color.colorred));
                            } else {
                                Toast.makeText(activity, "Removed From My Clinic.", Toast.LENGTH_SHORT).show();
                                txtClinicName.setTextColor(view.getResources().getColor(R.color.colorblack));
                            }
                        }
                    }

                    return true;
                }
            });
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //  if(ActivitySplashScreen.download_flag) {
                    if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                        int clinicId = Integer.parseInt(((TextView) view.findViewById(R.id.textViewClinicId)).getText().toString());
                        Intent intent = new Intent(getApplicationContext(), ActivitySearchClinicDetailed.class);
                        intent.putExtra("CID", clinicId);
                        intent.putExtra("Caller", getString(R.string.ActivityPreferredClinic));
                        startActivity(intent);
                        finish();
                    }

                }
            });

        } else {
            Toast.makeText(activity, "No Clinic found.", Toast.LENGTH_SHORT).show();
        }
    }
    //endregion
    // region Exit from App
    private final Runnable mRunnable=new Runnable() {
        @Override
        public void run() {
            doubleBackToExitPressedOnce=false;
        }
    };
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mHandler!=null){
            mHandler.removeCallbacks(mRunnable);

        }
        handler.removeCallbacks(r);
    }
    public void onBackPressed(){
        if(doubleBackToExitPressedOnce){
            super.onBackPressed();
            databaseHandlerClinicInfo.deletetestclinic();
            finish();
            return;
        }
        this.doubleBackToExitPressedOnce=true;
        Toast.makeText(this, "Please click Back again to exit", Toast.LENGTH_SHORT).show();
        mHandler.postDelayed(mRunnable,2000);
    }
    //endregion

}
