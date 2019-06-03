package sg.com.ehealthassist.ehealthassist.Events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Event.EventsView;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 1/7/17.
 */
public class EventViewAdapter extends BaseAdapter {
    private Context _mcont;
    private List<EventsView> list_event;

    public EventViewAdapter(Context _mcontext, List<EventsView> lst_events) {
        this._mcont = _mcontext;
        this.list_event = lst_events;
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list_event.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_eventview, null);
            v.txt_eventname = (TextView) convertView.findViewById(R.id.txt_eventname);
            v.txt_event_date = (TextView) convertView.findViewById(R.id.txt_event_date);
            v.txt_event_time = (TextView) convertView.findViewById(R.id.txt_event_time);
            v.txt_event_description = (TextView) convertView.findViewById(R.id.txt_event_description);
            v.imgstatus=(ImageView)convertView.findViewById(R.id.imgstatus);
            v.txt_event_venue=(TextView)convertView.findViewById(R.id.txt_event_venue);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        v.txt_eventname.setText(list_event.get(position).getEventName());
        String event_fromdate, event_Todate, event_date = "";
        /*if (Utils.eventcomparedate(list_event.get(position).getFromDate(), list_event.get(position).getToDate()) == 1) {
            event_fromdate = Utils.BookDateFormat(list_event.get(position).getFromDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
            event_date = event_fromdate;
        } else {
            event_fromdate = Utils.BookDateFormat(list_event.get(position).getFromDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
            event_Todate = Utils.BookDateFormat(list_event.get(position).getToDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
            event_date = event_fromdate + " to " + event_Todate;
        }*/
        event_fromdate = Utils.BookDateFormat(list_event.get(position).getFromDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
        event_Todate = Utils.BookDateFormat(list_event.get(position).getToDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
        event_date = event_fromdate + " to " + event_Todate;


        String event_formtime =  Utils.BookDateFormat(list_event.get(position).getFromTime(),"HH:mm","hh:mm a")  ;

        String event_Totime = Utils.BookDateFormat( list_event.get(position).getToTime(),"HH:mm","hh:mm a");


        v.txt_event_date.setText(event_date);

        v.txt_event_time.setText(event_formtime + " to " + event_Totime);

        v.txt_event_description.setText(list_event.get(position).getEventDesc());
        v.txt_event_venue.setText(list_event.get(position).getVenue());
        String status = list_event.get(position).getStatus();
        if(status.equals("R") ){
            v.imgstatus.setVisibility(View.VISIBLE);
        }
        else{
            v.imgstatus.setVisibility(View.GONE);
       }
        return convertView;
    }

    class ViewHolder {
        TextView txt_eventname;
        TextView txt_event_date;
        TextView txt_event_time;
        TextView txt_event_description;
        ImageView imgstatus;
        TextView txt_event_venue;
    }
}
