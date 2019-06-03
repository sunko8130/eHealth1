package sg.com.ehealthassist.ehealthassist.Clinic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.CustomUI.CustomListView;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.OpeningHours;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 11/3/2017.
 */

public class OpenHourListviewAdapter  extends BaseAdapter {
    private Context _mcont;
    private List<OpeningHours> list_openhr;

    public OpenHourListviewAdapter(Context _mcontext, List<OpeningHours> lst_openhr) {
        this._mcont = _mcontext;
        this.list_openhr = lst_openhr;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list_openhr.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_clinic_openhourlistview, null);
            v.txt_openday = (TextView) convertView.findViewById(R.id.txt_openday);
            v.openlistView = (CustomListView) convertView
                    .findViewById(R.id.listview_toolbar);

            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        OpeningHours object = list_openhr.get(position);
        v.txt_openday.setText(object.getOpen_day());

        OpenTimeListAdapter opentime = new OpenTimeListAdapter(_mcont,object.getOpen_time());
         v.openlistView.setAdapter(opentime);
         v.openlistView.setExpanded(true);

        return convertView;
    }

    class ViewHolder {
        TextView txt_openday;
        CustomListView openlistView;
    }
}
