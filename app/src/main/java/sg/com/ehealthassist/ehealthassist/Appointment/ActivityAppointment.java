package sg.com.ehealthassist.ehealthassist.Appointment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyApptListSession;
import sg.com.ehealthassist.ehealthassist.Account.ActivityLogIn;
import sg.com.ehealthassist.ehealthassist.CustomUI.CustomOneDayDecorator;
import sg.com.ehealthassist.ehealthassist.CustomUI.EventBgDecorator;
import sg.com.ehealthassist.ehealthassist.CustomUI.EventBgDecorators;
import sg.com.ehealthassist.ehealthassist.CustomUI.TodayDecorator;
import sg.com.ehealthassist.ehealthassist.Models.Appointment.AppointmentSlot;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityAppointment extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {
    MaterialCalendarView mvcalendar;
    Context _mcontext;
    TextView main_toolbar_title,txtnodoctor_msg;
    ImageButton toolbar_imgbutton_back;
    ListView lv_doctorlist;
    ListAppointmentSlotAdapter availdocAdapt;
    public static ArrayList<AppointmentSlot> lstapptsession = new ArrayList<AppointmentSlot>();
    String usertoken = "";
    int clinicId = 0;String clinic_name ="",appt_mode ="";
    SharedPreferences pref = null;
    DateFormat Month_Formatter = new SimpleDateFormat("yyyyMM");
    DateFormat SelectedDate_Formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    ArrayList<CalendarDay> dates_doctorlist = new ArrayList<CalendarDay>();
    AppointmentSlot selected_appointSlot;
   // CustomOneDayDecorator cus_day ;
   // EventBgDecorator eventbg ;

   // CustomOneDayDecorator cus_day = new CustomOneDayDecorator();

   // EventBgDecorators eventbg = new EventBgDecorators();
    int[] array_color;int color_position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_appointment);
        _mcontext = this;
        pref = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        array_color = getResources().getIntArray(R.array.appt_doctor_color);
        init();
        findviewById();

        Calendar cal_finish = Calendar.getInstance();
        cal_finish.add(Calendar.MONTH, 3);
        Calendar cal_start = Calendar.getInstance();

        mvcalendar.state().edit().setMinimumDate(cal_start).commit();
        mvcalendar.state().edit().setMaximumDate(cal_finish).commit();
        mvcalendar.addDecorator(new TodayDecorator(_mcontext));
        mvcalendar.setOnDateChangedListener(this);
        mvcalendar.setOnMonthChangedListener(this);

        String sessionmonth = Month_Formatter.format(CalendarDay.today().getDate());

        callAppointmentListSession(sessionmonth);
        setEvent();
    }
    public void findviewById() {
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_activity_list_appointment_slot));
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        mvcalendar = (MaterialCalendarView) findViewById(R.id.calendarView);
        mvcalendar.setSelectedDate(Calendar.getInstance());
        lv_doctorlist = (ListView) findViewById(R.id.lv_doctorlist);
        txtnodoctor_msg=(TextView)findViewById(R.id.txtnodoctor_msg);
    }

    public void setEvent() {
        lv_doctorlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected_appointSlot = lstapptsession.get(position);
                AppointmentSlot object_slot = lstapptsession.get(position);
                availdocAdapt.setSelection(position);

                dates_doctorlist.clear();

                if (object_slot.getAvailabeleSlots().size() > 0) {
                    for (int i = 0; i < object_slot.getAvailabeleSlots().size(); i++) {
                        String date = object_slot.getAvailabeleSlots().get(i);
                        Date new_date = Utils.reminderTimeFormat(date, "yyyy-MM-dd'T'HH:mm:ss", "dd-MM-yyyy");
                        CalendarDay day = CalendarDay.from(new_date);
                        dates_doctorlist.add(day);
                    }
                    int colorarrysize = array_color.length;
                    if(colorarrysize <= position){
                        color_position = (position)%colorarrysize;
                    }else{
                        color_position = position;
                    }
                    Log.e("listview",dates_doctorlist.size()+"");

                    mvcalendar.addDecorators(new EventBgDecorator(_mcontext,array_color[color_position],dates_doctorlist));
                    mvcalendar.addDecorator(new TodayDecorator(_mcontext));
                    mvcalendar.refreshDrawableState();
                    mvcalendar.invalidateDecorators();


                   /* eventbg.setColor(array_color[color_position]);
                    eventbg.setDates(dates_doctorlist);
                    mvcalendar.addDecorators(eventbg);
                    mvcalendar.refreshDrawableState();
                    mvcalendar.invalidateDecorators();
*/
                }
            }

        });
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });
    }

    public void init() {
        usertoken = pref.getString(getString(R.string.pref_login_Access_token), "");
        Intent getclinicid = getIntent();
        clinicId = getclinicid.getIntExtra("CID", 0);
        clinic_name = getclinicid.getStringExtra("CName");
        appt_mode = getclinicid.getStringExtra("appt_mode");
        Log.e("clinic_name",clinic_name);
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {

      /*  if (dates_doctorlist != null) {
            if (dates_doctorlist.size() > 0) {
                List<CalendarDay> tempcalenday = new ArrayList<CalendarDay>();
                tempcalenday.addAll(dates_doctorlist);
                if (tempcalenday.contains(calendarDay)) {
                    tempcalenday.remove(calendarDay);
                }
            }
        }*/
         // mvcalendar.removeDecorators();

      /*  if(dates_doctorlist!=null){

            mvcalendar.invalidateDecorators();
            mvcalendar.addDecorator(new TodayDecorator(_mcontext));
            mvcalendar.addDecorators(new EventBgDecorator(_mcontext,array_color[color_position],dates_doctorlist));
            mvcalendar.refreshDrawableState();

        }*/

           // mvcalendar.invalidateDecorators();
           // mvcalendar.refreshDrawableState();


        if (dates_doctorlist != null) {
            if (dates_doctorlist.size() > 0) {
                List<CalendarDay> tempcalenday = new ArrayList<CalendarDay>();
                tempcalenday.addAll(dates_doctorlist);
                if (tempcalenday.contains(calendarDay)) {
                    tempcalenday.remove(calendarDay);
                }
            }
        }
       // cus_day.setDate(calendarDay.getDate());
     //   mvcalendar.addDecorators(cus_day);

      /*  eventbg.setColor(array_color[color_position]);
        if (dates_doctorlist != null) {
            eventbg.setDates(dates_doctorlist);
        }
        mvcalendar.addDecorators(eventbg);
        mvcalendar.refreshDrawableState();
        mvcalendar.invalidateDecorators();*/

        if(dates_doctorlist.size()>0 && dates_doctorlist != null){
            if(dates_doctorlist.contains(calendarDay)){
                if(!calendarDay.getDate().equals(Calendar.getInstance().getTime().getDate())){
                    String selecteddate = SelectedDate_Formatter.format(calendarDay.getDate());
                    Intent book_intent = new Intent(this,ActivityBookAppt.class);
                    book_intent.putExtra("CID",clinicId);
                    book_intent.putExtra("CName",clinic_name);
                    book_intent.putExtra("appt_mode",appt_mode);
                    book_intent.putExtra("selecteddate",selecteddate);
                    book_intent.putExtra("doctorname",selected_appointSlot.getDisplayname());
                    book_intent.putExtra("doctorid",selected_appointSlot.getId());
                    startActivity(book_intent);
                }
            }
        }
    }
    @Override
    public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
        String sessionmonth = Month_Formatter.format(calendarDay.getDate());
        callAppointmentListSession(sessionmonth);
    }

    /*  public void clearall() {
          dates_doctorlist.clear();
          mvcalendar.removeDecorators();
          mvcalendar.setSelectedDate(CalendarDay.today().getDate());
          oneDayDecorator.setDate(CalendarDay.today().getDate());
          mvcalendar.addDecorator(oneDayDecorator);
          mvcalendar.addDecorator(todayDecorator);
          mvcalendar.invalidateDecorators();
      }
  */
    public void callAppointmentListSession(String month) {
        if (Utils.hasInternetConnection(_mcontext)) {
            if (!usertoken.equals("")) {
                if (!pref.getBoolean(getString(R.string.pref_is_account_verified), false)) {
                    showDialog(getString(R.string.error_response_invalidtoken_code));
                } else {
                    VolleyApptListSession _volley_appt_session = new VolleyApptListSession(_mcontext);
                    _volley_appt_session.GetApptListSessionJson(clinicId,appt_mode, month, usertoken, new VolleyApptListSession.VolleyCallback() {
                        @Override
                        public void onSuccess(ArrayList<AppointmentSlot> result) {
                            lstapptsession = result;

                            if(result.size()>0){
                                txtnodoctor_msg.setVisibility(View.GONE);
                                lv_doctorlist.setVisibility(View.VISIBLE);
                                mvcalendar.invalidate();
                                LoadData();

                            }else{
                                txtnodoctor_msg.setVisibility(View.VISIBLE);
                                lv_doctorlist.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFail(String errorcode, String errormsg) {
                            //Utils.showAlertDialog(_mcontext,errorcode,errormsg);
                            Log.e(errorcode,errormsg);
                            txtnodoctor_msg.setVisibility(View.VISIBLE);
                            lv_doctorlist.setVisibility(View.GONE);
                        }
                    });
                }
            } else {
                Intent intent = new Intent(_mcontext, ActivityLogIn.class);
                intent.putExtra("CID", clinicId);
                intent.putExtra("from", "listAppointmentSlot");
                this.startActivity(intent);
                finish();
            }

        } else {
            Utils.showInternetRequiredDialog(_mcontext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
    }

    //region ShowDialog()
    public void showDialog(String errorcode) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(_mcontext);
        alertDialog.setTitle("Appointment Slot");
        if (errorcode.equals(getString(R.string.error_response_invalidtoken_code))) {
            alertDialog.setMessage(getString(R.string.error_response_upload_profile_status_InvalidUserToken));
        } else {
            alertDialog.setMessage(getString(R.string.error_response_doctoronduty_no_information_avaliavle));
        }
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    //endregion

    public void LoadData() {
        availdocAdapt = new ListAppointmentSlotAdapter(_mcontext, lstapptsession);
        lv_doctorlist.setAdapter(availdocAdapt);
        int selected_position = 0;
        if(selected_appointSlot != null){
            for(AppointmentSlot lstdoctor : lstapptsession){
                if(lstdoctor.getCasdoctorid().equals(selected_appointSlot.getCasdoctorid())){
                    selected_position = lstapptsession.indexOf(lstdoctor);
                    break;
                }
            }
        }
        lv_doctorlist.setItemChecked(selected_position,true);
        lv_doctorlist.setSelection(selected_position);
        lv_doctorlist.setSelected(true);
        lv_doctorlist.performItemClick(lv_doctorlist.getChildAt(selected_position), selected_position, 0);
        //availdocAdapt.setSelection(selected_position);
        availdocAdapt.notifyDataSetChanged();
    }

    public void returnback(){
        Intent intent_flow = new Intent(_mcontext,ActivityApptFlow.class);
        intent_flow.putExtra("CID", Integer.valueOf(clinicId));
        intent_flow.putExtra("CName", clinic_name);
        intent_flow.putExtra("appt_mode", appt_mode);
        _mcontext.startActivity(intent_flow);
        finish();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }
}
