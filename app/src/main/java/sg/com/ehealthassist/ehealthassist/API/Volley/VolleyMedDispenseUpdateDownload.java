package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.content.Context;

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
public class VolleyMedDispenseUpdateDownload {
    private String Tag_json_obj = "_req_medDispense_list_request";
    Context _vcontext;
    public interface VolleyCallback {
        void onSuccess(String mes);

        void onFail(String errorcode, String errormessage);
    }


    public VolleyMedDispenseUpdateDownload(Context mcontext){
        this._vcontext = mcontext;
    }

    public void PostUpdateDownloadMedDispense(final String accesstoken, JSONObject param, VolleyCallback callback){
        JsonObjectRequest _reqjson = new JsonObjectRequest(Request.Method.POST, Constant.URL_MedDispenseupdatedownload,param,
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
                if(response.getString(Constant.NODE_STATUS).equals("OK")){
                    JSONObject data = response.getJSONObject("Data");
                    String message  = data.getString("Message");
                    callback.onSuccess(message);
                }
            }
            else{
                if(response.has("Code")){
                    String errorcode = response.getString("Code");
                    String errormsg = response.getString("Message");
                    callback.onFail(errorcode,errormsg);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getError(String error, VolleyCallback callback) {
        try {
            try {
                JSONObject errorjson = new JSONObject(error);
                String errorcode = "",errormsg = "";
                if(errorjson.has(Constant.NODE_DATA)){
                    JSONObject data = errorjson.getJSONObject(Constant.NODE_DATA);
                    if(data.has(Constant.NODE_ErrorCode)){
                        errorcode = data.getString(Constant.NODE_ErrorCode);
                    }if(data.has(Constant.NODE_ErrorMessage)){
                        errormsg = data.getString(Constant.NODE_ErrorMessage);
                    }
                }
                callback.onFail(errorcode, errormsg);

            } catch (Exception ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

