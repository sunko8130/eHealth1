package sg.com.ehealthassist.ehealthassist.Models.Medical;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class MedicalDispense  implements Parcelable {
    String memberid="";
    String Nric="";
    String Nrictype="";
    String DOB="";
    String QueueID="";
    String VisitNo="";
    String VisitDate="";
    String ClinicId="";
   // String isReminder="";
   // String startDate="";
  //  String starttime="";
  //  String numofDays="";
    String ClinicName="";
    String patientName="";


    public MedicalDispense(){}
    public MedicalDispense(String memberid, String nric, String nrictype, String DOB,
                           String queueID, String visitNo, String visitDate, String clinicId,
                           String clinicname,String patientname) {
        this.memberid = memberid;
        this.Nric = nric;
        this.Nrictype = nrictype;
        this.DOB = DOB;
        this.QueueID = queueID;
        this.VisitNo = visitNo;
        this.VisitDate = visitDate;
        this.ClinicId = clinicId;
       // this.isReminder = isReminder;
       // this.startDate = startDate;
       // this.starttime = starttime;
       // this.numofDays = numofDays;
        this.ClinicName = clinicname;
        this.patientName = patientname;

    }
    public MedicalDispense(String memberid, String nric, String nrictype, String DOB,
                           String queueID, String visitNo, String visitDate, String clinicId,String patientname
    ) {
        this.memberid = memberid;
        this.Nric = nric;
        this.Nrictype = nrictype;
        this.DOB = DOB;
        this.QueueID = queueID;
        this.VisitNo = visitNo;
        this.VisitDate = visitDate;
        this.ClinicId = clinicId;
        this.patientName = patientname;

    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getNric() {
        return Nric;
    }

    public void setNric(String nric) {
        Nric = nric;
    }

    public String getNrictype() {
        return Nrictype;
    }

    public void setNrictype(String nrictype) {
        Nrictype = nrictype;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getQueueID() {
        return QueueID;
    }

    public void setQueueID(String queueID) {
        QueueID = queueID;
    }

    public String getVisitNo() {
        return VisitNo;
    }

    public void setVisitNo(String visitNo) {
        VisitNo = visitNo;
    }

    public String getVisitDate() {
        return VisitDate;
    }

    public void setVisitDate(String visitDate) {
        VisitDate = visitDate;
    }

    public String getClinicId() {
        return ClinicId;
    }

    public void setClinicId(String clinicId) {
        ClinicId = clinicId;
    }

    public String getClinicName() {
        return ClinicName;
    }

    public void setClinicName(String clinicName) {
        ClinicName = clinicName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

   /* public String getIsReminder() {
        return isReminder;
    }

    public void setIsReminder(String isReminder) {
        this.isReminder = isReminder;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getNumofDays() {
        return numofDays;
    }

    public void setNumofDays(String numofDays) {
        this.numofDays = numofDays;
    }
*/
    //region Parcelable
    public MedicalDispense(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.memberid = in.readString();
        this.Nric = in.readString();
        this.Nrictype = in.readString();
        this.DOB = in.readString();
        this.QueueID = in.readString();
        this.VisitNo = in.readString();
        this.VisitDate = in.readString();
        this.ClinicId = in.readString();
     /*   this.isReminder = in.readString();
        this.startDate = in.readString();
        this.starttime = in.readString();
        this.numofDays = in.readString();*/
        this.ClinicName = in.readString();
        this.patientName = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(memberid);
        dest.writeString(Nric);
        dest.writeString(Nrictype);
        dest.writeString(DOB);
        dest.writeString(QueueID);
        dest.writeString(VisitNo);
        dest.writeString(VisitDate);
        dest.writeString(ClinicId);
       /* dest.writeString(isReminder);
        dest.writeString(startDate);
        dest.writeString(starttime);
        dest.writeString(numofDays);*/
        dest.writeString(ClinicName);
        dest.writeString(patientName);
    }

    public static Creator<MedicalDispense> CREATOR = new Parcelable.Creator<MedicalDispense>() {

        @Override
        public MedicalDispense createFromParcel(Parcel source) {
            return new MedicalDispense(source);
        }

        @Override
        public MedicalDispense[] newArray(int size) {
            return new MedicalDispense[size];
        }

    };
    //endregion

}
