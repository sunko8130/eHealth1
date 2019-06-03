package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import sg.com.ehealthassist.ehealthassist.DB.DBQueueRequest;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Queue.RequestQueueLog;
import sg.com.ehealthassist.ehealthassist.Other.AppController;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class VolleyQueueRegister {
    private String Tag_json_obj = "_req_queue_register_api";
    Context _vcontext;

    int _clinicid = 0;
    String _clinicname = "";
    String _patientnric = "";
    String _patientdob = "";
    String _usertoken;
    String requestname;
    private ProgressDialog pDialog;
    SharedPreferences preferences = null;

    public interface VolleyCallback {
        void onSuccess(String status);

        void onFail(String errorcode, String errormessage);
    }

    public VolleyQueueRegister(Context _mcontext, String request_name, int clinicid, String clinicname, String patientdob, String patientnric, String usertoken) {
        _vcontext = _mcontext;
        _clinicid = clinicid;
        _clinicname = clinicname;
        _patientdob = patientdob;
        _patientnric = patientnric;
        _usertoken = usertoken;
        requestname = request_name;
        preferences = _vcontext.getSharedPreferences(_vcontext.getString(R.string.preference_name), _vcontext.MODE_PRIVATE);
        pDialog = new ProgressDialog(_vcontext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
    }


    //============================================================
    //			Get Method All Queue Register
    //============================================================

    public void PostQueueRegisterJson(final int clinicId, final String accesstoken, JSONObject param, VolleyCallback callback) {
        showpDialog();
        Log.e("token", accesstoken);
        Log.e("clinicid", clinicId + "");
        Log.e("rqqueuejson", param.toString());
        String uri = String
                .format(Constant.URL_QUEUEREGISTER + clinicId
                );

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                uri, param,
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
                hidepDialog();
                setTvResultText(response, callback);
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener(final VolleyCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidepDialog();
                String body = "";
                try {
                    if (error.networkResponse.data != null) {
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            Log.e("Response Error Upload", body);
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
        Log.e("queue register", response.toString());
        try {
            String status = "",requestId="",qmessage="";
            if(response.has(Constant.NODE_STATUS)){
                 status = response.getString(Constant.NODE_STATUS);
            }
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                if(response.has(Constant.NODE_DATA)){
                    JSONObject data = response.getJSONObject(Constant.NODE_DATA);
                    if(data.has(Constant.NODE_RequestId)){
                         requestId = data.getString(Constant.NODE_RequestId);
                    }
                    if(data.has(Constant.NODE_QMessage)){
                         qmessage = data.getString(Constant.NODE_QMessage);
                    }
                }
                RequestQueueLog new_queue_log = new RequestQueueLog(0, _patientnric, _usertoken, Utils.getcurrenttime("dd-MMM-yyyy hh:mm a"), _clinicid, _clinicname, _patientdob,
                        requestId, 0, "", qmessage, requestname, "Pending");
                saverequestLog(new_queue_log);

                callback.onSuccess("Ok");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void saverequestLog(RequestQueueLog Qlog) {
        DBQueueRequest dbqlog = new DBQueueRequest(_vcontext);
        dbqlog.addRequestLog(Qlog);
    }

    public void getError(String error, VolleyCallback callback) {
        try {
            JSONObject errorjson = new JSONObject(error);
            String errorcode="",errormsg ="";

            if(errorjson.has(Constant.NODE_DATA)){
                JSONObject data = errorjson.getJSONObject(Constant.NODE_DATA);
                if(data.has(Constant.NODE_ErrorMessage)){
                     errormsg = data.getString(Constant.NODE_ErrorMessage);
                }
                if(data.has(Constant.NODE_ErrorCode)){
                     errorcode = data.getString(Constant.NODE_ErrorCode);
                }
            }

            callback.onFail(errorcode, errormsg);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

