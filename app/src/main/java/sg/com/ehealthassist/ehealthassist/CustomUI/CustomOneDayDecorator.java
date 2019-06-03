package sg.com.ehealthassist.ehealthassist.CustomUI;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by User on 7/3/2017.
 */

public class CustomOneDayDecorator implements DayViewDecorator {
    private CalendarDay date;

    private static final int color = Color.parseColor("#FFB300");

    public CustomOneDayDecorator(CalendarDay calendar) {
        date = calendar;
    }


    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {

        view.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

       // view.setSelectionDrawable(new ColorDrawable(color));
    }

    /**
     * We're changing the internals, so make sure to call {@linkplain MaterialCalendarView#invalidateDecorators()}
     */
    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}
