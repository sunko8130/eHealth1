package sg.com.ehealthassist.ehealthassist.Home;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class HomeViewPager extends PagerAdapter {

    private Context mContext;
    private int[] mResources;

    public HomeViewPager(Context mContext, int[] mResources) {
        this.mContext = mContext;
        this.mResources = mResources;
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.home_pager_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_advertise);
        imageView.setImageResource(mResources[position]);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}

