package sg.com.ehealthassist.ehealthassist.Appointment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Appointment.BookInfo;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */

public class EAppointmentCardAdapter extends BaseAdapter {
    private Context _mcont;
    private List<BookInfo> list_book;
    DBClinicInfo dbclinic = null;

    public EAppointmentCardAdapter(Context context, List<BookInfo> booklist) {
        this._mcont = context;
        this.list_book = booklist;
        this.dbclinic = new DBClinicInfo(context);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list_book.size();
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
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_eappointment_card, null);
            v.appointment_time = (TextView) convertView.findViewById(R.id.appt_time);
            v.appointment_date = (TextView) convertView.findViewById(R.id.appt_date);
            v.clinic_name = (TextView) convertView.findViewById(R.id.appt_clinic);
            v.appt_requestor_nric = (TextView) convertView.findViewById(R.id.appt_requestor_nric);
            v.appt_status = (TextView) convertView.findViewById(R.id.appt_status);
            v.textstatus_state = (TextView) convertView.findViewById(R.id.textstatus_state);
            v.txt_appt_month = (TextView) convertView.findViewById(R.id.txt_appt_month);
            v.txt_appt_day = (TextView) convertView.findViewById(R.id.txt_appt_day);
            v.txt_appt_session = (TextView) convertView.findViewById(R.id.appt_session);
            v.lblappt_time = (TextView) convertView.findViewById(R.id.lbl_time);
            v.lblappt_session = (TextView) convertView.findViewById(R.id.lbl_session);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        v.clinic_name.setText(list_book.get(position).getClinic_name());
        v.appointment_date.setText(Utils.BookDateFormat(list_book.get(position).getBook_date(), "yyyy-MM-dd'T'HH:mm:ss", "EEE, dd-MMM-yyyy"));
        v.appointment_time.setText(Utils.BookDateFormat(list_book.get(position).getBook_date(), "yyyy-MM-dd'T'HH:mm:ss", "hh:mm aa"));
        if (!list_book.get(position).getBook_date().equals("")) {
            Date date = Utils.reminderTimeFormat(list_book.get(position).getBook_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
            String stringMonth = (String) android.text.format.DateFormat.format("MMM", date);
            String day = (String) android.text.format.DateFormat.format("dd", date);
            v.txt_appt_day.setText(day);
            v.txt_appt_month.setText(stringMonth);
        }

        String request_patient = list_book.get(position).getRequestor_name() + " ("
                + list_book.get(position).getRequestor_nric() + ")";
        v.appt_requestor_nric.setText(request_patient);
        v.appt_status.setText(list_book.get(position).getApp_status());
        if (list_book.get(position).getApp_status().equals("Pending")) {
            //v.textstatus_state.setTextColor(_mcont.getResources().getColor(R.color.cas_color_tab_yellow));
            v.appt_status.setTextColor(_mcont.getResources().getColor(R.color.cas_color_tab_yellow));

        } else if (list_book.get(position).getApp_status().equals("Cancelled By Patient")) {
            //  v.textstatus_state.setTextColor(_mcont.getResources().getColor(R.color.cas_color_red));
            v.appt_status.setTextColor(_mcont.getResources().getColor(R.color.colorred));
        } else if (list_book.get(position).getApp_status().equals("Cancelled By Clinic")) {
            //  v.textstatus_state.setTextColor(_mcont.getResources().getColor(R.color.cas_color_red));
            v.appt_status.setTextColor(_mcont.getResources().getColor(R.color.colorred));
        } else if (list_book.get(position).getApp_status().equals("Accepted")) {
            //  v.textstatus_state.setTextColor(_mcont.getResources().getColor(R.color.cas_color_green));
            //  setAlarm(list_book.get(position));
            v.appt_status.setTextColor(_mcont.getResources().getColor(R.color.cas_success_green));

        } else if (list_book.get(position).getApp_status().equals("Confirmed")) {
            //  v.textstatus_state.setTextColor(_mcont.getResources().getColor(R.color.cas_color_green));
            v.appt_status.setTextColor(_mcont.getResources().getColor(R.color.cas_success_green));
        } else if (list_book.get(position).getApp_status().equals("Rescheduled")) {
            //  v.textstatus_state.setTextColor(_mcont.getResources().getColor(R.color.cas_color_red));
            v.appt_status.setTextColor(_mcont.getResources().getColor(R.color.pink_accent_900));
        } else {
            v.appt_status.setTextColor(_mcont.getResources().getColor(R.color.colorred));
        }
        v.txt_appt_session.setText(list_book.get(position).getApptsession());

       /* if(list_book.get(position).getApp_status().equals("Accepted") ){
            v.appointment_time.setVisibility(View.VISIBLE);
            v.lblappt_time.setVisibility(View.VISIBLE);
            v.lblappt_session.setVisibility(View.GONE);
            v.txt_appt_session.setVisibility(View.GONE);
        }
        else{*/

        if (list_book.get(position).getAppt_type().equals("B")) {
            v.appointment_time.setVisibility(View.GONE);
            v.lblappt_time.setVisibility(View.GONE);
            v.lblappt_session.setVisibility(View.VISIBLE);
            v.lblappt_session.setText("Time :");
            v.txt_appt_session.setVisibility(View.VISIBLE);
        } else {
            if (v.appointment_time.getText().toString().equals("12:00 am")) {
                v.appointment_time.setVisibility(View.GONE);
                v.lblappt_time.setVisibility(View.GONE);
                v.lblappt_session.setText("Session :");
                v.lblappt_session.setVisibility(View.VISIBLE);
                v.txt_appt_session.setVisibility(View.VISIBLE);

            } else {
                v.appointment_time.setVisibility(View.VISIBLE);
                v.lblappt_time.setVisibility(View.VISIBLE);
                v.lblappt_session.setVisibility(View.GONE);
                v.txt_appt_session.setVisibility(View.GONE);
            }
        }

        /*  }*/
        return convertView;
    }

    class ViewHolder {
        TextView lblappt_time;
        TextView lblappt_session;
        TextView appointment_time;
        TextView appointment_date;
        TextView clinic_name;
        TextView appt_requestor_nric;
        TextView appt_status;
        TextView textstatus_state;
        TextView txt_appt_month;
        TextView txt_appt_day;
        TextView txt_appt_session;
    }

}

