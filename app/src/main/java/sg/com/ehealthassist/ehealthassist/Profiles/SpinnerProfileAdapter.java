package sg.com.ehealthassist.ehealthassist.Profiles;

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


public class SpinnerProfileAdapter extends BaseAdapter {

    Context _mcont;
    List<String> list_special;
    public SpinnerProfileAdapter(Context context, List<String> speciallist) {
        this._mcont = context;
        this.list_special = speciallist;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list_special.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder v = null;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_specialist_clinics, null);
            v._name = (TextView) convertView.findViewById(R.id.txt_specialist);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        v._name.setText( list_special.get(position));

        return convertView;
    }
    class ViewHolder {
        TextView _name;
    }
}

