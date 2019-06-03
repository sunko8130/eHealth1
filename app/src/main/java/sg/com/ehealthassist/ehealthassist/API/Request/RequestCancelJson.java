package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class RequestCancelJson {
    public String LongBookingId = "";
    public String shortBookingid = "";


    public RequestCancelJson(String longbookid,String shortbookid) {
        this.LongBookingId = longbookid;
        this.shortBookingid = shortbookid;

    }
    public String ObjecttoJson(){
        String  bookjson = "{\"LongBookingId\" :" +   this.LongBookingId  + " , \"ShortBookingId\":" + this.shortBookingid +" }";

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

