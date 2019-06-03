package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by User on 6/30/2017.
 */

public class RequestForgotPwd {
    String mobile= "";

    public RequestForgotPwd( String mobile) {
        this.mobile = mobile;

    }
    public String objecttoJson(){
        String otpjson = "{\"Mobile\":\""+mobile+"\"}";
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
