package sg.com.ehealthassist.ehealthassist.Models.Clinic;

import java.util.List;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class OpeningHours {
    String open_day;
    List<String> open_time;

    public OpeningHours(String open_day, List<String> open_time) {
        this.open_day = open_day;
        this.open_time = open_time;
    }

    public String getOpen_day() {
        return open_day;
    }

    public void setOpen_day(String open_day) {
        this.open_day = open_day;
    }

    public List<String> getOpen_time() {
        return open_time;
    }

    public void setOpen_time(List<String> open_time) {
        this.open_time = open_time;
    }
}

