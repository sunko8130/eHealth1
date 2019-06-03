package sg.com.ehealthassist.ehealthassist.Models.Appointment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class AppointmentSlot implements Parcelable {
    int id = 0;
    int clinicid = 0;
    String casdoctorid = "";
    String fullname = "";
    String Displayname = "";
    String profilepicture = "";
    String smc = "";
    int status = 1;
    String editor = "";
    String creator = "";
    String enableAppt = "false";
    List<String> availabeleSlots = new ArrayList<String>();
    List<String> apptSession = new ArrayList<String>();

    public AppointmentSlot() {
    }

    public AppointmentSlot(int id, int clinicid, String casdoctorid, String fullname, String displayname,
                           String profilepicture, String smc, int status, String editor, String creator, String enableAppt,
                           List<String> availabeleSlots,List<String> apptSession) {
        this.id = id;
        this.clinicid = clinicid;
        this.casdoctorid = casdoctorid;
        this.fullname = fullname;
        this.Displayname = displayname;
        this.profilepicture = profilepicture;
        this.smc = smc;
        this.status = status;
        this.editor = editor;
        this.creator = creator;
        this.enableAppt = enableAppt;
        this.availabeleSlots = availabeleSlots;
        this.apptSession = apptSession;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClinicid() {
        return clinicid;
    }

    public void setClinicid(int clinicid) {
        this.clinicid = clinicid;
    }

    public String getCasdoctorid() {
        return casdoctorid;
    }

    public void setCasdoctorid(String casdoctorid) {
        this.casdoctorid = casdoctorid;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getDisplayname() {
        return Displayname;
    }

    public void setDisplayname(String displayname) {
        Displayname = displayname;
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

    public String getSmc() {
        return smc;
    }

    public void setSmc(String smc) {
        this.smc = smc;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEditor() {
        return editor;
    }

    public void setEditor(String editor) {
        this.editor = editor;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public List<String> getAvailabeleSlots() {
        return availabeleSlots;
    }

    public void setAvailabeleSlots(List<String> availabeleSlots) {
        this.availabeleSlots = availabeleSlots;
    }

    public String isEnableAppt() {
        return enableAppt;
    }

    public void setEnableAppt(String enableAppt) {
        this.enableAppt = enableAppt;
    }

    public List<String> getApptSession() {
        return apptSession;
    }

    public void setApptSession(List<String> apptSession) {
        this.apptSession = apptSession;
    }

    //region Parcelable()
    public AppointmentSlot(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readInt();
        this.clinicid = in.readInt();
        this.casdoctorid = in.readString();
        this.fullname = in.readString();
        this.Displayname = in.readString();
        this.profilepicture = in.readString();
        this.smc = in.readString();
        this.status = in.readInt();
        this.editor = in.readString();
        this.creator = in.readString();
        this.enableAppt = in.readString();
        this.availabeleSlots = in.readArrayList(null);
        this.apptSession = in.readArrayList(null);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(clinicid);
        dest.writeString(casdoctorid);
        dest.writeString(fullname);
        dest.writeString(Displayname);
        dest.writeString(profilepicture);
        dest.writeString(smc);
        dest.writeInt(status);
        dest.writeString(editor);
        dest.writeString(creator);
        dest.writeList(availabeleSlots);
        dest.writeList(apptSession);
    }

    public static Creator<AppointmentSlot> CREATOR = new Parcelable.Creator<AppointmentSlot>() {

        @Override
        public AppointmentSlot createFromParcel(Parcel source) {
            return new AppointmentSlot(source);
        }

        @Override
        public AppointmentSlot[] newArray(int size) {
            return new AppointmentSlot[size];
        }

    };
    //endregion

}
