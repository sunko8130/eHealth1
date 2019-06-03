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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by User on 6/29/2017.
 */

public class VolleyVersionChanges  {

    private String Tag_json_obj = "_req_version_request";
    Context _vcontext;

    public interface VolleyCallback {
        void onSuccess(String version,String version_url,boolean forceupdate);

        void onFail(String errorcode,String errormessage);
    }
    public VolleyVersionChanges(Context mcontext){
        _vcontext = mcontext;
    }

    public void GetVersionRequest(JSONObject param, VolleyCallback callback){
        Log.e("param",param.toString());
        String uri = String
                .format(Constant.URL_VERSIONCHANGES);


        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                uri,param,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback)) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "APPLICATION/json; charset=utf-8");

                return headers;
            }

            @Override
            public String getBodyContentType() {
                return "APPLICATION/json; charset=utf-8";
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(myReq,Tag_json_obj);
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener(final VolleyCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setTvResultText(response, callback);
            }
        };
    }
    public void setTvResultText(JSONObject response, VolleyCallback callback) {
        try {
            String status = response.getString(Constant.NODE_STATUS);
            String version="",version_url = ""; boolean forceupdate = false;
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                JSONObject data_json = response.getJSONObject(Constant.NODE_DATA);
                version = data_json.getString(Constant.NODE_VersionNo);
                version_url = data_json.getString(Constant.NODE_URL);
                forceupdate = data_json.getBoolean(Constant.NODE_ForceUpdate);
            }

            callback.onSuccess(version,version_url,forceupdate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
                            Log.e("Response Error Upload", body);
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

    public void getError(String error, VolleyCallback callback) {
        String errorcode ="",errormsg="";
        try {
            JSONObject errorjson = new JSONObject(error);

            if(errorjson.has(Constant.NODE_DATA)){
               JSONObject data = errorjson.getJSONObject(Constant.NODE_DATA);
                if(data.has(Constant.NODE_ErrorCode)){
                     errorcode = data.getString(Constant.NODE_ErrorCode);
                }
                if(data.has(Constant.NODE_ErrorMessage)){
                     errormsg = data.getString(Constant.NODE_ErrorMessage);
                }
            }

            callback.onFail(errorcode,errormsg);

        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onFail(errorcode,errormsg);
        }
    }
}
