package sg.com.ehealthassist.ehealthassist.Profiles;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Clinic.ActivityClinicDeatail;
import sg.com.ehealthassist.ehealthassist.CustomUI.MaterialTab;
import sg.com.ehealthassist.ehealthassist.CustomUI.MaterialTabHost;
import sg.com.ehealthassist.ehealthassist.CustomUI.MaterialTabListener;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Medication.FragmentMedCondition;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Profile.ResponseDownloadStatus;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityMedicalProfile extends AppCompatActivity implements MaterialTabListener {

    MaterialTabHost tabHost;
    ViewPager pager;
    ImageButton toolbar_imgback_button;
    public static Activity _mactivity;
    public static boolean activity_detect = false;
    ViewPagerAdapter adapter;
    String from = "",hecode = "",clinic_name ="";
    int from_clinic_id,from_flag = 0,member,id;
    DBClinicInfo databaseHandlerClinicInfo;
    TextView main_toolbar_title;
    ArrayList<ResponseDownloadStatus> lst_downlaod=new ArrayList<ResponseDownloadStatus>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_activity_medical_profile);
        main_toolbar_title = (TextView)findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_medical_profile));
        _mactivity = this;
        activity_detect = true;
        databaseHandlerClinicInfo = new DBClinicInfo(getApplicationContext());
        Intent i = getIntent();
        from = i.getStringExtra("from");
        from_clinic_id = i.getIntExtra("clinic_id",0);
        member = i.getIntExtra("member",0);
        id = i.getIntExtra("id",0);
        if(from.equals("downloadprofilestatus")){
            lst_downlaod = i.getParcelableArrayListExtra("profile_status");
            hecode = i.getStringExtra("hecode");
            clinic_name = i.getStringExtra("clinicname");
        }

        findViewsById();
        setEvents();
    }
    //region findViewbyid()
    public void findViewsById() {
        toolbar_imgback_button=(ImageButton)findViewById(R.id.toolbar_imgbackbutton);
        tabHost = (MaterialTabHost) findViewById(R.id.tabHostmedical);
        pager = (ViewPager) findViewById(R.id.pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
    }
    //endregion
    //region setEvent()
    public void setEvents() {

        pager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                tabHost.setSelectedNavigationItem(position);
            }
        });
        for (int i = 0; i < adapter.getCount(); i++) {
            tabHost.addTab(
                    tabHost.newTab()
                            .setText(adapter.getPageTitle(i))
                            .setTabListener(this)
            );
        }

        toolbar_imgback_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });
    }
    //endregion
    //region ViewPagerAdapter class
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        String tabs[];

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.tabs);
        }

        public Fragment getItem(int num) {
            Bundle bundle = new Bundle();
            bundle.putString("from",from);
            bundle.putInt("clinic_id",from_clinic_id);
            bundle.putInt("member",member);
            bundle.putInt("id",id);

            Fragment fragmentManager = null;
            switch (num) {
                case 0:
                    fragmentManager = new FragmentBioData();
                    break;
                case 1:
                    fragmentManager = new FragmentMedCondition();
                    break;

            }
            fragmentManager.setArguments(bundle);
            return fragmentManager;
        }

        @Override
        public int getCount() {
            return tabs.length;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabs[position];
        }
    }
    //endregion
    //region Tabselected()
    @Override
    public void onTabSelected(MaterialTab tab) {
        pager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab tab) {

    }

    @Override
    public void onTabUnselected(MaterialTab tab) {

    }
    //endregion
    //region onActivityResult()
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
               // Intent intent_manual = new Intent(this, ActivityManualClinicDialog.class);
               // startActivity(intent_manual);

            } else {
                if (!result.getContents().matches("")) {
                    ClinicInfo findclinic= databaseHandlerClinicInfo.getClinicInfobyHECODE(result.getContents());
                    if(findclinic==null){
                        //Toast.makeText(this, "Clinic id doesn't exit", Toast.LENGTH_LONG).show();
                        Utils.showAlertDialog(this,"Alert","Invalid QR Code ,Please try with Another QR Code");
                    }else{
                        Intent intent_scan = new Intent(this, ActivityClinicDeatail.class);
                        intent_scan.putExtra("hecode", result.getContents());
                        startActivity(intent_scan);
                    }
                } else {
                    // Toast.makeText(this, "Invalid Clinic Id", Toast.LENGTH_LONG).show();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    //endregion
    //region keydown
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }
    public  void returnback(){
        if(from.equals("downloadprofilestatus")){
            Intent intent_download = new Intent(_mactivity, Activitydownloadprofilestatus.class);
            intent_download.putParcelableArrayListExtra("profile_status", lst_downlaod);
            intent_download.putExtra("memberid", member+"");
            intent_download.putExtra("hecode", hecode);
            intent_download.putExtra("clinicid",from_clinic_id );
            intent_download.putExtra("clinicname", clinic_name);
            startActivity(intent_download);
            finish();

        }else{
            Intent intent = new Intent(getApplicationContext(), ActivityMedProfileList.class);
            startActivity(intent);
            finish();
        }

    }
    //endregion

}

