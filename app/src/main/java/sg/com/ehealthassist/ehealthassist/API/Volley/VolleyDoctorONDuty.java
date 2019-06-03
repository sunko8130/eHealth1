package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.content.Context;
import android.content.SharedPreferences;
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

import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Doctor.DoctorDutyList;
import sg.com.ehealthassist.ehealthassist.Models.Doctor.Doctors;
import sg.com.ehealthassist.ehealthassist.Other.AppController;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class VolleyDoctorONDuty {
    private String Tag_json_obj = "_req_dod_api";
    Context _vcontext;
    SharedPreferences preferences = null;

    public interface VolleyCallback {
        void onSuccess(DoctorDutyList result);

        void onFail(String errorcode, String errormsg);
    }

    public VolleyDoctorONDuty(Context _mcontext) {
        _vcontext = _mcontext;
        preferences = _vcontext.getSharedPreferences(_vcontext.getString(R.string.preference_name), _vcontext.MODE_PRIVATE);

    }
    //============================================================
    //			Get Method All Doctor on Duty Register
    //============================================================

    public void GetDoctorOnDutyJson(final int clinicId, final String accesstoken, VolleyCallback callback) {
        String uri = String
                .format(Constant.URL_DOCTORONDUTY + clinicId + "/dod"
                );

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
                        Toast.makeText(_vcontext, "established Connection Failed", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        };
    }

    public void setTvResultText(JSONObject response, VolleyCallback callback) {
        DoctorDutyList list_dod = new DoctorDutyList();
        try {
            if(response.has(Constant.NODE_STATUS)){
                String status = response.getString(Constant.NODE_STATUS);
                if (status.equals(Constant.CHECK_STATUS_OK)) {
                    JSONObject data_json = response.getJSONObject(Constant.NODE_DATA);
                    list_dod = GetDoctorObject(data_json);
                }
            }
            callback.onSuccess(list_dod);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getError(String error, VolleyCallback callback) {
        try {
            JSONObject errorjson = new JSONObject(error);
            String errorcode = "",errormsg="";
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

    public DoctorDutyList GetDoctorObject(JSONObject JSONDOC) {

        DoctorDutyList new_doclist = new DoctorDutyList();
        List<Doctors> list_doct = new ArrayList<Doctors>();
        try {
            if (JSONDOC.has(Constant.NODE_DUTYDATE)) {
                new_doclist.setDutydate(JSONDOC.getString(Constant.NODE_DUTYDATE));
            }
            if (JSONDOC.has(Constant.NODE_DOCTORS)) {
                JSONArray doctors_list = JSONDOC.getJSONArray(Constant.NODE_DOCTORS);
                for (int doc = 0; doc < doctors_list.length(); doc++) {
                    JSONObject json_obj = doctors_list.getJSONObject(doc);
                    list_doct.add(GetListDoctor(json_obj));
                }
                new_doclist.setDoctors(list_doct);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new_doclist;
    }

    public Doctors GetListDoctor(JSONObject json_obj) {
        Doctors new_doctor = new Doctors();
        try {
            int Id = 0;
            String name = "";
            if (json_obj.has(Constant.NODE_ID)) {
                Id = json_obj.getInt(Constant.NODE_ID);
            }
            if (json_obj.has(Constant.NODE_NAME)) {
                name = json_obj.getString(Constant.NODE_NAME);
            }
            new_doctor.setDr_id(Id);
            new_doctor.setDr_name(name);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new_doctor;
    }


}

