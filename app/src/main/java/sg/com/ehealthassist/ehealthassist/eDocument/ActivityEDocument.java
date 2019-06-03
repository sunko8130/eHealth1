package sg.com.ehealthassist.ehealthassist.eDocument;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;


import java.io.File;

import sg.com.ehealthassist.ehealthassist.Account.ActivityCreateAccount;
import sg.com.ehealthassist.ehealthassist.ActivityHome;
import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.CustomUI.MaterialTab;
import sg.com.ehealthassist.ehealthassist.CustomUI.MaterialTabHost;
import sg.com.ehealthassist.ehealthassist.CustomUI.MaterialTabListener;
import sg.com.ehealthassist.ehealthassist.DB.DBCertsCapture;
import sg.com.ehealthassist.ehealthassist.DB.DBFinCapture;
import sg.com.ehealthassist.ehealthassist.DB.DBLABCapture;
import sg.com.ehealthassist.ehealthassist.DB.DBRADCapture;
import sg.com.ehealthassist.ehealthassist.Models.EDocument.CertsCapture;
import sg.com.ehealthassist.ehealthassist.Models.EDocument.FinCapture;
import sg.com.ehealthassist.ehealthassist.Models.EDocument.LABCapture;
import sg.com.ehealthassist.ehealthassist.Models.EDocument.RADCapture;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityEDocument extends AppCompatActivity implements MaterialTabListener {

    MaterialTabHost tabHost;
    ViewPager pager;
    String tabs[];
    ImageButton toolbar_imgbutton_back,imgbtn_edocrefresh;
    ViewPagerAdapter adapter;
    TextView main_toolbar_title;
    DBLABCapture dbLabCapture;
    DBRADCapture dbradCapture;
    DBCertsCapture dbCertsCapture;
    DBFinCapture dbfinCapture;
    Context _mcontext;
    String memberid = "";
    SharedPreferences preferences = null;
    boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_edocument);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText("E Document");
        _mcontext = this;
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        memberid = preferences.getString(getString(R.string.pref_login_memberId), "");
        isLoggedIn = preferences.getBoolean(getString(R.string.pref_is_logged_in), false);
        Intent edocintent = getIntent();
        String from = edocintent.getStringExtra("from");
        dbLabCapture = new DBLABCapture(_mcontext);
        dbradCapture = new DBRADCapture(_mcontext);
        dbCertsCapture = new DBCertsCapture(_mcontext);
        dbfinCapture = new DBFinCapture(_mcontext);
        findViewById();
        setEvent();
        if (from.equals("ActivityShowCaptureImage")) {
            int position = edocintent.getIntExtra("Frag position",0);
            selectFragment(position);
        }
    }

    //region find & event
    public void findViewById() {
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        tabHost = (MaterialTabHost) findViewById(R.id.tabHostedoc);
        pager = (ViewPager) findViewById(R.id.edoc_pager);
        imgbtn_edocrefresh= (ImageButton)findViewById(R.id.imgbtn_edocrefresh);
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
    }

    public void setEvent(){
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
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnback();
            }
        });

        imgbtn_edocrefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(getFragmentRefreshListener()!=null){
                    getFragmentRefreshListener().onRefresh();
                }
              // pager.getAdapter().notifyDataSetChanged();
            }
        });
    }
    //endregion

    //region Tabselected()

    public void selectFragment(int position) {
        pager.setCurrentItem(position);
    }
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

    //region ViewPagerAdapter()
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            tabs = getResources().getStringArray(R.array.edoc_tabs);
        }

        public Fragment getItem(int num) {
            Fragment fragmentManager = null;
            switch (num) {
                case 0:
                    fragmentManager = new FragmentFin();
                    break;
                case 1:
                    fragmentManager = new FragmentLab();
                    break;
                case 2:
                    fragmentManager = new FragmentRad();
                    break;
                case 3:
                    fragmentManager = new FragmentCert();
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
            Log.e("position",tabs[position]);
            return tabs[position];
        }

      /*  @Override
        public int getItemPosition(Object object) {
            int  updateposition = pager.getCurrentItem();
            if(updateposition == 0){
                FragmentFin f = (FragmentFin) object;
                f.update();
            }
            return super.getItemPosition(object);
        }*/
    }
    //endregion

    //region onActivityResult
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            showtitleDialog(requestCode);

        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    //endregion

    //region Title & desc Dialog
    public void showtitleDialog(final int request){
        final Dialog edoc_dialog = new Dialog(_mcontext);
        edoc_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        edoc_dialog.setContentView(R.layout.dialogedocument);
        edoc_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button btnok = (Button) edoc_dialog.findViewById(R.id.btn_titlesubmit);
        Button btntitlecancel = edoc_dialog.findViewById(R.id.btn_titlecancel);
        final EditText edtedoctitle = (EditText) edoc_dialog.findViewById(R.id.edtedocTitle) ;
        final EditText edtedocdesc = (EditText)edoc_dialog.findViewById(R.id.edtedocdesc);

        //region OK
        btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedImagePath = "",selectedImagecreateDate="";
                int setfrag=0;
                String title = edtedoctitle.getText().toString();
                String desc = edtedocdesc.getText().toString();
                if(title.equals("")){
                    showDeleteDialog("Error",request);
                    edoc_dialog.dismiss();
                }else{
                    //region check position
                    switch (request){
                        case 1:
                            selectedImagePath = FragmentLab.getImagePath();
                            selectedImagecreateDate=FragmentLab.getImageCreatedate();
                            setfrag=1;
                            LABCapture objlab= new LABCapture(0,memberid,title,selectedImagecreateDate,desc,false);
                            dbLabCapture.addLABLog(objlab);
                            break;
                        case 2:
                            selectedImagePath = FragmentRad.getImagePath();
                            selectedImagecreateDate=FragmentRad.getImageCreatedate();
                            setfrag=2;
                            RADCapture objrad = new RADCapture(0,memberid,title,selectedImagecreateDate,desc,false);
                            dbradCapture.addRABLog(objrad);
                            break;
                        case 3:
                            selectedImagePath = FragmentCert.getImagePath();
                            selectedImagecreateDate=FragmentCert.getImageCreatedate();
                            setfrag=3;
                            CertsCapture objcerts = new CertsCapture(0,memberid,title,selectedImagecreateDate,desc,false);
                            dbCertsCapture.addCertsLog(objcerts);
                            break;
                        case 4:
                            selectedImagePath = FragmentFin.getImagePath();
                            selectedImagecreateDate=FragmentFin.getImageCreatedate();
                            setfrag=0;
                            FinCapture objfin = new FinCapture(0,memberid,title,selectedImagecreateDate,desc,false);
                            dbfinCapture.addFinLog(objfin);
                            break;
                    }
                    //endregion
                    edoc_dialog.dismiss();
                    finish();
                    Intent i = new Intent(_mcontext, ActivityShowCaptureImage.class);
                    i.putExtra("Frag position",setfrag);
                    i.putExtra("ceratedate",selectedImagecreateDate);
                    i.putExtra("path", selectedImagePath);
                    startActivity(i);
                }
            }
        });
        //endregion

        //region Cancel
        btntitlecancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedImagePath = "";
                switch (request){
                    case 1:
                        selectedImagePath = FragmentLab.getImagePath();
                        break;
                    case 2:
                        selectedImagePath = FragmentRad.getImagePath();
                        break;
                    case 3:
                        selectedImagePath = FragmentCert.getImagePath();
                        break;
                    case 4:
                        selectedImagePath = FragmentFin.getImagePath();
                        break;
                }
                if(!selectedImagePath.equals("")){
                    deleteimagefile(selectedImagePath);
                }
                edoc_dialog.dismiss();
            }
        });
        //endregion

        edoc_dialog.show();
    }

    public void showDeleteDialog(String title, final int requestcode) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Title cannot empty")
                .setTitle(title)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      showtitleDialog(requestcode);

                    }
                });
        alertBuilder.create().show();

    }

    public void deleteimagefile(String path){
        File imgFile = new File(path);
        if (imgFile.exists()) {
            imgFile.delete();
        }
    }
    //endregion

    //region back function
    public void returnback(){
        Intent intent = new Intent(getApplicationContext(), ActivityHome.class);
        startActivity(intent);
        finish();
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnback();
    }
    //endregion

    private FragmentRefreshListener fragmentRefreshListener;

    public FragmentRefreshListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }
    public void setFragmentRefreshListener(FragmentRefreshListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }
    public interface FragmentRefreshListener{
        void onRefresh();
    }

    public interface Updateable {
        public void update();
    }

}
