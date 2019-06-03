package sg.com.ehealthassist.ehealthassist.Models.Appointment;

/**
 * Created by User on 6/30/2017.
 */

public class ApptAlarmLog {
    String long_book_id = "";
    String short_book_id = "";
    Boolean _2hr,_1day,_2days,_1week = false;
    int alarm_id ;
    String alarm_unit="";// only for cancel;
    public ApptAlarmLog() {
    }

    public ApptAlarmLog(String long_book_id,String short_book_id, Boolean _2hr, Boolean _1day, Boolean _2days, Boolean _1week,int _alarm_id,String alarm_unit) {
        this.long_book_id = long_book_id;
        this.short_book_id = short_book_id;
        this._2hr = _2hr;
        this._1day = _1day;
        this._2days = _2days;
        this._1week = _1week;
        this.alarm_id = _alarm_id;
        this.alarm_unit = alarm_unit;
    }

    public String getLong_book_id() {
        return long_book_id;
    }

    public void setLong_book_id(String long_book_id) {
        this.long_book_id = long_book_id;
    }

    public Boolean is_2hr() {
        return _2hr;
    }

    public void set_2hr(Boolean _2hr) {
        this._2hr = _2hr;
    }

    public Boolean is_1day() {
        return _1day;
    }

    public void set_1day(Boolean _1day) {
        this._1day = _1day;
    }

    public Boolean is_2days() {
        return _2days;
    }

    public void set_2days(Boolean _2days) {
        this._2days = _2days;
    }

    public Boolean is_1week() {
        return _1week;
    }

    public void set_1week(Boolean _1week) {
        this._1week = _1week;
    }

    public int getAlarm_id() {
        return alarm_id;
    }

    public void setAlarm_id(int alarm_id) {
        this.alarm_id = alarm_id;
    }

    public String getAlarm_unit() {
        return alarm_unit;
    }

    public void setAlarm_unit(String alarm_unit) {
        this.alarm_unit = alarm_unit;// json string
    }

    public String getShort_book_id() {
        return short_book_id;
    }

    public void setShort_book_id(String short_book_id) {
        this.short_book_id = short_book_id;
    }
}
