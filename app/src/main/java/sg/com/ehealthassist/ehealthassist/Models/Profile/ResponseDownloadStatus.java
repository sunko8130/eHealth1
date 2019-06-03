package sg.com.ehealthassist.ehealthassist.Models.Profile;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class ResponseDownloadStatus implements Parcelable {

    int id =0;
    int clinicid = 0;
    String nric = "";
    String nrictype = "";
    String dob = "";
    String memberid = "";
    String modifiedDate = "";
    String downloaded = "";
    String hecode = "";
    String nricname = "";
    int gender=-1;

    public ResponseDownloadStatus(){}
    public ResponseDownloadStatus(int id, int clinic, String nric, String nrictype, String dob,
                                  String memberid, String modifiedDate, String downloaded, String hecode,
                                  String nricname,int gender) {
        this.id = id;
        this.clinicid = clinic;
        this.nric = nric;
        this.nrictype = nrictype;
        this.dob = dob;
        this.memberid = memberid;

        this.modifiedDate = modifiedDate;
        this.downloaded = downloaded;
        this.hecode = hecode;
        this.nricname = nricname;
        this.gender = gender;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClinic() {
        return clinicid;
    }

    public void setClinic(int clinic) {
        this.clinicid = clinic;
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

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        modifiedDate = modifiedDate;
    }

    public String getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(String downloaded) {
        this.downloaded = downloaded;
    }

    public String getHecode() {
        return hecode;
    }

    public void setHecode(String hecode) {
        this.hecode = hecode;
    }

    public String getNricname() {
        return nricname;
    }

    public void setNricname(String nricname) {
        this.nricname = nricname;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    //region Parcel
    public ResponseDownloadStatus(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readInt();
        this.clinicid = in.readInt();
        this.nric = in.readString();
        this.nrictype = in.readString();
        this.dob = in.readString();
        this.memberid = in.readString();
        this.modifiedDate = in.readString();
        this.downloaded = in.readString();
        this.hecode = in.readString();
        this.nricname = in.readString();
        this.gender = in.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.id);
        out.writeInt(this.clinicid);
        out.writeString(this.nric);
        out.writeString(this.nrictype);
        out.writeString(this.dob);
        out.writeString(this.memberid);
        out.writeString(this.modifiedDate);
        out.writeString(this.downloaded);
        out.writeString(this.hecode);
        out.writeString(this.nricname);
        out.writeInt(this.gender);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ResponseDownloadStatus createFromParcel(Parcel in) {
            return new ResponseDownloadStatus(in);
        }

        public ResponseDownloadStatus[] newArray(int size) {
            return new ResponseDownloadStatus[size];
        }
    };
    //endregion

}

