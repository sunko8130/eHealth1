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
import java.util.HashMap;
import java.util.Map;

import sg.com.ehealthassist.ehealthassist.DB.DBPublicHoliday;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.PHDay.PublicHoliday;
import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by User on 6/29/2017.
 */

public class VolleyPublicHoliday {
    private String Tag_json_obj = "_req_holiday";
    Context _vcontext;
    DBPublicHoliday dbdecoration;


    public VolleyPublicHoliday(Context _mcontext) {
        _vcontext = _mcontext;
        dbdecoration = new DBPublicHoliday(_mcontext);
    }

    public interface VolleyCallback {
        void onSuccess(String pubholidaylists);

        void onFail(String errorcode, String errormessage);
    }

    public void GetPublicholidayJsonData(VolleyCallback callback) {

        String uri = String
                .format(Constant.URL_PUBLICHOLIDAY);
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

        try {
            String status = response.getString(Constant.NODE_STATUS);

            if (status.equals(Constant.CHECK_STATUS_OK)) {
                if (response.has(Constant.NODE_DATA)) {
                    JSONArray response_data = response.getJSONArray(Constant.NODE_DATA);

                    if (response_data.length() > 0) {
                        for (int i = 0; i < response_data.length(); i++) {
                            JSONObject dataobject = response_data.getJSONObject(i);
                            PublicHoliday holiday = GetPublicholidayObject(dataobject);
                            dbdecoration.addPublicLog(holiday);
                        }
                    }
                }
            }
            callback.onSuccess("Ok");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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

    public void getError(String error, VolleyCallback callback) {
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

            callback.onFail(errorcode, errormsg);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public PublicHoliday GetPublicholidayObject(JSONObject JSONDOC) {
        PublicHoliday new_public = new PublicHoliday();
        try {
            if (JSONDOC.has(Constant.NODE_HolidayDesc)) {
                new_public.setHoliday_description(JSONDOC.getString(Constant.NODE_HolidayDesc));
            }
            if (JSONDOC.has(Constant.NODE_HolidayDate)) {
                new_public.setHoliday_date(JSONDOC.getString(Constant.NODE_HolidayDate));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new_public;
    }
}
