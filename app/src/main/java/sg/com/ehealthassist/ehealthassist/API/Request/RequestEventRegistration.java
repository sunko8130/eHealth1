package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by User on 9/5/2017.
 */

public class RequestEventRegistration {
    Long EventID;

    public RequestEventRegistration(long eventid) {
        this.EventID = eventid;
    }
    public String objecttoJson(){
        String otpjson = "{\"EventID\":"+ EventID +"  }";
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
