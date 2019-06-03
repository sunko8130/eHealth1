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

public class VolleyMemberQuery {
    private String Tag_json_obj = "_req_memberquery_api";
    Context _vcontext;
    SharedPreferences preferences = null;
    String memberid = "";
    // DatabaseHandlerMedProfile medRQL;



    public VolleyMemberQuery(Context _mcontext) {
        _vcontext = _mcontext;
        preferences = _vcontext.getSharedPreferences(_vcontext.getString(R.string.preference_name), _vcontext.MODE_PRIVATE);
        memberid = preferences.getString(_vcontext.getString(R.string.pref_login_memberId), "");
        //medRQL = new DatabaseHandlerMedProfile(_vcontext);

    }

    public interface VolleyCallback {
        void onSuccess(String message);

        void onFail(String errorcode, String errormessage);
    }

    //============================================================
    //			Get Method All Doctor on Duty Register
    //============================================================

    public void GetMemberQueryJson(final String accesstoken,VolleyCallback callback) {

        String uri = String
                .format(Constant.URL_MemberQuery + memberid
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

    private Response.Listener<JSONObject> createMyReqSuccessListener(final VolleyCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setTvResultText(response,callback);
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
                            getError(body,callback);
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

    public void setTvResultText(JSONObject response,VolleyCallback callback) {
        try {
            String status = response.getString(Constant.NODE_STATUS);
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                JSONObject data = response.getJSONObject(Constant.NODE_DATA);
                String memberid = data.getString(Constant.NODE_MemberId);
                String Email = data.getString(Constant.NODE_EMAIL);
                String mobileno = data.getString(Constant.NODE_MobileNo);
                String countryisocode = data.getString(Constant.NODE_CountryISOCode);
                String countrycode = data.getString(Constant.NODE_CountryCode);
                String substr_mobile = mobileno.substring(countrycode.length()).toString();

                preferences.edit()
                        .putString(_vcontext.getString(R.string.pref_main_user_data_hp), substr_mobile)
                        .putString(_vcontext.getString(R.string.pref_main_user_data_email), Email)
                        .putString(_vcontext.getString(R.string.pref_main_user_data_hp_iso),countryisocode)
                        .putInt(_vcontext.getString(R.string.pref_main_user_data_hp_code),Integer.parseInt(countrycode))
                        .apply();
            }
            callback.onSuccess(status);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getError(String error,VolleyCallback callback) {
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

}
