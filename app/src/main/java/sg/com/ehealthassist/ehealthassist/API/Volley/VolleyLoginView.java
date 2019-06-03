package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

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
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 6/30/2017.
 */

public class VolleyLoginView {
    private String Tag_json_obj = "_req_login_view";
    Context _vcontext;
    DBMedProfile dbmedprofile;
    SharedPreferences preferences = null;
    String nationality = "";

    public VolleyLoginView(Context mcontext,String nationality) {
        _vcontext = mcontext;
        this.nationality = nationality;
        preferences = _vcontext.getSharedPreferences(_vcontext.getString(R.string.preference_name), _vcontext.MODE_PRIVATE);
        dbmedprofile = new DBMedProfile(_vcontext);

    }
    public interface VolleyCallback {
        void onSuccess(String name,String nric,String nricType,String dob);

        void onFail(String errorcode, String errormessage);
    }

    public void GetLoginViewJsonData(final String accesstoken, JSONObject param, VolleyCallback callback) {

        String uri = String
                .format(Constant.URL_LoginView);
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                uri,param,
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

    public void setTvResultText(JSONObject response, VolleyCallback callback) {

        try {
            String status = response.getString(Constant.NODE_STATUS);
            if (status.equals(Constant.CHECK_STATUS_OK)) {


                JSONObject data = response.getJSONObject(Constant.NODE_DATA);
                String memberId = data.getString(Constant.NODE_MemberId);
                // String AccessToken = data.getString(Constant.NODE_AccessToken);
                String Email = data.getString(Constant.NODE_EMAIL);
                String Mobileno = data.getString(Constant.NODE_MobileNo);
                String countryisocode = data.getString(Constant.NODE_CountryISOCode);
                String countrycode = data.getString(Constant.NODE_CountryCode);
                String substr_mobile = Mobileno.substring(countrycode.length()).toString();
                String nric = data.getString(Constant.NODE_NRIC);
                String nric_type =data.getString(Constant.NODE_NRICTYPE);
                String  name = data.getString(Constant.NODE_ApptFullName);
                String dob = data.getString("DOB");
                boolean AccountVerified = data.getBoolean(Constant.NODE_AccountVerified);
                preferences.edit()
                        .putString(_vcontext.getString(R.string.pref_main_user_data_email), Email)
                        .putString(_vcontext.getString(R.string.pref_login_memberId), memberId)
                        .putString(_vcontext.getString(R.string.pref_main_user_data_hp), substr_mobile)
                        .putString(_vcontext.getString(R.string.pref_main_user_data_hp_iso),countryisocode)
                        .putInt(_vcontext.getString(R.string.pref_main_user_data_hp_code),Integer.parseInt(countrycode))
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
                    if(!nric_type.equals("")){
                        member_profile.setNric_type(Integer.parseInt(nric_type));
                    }
                    if(!nric.equals("")){
                        member_profile.setNric(nric);
                    }
                    if(!name.equals("")){
                        member_profile.setNric_name(name);
                    }
                    if(!dob.equals("")){
                        member_profile.setDob(dob);
                    }
                    if(countrycode.equals("SG")){
                        member_profile.setNationality(_vcontext.getResources().getString(R.string.defaultnational));
                    }
                    else{
                        member_profile.setNationality(nationality);
                    }
                    dbmedprofile.updateMedicalProfile(member_profile);
                    preferences.edit()
                            .putString(_vcontext.getString(R.string.pref_main_user_data_hp_nationality),_vcontext.getResources().getString(R.string.defaultnational)).apply();

                } else {
                    MedicalProfile medicalProfile = dbmedprofile.getMedProfilebyflag(memberId);
                    medicalProfile.setMemberid(memberId);
                    medicalProfile.setEmail(Email);
                    medicalProfile.setTel1(substr_mobile);
                    medicalProfile.setTel1_code(Integer.parseInt(countrycode));
                    medicalProfile.setTel1_iso(countryisocode);
                    medicalProfile.setMember(1);
                    medicalProfile.setRelation(1);
                    if(!nric_type.equals("")){
                        medicalProfile.setNric_type(Integer.parseInt(nric_type));
                    }
                    if(!nric.equals("")){
                        medicalProfile.setNric(nric);
                    }
                    if(!name.equals("")){
                        medicalProfile.setNric_name(name);
                    }
                    if(!dob.equals("")){
                        medicalProfile.setDob(dob);
                    }
                    if(countrycode.equals("SG")){
                        medicalProfile.setNationality(_vcontext.getResources().getString(R.string.defaultnational));
                    }
                    else{
                        medicalProfile.setNationality(nationality);
                    }
                    dbmedprofile.updateMedicalProfile(medicalProfile);
                    preferences.edit()
                            .putString(_vcontext.getString(R.string.pref_main_user_data_hp_nationality),_vcontext.getResources().getString(R.string.defaultnational)).apply();
                }

                callback.onSuccess(name , nric, nric_type,dob);
                Log.e("login response ",response.toString());
                Log.e("login view ",name + " ,"+nric + ", "+nric_type+","+dob);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

                            getError(body,statuscode,callback);

                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        };
    }

    public void getError(String error,int statuscode,  VolleyCallback callback) {
        try {
            Log.e("error",error);
            String errorcode="",errormsg = "";
            JSONObject errorjson = new JSONObject(error);
            if(statuscode==400){
                if(errorjson.has("error")){
                    errorcode = errorjson.getString("error");
                }
                if(errorjson.has("error_description")){
                    errormsg = errorjson.getString("error_description");
                }
            }else{
                if(errorjson.has(Constant.NODE_DATA)){
                    JSONObject data = errorjson.getJSONObject(Constant.NODE_DATA);
                    if(data.has(Constant.NODE_ErrorCode)){
                        errorcode = data.getString(Constant.NODE_ErrorCode);
                    }
                    if(data.has(Constant.NODE_ErrorMessage)){
                        errormsg = data.getString(Constant.NODE_ErrorMessage);
                    }
                }
            }
            callback.onFail(errorcode,errormsg);
            //ActivityLogIn.tverrormsg.setText(errormsg);
            //ActivityLogIn.tverrormsg.setVisibility(View.VISIBLE);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
