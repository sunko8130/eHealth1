package sg.com.ehealthassist.ehealthassist.Messages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyGetMessages;
import sg.com.ehealthassist.ehealthassist.ActivityHome;
import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityMessageList extends AppCompatActivity {

    ListView lvmessagelist;
    List<Messages> lstmessage;
    SharedPreferences preferences = null;
    static String usertoken,token_type;boolean account_verified;
    TextView  main_toolbar_title,txt_msgevent;
    ImageButton  imgmessagebtn_refresh,toolbar_imgbutton_back;
    MessageAdapter mesaAdpt;
    Context _context;
    String from=""; String mesid="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_message_list);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText("Message");
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        _context = this;
        usertoken  = preferences.getString(getString(R.string.pref_login_Access_token),"");
        account_verified = preferences.getBoolean(getString(R.string.pref_is_account_verified), false);
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        if(from.equals("GCMIntentService")){
            mesid = intent.getStringExtra("MessageID");
        }
        findViewById();
        setEvent();
        callvolleyMessageapi();

    }

    public void findViewById(){
        lvmessagelist = (ListView) findViewById(R.id.lvmessagelist);
        imgmessagebtn_refresh = (ImageButton) findViewById(R.id.imgmessagebtn_refresh);
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        txt_msgevent=(TextView)findViewById(R.id.txt_msgevent);
    }

    public  void setEvent(){
        lvmessagelist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Messages message_obj = lstmessage.get(i);
                Intent intent_message = new Intent(ActivityMessageList.this, ActivityMessageDetails.class);
                intent_message.putExtra("message", message_obj);
                startActivity(intent_message);
                finish();
            }
        });
        imgmessagebtn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callvolleyMessageapi();
            }
        });
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });
    }
    public void loadAdapter(){
        mesaAdpt = new MessageAdapter(_context,lstmessage);
        lvmessagelist.setAdapter(mesaAdpt);
        mesaAdpt.notifyDataSetChanged();
    }

    public void callvolleyMessageapi(){
        if (Utils.hasInternetConnection(this)) {
            Long memberid = 0L;

            if (!usertoken.equals("") && account_verified) {
                txt_msgevent.setVisibility(View.GONE);
                memberid = Long.parseLong(preferences.getString(_context.getString(R.string.pref_login_memberId), "0"));
                VolleyGetMessages _vmessage = new VolleyGetMessages(_context);
                _vmessage.GetMessageRequest(usertoken, new VolleyGetMessages.VolleyCallback() {
                    @Override
                    public void onSuccess(String message, ArrayList<Messages> mlist) {
                        lstmessage = mlist;
                        loadAdapter();
                        int id = Integer.parseInt(mesid);
                        if(id>0){
                            for(Messages obj : lstmessage){
                                if(obj.getId()== id){
                                    Messages message_obj = obj;
                                    Intent intent_message = new Intent(ActivityMessageList.this, ActivityMessageDetails.class);
                                    intent_message.putExtra("message", message_obj);
                                    startActivity(intent_message);
                                    finish();
                                }
                            }

                        }
                    }

                    @Override
                    public void onFail(String errorcode, String errormessage) {
                        Utils.showAlertDialog(_context,errorcode,errormessage);
                    }
                });
            }
           else{
                txt_msgevent.setVisibility(View.VISIBLE);
            }
        }else{
            Utils.showInternetRequiredDialog(this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnback();
    }

    public void returnback() {
        Intent intent = new Intent(getApplicationContext(), ActivityHome.class);
        startActivity(intent);
        finish();
    }
}
