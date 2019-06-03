package sg.com.ehealthassist.ehealthassist.Models.Profile;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by User on 6/30/2017.
 */

public class MedicalProfile  implements Parcelable  {
    int id ;
    int clinic_id;
    int nric_type =-1;
    String nric ="";
    String nric_name = "";
    int gender=-1;
    int language = -1;
    String nationality="";
    String dob="";
    String block_no="";
    String street="";
    String building_name="";
    String unit_no="";
    String postal_code="";
    String tel1="";
    String tel2="";
    String email="";
    String allergy="";
    String memberid="";
    int relation=-1,married_staus=-1;
    int member;
    int flag_upload;
    String tel1_iso = "";
    int tel1_code = 0;
    String address1= "";
    String address2="";
    String address3="";
    String address4="";

    public MedicalProfile(){}
    public MedicalProfile(int id, int clinic_id, int nric_type, String nric, String nric_name, int gender, String nationality,
                          String dob, String block_no, String street, String building_name, String unit_no, String postal_code,
                          String tel1, String tel2, String email, String allergy,String memberid,int relation,int member,
                          int flag_upload,int married_staus,int language,String tel1_iso,int tel1_code,String address1,String address2,
                          String address3,String address4) {
        this.id = id;
        this.clinic_id = clinic_id;
        this.nric_type = nric_type;
        this.nric = nric;
        this.nric_name = nric_name;
        this.gender = gender;
        this.nationality = nationality;
        this.dob = dob;
        this.block_no = block_no;
        this.street = street;
        this.building_name = building_name;
        this.unit_no = unit_no;
        this.postal_code = postal_code;
        this.tel1 = tel1;
        this.tel2 = tel2;
        this.email = email;
        this.allergy = allergy;
        this.memberid = memberid;
        this.relation = relation;
        this.member = member;
        this.flag_upload = flag_upload;
        this.married_staus = married_staus;
        this.language = language;
        this.tel1_iso = tel1_iso;
        this.tel1_code = tel1_code;
        this.tel1_iso = tel1_iso;
        this.address1 = address1;
        this.address2 = address2;
        this.address3 = address3;
        this.address4 = address4;


    }
    //region Getter & Setter

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClinic_id() {
        return clinic_id;
    }

    public void setClinic_id(int clinic_id) {
        this.clinic_id = clinic_id;
    }

    public int getNric_type() {
        return nric_type;
    }

    public void setNric_type(int nric_type) {
        this.nric_type = nric_type;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getNric_name() {
        return nric_name;
    }

    public void setNric_name(String nric_name) {
        this.nric_name = nric_name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getBlock_no() {
        return block_no;
    }

    public void setBlock_no(String block_no) {
        this.block_no = block_no;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getBuilding_name() {
        return building_name;
    }

    public void setBuilding_name(String building_name) {
        this.building_name = building_name;
    }

    public String getUnit_no() {
        return unit_no;
    }

    public void setUnit_no(String unit_no) {
        this.unit_no = unit_no;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getTel1() {
        return tel1;
    }

    public void setTel1(String tel1) {
        this.tel1 = tel1;
    }

    public String getTel2() {
        return tel2;
    }

    public void setTel2(String tel2) {
        this.tel2 = tel2;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAllergy() {
        return allergy;
    }

    public void setAllergy(String allergy) {
        this.allergy = allergy;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public int getMember() {
        return member;
    }

    public void setMember(int member) {
        this.member = member;
    }

    public int getFlag_upload() {
        return flag_upload;
    }

    public void setFlag_upload(int flag_upload) {
        this.flag_upload = flag_upload;
    }

    public int getMarried_staus() {
        return married_staus;
    }

    public void setMarried_staus(int married_staus) {
        this.married_staus = married_staus;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public String getTel1_iso() {
        return tel1_iso;
    }

    public void setTel1_iso(String tel1_iso) {
        this.tel1_iso = tel1_iso;
    }

    public int getTel1_code() {
        return tel1_code;
    }

    public void setTel1_code(int tel1_code) {
        this.tel1_code = tel1_code;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getAddress4() {
        return address4;
    }

    public void setAddress4(String address4) {
        this.address4 = address4;
    }
    //endregion
    //region Parcelable
    public MedicalProfile(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readInt();
        this.clinic_id = in.readInt();
        this.nric_type = in.readInt();
        this.nric = in.readString();
        this.nric_name = in.readString();
        this.gender = in.readInt();
        this.nationality = in.readString();
        this.dob = in.readString();
        this.block_no = in.readString();
        this.street = in.readString();
        this.building_name = in.readString();
        this.unit_no = in.readString();
        this.postal_code = in.readString();
        this.tel1 = in.readString();
        this.tel2 = in.readString();
        this.email = in.readString();
        this.allergy = in.readString();
        this.memberid = in.readString();
        this.relation = in.readInt();
        this.member = in.readInt();
        this.flag_upload = in.readInt();
        this.married_staus = in.readInt();
        this.language = in.readInt();
        this.tel1_iso = in.readString();
        this.tel1_code = in.readInt();
        this.address1 = in.readString();
        this.address2 = in.readString();
        this.address3 = in.readString();
        this.address4 = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(clinic_id);
        dest.writeInt(nric_type);
        dest.writeString(nric);
        dest.writeString(nric_name);
        dest.writeInt(gender);
        dest.writeString(nationality);
        dest.writeString(dob);
        dest.writeString(block_no);
        dest.writeString(street);
        dest.writeString(building_name);
        dest.writeString(unit_no);
        dest.writeString(postal_code);
        dest.writeString(tel1);
        dest.writeString(tel2);
        dest.writeString(email);
        dest.writeString(allergy);
        dest.writeString(memberid);
        dest.writeInt(relation);
        dest.writeInt(member);
        dest.writeInt(flag_upload);
        dest.writeInt(married_staus);
        dest.writeInt(language);
        dest.writeInt(tel1_code);
        dest.writeString(tel1_iso);
        dest.writeString(address1);
        dest.writeString(address2);
        dest.writeString(address3);
        dest.writeString(address4);

    }

    public static Parcelable.Creator<MedicalProfile> CREATOR = new Parcelable.Creator<MedicalProfile>() {

        @Override
        public MedicalProfile createFromParcel(Parcel source) {
            return new MedicalProfile(source);
        }

        @Override
        public MedicalProfile[] newArray(int size) {
            return new MedicalProfile[size];
        }

    };
    //endregion
}
