package sg.com.ehealthassist.ehealthassist.Events;

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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestEventView;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyEventView;
import sg.com.ehealthassist.ehealthassist.ActivityHome;
import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.Models.Event.EventsView;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityEvents extends AppCompatActivity {

    ListView lveventslist;
    TextView txt_msg_event, main_toolbar_title;
    ImageButton imgbtn_refresh, toolbar_imgbutton_back;

    EventViewAdapter event_adapter;
    List<EventsView> lstevent;
    SharedPreferences preferences = null;
    static String usertoken,token_type;boolean account_verified;
    Context _context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_events);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_activity_events));
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        _context = this;
        usertoken  = preferences.getString(getString(R.string.pref_login_Access_token),"");

        account_verified = preferences.getBoolean(getString(R.string.pref_is_account_verified), false);

        findViewbyid();
        setEvent();
        callvolleyeventapi();
    }
    //region findviewbyid()
    public void findViewbyid() {
        lveventslist = (ListView) findViewById(R.id.lvevents);
        txt_msg_event = (TextView) findViewById(R.id.txt_msgevent);
        imgbtn_refresh = (ImageButton) findViewById(R.id.imgbtn_refresh);
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);


    }

    //endregion
    //region setEvent()
    public void setEvent() {
        lveventslist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EventsView event_obj = lstevent.get(position);
                Intent intent_event = new Intent(ActivityEvents.this, ActivityEventDetails.class);
                intent_event.putExtra("event", event_obj);
                startActivity(intent_event);
                finish();
            }
        });
        imgbtn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callvolleyeventapi();
            }
        });
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });

    }

    //endregion
    //region Get Event List via VolleyEvent api
    public void callvolleyeventapi() {
        if (Utils.hasInternetConnection(this)) {
            Long memberid = 0L;

            if(!usertoken.equals("") && account_verified){
                memberid =Long.parseLong( preferences.getString(_context.getString(R.string.pref_login_memberId), "0"));
            }

            RequestEventView request = new RequestEventView(memberid);
            JSONObject param = request.StringtoJsonObject(request.objecttoJson());
            VolleyEventView v_events = new VolleyEventView(this);
            v_events.GetEvenJsonData(param,new VolleyEventView.VolleyCallback() {
                @Override
                public void onSuccess(List<EventsView> eventslist) {
                    if (eventslist.size() > 0) {
                        lstevent = new ArrayList<EventsView>();
                        lstevent = eventslist;
                        LoadData();
                        txt_msg_event.setVisibility(View.INVISIBLE);
                    } else {
                        txt_msg_event.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onFail(String errorcode, String errormessage) {
                }
            });
        } else {
            Utils.showInternetRequiredDialog(this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
    }

    public void LoadData() {
        event_adapter = new EventViewAdapter(this, lstevent);
        lveventslist.setAdapter(event_adapter);
    }
    //endregion

    // region Exit from App()
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_queue__appoint, menu);
        return true;
    }
*/
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
    //endregion

}