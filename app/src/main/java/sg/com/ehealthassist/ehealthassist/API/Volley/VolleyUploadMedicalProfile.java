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

import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.AppController;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Profiles.ActivityMedicalProfile;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class VolleyUploadMedicalProfile {
    private String Tag_json_obj = "_req_upload_medicalprofile_api";
    Context _vcontext;
    String clinicName, background = "";
    SharedPreferences preferences = null;
    private ProgressDialog pDialog;
    MedicalProfile profile;
    DBMedProfile dbmedprofile;

    public interface VolleyCallback {
        void onSuccess(String status);

        void onFail(String errorcode, String errormsg);
    }

    public VolleyUploadMedicalProfile(Context _mcontext, String name, MedicalProfile upload_profile, String bg) {
        _vcontext = _mcontext;
        clinicName = name;
        dbmedprofile = new DBMedProfile(_mcontext);
        this.profile = upload_profile;
        preferences = _vcontext.getSharedPreferences(_vcontext.getString(R.string.preference_name), _vcontext.MODE_PRIVATE);
        this.background = bg;
        if (!this.background.equals("list")) {
            pDialog = new ProgressDialog(_vcontext);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
        }
    }
    //============================================================
    //			Get Method All product option
    //============================================================

    public void PostUploaddProfileJson(final String accesstoken, JSONObject param, VolleyCallback callback) {

        Log.e("rquploadjson", param.toString());
        if (!this.background.equals("list")) {
            showpDialog();
        }
        String uri = String
                .format(Constant.URL_MEDICALPROFILEUPLOAD
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
                        Toast.makeText(_vcontext, "established Connection Failed", Toast.LENGTH_LONG).show();
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        };
    }

    public void setTvResultText(JSONObject response, VolleyCallback callback) {
        profile.setFlag_upload(1);
        dbmedprofile.updateMedicalProfile(profile);

        if (this.background.equals("rq")) {
            callback.onSuccess("Ok");
        } else if (!this.background.equals("list")) {
            hidepDialog();
            ActivityMedicalProfile._mactivity.finish();
            Utils.showAlertDialog(_vcontext, "Profile Submission Successful", _vcontext.getString(R.string.error_response_upload_profile_submission_success) + " " + clinicName + " to download your profile", "ActivityMedicalProfileList");
            callback.onSuccess("Ok");
        }
    }

    public void getError(String error, VolleyCallback callback) {
        if (!this.background.equals("list")) {
            hidepDialog();
            try {
                Log.e("Error mess:", error);
                JSONObject errorjson = new JSONObject(error);
                String errorcode="",errormsg="";
                if(errorjson.has(Constant.NODE_DATA)){
                    JSONObject data = errorjson.getJSONObject(Constant.NODE_DATA);
                    if(data.has(Constant.NODE_ErrorCode)){
                         errorcode = data.getString(Constant.NODE_ErrorCode);
                    }
                    if(data.has(Constant.NODE_ErrorMessage)) {
                        errormsg = data.getString(Constant.NODE_ErrorMessage);
                    }
                }

                ActivityMedicalProfile._mactivity.finish();
                callback.onFail(errorcode, errormsg);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    private void showpDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}

