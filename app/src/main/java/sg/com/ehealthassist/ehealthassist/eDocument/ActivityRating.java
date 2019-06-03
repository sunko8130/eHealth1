package sg.com.ehealthassist.ehealthassist.eDocument;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestInvoiceRate;
import sg.com.ehealthassist.ehealthassist.API.Request.RequestRating;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyInvoiceRate;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyRating;
import sg.com.ehealthassist.ehealthassist.DB.DBCertsCapture;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBFinCapture;
import sg.com.ehealthassist.ehealthassist.DB.DBLABCapture;
import sg.com.ehealthassist.ehealthassist.DB.DBRADCapture;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.EDocument.Rating;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityRating extends Activity {

    ArrayList<Rating> lst_rate;
    String[] arry_ques;
    RatingAdapter adapter;
    Context _context;
    ListView lv_rating, lv_rateclinic;
    Button btn_ratingsubmit, btn_cancel;
    public static String clinic_code = "",clinicname = "";
    String memberid = "", usertoken = "", invoiceno = "";
    int clinicid = 0;
    boolean account_verified = false;
    SharedPreferences preferences = null;
    int postion = 0;
    String createdate = "", from = "";
    DBFinCapture dbfin;
    DBCertsCapture dbcerts;
    DBLABCapture dblab;
    DBRADCapture dbrad;
    DBClinicInfo dbclinic;
    ClinicRateAdapter clinicadapter;
    List<ClinicInfo> lstclinic = new ArrayList<ClinicInfo>();
    public Dialog clinic_dialog;
    public static TextView txtclinicname;
    RelativeLayout rlclinic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        setContentView(R.layout.activity_rating);
        _context = this;
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        lv_rating = (ListView) findViewById(R.id.lv_rating);
        btn_ratingsubmit = (Button) findViewById(R.id.btn_ratingsubmit);
        btn_cancel = (Button) findViewById(R.id.btn_ratingcancel);
        txtclinicname = (TextView)findViewById(R.id.txtrateclinicname) ;
        rlclinic = (RelativeLayout)findViewById(R.id.rlclinic);
        usertoken = preferences.getString(getString(R.string.pref_login_Access_token), "");
        account_verified = preferences.getBoolean(getString(R.string.pref_is_account_verified), false);
        memberid = preferences.getString(getString(R.string.pref_login_memberId), "");
        dbfin = new DBFinCapture(_context);
        dblab = new DBLABCapture(_context);
        dbrad = new DBRADCapture(_context);
        dbcerts = new DBCertsCapture(_context);
        dbclinic = new DBClinicInfo(_context);
        Intent gintent = getIntent();
        from = gintent.getStringExtra("from");
        if (from.equals("ActivityEReceiptDetails")) {
            clinic_code = "";
            clinicid = gintent.getIntExtra("clinicid",0);
            ClinicInfo info = dbclinic.getClinicInfo(clinicid);
            clinic_code = info.getHecode();
            clinicname = gintent.getStringExtra("clinicname");
            invoiceno = gintent.getStringExtra("invoiceno");
            txtclinicname.setText(clinicname);
            btn_cancel.setVisibility(View.GONE);
            Log.e("clinicname",clinicname);
            rlclinic.setEnabled(false);
            txtclinicname.setEnabled(false);
        } else {
            clinic_code = "";
            postion = gintent.getIntExtra("update position", 0);
            createdate = gintent.getStringExtra("createdate");
            btn_cancel.setVisibility(View.VISIBLE);
            rlclinic.setEnabled(true);
            txtclinicname.setEnabled(true);
        }
        LoadQuestion();
        LoadRating();
        setEvent();
    }

    //region setEvent
    public void setEvent() {

        btn_ratingsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str_review1 = getResources().getString(R.string.reviewq1);
                if (Utils.hasInternetConnection(_context)) {
                    if (!usertoken.equals("") && account_verified) {
                      //  if (!from.equals("ActivityEReceiptDetails")) {
                            if(!clinic_code.equals("")){

                                RequestRating robj = new RequestRating();
                                robj.setClinic_code(clinic_code);
                                robj.setMemberid(memberid);
                               // boolean flag = false;
                                if (lst_rate.size() > 0) {
                                    for (int i = 0; i < lst_rate.size(); i++) {
                                     //   flag = false;
                                        switch (i) {
                                            case 0:
                                                robj.setQ1(lst_rate.get(i).getScore() + "");
                                                break;
                                            case 1:
                                                robj.setQ2(lst_rate.get(i).getScore() + "");
                                                break;
                                            case 2:
                                                robj.setQ3(lst_rate.get(i).getScore() + "");
                                                break;
                                            case 3:

                                                robj.setQ4(lst_rate.get(i).getScore() + "");
                                                break;
                                            case 4:

                                                robj.setQ5(lst_rate.get(i).getScore() + "");
                                                break;
                                            case 5:
                                                robj.setQ6(lst_rate.get(i).getScore() + "");
                                                break;

                                        }
                                    }
                                }
                                Log.e("question",robj.getQ1());
                                if(robj.getQ1().equals("0.0") || robj.getQ2().equals("0.0")|| robj.getQ3().equals("0.0")){
                                    showAlertDialog(_context,"Alert", str_review1,"N");
                                }else{
                                    callvolleyrating(robj);
                                    if(from.equals("ActivityEReceiptDetails")){
                                        callvolleyinvoicerating();
                                    }
                                }

                            }else{
                                showAlertDialog(_context,"Alert", str_review1,"N");
                            }
                    } else {
                        Utils.showAlertDialog(_context, getString(R.string.error_response_invalidtoken_message), getString(R.string.error_response_upload_profile_status_InvalidUserToken));
                        return;
                    }
                } else {
                    Utils.showInternetRequiredDialog(_context, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                    return;
                }
            }


        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rlclinic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(_context,ActivityRatingClinic.class);
                startActivity(intent);
            }
        });
    }
    //endregion

    //region callvolley
    public void callvolleyrating(RequestRating robj) {

        JSONObject param = robj.StringtoJsonObject(robj.objecttoJson());
        VolleyRating _vrating = new VolleyRating(_context);
        Log.e("rating param",param.toString());
        _vrating.GetRatingInsert(param, usertoken, new VolleyRating.VolleyCallback() {
            @Override
            public void onSuccess(String message) {
                Log.e("Rating", message);
                switch (postion) {
                    case 0:
                        dbfin.updateRatebycreatedate(createdate, memberid);
                        break;
                    case 1:
                        dblab.updateRatebycreatedate(createdate, memberid);
                        break;
                    case 2:
                        dbrad.updateRatebycreatedate(createdate, memberid);
                        break;
                    case 3:
                        dbcerts.updateRatebycreatedate(createdate, memberid);
                        break;
                }

                showAlertDialog(_context, "Rate", message,"Y");
            }

            @Override
            public void onFail(String errorcode, String errormessage) {
                Utils.showAlertDialog(_context, errorcode, errormessage);
            }
        });
    }

    public void callvolleyinvoicerating() {

        RequestInvoiceRate requestobj = new RequestInvoiceRate(clinicid, invoiceno);
        JSONObject param = requestobj.StringtoJsonObject(requestobj.objecttoJson());

        VolleyInvoiceRate _vinvoicerate = new VolleyInvoiceRate(_context);
        _vinvoicerate.GetInvoiceRating(param, usertoken, new VolleyInvoiceRate.VolleyCallback() {
            @Override
            public void onSuccess(String message) {
                showAlertDialog(_context, "Rate", message,"Y");
            }

            @Override
            public void onFail(String errorcode, String errormessage) {
                Utils.showAlertDialog(_context, errorcode, errormessage);
            }
        });
    }

    //endregion

    public  void showAlertDialog(Context context, String title, String message, final String isfinish) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(isfinish.equals("Y")){
                    finish();
                }
            }
        });
        alertDialog.show();
    }

    //region loadQuestion & rating
    public void LoadQuestion() {
        arry_ques = getResources().getStringArray(R.array.ratequestion);
        lst_rate = new ArrayList<Rating>();
        if (arry_ques.length > 0) {
            for (String ques : arry_ques) {
                Rating obj = new Rating(ques, 0);
                lst_rate.add(obj);
            }
        }
    }

    public void LoadRating() {
        adapter = new RatingAdapter(_context, lst_rate);
        lv_rating.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    //endregion

    //region call Clinic Page
  /*  public void showclinicDialog() {
        if (clinic_dialog != null && clinic_dialog.isShowing()) {
            return;
        }
        clinic_dialog = new Dialog(_context);
        clinic_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        clinic_dialog.setContentView(R.layout.dialogratingclinic);
        clinic_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        lv_rateclinic = (ListView) clinic_dialog.findViewById(R.id.lv_rateclinic);
        lstclinic = dbclinic.getAllClinicList();
        LoadClinic(lstclinic);

        clinic_dialog.show();
    }

    public void LoadClinic(List<ClinicInfo> lstinfo) {
        clinicadapter = new ClinicRateAdapter(_context, lstinfo);
        lv_rateclinic.setAdapter(clinicadapter);
        clinicadapter.notifyDataSetChanged();
    }
*/

    //endregion
}
