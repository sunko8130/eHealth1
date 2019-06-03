package sg.com.ehealthassist.ehealthassist.Profiles;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.CustomUI.CustomListView;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Models.Profile.ResponseDownloadStatus;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Queue.ActivityRegisterQueue;
import sg.com.ehealthassist.ehealthassist.R;

public class Activitydownloadprofilestatus extends AppCompatActivity {
    ImageButton toolbar_imgbutton_back;
    TextView main_toolbar_title;
    DBMedProfile dbmedprofile;
    Context context;
    CustomListView lv_profiledownload_status;
    Button btn_create_profile;
    ArrayList<ResponseDownloadStatus> lst_downlaod=new ArrayList<ResponseDownloadStatus>();
    ArrayList<MedicalProfile> lst_medprofile = new ArrayList<MedicalProfile>();
    String login_memberid = "", hecode = "",clinic_name =""; int clinicid= 0;
    DownloadProfileStatusAdapter downprofileadapter ;
    ArrayList<ResponseDownloadStatus> new_list_status;
    MedicalProfile medicalobj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_activitydownloadprofilestatus);
        context = this;
        dbmedprofile = new DBMedProfile(this);
        toolbar();
        findViewbyId();
        Intent get_intent = getIntent();
        lst_downlaod = get_intent.getParcelableArrayListExtra("profile_status");
        login_memberid = get_intent.getStringExtra("memberid");
        hecode = get_intent.getStringExtra("hecode");
        clinicid = get_intent.getIntExtra("clinicid",0);
        clinic_name = get_intent.getStringExtra("clinicname");
        LoadData();
        setEvent();
    }
    public void toolbar(){
        main_toolbar_title = (TextView)findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_downloadprofile_status));
        toolbar_imgbutton_back = (ImageButton)findViewById(R.id.toolbar_imgbackbutton);
    }

    public void findViewbyId(){
        btn_create_profile = (Button)findViewById(R.id.btn_create_profile);
        lv_profiledownload_status =(CustomListView) findViewById(R.id.lv_profiledownload_status);
    }
    public void LoadData(){
        new_list_status = new ArrayList<ResponseDownloadStatus>();
        lst_medprofile = dbmedprofile.getMedProfilebyMemberid(login_memberid);
        if(lst_medprofile.size()>0){
            for(int i=0;i<lst_medprofile.size();i++){
                int count = 0;
                for(int y =0;y <lst_downlaod.size();y++){
                    if(lst_medprofile.get(i).getNric().equals(lst_downlaod.get(y).getNric())
                            && lst_medprofile.get(i).getNric_type() == Integer.parseInt(lst_downlaod.get(y).getNrictype())){
                        lst_downlaod.get(y).setNricname(lst_medprofile.get(i).getNric_name());
                        lst_downlaod.get(y).setGender(lst_medprofile.get(i).getGender());
                        new_list_status.add(lst_downlaod.get(y));
                        count = 1;
                        break;
                    }
                    else{
                        count = 0;
                    }
                }
                if(count==0){
                    ResponseDownloadStatus unknown_object = new ResponseDownloadStatus();
                    unknown_object.setClinic(lst_medprofile.get(i).getClinic_id());
                    unknown_object.setDownloaded("U");
                    unknown_object.setNricname(lst_medprofile.get(i).getNric_name());
                    unknown_object.setNric(lst_medprofile.get(i).getNric());
                    unknown_object.setNrictype(lst_medprofile.get(i).getNric_type()+"");
                    unknown_object.setMemberid(lst_medprofile.get(i).getMemberid());
                    unknown_object.setDob(lst_medprofile.get(i).getDob());
                    unknown_object.setGender(lst_medprofile.get(i).getGender());
                    unknown_object.setHecode(hecode);
                    new_list_status.add(unknown_object);
                }
            }
            LoadDataToAdapter(new_list_status);
        }
    }

    public void LoadDataToAdapter(ArrayList<ResponseDownloadStatus>  lstdownstatus){
        downprofileadapter = new DownloadProfileStatusAdapter(this,lstdownstatus);
        downprofileadapter.notifyDataSetChanged();
        lv_profiledownload_status.setAdapter(downprofileadapter);
        lv_profiledownload_status.setExpanded(true);
    }
    public void setEvent(){
        lv_profiledownload_status.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ResponseDownloadStatus parcel_object = new_list_status.get(position);
                int nrictype = Integer.parseInt(new_list_status.get(position).getNrictype());
                medicalobj = dbmedprofile.getMedProfilebyNRIC_memberid(nrictype,
                        new_list_status.get(position).getNric(), login_memberid);

                if(checkMedicalObject(medicalobj)){
                    Utils.showAlertDialog(context,"Alert",getString(R.string.profile_invalid));
                }else{
                    Intent back_intent = new Intent(context,ActivityRegisterQueue.class);
                    back_intent.putExtra("clinicid",clinicid);
                    back_intent.putExtra("hecode", hecode);
                    back_intent.putExtra("from_activity","downloadprofilestatus");
                    back_intent.putExtra("download_status",parcel_object);
                    back_intent.putExtra("clinicname",clinic_name);
                    startActivity(back_intent);
                    finish();
                }
            }
        });
        btn_create_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lst_medprofile.size() < 10) {
                    Log.e("call create profile","arrived profiel create");
                    Intent intent = new Intent(context, ActivityMedicalProfile.class);
                    intent.putExtra("from", "downloadprofilestatus");
                    intent.putExtra("clinic_id", clinicid);
                    intent.putExtra("member",Integer.parseInt(login_memberid));
                    intent.putExtra("id",0);
                    intent.putExtra("hecode", hecode);
                    intent.putExtra("clinicname",clinic_name);
                    intent.putParcelableArrayListExtra("profile_status", lst_downlaod);
                    startActivity(intent);
                    finish();
                } else {
                    Utils.showAlertDialog(context, "Medical Profile", getString(R.string.response_10patients));
                }
            }
        });
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });
    }

    public Boolean checkMedicalObject(MedicalProfile obj){
        boolean hasError = false;
        String telph = obj.getTel1();
        if(telph.length()<8){
            hasError = true;
        }
        return hasError;
    }
    public void returnback(){
        Intent back_intent = new Intent(context,ActivityRegisterQueue.class);
        back_intent.putExtra("clinicid",clinicid);
        back_intent.putExtra("hecode", hecode);
        back_intent.putExtra("from_activity","activitysearchclinicdetail");
        back_intent.putExtra("clinicname",clinic_name);
        startActivity(back_intent);
        finish();
    }
}

