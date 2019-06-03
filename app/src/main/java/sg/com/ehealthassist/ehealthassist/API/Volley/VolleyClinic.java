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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.ActivitySplashScreen;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBClinic_Services;
import sg.com.ehealthassist.ehealthassist.DB.DBTestClinic;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.Clinic_Services;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Other.AppController;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 6/29/2017.
 */

public class VolleyClinic {
    private String Tag_json_obj = "_req_clinicInfo_api";
    SharedPreferences pref_constant = null;
    Context _vcontext;
    List<ClinicInfo> list_clinic = new ArrayList<ClinicInfo>();
    int successRecord = 0, failedRecord = 0, currentRecord = 0;
    public String newpaging = "";
    public static boolean success = false;
    DBClinic_Services db_clinicservice;
    Integer[] test_clinic_array ={1000,1001,1002,1006,2006,2118,8000,8081,8165};


    public interface VolleyCallback {
        void onSuccess(boolean flag);

        void onFail(String errorcode, String message);
    }

    public VolleyClinic(Context _mcontext) {
        _vcontext = _mcontext;
        db_clinicservice = new DBClinic_Services(_vcontext);
        pref_constant = _vcontext.getSharedPreferences(_vcontext.getString(R.string.preference_constant), _vcontext.MODE_PRIVATE);
    }

    //===========================================================
    //			Get Method All Clinic  list
    //===========================================================
    public void GetClinicJson(String urlJsonObj, VolleyCallback callback) {
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
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (response.has(Constant.NODE_STATUS)) {
                        String status = response.getString(Constant.NODE_STATUS);
                        if (status.equals(Constant.CHECK_STATUS_OK)) {
                            if (response.has(Constant.NODE_DATA)) {
                                JSONArray response_data = response.getJSONArray(Constant.NODE_DATA);
                                for (int i = 0; i < response_data.length(); i++) {
                                    JSONObject dataObj = (JSONObject) response_data.get(i);
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
                                    list_clinic.add(get_clinicinfo);

                                    DBClinicInfo dbHandler = new DBClinicInfo(_vcontext);
                                    DBTestClinic dbHandler_testclinic = new DBTestClinic(_vcontext);

                                    if (Arrays.asList(test_clinic_array).contains(get_clinicinfo.get_id())) {
                                        if (dbHandler_testclinic.updateClinicInfos(get_clinicinfo) > 0) {
                                            successRecord++;
                                            ActivitySplashScreen.download_count = successRecord + "";

                                        } else {
                                            failedRecord++;
                                        }
                                    } else {
                                        if (dbHandler.updateClinicInfos(get_clinicinfo) > 0) {
                                            successRecord++;
                                            ActivitySplashScreen.download_count = successRecord + "";
                                        } else {
                                            failedRecord++;
                                        }
                                    }
                                }


                                // if(response.has(Constant.NODE_HASNEXT)){
                                if (response.has(Constant.NODE_PAGINATION)) {
                                    JSONObject page = response.getJSONObject(Constant.NODE_PAGINATION);
                                    if (page.has(Constant.NODE_HASNEXT)) {
                                        boolean hasnext = page.getBoolean(Constant.NODE_HASNEXT);
                                        newpaging = page.getString(Constant.NODE_NEXTFROM);
                                        pref_constant.edit().
                                                putString(_vcontext.getString(R.string.pref_permanent_clinic_info_row_version), newpaging)
                                                .apply();
                                        Log.e("New paging", newpaging);
                                        if (hasnext) {

                                            GetClinicJson(Constant.URL_CLINICINFO + newpaging, new VolleyCallback() {
                                                @Override
                                                public void onSuccess(boolean flag) {

                                                }

                                                @Override
                                                public void onFail(String errorcode, String message) {

                                                }
                                            });
                                        } else {
                                            if (successRecord == list_clinic.size()) {

                                                Log.e("Total record", successRecord + " " + list_clinic.size());
                                                ActivitySplashScreen.download_flag = true;
                                                ActivitySplashScreen.download_count = successRecord + "";
                                                editpreferencedownload_flag(true);
                                                callback.onSuccess(true);
                                            }
                                        }
                                    } else {

                                        if (successRecord == list_clinic.size()) {
                                            Log.e("Total record", successRecord + " " + list_clinic.size());
                                            ActivitySplashScreen.download_flag = true;
                                            editpreferencedownload_flag(true);
                                            callback.onSuccess(true);
                                        }
                                    }
                                }

                            } else {
                                ActivitySplashScreen.download_flag = true;
                                editpreferencedownload_flag(true);
                                callback.onSuccess(true);
                            }

                        } else {
                            ActivitySplashScreen.download_flag = true;
                            editpreferencedownload_flag(true);
                            callback.onSuccess(true);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(Tag_json_obj, "Error :" + e.getMessage());
                    Toast.makeText(_vcontext,
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }).start();
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
                            ActivitySplashScreen.download_flag = true;
                            editpreferencedownload_flag(true);
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

    public void editpreferencedownload_flag(boolean download_flag) {
        pref_constant.edit().putBoolean(_vcontext.getString(R.string.pref_permanent_clinic_download_flag), download_flag).apply();
    }
}
