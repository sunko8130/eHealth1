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

import sg.com.ehealthassist.ehealthassist.DB.DBQueueRequest;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Queue.QueuelistView;
import sg.com.ehealthassist.ehealthassist.Models.Queue.RequestQueueLog;
import sg.com.ehealthassist.ehealthassist.Other.AppController;
import sg.com.ehealthassist.ehealthassist.Other.Utils;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class VolleyQueueView {
    private String Tag_json_obj = "_req_queue_view_list";
    Context _vcontext;
    private ProgressDialog pDialog;
    DBQueueRequest dbqueulog = null;


    public VolleyQueueView(Context mcontext) {
        _vcontext = mcontext;
        dbqueulog = new DBQueueRequest(_vcontext);
        pDialog = new ProgressDialog(_vcontext);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
    }

    public interface VolleyCallback {
        void onSuccess(List<QueuelistView> eventslist);

        void onFail(String errorcode, String errormessage);
    }

    public void GetQueueViewJsonData(final String accesstoken, VolleyCallback callback) {
        showpDialog();
        String uri = String
                .format(Constant.URL_QUEUEVIEW);
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,
                uri,
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
        List<QueuelistView> list_events = new ArrayList<QueuelistView>();
        try {
            String status = response.getString(Constant.NODE_STATUS);

            if (status.equals(Constant.CHECK_STATUS_OK)) {
                if (response.has(Constant.NODE_DATA)) {
                    JSONArray response_data = response.getJSONArray(Constant.NODE_DATA);
                    if (response_data.length() > 0) {
                        for (int i = 0; i < response_data.length(); i++) {
                            JSONObject dataobject = response_data.getJSONObject(i);
                            QueuelistView book = GetQueueViewObject(dataobject);
                            list_events.add(book);
                        }
                    }
                }
            }
            callback.onSuccess(list_events);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public QueuelistView GetQueueViewObject(JSONObject json) {

        QueuelistView new_eventinfo = new QueuelistView();
        try {
            if (json.has(Constant.NODE_STATUSCODE)) {
                new_eventinfo.setStatuscode(json.getInt(Constant.NODE_STATUSCODE));
            }
            if (json.has(Constant.NODE_RejectReason)) {
                new_eventinfo.setRejectreason(json.getString(Constant.NODE_RejectReason));
            }
            if (json.has(Constant.NODE_RequestId)) {
                String requestid = json.getString(Constant.NODE_RequestId);
                new_eventinfo.setRequestid(requestid);
            }

            if (json.has(Constant.NODE_PATIENTNRIC)) {
                new_eventinfo.setPatientnric(json.getString(Constant.NODE_PATIENTNRIC));
            }
            if (json.has(Constant.NODE_PATIENTDOB)) {
                String node_dob = json.getString(Constant.NODE_PATIENTDOB);
                String patientdob = Utils.BookDateFormat(node_dob, "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
                new_eventinfo.setPatientdob(patientdob);
            }
            if (json.has(Constant.NODE_REQUESTDATETIME)) {
                String node_requestid = json.getString(Constant.NODE_REQUESTDATETIME);

                String requestid = Utils.BookDateFormat(node_requestid, "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy hh:mm a");
                new_eventinfo.setRequestdatetime(requestid);
            }
            if (json.has(Constant.NODE_QMessage)) {
                new_eventinfo.setQmessage(json.getString(Constant.NODE_QMessage));
            }
            if (json.has(Constant.NODE_ApptClinicId)) {
                new_eventinfo.setClinicid(json.getInt(Constant.NODE_ApptClinicId));
            }
            if (json.has(Constant.NODE_ClinicName)) {
                new_eventinfo.setClinicname(json.getString(Constant.NODE_ClinicName));
            }
            if (json.has(Constant.NODE_QUEUENO)) {
                new_eventinfo.setQueueno(json.getString(Constant.NODE_QUEUENO));
            }
            if (json.has(Constant.NODE_CURRENTQUEUE)) {
                new_eventinfo.setCurrentqueue(json.getString(Constant.NODE_CURRENTQUEUE));
            }
            if (json.has(Constant.NODE_QSTATUS)) {
                new_eventinfo.setQstatus(json.getString(Constant.NODE_QSTATUS));
            }
            if (json.has(Constant.NODE_NRICTYPE)) {
                new_eventinfo.setPatientnric_type(Integer.parseInt(json.getString(Constant.NODE_NRICTYPE)));
            }
            if(json.has(Constant.NODE_PATIENTNAME)){
                new_eventinfo.setPatientname(json.getString(Constant.NODE_PATIENTNAME));
            }else{
                if (!new_eventinfo.getRequestid().equals("")) {
                    new_eventinfo.setPatientname(getpatientname(new_eventinfo.getRequestid()));
                } else {
                    new_eventinfo.setPatientname("");
                }
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
        hidepDialog();
        try {
            JSONObject errorjson = new JSONObject(error);
            String errorcode="",errormsg="";
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

    private void showpDialog() {
        if (!pDialog.isShowing()) {
            pDialog.show();
        }
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public String getpatientname(String requestid) {
        String name = "";
        RequestQueueLog requestlog = dbqueulog.getResponseQueue(requestid);
        if (!requestlog.equals(null)) {
            name = requestlog.getRequest_name();
        }
        Log.e("name", name);
        return name;
    }

}

