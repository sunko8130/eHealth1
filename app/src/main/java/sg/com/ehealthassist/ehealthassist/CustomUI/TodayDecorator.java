package sg.com.ehealthassist.ehealthassist.CustomUI;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;

import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 7/3/2017.
 */

public class TodayDecorator implements DayViewDecorator {
    private CalendarDay date;

    private  Context _mcontext;

    private static final int color = Color.parseColor("#F06292");

    //F06292
    public TodayDecorator(Context context) {
        date = CalendarDay.today();
        this._mcontext = context;

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {

        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) {

         view.setSelectionDrawable(new ColorDrawable(color));
       // view.setBackgroundDrawable(new ColorDrawable(color));

    }

    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}
