package sg.com.ehealthassist.ehealthassist.eDocument;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;


import sg.com.ehealthassist.ehealthassist.Models.EDocument.RADCapture;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 9/11/2017.
 */

public class RadCaptureAdapter  extends BaseAdapter {
    private Context _mcont;
    private List<RADCapture> list_rad;

    public RadCaptureAdapter(Context _mcontext, List<RADCapture> lst_rad) {
        this._mcont = _mcontext;
        this.list_rad = lst_rad;
    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list_rad.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_radview, null);
            v.txt_imageradtitle = (TextView)convertView.findViewById(R.id.txt_imageradtitle) ;
            v.txt_imageraddesc = (TextView)convertView.findViewById(R.id.txt_imageraddesc) ;
            v.txt_imageradcreateDate = (TextView)convertView.findViewById(R.id.txt_imageradcreateDate) ;
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        RADCapture labobject = list_rad.get(position);
        v.txt_imageradtitle.setText(labobject.getTitle());
        v.txt_imageraddesc.setText(labobject.getDesc());
        String createDate = Utils.BookDateFormat(labobject.getCreatedate(),"yyyyMMddHHmmss","yyyy-MM-dd hh:mm a");
        v.txt_imageradcreateDate.setText(createDate);
        return convertView;
    }

    class ViewHolder {
        TextView txt_imageradtitle;
        TextView txt_imageraddesc;
        TextView txt_imageradcreateDate;

    }
}
