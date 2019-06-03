package sg.com.ehealthassist.ehealthassist.Queue;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestQueueCancel;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyQueueCancel;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.Address;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Queue.QueuelistView;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Queue_Appt.ActivityQueue_Appoint;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityQueueDetailDialogs extends AppCompatActivity {

    ImageButton queue_direction,toolbar_imgbutton_back;
    SharedPreferences pref = null;
    Context _mcont;
    TextView queue_dtl_clinic,queue_clinic_address,queue_patient,
            queue_dob,queue_request_date,queue_current_no,queue_get_no,
            queue_qmessage,txt_queue_reson,txt_qstatus;
    QueuelistView queue_object = new QueuelistView();
    int position = -1;
    ClinicInfo info = new ClinicInfo();
    DBClinicInfo dbclinic = null;
    String clinic_address,clinic_name;
    RelativeLayout rlrejectreason,rlqmessage,rlqueue_no,rlcurrent_no,rlclinic_address;
    TextView main_toolbar_title;
    Button btnqueue_cancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_queue_detail_dialogs);
        main_toolbar_title = (TextView)findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_activity_queue_detail_dialog));
        init();
        findviewbyid();
        Intent inbook = getIntent();
        queue_object = inbook.getParcelableExtra("queue_object");
        position = inbook.getIntExtra("list_position",-1);

        LoadData();
        setEvent();
    }
    public void init(){
        pref = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        _mcont = this;
        this.dbclinic = new DBClinicInfo(_mcont);
    }
    public void findviewbyid(){
        toolbar_imgbutton_back = (ImageButton)findViewById(R.id.toolbar_imgbackbutton);
        queue_direction = (ImageButton)findViewById(R.id.queue_direction);
        queue_dtl_clinic =(TextView)findViewById(R.id.queue_dtl_clinic);
        queue_clinic_address =(TextView)findViewById(R.id.queue_clinic_address);
        queue_patient =(TextView)findViewById(R.id.queue_patient);
        queue_dob =(TextView)findViewById(R.id.queue_dob);
        queue_request_date =(TextView)findViewById(R.id.queue_request_date);
        queue_current_no =(TextView)findViewById(R.id.queue_current_no);
        queue_get_no =(TextView)findViewById(R.id.queue_get_no);
        queue_qmessage =(TextView)findViewById(R.id.queue_qmessage);
        txt_queue_reson =(TextView)findViewById(R.id.txt_queue_rejectreason);
        txt_qstatus=(TextView)findViewById(R.id.txt_qstatus);
        rlrejectreason=(RelativeLayout)findViewById(R.id.rlrejectreason);
        rlqmessage=(RelativeLayout)findViewById(R.id.rlqmessage);
        rlqueue_no=(RelativeLayout)findViewById(R.id.rlqueue_no);
        rlcurrent_no=(RelativeLayout)findViewById(R.id.rlcurrent_no);
        rlclinic_address = (RelativeLayout)findViewById(R.id.rlclinic_address);
        btnqueue_cancel = (Button)findViewById(R.id.btnqueue_cancel);
    }
    public void LoadData(){
        info = dbclinic.getClinicInfo(queue_object.getClinicid());

        clinic_address = getAddress();
        if(info.get_name().equals("")){
            clinic_name = queue_object.getClinicname();
        }else{
            clinic_name = info.get_name();
        }
        queue_dtl_clinic.setText(clinic_name);
        queue_clinic_address.setText(clinic_address);
        queue_patient.setText(queue_object.getPatientname()+" ( "+queue_object.getPatientnric() + " )");
        queue_dob.setText(queue_object.getPatientdob());
        queue_request_date.setText(queue_object.getRequestdatetime());
        queue_current_no.setText(queue_object.getCurrentqueue()+"");
        queue_get_no.setText(queue_object.getQueueno());
        queue_qmessage.setText(queue_object.getQmessage());
        txt_queue_reson.setText(queue_object.getRejectreason());
        txt_qstatus.setText(queue_object.getQstatus());

        if (Double.parseDouble(queue_object.getQueueno()) < 0) {
            txt_qstatus.setTextColor(_mcont.getResources().getColor(R.color.colorred));
            rlqmessage.setVisibility(View.GONE);
            rlrejectreason.setVisibility(View.VISIBLE);
            btnqueue_cancel.setVisibility(View.GONE);
            queue_get_no.setText("---");
            //  rlcurrent_no.setVisibility(View.GONE);

        } else if (Double.parseDouble(queue_object.getQueueno()) == 0) {
            txt_qstatus.setTextColor(_mcont.getResources().getColor(R.color.cas_color_tab_yellow));
            rlqmessage.setVisibility(View.VISIBLE);
            rlrejectreason.setVisibility(View.GONE);
            btnqueue_cancel.setVisibility(View.VISIBLE);
            queue_get_no.setText("---");
            //  rlcurrent_no.setVisibility(View.GONE);
        } else {
            txt_qstatus.setTextColor(_mcont.getResources().getColor(R.color.cas_success_green));
            rlqmessage.setVisibility(View.VISIBLE);
            rlrejectreason.setVisibility(View.GONE);
            btnqueue_cancel.setVisibility(View.GONE);
            // rlcurrent_no.setVisibility(View.VISIBLE);
        }

    }
    public void setEvent(){
        queue_direction.setOnClickListener(btnLocateUsOnClickListener);
        rlclinic_address.setOnClickListener(btnLocateUsOnClickListener);
        toolbar_imgbutton_back.setOnClickListener(imgtoolbarbackOnClickListener);
        btnqueue_cancel.setOnClickListener(btnCancleQueue);
    }
    public void relativechangeLayout(){
        RelativeLayout.LayoutParams layoutParams =
                (RelativeLayout.LayoutParams)rlrejectreason.getLayoutParams();
        layoutParams.addRule(RelativeLayout.BELOW, R.id.rlqueue_no);
        rlrejectreason.setLayoutParams(layoutParams);
    }
    public String getAddress() { // load address
        Address address = info.getClinic_address();
        String str_address = address.getBlockno() + " " +
                address.getUnitno() + " " +
                address.getStreet() + " " +
                "S(" + address.getPostal() + ")";
        return str_address;
    }

    //region Cancel Queue
    View.OnClickListener btnCancleQueue = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_mcont);
            alertDialogBuilder.setMessage(_mcont.getString(R.string.error_response_queue_delete));
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    String usertoken = pref.getString(_mcont.getString(R.string.pref_login_Access_token),"");
                    if (!usertoken.equals("")) {
                        if (Utils.hasInternetConnection(_mcont)) {
                            if (pref.getBoolean(_mcont.getString(R.string.pref_is_account_verified), false)) {
                                RequestQueueCancel data_qcancel = new RequestQueueCancel(queue_object.getClinicid(),queue_object.getRequestid(),queue_object.getQueueno());
                                JSONObject json_qcancel = data_qcancel.StringtoJsonObject(data_qcancel.ObjecttoJson());
                                VolleyQueueCancel v_qcancel = new VolleyQueueCancel(_mcont);
                                v_qcancel.GetQueueCancelJson(usertoken,queue_object.getClinicid() ,json_qcancel, new VolleyQueueCancel.VolleyCallback() {
                                    @Override
                                    public void onSuccess(String message) {

                                        if(position > -1){
                                            FragmentQueue.lstqueueview.get(position).setQstatus("Cancelled");
                                            FragmentQueue.lstqueueview.get(position).setQueueno("-1");
                                            FragmentQueue.adapter.notifyDataSetChanged();
                                            txt_qstatus.setText("Cancelled");
                                            txt_qstatus.setTextColor(_mcont.getResources().getColor(R.color.colorred));
                                        }
                                        Utils.showAlertDialog(_mcont,"Alert",getResources().getString(R.string.response_queue_cancle));
                                    }
                                    @Override
                                    public void onFail(String errorcode, String errormessage) {
                                        Utils.showAlertDialog(_mcont,"Alert",errormessage);
                                    }
                                });

                            } else {
                                Utils.showAlertDialog(_mcont, _mcont.getString(R.string.error_response_invalidtoken_message), _mcont.getString(R.string.error_response_upload_profile_status_InvalidUserToken));
                                return;
                            }
                        } else {
                            Utils.showInternetRequiredDialog(_mcont, _mcont.getString(R.string.title_internet_require), _mcont.getString(R.string.msg_no_internet_connection_setup));
                            return;
                        }
                    }
                }
            });
            alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    };
    //endregion
    //region toolbar image back
    View.OnClickListener imgtoolbarbackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            returnback();
        }
    };
    //endregion
    //region btnLocateUsOnClickListener
    View.OnClickListener btnLocateUsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                ApplicationInfo mapinfo = getPackageManager().getApplicationInfo(getString(R.string.google_maps_package_name), 0);
                if (!mapinfo.enabled) {
                    getGoogleMapsDialog();
                    return;
                }
                String url;
                if (info.getClinic_location().get_lat().equals("") || info.getClinic_location().get_lng().equals(""))
                    url = "geo:0,0?q=SINGAPORE+" + info.getClinic_address().getPostal();
                else {
                    clinic_name = clinic_name.replace(" ", "+");
                    url = "geo:0,0?q=" + info.getClinic_location().get_lat() + "," + info.getClinic_location().get_lng() + "(" + clinic_name + ")";
                }
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                intent.setPackage(getString(R.string.google_maps_package_name));
                startActivity(intent);
            } catch (PackageManager.NameNotFoundException e) {
                getGoogleMapsDialog();
            }
        }
    };
    //endregion
    //region GoogleMapDialog()
    private void getGoogleMapsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityQueueDetailDialogs.this);
        builder.setMessage(R.string.msg_google_map_required);
        builder.setPositiveButton("Enable/Install", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_google_map_install)));
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //endregion

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnback();
    }
    public void returnback(){
        Intent intent = new Intent(getApplicationContext(), ActivityQueue_Appoint.class);
        intent.putExtra("from","ActivityQueueDetailDialogs");
        startActivity(intent);
        finish();
    }
    //endregion
}

