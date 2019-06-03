package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.content.Context;
import android.content.Intent;
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

import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by thazinhlaing on 1/7/17.
 */
public class VolleyVerifyMember {
    private String Tag_json_obj = "_req_sms_request";
    Context _vcontext;
    String verifyby="";

    public VolleyVerifyMember(Context mcontext){
        _vcontext = mcontext;
    }

    public void GetSMSRequest( String memberid, String verifyby){
        this.verifyby = verifyby;
        String uri = String
                .format(Constant.URL_VERIFYMEMBER + memberid+"/"+ verifyby);

        Log.e("rqverifyjson",uri);
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                uri,
                createMyReqSuccessListener(),
                createMyReqErrorListener()) {

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
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(myReq,Tag_json_obj);
    }

    //============================================================
    //			Response Result
    //============================================================
    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("sms log",response.toString());
                setTvResultText(response);
            }
        };
    }
    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body = "";
                try{
                    if (error.networkResponse.data != null) {
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            Log.e("Sms Error Upload", body);
                            getError(body);

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

    public void setTvResultText(JSONObject response) {
        if(this.verifyby.equals("Email")){
            Intent intent = new Intent(_vcontext, ActivityHome1.class);
            _vcontext.startActivity(intent);
        }
    }

    public void getError(String error) {
        try {
            Log.e("Error mess:",error);
            JSONObject errorjson=new JSONObject(error);
            JSONObject data = errorjson.getJSONObject(Constant.NODE_DATA);
            String errorcode = data.getString(Constant.NODE_ErrorCode);
            String errormsg = data.getString(Constant.NODE_ErrorMessage);

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

}
