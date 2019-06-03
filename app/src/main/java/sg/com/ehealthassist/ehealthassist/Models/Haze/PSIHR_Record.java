package sg.com.ehealthassist.ehealthassist.Models.Haze;

import org.parceler.Parcel;

/**
 * Created by thazinhlaing on 1/7/17.
 */
@Parcel
public class PSIHR_Record {
    String timestamp;
    Reading[] reading ;

    public PSIHR_Record(){}
    public PSIHR_Record(String timestamp, Reading[] reading) {
        this.timestamp = timestamp;
        this.reading = reading;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Reading[] getReading() {
        return reading;
    }

    public void setReading(Reading[] reading) {
        this.reading = reading;
    }
}

