package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestJsonRegister;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Other.AppController;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 6/30/2017.
 */

public class VolleyRegisterMember {
    private String Tag_json_obj = "_req_registermember_api";
    Context _vcontext;
    SharedPreferences preferences = null;
    RequestJsonRegister objregister;
    private ProgressDialog pDialog;
    DBMedProfile dbmedprofile;


    public interface VolleyCallback {
        void onSuccess(JSONObject data_json);

        void onFail(String errorcode, String errormessage);
    }

    public VolleyRegisterMember(Context _mcontext, RequestJsonRegister register) {
        _vcontext = _mcontext;
        objregister = register;
        dbmedprofile = new DBMedProfile(_vcontext);
        preferences = _vcontext.getSharedPreferences(_vcontext.getString(R.string.preference_name), _vcontext.MODE_PRIVATE);
        pDialog = new ProgressDialog(_vcontext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
    }

    //============================================================
    //			Get Method All product option
    //============================================================
    public void GetRegisterJson(JSONObject param, VolleyCallback callback) {
        showpDialog();
        //Log.e("rqregisterjson", param.toString());
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                Constant.URL_REGISTER, param,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback)) {
            @Override
            public String getBodyContentType() {
                return "APPLICATION/json; charset=utf-8";
            }
        };

        myReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
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
                Log.e("register success", response.toString());
                setTvResultText(response, callback);
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener(final VolleyCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body = "";
                Log.e("register fail", error.toString());
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
        hidepDialog();
        Log.e("register response", response.toString());
        try {
            String status = response.getString(Constant.NODE_STATUS);
            String memberid="",accesstoken="";
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                if(response.has(Constant.NODE_DATA)){
                    JSONObject data_json = response.getJSONObject(Constant.NODE_DATA);
                    if(data_json.has(Constant.NODE_MemberId)){
                         memberid = data_json.getString(Constant.NODE_MemberId);
                    }
                    if(data_json.has(Constant.NODE_AccessToken)){
                         accesstoken = data_json.getString(Constant.NODE_AccessToken);
                    }
                    callback.onSuccess(data_json);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void getError(String error, VolleyCallback callback) {
        hidepDialog();
        try {
            JSONObject errorjson = new JSONObject(error);
            String errorcode="",errormsg="";
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
