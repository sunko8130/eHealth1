package sg.com.ehealthassist.ehealthassist.API.Volley;

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

import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.AppController;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class VolleyAuth {
    private String Tag_json_obj = "_req_login_api";
    Context _vcontext;

    DBMedProfile dbmedprofile;
    SharedPreferences preferences = null;
    String nationality = "";

    public VolleyAuth(Context _mcontext, String nationality) {//String from, int clinic_Id{{
        _vcontext = _mcontext;
        this.nationality = nationality;
        preferences = _vcontext.getSharedPreferences(_vcontext.getString(R.string.preference_name), _vcontext.MODE_PRIVATE);
        dbmedprofile = new DBMedProfile(_vcontext);
    }

    public interface VolleyCallback {
        void onSuccess(String message);

        void onFail(String errorcode, String errormessage);
    }
    //============================================================
    //			Get Method All product option
    //============================================================

    public void GetClinicJson(JSONObject param, VolleyCallback callback) {
        //  showpDialog();
        Log.e("rqauthjson", param.toString());

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                Constant.URL_AUTH, param,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback));

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
                Log.e("volley", error.toString());
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
        try {
            String status = response.getString(Constant.NODE_STATUS);
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                String memberId = "", Email = "", Mobileno = "", countryisocode = "";
                String countrycode = "", substr_mobile = "", nric = "", nric_type = "";
                String name = "", dob = "";
                boolean AccountVerified = false;

                JSONObject data = response.getJSONObject(Constant.NODE_DATA);

                if (data.has(Constant.NODE_MemberId)) {
                    memberId = data.getString(Constant.NODE_MemberId);
                }
                if (data.has(Constant.NODE_EMAIL)) {
                    Email = data.getString(Constant.NODE_EMAIL);
                }
                if (data.has(Constant.NODE_MobileNo)) {
                    Mobileno = data.getString(Constant.NODE_MobileNo);
                }
                if (data.has(Constant.NODE_CountryISOCode)) {
                    countryisocode = data.getString(Constant.NODE_CountryISOCode);
                }
                if (data.has(Constant.NODE_CountryCode)) {
                    countrycode = data.getString(Constant.NODE_CountryCode);
                    substr_mobile = Mobileno.substring(countrycode.length()).toString();
                }
                if (data.has(Constant.NODE_NRIC)) {
                    nric = data.getString(Constant.NODE_NRIC);
                }
                if (data.has(Constant.NODE_NRICTYPE)) {
                    nric_type = data.getString(Constant.NODE_NRICTYPE);
                }
                if (data.has(Constant.NODE_ApptFullName)) {
                    name = data.getString(Constant.NODE_ApptFullName);
                }
                if (data.has("DOB")) {
                    dob = data.getString("DOB");
                }
                if (data.has(Constant.NODE_AccountVerified)) {
                    AccountVerified = data.getBoolean(Constant.NODE_AccountVerified);
                }

                // String AccessToken = data.getString(Constant.NODE_AccessToken);
                preferences.edit()
                        .putString(_vcontext.getString(R.string.pref_main_user_data_email), Email)
                        .putString(_vcontext.getString(R.string.pref_login_memberId), memberId)
                        .putString(_vcontext.getString(R.string.pref_main_user_data_hp), substr_mobile)
                        .putString(_vcontext.getString(R.string.pref_main_user_data_hp_iso), countryisocode)
                        .putInt(_vcontext.getString(R.string.pref_main_user_data_hp_code), Integer.parseInt(countrycode))
                        // .putString(_vcontext.getString(R.string.pref_login_Access_token), AccessToken)
                        .putBoolean(_vcontext.getString(R.string.pref_is_logged_in), true)
                        .putBoolean(_vcontext.getString(R.string.pref_is_account_verified), AccountVerified)
                        .putBoolean(_vcontext.getString(R.string.pref_update_profile_flag), true)
                        .apply();


                if (dbmedprofile.existmemberprofile(memberId) < 1) {
                    MedicalProfile member_profile = new MedicalProfile();
                    member_profile.setMemberid(memberId);
                    member_profile.setEmail(Email);
                    member_profile.setTel1(substr_mobile);
                    member_profile.setTel1_code(Integer.parseInt(countrycode));
                    member_profile.setTel1_iso(countryisocode);
                    member_profile.setMember(1);
                    member_profile.setFlag_upload(0);
                    member_profile.setRelation(1);
                    member_profile.setNric_type(-1);
                    member_profile.setGender(-1);
                    member_profile.setMarried_staus(-1);
                    if (!nric_type.equals("")) {
                        member_profile.setNric_type(Integer.parseInt(nric_type));
                    }
                    if (!nric.equals("")) {
                        member_profile.setNric(nric);
                    }
                    if (!name.equals("")) {
                        member_profile.setNric_name(name);
                    }
                    if (!dob.equals("")) {
                        member_profile.setDob(dob);
                    }
                    if (countrycode.equals("SG")) {
                        member_profile.setNationality("Singapore");
                    } else {
                        member_profile.setNationality(nationality);
                    }
                    dbmedprofile.updateMedicalProfile(member_profile);
                } else {
                    MedicalProfile medicalProfile = dbmedprofile.getMedProfilebyflag(memberId);
                    medicalProfile.setMemberid(memberId);
                    medicalProfile.setEmail(Email);
                    medicalProfile.setTel1(substr_mobile);
                    medicalProfile.setTel1_code(Integer.parseInt(countrycode));
                    medicalProfile.setTel1_iso(countryisocode);
                    medicalProfile.setMember(1);
                    medicalProfile.setRelation(1);
                    if (!nric_type.equals("")) {
                        medicalProfile.setNric_type(Integer.parseInt(nric_type));
                    }
                    if (!nric.equals("")) {
                        medicalProfile.setNric(nric);
                    }
                    if (!name.equals("")) {
                        medicalProfile.setNric_name(name);
                    }
                    if (!dob.equals("")) {
                        medicalProfile.setDob(dob);
                    }
                    if (countrycode.equals("SG")) {
                        medicalProfile.setNationality("Singapore");
                    } else {
                        medicalProfile.setNationality(nationality);
                    }
                    dbmedprofile.updateMedicalProfile(medicalProfile);
                }
            }
            callback.onSuccess("OK");

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getError(String error, VolleyCallback callback) {
        String errorcode = "", errormsg = "";
        try {
            JSONObject errorjson = new JSONObject(error);

            if (errorjson.has(Constant.NODE_DATA)) {
                JSONObject data = errorjson.getJSONObject(Constant.NODE_DATA);
                if (data.has(Constant.NODE_ErrorCode)) {
                    errorcode = data.getString(Constant.NODE_ErrorCode);
                }
                if (data.has(Constant.NODE_ErrorMessage)) {
                    errormsg = data.getString(Constant.NODE_ErrorMessage);
                }
            }
            callback.onFail(errorcode, errormsg);

        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onFail("Error", "Can't connect to Server please try again later");
        }
    }
}
