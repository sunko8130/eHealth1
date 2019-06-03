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
 * Created by thazinhlaing on 1/7/17.
 */
public class VolleyInsertMemberListProfile {
    private String Tag_json_obj = "_req_insert_memberprofile";
    Context _vcontext;
    public interface VolleyCallback {
        void onSuccess(String message);

        void onFail(String errorcode, String errormessage);
    }
    public VolleyInsertMemberListProfile(Context mcontext) {
        this._vcontext = mcontext;
    }

    public void GetInsertMemberprofileRequest(JSONObject param, final String accesstoken, VolleyCallback callback) {

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                Constant.URL_insertmemberprofile, param,
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
    private Response.Listener<JSONObject> createMyReqSuccessListener(final VolleyInsertMemberListProfile.VolleyCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                setvolleyResultText(response, callback);
            }
        };
    }

    public void setvolleyResultText(JSONObject resultjson, VolleyCallback callback){
        try {
            String status = resultjson.getString(Constant.NODE_STATUS);
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                JSONObject dataarray = resultjson.getJSONObject(Constant.NODE_DATA);
                if(dataarray.has("MemberId")){
                    String memberid =  dataarray.getString("MemberId");
                }
                if(dataarray.has("Message")){
                    String message  = dataarray.getString("Message");
                }
            }
            Log.e("insert profile", resultjson.toString());
            callback.onSuccess(status);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    //============================================================
    //			Response Error
    //============================================================
    private Response.ErrorListener createMyReqErrorListener(final VolleyInsertMemberListProfile.VolleyCallback callback) {
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
    public void getError(String error, VolleyCallback callback) {

        try {
            JSONObject errorjson = new JSONObject(error);
            String errorcode = "",errormsg ="";
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

}
