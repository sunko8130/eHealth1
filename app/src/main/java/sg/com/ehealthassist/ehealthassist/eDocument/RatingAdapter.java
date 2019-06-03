package sg.com.ehealthassist.ehealthassist.eDocument;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.EDocument.Rating;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 9/12/2017.
 */

public class RatingAdapter extends BaseAdapter {
    private Context _mcont;
    private List<Rating> list_rate;

    public RatingAdapter(Context _mcontext, List<Rating> lst_rate) {
        this._mcont = _mcontext;
        this.list_rate = lst_rate;
    }
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list_rate.size();
    }

    @Override
    public Rating getItem(int position) {

        return list_rate.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder v = null;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_rating, null);
            v.txt_question = (TextView)convertView.findViewById(R.id.txtquestion) ;
            v.ratingBar =(RatingBar)convertView.findViewById(R.id.ratebar);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }
        Rating labobject = list_rate.get(position);
        v.txt_question.setText(labobject.getQuestion());
        v.ratingBar.setOnRatingBarChangeListener(onRatingChangedListener(v, position));
        return convertView;
    }
    private RatingBar.OnRatingBarChangeListener onRatingChangedListener(final ViewHolder holder, final int position) {
        return new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Rating labobject = getItem(position);
                labobject.setScore(v);


            }
        };
    }
    class ViewHolder {
        TextView txt_question;
        RatingBar ratingBar;


    }
}
