package sg.com.ehealthassist.ehealthassist.API.Volley;

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

import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class VolleyQueueWatch {
    private String Tag_json_obj = "_req_queuewatch_api";
    Context _vcontext;
    SharedPreferences preferences = null;

    public interface VolleyCallback {
        void onSuccess(int currentqueueno);

        void onFail(String errorcode,String errormsg);
    }

    public VolleyQueueWatch(Context vcontext) {
        this._vcontext = vcontext;
    }
    //============================================================
    //			Get Method  Queue Watch
    //============================================================
    public void PostQueueWatchJson(final int clinicId, final String accesstoken, VolleyCallback callback) {
        Log.e("token", accesstoken);
        Log.e("clinicid", clinicId + "");
        String uri = String
                .format(Constant.URL_QUEUEWATCH + clinicId
                );

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
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
                //  hidepDialog();
                setTvResultText(response,callback);
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener(final VolleyCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // hidepDialog();
                String body = "";
                try{
                    if (error.networkResponse.data != null) {
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            Log.e("Response Error Upload", body);
                            getError(body,callback);

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

    public void setTvResultText(JSONObject response,VolleyCallback callback) {
        Log.e("queue watch", response.toString());
        try {
            if(response.has(Constant.NODE_STATUS)){
                String status = response.getString(Constant.NODE_STATUS);
                int currentQueueNo =0;
                if (status.equals(Constant.CHECK_STATUS_OK)) {
                    if(response.has(Constant.NODE_DATA)){
                        JSONObject data = response.getJSONObject(Constant.NODE_DATA);
                        if(data.has(Constant.NODE_IsOpen)){
                            boolean is_clinic_open = data.getBoolean(Constant.NODE_IsOpen);
                        }
                        if(data.has(Constant.NODE_NoOfPeopleInQueue)){
                            int noOfpeopleInQueue = data.getInt(Constant.NODE_NoOfPeopleInQueue);
                        }
                        if(data.has(Constant.NODE_CurrentQueueNo)){
                             currentQueueNo = data.getInt(Constant.NODE_CurrentQueueNo);
                        }
                    }
                    callback.onSuccess(currentQueueNo);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void getError(String error,VolleyCallback callback) {
        try {
            JSONObject errorjson = new JSONObject(error);
            String errorcode="",errormsg="";
            if(errorjson.has(Constant.NODE_STATUS)){
                String status = errorjson.getString(Constant.NODE_STATUS);
            }
            if(errorjson.has(Constant.NODE_DATA)){
                JSONObject data = errorjson.getJSONObject(Constant.NODE_DATA);
                if(data.has(Constant.NODE_ErrorMessage)){
                     errormsg = data.getString(Constant.NODE_ErrorMessage);
                }
                if(data.has(Constant.NODE_ErrorCode)){
                     errorcode = data.getString(Constant.NODE_ErrorCode);
                }
            }
            callback.onFail(errorcode,errormsg);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}


