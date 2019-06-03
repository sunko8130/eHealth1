package sg.com.ehealthassist.ehealthassist.Messages;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 9/27/2017.
 */

public class Messages  implements Parcelable {
    int id;
    long clinicid;
    long memberid;
    String nric;
    String nrictype;
    String dob;
    String message;
    String createdate;
    int status;
    String messageReply;
    String downloaded;
    String patientname;
    long mobileno;


    public Messages(){

    }
    public Messages(int id, long clinicid, long memberid, String nric, String nrictype, String dob,
                    String message, String createdate, int status,String messageReply, String downloaded,
                    String patientname,long mobileno
            ) {
        this.id = id;
        this.clinicid = clinicid;
        this.memberid = memberid;
        this.nric = nric;
        this.nrictype = nrictype;
        this.dob = dob;
        this.message = message;
        this.createdate = createdate;
        this.status = status;
        this.downloaded = downloaded;
        this.messageReply = messageReply;
        this.patientname = patientname;
        this.mobileno = mobileno;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getClinicid() {
        return clinicid;
    }

    public void setClinicid(long clinicid) {
        this.clinicid = clinicid;
    }

    public long getMemberid() {
        return memberid;
    }

    public void setMemberid(long memberid) {
        this.memberid = memberid;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getNrictype() {
        return nrictype;
    }

    public void setNrictype(String nrictype) {
        this.nrictype = nrictype;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(String downloaded) {
        this.downloaded = downloaded;
    }

    public String getMessageReply() {
        return messageReply;
    }

    public void setMessageReply(String messageReply) {
        this.messageReply = messageReply;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public long getMobileno() {
        return mobileno;
    }

    public void setMobileno(long mobileno) {
        this.mobileno = mobileno;
    }

    //region Parcelable()
    public Messages(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readInt();
        this.clinicid = in.readLong();
        this.memberid = in.readLong();
        this.nric = in.readString();
        this.nrictype = in.readString();
        this.dob = in.readString();
        this.message = in.readString();
        this.createdate = in.readString();
        this.status = in.readInt();
        this.downloaded = in.readString();
        this.messageReply = in.readString();
        this.patientname = in.readString();
        this.mobileno = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(clinicid);
        dest.writeLong(memberid);
        dest.writeString(nric);
        dest.writeString(nrictype);
        dest.writeString(dob);
        dest.writeString(message);
        dest.writeString(createdate);
        dest.writeInt(status);
        dest.writeString(downloaded);
        dest.writeString(messageReply);
        dest.writeString(patientname);
        dest.writeLong(mobileno);

    }

    public static Creator<Messages> CREATOR = new Creator<Messages>() {

        @Override
        public Messages createFromParcel(Parcel source) {
            return new Messages(source);
        }

        @Override
        public Messages[] newArray(int size) {
            return new Messages[size];
        }

    };
    //endregion
}
