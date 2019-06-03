package sg.com.ehealthassist.ehealthassist.Account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import sg.com.ehealthassist.ehealthassist.CustomUI.SpinnerStringAdapter;
import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.R;
import sg.com.ehealthassist.ehealthassist.ActivitySetting;

public class ActivitySetupPIN extends AppCompatActivity {
    //Toolbar toolbar;
    SharedPreferences preferences = null;
    EditText txtPIN, txtConfirmPIN;
    TextView textViewSetupPIN,main_toolbar_title;
    Button btnConfirmPIN;
    ArrayAdapter<CharSequence> adapterMinutesToDisablePIN;
    Spinner spinner;
    private int minutesToLockProfile;
    String[] setuppin_type;
    SpinnerStringAdapter adapterpin;
    ImageButton toolbar_imgbutton_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_setup_pin);

       /* toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/

        main_toolbar_title = (TextView)findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_setup_pin));
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        ActivityHome1.close();
        findViewsById();
        bindSpinner();
        loadData();
        setEvents();
    }

    //region findViewsById()
    private void findViewsById() {
        btnConfirmPIN = (Button) findViewById(R.id.buttonSetupConfirmPIN);
        txtPIN = (EditText) findViewById(R.id.editTextCreatePIN);
        txtConfirmPIN = (EditText) findViewById(R.id.editTextConfirmCreatePIN);
        spinner = (Spinner) findViewById(R.id.spinnerSetupPinMinutes);
        textViewSetupPIN = (TextView) findViewById(R.id.textViewSetupPIN);
        toolbar_imgbutton_back = (ImageButton)findViewById(R.id.toolbar_imgbackbutton);
    }

    //region setEvent()
    private void setEvents() {
        btnConfirmPIN.setOnClickListener(btnConfirmPINOnClickListener);
        txtPIN.addTextChangedListener(textWatcherPIN);
        txtConfirmPIN.addTextChangedListener(textWatcherConfirmPIN);
        spinner.setOnItemSelectedListener(spinnerItemSelectedListener);
        toolbar_imgbutton_back.setOnClickListener(imgbtntoolbarback);
    }
    //region toolbar back
    View.OnClickListener imgbtntoolbarback = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            returnback();
        }
    };
    //endregion
    //region btnConfirmPINOnClickListener()
    View.OnClickListener btnConfirmPINOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (txtPIN.getText().length() == 6) {
                String pin = txtPIN.getText().toString();
                String confirmPin = txtConfirmPIN.getText().toString();
                if (pin.equals(confirmPin)) {
                    if (minutesToLockProfile > 20) {
                        pin = "";
                    }
                    preferences.edit()
                            .putString(getString(R.string.pref_main_user_data_pin), pin)
                            .putInt(getString(R.string.pref_minutes_to_lock_medical_profile), minutesToLockProfile)
                            .apply();
                    setResult(RESULT_OK);
                    finish();
                    Intent gologin = new Intent(getApplicationContext(), ActivityPIN.class);
                    startActivity(gologin);
                } else {
                    Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
                    vibrator.vibrate(500);
                    txtConfirmPIN.setText("");
                    Toast.makeText(getApplicationContext(), R.string.err_pin_not_match, Toast.LENGTH_SHORT).show();
                }
            } else {
                txtPIN.setError(getString(R.string.err_invalid_pin_length));
            }
        }
    };
    //endregion
    //endregion
    //region LoadData()
    private void loadData() {
        String storedPin = preferences.getString(getString(R.string.pref_main_user_data_pin), "");
        if (!storedPin.equals("")) {
            int minutesToLock = preferences.getInt(getString(R.string.pref_minutes_to_lock_medical_profile), 21);
            String selectedValue = getSpinnerValueByMinutesToLockProfile(minutesToLock);
            txtPIN.setText(storedPin);
            txtConfirmPIN.setText(storedPin);
            setuppin_type= getResources().getStringArray(R.array.array_minutes_to_lock_profile);
            adapterpin = new SpinnerStringAdapter(this, R.layout.spinner_profile_selected,setuppin_type );
            spinner.setSelection(adapterpin.getPosition(selectedValue));
        }
    }

    private void bindSpinner() {
        adapterMinutesToDisablePIN = ArrayAdapter.createFromResource(this, R.array.array_minutes_to_lock_profile, R.layout.custom_spinner_style);
        adapterMinutesToDisablePIN.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapterMinutesToDisablePIN);
    }
    AdapterView.OnItemSelectedListener spinnerItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        protected Adapter initializeAdapter = null;

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (initializeAdapter != parent.getAdapter()) {
                initializeAdapter = parent.getAdapter();
                return;
            }
            minutesToLockProfile = setMinutesToLockProfileBySpinnerValue(parent, position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private int setMinutesToLockProfileBySpinnerValue(AdapterView<?> parent, int position) {
        String selectedValue = parent.getItemAtPosition(position).toString();
        switch (selectedValue) {
            case "Immediate":
                return 0;
            case "After 1 Minute":
                return 1;
            case "After 3 Minutes":
                return 3;
            case "After 5 Minutes":
                return 5;
            case "After 10 Minutes":
                return 10;
            case "After 15 Minutes":
                return 15;
            case "After 20 Minutes":
                return 20;
            default:
                return 21;
        }
    }

    private String getSpinnerValueByMinutesToLockProfile(int minutesToLockProfile) {
        switch (minutesToLockProfile) {
            case 0:
                return "Immediate";
            case 1:
                return "After 1 Minute";
            case 3:
                return "After 3 Minutes";
            case 5:
                return "After 5 Minutes";
            case 10:
                return "After 10 Minutes";
            case 15:
                return "After 15 Minutes";
            case 20:
                return "After 20 Minutes";
            default:
                return "Never";
        }
    }
    //endregion
    //region TextWatcher()
    TextWatcher textWatcherPIN = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 6) {
                txtConfirmPIN.requestFocus();
            } else {
                textViewSetupPIN.setText(getString(R.string.pin_message));
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
    TextWatcher textWatcherConfirmPIN = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 6) {
                if (txtPIN.getText().toString().equals(s.toString())) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txtConfirmPIN.getWindowToken(), 0);
                    spinner.requestFocus();
                } else {
                    txtConfirmPIN.setError(getString(R.string.err_pin_not_match));
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void returnback() {
        Intent intent = new Intent(getApplicationContext(), ActivitySetting.class);
        startActivity(intent);
        finish();
    }
    //endregion
}
