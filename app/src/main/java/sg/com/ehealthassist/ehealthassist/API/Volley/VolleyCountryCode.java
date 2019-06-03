package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.content.Context;
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
import java.util.Map;

import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Profile.CountryCode;
import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by User on 6/30/2017.
 */

public class VolleyCountryCode {
    private String Tag_json_obj = "_req_countrycode";
    Context _vcontext;

    public VolleyCountryCode(Context _mcontext) {
        _vcontext = _mcontext;

    }
    public interface VolleyCallback {
        void onSuccess(ArrayList<CountryCode> success);

        void onFail(String errorcode, String errormessage);
    }


    public void GetCountryCodeJsonData(VolleyCallback callback) {

        String uri = String
                .format(Constant.URL_CountryCode);
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,
                uri,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback)) {
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
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myReq, Tag_json_obj);
    }

    private Response.Listener<JSONObject> createMyReqSuccessListener(final VolleyCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setTvResultText(response, callback);
            }
        };
    }

    public void setTvResultText(JSONObject response, VolleyCallback callback) {
        ArrayList<CountryCode> list_countrycode = new ArrayList<CountryCode>();
        try {
            String status = response.getString(Constant.NODE_STATUS);
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                JSONArray response_data = response.getJSONArray(Constant.NODE_DATA);
                if (response_data.length() > 0) {
                    for (int i = 0; i < response_data.length(); i++) {
                        JSONObject dataobject = response_data.getJSONObject(i);

                        CountryCode countryCode = GetCountryCodeInfoObject(dataobject);
                        list_countrycode.add(countryCode);
                    }
                }
            }
            callback.onSuccess(list_countrycode);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public CountryCode GetCountryCodeInfoObject(JSONObject json) {

        CountryCode new_countrycode = new CountryCode();
        try {
            if (json.has("CountryName")) {
                new_countrycode.setCountryName(json.getString("CountryName"));
            }
            if (json.has("CountryCode")) {
                new_countrycode.setCountryCode(json.getInt("CountryCode"));
            }
            if (json.has("ISOCode")) {
                new_countrycode.setISOCode(json.getString("ISOCode"));
            }
            if (json.has("Active")) {
                new_countrycode.setActive(json.getBoolean("Active"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new_countrycode;
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

    public void getError(String error, VolleyCallback callback) {
        String errorcode ="",errormsg="";
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

}
