package sg.com.ehealthassist.ehealthassist.Models.Clinic;

/**
 * Created by User on 6/29/2017.
 */

public class Clinic_Services {
    int clinic_id;
    String serivices_name;

    public Clinic_Services() {
    }

    public Clinic_Services(int clinic_id, String serivices_name) {
        this.clinic_id = clinic_id;
        this.serivices_name = serivices_name;
    }

    public int getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(int clinic_id) {
        this.clinic_id = clinic_id;
    }

    public String getSerivices_name() {
        return serivices_name;
    }

    public void setSerivices_name(String serivices_name) {
        this.serivices_name = serivices_name;
    }
}
