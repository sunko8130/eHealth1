package sg.com.ehealthassist.ehealthassist.API.Volley;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBClinic_Services;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.Clinic_Services;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Other.AppController;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class VolleyClinicInfo {
    private String Tag_json_obj = "_req_clinicInfo_api";
    SharedPreferences pref_constant = null;
    Context _vcontext;
    DBClinic_Services db_clinicservice;

    public interface VolleyCallback {
        void onSuccess(boolean flag);

        void onFail(String errorcode, String message);
    }

    public VolleyClinicInfo(Context _mcontext) {
        _vcontext = _mcontext;
        db_clinicservice = new DBClinic_Services(_vcontext);
        pref_constant = _vcontext.getSharedPreferences(_vcontext.getString(R.string.preference_constant), _vcontext.MODE_PRIVATE);
    }

    //===========================================================
    //			Get Method All Clinic  list
    //===========================================================
    public void GetClinicbyidJson(String urlJsonObj, VolleyCallback callback) {
        String uri = String
                .format(urlJsonObj);

        JsonObjectRequest myReq = new JsonObjectRequest(Request.Method.GET,
                uri,
                createMyReqSuccessListener(callback),
                createMyReqErrorListener(callback)) {
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
    //			Success Response Result
    //============================================================
    private Response.Listener<JSONObject> createMyReqSuccessListener(final VolleyCallback callback) {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                setTvResultText(response, callback);
            }
        };
    }

    public void setTvResultText(final JSONObject response, final VolleyCallback callback) {
        try {
            if (response.has(Constant.NODE_STATUS)) {
                String status = response.getString(Constant.NODE_STATUS);
                if (status.equals(Constant.CHECK_STATUS_OK)) {
                    if (response.has(Constant.NODE_DATA)) {
                        JSONObject dataObj = response.getJSONObject(Constant.NODE_DATA);
                        ClinicInfo get_clinicinfo = GetClinicObject(dataObj);
                        if (dataObj.has("Services")) {
                            db_clinicservice.deleteClinicServicesbyclinicid(get_clinicinfo.get_id());
                            JSONArray json_arr_services = dataObj.getJSONArray("Services");
                            for (int j = 0; j < json_arr_services.length(); j++) {
                                String service = json_arr_services.getString(j);
                                Clinic_Services obj_clinicservice = new Clinic_Services(get_clinicinfo.get_id(), service);
                                db_clinicservice.addServices(obj_clinicservice);
                            }
                        }
                        DBClinicInfo dbHandler = new DBClinicInfo(_vcontext);
                        if (dbHandler.updateClinicInfos(get_clinicinfo) > 0) {
                            Log.e("update success", "clinic info");
                        }
                    }
                }
            }
            callback.onSuccess(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //============================================================
    //			Error Response Result
    //============================================================
    private Response.ErrorListener createMyReqErrorListener(final VolleyCallback callback) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("volley error", error.toString());
                String body = "";
                try {
                    if (error.networkResponse.data != null) {
                        try {
                            body = new String(error.networkResponse.data, "UTF-8");

                            callback.onFail("error", error.toString());
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

    public ClinicInfo GetClinicObject(JSONObject JSONOBG) {
        ClinicInfo clinicInfo = new ClinicInfo();
        try {
            if (JSONOBG.has(Constant.NODE_ID)) {
                clinicInfo.set_id(JSONOBG.getInt(Constant.NODE_ID));
            }
            if (JSONOBG.has(Constant.NODE_NAME)) {
                clinicInfo.set_name(JSONOBG.getString(Constant.NODE_NAME));
            }
            if (JSONOBG.has(Constant.NODE_ADDRESS)) {
                JSONObject address_jsonobj = JSONOBG.getJSONObject(Constant.NODE_ADDRESS);
                if (address_jsonobj.has(Constant.NODE_BLOCKNO)) {
                    clinicInfo.getClinic_address().setBlockno(address_jsonobj.getString(Constant.NODE_BLOCKNO));
                }
                if (address_jsonobj.has(Constant.NODE_STREET)) {
                    clinicInfo.getClinic_address().setStreet(address_jsonobj.getString(Constant.NODE_STREET));
                }
                if (address_jsonobj.has(Constant.NODE_UNITNO)) {
                    clinicInfo.getClinic_address().setUnitno(address_jsonobj.getString(Constant.NODE_UNITNO));
                }
                if (address_jsonobj.has(Constant.NODE_BUILDING)) {
                    clinicInfo.getClinic_address().setBuilding(address_jsonobj.getString(Constant.NODE_BUILDING));
                }
                if (address_jsonobj.has(Constant.NODE_POSTAL)) {
                    clinicInfo.getClinic_address().setPostal(address_jsonobj.getString(Constant.NODE_POSTAL));
                }
                if (address_jsonobj.has(Constant.NODE_REGION)) {
                    clinicInfo.getClinic_address().setRegion(address_jsonobj.getString(Constant.NODE_REGION));
                }
                if (address_jsonobj.has(Constant.NODE_PLACETOWN)) {
                    clinicInfo.getClinic_address().setPlacetown(address_jsonobj.getString(Constant.NODE_PLACETOWN));
                }
                if (address_jsonobj.has(Constant.NODE_NEARESTMRT)) {
                    clinicInfo.getClinic_address().setNearestmrt(address_jsonobj.getString(Constant.NODE_NEARESTMRT));
                }
            }
            if (JSONOBG.has(Constant.NODE_CONTACT)) {
                JSONObject contact_json = JSONOBG.getJSONObject(Constant.NODE_CONTACT);
                if (contact_json.has(Constant.NODE_TEL1)) {
                    clinicInfo.getClinic_contact().set_tel1(contact_json.getInt(Constant.NODE_TEL1));
                }
                if (contact_json.has(Constant.NODE_TEL2)) {
                    clinicInfo.getClinic_contact().set_tel2(contact_json.getInt(Constant.NODE_TEL2));
                }
                if (contact_json.has(Constant.NODE_FAX)) {
                    clinicInfo.getClinic_contact().set_fax(contact_json.getInt(Constant.NODE_FAX));
                }
                if (contact_json.has(Constant.NODE_EMAIL)) {
                    clinicInfo.getClinic_contact().set_email(contact_json.getString(Constant.NODE_EMAIL));
                }
                if (contact_json.has(Constant.NODE_WEBSITE)) {
                    clinicInfo.getClinic_contact().set_website(contact_json.getString(Constant.NODE_WEBSITE));
                }
            }
            if (JSONOBG.has(Constant.NODE_CLINICTYPE)) {
                clinicInfo.setClinicType(JSONOBG.getString(Constant.NODE_CLINICTYPE));
            }
            if (JSONOBG.has(Constant.NODE_OPERATINGHOURS)) {
                JSONArray opernHrArray = JSONOBG.getJSONArray(Constant.NODE_OPERATINGHOURS);
                String operninghrstr = JSONOBG.getString(Constant.NODE_OPERATINGHOURS);
                clinicInfo.set_operatinghr(operninghrstr);
            }
            if (JSONOBG.has(Constant.NODE_LOCATION)) {
                JSONObject jsonlocation = JSONOBG.getJSONObject(Constant.NODE_LOCATION);
                if (jsonlocation.has(Constant.NODE_LAT)) {
                    clinicInfo.getClinic_location().set_lat(jsonlocation.getString(Constant.NODE_LAT));
                }
                if (jsonlocation.has(Constant.NODE_LNG)) {
                    clinicInfo.getClinic_location().set_lng(jsonlocation.getString(Constant.NODE_LNG));
                }
            }
            if (JSONOBG.has(Constant.NODE_IS247)) {
                clinicInfo.set_is24Hours(JSONOBG.getBoolean(Constant.NODE_IS247));
            }
            if (JSONOBG.has(Constant.NODE_ISCHAS)) {
                clinicInfo.set_isChas(JSONOBG.getBoolean(Constant.NODE_ISCHAS));
            }
            if (JSONOBG.has(Constant.NODE_ISQUEUEENABLED)) {
                clinicInfo.set_isQueueEnabled(JSONOBG.getBoolean(Constant.NODE_ISQUEUEENABLED));
            }
            if (JSONOBG.has(Constant.NODE_TOSHOW)) {
                clinicInfo.set_toShow(JSONOBG.getBoolean(Constant.NODE_TOSHOW));
            }
            if (JSONOBG.has(Constant.NODE_LOGOID)) {
                clinicInfo.setLogoloid(JSONOBG.getString(Constant.NODE_LOGOID));
            }
            if (JSONOBG.has(Constant.NODE_Specialty)) {
                clinicInfo.set_specialty(JSONOBG.getString(Constant.NODE_Specialty));
            }
            if (JSONOBG.has(Constant.NODE_ISAPPTENABLED)) {
                clinicInfo.set_isapptEnabled(JSONOBG.getBoolean(Constant.NODE_ISAPPTENABLED));
            }
            if (JSONOBG.has(Constant.NODE_Clinic_note)) {
                clinicInfo.setClinic_note(JSONOBG.getString(Constant.NODE_Clinic_note));
            }
            if (JSONOBG.has(Constant.NODE_Clinic_HECODE)) {
                clinicInfo.setHecode(JSONOBG.getString(Constant.NODE_Clinic_HECODE));
            }
            if (JSONOBG.has("ApptType")) {
                clinicInfo.setAppt_mode(JSONOBG.getString("ApptType"));
            }

        } catch (Exception ex) {
            Log.e(Tag_json_obj, ex.toString());
        }
        return clinicInfo;

    }
}

