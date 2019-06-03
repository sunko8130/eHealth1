package sg.com.ehealthassist.ehealthassist.Clinic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */


public class ClinicTypeAdapter extends BaseAdapter {
    private Context _mcont;
    private List<String> type_items;

    public ClinicTypeAdapter(Context context, List<String> lsttype) {
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_specialist_clinics, null);
            v.txtservice_name = (TextView) convertView.findViewById(R.id.txt_specialist);

            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        String dr_name = type_items.get(position);

        v.txtservice_name.setText(dr_name);

        return convertView;
    }

    class ViewHolder {
        TextView txtservice_name;

    }
}


