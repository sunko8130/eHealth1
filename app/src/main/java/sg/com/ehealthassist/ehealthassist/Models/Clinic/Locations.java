package sg.com.ehealthassist.ehealthassist.Models.Clinic;

/**
 * Created by User on 6/29/2017.
 */

public class Locations {
    String _lat="0",_lng="0";

    public Locations() {
    }

    public Locations(String _lat, String _lng) {
        this._lat = _lat;
        this._lng = _lng;
    }

    public String get_lat() {
        return _lat;
    }

    public void set_lat(String _lat) {
        this._lat = _lat;
    }

    public String get_lng() {
        return _lng;
    }

    public void set_lng(String _lng) {
        this._lng = _lng;
    }
}
