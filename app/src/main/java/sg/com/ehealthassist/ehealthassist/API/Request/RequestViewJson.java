package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class RequestViewJson {
    public String memberid = "";

    public RequestViewJson(String memberid) {
        this.memberid = memberid;
    }
    public String ObjecttoJson(){
        String  viewjson = "{\"MemberId\" :" +   this.memberid  + "}";
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

