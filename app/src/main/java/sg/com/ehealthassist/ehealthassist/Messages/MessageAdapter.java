package sg.com.ehealthassist.ehealthassist.Messages;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 9/27/2017.
 */

public class MessageAdapter  extends BaseAdapter {

    private Context _mcont;
    private List<Messages> list_message;
    DBClinicInfo dbinfo ;

    public MessageAdapter(Context _mcontext, List<Messages> lst_mes) {
        this._mcont = _mcontext;
        this.list_message = lst_mes;
        dbinfo = new DBClinicInfo(_mcontext);
    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list_message.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_message, null);
            v.txtclinic_name = (TextView)convertView.findViewById(R.id.txtclinic_name) ;
            v.txtmessage_value = (TextView)convertView.findViewById(R.id.txtmessage_value) ;
            v.txtcreatedate_value = (TextView)convertView.findViewById(R.id.txtcreatedate_value) ;
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        Messages mesobject = list_message.get(position);
        int clinicid  = Integer.parseInt(mesobject.getClinicid()+"") ;
        ClinicInfo info = dbinfo.getClinicInfo(clinicid);
        v.txtclinic_name.setText(info.get_name());
        v.txtmessage_value.setText(mesobject.getMessage());

        String cDate = mesobject.getCreatedate();
        String createDate="";
        if(!cDate.equals("")){
           createDate  = Utils.BookDateFormat(mesobject.getCreatedate(),"yyyy-MM-dd'T'HH:mm:ss","yyyy-MM-dd hh:mm a");
        }

        v.txtcreatedate_value.setText(createDate);
        return convertView;
    }

    class ViewHolder {
        TextView txtclinic_name;
        TextView txtmessage_value;
        TextView txtcreatedate_value;

    }
}
