package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by User on 9/27/2017.
 */

public class RequestUpdateMessage {
    int id;
    long clinicid;
    long memberid;
    String nric;
    String nrictype;
    String dob;
    String message;
    String createdate;
    int status;
    String messageReply;
    String downloaded;
    Long mobileno;

    public RequestUpdateMessage(int id, long clinicid, long memberid, String nric, String nrictype, String dob, String message,
                                String createdate, int status, String messageReply, String downloaded,Long mobileno) {
        this.id = id;
        this.clinicid = clinicid;
        this.memberid = memberid;
        this.nric = nric;
        this.nrictype = nrictype;
        this.dob = dob;
        this.message = message;
        this.createdate = createdate;
        this.status = status;
        this.messageReply = messageReply;
        this.downloaded = downloaded;
        this.mobileno = mobileno;
    }

    public String ObjecttoJson(){
        String  bookjson = "{\"ID\" :\"" +   this.id  + "\" ," +
                " \"ClinicID\":\"" + this.clinicid +"\"," +
                "\"MemberID\":\"" + this.memberid +"\"," +
              //  "\"Nric\": \"" + this.nric +"\"," +
              //  "\"NricType\": \"" + this.nrictype +"\"," +
              //  "\"DOB\": \"" + this.dob +"\"," +
                "\"Mobile\": \"" + this.mobileno +"\"," +
                "\"Message\": \"" + this.message +"\"," +
                "\"CreateDate\": \"" + this.createdate +"\"," +
                "\"Status\": \"" + this.status +"\"," +
                "\"MessageReply\": \"" + this.messageReply +"\"," +
                "\"Downloaded\": \"" + this.downloaded +"\"" +

                "}";

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
