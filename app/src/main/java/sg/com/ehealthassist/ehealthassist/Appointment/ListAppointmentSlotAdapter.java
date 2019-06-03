package sg.com.ehealthassist.ehealthassist.Appointment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;

import java.util.List;

import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Appointment.AppointmentSlot;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */


public class ListAppointmentSlotAdapter extends BaseAdapter {
    public Activity activity;
    private Context _mcont;
    private List<AppointmentSlot> list_slot;
    DBClinicInfo dbclinic = null;
    ImageLoader mImageLoader;
    private static final int NOT_SELECTED = -1;
    private int selectedPos = NOT_SELECTED;
    int[] array_color;int color_position = -1;


    public ListAppointmentSlotAdapter(Context context, List<AppointmentSlot> lstdoctors) {
        this._mcont = context;
        this.list_slot = lstdoctors;
        this.dbclinic = new DBClinicInfo(context);
        array_color = _mcont.getResources().getIntArray(R.array.appt_doctor_color);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return list_slot.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder v = null;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.listslot_cardviewrow, null);
            // v.doctor_image = (NetworkImageView) convertView.findViewById(R.id.doctor_image);
            v.doctor_name = (TextView) convertView.findViewById(R.id.doctor_name);
            v.clinic_name = (TextView) convertView.findViewById(R.id.clinic_name);
            v.doctor_image= (ImageView) convertView.findViewById(R.id.doctor_image);
            //v.button_book = (Button) convertView.findViewById(R.id.button_book);
            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        v.doctor_name.setText("Dr. " + list_slot.get(position).getDisplayname());
        final ClinicInfo info = dbclinic.getClinicInfo(list_slot.get(position).getClinicid());
        v.clinic_name.setText(info.get_name());
        final AppointmentSlot time_slot = list_slot.get(position);
      /*  v.button_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseHandlerSlot dbslot = new DatabaseHandlerSlot(_mcont);
                dbslot.deleteAllSlot();
                dbslot.addSlotlog(time_slot.getAvailabeleSlots());

                Intent intent = new Intent(_mcont, ActivityAvailableTimeSlot.class);
                intent.putExtra("availableslot", time_slot);
                intent.putExtra("clinic_name", info.get_name());
                _mcont.startActivity(intent);
            }
        });*/

        //region ImageLoader
     /*   mImageLoader = VolleyImageRequest.getInstance(_mcont)
                .getImageLoader();
        final String url = "http://goo.gl/0rkaBz";

        mImageLoader.get(url, ImageLoader.getImageListener(v.doctor_image,
                R.mipmap.ic_launcher, R.drawable.image2));
        v.doctor_image.setImageUrl(url, mImageLoader);*/
        //endregion
        int colorarrysize = array_color.length;
        if(colorarrysize <= position){
            color_position = (position)%colorarrysize;
        }else{
            color_position = position;
        }

        v.doctor_image.setBackgroundColor(array_color[color_position]);
        if (position == selectedPos) {
            // your color for selected item
            convertView.setBackgroundColor(_mcont.getResources().getColor(R.color.cas_background));
        } else {
            // your color for non-selected item
            convertView.setBackgroundColor(_mcont.getResources().getColor(R.color.colorwhite));
        }
        return convertView;
    }

    class ViewHolder {
        // NetworkImageView doctor_image;
        TextView doctor_name;
        TextView clinic_name;
        ImageView doctor_image;
        //  Button button_book;
    }

    public void setSelection(int position) {

        selectedPos = position;
        notifyDataSetChanged();
    }

}

