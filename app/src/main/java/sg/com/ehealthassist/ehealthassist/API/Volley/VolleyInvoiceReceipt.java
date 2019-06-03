package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.app.ProgressDialog;
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
import java.util.Iterator;
import java.util.Map;

import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Payment.InvoiceReceipt;
import sg.com.ehealthassist.ehealthassist.Models.Payment.PaymentType;
import sg.com.ehealthassist.ehealthassist.Models.Payment.Payment_Receipt;
import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class VolleyInvoiceReceipt {
    private String Tag_json_obj = "_req_invoice_receipt";
    Context _vcontext;
    private ProgressDialog pDialog;
    DBClinicInfo dbclinicinfo;

    public VolleyInvoiceReceipt(Context _mcontext) {
        _vcontext = _mcontext;
        dbclinicinfo = new DBClinicInfo(_vcontext);
        pDialog = new ProgressDialog(_vcontext);
        pDialog.setMessage("Loading...");
        pDialog.setCancelable(false);
    }

    public interface VolleyCallback {
        void onSuccess(ArrayList<InvoiceReceipt> message, ArrayList<String> lstname);

        void onFail(String errorcode, String errormessage);
    }

    //===========================================================
    //			Get Method Payment list
    //===========================================================


    public void GetPaymentInvoiceJson(final String usertoken,JSONObject param, VolleyCallback callback) {
        showpDialog();
        String uri = String
                .format(Constant.URL_PaymentInvoice);
        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.POST,
                uri, param,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback)) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "APPLICATION/json; charset=utf-8");
                headers.put("Authorization", usertoken);
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

    public void setTvResultText(JSONObject response, VolleyCallback callback) {
        hidepDialog();
        ArrayList<InvoiceReceipt> list_invoice = new ArrayList<InvoiceReceipt>();
        ArrayList<String> lst_name = new ArrayList<String>();
        try {
            String status = response.getString(Constant.NODE_STATUS);
            if (status.equals(Constant.CHECK_STATUS_OK)) {
                JSONArray data = response.getJSONArray(Constant.NODE_DATA);

                if (data.length() > 0) {
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject pay_json = data.getJSONObject(i);
                        InvoiceReceipt get_object = GetPaymentObject(pay_json);
                        String duplicatenric = get_object.getNric_name() + " (" + get_object.getNric() + ")";
                        if (!lst_name.contains(duplicatenric)) {
                            lst_name.add(duplicatenric);
                        }
                        list_invoice.add(get_object);
                    }
                }

            }
            callback.onSuccess(list_invoice, lst_name);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public InvoiceReceipt GetPaymentObject(JSONObject json) {
        InvoiceReceipt invoice_obj = new InvoiceReceipt();
        try {
            if (json.has("ID")) {
                invoice_obj.setId(json.getInt("ID"));
            }
            if (json.has("MemberID")) {
                invoice_obj.setMemberid(json.getInt("MemberID"));
            }
            if (json.has("NRIC")) {
                invoice_obj.setNric(json.getString("NRIC"));
            }
            if (json.has("Name")) {
                invoice_obj.setNric_name(json.getString("Name"));
            }
            if (json.has("ClinicID")) {
                ClinicInfo clinic_obj = dbclinicinfo.getClinicInfo(json.getInt("ClinicID"));
                invoice_obj.setClinicname(clinic_obj.get_name());
                invoice_obj.setClinicid(json.getInt("ClinicID"));
            }
            if (json.has("ClinicGSTNo")) {
                invoice_obj.setClinicgstno(json.getString("ClinicGSTNo"));
            }
            if (json.has("InvoiceNo")) {
                invoice_obj.setInvoiceno(json.getString("InvoiceNo"));
            }
            if (json.has("BeforeGSTAmount")) {
                invoice_obj.setBeforegstamount(json.getString("BeforeGSTAmount"));
            }
            if (json.has("OutstandingAmount")) {
                invoice_obj.setOutstandingamount(json.getString("OutstandingAmount"));
            }
            if (json.has("InvoiceAmount")) {
                invoice_obj.setInvoiceamount(json.getString("InvoiceAmount"));
            }
            if (json.has("InvoiceURL")) {
                invoice_obj.setInvoiceurl(json.getString("InvoiceURL"));
            }
            if (json.has("InvoiceDate")) {
                invoice_obj.setInvoicedate(json.getString("InvoiceDate"));
            }
            if (json.has("ModifiedDate")) {
                invoice_obj.setModifieddate(json.getString("ModifiedDate"));
            }
            if (json.has("Status")) {
                invoice_obj.setStatus(json.getString("Status"));
            }
            if (json.has("isRate")) {
                invoice_obj.setIsRate(json.getString("isRate"));
            }
            if (json.has("Payment_Receipt")) {
                JSONArray json_receipt = json.getJSONArray("Payment_Receipt");
                ArrayList<Payment_Receipt> payReceiptlist = new ArrayList<Payment_Receipt>();
                if (json_receipt.length() > 0) {
                    for (int ii = 0; ii < json_receipt.length(); ii++) {
                        JSONObject receipt_json = json_receipt.getJSONObject(ii);
                        payReceiptlist.add(GetPaymentReceipt(receipt_json));
                    }
                }
                invoice_obj.setPay_receiptList(payReceiptlist);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return invoice_obj;
    }

    public Payment_Receipt GetPaymentReceipt(JSONObject json_receipt) {
        Payment_Receipt obj_payreceipt = new Payment_Receipt();
        try {
            if (json_receipt.has("ID")) {
                obj_payreceipt.setId(json_receipt.getInt("ID"));
            }
            if (json_receipt.has("ClinicID")) {
                obj_payreceipt.setClinicid(json_receipt.getInt("ClinicID"));
            }
            if (json_receipt.has("ReceiptNo")) {
                obj_payreceipt.setReceiptno(json_receipt.getString("ReceiptNo"));
            }
            if (json_receipt.has("InvoiceNo")) {
                obj_payreceipt.setInvoiceno(json_receipt.getString("InvoiceNo"));
            }
            if (json_receipt.has("ReceiptAmount")) {
                obj_payreceipt.setReceiptamount(json_receipt.getString("ReceiptAmount"));
            }
            if (json_receipt.has("ReceiptDate")) {
                obj_payreceipt.setReceiptdate(json_receipt.getString("ReceiptDate"));
            }
            if (json_receipt.has("ModifiedDate")) {
                obj_payreceipt.setModifieddate(json_receipt.getString("ModifiedDate"));
            }
            if (json_receipt.has("ReceiptURL")) {
                obj_payreceipt.setReceipturl(json_receipt.getString("ReceiptURL"));
            }
            if (json_receipt.has("PaymentMode")) {
                JSONObject paymentmode = json_receipt.getJSONObject("PaymentMode");
                ArrayList<PaymentType> pay_obj = new ArrayList<PaymentType>();
                for (String key : iterate(paymentmode.keys())) {
                    PaymentType pay = new PaymentType(key, paymentmode.getString(key));
                    pay_obj.add(pay);
                }
                obj_payreceipt.setPaymentlist(pay_obj);
            }
            if (json_receipt.has("Status")) {
                obj_payreceipt.setStatus(json_receipt.getString("Status"));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return obj_payreceipt;
    }


    //===========================================================
    //			Get Method All Clinic  list
    //===========================================================
    private Response.Listener<JSONObject> createMyReqSuccessListener(final VolleyCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setTvResultText(response, callback);
            }
        };
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
        try {
            JSONObject errorjson = new JSONObject(error);
            String errorcode ="",errormsg="";
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
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private <T> Iterable<T> iterate(final Iterator<T> i) {
        return new Iterable<T>() {
            @Override
            public Iterator<T> iterator() {
                return i;
            }
        };
    }
}

