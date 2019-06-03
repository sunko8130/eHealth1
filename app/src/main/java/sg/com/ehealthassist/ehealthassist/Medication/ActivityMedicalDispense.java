package sg.com.ehealthassist.ehealthassist.Medication;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyMedicalDispenseList;
import sg.com.ehealthassist.ehealthassist.ActivityHome;
import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBMedDispense;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalDispense;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityMedicalDispense extends AppCompatActivity {
    TextView main_toolbar_title;
    Context _mcontext;
    SharedPreferences preferences = null;
    String usertoken = "", memberid = "";
    boolean accountverify= false;
    DBMedDispense dbmeddispense;
    DBClinicInfo dbclinicinfo;
    DBMedProfile dbmedprofile;
    ArrayList<MedicalDispense> lstmedidispenes;
    ArrayList<String> lstclinic;
    ImageButton toolbar_imgbutton_back;
    ListView lvmedication;
    MedicationclinicAdapter adapter;
    ClinicInfo clinicname;
    MedicalProfile profiename;
    ImageButton img_refresh;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_medical_dispense);
        _mcontext = this;
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        usertoken = preferences.getString(getString(R.string.pref_login_Access_token), "");
        memberid = preferences.getString(getString(R.string.pref_login_memberId), "");
        accountverify = preferences.getBoolean(getString(R.string.pref_is_account_verified),false);
      //  preferences.edit().putBoolean(getString(R.string.pref_is_account_verified), true)
        dbmeddispense = new DBMedDispense(_mcontext);
        dbclinicinfo = new DBClinicInfo(_mcontext);
        dbmedprofile = new DBMedProfile(_mcontext);
        pDialog = new ProgressDialog(_mcontext);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        Intent getintent = getIntent();
        String from_activity = getintent.getStringExtra("from_activity");

        if(from_activity.equals("ReminderService")){
            String visitno = getintent.getStringExtra("visitno");
            String ids = getintent.getStringExtra("ids");
            String messages = getintent.getStringExtra("messages");
            String uuid = getintent.getStringExtra("uuid");
            String freqcode = getintent.getStringExtra("freqcode");
            showReminderDialog(visitno,ids,messages,uuid,freqcode);
        }

        findviedByid();
        callvolley();
        setEvent();
    }
    public void findviedByid() {
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText("Medication");
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        lvmedication = (ListView) findViewById(R.id.lvdispense);
        img_refresh = (ImageButton) findViewById(R.id.imgbtn_medDipsenseRefresh);
    }

    public void setEvent() {
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });

        lvmedication.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MedicalDispense med = new MedicalDispense();
                med.setPatientName(lstmedidispenes.get(position).getPatientName());
                med.setClinicName(lstmedidispenes.get(position).getClinicName());
                med.setMemberid(lstmedidispenes.get(position).getMemberid());
                med.setNric(lstmedidispenes.get(position).getNric());
                med.setNrictype(lstmedidispenes.get(position).getNrictype());
                med.setDOB(lstmedidispenes.get(position).getDOB());
                med.setQueueID(lstmedidispenes.get(position).getQueueID());
                med.setVisitNo(lstmedidispenes.get(position).getVisitNo());
                med.setVisitDate(lstmedidispenes.get(position).getVisitDate());
                med.setClinicId(lstmedidispenes.get(position).getClinicId());
                Intent intent = new Intent(_mcontext, ActivityMedDispenseDetails.class);
                intent.putExtra("dispense", med);
                _mcontext.startActivity(intent);
                finish();
            }
        });
        img_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callvolley();
            }
        });
    }

    public void getData() {
        lstmedidispenes = new ArrayList<MedicalDispense>();
        lstclinic = dbmeddispense.getclinicidbyvisitdate();
        if (lstclinic.size() > 0) {

            for (int c = 0; c < lstclinic.size(); c++) {
                ArrayList<MedicalDispense> lst_med = dbmeddispense.getMedicationbyLVisitDate(lstclinic.get(c));
                for (MedicalDispense dispense : lst_med) {
                    Log.e("patient name",dispense.getPatientName());
                    clinicname = dbclinicinfo.getClinicInfo(Integer.parseInt(lstclinic.get(c)));
                    if(dispense.getPatientName().equals("")){
                        profiename = dbmedprofile.getMedProfilebyNRIC_memberid(Integer.parseInt(dispense.getNrictype()), dispense.getNric(), dispense.getMemberid());
                        dispense.setPatientName(profiename.getNric_name());
                    }
                    dispense.setClinicName(clinicname.get_name());
                    lstmedidispenes.add(dispense);
                }
            }
            LoadData();
        }

    }

    public void LoadData() {
        if(lstmedidispenes.size()>0){
            adapter = new MedicationclinicAdapter(_mcontext, lstmedidispenes);
            lvmedication.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }

    public void callvolley() {
        if (Utils.hasInternetConnection(_mcontext)) {
            if(accountverify){
                showpDialog();
                VolleyMedicalDispenseList _vmeddispenselists = new VolleyMedicalDispenseList(_mcontext, usertoken);
                _vmeddispenselists.GetMedDispenseLists(new VolleyMedicalDispenseList.VolleyCallback() {
                    @Override
                    public void onSuccess(String count) {
                        getData();
                        hidepDialog();
                    }

                    @Override
                    public void onFail(String errorcode, String errormessage) {
                        hidepDialog();
                        Utils.showAlertDialog(_mcontext, errorcode, errormessage);
                    }
                });
            }

        } else {
            Utils.showInternetRequiredDialog(_mcontext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
    }
    public void showReminderDialog(String visitno,String ids,String message,String uuid,String freqcode){
        final Dialog drive_dialog = new Dialog(_mcontext);
        drive_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        drive_dialog.setContentView(R.layout.activity_alarm_reminder);
        drive_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button btn_reminder_ok = (Button)drive_dialog.findViewById(R.id.btn_reminder_ok);
        TextView txtmedmessage =(TextView)drive_dialog.findViewById(R.id.txtmedmessage);
        txtmedmessage.setText(message);
        btn_reminder_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drive_dialog.dismiss();
            }
        });
        drive_dialog.show();
    }

    public void returnback() {
        Intent intent = new Intent(getApplicationContext(), ActivityHome.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnback();
    }
    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

