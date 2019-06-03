package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by thazinhlaing on 30/6/17.
 */
public class RequestPcodejson {
    String pcode;

    public RequestPcodejson(String pcode){
        this.pcode = pcode;

    }

    public String objecttoJson(){
        String otpjson = "{\"Pcode\":\""+ pcode +"\"}";
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
