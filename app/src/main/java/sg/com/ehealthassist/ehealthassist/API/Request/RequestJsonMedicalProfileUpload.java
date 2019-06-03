package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class RequestJsonMedicalProfileUpload {
    int ClinicId;
    String Nric;
    int NricType;
    String FullName;
    int Sex;
    String Nationality;
    String DateOfBirth;
    RequestAddress Address;
    RequestContact Contact;
    String Allergy;
    String relationship;
    int married_status;
    String hecode;
    int language;

    public RequestJsonMedicalProfileUpload() {}

    public RequestJsonMedicalProfileUpload(int clinicId, String nric, int nricType, String fullName, int sex, String nationality,
                                           String dateOfBirth, RequestAddress address, RequestContact contact, String allergy,String relationship,
                                           int married_status,String hecode,int language) {
        this.ClinicId = clinicId;
        this.Nric = nric;
        this.NricType = nricType;
        this.FullName = fullName;
        this.Sex = sex;
        this.Nationality = nationality;
        this.DateOfBirth = dateOfBirth;
        this.Address = address;
        this.Contact = contact;
        this.Allergy = allergy;
        this.relationship = relationship;
        this.married_status = married_status;
        this.hecode = hecode;
        this.language = language;
    }

    public int getClinicId() {
        return ClinicId;
    }

    public void setClinicId(int clinicId) {
        ClinicId = clinicId;
    }

    public String getNric() {
        return Nric;
    }

    public void setNric(String nric) {
        Nric = nric;
    }

    public int getNricType() {
        return NricType;
    }

    public void setNricType(int nricType) {
        NricType = nricType;
    }

    public String getFullName() {
        return FullName;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public int getSex() {
        return Sex;
    }

    public void setSex(int sex) {
        Sex = sex;
    }

    public String getNationality() {
        return Nationality;
    }

    public void setNationality(String nationality) {
        Nationality = nationality;
    }

    public String getDateOfBirth() {
        return DateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        DateOfBirth = dateOfBirth;
    }

    public RequestAddress getAddress() {
        return this.Address;
    }

    public void setAddress(RequestAddress address) {
        this.Address = address;
    }

    public RequestContact getContact() {
        return this.Contact;
    }

    public void setContact(RequestContact contact) {
        this.Contact = contact;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public int getMarried_status() {
        return married_status;
    }

    public void setMarried_status(int married_status) {
        this.married_status = married_status;
    }

    public String getAllergy() {
        return Allergy;
    }

    public String getHecode() {
        return hecode;
    }

    public void setHecode(String hecode) {
        this.hecode = hecode;
    }

    public int getLanguage() {
        return language;
    }

    public void setLanguage(int language) {
        this.language = language;
    }

    public void createAddress(String blockNo, String street, String building, String unitNo, String postalCode,
                              String address1, String address2, String address3, String address4){
        RequestAddress new_address=new RequestAddress( blockNo,  street,  building,  unitNo,  postalCode,address1,address2,address3,address4);
        setAddress(new_address);

    }
    public void createContact(String mobile, String tel1, String email,String countryisocode){
        RequestContact new_contact=new RequestContact(  mobile,  tel1,  email,countryisocode);
        setContact(new_contact);
    }

    public void setAllergy(String allergy) {
        Allergy = allergy;
    }

    public String ObjecttoJson(RequestJsonMedicalProfileUpload dataobject){
        //String json=gson.toJson(dataobject);
        String json="{" + "\"ClinicId\":"+ ClinicId +","+
                "\"Nric\":\""+ Nric +"\","+
                "\"NricType\":"+ NricType +","+
                "\"FullName\":\""+ FullName +"\","+
                "\"Sex\":"+ Sex +","+
                "\"Nationality\":\""+ Nationality +"\","+
                "\"DateOfBirth\":\""+ DateOfBirth +"\","+
                "\"Address\":"+ Address.ObjecttoJson(getAddress()) +","+
                "\"Contact\":"+ Contact.ObjecttoJson(getContact()) +","+
                "\"Allergy\":\""+ Allergy +"\","+
                "\"Relationship\":\""+ relationship +"\","+
                "\"Marital\":\"" + married_status + "\"," +
                "\"Language\":\"" + language + "\"," +
                "\"HECode\":\"" + hecode + "\"" +

                "}";


        return json;
    }

    public JSONObject StringtoJsonObject(String requeststr){
        JSONObject json=null;
        try {
            json=new JSONObject(requeststr);
        }catch (Exception ex){
            ex.toString();
        }
        return json;
    }
}

class RequestAddress{
    public String BlockNo="";
    public  String Street="";
    public String Building="";
    public String UnitNo="";
    public String PostalCode="0";
    public String Address1,Address2,Address3,Address4 = "";

    public RequestAddress(String blockNo, String street, String building, String unitNo,
                          String postalCode,String address1,String address2,String address3,
                          String address4 ) {
        BlockNo = blockNo;
        Street = street;
        Building = building;
        UnitNo = unitNo;
        PostalCode = postalCode;
        Address1 = address1;
        Address2 = address2;
        Address3 = address3;
        Address4 = address4;
    }
    public String ObjecttoJson(RequestAddress dataobject){

        //String json=gson.toJson(dataobject);
        String json="{" +
                "\"BlockNo\":\""+ BlockNo +"\","+
                "\"Street\":\""+ Street +"\","+
                "\"Building\":\""+ Building +"\","+
                "\"UnitNo\":\""+ UnitNo +"\","+
                "\"PostalCode\":\""+ PostalCode +"\","+
                "\"Address1\":\""+ Address1 +"\","+
                "\"Address2\":\""+ Address2 +"\","+
                "\"Address3\":\""+ Address3 +"\","+
                "\"Address4\":\""+ Address4 +"\""+

                "}";


        return json;
    }

    public JSONObject StringtoJsonObject(String requeststr){
        JSONObject json=null;
        try {
            json=new JSONObject(requeststr);
        }catch (Exception ex){
            ex.toString();
        }
        return json;
    }
}

class RequestContact{
    public String Mobile="0";
    public String Tel1="0";
    public String Email="";
    public String countryisocode="";

    public RequestContact(String mobile, String tel1, String email,String countryisocode) {
        Mobile = mobile;
        Tel1 = tel1;
        Email = email;
        this.countryisocode = countryisocode;
    }
    public String ObjecttoJson(RequestContact dataobject){

        String json="{" +
                "\"Mobile\":"+ (Mobile.equals("")?0:Mobile)  +","+
                "\"Tel1\":"+ (Tel1.equals("")?0:Tel1) +","+
                "\"Email\":\""+ Email +"\","+
                "\"CountryISOCode\":\"" + countryisocode + "\""+
                "}";


        return json;
    }

    public JSONObject StringtoJsonObject(String requeststr){
        JSONObject json=null;
        try {
            json=new JSONObject(requeststr);
        }catch (Exception ex){
            ex.toString();
        }
        return json;
    }
}

