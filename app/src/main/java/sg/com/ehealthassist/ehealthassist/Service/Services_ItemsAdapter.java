package sg.com.ehealthassist.ehealthassist.Service;

/**
 * Created by thazinhlaing on 2/7/17.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Services.Services_Items;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 10/24/2016.
 */
public class Services_ItemsAdapter extends BaseAdapter {
    private Context _mcont;
    private List<Services_Items> services_items;

    public Services_ItemsAdapter(Context context, List<Services_Items> lstservices) {
        this._mcont = context;
        this.services_items = lstservices;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return services_items.size();
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
        String dr_name = services_items.get(position).getServices_name();

        v.txtservice_name.setText(dr_name);

        return convertView;
    }

    class ViewHolder {
        TextView txtservice_name;

    }
}

