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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by User on 7/20/2017.
 */

public class VolleyGetallMemberListingProfile {
    private String Tag_json_obj = "_req_all_memberprofile";
    Context _vcontext;
    public DBMedProfile dbmedprofile;
    public  ArrayList<MedicalProfile> lstmed_profile = new ArrayList<MedicalProfile>();
    String  login_memberid,mobile,isocode,nationality="";int countrycode;


    public interface VolleyCallback {
        void onSuccess(String message);

        void onFail(String errorcode, String errormessage);
    }

    public VolleyGetallMemberListingProfile(Context mcontext,String loginmemberid,String mobile,String isocode,int countrycode,String nationality) {
        this._vcontext = mcontext;
        dbmedprofile = new DBMedProfile(_vcontext);
        login_memberid = loginmemberid;
        this.mobile = mobile;
        this.isocode = isocode;
        this.nationality = nationality;
        this.countrycode = countrycode;
        lstmed_profile  = dbmedprofile.getMedProfilebyMemberid(loginmemberid);
    }

    public void GetallMemberprofileRequest(final String accesstoken, VolleyCallback callback) {

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,
                Constant.URL_viewmemberprofile,
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

                setvolleyResultText(response, callback);
            }
        };
    }

    public void setvolleyResultText(JSONObject resultjson, VolleyCallback callback){
        try {
            ArrayList<MedicalProfile> json_medlist = new ArrayList<MedicalProfile>();
            String status = resultjson.getString(Constant.NODE_STATUS);

            if (status.equals(Constant.CHECK_STATUS_OK)) {
                JSONArray dataarray = resultjson.getJSONArray(Constant.NODE_DATA);

                for(int i =0;i<dataarray.length();i++){
                    JSONObject jobject = dataarray.getJSONObject(i);
                    String id = "",memberid="",name="",nric="",dob="",mainaccount="";
                    int nric_type =-1;
                    if(jobject.has("ID")){
                         id = jobject.getString("ID");
                    }
                    if(jobject.has("MemberID")){
                         memberid = jobject.getString("MemberID");
                    }
                    if(jobject.has(Constant.NODE_NAME)){
                         name = jobject.getString(Constant.NODE_NAME);
                    }
                    if(jobject.has(Constant.NODE_NRIC)){
                         nric = jobject.getString(Constant.NODE_NRIC);
                    }
                    if(jobject.has(Constant.NODE_NRICTYPE)){
                         nric_type = Integer.parseInt(jobject.getString(Constant.NODE_NRICTYPE)) ;
                    }
                    if(jobject.has("DOB")){
                         dob = jobject.getString("DOB");
                    }
                    if(jobject.has("MainAccount")){
                         mainaccount = jobject.getString("MainAccount");
                    }

                    MedicalProfile medobj = new MedicalProfile();
                    medobj.setMemberid(memberid);
                    medobj.setNric_name(name);
                    medobj.setNric(nric);
                    medobj.setNric_type(nric_type);
                    medobj.setDob(dob);
                  //  medobj.setTel1(mobile);
                  //  medobj.setTel1_iso(isocode);
                  //  medobj.setTel1_code(countrycode);
                    medobj.setNationality(nationality);
                    if(mainaccount.equals("Y")){
                        medobj.setMember(1);
                    }else{
                        medobj.setMember(0);
                    }

                    json_medlist.add(medobj);

                }
            }
            if(json_medlist.size()>0){
                mappingProfile(json_medlist,lstmed_profile);
            }

            callback.onSuccess(status);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void mappingProfile(ArrayList<MedicalProfile> jsonlist,ArrayList<MedicalProfile> localprofilelist ){
            int flag =0;
            ArrayList<MedicalProfile> tempprofile = new ArrayList<>();
            for(int i =0 ;i<jsonlist.size();i++){
                for(int ii=0;ii<localprofilelist.size();ii++){
                    String jsonnric = jsonlist.get(i).getNric();
                    int jsonnrictype = jsonlist.get(i).getNric_type();
                    String jsondob = jsonlist.get(i).getDob();
                    String localnric = localprofilelist.get(ii).getNric();
                    int localnrictype = localprofilelist.get(ii).getNric_type();
                    String localdob = localprofilelist.get(ii).getDob();
                    int localmember = localprofilelist.get(ii).getMember();
                    if(jsonnric.equals(localnric) && jsonnrictype== localnrictype && jsondob.equals(localdob)){
                        // nothing do
                        flag = 1;
                        tempprofile.add(localprofilelist.get(ii));
                        break;
                    }
                    else{
                        // insert new one
                       flag =0;
                    }
                }
                if(flag >0){
                    // nothing do
                }
                else{
                    tempprofile.add(jsonlist.get(i));
                    //insert new one
                }
            }
            syncbackTolocal(tempprofile);
    }

    public void syncbackTolocal(ArrayList<MedicalProfile> syncprofile){
        if(lstmed_profile.size()>0){
            dbmedprofile.deleteprofileNotMember(login_memberid);
        }
        if(syncprofile.size()>0){
            for(int i =0;i<syncprofile.size();i++){
                dbmedprofile.updateMedicalProfile(syncprofile.get(i));
            }
        }

    }
    //============================================================
    //			Response Error
    //============================================================
    private Response.ErrorListener createMyReqErrorListener(final VolleyCallback callback) {
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
