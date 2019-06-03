package sg.com.ehealthassist.ehealthassist.CustomUI;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Date;

import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 9/26/2017.
 */

public class EventDecorator implements DayViewDecorator {
    private  Drawable drawable;
    private  CalendarDay day;
    private  int color;

    public EventDecorator(MaterialCalendarView view, Date date, int color) {
        this.day = CalendarDay.from(date);
        this.color = color;
        this.drawable = createTintedDrawable(view.getContext(), color);
    }
    @Override
    public boolean shouldDecorate(CalendarDay day) {
        if (this.day.equals(day)) {
            return true;
        }
        return false;
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setSelectionDrawable(drawable);
    }
    private static Drawable createTintedDrawable(Context context, int color) {
        return applyTint(createBaseDrawable(context), color);
    }

    private static Drawable applyTint(Drawable drawable, int color) {
        Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
        DrawableCompat.setTint(wrappedDrawable, color);
        return wrappedDrawable;
    }

    private static Drawable createBaseDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.myselector);
    }
}
