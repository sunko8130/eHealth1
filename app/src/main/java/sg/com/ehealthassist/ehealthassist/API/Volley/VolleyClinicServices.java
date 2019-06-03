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

import sg.com.ehealthassist.ehealthassist.DB.DBService_items;
import sg.com.ehealthassist.ehealthassist.DB.DBServices;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Services.Services;
import sg.com.ehealthassist.ehealthassist.Models.Services.Services_Items;
import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by User on 6/29/2017.
 */

public class VolleyClinicServices {
    private String Tag_json_obj = "_clinic_services";
    Context _vcontext;
    DBServices db_services;
    DBService_items db_service_items;

    public VolleyClinicServices(Context _mcontext) {
        _vcontext = _mcontext;
        db_services = new DBServices(_vcontext);
        db_service_items = new DBService_items(_vcontext);

    }
    //============================================================
    //			Get Method Clinic Services
    //============================================================

    public void GetClinicServicesJson() {

        String uri = String
                .format(Constant.URL_Clinic_Services);

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,
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

        try {
            String status = response.getString(Constant.NODE_STATUS);
            int id =-1;String clinictype="";String specialty = "";
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                JSONArray data_json = response.getJSONArray(Constant.NODE_DATA);
                for (int i = 0; i < data_json.length(); i++) {
                    JSONObject json_service = data_json.getJSONObject(i);
                    if(json_service.has("Id")){
                         id = json_service.getInt("Id");
                    }
                    if(json_service.has("ClinicType")){
                         clinictype = json_service.getString("ClinicType");
                    }
                    if(json_service.has("Specialty")){
                         specialty = json_service.getString("Specialty");
                    }
                    if(json_service.has("Services")){
                        JSONArray json_services_item = json_service.getJSONArray("Services");
                        Services obj_services = new Services(id, clinictype, specialty);
                        db_services.addServices(obj_services);
                        for (int j = 0; j < json_services_item.length(); j++) {
                            String service_item = json_services_item.getString(j);
                            Services_Items obj_service_item = new Services_Items(id, service_item);
                            db_service_items.addServices_item(obj_service_item);
                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void getError(String error) {
        try {
            JSONObject errorjson = new JSONObject(error);
            String errorcode = "",errormsg ="";
            if(errorjson.has(Constant.NODE_DATA)){
                JSONObject data = errorjson.getJSONObject(Constant.NODE_DATA);
                if(data.has(Constant.NODE_ErrorCode)){
                     errorcode = data.getString(Constant.NODE_ErrorCode);
                }
                if(data.has(Constant.NODE_ErrorMessage)){
                     errormsg = data.getString(Constant.NODE_ErrorMessage);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
