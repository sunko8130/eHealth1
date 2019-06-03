package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by thazinhlaing on 2/7/17.
 */

public class Requestdeleteprofilejson {
    String memberid,Nric,NricType,dob;

    public Requestdeleteprofilejson(String memberid, String Nric, String NricType,String dob) {
        this.memberid = memberid;
        this.Nric = Nric;
        this.NricType = NricType;
        this.dob = dob;

    }
    public String objecttoJson(){
        String otpjson = "{\"MemberId\":"+ memberid +" ,\"Nric\":\""+Nric+"\",\"NricType\":\""+NricType+"\",\"DOB\": \"" + dob  + "\" }";
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

