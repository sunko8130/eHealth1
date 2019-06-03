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

import sg.com.ehealthassist.ehealthassist.DB.DBBookInfo;
import sg.com.ehealthassist.ehealthassist.Models.Appointment.BookInfo;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class VolleyAppointmentView {
    private String Tag_json_obj = "_req_appt_view";
    Context _vcontext;
    private ProgressDialog pDialog;
    DBBookInfo dbHandler;

    public VolleyAppointmentView(Context _mcontext) {
        _vcontext = _mcontext;
        pDialog = new ProgressDialog(_vcontext);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
        dbHandler = new DBBookInfo(_vcontext);
        dbHandler.deleteAllBooks();
    }

    public interface VolleyCallback {
        void onSuccess(List<BookInfo> lstbook);

        void onFail(String errorcode, String message);
    }

    public void GetApptViewJson(final String accesstoken, VolleyCallback callback) {
        showpDialog();
        String uri = String
                .format(Constant.URL_VIEW);
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
        List<BookInfo> list_book = new ArrayList<BookInfo>();
        try {
            String status = response.getString(Constant.NODE_STATUS);
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                JSONArray response_data = response.getJSONArray(Constant.NODE_DATA);
                if (response_data.length() > 0) {
                    for (int i = 0; i < response_data.length(); i++) {
                        JSONObject dataobject = response_data.getJSONObject(i);
                        if(dataobject.has("detail")){
                            JSONObject json_detail = dataobject.getJSONObject("detail");
                            BookInfo book = GetBookInfoObject(json_detail);
                            if (dbHandler.updatebookInfos(book) > 0) {
                                list_book.add(book);
                            }
                        }
                    }
                }
                callback.onSuccess(list_book);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public BookInfo GetBookInfoObject(JSONObject json) {

        BookInfo new_bookinfo = new BookInfo();
        try {
            if (json.has(Constant.NODE_LongBookId)) {
                new_bookinfo.setLong_id(json.getString(Constant.NODE_LongBookId));
            }
            if (json.has(Constant.NODE_ShortBookId)) {
                new_bookinfo.setShort_id(json.getString(Constant.NODE_ShortBookId));
            }
            if (json.has(Constant.NODE_ApptClinicId)) {
                new_bookinfo.setClinic_id(json.getInt(Constant.NODE_ApptClinicId));
            }
            if (json.has(Constant.NODE_ClinicName)) {
                new_bookinfo.setClinic_name(json.getString(Constant.NODE_ClinicName));
            }
            if (json.has(Constant.NODE_DoctorId)) {
                new_bookinfo.setDoctor_id(json.getInt(Constant.NODE_DoctorId));
            }
            if (json.has(Constant.NODE_DoctorDisplayName)) {
                new_bookinfo.setDoctor_name(json.getString(Constant.NODE_DoctorDisplayName));
            }
            if (json.has(Constant.NODE_BookingTime)) {
                new_bookinfo.setBook_date(json.getString(Constant.NODE_BookingTime));
            }
            if (json.has(Constant.NODE_Remarks)) {
                new_bookinfo.setDoc_remark(json.getString(Constant.NODE_Remarks));
            }
            if (json.has(Constant.NODE_RequestorNric)) {
                new_bookinfo.setRequestor_nric(json.getString(Constant.NODE_RequestorNric));
            }
            if (json.has(Constant.NODE_RequestorName)) {
                new_bookinfo.setRequestor_name(json.getString(Constant.NODE_RequestorName));
            }
            if (json.has(Constant.NODE_ApptStatus)) {
                new_bookinfo.setApp_status(json.getString(Constant.NODE_ApptStatus));
            }
            if (json.has(Constant.NODE_RequestorNricType)) {
                new_bookinfo.setRequestor_nrictype(json.getString(Constant.NODE_RequestorNricType));
            }
            if (json.has("MemberId")) {
                new_bookinfo.setMemberid(json.getString("MemberId"));
            }
            if (json.has("RequestorPhone")) {
                new_bookinfo.setRequestorphone(json.getString("RequestorPhone"));
            }
            if (json.has("RequestorMessage")) {
                new_bookinfo.setRequestorMessage(json.getString("RequestorMessage"));
            }
            if (json.has("StatusDescription")) {
                new_bookinfo.setApp_status_description(json.getString("StatusDescription"));
            }
            String session = "";
            if (json.has("Session")) {
                session = json.getString("Session");
                new_bookinfo.setApptsession(session);
            }
            if (json.has("ApptTiming")) {
                new_bookinfo.setApptTime(json.getString("ApptTiming"));
            }
            if (json.has("ApptSession")) {
                JSONArray jsonarr = json.getJSONArray("ApptSession");
                for (int i = 0; i < jsonarr.length(); i++) {
                    JSONObject json_sessiontime = jsonarr.getJSONObject(i);
                    if(json_sessiontime.has("Session")){
                        String j_session = json_sessiontime.getString("Session");
                        if (j_session.equals(session)) {
                            if(json_sessiontime.has("SessionTime")){
                                String j_time = json_sessiontime.getString("SessionTime");
                                new_bookinfo.setApptsessiontime(j_time);
                                break;
                            }
                        }
                    }
                }
            }
            if (json.has("PatientRemark")) {
                new_bookinfo.setPatientremark(json.getString("PatientRemark"));
            }
            if(json.has("ApptType")){
                new_bookinfo.setAppt_type(json.getString("ApptType"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new_bookinfo;
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
            String errorcode = "",errormsg = "";
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
}

