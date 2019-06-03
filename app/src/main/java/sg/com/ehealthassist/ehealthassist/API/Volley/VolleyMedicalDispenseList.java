package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestMedicalDispense;
import sg.com.ehealthassist.ehealthassist.Alarm.ReminderReceiver;
import sg.com.ehealthassist.ehealthassist.DB.DBMedDispense;
import sg.com.ehealthassist.ehealthassist.DB.DBMedReminder;
import sg.com.ehealthassist.ehealthassist.DB.DBMedicalItem;
import sg.com.ehealthassist.ehealthassist.Medication.MedicalReminderTime;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalDispense;
import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalItem;
import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalReminder;
import sg.com.ehealthassist.ehealthassist.Other.AppController;
import static android.content.Context.ALARM_SERVICE;
/**
 * Created by thazinhlaing on 2/7/17.
 */

public class VolleyMedicalDispenseList {
    private String Tag_json_obj = "_req_medDispense_list_request";
    Context _vcontext;
    DBMedDispense db_medDispense;
    DBMedicalItem db_meditem;
    DBMedReminder dbmedreminder;
    String accesstoken = "";

    public interface VolleyCallback {
        void onSuccess(String count);

        void onFail(String errorcode, String errormessage);
    }
    public VolleyMedicalDispenseList(Context mcontext, String accesstoken){
        this._vcontext = mcontext;
        db_medDispense = new DBMedDispense(_vcontext);
        db_meditem = new DBMedicalItem(_vcontext);
        dbmedreminder = new DBMedReminder(_vcontext);
        this.accesstoken = accesstoken;

    }

    public void GetMedDispenseLists(VolleyCallback callback){

        JsonObjectRequest _reqjson = new JsonObjectRequest(Request.Method.GET, Constant.URL_MedDispenselist,
                createMyReqSuccessListener(callback),createMyReqErrorListener(callback)){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "APPLICATION/json; charset=utf-8");
                headers.put("Authorization",accesstoken);

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "APPLICATION/json; charset=utf-8";
            }
        };
        _reqjson.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(_reqjson, Tag_json_obj);
    }

    //============================================================
    //			Response Result
    //============================================================
    private Response.Listener<JSONObject> createMyReqSuccessListener(final VolleyCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                setTvResultText(response, callback);
            }
        };
    }
    private Response.ErrorListener createMyReqErrorListener(final VolleyCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body = "";
                try {
                    if (error.networkResponse.data != null) {
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");

                            getError(body, callback);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {

                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
    }
    public void setTvResultText(JSONObject response, VolleyCallback callback) {

        try {
            if(response.has(Constant.NODE_STATUS)){
                Log.e("medical",response.toString());

                if(response.getString(Constant.NODE_STATUS).equals("OK")){
                    JSONArray data = response.getJSONArray("Data");
                    ArrayList<MedicalDispense> lstmedDispense = new ArrayList<MedicalDispense>();
                    if(data.length()>0){
                        for(int d =0;d<data.length();d++){
                            JSONObject json_obj = data.getJSONObject(d);
                            String memberid = json_obj.getString("MemberId");
                            String Nric = json_obj.getString("Nric");
                            String NricType = json_obj.getString("NricType");
                            String Name = json_obj.getString("Name");
                            String DOB = json_obj.getString("DOB");
                            String QueueID = json_obj.getString("QueueID");
                            String VisitNo = json_obj.getString("VisitNo");
                            String VisitDate = json_obj.getString("VisitDate");
                            String ClinicId = json_obj.getString("ClinicId");
                            JSONArray _jsonmedilist = json_obj.getJSONArray("listmedItem");
                            //get medical item list
                            ArrayList<MedicalItem> _medlist=new ArrayList<MedicalItem>();

                            for(int i =0;i < _jsonmedilist.length();i++){
                                JSONObject obj_medilist = _jsonmedilist.getJSONObject(i);
                                String id = obj_medilist.getString("id");
                                String itemno = obj_medilist.getString("itemno");
                                String medicalType = obj_medilist.getString("medicalType");
                                String medicalCode = obj_medilist.getString("medicalCode");
                                String medicalName = obj_medilist.getString("medicalName");
                                String medicalUsage = obj_medilist.getString("medicalUsage");
                                String medicalDosage = obj_medilist.getString("medicalDosage");
                                String medicalDosageUnit = obj_medilist.getString("medicalDosageUnit");
                                String medicalFreq = obj_medilist.getString("medicalFreq");
                                String medicalFreqCode = obj_medilist.getString("medicalFreqCode");
                                String medicalTotalQty = obj_medilist.getString("medicalTotalQty");
                                String medicalTotalQtyUnit = obj_medilist.getString("medicalTotalQtyUnit");
                                String PreCaution1 = obj_medilist.getString("PreCaution1");
                                String PreCaution2 = obj_medilist.getString("PreCaution2");
                                String PreCaution3 = obj_medilist.getString("PreCaution3");
                                String downloaded = obj_medilist.getString("downloaded");


                                MedicalItem  _newitem = new MedicalItem(VisitNo,ClinicId, id,itemno,medicalType,medicalCode,medicalName,
                                            medicalUsage,medicalDosage,medicalDosageUnit,medicalFreq,medicalFreqCode,medicalTotalQty,
                                            medicalTotalQtyUnit,PreCaution1,PreCaution2,PreCaution3,downloaded,0,0,0,0L,0,"");



                                _medlist.add(_newitem);
                            }
                            ArrayList<MedicalItem> existItem = db_meditem.getmedicalItems(VisitNo);

                            for(int i=0;i<existItem.size();i++){
                                int hasid = 0;
                                for(int ii=0;ii<_medlist.size();ii++){
                                    if(_medlist.get(ii).getItemno().equals(existItem.get(i).getItemno())){
                                        _medlist.get(ii).setIs_reminder(existItem.get(i).getIs_reminder());
                                        _medlist.get(ii).setStartdate(existItem.get(i).getStartdate());
                                        _medlist.get(ii).setEnddate(existItem.get(i).getEnddate());
                                        _medlist.get(ii).setNextdate(existItem.get(i).getNextdate());
                                        _medlist.get(ii).setNumberofdays(existItem.get(i).getNumberofdays()) ;
                                        _medlist.get(ii).setSlotinterval(existItem.get(i).getSlotinterval());
                                        hasid = 1;
                                        break;
                                    }
                                    else{
                                        hasid = 0;
                                    }
                                }
                                if(hasid>0){
                                    db_meditem.deletemeditembyid(existItem.get(i).getId(),existItem.get(i).getVisitno());
                                }
                            }
                            //save medical item
                            for(int add=0;add<_medlist.size();add++){

                                long update = db_meditem.updatemedicalItem(_medlist.get(add));
                                //update downloaded 'Y'
                                if(update>0){
                                    RequestMedicalDispense reqparam = new RequestMedicalDispense();
                                    JSONObject jsonparam = reqparam.StringtoJsonObject(reqparam.IDObjecttoJson(_medlist.get(add).getId()));
                                    VolleyMedDispenseUpdateDownload _vdispendupdate = new VolleyMedDispenseUpdateDownload(_vcontext);
                                    _vdispendupdate.PostUpdateDownloadMedDispense(accesstoken, jsonparam, new VolleyMedDispenseUpdateDownload.VolleyCallback() {
                                        @Override
                                        public void onSuccess(String mes) {
                                            Log.e("update",mes);
                                        }

                                        @Override
                                        public void onFail(String errorcode, String errormessage) {
                                            Log.e(errorcode,errormessage);
                                        }
                                    });
                                }
                            }
                            //save medical dispense
                            MedicalDispense _newdispense = new MedicalDispense(memberid,Nric,NricType,DOB,QueueID,VisitNo,VisitDate,ClinicId,Name);
                            lstmedDispense.add(_newdispense);
                        }
                        if(lstmedDispense.size()>0){
                            ArrayList<MedicalDispense> extdispens = db_medDispense.getallmedicalDispense();
                            for(int d = 0;d<extdispens.size();d++){
                                int hasvisitno = 0;
                                for(int dd= 0 ;dd <lstmedDispense.size();dd++){
                                    if(!lstmedDispense.get(dd).getVisitNo().equals(extdispens.get(d).getVisitNo())){
                                        hasvisitno = 1;
                                    }else{
                                        hasvisitno = 0;
                                        break;
                                    }
                                }
                                if(hasvisitno>0){
                                    db_medDispense.deletemedicaldispense(extdispens.get(d).getVisitNo());
                                }
                            }
                        }
                        for(int i =0;i<lstmedDispense.size();i++){
                            db_medDispense.updatemedicalDispense(lstmedDispense.get(i));
                        }
                    }
                    else{
                        ArrayList<MedicalDispense> canceldisplist = db_medDispense.getallmedicalDispense();
                        for (int can = 0;can< canceldisplist.size();can++){
                            cancelreminderAlarm(canceldisplist.get(can).getVisitNo());
                        }
                        db_medDispense.deleteallmedicaldispense();
                        db_meditem.deleteallmedicalitem();
                    }
                }
            }
            callback.onSuccess("OK");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public void cancelreminderAlarm(String visitno){
        try{
            ArrayList<MedicalReminder> lstreminder  = dbmedreminder.getmedicationReminderbyvisitno(visitno);
            if(lstreminder.size()>0){
                for(int i =0;i<lstreminder.size();i++){
                    AlarmManager alarmManager = (AlarmManager) _vcontext.getSystemService(ALARM_SERVICE);
                    Intent myIntent = new Intent(_vcontext, ReminderReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(_vcontext, Integer.parseInt(lstreminder.get(i).getUuid()), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.cancel(pendingIntent);
                }
                dbmedreminder.deletemedreminderbyvisitno(visitno);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
    public void getError(String error, VolleyCallback callback) {
        try {
            try {
                JSONObject errorjson = new JSONObject(error);
                String errormessage="";
                if(errorjson.has("Message")){
                     errormessage = errorjson.getString("Message");
                }
                callback.onFail("Message", errormessage);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}


