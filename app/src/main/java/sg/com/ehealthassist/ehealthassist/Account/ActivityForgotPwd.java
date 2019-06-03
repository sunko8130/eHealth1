package sg.com.ehealthassist.ehealthassist.Account;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestForgotPwd;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyCountryCode;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyForgotPassword;
import sg.com.ehealthassist.ehealthassist.CountryCode.CountryCodeAdapter;
import sg.com.ehealthassist.ehealthassist.DB.DBBookInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBQueueRequest;
import sg.com.ehealthassist.ehealthassist.Models.Appointment.BookInfo;
import sg.com.ehealthassist.ehealthassist.Models.Profile.CountryCode;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityForgotPwd extends AppCompatActivity {
    TextView txt_forgotpwd, main_toolbar_title;
    EditText edit_phone;
    //Button btn_forgotpwd, btn_countryiso, btn_countrycode;
    Button btn_forgotpwd_cancel, btn_forgotpwd_reset, btn_dropdowncode;
    ImageButton toolbar_imgbutton_back;
    Context mContext;
    String fromactivity = "";
    String login = "login";
    String accountinfo = "accountinfo";
    SharedPreferences preferences = null;
    ListView lvcountrycode;
    CountryCodeAdapter adpt_countrycode;
    ArrayList<CountryCode> lst_countrycode = new ArrayList<CountryCode>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgot_pwd);
        mContext = this;
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        Intent i = getIntent();
        fromactivity = i.getStringExtra("from");
        findviewbyid();
        callvolleyCountryCode();
        setEvent();
    }

    public void findviewbyid() {
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(R.string.title_activity_forgot_pwd);
        txt_forgotpwd = (TextView) findViewById(R.id.txtforgot_error);
        txt_forgotpwd.setVisibility(View.GONE);
        edit_phone = (EditText) findViewById(R.id.edit_phone);
        btn_dropdowncode = findViewById(R.id.btn_forgotPwddropdownccode);
        btn_forgotpwd_cancel = findViewById(R.id.btn_forgotpwd_cancel);
        btn_forgotpwd_reset = findViewById(R.id.btn_forgotpwd_reset);

    }

    public void setEvent() {
        btn_forgotpwd_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean hasError = false;
                if (edit_phone.getText().toString().length() <= 0) {
                    edit_phone.setError("Required");
                    hasError = true;
                }
                if (hasError) {
                    return;
                }
                if (!Utils.isValidSGMobile(edit_phone.getText())) {
                    edit_phone.setError(getText(R.string.err_invalid_mobile));
                    hasError = true;
                }
                if (hasError) {
                    return;
                }
                if (Utils.hasInternetConnection(mContext)) {
                    String dropdowncode = btn_dropdowncode.getText().toString();
                    String mobileno = dropdowncode.substring(3) + edit_phone.getText().toString();
                    RequestForgotPwd reqforgot = new RequestForgotPwd(mobileno);
                    JSONObject param = reqforgot.StringtoJsonObject(reqforgot.objecttoJson());
                    VolleyForgotPassword v_forgotpwd = new VolleyForgotPassword(mContext);
                    v_forgotpwd.GetForgotPwdRequest(param, new VolleyForgotPassword.VolleyCallback() {
                        @Override
                        public void onSuccess(String message) {
                            txt_forgotpwd.setVisibility(View.VISIBLE);
                            txt_forgotpwd.setText(message);
                            if (fromactivity.equals(accountinfo)) {
                                System.out.println(fromactivity);
                                preferences.edit().clear().commit();
                                DBBookInfo deletebook = new DBBookInfo(getApplicationContext());
                                ArrayList<BookInfo> booklist = deletebook.getRequestLog();
                                for (int d = 0; d < booklist.size(); d++) {
                                    Utils.CancleAlarm(mContext, booklist.get(d).getId());
                                }
                                deletebook.deleteAllBooks();
                                DBQueueRequest deleterequest = new DBQueueRequest(getApplicationContext());
                                String currentdate = Utils.getcurrenttime("dd-MMM-yyyy hh:mm a");
                                deleterequest.deleteQueuebyCurrentDate(currentdate);
                            }
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        Thread.sleep(1000);
                                        gotologinpage();
                                    } catch (Exception ex) {

                                    }
                                }
                            }).start();
                        }

                        @Override
                        public void onFail(String errorcode, String errormessage) {
                            txt_forgotpwd.setVisibility(View.VISIBLE);
                            txt_forgotpwd.setText(errormessage);
                        }
                    });
                } else {
                    Utils.showInternetRequiredDialog(mContext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                    return;
                }
            }
        });
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });

        btn_dropdowncode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCountryCode();
            }
        });
    }

    public void gotologinpage() {
        Intent loginIntent = new Intent(getApplicationContext(), ActivityCreateAccount.class);
        loginIntent.putExtra("from", "forgotpwd");
        loginIntent.putExtra("CID", 0);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(loginIntent);
        overridePendingTransition(0, 0);
        finish();
    }

    public void returnback() {
        Intent intent = null;
        if (fromactivity.equals(login)) {
            finish();
            intent = new Intent(getApplicationContext(), ActivityLogIn.class);
            intent.putExtra("from", "forgotpwd");
            intent.putExtra("CID", 0);
            startActivity(intent);
            finish();
        } else if (fromactivity.equals(accountinfo)) {

            intent = new Intent(getApplicationContext(), ActivityMyAccountInfo.class);
            startActivity(intent);
            finish();
        }
    }

    //region countrycode
    public void DialogCountryCode() {
        final Dialog dialog = new Dialog(ActivityForgotPwd.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogcountrycode);

        lvcountrycode = (ListView) dialog.findViewById(R.id.lvcountrycode);
        LoadCountryCode();
        lvcountrycode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CountryCode code = lst_countrycode.get(position);
                btn_dropdowncode.setText(code.getISOCode() + "+" + code.getCountryCode());
                //btn_countrycode.setText("+" + code.getCountryCode());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void callvolleyCountryCode() {
        if (Utils.hasInternetConnection(mContext)) {
            VolleyCountryCode v_countryCode = new VolleyCountryCode(mContext);
            v_countryCode.GetCountryCodeJsonData(new VolleyCountryCode.VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<CountryCode> success) {
                    lst_countrycode = success;
                    if (lst_countrycode.size() > 0) {
                        for (int i = 0; i < lst_countrycode.size(); i++) {
                            if (lst_countrycode.get(i).getISOCode().equals(getResources().getString(R.string.default_countryisocode))) {
                                btn_dropdowncode.setText(lst_countrycode.get(i).getISOCode() + "+" + lst_countrycode.get(i).getCountryCode());
                                //btn_countrycode.setText("+" + lst_countrycode.get(i).getCountryCode());
                                break;
                            }
                        }
                    }
                }

                @Override
                public void onFail(String errorcode, String errormessage) {
                    btn_dropdowncode.setText(getResources().getString(R.string.default_countryisocode) + "+" + getResources().getString(R.string.default_countrycode));
                    //btn_countrycode.setText("+" + getResources().getString(R.string.default_countrycode));
                }
            });
        } else {
            btn_dropdowncode.setText(getResources().getString(R.string.default_countryisocode) + "+" + getResources().getString(R.string.default_countrycode));
            //btn_countrycode.setText("+" + getResources().getString(R.string.default_countrycode));
        }
    }

    public void LoadCountryCode() {
        adpt_countrycode = new CountryCodeAdapter(this, lst_countrycode);
        adpt_countrycode.notifyDataSetChanged();
        lvcountrycode.setAdapter(adpt_countrycode);
    }
    //endregion

    // region Exit from App()
  /*  @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_forgotpwd, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
         switch (id) {
            case android.R.id.home:
                    returnback();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }
    //endregion


}
