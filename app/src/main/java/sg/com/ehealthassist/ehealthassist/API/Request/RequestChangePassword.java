package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by User on 6/30/2017.
 */

public class RequestChangePassword {
    String memberid,oldpassword,newpassword = "";
    public RequestChangePassword(String memberid, String oldpassword, String newpassword) {
        this.memberid = memberid;
        this.oldpassword = oldpassword;
        this.newpassword = newpassword;
    }
    public String objecttoJson(){
        String otpjson = "{\"MemberId\":"+ memberid +" ,\"Oldpassword\":\""+oldpassword+"\",\"Newpassword\":\""+newpassword+"\"  }";
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
