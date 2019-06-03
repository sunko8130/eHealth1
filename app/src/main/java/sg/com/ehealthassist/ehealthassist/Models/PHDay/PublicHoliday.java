package sg.com.ehealthassist.ehealthassist.Models.PHDay;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 6/29/2017.
 */

public class PublicHoliday implements Parcelable{
    String holiday_description = "";
    String holiday_date  = "";


    public PublicHoliday(){}

    public PublicHoliday(String holiday_description, String holiday_date) {
        this.holiday_description = holiday_description;
        this.holiday_date = holiday_date;
    }

    public String getHoliday_description() {
        return holiday_description;
    }

    public void setHoliday_description(String holiday_description) {
        this.holiday_description = holiday_description;
    }

    public String getHoliday_date() {
        return holiday_date;
    }

    public void setHoliday_date(String holiday_date) {
        this.holiday_date = holiday_date;
    }

    //region Parcelable()
    public PublicHoliday(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.holiday_description = in.readString();
        this.holiday_date = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(holiday_description);
        dest.writeString(holiday_date);
    }

    public static Parcelable.Creator<PublicHoliday> CREATOR = new Parcelable.Creator<PublicHoliday>() {

        @Override
        public PublicHoliday createFromParcel(Parcel source) {
            return new PublicHoliday(source);
        }

        @Override
        public PublicHoliday[] newArray(int size) {
            return new PublicHoliday[size];
        }

    };
}
