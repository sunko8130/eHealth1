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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Other.AppController;
import sg.com.ehealthassist.ehealthassist.Other.Utils;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class VolleyBookApptSlot {
    private String Tag_json_obj = "_req_book_api";
    Context _vcontext;
    private ProgressDialog pDialog;
    String bookid, longid, status, status_desc = "";

    public interface VolleyCallback {
        void onSuccess(String bookid, String longbookid, String status, String statusdescription);

        void onFail(String errorcode, String errormsg);
    }

    public VolleyBookApptSlot(Context _mcontext) {
        _vcontext = _mcontext;

        pDialog = new ProgressDialog(_vcontext);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
    }

    public void GetListApptSlotJson(final String accesstoken, JSONObject param, VolleyCallback callback) {
        showpDialog();
        Log.e("rqbookslotjson", param.toString());
        String uri = String
                .format(Constant.URL_LISTBookSlot);
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                uri, param,
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

    //============================================================
    //			Response Result
    //============================================================
    private Response.Listener<JSONObject> createMyReqSuccessListener(final VolleyCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("List Appt Slot", response.toString());
                setTvResultText(response, callback);
            }
        };
    }

    public void setTvResultText(JSONObject response, VolleyCallback callback) {
        hidepDialog();
        try {
            String status = response.getString(Constant.NODE_STATUS);
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                JSONObject data = response.getJSONObject(Constant.NODE_DATA);
                if (data.has(Constant.NODE_IsSuccess)) {
                    if (data.getBoolean(Constant.NODE_IsSuccess)) {
                        if (data.has(Constant.NODE_LongBookId)) {
                            longid = data.getString(Constant.NODE_LongBookId);
                        }
                        if (data.has(Constant.NODE_ShortBookId)) {
                            bookid = data.getString(Constant.NODE_ShortBookId);
                        }
                        if (data.has("Message")) {
                            status = data.getString("Message");
                        }
                        if (data.has("MessageStatus")) {
                            status_desc = data.getString("MessageStatus");
                        }
                    }
                }
            }
            callback.onSuccess(bookid, longid, status, status_desc);
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
                    Utils.showAlertDialog(_vcontext, "Error", ex.getMessage());
                }
            }
        };
    }

    public void getError(String error, VolleyCallback callback) {
        hidepDialog();
        try {
            JSONObject errorjson = new JSONObject(error);
            String errorcode = "", errormsg = "";
            if (errorjson.has(Constant.NODE_DATA)) {
                JSONObject data = errorjson.getJSONObject(Constant.NODE_DATA);
                if (data.has(Constant.NODE_ErrorCode)) {
                    errorcode = data.getString(Constant.NODE_ErrorCode);
                }
                if (data.has(Constant.NODE_ErrorMessage)) {
                    errormsg = data.getString(Constant.NODE_ErrorMessage);
                }
            }
            callback.onFail(errorcode, errormsg);
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.showAlertDialog(_vcontext, "Api Error", "Server Response Error");
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

