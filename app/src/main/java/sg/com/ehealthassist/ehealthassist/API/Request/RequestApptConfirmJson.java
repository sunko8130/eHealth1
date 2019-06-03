package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class RequestApptConfirmJson {

    String LongBookingId = "";
    String shortBookingId = "";


    public RequestApptConfirmJson(String longbookid,String shortbookid) {
        this.LongBookingId = longbookid;
        this.shortBookingId = shortbookid;
    }
    public String ObjecttoJson(){
        String  bookjson = "{\"LongBookingId\" :" +   this.LongBookingId  + ", \"ShortBookingId\": " + this.shortBookingId + " }";
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

