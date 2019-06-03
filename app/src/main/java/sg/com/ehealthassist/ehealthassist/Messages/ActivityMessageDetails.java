package sg.com.ehealthassist.ehealthassist.Messages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestUpdateMessage;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyUpdateMessage;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityMessageDetails extends AppCompatActivity {

    TextView main_toolbar_title;
    TextView txtclinicname, textViewClinicDetailsBlockNo, textViewClinicDetailsStreet,
            textViewClinicDetailsUnitNo, textViewClinicDetailsBuildingName,
            textViewClinicDetailsPostalCode, txtpatientname, txtdate, txtclinicmessage;
    EditText editreplymessage;
    Button btn_messagereply;
    ImageButton toolbar_imgbutton_back;
    Messages parmMessage = new Messages();
    DBClinicInfo dbinfo;
    Context _mcontext;
    SharedPreferences preferences = null;
    static String usertoken, token_type;
    boolean account_verified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_message_details);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText("Message Details");
        _mcontext = this;
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        usertoken = preferences.getString(getString(R.string.pref_login_Access_token), "");
        account_verified = preferences.getBoolean(getString(R.string.pref_is_account_verified), false);
        dbinfo = new DBClinicInfo(_mcontext);
        Intent get = getIntent();
        parmMessage = get.getParcelableExtra("message");
        findViewbyid();
        loadData();

    }

    public void findViewbyid() {
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        txtclinicname = (TextView) findViewById(R.id.txtclinicname);
        textViewClinicDetailsBlockNo = (TextView) findViewById(R.id.textViewClinicDetailsBlockNo);
        textViewClinicDetailsStreet = (TextView) findViewById(R.id.textViewClinicDetailsStreet);
        textViewClinicDetailsUnitNo = (TextView) findViewById(R.id.textViewClinicDetailsUnitNo);
        textViewClinicDetailsBuildingName = (TextView) findViewById(R.id.textViewClinicDetailsBuildingName);
        textViewClinicDetailsPostalCode = (TextView) findViewById(R.id.textViewClinicDetailsPostalCode);
        txtpatientname = (TextView) findViewById(R.id.txtpatientname);
        txtdate = (TextView) findViewById(R.id.txtdate);
        txtclinicmessage = (TextView) findViewById(R.id.txtclinicmessage);
        editreplymessage = (EditText) findViewById(R.id.editreplymessage);
        btn_messagereply = (Button) findViewById(R.id.btn_messagereply);

    }

    public void loadData() {
        int clinicid = Integer.parseInt(parmMessage.getClinicid() + "");
        ClinicInfo info = dbinfo.getClinicInfo(clinicid);
        txtclinicname.setText(info.get_name());
        textViewClinicDetailsBlockNo.setText(info.getClinic_address().getBlockno());
        textViewClinicDetailsBuildingName.setText(info.getClinic_address().getBuilding());
        textViewClinicDetailsStreet.setText(info.getClinic_address().getStreet());
        textViewClinicDetailsPostalCode.setText(info.getClinic_address().getPostal());
        textViewClinicDetailsUnitNo.setText(info.getClinic_address().getUnitno());
        txtpatientname.setText(parmMessage.getPatientname());
        txtclinicmessage.setText(parmMessage.getMessage());
        editreplymessage.setText(parmMessage.getMessageReply());
        String createDate = "";
        if (!parmMessage.getCreatedate().equals("")) {
            createDate = Utils.BookDateFormat(parmMessage.getCreatedate(), "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd hh:mm a");
        }
        txtdate.setText(createDate);
        if (parmMessage.getStatus() == 1 || parmMessage.getStatus() == 2) {
            btn_messagereply.setVisibility(View.VISIBLE);
            editreplymessage.setEnabled(true);
            if(parmMessage.getStatus()==1){
                callupdateMessageapi(2);
            }
        } else {
            btn_messagereply.setVisibility(View.GONE);
            editreplymessage.setEnabled(false);
        }
        setEvent();
    }

    public void setEvent() {
        btn_messagereply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callupdateMessageapi(3);
            }
        });

        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnback();
            }
        });
    }

    public void callupdateMessageapi(int status) {
        if (Utils.hasInternetConnection(this)) {
            if (!usertoken.equals("") && account_verified) {
                String messagereply = editreplymessage.getText().toString();
                parmMessage.setStatus(status);
                parmMessage.setDownloaded("N");
                if(status==3){
                    if (!messagereply.equals("")) {
                        RequestUpdateMessage reqmes = new RequestUpdateMessage(parmMessage.getId(), parmMessage.getClinicid(),
                                parmMessage.getMemberid(), parmMessage.getNric(), parmMessage.getNrictype(), parmMessage.getDob()
                                , parmMessage.getMessage(), parmMessage.createdate, parmMessage.getStatus(), messagereply,
                                parmMessage.getDownloaded(),parmMessage.mobileno);

                        String strobj = reqmes.ObjecttoJson();
                        JSONObject paramobj = reqmes.StringtoJsonObject(reqmes.ObjecttoJson());
                        Log.e("parma", strobj);
                        callMessageAPI(paramobj,3);

                    } else {
                        Utils.showAlertDialog(_mcontext, "Alert", "Reply Message should not be empty");
                    }
                }else{
                    parmMessage.setStatus(status);
                    RequestUpdateMessage reqmes = new RequestUpdateMessage(parmMessage.getId(), parmMessage.getClinicid(),
                            parmMessage.getMemberid(), parmMessage.getNric(), parmMessage.getNrictype(), parmMessage.getDob()
                            , parmMessage.getMessage(), parmMessage.createdate, parmMessage.getStatus(), messagereply,
                            parmMessage.getDownloaded(),parmMessage.mobileno);

                    String strobj = reqmes.ObjecttoJson();
                    JSONObject paramobj = reqmes.StringtoJsonObject(reqmes.ObjecttoJson());
                    Log.e("parma", strobj);
                    callMessageAPI(paramobj,2);
                }
            }
        } else {
            Utils.showInternetRequiredDialog(this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
    }

    public void callMessageAPI(JSONObject updateobj, final int status){
        VolleyUpdateMessage _vupdate = new VolleyUpdateMessage(_mcontext);
        _vupdate.UpdateMessageRequest(updateobj, usertoken, new VolleyUpdateMessage.VolleyCallback() {
            @Override
            public void onSuccess(String messag) {
                if(status == 3){
                    Utils.showAlertDialog(_mcontext, "Alert", messag);
                    btn_messagereply.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFail(String errorcode, String errormessage) {
                Utils.showAlertDialog(_mcontext, errorcode, errormessage);
                btn_messagereply.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnback();
    }

    public void returnback() {
        Intent intent = new Intent(getApplicationContext(), ActivityMessageList.class);
        intent.putExtra("from", "ActivityMessageDetails");
        startActivity(intent);
        finish();
    }
}
