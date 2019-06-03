package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class RequestQueueCancel {
    public int clinicid;
    public String requestid ;
    public  String queueno;


    public RequestQueueCancel(int clinicid,String requestid,String queueno) {
        this.clinicid = clinicid;
        this.requestid = requestid;
        this.queueno = queueno;
    }
    public String ObjecttoJson(){
        String  bookjson = "{\"clinicid\" :" +   this.clinicid  + " , \"requestid\":" + this.requestid +",\"queueno\": " + this.queueno +"}";

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

