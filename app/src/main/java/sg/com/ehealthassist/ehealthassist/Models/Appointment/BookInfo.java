package sg.com.ehealthassist.ehealthassist.Models.Appointment;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 6/30/2017.
 */

public class BookInfo implements Parcelable {
    public int id = 0;
    public int doctor_id = 0;
    public int clinic_id = 0;
    public String clinic_name = "";
    public String doctor_name = "";
    public String short_id = "";
    public String long_id = "";
    public String book_date = "";
    public String doc_profilepic = "";
    public String doc_remark = "";
    public String requestor_nric = "";
    public String requestor_nrictype = "";
    public String requestor_name = "";
    public String app_status;
    public String memberid = "";
    public String requestorphone = "";
    public String requestorMessage = "";
    public String apptsession = "";
    public String apptsessiontime = "";
    public String apptTime = "";
    public String patientremark = "";
    public String app_status_description = "";
    public String appt_type = "" ;

    public BookInfo() {
    }
    public BookInfo(int id, int doctor_id, int clinic_id, String clinic_name, String doctor_name, String short_id, String long_id,
                    String book_date, String doc_profilepic, String remark,String requestor_nric,String requestor_name,
                    String appt_status,String requestor_nrictype,String memberid,String requestorphone,String requestorMessage,
                    String apptsession,String apptsessiontime,String apptTime,String patientremark,String app_status_description,
                    String appt_type) {
        this.id = id;
        this.doctor_id = doctor_id;
        this.clinic_id = clinic_id;
        this.clinic_name = clinic_name;
        this.doctor_name = doctor_name;
        this.short_id = short_id;
        this.long_id = long_id;
        this.book_date = book_date;
        this.doc_profilepic = doc_profilepic;
        this.doc_remark = remark;
        this.requestor_nric = requestor_nric;
        this.requestor_name = requestor_name;
        this.app_status = appt_status;
        this.requestor_nrictype = requestor_nrictype;
        this.memberid = memberid;
        this.requestorphone = requestorphone;
        this.requestorMessage = requestorMessage;
        this.apptsession = apptsession;
        this.apptsessiontime = apptsessiontime;
        this.apptTime = apptTime;
        this.patientremark = patientremark;
        this.app_status_description = app_status_description;
        this.appt_type = appt_type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDoctor_id() {
        return doctor_id;
    }

    public void setDoctor_id(int doctor_id) {
        this.doctor_id = doctor_id;
    }

    public int getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(int clinic_id) {
        this.clinic_id = clinic_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getShort_id() {
        return short_id;
    }

    public void setShort_id(String short_id) {
        this.short_id = short_id;
    }

    public String getLong_id() {
        return long_id;
    }

    public void setLong_id(String long_id) {
        this.long_id = long_id;
    }

    public String getBook_date() {
        return book_date;
    }

    public void setBook_date(String book_date) {
        this.book_date = book_date;
    }

    public String getDoc_profilepic() {
        return doc_profilepic;
    }

    public void setDoc_profilepic(String doc_profilepic) {
        this.doc_profilepic = doc_profilepic;
    }

    public String getDoc_remark() {
        return doc_remark;
    }

    public void setDoc_remark(String doc_remark) {
        this.doc_remark = doc_remark;
    }

    public String getClinic_name() {
        return clinic_name;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public String getRequestor_nric() {
        return requestor_nric;
    }

    public void setRequestor_nric(String requestor_nric) {
        this.requestor_nric = requestor_nric;
    }

    public String getRequestor_name() {
        return requestor_name;
    }

    public void setRequestor_name(String requestor_name) {
        this.requestor_name = requestor_name;
    }

    public String getApp_status() {
        return app_status;
    }

    public void setApp_status(String app_status) {
        this.app_status = app_status;
    }

    public String getRequestor_nrictype() {
        return requestor_nrictype;
    }

    public void setRequestor_nrictype(String requestor_nrictype) {
        this.requestor_nrictype = requestor_nrictype;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getRequestorphone() {
        return requestorphone;
    }

    public void setRequestorphone(String requestorphone) {
        this.requestorphone = requestorphone;
    }

    public String getRequestorMessage() {
        return requestorMessage;
    }

    public void setRequestorMessage(String requestorMessage) {
        this.requestorMessage = requestorMessage;
    }

    public String getApptsession() {
        return apptsession;
    }

    public void setApptsession(String apptsession) {
        this.apptsession = apptsession;
    }

    public String getApptsessiontime() {
        return apptsessiontime;
    }

    public void setApptsessiontime(String apptsessiontime) {
        this.apptsessiontime = apptsessiontime;
    }

    public String getApptTime() {
        return apptTime;
    }

    public void setApptTime(String apptTime) {
        this.apptTime = apptTime;
    }

    public String getPatientremark() {
        return patientremark;
    }

    public void setPatientremark(String patientremark) {
        this.patientremark = patientremark;
    }


    public String getApp_status_description() {
        return app_status_description;
    }

    public void setApp_status_description(String app_status_description) {
        this.app_status_description = app_status_description;
    }

    public String getAppt_type() {
        return appt_type;
    }

    public void setAppt_type(String appt_type) {
        this.appt_type = appt_type;
    }

    //region Parcelable()
    public BookInfo(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readInt();
        this.doctor_id = in.readInt();
        this.clinic_id = in.readInt();
        this.doctor_name = in.readString();
        this.short_id = in.readString();
        this.long_id = in.readString();
        this.book_date = in.readString();
        this.doc_profilepic = in.readString();
        this.doc_remark = in.readString();
        this.clinic_name = in.readString();
        this.requestor_nric = in.readString();
        this.requestor_name = in.readString();
        this.app_status = in.readString();
        this.requestor_nrictype = in.readString();
        this.memberid = in.readString();
        this.requestorphone = in.readString();
        this.requestorMessage = in.readString();
        this.apptsession = in.readString();
        this.apptsessiontime = in.readString();
        this.apptTime = in.readString();
        this.patientremark = in.readString();
        this.app_status_description = in.readString();
        this.appt_type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(doctor_id);
        dest.writeInt(clinic_id);
        dest.writeString(doctor_name);
        dest.writeString(short_id);
        dest.writeString(long_id);
        dest.writeString(book_date);
        dest.writeString(doc_profilepic);
        dest.writeString(doc_remark);
        dest.writeString(clinic_name);
        dest.writeString(requestor_nric);
        dest.writeString(requestor_name);
        dest.writeString(app_status);
        dest.writeString(requestor_nrictype);
        dest.writeString(memberid);
        dest.writeString(requestorphone);
        dest.writeString(requestorMessage);
        dest.writeString(apptsession);
        dest.writeString(apptsessiontime);
        dest.writeString(apptTime);
        dest.writeString(patientremark);
        dest.writeString(app_status_description);
        dest.writeString(appt_type);
    }

    public static Creator<BookInfo> CREATOR = new Creator<BookInfo>() {

        @Override
        public BookInfo createFromParcel(Parcel source) {
            return new BookInfo(source);
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }

    };
    //endregion
}
