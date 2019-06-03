package sg.com.ehealthassist.ehealthassist.Account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.Profiles.ActivityMedProfileList;
import sg.com.ehealthassist.ehealthassist.Queue_Appt.ActivityQueue_Appoint;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityPIN extends AppCompatActivity {
    // Toolbar toolbar;
    SharedPreferences preferences = null;
    Button btnPin1, btnPin2, btnPin3, btnPin4, btnPin5, btnPin6, btnPin7, btnPin8, btnPin9, btnPin0, btnPinClear, btnPinBackspace;
    ImageView iv1, iv2, iv3, iv4, iv5, iv6;
    private int minutesToLockProfile = 21;
    private String inputPin = "", callNext, callBack;
    TextView main_toolbar_title;
    ImageButton toolbar_imgbutton_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pin);
        /*toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        main_toolbar_title = (TextView)findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_activity_pin));
        findViewsById();
        ActivityHome1.close();
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        minutesToLockProfile = preferences.getInt(getString(R.string.pref_minutes_to_lock_medical_profile), 21);
        boolean isPinRequired = minutesToLockProfile <= 20;
        boolean isLoggedIn = preferences.getBoolean(getString(R.string.pref_is_logged_in), false);
        if (!isLoggedIn) {
            Intent loginIntent = new Intent(getApplicationContext(), ActivityLogIn.class);
            loginIntent.putExtra("from", "pin");
            loginIntent.putExtra("CID", 0);
            startActivity(loginIntent);
            finish();
            return;
        }
        callNext = getIntent().getStringExtra("CallNext");
        callBack = getIntent().getStringExtra("CallBack");
        Intent intent = getIntentByCallBackName();
        if (isPinRequired) {
            if (!IsLastPinSessionExpired()) {
                startActivity(intent);
                finish();
            }
        } else {
            startActivity(intent);
            finish();
        }
        btnPin0.setOnClickListener(numberPadOnClickListener);
        btnPin1.setOnClickListener(numberPadOnClickListener);
        btnPin2.setOnClickListener(numberPadOnClickListener);
        btnPin3.setOnClickListener(numberPadOnClickListener);
        btnPin4.setOnClickListener(numberPadOnClickListener);
        btnPin5.setOnClickListener(numberPadOnClickListener);
        btnPin6.setOnClickListener(numberPadOnClickListener);
        btnPin7.setOnClickListener(numberPadOnClickListener);
        btnPin8.setOnClickListener(numberPadOnClickListener);
        btnPin9.setOnClickListener(numberPadOnClickListener);
        btnPinClear.setOnClickListener(numberPadOnClickListener);
        btnPinBackspace.setOnClickListener(numberPadOnClickListener);
        toolbar_imgbutton_back.setOnClickListener(toolbarbcakOnClicListner);
    }

    //region findViewsById()
    private void findViewsById() {
        iv1 = (ImageView) findViewById(R.id.ivPIN1);
        iv2 = (ImageView) findViewById(R.id.ivPIN2);
        iv3 = (ImageView) findViewById(R.id.ivPIN3);
        iv4 = (ImageView) findViewById(R.id.ivPIN4);
        iv5 = (ImageView) findViewById(R.id.ivPIN5);
        iv6 = (ImageView) findViewById(R.id.ivPIN6);

        btnPin1 = (Button) findViewById(R.id.buttonPinPad1);
        btnPin2 = (Button) findViewById(R.id.buttonPinPad2);
        btnPin3 = (Button) findViewById(R.id.buttonPinPad3);
        btnPin4 = (Button) findViewById(R.id.buttonPinPad4);
        btnPin5 = (Button) findViewById(R.id.buttonPinPad5);
        btnPin6 = (Button) findViewById(R.id.buttonPinPad6);
        btnPin7 = (Button) findViewById(R.id.buttonPinPad7);
        btnPin8 = (Button) findViewById(R.id.buttonPinPad8);
        btnPin9 = (Button) findViewById(R.id.buttonPinPad9);
        btnPin0 = (Button) findViewById(R.id.buttonPinPad0);
        btnPinClear = (Button) findViewById(R.id.buttonPinPadClear);
        btnPinBackspace = (Button) findViewById(R.id.buttonPinPadBackspace);
        toolbar_imgbutton_back = (ImageButton)findViewById(R.id.toolbar_imgbackbutton);
    }

    //endregion
    //region getIntentByCallBackName()
    private Intent getIntentByCallBackName() {
        Intent intent;
        if (callNext == null) {
            intent = new Intent(getApplicationContext(), ActivityMedProfileList.class);
        } else if (callNext.equals("ActivitySetupPIN")) {
            intent = new Intent(getApplicationContext(), ActivitySetupPIN.class);
        } else if (callNext.equals("ActivityMedicalProfile")) {
            intent = new Intent(getApplicationContext(), ActivityMedProfileList.class);
        } else {
            intent = new Intent(getApplicationContext(), ActivityMedProfileList.class);
        }
        intent.putExtra("CallBack", callBack);
        return intent;
    }

    //endregion
    //region Events()
    View.OnClickListener numberPadOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.buttonPinPad0:
                    inputPin += "0";
                    break;
                case R.id.buttonPinPad1:
                    inputPin += "1";
                    break;
                case R.id.buttonPinPad2:
                    inputPin += "2";
                    break;
                case R.id.buttonPinPad3:
                    inputPin += "3";
                    break;
                case R.id.buttonPinPad4:
                    inputPin += "4";
                    break;
                case R.id.buttonPinPad5:
                    inputPin += "5";
                    break;
                case R.id.buttonPinPad6:
                    inputPin += "6";
                    break;
                case R.id.buttonPinPad7:
                    inputPin += "7";
                    break;
                case R.id.buttonPinPad8:
                    inputPin += "8";
                    break;
                case R.id.buttonPinPad9:
                    inputPin += "9";
                    break;
                case R.id.buttonPinPadClear:
                    inputPin = "";
                    break;
                case R.id.buttonPinPadBackspace:
                    if (inputPin.length() > 0) {
                        inputPin = inputPin.substring(0, inputPin.length() - 1);
                    }
                    break;
            }
            setPinDisplayIcon();
            if (inputPin.length() == 6) {
                String storedPin = preferences.getString(getString(R.string.pref_main_user_data_pin), "");
                if (inputPin.equals(storedPin)) {
                    preferences.edit().putLong(getString(R.string.pref_user_last_pin_login_time_millis), System.currentTimeMillis()).apply();
                    Intent intent = getIntentByCallBackName();
                    startActivity(intent);
                    finish();
                } else {
                    inputPin = "";
                    setPinDisplayIcon();
                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(300);
                    Toast.makeText(getApplicationContext(), R.string.err_invalid_pin, Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    //endregion
    //region toolbar image back button
    View.OnClickListener toolbarbcakOnClicListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            returnback();
        }
    };
    //endregion
    //region setPinDisplayIcon()
    private void setPinDisplayIcon() {
        int pinLength = inputPin.length();
        iv1.setImageResource(R.drawable.ic_pin_empty);
        iv2.setImageResource(R.drawable.ic_pin_empty);
        iv3.setImageResource(R.drawable.ic_pin_empty);
        iv4.setImageResource(R.drawable.ic_pin_empty);
        iv5.setImageResource(R.drawable.ic_pin_empty);
        iv6.setImageResource(R.drawable.ic_pin_empty);
        switch (pinLength) {
            case 1:
                iv1.setImageResource(R.drawable.ic_pin_filled);
                break;
            case 2:
                iv1.setImageResource(R.drawable.ic_pin_filled);
                iv2.setImageResource(R.drawable.ic_pin_filled);
                break;
            case 3:
                iv1.setImageResource(R.drawable.ic_pin_filled);
                iv2.setImageResource(R.drawable.ic_pin_filled);
                iv3.setImageResource(R.drawable.ic_pin_filled);
                break;
            case 4:
                iv1.setImageResource(R.drawable.ic_pin_filled);
                iv2.setImageResource(R.drawable.ic_pin_filled);
                iv3.setImageResource(R.drawable.ic_pin_filled);
                iv4.setImageResource(R.drawable.ic_pin_filled);
                break;
            case 5:
                iv1.setImageResource(R.drawable.ic_pin_filled);
                iv2.setImageResource(R.drawable.ic_pin_filled);
                iv3.setImageResource(R.drawable.ic_pin_filled);
                iv4.setImageResource(R.drawable.ic_pin_filled);
                iv5.setImageResource(R.drawable.ic_pin_filled);
                break;
            case 6:
                iv1.setImageResource(R.drawable.ic_pin_filled);
                iv2.setImageResource(R.drawable.ic_pin_filled);
                iv3.setImageResource(R.drawable.ic_pin_filled);
                iv4.setImageResource(R.drawable.ic_pin_filled);
                iv5.setImageResource(R.drawable.ic_pin_filled);
                iv6.setImageResource(R.drawable.ic_pin_filled);
                break;
        }
    }

    //endregion
    //region IsLastPinSessionExpired()
    private boolean IsLastPinSessionExpired() {
        long lastSuccessLogin = preferences.getLong(getString(R.string.pref_user_last_pin_login_time_millis), 0);
        int pinSessionExpired = minutesToLockProfile * 60000;
        return ((lastSuccessLogin + pinSessionExpired) < System.currentTimeMillis());
    }

    //endregion
    //region onKeyDown()
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void returnback() {
        ActivityQueue_Appoint.close();
        Intent intent = new Intent(ActivityPIN.this, ActivityHome1.class);
        intent.putExtra("from","ActivityPIN");
        startActivity(intent);
    }
    //endregion
}
