package sg.com.ehealthassist.ehealthassist.Appointment;

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

public class ApptSessionAdapter extends BaseAdapter {
    private Context _mcont;
    private List<String> lst_session;

    public ApptSessionAdapter(Context context, List<String> lstsession) {
        this._mcont = context;
        this.lst_session = lstsession;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return lst_session.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_apptsession, null);
            v.txtsession_name = (TextView) convertView.findViewById(R.id.txt_session);

            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        String session_name = lst_session.get(position);

        v.txtsession_name.setText(session_name);

        return convertView;
    }

    class ViewHolder {
        TextView txtsession_name;
    }
}

