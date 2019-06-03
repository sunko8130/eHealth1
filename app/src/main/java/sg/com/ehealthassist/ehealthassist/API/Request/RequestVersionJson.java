package sg.com.ehealthassist.ehealthassist.API.Request;

/**
 * Created by User on 6/29/2017.
 */

import org.json.JSONObject;
public class RequestVersionJson {

    int mobileOs = 1;

    public RequestVersionJson() {

    }
    public String ObjecttoJson(){
        String  viewjson = "{\"MobileOs\" :" +  mobileOs + "}";
        return  viewjson;
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