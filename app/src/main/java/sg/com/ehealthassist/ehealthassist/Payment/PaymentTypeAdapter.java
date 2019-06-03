package sg.com.ehealthassist.ehealthassist.Payment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Payment.PaymentType;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */

public class PaymentTypeAdapter extends BaseAdapter {
    private Context _mcont;
    private List<PaymentType> typelist;

    public PaymentTypeAdapter(Context context, List<PaymentType> lstpaymenttype) {
        this._mcont = context;
        this.typelist = lstpaymenttype;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return typelist.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_paymenttype, null);
            v.txt_payment_type = (TextView) convertView.findViewById(R.id.txt_payment_type);
            v.txt_payment_value = (TextView) convertView.findViewById(R.id.txt_payment_value);

            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        PaymentType pay_object = typelist.get(position);
        v.txt_payment_type.setText(pay_object.getType() + " :");
        double payment_amt = Double.parseDouble(pay_object.getValue());
        v.txt_payment_value.setText("S$ "+String.format( "%.2f", payment_amt ) );

        return convertView;
    }

    class ViewHolder {
        TextView txt_payment_type,txt_payment_value;

    }
}

