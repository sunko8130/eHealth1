package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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
import java.util.List;
import java.util.Map;

import sg.com.ehealthassist.ehealthassist.Models.Appointment.AppointmentSlot;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class VolleyListAppointmentSlot {
    private String Tag_json_obj = "_req_appointmeslot_api";
    Context _vcontext;
    private ProgressDialog pDialog;
    List<String> anydoctor_availabletime = new ArrayList<String>();
    List<String> anydoctor_availabelsession = new ArrayList<String>();
    int clinicid = 0;

    public interface VolleyCallback {
        void onSuccess(ArrayList<AppointmentSlot> result);

        void onFail(String errorcode, String errormsg);
    }

    public VolleyListAppointmentSlot(Context _mcontext) {
        _vcontext = _mcontext;
        pDialog = new ProgressDialog(_vcontext);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
    }

    //===========================================================
    //			Get Method All product option
    //===========================================================
    public void GetListApptSlotJson(String urlJsonObj, int clinicid, String datestring, final String accesstoken, VolleyCallback callback) {
        showpDialog();
        String uri = String
                .format(urlJsonObj + clinicid + "/" + datestring + "/GetAppointmentList");
        Log.e("slot url",uri);
        this.clinicid = clinicid;
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,
                uri,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback)) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "APPLICATION/json; charset=utf-8");
                headers.put("Authorization", accesstoken);

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "APPLICATION/json; charset=utf-8";
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(myReq, Tag_json_obj);
    }

    //============================================================
    //			Response Result
    //============================================================
    private Response.Listener<JSONObject> createMyReqSuccessListener(final VolleyCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("List Appt Slot", response.toString());
                setTvResultText(response, callback);
            }
        };
    }

    public AppointmentSlot GetListApptSlot(JSONObject _data) {
        AppointmentSlot new_listappt = new AppointmentSlot();
        List<String> available_time = new ArrayList<String>();
        try {
            if (_data.has(Constant.NODE_ApptDoctor)) {

                JSONObject appt_json = _data.getJSONObject(Constant.NODE_ApptDoctor);
                if (appt_json.has(Constant.NODE_ApptId)) {
                    new_listappt.setId(appt_json.getInt(Constant.NODE_ApptId));
                }
                if (appt_json.has(Constant.NODE_ApptClinicId)) {
                    new_listappt.setClinicid(appt_json.getInt(Constant.NODE_ApptClinicId));
                }
                if (appt_json.has(Constant.NODE_ApptCasDoctorId)) {
                    new_listappt.setCasdoctorid(appt_json.getString(Constant.NODE_ApptCasDoctorId));
                }
                if (appt_json.has(Constant.NODE_ApptFullName)) {
                    new_listappt.setFullname(appt_json.getString(Constant.NODE_ApptFullName));
                }
                if (appt_json.has(Constant.NODE_ApptDisplayName)) {
                    new_listappt.setDisplayname(appt_json.getString(Constant.NODE_ApptDisplayName));
                }
                if (appt_json.has(Constant.NODE_ApptProfilePic)) {
                    new_listappt.setProfilepicture(appt_json.getString(Constant.NODE_ApptProfilePic));
                }
                if (appt_json.has(Constant.NODE_ApptSmc)) {
                    new_listappt.setSmc(appt_json.getString(Constant.NODE_ApptSmc));
                }
                if (appt_json.has(Constant.NODE_ApptStatus)) {
                    new_listappt.setStatus(appt_json.getInt(Constant.NODE_STATUS));
                }
                if (appt_json.has(Constant.NODE_ApptEditor)) {
                    new_listappt.setEditor(appt_json.getString(Constant.NODE_ApptEditor));
                }
                if (appt_json.has(Constant.NODE_ApptCreator)) {
                    new_listappt.setCreator(appt_json.getString(Constant.NODE_ApptCreator));
                }
            }
            if (_data.has(Constant.NODE_ApptAvailableSlots)) {
                JSONArray availableslot = _data.getJSONArray(Constant.NODE_ApptAvailableSlots);
                for (int i = 0; i < availableslot.length(); i++) {
                    available_time.add(availableslot.getString(i));
                    if(!anydoctor_availabletime.contains(availableslot.getString(i))){
                        anydoctor_availabletime.add(availableslot.getString(i));
                    }
                }
                new_listappt.setAvailabeleSlots(available_time);
            }
        } catch (Exception ex) {
            Log.e("Appoint Error", ex.toString());
        }
        return new_listappt;
    }

    private Response.ErrorListener createMyReqErrorListener(final VolleyCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body = "";
                try{
                    if (error.networkResponse.data != null) {
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            getError(body, callback);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(_vcontext, "established Connection Failed", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }
        };
    }

    public void setTvResultText(JSONObject response, VolleyCallback callback) {
        hidepDialog();
        ArrayList<AppointmentSlot> list_appoint = new ArrayList<AppointmentSlot>();
        try {
            String status = response.getString(Constant.NODE_STATUS);
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                JSONArray dataarray = response.getJSONArray(Constant.NODE_DATA);
                for (int i = 0; i < dataarray.length(); i++) {
                    JSONObject _data = dataarray.getJSONObject(i);
                    list_appoint.add(GetListApptSlot(_data));
                }
              /*  if(list_appoint.size()>0){
                    AppointmentSlot new_listappt = new AppointmentSlot();
                    new_listappt.setClinicid(clinicid);
                    new_listappt.setDisplayname("Any Doctor");
                    new_listappt.setApptSession(anydoctor_availabelsession);
                    Collections.sort(anydoctor_availabletime);
                    new_listappt.setAvailabeleSlots(anydoctor_availabletime);
                    list_appoint.add(0,new_listappt);
                }*/

                Log.e("size appoint slot", list_appoint.size() + "**");
            }
            callback.onSuccess(list_appoint);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getError(String error, VolleyCallback callback) {
        hidepDialog();
        try {
            JSONObject errorjson = new JSONObject(error);
            String errorcode ="",errormsg="";
            if(errorjson.has(Constant.NODE_DATA)){
                JSONObject data = errorjson.getJSONObject(Constant.NODE_DATA);
                if(data.has(Constant.NODE_ErrorCode)){
                     errorcode = data.getString(Constant.NODE_ErrorCode);
                }
                if(data.has(Constant.NODE_ErrorMessage)){
                     errormsg = data.getString(Constant.NODE_ErrorMessage);
                }
            }

            callback.onFail(errorcode, errormsg);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.dismiss();
        pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}


