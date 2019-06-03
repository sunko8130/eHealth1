package sg.com.ehealthassist.ehealthassist.Models.Queue;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thazinhlaing on 2/7/17.
 */

public class QueuelistView implements Parcelable {

    int statuscode;
    String requestid;
    String patientnric;
    String patientdob;
    String patientname;
    String requestdatetime;
    String qmessage;
    int clinicid;
    String clinicname;
    String queueno;
    String currentqueue;
    String qstatus;
    String rejectreason;
    int patientnric_type;

    public QueuelistView(){}
    public QueuelistView( int statuscode,String requestid, String patientnric,
                          String patientdob, String patientname,
                          String requestdatetime, String qmessage,
                          int clinicid, String clinicname, String  queueno,
                          String currentqueue,String qstatus,String rejectreason,int patientnric_type) {

        this.statuscode = statuscode;
        this.requestid = requestid;
        this.patientnric = patientnric;
        this.patientdob = patientdob;
        this.patientname = patientname;
        this.requestdatetime = requestdatetime;
        this.qmessage = qmessage;
        this.clinicid = clinicid;
        this.clinicname = clinicname;
        this.queueno = queueno;
        this.currentqueue = currentqueue;
        this.qstatus =qstatus;
        this.rejectreason = rejectreason;
        this.patientnric_type = patientnric_type;
    }

    public int getStatuscode() {
        return statuscode;
    }

    public void setStatuscode(int statuscode) {
        this.statuscode = statuscode;
    }

    public String  getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getPatientnric() {
        return patientnric;
    }

    public void setPatientnric(String patientnric) {
        this.patientnric = patientnric;
    }

    public String getPatientdob() {
        return patientdob;
    }

    public void setPatientdob(String patientdob) {
        this.patientdob = patientdob;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    public String getRequestdatetime() {
        return requestdatetime;
    }

    public void setRequestdatetime(String requestdatetime) {
        this.requestdatetime = requestdatetime;
    }

    public String getQmessage() {
        return qmessage;
    }

    public void setQmessage(String qmessage) {
        this.qmessage = qmessage;
    }

    public int getClinicid() {
        return clinicid;
    }

    public void setClinicid(int clinicid) {
        this.clinicid = clinicid;
    }

    public String getClinicname() {
        return clinicname;
    }

    public void setClinicname(String clinicname) {
        this.clinicname = clinicname;
    }

    public String getQueueno() {
        return queueno;
    }

    public void setQueueno(String  queueno) {
        this.queueno = queueno;
    }

    public String getCurrentqueue() {
        return currentqueue;
    }

    public void setCurrentqueue(String currentqueue) {
        this.currentqueue = currentqueue;
    }

    public String getQstatus() {
        return qstatus;
    }

    public void setQstatus(String qstatus) {
        this.qstatus = qstatus;
    }

    public String getRejectreason() {
        return rejectreason;
    }

    public void setRejectreason(String rejectreason) {
        this.rejectreason = rejectreason;
    }

    public int getPatientnric_type() {
        return patientnric_type;
    }

    public void setPatientnric_type(int patientnric_type) {
        this.patientnric_type = patientnric_type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.statuscode);
        dest.writeString(this.requestid);
        dest.writeString(this.patientnric);
        dest.writeString(this.patientdob);
        dest.writeString(this.patientname);
        dest.writeString(this.requestdatetime);
        dest.writeString(this.qmessage);
        dest.writeInt(this.clinicid);
        dest.writeString(this.clinicname);
        dest.writeString(this.queueno);
        dest.writeString(this.currentqueue);
        dest.writeString(this.qstatus);
        dest.writeString(this.rejectreason);
        dest.writeInt(this.patientnric_type);
    }

    protected QueuelistView(Parcel in) {
        this.statuscode = in.readInt();
        this.requestid = in.readString();
        this.patientnric = in.readString();
        this.patientdob = in.readString();
        this.patientname = in.readString();
        this.requestdatetime = in.readString();
        this.qmessage = in.readString();
        this.clinicid = in.readInt();
        this.clinicname = in.readString();
        this.queueno = in.readString();
        this.currentqueue = in.readString();
        this.qstatus = in.readString();
        this.rejectreason = in.readString();
        this.patientnric_type = in.readInt();

    }

    public static final Parcelable.Creator<QueuelistView> CREATOR = new Parcelable.Creator<QueuelistView>() {
        @Override
        public QueuelistView createFromParcel(Parcel source) {
            return new QueuelistView(source);
        }

        @Override
        public QueuelistView[] newArray(int size) {
            return new QueuelistView[size];
        }
    };
}

