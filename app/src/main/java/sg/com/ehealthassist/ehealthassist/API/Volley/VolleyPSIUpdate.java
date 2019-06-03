package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import org.json.XML;

import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Haze.PSIHR_Record;
import sg.com.ehealthassist.ehealthassist.Models.Haze.PSIHR_Region;
import sg.com.ehealthassist.ehealthassist.Models.Haze.Reading;
import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by thazinhlaing on 1/7/17.
 */
public class VolleyPSIUpdate {
    private String Tag_json_obj = "_req_psi_request";
    Context _vcontext;
    private ProgressDialog pDialog;

    public interface VolleyCallback {
        void onSuccess(ArrayList<PSIHR_Region> listpsi);

        void onFail(String errorcode, String errormessage);
    }

    public VolleyPSIUpdate(Context mcontext) {
        this._vcontext = mcontext;
        pDialog = new ProgressDialog(_vcontext);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
    }


    public void GetPSIUpdateRequest(final VolleyCallback callback){
        showpDialog();
        StringRequest request = new StringRequest(Request.Method.GET, Constant.psi_update,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        setTvResultText(response, callback);
                    }
                }, new Response.ErrorListener() {
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
        });
        AppController.getInstance().addToRequestQueue(request, Tag_json_obj);
    }

    //============================================================
    //			Response Result
    //============================================================

    public void setTvResultText(String response, VolleyCallback callback) {
        hidepDialog();
        try {
            JSONObject json = XML.toJSONObject(response);
            ArrayList<PSIHR_Region> pmsi_list = new ArrayList<PSIHR_Region>();
            if (json.has("channel")) {
                JSONObject json_channel = json.getJSONObject("channel");
                if(json_channel.has("item")){
                    JSONObject json_item = json_channel.getJSONObject("item");
                    if(json_item.has("region")){
                        JSONArray json_region = json_item.getJSONArray("region");
                        pmsi_list = getPSIArrayList(json_region);
                    }
                }
                callback.onSuccess(pmsi_list);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            callback.onFail("error",ex.toString());
        }
    }

    public ArrayList<PSIHR_Region> getPSIArrayList(JSONArray json_arr ){
        ArrayList<PSIHR_Region> list_psi = new ArrayList<PSIHR_Region>();

        for(int i=0;i < json_arr.length();i++) {

            PSIHR_Region new_region = new PSIHR_Region();
            try {
                String id="",latitude="",longitude="",timestamp="",
                        type="",value="";
                PSIHR_Record new_record = new PSIHR_Record();
                JSONObject json_object = json_arr.getJSONObject(i);
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
                        JSONArray reading = record.getJSONArray("reading");

                        if (reading.length() > 0) {
                            Reading[] arr_reading = new Reading[reading.length()];
                            for (int r = 0; r < reading.length(); r++) {
                                Reading object = new Reading();
                                JSONObject json_readObject = reading.getJSONObject(r);
                                if(json_readObject.has("type")){
                                     type = json_readObject.getString("type");
                                }
                                if(json_readObject.has("value")){
                                     value = json_readObject.getString("value");
                                }
                                object.setType(type);
                                object.setValue(value);
                                arr_reading[r] = object;
                            }
                            Log.e("reading", arr_reading.length + "");
                            new_record.setReading(arr_reading);
                            new_record.setTimestamp(timestamp);
                        }
                    }
                }
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

}
