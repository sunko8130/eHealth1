package sg.com.ehealthassist.ehealthassist.API.Volley;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

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

public class VolleyAuthenticationService {
    private String Tag_json_obj = "_req_auth_token_request";
    Context _vcontext;

    public interface VolleyCallback {
        void onSuccess(JSONObject message);

        void onFail(String errorcode, String errormessage);
    }

    public VolleyAuthenticationService(Context mcontext) {
        this._vcontext = mcontext;
    }

    public void GetStringRequest(final String username, final String passsword, VolleyCallback callback) {
        Log.e("jwt param", username + " " + passsword);

        StringRequest req = new StringRequest(Request.Method.POST, Constant.URL_Authtoken,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback)) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> pars = new HashMap<String, String>();
                pars.put("Content-Type", "application/x-www-form-urlencoded");
                return pars;
            }

            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded";
            }

            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pars = new HashMap<String, String>();
                pars.put("grant_type", _vcontext.getString(R.string.mobile_jwtgranttype));
                pars.put("client_id", _vcontext.getString(R.string.mobile_jwtclientid));
                pars.put("client_secret", "");
                pars.put("username", username);
                pars.put("password", passsword);
                return pars;
            }
        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(req, Tag_json_obj);
    }

    //============================================================
    //			Response Result
    //============================================================
    private Response.Listener<String> createMyReqSuccessListener(final VolleyCallback callback) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                setTvResultText(response, callback);
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener(final VolleyCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body = "";
                int statuscode = 0;
                try {
                    if (error.networkResponse.data != null) {
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            statuscode = error.networkResponse.statusCode;
                            getError(body, statuscode, callback);

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

    public void setTvResultText(String response, VolleyCallback callback) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.e("auth service", response.toString());
            callback.onSuccess(jsonObject);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public void getError(String error, int statuscode, VolleyCallback callback) {
        String errorcode = "";
        String errormsg = "";
        try {
            JSONObject errorjson = new JSONObject(error);
            if (statuscode == 400) {
                errorcode = errorjson.getString("error");
                errormsg = errorjson.getString("error_description");
            }
            callback.onFail(errorcode, errormsg);

        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onFail(errorcode, errormsg);
        }
    }
}
