package sg.com.ehealthassist.ehealthassist.Models.Reminder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 6/30/2017.
 */

public class ReminderTime  implements Parcelable{
    String time = "0.00";
    int qty = 1;
    int take = 0;

    public ReminderTime(String time,int qty,int take) {
        this.time = time;
        this.qty = qty;
        this.take = take;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getTake() {
        return take;
    }

    public void setTake(int take) {
        this.take = take;
    }

    public ReminderTime(Parcel in){
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in){
        this.time = in.readString();
        this.qty = in.readInt();
        this.take = in.readInt();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel dest,int flags){
        dest.writeString(time);
        dest.writeInt(qty);
        dest.writeInt(take);
    }
    public static Parcelable.Creator<ReminderTime> CREATOR = new Parcelable.Creator<ReminderTime>() {

        @Override
        public ReminderTime createFromParcel(Parcel source) {
            return new ReminderTime(source);
        }

        @Override
        public ReminderTime[] newArray(int size) {
            return new ReminderTime[size];
        }

    };

}
