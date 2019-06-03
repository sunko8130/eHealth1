package sg.com.ehealthassist.ehealthassist.Clinic;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import sg.com.ehealthassist.ehealthassist.ActivityHome;
import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.FragmentNearbyClinic;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityNearby_panel extends AppCompatActivity {
    TextView main_toolbar_title;
    ImageButton toolbar_imgbutton_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_nearby_panel);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_activity_nearby_doctor));
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        if (savedInstanceState == null) {
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
            trans.add(R.id.activity_nearby_panel, FragmentNearbyClinic.newInstance());
            trans.commit();
        }
        setEvent();
    }
    public void setEvent(){
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnback();
            }
        });
    }
    public void returnback() {
        Intent intent = new Intent(getApplicationContext(), ActivityHome.class);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }
}
