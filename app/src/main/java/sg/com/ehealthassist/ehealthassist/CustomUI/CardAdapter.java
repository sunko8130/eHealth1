package sg.com.ehealthassist.ehealthassist.CustomUI;

import android.support.v7.widget.CardView;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public interface CardAdapter {
    int MAX_ELEVATION_FACTOR = 5;
    float getBaseElevation();
    CardView getCardViewAt(int postion);
    int getCount();
}
