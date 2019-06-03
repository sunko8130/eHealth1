package sg.com.ehealthassist.ehealthassist.API.Volley;

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
import java.util.Map;

import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Profile.ResponseDownloadStatus;
import sg.com.ehealthassist.ehealthassist.Other.AppController;
import sg.com.ehealthassist.ehealthassist.Other.Utils;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class VolleyPatientProfileDownloadStatus {
    private String Tag_json_obj = "_req_patientprofiledownload_api";
    Context _vcontext;

    public VolleyPatientProfileDownloadStatus(Context _mcontext) {
        _vcontext = _mcontext;
    }

    public interface VolleyCallback {
        void onSuccess(ArrayList<ResponseDownloadStatus> status);

        void onFail(String errorcode, String errormessage);
    }
    //============================================================
    //			Post Method PatientProfileDownloadStatus
    //============================================================

    public void PostPatientProfileDownloadStatusJson(final String accesstoken , JSONObject param, VolleyCallback callback) {

        String uri = String
                .format(Constant.URL_PatientProfileDownloadStatus
                );
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                uri, param,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback)) {
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

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(myReq, Tag_json_obj);

    }

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
                            Log.e("Response Error Clinic", body);
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
        try {
            ArrayList<ResponseDownloadStatus> response_downloadlist = new ArrayList<ResponseDownloadStatus>();
            String status = response.getString(Constant.NODE_STATUS);
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                if (response.has(Constant.NODE_DATA)) {
                    Log.e("downlaod status", response.toString());
                    JSONArray jsonarr_object = response.getJSONArray(Constant.NODE_DATA);
                    for (int i = 0; i < jsonarr_object.length(); i++) {
                        JSONObject json_profile_obj = jsonarr_object.getJSONObject(i);
                        response_downloadlist.add(GetPatientProfileDownloadStatus(json_profile_obj));
                    }

                }
            }

            callback.onSuccess(response_downloadlist);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getError(String error, VolleyCallback callback) {
        try {
            Log.e("Error mess:", error);
            JSONObject errorjson = new JSONObject(error);
            String errorcode="",errormsg="";
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

    public ResponseDownloadStatus GetPatientProfileDownloadStatus(JSONObject ppdownloadstatus) {
        ResponseDownloadStatus response_object = new ResponseDownloadStatus();
        try {
            if (ppdownloadstatus.has("ID")) {
                response_object.setId(ppdownloadstatus.getInt("ID"));
            }
            if (ppdownloadstatus.has("ClinicID")) {
                response_object.setClinic(ppdownloadstatus.getInt("ClinicID"));
            }
            if (ppdownloadstatus.has("Nric")) {
                response_object.setNric(ppdownloadstatus.getString("Nric"));
            }
            if (ppdownloadstatus.has("NricType")) {
                response_object.setNrictype(ppdownloadstatus.getString("NricType"));
            }
            if (ppdownloadstatus.has("DOB")) {
                String dob_fromat = Utils.GridviewChangefromat(ppdownloadstatus.getString("DOB"), "yyyy-MM-dd", "dd-MMM-yyyy");
                response_object.setDob(dob_fromat);
            }
            if (ppdownloadstatus.has("MemberID")) {
                response_object.setMemberid(ppdownloadstatus.getString("MemberID"));
            }
            if (ppdownloadstatus.has("ModifiedDate")) {
                response_object.setModifiedDate(ppdownloadstatus.getString("ModifiedDate"));
            }
            if (ppdownloadstatus.has("Downloaded")) {
                response_object.setDownloaded(ppdownloadstatus.getString("Downloaded"));
            }
            if (ppdownloadstatus.has("HECode")) {
                response_object.setHecode(ppdownloadstatus.getString("HECode"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return response_object;
    }
}

