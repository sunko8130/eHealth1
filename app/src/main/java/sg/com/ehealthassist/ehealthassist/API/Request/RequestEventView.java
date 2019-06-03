package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by User on 9/6/2017.
 */

public class RequestEventView {
    Long MemberId;

    public RequestEventView(long memberid) {
        this.MemberId = memberid;
    }
    public String objecttoJson(){
        String otpjson = "{\"MemberID\":"+ MemberId +"  }";
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
