package sg.com.ehealthassist.ehealthassist.eDocument;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.EDocument.FinCapture;
import sg.com.ehealthassist.ehealthassist.Models.EDocument.LABCapture;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 9/12/2017.
 */

public class FinCaptureAdapter extends BaseAdapter {
    private Context _mcont;
    private List<FinCapture> list_fin;

    public FinCaptureAdapter(Context _mcontext, List<FinCapture> lst_fin) {
        this._mcont = _mcontext;
        this.list_fin = lst_fin;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list_fin.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_finview, null);
            v.txt_imagetitle = (TextView)convertView.findViewById(R.id.txt_imagefintitle) ;
            v.txt_imagedesc = (TextView)convertView.findViewById(R.id.txt_imagefindesc) ;
            v.txt_imagecreateDate = (TextView)convertView.findViewById(R.id.txt_imagefincreateDate) ;
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        FinCapture labobject = list_fin.get(position);
        v.txt_imagetitle.setText(labobject.getTitle());
        v.txt_imagedesc.setText(labobject.getDesc());
        String createDate = Utils.BookDateFormat(labobject.getCreatedate(),"yyyyMMddHHmmss","yyyy-MM-dd hh:mm a");
        v.txt_imagecreateDate.setText(createDate);
        return convertView;
    }

    class ViewHolder {
        TextView txt_imagetitle;
        TextView txt_imagedesc;
        TextView txt_imagecreateDate;
    }
}
