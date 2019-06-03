package sg.com.ehealthassist.ehealthassist.Models.Doctor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class DoctorDutyList {

    private String dutydate;
    List<Doctors> lstdoctors = new ArrayList<>();

    public DoctorDutyList (){}
    public DoctorDutyList(String dutydate,  List<Doctors> lstdoctors) {
        this.dutydate = dutydate;
        this.lstdoctors = lstdoctors;
    }
    public void setDutydate(String dutydate) {
        this.dutydate = dutydate;
    }

    public void setDoctors(List<Doctors> doctors) {
        this.lstdoctors = doctors;
    }

    public String getDutydate() {
        return this.dutydate;
    }

    public List<Doctors> getLstDoctors() {
        return this.lstdoctors;
    }

    public void addDoctor(int dr_id, String name) {
        Doctors new_dr = new Doctors();
        new_dr.setDr_id(dr_id);
        new_dr.setDr_name(name);
        this.lstdoctors.add(new_dr);
    }
}
