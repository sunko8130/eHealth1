package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.content.Context;

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

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class VolleyLogout {
    private String Tag_json_obj = "_req_login_out";
    Context _vcontext;

    public VolleyLogout(Context mcontext) {
        _vcontext = mcontext;
    }

    public void PostLogOutJsonData(final String accesstoken,JSONObject param) {

        String uri = String
                .format(Constant.URL_Logout);
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                uri,param,
                createMyReqSuccessListener(),
                createMyReqErrorListener()) {
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
    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setTvResultText(response);
            }
        };
    }

    public void setTvResultText(JSONObject response ) {
        try{
            String status = response.getString(Constant.NODE_STATUS);
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                JSONObject data = response.getJSONObject(Constant.NODE_DATA);
                String issuccess = data.getString("IsSuccess");
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    private Response.ErrorListener createMyReqErrorListener() {
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

                            getError(body,statuscode);

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

    public void getError(String error,int statuscode) {
        try {
            String errorcode,errormsg = "";
            JSONObject errorjson = new JSONObject(error);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

