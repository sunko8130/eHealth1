package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Haze.PM2_5Record;
import sg.com.ehealthassist.ehealthassist.Models.Haze.PM2_5Region;
import sg.com.ehealthassist.ehealthassist.Models.Haze.Reading;
import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by thazinhlaing on 1/7/17.
 */
public class VolleyPM2_5Update {
    private String Tag_json_obj = "_req_psi_request";
    Context _vcontext;

    private ProgressDialog pDialog;

    public interface VolleyCallback {
        void onSuccess(ArrayList<PM2_5Region> listpm2_5);

        void onFail(String errorcode, String errormessage);
    }
    public VolleyPM2_5Update(Context mcontext) {
        this._vcontext = mcontext;
        pDialog = new ProgressDialog(_vcontext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
    }
    public void GetPM2_5UpdateRequest( VolleyCallback callback) {
        showpDialog();
        StringRequest req = new StringRequest(Request.Method.GET, Constant.pm2_5_update,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback)) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> pars = new HashMap<String, String>();
                pars.put("Content-Type", "text/xml");
                return pars;
            }

            @Override
            public String getBodyContentType() {
                return "text/xml";
            }


        };
        req.setRetryPolicy(new DefaultRetryPolicy(
                0,
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(req, Tag_json_obj);
    }

    //============================================================
    //			Response Result
    //============================================================
    private Response.Listener<String> createMyReqSuccessListener(final VolleyCallback callback) {
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                setTvResultText(response, callback);
            }
        };
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
                            getError(body, statuscode, callback);

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

    public void setTvResultText(String response, VolleyCallback callback) {
        hidepDialog();
        if (response == null) {
            //error
        } else {
            ArrayList<PM2_5Region> pm2_5_list = new ArrayList<PM2_5Region>();
            try {
                JSONObject json = XML.toJSONObject(response);
                if (json.has("channel")) {
                    JSONObject json_channel = json.getJSONObject("channel");
                    if(json_channel.has("item")){
                        JSONObject json_item = json_channel.getJSONObject("item");
                        if(json_item.has("region")){
                            JSONArray json_region = json_item.getJSONArray("region");
                            pm2_5_list = getPM2_5ArrayList(json_region);
                        }
                    }
                    callback.onSuccess(pm2_5_list);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void getError(String error, int statuscode, VolleyCallback callback) {
        hidepDialog();
        callback.onFail(statuscode+"",error);
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.dismiss();
        pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public ArrayList<PM2_5Region> getPM2_5ArrayList(JSONArray json_arr ){
        ArrayList<PM2_5Region> list_psi = new ArrayList<PM2_5Region>();
        for(int i=0;i < json_arr.length();i++) {
            PM2_5Region new_region = new PM2_5Region();
            try {
                JSONObject json_object = json_arr.getJSONObject(i);
                String id="",latitude="",longitude="",timestamp="",type="",
                        value="";
                if(json_object.has("id")){
                    id = json_object.getString("id");
                }
                if(json_object.has("latitude")){
                     latitude = json_object.getString("latitude");
                }
                if(json_object.has("longitude")){
                     longitude = json_object.getString("longitude");
                }
                if(json_object.has("record")){
                    JSONObject record = json_object.getJSONObject("record");
                    if(record.has("timestamp")){
                         timestamp = record.getString("timestamp");
                    }
                    if(record.has("reading")){
                        JSONObject reading = record.getJSONObject("reading");
                        if(reading.has("type")){
                             type = reading.getString("type");
                        }
                        if(reading.has("value")){
                             value = reading.getString("value");
                        }
                    }
                }

                Reading arr_reading = new Reading();
                arr_reading.setType(type);
                arr_reading.setValue(value);

                PM2_5Record new_record = new PM2_5Record();
                new_record.setReading(arr_reading);
                new_record.setTimestamp(timestamp);

                new_region.setId(id);
                new_region.setLatitude(latitude);
                new_region.setLngitude(longitude);
                new_region.setRecord(new_record);
                list_psi.add(new_region);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return  list_psi;
    }
}
