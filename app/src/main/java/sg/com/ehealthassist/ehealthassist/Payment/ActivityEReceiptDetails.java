package sg.com.ehealthassist.ehealthassist.Payment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.CustomUI.CustomListView;
import sg.com.ehealthassist.ehealthassist.CustomUI.DownloadPDF;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Payment.InvoiceReceipt;
import sg.com.ehealthassist.ehealthassist.Models.Payment.Payment_Receipt;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;
import sg.com.ehealthassist.ehealthassist.eDocument.ActivityEDocument;
import sg.com.ehealthassist.ehealthassist.eDocument.ActivityRating;

public class ActivityEReceiptDetails extends AppCompatActivity {
    TextView main_toolbar_title,txt_clinicname,txtname_value,txtinvoice_amt_value2,
            txtnric_value,txtinvoice_no_value,txtinvoice_amt_value,txtTotal_paid_value,txtTotal_remain_amt_value
            ,txtclinic_gst_value,txtinvoice_date_value,txt_invoice_no_1,txt_invoice_no_status;
    Button btn_invoice_pdf;
    ImageButton toolbar_imgbutton_back;
    InvoiceReceipt obj_invoice ;
    ArrayList<Payment_Receipt> lstpayment_receipt = new ArrayList<Payment_Receipt>();
    CustomListView lv_paymentlist;
    Context _mcontext;
    PaymentReceiptAdapter payreceiptadapter;
    SharedPreferences preferences = null;
    String usertoken = "";
    ScrollView scrollview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_activity_ereceipt_details);
        _mcontext = this;
        preferences = _mcontext.getSharedPreferences(_mcontext.getString(R.string.preference_name), _mcontext.MODE_PRIVATE);
        usertoken = preferences.getString(_mcontext.getString(R.string.pref_login_Access_token),"");
        findViewByid();
        Intent invoice_object = getIntent();
        obj_invoice  = invoice_object.getParcelableExtra("InvoiceObject");
        lstpayment_receipt = obj_invoice.getPay_receiptList();
        LoadData();
        setEvent();
        Log.e("isRate Result",obj_invoice.getIsRate());
        if(obj_invoice.getIsRate().equals("N")){
            toRateDialog();
        }
    }
    //region find
    public  void findViewByid(){
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText("E - Receipt Details");
        toolbar_imgbutton_back = (ImageButton)findViewById(R.id.toolbar_imgbackbutton);
        txtname_value = (TextView) findViewById(R.id.txtname_value);
        txtnric_value = (TextView) findViewById(R.id.txtnric_value);
        txt_clinicname = (TextView) findViewById(R.id.txt_clinicname_value);
        txtclinic_gst_value = (TextView) findViewById(R.id.txtclinic_gst_value);
        txtinvoice_no_value = (TextView) findViewById(R.id.txtinvoice_no_value);
        txtinvoice_amt_value = (TextView) findViewById(R.id.txtinvoice_amt_value);
        txtinvoice_amt_value2 = (TextView)findViewById(R.id.txtinvoice_amt_value2);
        txtTotal_paid_value =(TextView)findViewById(R.id.txtTotal_paid_value);
        txtTotal_remain_amt_value=(TextView)findViewById(R.id.txtTotal_remain_amt_value);
        lv_paymentlist = (CustomListView)findViewById(R.id.lv_receiptview);
        txtinvoice_date_value = (TextView)findViewById(R.id.txtinvoice_date_value);
        txt_invoice_no_1 = (TextView)findViewById(R.id.txt_invoice_no_1);
        txt_invoice_no_status = (TextView)findViewById(R.id.txt_invoice_no_status);
        btn_invoice_pdf=(Button)findViewById(R.id.btn_invoice_pdf);
        scrollview = (ScrollView)findViewById(R.id.scrollview);
        scrollview.fullScroll(View.FOCUS_UP);
        scrollview.pageScroll(View.FOCUS_UP);
    }
    //endregion

    //region load invoice data
    public void LoadData(){
        txt_invoice_no_1.setText("INVOICE ("+ obj_invoice.getInvoiceno()+ ")");
        if(obj_invoice.getStatus().equals("")){
            txt_invoice_no_status.setText("");
        }else{
            txt_invoice_no_status.setText(" - "+obj_invoice.getStatus());
            txt_invoice_no_status.setTextColor(getResources().getColor(R.color.colorred));
        }
        txtname_value.setText(obj_invoice.getNric_name());
        txtnric_value.setText(obj_invoice.getNric());
        txtinvoice_no_value.setText(obj_invoice.getInvoiceno());

        txtclinic_gst_value.setText(obj_invoice.getClinicgstno());
        txt_clinicname.setText(obj_invoice.getClinicname());
        String changedate = Utils.GridviewChangefromat(obj_invoice.getInvoicedate(),"yyyy-MM-dd","dd-MMM-yyyy");
        txtinvoice_date_value.setText(changedate);

        double invoice_amount = Double.parseDouble(obj_invoice.getInvoiceamount());
        double outstanding_amount = Double.parseDouble(obj_invoice.getOutstandingamount());
        double final_receipt=0.0;

        final_receipt = invoice_amount-outstanding_amount;

        txtinvoice_amt_value.setText("S$ "+ String.format( "%.2f", invoice_amount));
        txtinvoice_amt_value2.setText("S$ " + String.format( "%.2f", invoice_amount) );
        txtTotal_paid_value.setText("S$ " + String.format( "%.2f", final_receipt)  );
        txtTotal_remain_amt_value.setText("S$ " + String.format( "%.2f", outstanding_amount));
        Loadlistview();
    }

   public void Loadlistview(){
        payreceiptadapter = new PaymentReceiptAdapter(_mcontext,lstpayment_receipt);
        lv_paymentlist.setAdapter(payreceiptadapter);
        payreceiptadapter.notifyDataSetChanged();
        lv_paymentlist.setExpanded(true);
    }
    //endregion

    //region event
    public void setEvent(){
        btn_invoice_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String filename = obj_invoice.getClinicid()+"_"+ obj_invoice.getInvoiceno()+"_"+obj_invoice.getModifieddate() + ".pdf";
                Log.e("filename",filename);
                String folder = "invoices";
                //create
                    if(checkfileexist(filename,folder)){
                        view(folder,filename);
                    }
                    else{
                        if(Utils.hasInternetConnection(_mcontext)){
                            String pdfurl = endcodepdfurl(obj_invoice.getId()+"","I");
                            Log.e("pdfurl",pdfurl);
                            if(modifieddatenotsame(obj_invoice.getInvoiceno(),obj_invoice.getModifieddate(),folder)){
                                new DownloadPDF(_mcontext,"invoices",usertoken).execute(pdfurl,filename);
                            }else{
                                new DownloadPDF(_mcontext,"invoices",usertoken).execute(pdfurl,filename);
                            }
                        }
                        else{
                            Utils.showInternetRequiredDialog(_mcontext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                            return;
                        }
                    }
            }
        });
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });
    }
    //endregion

    //region pdf file
    public  void view(String folderpath,String filename){
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/ehealthassist/" + folderpath +"/"+ filename);  // -> filename = maven.pdf
        Uri path = null;
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) {
            path = FileProvider.getUriForFile(
                    _mcontext,
                    _mcontext.getApplicationContext()
                            .getPackageName() + ".provider", pdfFile);

        }else{
            path = Uri.fromFile(pdfFile);
        }
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try{
            _mcontext.startActivity(pdfIntent);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    public boolean modifieddatenotsame(String invoiceno ,String modifidate,String folderpath){
        boolean flag = false;
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory, "ehealthassist/"+folderpath);
        folder.mkdir();
        File[] listoffile = folder.listFiles();
        if(listoffile!=null){
            if(listoffile.length>0){
                for(int i =0;i< listoffile.length;i++){
                    String filename =listoffile[i].getName();
                    String[] subarry = filename.split("_");
                    if(subarry.length>=3){
                        String f_invoiceno = subarry[1];
                        String f_modifiedate = subarry[2];
                        if(f_invoiceno.equals(invoiceno)){
                            if(!f_modifiedate.equals(modifidate)){
                                listoffile[i].delete();
                                flag=true;
                                break;
                            }
                        }
                    }
                }
            }
        }

        return flag;

    }
    public boolean checkfileexist(String filename,String folderpath){
        boolean flag = false;
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        File folder = new File(extStorageDirectory, "ehealthassist/"+folderpath);
        folder.mkdir();
        File pdfFile = new File(folder, filename);

        try {
            if (pdfFile.exists()) {
                flag = true;
            }
            else{
                flag = false;
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return flag;
    }
    public String endcodepdfurl(String id,String type){
        String url = "";
        try{
            url = Constant.URL_ReceiptPDF + "ID=" +id + "&Type="+type;

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return url;
    }
    //endregion

    //region to Rate invoice
    public void callrating(){
        Intent intent = new Intent(this,ActivityRating.class);
        intent.putExtra("invoiceno",obj_invoice.getInvoiceno());
        intent.putExtra("clinicname",obj_invoice.getClinicname());
        intent.putExtra("clinicid",obj_invoice.getClinicid());
        intent.putExtra("from","ActivityEReceiptDetails");
        Log.e("clinicname",obj_invoice.getClinicname());
        startActivity(intent);
    }

    public void toRateDialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ActivityEReceiptDetails.this);
        alertBuilder.setMessage("Please rate the Clinic")
                .setTitle("Rating")
                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        callrating();
                        dialog.dismiss();
                    }
                });
        alertBuilder.create().show();
    }
    //endregion

    //region back function
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnback();
    }

    public void returnback(){
        Intent intent = new Intent(getApplicationContext(), ActivityEDocument.class);
        intent.putExtra("from","ActivityEReceiptDetails");
        startActivity(intent);
        finish();
    }
    //endregion
}
