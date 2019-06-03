package sg.com.ehealthassist.ehealthassist.Queue;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.DB.DBQueueRequest;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Models.Queue.QueuelistView;
import sg.com.ehealthassist.ehealthassist.Models.Queue.RequestQueueLog;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class QueueAdapter extends BaseAdapter {

    private Context _mcont;
    private List<QueuelistView> lstqueuelist;
    SharedPreferences preferences = null;
    String usertoken ="";
    DBMedProfile dbmedical;
    MedicalProfile med = new MedicalProfile();

    public QueueAdapter(Context context, List<QueuelistView> queuelist) {
        this._mcont = context;
        this.lstqueuelist = queuelist;
        preferences = _mcont.getSharedPreferences(_mcont.getString(R.string.preference_name), _mcont.MODE_PRIVATE);
        usertoken = preferences.getString(_mcont.getString(R.string.pref_login_Access_token), "");
        dbmedical = new DBMedProfile(context);

    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return lstqueuelist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder v = null;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_queueregister, null);
            v.queuedate = (TextView) convertView.findViewById(R.id.txtqueue_date);
            v.queueclinic = (TextView) convertView.findViewById(R.id.txtqueue_clinic);
            v.request_nric = (TextView) convertView.findViewById(R.id.txtrequest_nric);
            v.queueno = (TextView) convertView.findViewById(R.id.txtqueue_queue);
            v.txtcurrentqueue = (TextView) convertView.findViewById(R.id.txtcurrentqueue);
            v.queuestatus = (TextView) convertView.findViewById(R.id.txtqueue_status);
            // v.imgbtn_delete = (ImageButton)convertView.findViewById(R.id.img_delete);
            v.rlcurrentno = (RelativeLayout)convertView.findViewById(R.id.rlcurrent_no);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        final QueuelistView obj_queuelist = lstqueuelist.get(position);
        v.queuedate.setText(obj_queuelist.getRequestdatetime());

        v.queueclinic.setText(obj_queuelist.getClinicname());

        med = dbmedical.getMedProfilebyNRIC(obj_queuelist.getPatientnric_type(),obj_queuelist.getPatientnric());

        String name = "";
        if(!obj_queuelist.getPatientname().equals("")){
            name = obj_queuelist.getPatientname();
        }else {
            name = med.getNric_name();
        }
        String patient = name+ " ("+obj_queuelist.getPatientnric()+")";

        obj_queuelist.setPatientname(name);
        v.request_nric.setText(patient);
        v.queueno.setText("" + obj_queuelist.getQueueno());
        v.queuestatus.setText(obj_queuelist.getQstatus());
        v.txtcurrentqueue.setText(obj_queuelist.getCurrentqueue()+"");

        if (Double.parseDouble(obj_queuelist.getQueueno()) < 0) {
            v.queuestatus.setTextColor(_mcont.getResources().getColor(R.color.colorred));
            v.queueno.setText("---");
            //  v.imgbtn_delete.setVisibility(View.GONE);
            // v.rlcurrentno.setVisibility(View.GONE);
        } else if (Double.parseDouble(obj_queuelist.getQueueno()) == 0) {

            v.queuestatus.setTextColor(_mcont.getResources().getColor(R.color.cas_color_tab_yellow));
            //  v.imgbtn_delete.setVisibility(View.VISIBLE);
            v.queueno.setText("---");
            // v.rlcurrentno.setVisibility(View.GONE);


        } else {
            v.queuestatus.setTextColor(_mcont.getResources().getColor(R.color.cas_success_green));
            //  v.imgbtn_delete.setVisibility(View.GONE);
            //  v.rlcurrentno.setVisibility(View.VISIBLE);
        }


    /*    v.imgbtn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_mcont);
                alertDialogBuilder.setMessage(_mcont.getString(R.string.error_response_queue_delete));
                alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                      *//*  int map_queue = queueloglist.get(position).getQueue_no();
                       // if (map_queue != 0) {
                            delete( queueloglist.get(position));
                            queueloglist.remove(position);
                            FragmentQueue.adapter.notifyDataSetChanged();

                        //}*//*
                        if (!usertoken.equals("")) {
                            if (Utils.hasInternetConnection(_mcont)) {
                                if (preferences.getBoolean(_mcont.getString(R.string.pref_is_account_verified), false)) {
                                    RequestQueueCancel data_qcancel = new RequestQueueCancel(obj_queuelist.getClinicid(),obj_queuelist.getRequestid(),obj_queuelist.getQueueno());
                                    JSONObject json_qcancel = data_qcancel.StringtoJsonObject(data_qcancel.ObjecttoJson());
                                    VolleyQueueCancel v_qcancel = new VolleyQueueCancel(_mcont);
                                    v_qcancel.GetQueueCancelJson(usertoken,obj_queuelist.getClinicid() ,json_qcancel, new VolleyQueueCancel.VolleyCallback() {
                                        @Override
                                        public void onSuccess(String message) {
                                            lstqueuelist.get(position).setQstatus("Cancelled");
                                            lstqueuelist.get(position).setQueueno("-1");
                                            FragmentQueue.adapter.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onFail(String errorcode, String errormessage) {
                                            Utils.showAlertDialog(_mcont,errorcode,errormessage);
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
        });*/

        return convertView;
    }
    public void delete(RequestQueueLog _map) {
        DBQueueRequest deleteRQL = new DBQueueRequest(_mcont);
        deleteRQL.deleteQueuebyRequestId(_map.getRequest_id());
    }
    class ViewHolder {
        TextView queuedate;
        TextView queueclinic;
        TextView request_nric;
        TextView queueno;
        TextView queuestatus;
        TextView txtcurrentqueue;
        // ImageButton imgbtn_delete;
        RelativeLayout rlcurrentno;
    }


}

