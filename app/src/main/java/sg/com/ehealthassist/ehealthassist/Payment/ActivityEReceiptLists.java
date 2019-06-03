/*
package sg.com.ehealthassist.ehealthassist.Payment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestInvoiceJson;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyInvoiceReceipt;
import sg.com.ehealthassist.ehealthassist.ActivityHome;
import sg.com.ehealthassist.ehealthassist.Models.Payment.InvoiceReceipt;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Profiles.SpinnerProfileAdapter;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityEReceiptLists extends AppCompatActivity {

    TextView main_toolbar_title, txterrormessage, txtTotalinvoice,
            txt_dialog_title, txtfilter_name;
    ImageButton toolbar_imgbutton_back, btn_ereceipt_refresh;

    ListView lv_invoice_receipt;
    Context _mcontext;
    SharedPreferences preferences = null;
    InvoiceReceiptAdapter invoice_adapter;
    SpinnerProfileAdapter profile_adapter;
    List<InvoiceReceipt> lstinvoice, lstlatest_load;
    List<String> lstsortName = new ArrayList<String>();
    ImageButton btn_filter;

    ListView lvfiltersortname;
    ImageButton image_close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_ereceipt_lists);
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);

        _mcontext = this;
        findviewbyid();
        LoadInvoiceReceipt();
        setEvent();

    }

    public void findviewbyid() {
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText("Financial");
        txterrormessage = (TextView) findViewById(R.id.txterrormessage);
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        lv_invoice_receipt = (ListView) findViewById(R.id.lv_invoicereceipt);
        txtTotalinvoice = (TextView) findViewById(R.id.txtTotalinvoice);
        btn_filter = (ImageButton) findViewById(R.id.btn_filter);
        txtfilter_name = (TextView) findViewById(R.id.txtfilter_name);
        btn_ereceipt_refresh = (ImageButton) findViewById(R.id.btn_ereceipt_refresh);
    }

    public void setEvent() {
        lv_invoice_receipt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                InvoiceReceipt object = lstlatest_load.get(position);
                Intent intent = new Intent(_mcontext, ActivityEReceiptDetails.class);
                intent.putExtra("InvoiceObject", object);
                startActivity(intent);
                finish();
            }
        });
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });
        btn_filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialogfiltername();
            }
        });
        btn_ereceipt_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadInvoiceReceipt();
            }
        });
    }

    public void LoadInvoiceReceipt() {
        if (Utils.hasInternetConnection(_mcontext)) {
            boolean isLoggedIn = preferences.getBoolean(getString(R.string.pref_is_logged_in), false);
            if (isLoggedIn) {
                txterrormessage.setVisibility(View.GONE);
                final String memberid = preferences.getString(getString(R.string.pref_login_memberId), "");
                String usertoken = preferences.getString(getString(R.string.pref_login_Access_token), "");
                RequestInvoiceJson param = new RequestInvoiceJson(memberid, "", "", "", "");
                String obj_param = param.objecttoJson();
                Log.e("param", obj_param);
                VolleyInvoiceReceipt v_receipt = new VolleyInvoiceReceipt(_mcontext);
                v_receipt.GetPaymentInvoiceJson(usertoken, param.StringtoJsonObject(obj_param), new VolleyInvoiceReceipt.VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList<InvoiceReceipt> listinvoice, ArrayList<String> lstnricname) {
                        lstinvoice = new ArrayList<InvoiceReceipt>();
                        lstlatest_load = new ArrayList<InvoiceReceipt>();
                        lstsortName.clear();
                        //lstsortName = new ArrayList<String>();
                        lstinvoice = listinvoice;
                        lstlatest_load = listinvoice;
                        lstsortName = lstnricname;
                        LoadAdapter(lstinvoice);
                        txtTotalinvoice.setText(lstinvoice.size() + " Invoice(s)");
                        txtfilter_name.setVisibility(View.VISIBLE);
                        btn_filter.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFail(String errorcode, String errormessage) {
                        txterrormessage.setVisibility(View.VISIBLE);
                        //  Utils.showAlertDialog(_mcontext,errorcode,errormessage);
                        txtTotalinvoice.setText(" 0 Invoice(s)");
                        btn_filter.setVisibility(View.GONE);
                        txtfilter_name.setVisibility(View.GONE);
                    }
                });
            } else {
                //  Utils.showInternetRequiredDialog(_mcontext, , getString(R.string.msg_no_internet_connection_setup));
                //return;
                txterrormessage.setVisibility(View.VISIBLE);
                btn_filter.setVisibility(View.GONE);
                txtfilter_name.setVisibility(View.GONE);
            }
        } else {
            Utils.showInternetRequiredDialog(_mcontext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            btn_filter.setVisibility(View.GONE);
            txtfilter_name.setVisibility(View.GONE);
            return;
        }

    }

    public void LoadAdapter(List<InvoiceReceipt> getlstinvoice) {
        lstlatest_load = getlstinvoice;
        txtTotalinvoice.setText(lstlatest_load.size() + " Invoice(s)");
        invoice_adapter = new InvoiceReceiptAdapter(this, getlstinvoice);
        lv_invoice_receipt.setAdapter(invoice_adapter);
        invoice_adapter.notifyDataSetChanged();
    }

    public void returnback() {
        Intent intent = new Intent(getApplicationContext(), ActivityHome.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnback();
    }

    public void filterbyname(String name) {
        ArrayList<InvoiceReceipt> filterreceipt = new ArrayList<InvoiceReceipt>();
        if (!name.equals("All")) {
            if (lstinvoice.size() > 0) {
                for (int i = 0; i < lstinvoice.size(); i++) {
                    String duplicatenric = lstinvoice.get(i).getNric_name() + " (" + lstinvoice.get(i).getNric() + ")";
                    if (duplicatenric.equals(name)) {
                        filterreceipt.add(lstinvoice.get(i));
                    }
                }
                LoadAdapter(filterreceipt);
            }
        } else {
            LoadAdapter(lstinvoice);
        }
    }

    public void Dialogfiltername() {
        final Dialog dialog = new Dialog(_mcontext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.activity_specialist_clinic_lists);
        lvfiltersortname = (ListView) dialog.findViewById(R.id.lvspecialist);
        image_close = (ImageButton) dialog.findViewById(R.id.img_close);
        txt_dialog_title = (TextView) dialog.findViewById(R.id.tool);
        txt_dialog_title.setText("Patient Name");

        if (!lstsortName.contains("All")) {
            lstsortName.add(0, "All");
        }

        LoadFilterSortAdapter();
        lvfiltersortname.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String sortname = lstsortName.get(position);
                filterbyname(sortname);
                if (!sortname.equals("All")) {
                    txtfilter_name.setText(splitname(sortname));
                } else {
                    txtfilter_name.setText(sortname);
                }
                dialog.dismiss();
            }
        });
        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public String splitname(String name) {
        String strname = "";
        if (!name.equals("")) {
            if (name.contains("(")) {
                String[] str = name.split("\\(");
                strname = str[0];
            }
        }
        return strname;
    }

    public void LoadFilterSortAdapter() {
        profile_adapter = new SpinnerProfileAdapter(this, lstsortName);
        lvfiltersortname.setAdapter(profile_adapter);
        profile_adapter.notifyDataSetChanged();
    }
}
*/
