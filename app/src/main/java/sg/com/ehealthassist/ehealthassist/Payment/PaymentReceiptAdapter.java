package sg.com.ehealthassist.ehealthassist.Payment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.CustomUI.CustomListView;
import sg.com.ehealthassist.ehealthassist.CustomUI.DownloadPDF;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Payment.Payment_Receipt;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */

class PaymentReceiptAdapter  extends BaseAdapter {
    private Context _mcont;
    private List<Payment_Receipt> paylist;
    PaymentTypeAdapter payadapter;
    SharedPreferences preferences = null;
    String usertoken = "";

    public PaymentReceiptAdapter(Context context, List<Payment_Receipt> lstpaymenttype) {
        this._mcont = context;
        this.paylist = lstpaymenttype;
        preferences = context.getSharedPreferences(context.getString(R.string.preference_name), context.MODE_PRIVATE);
        usertoken = preferences.getString(context.getString(R.string.pref_login_Access_token),"");
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return paylist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder v = null;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_paymentreceipt, null);
            v.textreceipt = (TextView) convertView.findViewById(R.id.textreceipt);
            v.txtreceipt_date_value = (TextView) convertView.findViewById(R.id.txtreceipt_date_value);
            v.txtreceipt_no_value = (TextView) convertView.findViewById(R.id.txtreceipt_no_value);
            v.txtreceipt_amt_value = (TextView) convertView.findViewById(R.id.txtreceipt_amt_value);
            v.btn_ereceipt_pdf = (Button)convertView.findViewById(R.id.btn_ereceipt_pdf);
            v.typelist = (CustomListView)convertView.findViewById(R.id.lv_paymenttypelist) ;
            v.btn_ereceipt_pdf =(Button)convertView.findViewById(R.id.btn_ereceipt_pdf);
            v.textreceipt_status =(TextView)convertView.findViewById(R.id.textreceipt_status);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        final Payment_Receipt pay_object = paylist.get(position);
        if(pay_object.getStatus().equals("")){
            v.textreceipt_status.setText("");
        }
        else{
            v.textreceipt_status.setText(" - "+ pay_object.getStatus());
            v.textreceipt_status.setTextColor(_mcont.getResources().getColor(R.color.colorred));
        }
        v.textreceipt.setText("RECEIPT (" +pay_object.getReceiptno() + ")");
        String changedate = Utils.GridviewChangefromat(pay_object.getReceiptdate(),"yyyy-MM-dd","dd-MMM-yyyy");
        v.txtreceipt_date_value.setText(changedate);
        v.txtreceipt_no_value.setText(pay_object.getReceiptno());
        double receiptamt = Double.parseDouble(pay_object.getReceiptamount());
        v.txtreceipt_amt_value.setText("S$ "+ String.format( "%.2f", receiptamt));

        payadapter = new PaymentTypeAdapter(_mcont,pay_object.getPaymentlist());
        v.typelist.setAdapter(payadapter);
        payadapter.notifyDataSetChanged();
        v.typelist.setExpanded(true);

        v.btn_ereceipt_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             /*   String url = endcodepdfurl(usertoken,pay_object.getId()+"","R");
                new DownloadPDF(_mcont).execute(url, pay_object.getReceiptno()+".pdf");*/
                String filename = pay_object.getId()+"_"+pay_object.getReceiptno()+".pdf";
                String folder = "receipts";
                if(checkfileexist(filename,folder)){
                    view(folder,filename);
                }
                else{
                    if(Utils.hasInternetConnection(_mcont)){
                        String pdfurl = endcodepdfurl(pay_object.getId()+"","R");
                        new DownloadPDF(_mcont,"receipts",usertoken).execute(pdfurl, pay_object.getId()+"_"+pay_object.getReceiptno()+".pdf");
                    }
                    else{
                        Utils.showInternetRequiredDialog(_mcont, _mcont.getString(R.string.title_internet_require), _mcont.getString(R.string.msg_no_internet_connection_setup));
                        return;
                    }
                }

            }
        });
        return convertView;
    }

    class ViewHolder {
        TextView textreceipt,txtreceipt_date_value,txtreceipt_no_value,txtreceipt_amt_value,textreceipt_status;
        CustomListView typelist;
        Button btn_ereceipt_pdf;
    }

    public String endcodepdfurl(String id,String type){
        String url = "";
        try{
            // String query = URLEncoder.encode(usertoken, "utf-8");
            url = Constant.URL_ReceiptPDF+ "ID=" +id + "&Type="+type;
            Log.e("pdfurl",url);

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return url;
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
    public  void view(String folderpath,String filename){
        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/ehealthassist/" + folderpath +"/"+ filename);  // -> filename = maven.pdf
        Uri path = null;
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);

        if (Build.VERSION.SDK_INT >= 24) {
            path = FileProvider.getUriForFile(
                    _mcont,
                    _mcont.getApplicationContext()
                            .getPackageName() + ".provider", pdfFile);

        }else{
            path = Uri.fromFile(pdfFile);
        }
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try{
            _mcont.startActivity(pdfIntent);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}

