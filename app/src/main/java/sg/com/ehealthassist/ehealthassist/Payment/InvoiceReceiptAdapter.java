/*
package sg.com.ehealthassist.ehealthassist.Payment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Payment.InvoiceReceipt;
import sg.com.ehealthassist.ehealthassist.Models.Payment.Payment_Receipt;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

*/
/**
 * Created by thazinhlaing on 2/7/17.
 *//*

public class InvoiceReceiptAdapter extends BaseAdapter {
    private Context _mcont;
    private List<InvoiceReceipt> listinvoice;
    DecimalFormat df = new DecimalFormat("#.##");

    public InvoiceReceiptAdapter(Context context, List<InvoiceReceipt> lstinvoice) {
        this._mcont = context;
        this.listinvoice = lstinvoice;
    }


    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return listinvoice.size();
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
        InvoiceReceipt invoice_object = listinvoice.get(position);
        v.txtclinic_value.setText(invoice_object.getClinicname());
        String changedate = Utils.GridviewChangefromat(invoice_object.getInvoicedate(),"yyyy-MM-dd","dd-MMM-yyyy");
        v.txtinvoicedate_value.setText(changedate);


        double invoice_amount = Double.parseDouble(invoice_object.getInvoiceamount()) ;

        //  double paid_amount = calculate_paidamount(invoice_object.getPay_receiptList());
        double outstanding_amount = Double.parseDouble(invoice_object.getOutstandingamount());

        double remaing_amt  = remainingAmount(invoice_amount,outstanding_amount);

        v.txtrinvoiceamount_value.setText("S$ "+ String.format( "%.2f", invoice_amount ));
        v.txtinvoiceno_value.setText("( Invoice no : "+invoice_object.getInvoiceno()+" )");

        if(outstanding_amount>0){
            v.txtrinvoiceamount_value.setTextColor(_mcont.getResources().getColor(R.color.colorred));
        }

        else{
            v.txtrinvoiceamount_value.setTextColor(_mcont.getResources().getColor(R.color.blue_primary_900));
        }

        return convertView;
    }

    class ViewHolder {
        TextView   txtrinvoiceamount_value,txtinvoicedate_value,txtclinic_value ,txtinvoiceno_value;
    }

  */
/*  public Double calculate_paidamount(ArrayList<Payment_Receipt> lstpayment_receipt){
        double total_receiptamt = 0.00;
        if(lstpayment_receipt.size()>0){
            for(int i =0;i<lstpayment_receipt.size();i++){
                String receipt_amt = lstpayment_receipt.get(i).getReceiptamount();
                if(!receipt_amt.equals("")){
                    total_receiptamt += Double.parseDouble(receipt_amt);
                }
            }
        }
        return total_receiptamt;
    }*//*


    public double remainingAmount(double invoiceamt,double paidamt){
        double remaintAmt = invoiceamt - paidamt;
        return remaintAmt;
    }

}

*/
