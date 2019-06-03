package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by thazinhlaing on 1/7/17.
 */
public class RequestSmsOTP {
    String memberid,otptoken = "";

    public RequestSmsOTP(String memberid,String otp){
        this.memberid = memberid;
        this.otptoken = otp;
    }


    public String objecttoJson(){
        String otpjson = "{\"MemberId\":"+ memberid +" ,\"OTP\":"+otptoken+" }";
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
