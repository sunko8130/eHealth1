package sg.com.ehealthassist.ehealthassist.CustomUI;

import android.graphics.drawable.ColorDrawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by User on 10/2/2017.
 */

public class EventBgDecorators implements DayViewDecorator {
    private int color;
    private HashSet<CalendarDay> dates;

    public EventBgDecorators(){}
    public EventBgDecorators(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public Collection<CalendarDay> getDates() {
        return dates;
    }

    public void setDates(Collection<CalendarDay> dates) {
        this.dates =  new HashSet<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {

        // view.setBackgroundDrawable(new ColorDrawable(color));
        view.setSelectionDrawable(new ColorDrawable(color));
    }
}
