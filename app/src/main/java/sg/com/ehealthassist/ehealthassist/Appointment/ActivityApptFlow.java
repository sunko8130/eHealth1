package sg.com.ehealthassist.ehealthassist.Appointment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import sg.com.ehealthassist.ehealthassist.Account.ActivityLogIn;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityApptFlow extends AppCompatActivity {
    TextView main_toolbar_title;
    ImageButton toolbar_imgbutton_back;
    Button btn_proceed;
    Context _mcontext;
    ImageView imgview_apptmode;
    int clinicid = 0;
    String clinic_name;
    String usertoken = "", appt_mode = "";
    SharedPreferences pref = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_appt_flow);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_activity_appointment_flow));
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        imgview_apptmode = (ImageView) findViewById(R.id.imgview_apptmode);
        btn_proceed = (Button) findViewById(R.id.btn_proceed);
        _mcontext = this;
        pref = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        Intent getintent = getIntent();
        clinicid = getintent.getIntExtra("CID", 0);
        clinic_name = getintent.getStringExtra("CName");
        appt_mode = getintent.getStringExtra("appt_mode");
        Log.e("appt_mode", appt_mode);
        if (appt_mode.equals("B")) {
            imgview_apptmode.setImageResource(R.drawable.flow_appointment_book);
        } else {
            imgview_apptmode.setImageResource(R.drawable.flow_appointment_request);
        }
        setEvent();
    }
    public void setEvent() {
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usertoken = pref.getString(getString(R.string.pref_login_Access_token), "");
                if (!usertoken.equals("")) {

                    Intent appt_intent = new Intent(_mcontext, ActivityAppointment.class);
                    appt_intent.putExtra("CID", Integer.valueOf(clinicid));
                    appt_intent.putExtra("CName", clinic_name);
                    appt_intent.putExtra("appt_mode", appt_mode);
                    startActivity(appt_intent);
                    finish();



                } else {
                    Intent intent = new Intent(_mcontext, ActivityLogIn.class);
                    intent.putExtra("CID", clinicid);
                    intent.putExtra("from", "Apptflow");
                    intent.putExtra("clinic_name", clinic_name);
                    intent.putExtra("appt_mode", appt_mode);
                    startActivity(intent);
                    finish();
                }
            }
        });
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
