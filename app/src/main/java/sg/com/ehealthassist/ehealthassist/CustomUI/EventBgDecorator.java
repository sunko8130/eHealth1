package sg.com.ehealthassist.ehealthassist.CustomUI;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 7/3/2017.
 */

public class EventBgDecorator implements DayViewDecorator {
    private int color;
    private HashSet<CalendarDay> dates;
    private  Drawable drawable;
    private  Context _mcontext;

    public EventBgDecorator(Context context){
        this._mcontext = context;
        this.drawable = _mcontext.getResources().getDrawable(R.drawable.myselector);
    }

    public EventBgDecorator(Context context , int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        this._mcontext = context;
        this.drawable = _mcontext.getResources().getDrawable(R.drawable.myselector);

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

        GradientDrawable gDrawable = (GradientDrawable)drawable;
        gDrawable.setColor(color);
        view.setSelectionDrawable(gDrawable);
    }
}
