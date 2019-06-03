package sg.com.ehealthassist.ehealthassist.eDocument;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.EDocument.CertsCapture;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 9/11/2017.
 */

public class CertsCaptureAdapter extends BaseAdapter {
    private Context _mcont;
    private List<CertsCapture> list_certs;

    public CertsCaptureAdapter(Context _mcontext, List<CertsCapture> lst_certs) {
        this._mcont = _mcontext;
        this.list_certs = lst_certs;
    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list_certs.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_certsview, null);
            v.txt_imagecertstitle = (TextView)convertView.findViewById(R.id.txt_imagecertstitle) ;
            v.txt_imagecertsdesc = (TextView)convertView.findViewById(R.id.txt_imagecertsdesc) ;
            v.txt_imagecertcreateDate = (TextView)convertView.findViewById(R.id.txt_imagecertcreateDate) ;
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        CertsCapture labobject = list_certs.get(position);
        v.txt_imagecertstitle.setText(labobject.getTitle());
        v.txt_imagecertsdesc.setText(labobject.getDesc());
        String createDate = Utils.BookDateFormat(labobject.getCreatedate(),"yyyyMMddHHmmss","yyyy-MM-dd hh:mm a");
        v.txt_imagecertcreateDate.setText(createDate);
        return convertView;
    }

    class ViewHolder {
        TextView txt_imagecertstitle;
        TextView txt_imagecertsdesc;
        TextView txt_imagecertcreateDate;

    }
}
