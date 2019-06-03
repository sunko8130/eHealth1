package sg.com.ehealthassist.ehealthassist.Doctors;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyDoctorONDuty;
import sg.com.ehealthassist.ehealthassist.Models.Doctor.DoctorDutyList;
import sg.com.ehealthassist.ehealthassist.Models.Doctor.Doctors;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityDoctor_On_Duty extends AppCompatActivity {
    public SharedPreferences pref = null;
    int clinicId = 0;
    static Context context;
    String usertoken,clinic_name = "";
    private ListView mListView;
    ImageView imgreload;
    TextView dod_clinic_name,dutydate,main_toolbar_title;
    DoctorAdapter docadapter ;
    List<Doctors> doclist = new ArrayList<>();
    // Toolbar toolbar;
    CardView card_view;
    ImageButton toolbar_imgbutton_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_doctor__on__duty);
        main_toolbar_title = (TextView)findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText("Doctor On Duty");
        init();
        findViewbyid();
        setEvent();
        GetDutyofDoctor();
    }
    //region init()
    public void init() {
        pref = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        usertoken = pref.getString(getString(R.string.pref_login_Access_token), "");
        context = this;
        Intent intent = getIntent();
        clinicId = intent.getIntExtra("CID", 0);
        clinic_name = intent.getStringExtra("CName");
    }
    //endregion
    public void findViewbyid(){
        mListView = (ListView) findViewById(R.id.material_listview);
        imgreload = (ImageView) findViewById(R.id.img_reload);
        dod_clinic_name = (TextView)findViewById(R.id.dod_clinic_name);
        dutydate = (TextView)findViewById(R.id.dutydate);
        card_view =(CardView)findViewById(R.id.card_view);
        card_view.setVisibility(View.GONE);
        dod_clinic_name.setText(clinic_name);
        toolbar_imgbutton_back = (ImageButton)findViewById(R.id.toolbar_imgbackbutton);
    }
    public void setEvent(){
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //region GetDutyofDoctor()  call api
    public void GetDutyofDoctor() {
        RotateImage();
        //// **************** Call API ************
        if (Utils.hasInternetConnection(context)) {
            if (!pref.getBoolean(getString(R.string.pref_is_account_verified), false)) {
                showDialog(getString(R.string.error_response_invalidtoken_code));
            } else {
                VolleyDoctorONDuty _vdoctoronduty = new VolleyDoctorONDuty(context);
                _vdoctoronduty.GetDoctorOnDutyJson(clinicId, usertoken, new VolleyDoctorONDuty.VolleyCallback() {
                    @Override
                    public void onSuccess(DoctorDutyList result) {
                        imgreload.setAnimation(null);
                        imgreload.setVisibility(View.INVISIBLE);
                        dutydate.setText("Date :" +result.getDutydate());
                        doclist = result.getLstDoctors();
                        card_view.setVisibility(View.VISIBLE);
                        LoadData();
                    }
                    @Override
                    public void onFail(String errorcode, String errormsg) {
                        card_view.setVisibility(View.GONE);
                        showDialog(errorcode);
                    }
                });
            }
        } else {
            Utils.showInternetRequiredDialog(ActivityDoctor_On_Duty.this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
    }
    //endregion

    public void LoadData(){
        docadapter  = new DoctorAdapter(context,doclist);
        mListView.setAdapter(docadapter);
        docadapter.notifyDataSetChanged();
    }
    //region ShowDialog()
    public void showDialog(String errorcode) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Doctor On Duty");
        if (errorcode.equals(getString(R.string.error_response_invalidtoken_code))) {
            alertDialog.setMessage(getString(R.string.error_response_upload_profile_status_InvalidUserToken));
        } else {
            alertDialog.setMessage(getString(R.string.error_response_doctoronduty_no_information_avaliavle));
        }
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        alertDialog.show();
    }

    //endregion
    //region menu()
  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_doctor__on__duty, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                supportFinishAfterTransition();
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
    //endregion
    //region roatateimage()
    public void RotateImage() {
        imgreload.setVisibility(View.VISIBLE);
        RotateAnimation anim = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        anim.setInterpolator(new LinearInterpolator());
        anim.setRepeatCount(Animation.INFINITE);
        anim.setDuration(300);
        imgreload.startAnimation(anim);
    }

    //endregion
}

