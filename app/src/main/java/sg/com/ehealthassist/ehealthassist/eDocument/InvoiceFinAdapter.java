package sg.com.ehealthassist.ehealthassist.eDocument;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


import sg.com.ehealthassist.ehealthassist.Models.EDocument.FinCapture;
import sg.com.ehealthassist.ehealthassist.Models.Payment.InvoiceReceipt;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 9/18/2017.
 */
import java.text.DecimalFormat;

public class InvoiceFinAdapter extends BaseAdapter {
    private Context _mcont;
    private List<InvoiceFin> listinvoicefin;
    DecimalFormat df = new DecimalFormat("#.##");

    public InvoiceFinAdapter(Context context, List<InvoiceFin> lstinvoice) {
        this._mcont = context;
        this.listinvoicefin = lstinvoice;
    }


    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return listinvoicefin.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_invoicereceipt, null);
            v.txtclinic_value = (TextView) convertView.findViewById(R.id.txtclinic_name);
            v.txtinvoicedate_value = (TextView) convertView.findViewById(R.id.txtinvoicedate_value);
            v.txtrinvoiceamount_value = (TextView) convertView.findViewById(R.id.txtrinvoiceamount_value);
            v.txtinvoiceno_value = (TextView)convertView.findViewById(R.id.txtinvoiceno_value) ;

            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        InvoiceFin infin = listinvoicefin.get(position);
        if(infin.isObject.equals("I")){
            InvoiceReceipt  invoice_object = (InvoiceReceipt)listinvoicefin.get(position).getMultiobj();
            v.txtclinic_value.setText(invoice_object.getClinicname());
            String changedate = Utils.GridviewChangefromat(invoice_object.getInvoicedate(),"yyyy-MM-dd","dd-MMM-yyyy");
            v.txtinvoicedate_value.setText(changedate);

            double invoice_amount = Double.parseDouble(invoice_object.getInvoiceamount()) ;
            double outstanding_amount = Double.parseDouble(invoice_object.getOutstandingamount());
            double remaing_amt  = remainingAmount(invoice_amount,outstanding_amount);

            v.txtrinvoiceamount_value.setText("S$ "+ String.format( "%.2f", invoice_amount ));
            v.txtinvoiceno_value.setText("( Invoice no : "+invoice_object.getInvoiceno()+" )");
            v.txtinvoiceno_value.setVisibility(View.VISIBLE);
            v.txtrinvoiceamount_value.setTextSize(18);
            if(outstanding_amount>0){
                v.txtrinvoiceamount_value.setTextColor(_mcont.getResources().getColor(R.color.colorred));
            }

            else{
                v.txtrinvoiceamount_value.setTextColor(_mcont.getResources().getColor(R.color.blue_primary_900));
            }

        }else{
            FinCapture fin_object= (FinCapture) listinvoicefin.get(position).getMultiobj();
            v.txtclinic_value.setText(fin_object.getTitle());
            v.txtinvoicedate_value.setText(fin_object.getDesc());
            String createDate = Utils.BookDateFormat(fin_object.getCreatedate(),"yyyyMMddHHmmss","yyyy-MM-dd hh:mm a");
            v.txtrinvoiceamount_value.setText(createDate);
            v.txtrinvoiceamount_value.setTextColor(_mcont.getResources().getColor(R.color.colorblack));
            v.txtrinvoiceamount_value.setTextSize(14);
            v.txtinvoiceno_value.setVisibility(View.GONE);
        }

        return convertView;
    }

    class ViewHolder {
        TextView   txtrinvoiceamount_value,txtinvoicedate_value,txtclinic_value ,txtinvoiceno_value;
    }



    public double remainingAmount(double invoiceamt,double paidamt){
        double remaintAmt = invoiceamt - paidamt;
        return remaintAmt;
    }
}
