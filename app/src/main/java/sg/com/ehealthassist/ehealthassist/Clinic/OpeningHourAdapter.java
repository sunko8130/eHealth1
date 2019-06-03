package sg.com.ehealthassist.ehealthassist.Clinic;

import android.content.Context;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.CustomUI.CustomGridView;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.OpeningHours;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class OpeningHourAdapter extends BaseAdapter {
    private Context _mcont;
    private List<OpeningHours> list_openhr;

    public OpeningHourAdapter(Context _mcontext, List<OpeningHours> lst_openhr) {
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_clinic_openinghours, null);
            v.txt_openday = (TextView) convertView.findViewById(R.id.txt_openday);
            v.gridView = (CustomGridView) convertView
                    .findViewById(R.id.GridView_toolbar);

            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        OpeningHours object = list_openhr.get(position);
        v.txt_openday.setText(object.getOpen_day());
        v.gridView.setNumColumns(3);// gridView.setGravity(Gravity.CENTER);//
        v.gridView.setHorizontalSpacing(5);// SimpleAdapter adapter =

        OpenHourGridview adapter = new OpenHourGridview(_mcont, object.getOpen_time());
        v.gridView.setAdapter(adapter);// Adapter
        int totalHeight = -15;
        for (int size = 0; size < adapter.getCount(); size++) {
            RelativeLayout relativeLayout = (RelativeLayout) adapter.getView(
                    size, null, v.gridView);
            TextView textView = (TextView) relativeLayout.getChildAt(0);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    _mcont.getResources().getDimension(R.dimen.gridview_txtview_textsize));
            textView.measure(0, 0);
            totalHeight += textView.getMeasuredHeight();
        }
        v.gridView.SetHeight(totalHeight);

        return convertView;
    }

    class ViewHolder {
        TextView txt_openday;
        CustomGridView gridView;

    }


}

