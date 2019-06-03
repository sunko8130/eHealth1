package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sg.com.ehealthassist.ehealthassist.Messages.Messages;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by User on 9/27/2017.
 */

public class VolleyGetMessages {

    private String Tag_json_obj = "_req_getmessages";
    Context _vcontext;
    private ProgressDialog pDialog;

    public interface VolleyCallback {
        void onSuccess(String message,ArrayList<Messages> mlist);

        void onFail(String errorcode, String errormessage);
    }
    public VolleyGetMessages(Context context) {
        _vcontext = context;
        pDialog = new ProgressDialog(_vcontext);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
    }

    public void GetMessageRequest(final String accesstoken, VolleyCallback callback) {
        showpDialog();
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,
                Constant.URL_GetMessage,
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
        hidepDialog();
        ArrayList<Messages> messlist = new ArrayList<Messages>();
        try {
            if (resultjson.has("Status")){
                String status = resultjson.getString("Status");

                if(status.equals("Ok")){
                    if (resultjson.has(Constant.NODE_DATA)){
                        JSONArray jsonarr = resultjson.getJSONArray(Constant.NODE_DATA);

                        if(jsonarr.length()>0){
                            for(int i =0;i<jsonarr.length();i++){
                                int id=0,messagestatus=0;long clinicid =0;long memberid=0;long mobileno= 0;
                                String nric="",nrictype="",dob="",message="",createdate="",
                                        downloaded="",patientname="",messageReply="";
                                JSONObject obj = jsonarr.getJSONObject(i);
                                if(obj.has("ID")){
                                    id = obj.getInt("ID");
                                }
                                if(obj.has("ClinicID")){
                                    clinicid = obj.getLong("ClinicID");
                                }
                                if(obj.has("MemberID")){
                                    memberid = obj.getLong("MemberID");
                                }
                                if(obj.has("Nric")){
                                    nric = obj.getString("Nric");
                                }
                                if(obj.has("NricType")){
                                    nrictype = obj.getString("NricType");
                                }
                                if(obj.has("DOB")){
                                    dob = obj.getString("DOB");
                                }
                                if(obj.has("Message")){
                                    message = obj.getString("Message");
                                }
                                if(obj.has("CreateDate")){
                                    createdate = obj.getString("CreateDate");
                                }
                                if(obj.has("Status")){
                                    messagestatus = obj.getInt("Status");
                                }
                                if(obj.has("Downloaded")){
                                    downloaded = obj.getString("Downloaded");
                                }
                                if(obj.has("PatientName")){
                                    patientname = obj.getString("PatientName");
                                }
                                if(obj.has("MessageReply")){
                                    messageReply= obj.getString("MessageReply");
                                }if(obj.has("Mobile")){
                                    mobileno = obj.getLong("Mobile");
                                }
                                Messages mesobj = new Messages(id,clinicid,memberid,nric,nrictype,dob,message,createdate,messagestatus,messageReply,downloaded,patientname,mobileno);
                                messlist.add(mesobj);
                            }
                        }
                    }
                }
                callback.onSuccess("Ok",messlist);

            }

        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onSuccess("Ok",messlist);
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
        hidepDialog();
        String errorcode="",errormsg="";
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
            callback.onFail(errorcode, errormsg);

        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onFail(errorcode, errormsg);
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
