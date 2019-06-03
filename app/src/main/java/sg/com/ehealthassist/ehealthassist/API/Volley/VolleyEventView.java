package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.app.ProgressDialog;
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

import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Event.EventsView;
import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by thazinhlaing on 1/7/17.
 */
public class VolleyEventView {
    private String Tag_json_obj = "_req_events_view";
    Context _vcontext;
    private ProgressDialog pDialog;

    public VolleyEventView(Context mcontext) {
        _vcontext = mcontext;
        pDialog = new ProgressDialog(_vcontext);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
    }

    public interface VolleyCallback {
        void onSuccess(List<EventsView> eventslist);

        void onFail(String errorcode, String errormessage);
    }

    public void GetEvenJsonData(JSONObject param ,VolleyCallback callback) {
        showpDialog();
        String uri = String
                .format(Constant.URL_EventView);
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                uri,param,
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
        hidepDialog();
        List<EventsView> list_events = new ArrayList<EventsView>();
        Log.e("eventlist",response.toString());
        try {
            String status = response.getString(Constant.NODE_STATUS);
            if (status.equals("OK")) {
                JSONArray response_data = response.getJSONArray(Constant.NODE_DATA);
                if (response_data.length() > 0) {
                    for (int i = 0; i < response_data.length(); i++) {
                        JSONObject dataobject = response_data.getJSONObject(i);
                        //JSONObject json_detail = dataobject.getJSONObject("detail");
                        EventsView book = GetEvenInfoObject(dataobject);
                        list_events.add(book);
                    }
                }
            }
            callback.onSuccess(list_events);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public EventsView GetEvenInfoObject(JSONObject json) {

        EventsView new_eventinfo = new EventsView();
        try {
            if (json.has(Constant.NODE_EventId)) {
                new_eventinfo.setEventId(json.getInt(Constant.NODE_EventId));
            }
            if (json.has(Constant.NODE_EventName)) {
                new_eventinfo.setEventName(json.getString(Constant.NODE_EventName));
            }
            if (json.has(Constant.NODE_EventDesc)) {
                new_eventinfo.setEventDesc(json.getString(Constant.NODE_EventDesc));
            }
            if (json.has(Constant.NODE_FromDate)) {
                new_eventinfo.setFromDate(json.getString(Constant.NODE_FromDate));
            }
            if (json.has(Constant.NODE_ToDate)) {
                new_eventinfo.setToDate(json.getString(Constant.NODE_ToDate));
            }
            if (json.has(Constant.NODE_FromTime)) {
                new_eventinfo.setFromTime(json.getString(Constant.NODE_FromTime));
            }
            if (json.has(Constant.NODE_ToTime)) {
                new_eventinfo.setToTime(json.getString(Constant.NODE_ToTime));
            }
            if (json.has(Constant.NODE_Venue)) {
                new_eventinfo.setVenue(json.getString(Constant.NODE_Venue));
            }
            if (json.has(Constant.NODE_ContactNo)) {
                new_eventinfo.setContactNo(json.getString(Constant.NODE_ContactNo));
            }
            if (json.has(Constant.NODE_EMAIL)) {
                new_eventinfo.setEmail(json.getString(Constant.NODE_EMAIL));
            }
            if (json.has(Constant.NODE_CreatedBy)) {
                new_eventinfo.setCreatedBy(json.getString(Constant.NODE_CreatedBy));
            }
            if (json.has(Constant.NODE_CreatedDate)) {
                new_eventinfo.setCreatedDate(json.getString(Constant.NODE_CreatedDate));
            }
            if(json.has("Status")){
                new_eventinfo.setStatus(json.getString("Status"));
            }
            if(json.has("RegisterEndDate")){
                new_eventinfo.setRegisterEndDate(json.getString("RegisterEndDate"));
            }
            if(json.has("MaxPeopleReg")){
                new_eventinfo.setMaxpeoplereg(json.getInt("MaxPeopleReg"));
            }
            if(json.has("isRegister")){
                new_eventinfo.setIsRegister(json.getString("isRegister"));
            }
            if(json.has("isMemberRegister")){
                new_eventinfo.setIsMemberRegister(json.getString("isMemberRegister"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new_eventinfo;
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
