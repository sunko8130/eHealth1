package sg.com.ehealthassist.ehealthassist.Models.Payment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class PaymentType implements  Parcelable{
    String type = "";
    String value = "0.00";

    public PaymentType(String type, String value) {
        this.type = type;
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public PaymentType(Parcel in) {
        readFromParcel(in);
    }
    public void readFromParcel(Parcel in) {
        this.type = in.readString();
        this.value = in.readString();
    }
    @Override
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(value);
    }

    public static Creator<PaymentType> CREATOR = new Parcelable.Creator<PaymentType>() {

        @Override
        public PaymentType createFromParcel(Parcel source) {
            return new PaymentType(source);
        }

        @Override
        public PaymentType[] newArray(int size) {
            return new PaymentType[size];
        }

    };
}

