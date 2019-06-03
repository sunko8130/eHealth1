package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 1/7/17.
 */
public class VolleyReVerifyMember {
    private String Tag_json_obj = "_req_reverify_api";
    SharedPreferences preferences = null;
    Context _vcontext;
    String verifby = "";

    public VolleyReVerifyMember(Context _mcontext) {
        _vcontext = _mcontext;
        preferences = _vcontext.getSharedPreferences(_vcontext.getString(R.string.preference_name), _vcontext.MODE_PRIVATE);

    }
    //============================================================
    //			Get Method All Queue Register
    //============================================================

    public void PostReVerifyMemberJson(String memberid, String verifyBy) {

        this.verifby = verifyBy;
        String uri = String
                .format(Constant.URL_REVERIFYMEMBER + memberid + "/" + verifyBy
                );
        Log.e("reverfy", uri);
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
                20 * 1000,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        AppController.getInstance().addToRequestQueue(myReq, Tag_json_obj);
    }

    //============================================================
    //			Response Result
    //============================================================
    private Response.Listener<JSONObject> createMyReqSuccessListener() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                setTvResultText(response);
            }
        };
    }

    private Response.ErrorListener createMyReqErrorListener() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                String body = "";
                try {
                    if (error.networkResponse.data != null) {
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");
                            Log.e("Response Error Upload", body);
                            getError(body);

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

    public void setTvResultText(JSONObject response) {
        Log.e("reverify member", response.toString());
        if (!verifby.equals("Sms")) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(_vcontext);
            alertBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                }
            });
            alertBuilder.setMessage("Request Send, please check email, including junk.");
            alertBuilder.show();
        }

    }

    public void getError(String error) {
        try {
            JSONObject errorjson = new JSONObject(error);
            String errorcode = "",errormsg="";
            if(errorjson.has(Constant.NODE_DATA)){
                JSONObject data = errorjson.getJSONObject(Constant.NODE_DATA);
                if(data.has(Constant.NODE_ErrorCode)){
                     errorcode = data.getString(Constant.NODE_ErrorCode);
                }
                if(data.has(Constant.NODE_ErrorMessage)){
                     errormsg = data.getString(Constant.NODE_ErrorMessage);
                }
            }
            Utils.showAlertDialog(_vcontext, errorcode, errormsg);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
