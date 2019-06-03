package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class RequestLogout {

    public String MobileOs = "";
    public String pushid = "";


    public RequestLogout(String mobileos,String pushid) {
        this.MobileOs = mobileos;
        this.pushid = pushid;

    }
    public String ObjecttoJson(){
        String  bookjson = "{\"MobileOs\" :" +   this.MobileOs  + " , \"PushId\":" + this.pushid +" }";

        return  bookjson;
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

