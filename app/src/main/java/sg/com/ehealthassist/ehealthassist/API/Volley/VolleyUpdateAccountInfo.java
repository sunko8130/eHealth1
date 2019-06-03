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
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 6/30/2017.
 */

public class VolleyUpdateAccountInfo {
    private String Tag_json_obj = "_req_accountinfo_request";
    Context _vcontext;
    SharedPreferences preferences = null;

    public VolleyUpdateAccountInfo(Context mcontext) {
        _vcontext = mcontext;
        preferences = _vcontext.getSharedPreferences(_vcontext.getString(R.string.preference_name), _vcontext.MODE_PRIVATE);
    }

    public interface VolleyCallback {
        void onSuccess(String message);

        void onFail(String errorcode, String errormessage);
    }

    public void GetAccountUpdateRequest(JSONObject param, VolleyCallback callback) {
        Log.e("rqupdateacc", param.toString());
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                Constant.URL_AccountUpdate, param,
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
                Log.e("update log", response.toString());
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
                            Log.e("Sms Error Upload", body);
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
            String status = response.getString(Constant.NODE_STATUS);
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                if(response.has(Constant.NODE_DATA)){
                    JSONObject data_json = response.getJSONObject(Constant.NODE_DATA);
                    if(data_json.has(Constant.NODE_MobileNo)){
                        String phone = data_json.getString(Constant.NODE_MobileNo);
                    }
                    if(data_json.has(Constant.NODE_EMAIL)){
                        String email = data_json.getString(Constant.NODE_EMAIL);
                    }
                    if(data_json.has(Constant.NODE_MemberId)){
                        String member_id = data_json.getString(Constant.NODE_MemberId);
                    }
                    if(data_json.has(Constant.NODE_CountryISOCode)){
                        String isocode = data_json.getString(Constant.NODE_CountryISOCode);
                    }
                }

               /* preferences.edit()
                        .putString(_vcontext.getString(R.string.pref_login_memberId), member_id)
                        .putString(_vcontext.getString(R.string.pref_main_user_data_hp), phone)
                        .putString(_vcontext.getString(R.string.pref_main_user_data_email), email)
                        .apply();
*/
            }
            callback.onSuccess(status);
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
}
