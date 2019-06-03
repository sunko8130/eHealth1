package sg.com.ehealthassist.ehealthassist.Events;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONObject;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestEventRegistration;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyEventRegistration;
import sg.com.ehealthassist.ehealthassist.Account.ActivityLogIn;
import sg.com.ehealthassist.ehealthassist.Models.Event.EventsView;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityEventDetails extends AppCompatActivity {
    TextView txt_event_name, txt_event_date, txt_event_time, txt_description, txt_venue, txt_contactno,
            txt_email,main_toolbar_title,txt_maxpeoplereg,txt_registerenddate,lbl_maxpeople,lblregisterenddate,
            txt_lblemail;
    View maxdateview,enddateview;
    static EventsView event_obj;
    String blank = "-----";
    static  Button btneventregister;
    ImageButton toolbar_imgbutton_back;
    Context _mcontext;
    SharedPreferences preferences = null;
    static String usertoken,token_type;boolean account_verified;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_activity_event_details);
        main_toolbar_title = (TextView)findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_event_detail));
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        _mcontext = this;
        Intent in_get = getIntent();
        event_obj = in_get.getParcelableExtra("event");
        usertoken  = preferences.getString(getString(R.string.pref_login_Access_token),"");

        account_verified = preferences.getBoolean(getString(R.string.pref_is_account_verified), false);
        findviewbyid();
        LoadData();
        setEvent();
    }

    public void findviewbyid() {
        txt_event_name = (TextView) findViewById(R.id.txt_event_name);
        txt_event_date = (TextView) findViewById(R.id.txt_event_date);
        txt_event_time = (TextView) findViewById(R.id.txt_event_time);
        txt_description = (TextView) findViewById(R.id.txt_description);
        txt_venue = (TextView) findViewById(R.id.txt_venue);
        txt_contactno = (TextView) findViewById(R.id.txt_contactno);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txt_maxpeoplereg = (TextView) findViewById(R.id.txt_maxpeoplereg);
        txt_registerenddate = (TextView) findViewById(R.id.txt_registerenddate);
        lbl_maxpeople = (TextView)findViewById(R.id.lbl_maxpeople);
        lblregisterenddate=(TextView)findViewById(R.id.lblregisterenddate);
        txt_lblemail=(TextView)findViewById(R.id.txt_lblemail);
        maxdateview = (View)findViewById(R.id.maxdateview);
        enddateview=(View)findViewById(R.id.enddateview);
        toolbar_imgbutton_back = (ImageButton)findViewById(R.id.toolbar_imgbackbutton);
        btneventregister =(Button)  findViewById(R.id.btneventregister);

    }
    public void setEvent(){
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });
        btneventregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!usertoken.equals("") && account_verified){
                    showEventRegistrationDialog(_mcontext,"Event Registration","Are you sure you want to register this event?");
                }else{
                    Intent intent_login = new Intent(_mcontext, ActivityLogIn.class);
                    intent_login.putExtra("from","event");
                    intent_login.putExtra("CID",0);
                    startActivity(intent_login);
                }

            }
        });
    }
    public void LoadData() {
        txt_event_name.setText(event_obj.getEventName());
        String event_fromdate, event_Todate, event_date = "";
       /* if (Utils.eventcomparedate(event_obj.getFromDate(), event_obj.getToDate()) == 1) {
            event_fromdate = Utils.BookDateFormat(event_obj.getFromDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
            event_date = event_fromdate;
        } else {
            event_fromdate = Utils.BookDateFormat(event_obj.getFromDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
            event_Todate = Utils.BookDateFormat(event_obj.getToDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
            event_date = event_fromdate + " to " + event_Todate;
        }*/
        event_fromdate = Utils.BookDateFormat(event_obj.getFromDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
        event_Todate = Utils.BookDateFormat(event_obj.getToDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");

        event_date = event_fromdate + " to " + event_Todate;
        txt_event_date.setText(event_date);

        String event_formtime =  Utils.BookDateFormat(event_obj.getFromTime(),"HH:mm","hh:mm a")  ;

        String event_Totime = Utils.BookDateFormat( event_obj.getToTime(),"HH:mm","hh:mm a");

        txt_event_time.setText(event_formtime + " to " + event_Totime);
        if (!event_obj.getEventDesc().equals("")) {
            txt_description.setText(event_obj.getEventDesc());
        } else {
            txt_description.setText(blank);
        }
        if (!event_obj.getVenue().equals("")) {
            txt_venue.setText(event_obj.getVenue());
        } else {
            txt_venue.setText(blank);
        }
        if (!event_obj.getContactNo().equals("")) {
            txt_contactno.setText(event_obj.getContactNo());
        } else {
            txt_contactno.setText(blank);
        }
        if (!event_obj.getEmail().equals("")) {
            txt_email.setText(event_obj.getEmail());
           } else {
            txt_email.setText(blank);
           }
        if(event_obj.getRegisterEndDate().equals("")){
            txt_registerenddate.setText("-");
            txt_registerenddate.setVisibility(View.GONE);
            lblregisterenddate.setVisibility(View.GONE);
            enddateview.setVisibility(View.GONE);

        }else{
            String regendDate = Utils.BookDateFormat(event_obj.getRegisterEndDate().toString(),"yyyy-MM-dd'T'HH:mm:ss","dd-MMM-yyyy");
            txt_registerenddate.setText(regendDate);
            txt_registerenddate.setVisibility(View.VISIBLE);
            lblregisterenddate.setVisibility(View.VISIBLE);
            enddateview.setVisibility(View.VISIBLE);
        }

        if(event_obj.getMaxpeoplereg()>0){
            txt_maxpeoplereg.setText(event_obj.getMaxpeoplereg()+"");
            if(event_obj.getIsRegister().equals("Y")){
                txt_maxpeoplereg.setVisibility(View.VISIBLE);
                lbl_maxpeople.setVisibility(View.VISIBLE);
                maxdateview.setVisibility(View.VISIBLE);
            }else{
                txt_maxpeoplereg.setVisibility(View.GONE);
                lbl_maxpeople.setVisibility(View.GONE);
                maxdateview.setVisibility(View.GONE);
            }

        }else{
            txt_maxpeoplereg.setText("No limit");
            if(event_obj.getIsRegister().equals("Y")){
                txt_maxpeoplereg.setVisibility(View.VISIBLE);
                lbl_maxpeople.setVisibility(View.VISIBLE);
                maxdateview.setVisibility(View.VISIBLE);
            }else{
                txt_maxpeoplereg.setVisibility(View.GONE);
                lbl_maxpeople.setVisibility(View.GONE);
                maxdateview.setVisibility(View.GONE);
            }

        }

       // if(!usertoken.equals("") && account_verified){
            switch (event_obj.getStatus()){
                case "R":
                    btneventregister.setVisibility(View.VISIBLE);
                    btneventregister.setText("Registered");
                    btneventregister.setEnabled(false);
                    break;
                case "O":
                    btneventregister.setVisibility(View.VISIBLE);
                    btneventregister.setText("Register");
                    btneventregister.setEnabled(true);
                    break;
                case "C":
                    btneventregister.setVisibility(View.VISIBLE);
                    btneventregister.setText("Closed");
                    btneventregister.setEnabled(false);
                    break;
                case "N":
                    btneventregister.setVisibility(View.GONE);
                    btneventregister.setText("None");
                    btneventregister.setEnabled(false);
                default:
                    btneventregister.setVisibility(View.GONE);
                    break;
            }
       // }
       // else{
          //  btneventregister.setVisibility(View.GONE);
       // }
    }
    // region Exit from App()
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void returnback(){
        Intent intent = new Intent(getApplicationContext(), ActivityEvents.class);
        startActivity(intent);
        finish();
    }
    public static void showEventRegistrationDialog(final Context context, final String title,final String message) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setMessage(message)
                .setTitle(title)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        return;
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            if(Utils.hasInternetConnection(context)){
                                RequestEventRegistration request = new RequestEventRegistration(event_obj.getEventId());
                                JSONObject param = request.StringtoJsonObject(request.objecttoJson());
                                VolleyEventRegistration v_eventreg = new VolleyEventRegistration(context);
                                v_eventreg.GetEventRegistraionRequest(param, usertoken, new VolleyEventRegistration.VolleyCallback() {
                                    @Override
                                    public void onSuccess(String message) {
                                        Utils.showAlertDialog(context,"Alert",message);
                                        btneventregister.setVisibility(View.VISIBLE);
                                        btneventregister.setText("Sent");
                                        btneventregister.setEnabled(false);
                                    }

                                    @Override
                                    public void onFail(String errorcode, String errormessage) {
                                        Utils.showAlertDialog(context,"Alert","Registration Unsuccessfully");
                                    }
                                });
                            }
                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
        alertBuilder.create().show();
    }
    //endregion


}
