package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by User on 6/30/2017.
 */

public class RequestAccountInfo {
    String memberid,mobile,email,countryisocode="";

    public RequestAccountInfo(String memberid, String mobile, String email,String countryisocode) {
        this.memberid = memberid;
        this.mobile = mobile;
        this.email = email;
        this.countryisocode = countryisocode;
    }
    public String objecttoJson(){
        String otpjson = "{\"MemberId\":"+ memberid +" ,\"Mobile\":"+mobile+",\"Email\":\""+email+"\",\"CountryISOCode\":\"" +
                countryisocode+"\" }";
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
