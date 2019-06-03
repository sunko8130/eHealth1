package sg.com.ehealthassist.ehealthassist.Appointment;


import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import sg.com.ehealthassist.ehealthassist.R;

public class ActivityAppointmentTest extends AppCompatActivity implements OnDateSelectedListener, OnMonthChangedListener {
    MaterialCalendarView mvcalendar;
    DateFormat Month_Formatter = new SimpleDateFormat("yyyyMM");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_appointment_test);

        Calendar cal_finish = Calendar.getInstance();
        cal_finish.add(Calendar.MONTH, 3);
        Calendar cal_start = Calendar.getInstance();

        mvcalendar.state().edit().setMinimumDate(cal_start).commit();
        mvcalendar.state().edit().setMaximumDate(cal_finish).commit();

        mvcalendar.setOnDateChangedListener(this);
        mvcalendar.setOnMonthChangedListener(this);

        String sessionmonth = Month_Formatter.format(CalendarDay.today().getDate());

    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {

    }

    @Override
    public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
        //mvcalendar.removeAllViews();

    }
}
