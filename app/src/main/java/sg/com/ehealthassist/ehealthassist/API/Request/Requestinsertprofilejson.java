package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by thazinhlaing on 1/7/17.
 */
public class Requestinsertprofilejson {
    String memberid,Nric,NricType,Name,DOB,mainaccount = "" ;

    public Requestinsertprofilejson(String memberid, String Nric, String NricType,String Name,String DOB,String mainaccount) {
        this.memberid = memberid;
        this.Nric = Nric;
        this.NricType = NricType;
        this.Name = Name;
        this.DOB = DOB;
        this.mainaccount = mainaccount;
    }
    public String objecttoJson(){
        String otpjson = "{\"MemberId\":"+ memberid +" ,\"Nric\":\""+Nric+"\",\"NricType\":\""+NricType+"\",\"Name\" : \"" +Name+
                "\",\"DOB\": \"" +DOB + "\",\"MainAccount\":\"" +   mainaccount + "\" }";
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
