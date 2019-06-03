package sg.com.ehealthassist.ehealthassist.Models.Services;

/**
 * Created by User on 6/29/2017.
 */

public class Services {
    int id;
    String clinic_type;
    String speciality;

    public Services(){

    }
    public Services(int id, String clinic_type, String speciality) {
        this.id = id;
        this.clinic_type = clinic_type;
        this.speciality = speciality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getClinic_type() {
        return clinic_type;
    }

    public void setClinic_type(String clinic_type) {
        this.clinic_type = clinic_type;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
