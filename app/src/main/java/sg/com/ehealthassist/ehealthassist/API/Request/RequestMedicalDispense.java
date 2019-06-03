package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class RequestMedicalDispense {
    String id="";


    public RequestMedicalDispense(){
    }


    public String IDObjecttoJson(String ID){
        String json ="{" + "\"ID\":\""+ID+ "\"}";

        return json;
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

