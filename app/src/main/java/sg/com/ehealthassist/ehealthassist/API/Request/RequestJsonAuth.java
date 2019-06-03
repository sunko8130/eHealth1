package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by User on 6/30/2017.
 */

public class RequestJsonAuth {
    String Phone_number;
    String Password;
    String Gcmid = "";


    public RequestJsonAuth(String phone_number, String passwordId,String gcmid) {
        this.Phone_number = phone_number;
        this.Password = passwordId;
        this.Gcmid = gcmid;
    }
    public RequestJsonAuth( String gcmid){
        this.Gcmid = gcmid;
    }

    public  void setGcmid(String gcmid){
        this.Gcmid = gcmid;
    }

    public String getGcmid(){
        return  this.Gcmid;
    }
    public String getPhone_number() {
        return Phone_number;
    }

    public void setPhone_number(String mobileId) {
        this.Phone_number = mobileId;
    }

    public String getPasswordId() {
        return Password;
    }

    public void setPasswordId(String passwordId) {
        this.Password = passwordId;
    }

    public String ObjecttoJson(RequestJsonAuth dataobject){
        //String json=gson.toJson(dataobject);
        String json="{ "+ " \"MobileNo\" "+ ":" + dataobject.getPhone_number() +"," + "\"Password\" "
                +":\""+dataobject.getPasswordId()+"\"," + "\"MobileOs\":" + 1 +  ",\"PushId\":" + "\""+dataobject.getGcmid()+"\"}";
        return json;
    }

    public String ObjectJsonforLoginView(String gcmid){
        String json="{\"MobileOs\":" + 1 +  ",\"PushId\":" + "\""+ gcmid+"\"}";

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
