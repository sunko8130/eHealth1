package sg.com.ehealthassist.ehealthassist.Models.Haze;

import org.parceler.Parcel;

/**
 * Created by thazinhlaing on 1/7/17.
 */
@Parcel
public class Reading {
    String type;
    String value;

    public Reading(){}


    public Reading(String type, String value) {
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
}

