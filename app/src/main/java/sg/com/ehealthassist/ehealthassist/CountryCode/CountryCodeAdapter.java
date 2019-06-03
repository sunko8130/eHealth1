package sg.com.ehealthassist.ehealthassist.CountryCode;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Models.Profile.CountryCode;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 6/30/2017.
 */

public class CountryCodeAdapter extends BaseAdapter {
    private Context _mcont;

    private ArrayList<CountryCode> type_items;

    public CountryCodeAdapter(Context context, ArrayList<CountryCode> lsttype) {
        this._mcont = context;
        this.type_items = lsttype;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return type_items.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_countrycode, null);
            v.txtcountrycode = (TextView) convertView.findViewById(R.id.txtcountrycode);
            v.txtcountryname = (TextView) convertView.findViewById(R.id.txtcountryname);
            v.txtisocode = (TextView) convertView.findViewById(R.id.txtisocode);

            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        String dr_name = type_items.get(position).getCountryName();
        String dr_isocode = "( " + type_items.get(position).getISOCode() + " )";
        String dr_countrycode = "+ " + type_items.get(position).getCountryCode();

        v.txtcountrycode.setText(dr_countrycode);
        v.txtcountryname.setText(dr_name);
        v.txtisocode.setText(dr_isocode);

        return convertView;
    }

    class ViewHolder {
        TextView txtcountrycode;
        TextView txtcountryname;
        TextView txtisocode;
    }
}
