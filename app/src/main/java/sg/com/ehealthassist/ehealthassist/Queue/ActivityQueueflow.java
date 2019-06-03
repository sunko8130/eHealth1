package sg.com.ehealthassist.ehealthassist.Queue;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import sg.com.ehealthassist.ehealthassist.Account.ActivityLogIn;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityQueueflow extends AppCompatActivity {
    TextView main_toolbar_title;
    ImageButton toolbar_imgbutton_back;
    Button btn_proceed;
    Context _mcontext;
    String hecode = "", from_activity = "", clinic_name = "";
    int clinicid = 0;
    String usertoken = "";
    SharedPreferences pref = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_queueflow);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_activity_select_profile));
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        _mcontext = this;
        pref = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        Intent get_param = getIntent();
        clinicid = get_param.getIntExtra("clinicid", 0);
        hecode = get_param.getStringExtra("hecode");
        clinic_name = get_param.getStringExtra("clinicname");
        from_activity = get_param.getStringExtra("from_activity");
        btn_proceed = (Button) findViewById(R.id.btn_proceed);
        setEvent();
    }

    public void setEvent() {
        btn_proceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usertoken = pref.getString(getString(R.string.pref_login_Access_token), "");
                if (!usertoken.equals("")) {
                    Intent intent_regqueue = new Intent(_mcontext, ActivityRegisterQueue.class);
                    intent_regqueue.putExtra("clinicid", clinicid);
                    intent_regqueue.putExtra("hecode", hecode);
                    intent_regqueue.putExtra("clinicname", clinic_name);
                    intent_regqueue.putExtra("from_activity", "activitysearchclinicdetail");
                    startActivity(intent_regqueue);
                    finish();
                }
                else{
                    Intent intent = new Intent(_mcontext, ActivityLogIn.class);
                    intent.putExtra("CID", clinicid);
                    intent.putExtra("from", "Queueflow");
                    intent.putExtra("hecode", hecode);
                    intent.putExtra("clinic_name", clinic_name);
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


