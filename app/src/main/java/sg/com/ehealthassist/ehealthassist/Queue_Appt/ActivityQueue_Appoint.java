package sg.com.ehealthassist.ehealthassist.Queue_Appt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import sg.com.ehealthassist.ehealthassist.ActivityHome;
import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.Appointment.FragmentAppointment;
import sg.com.ehealthassist.ehealthassist.Clinic.ActivitySearchClinicDetailed;
import sg.com.ehealthassist.ehealthassist.CustomUI.MaterialTab;
import sg.com.ehealthassist.ehealthassist.CustomUI.MaterialTabHost;
import sg.com.ehealthassist.ehealthassist.CustomUI.MaterialTabListener;
import sg.com.ehealthassist.ehealthassist.Queue.FragmentQueue;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityQueue_Appoint extends AppCompatActivity implements MaterialTabListener {

    Context mcontext;
    ImageButton toolbar_imgbutton_back;
    public static Activity mactivity;
    public static boolean activity_detect = false;
    MaterialTabHost tabHost;
    ViewPager pager;
    ViewPagerAdapter adapter;
    String tabs[];
    String fromactivity = "";
    TextView main_toolbar_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_queue__appoint);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.nav_my_queue_appt));
        mcontext = this;
        activity_detect = true;
        mactivity = this;
        close();
        findViewById();
        Intent intent = getIntent();
        fromactivity = intent.getStringExtra("from");
        setEvents();
        if (fromactivity.equals("ActivityDialogAppointment") || fromactivity.equals("ActivityAlarmshow")
                || fromactivity.equals("ActivityEApptCardDetail")) {
            selectFragment(1);
        }
    }

    //region findViewById()
    public void findViewById() {
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        tabHost = (MaterialTabHost) findViewById(R.id.tabHost);
        pager = (ViewPager) findViewById(R.id.myqueue_pager);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
    }
    //endregion
    //region setevent()
    public void setEvents() {
        toolbar_imgbutton_back.setOnClickListener(imgbtntoolbarbackOnClickListener);
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
                            .setTabListener(this));
        }
    }
    //region toolbar back button
    View.OnClickListener imgbtntoolbarbackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            returnback();
        }
    };
    //endregion

    public void selectFragment(int position) {
        pager.setCurrentItem(position);
    }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnback();
        return;
    }

    public void returnback() {
        Intent intent = new Intent(getApplicationContext(), ActivityHome.class);
        startActivity(intent);
        finish();
    }

    public static void close() {
        if (ActivitySearchClinicDetailed.detect_act) {
            ActivitySearchClinicDetailed.mactivity.finish();
        }
    }

    //endregion
    //region ViewPagerAdapter()
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.my_queue_appt);
        }

        public Fragment getItem(int num) {
            Fragment fragmentManager = null;
            switch (num) {
                case 0:
                    fragmentManager = new FragmentQueue();
                    break;
                case 1:
                    fragmentManager = new FragmentAppointment();
                    break;
            }
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


}

