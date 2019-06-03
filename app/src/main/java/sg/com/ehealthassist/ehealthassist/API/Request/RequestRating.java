package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by User on 9/12/2017.
 */

public class RequestRating {
    String clinic_code="",memberid="",q1="0",q2="0",q3="0",q4="0",q5="0",q6="0";

    public RequestRating(){}

    public String getClinic_code() {
        return clinic_code;
    }

    public void setClinic_code(String clinic_code) {
        this.clinic_code = clinic_code;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getQ1() {
        return q1;
    }

    public void setQ1(String q1) {
        this.q1 = q1;
    }

    public String getQ2() {
        return q2;
    }

    public void setQ2(String q2) {
        this.q2 = q2;
    }

    public String getQ3() {
        return q3;
    }

    public void setQ3(String q3) {
        this.q3 = q3;
    }

    public String getQ4() {
        return q4;
    }

    public void setQ4(String q4) {
        this.q4 = q4;
    }

    public String getQ5() {
        return q5;
    }

    public void setQ5(String q5) {
        this.q5 = q5;
    }

    public String getQ6() {
        return q6;
    }

    public void setQ6(String q6) {
        this.q6 = q6;
    }

    public RequestRating(String clinic_code, String memberid, String q1, String q2, String q3, String q4, String q5, String q6) {
        this.clinic_code = clinic_code;
        this.memberid = memberid;
        this.q1 = q1;
        this.q2 = q2;
        this.q3 = q3;
        this.q4 = q4;
        this.q5 = q5;
        this.q6 = q6;

    }
    public String objecttoJson(){
        String otpjson = "{\"Clinic_code\":\""+ clinic_code +"\",\"MemberID\":\""+memberid+"\",\"Q1\":"+q1+",\"Q2\":" +
                q2+",\"Q3\":"+q3+",\"Q4\":"+q4+",\"Q5\":"+q5+",\"Q6\":"+q6 +"}";

        return otpjson;
    }
    public JSONObject StringtoJsonObject(String otpstr){
        JSONObject json = null;
        try{
            json= new JSONObject(otpstr);
        }
        catch (Exception ex){
            ex.toString();
        }
        return  json;
    }
}
