package sg.com.ehealthassist.ehealthassist.API.Request;

import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by User on 6/30/2017.
 */

public class RequestJsonRegister {
    String Email="";
    String Mobile ="0";
    String CountryISOCode = "";
    String Password="";
    String PushId="";
    int MobileOs= 1;
    String DeviceId="";
    String nric = "";
    public Gson reggson=new Gson();

    public RequestJsonRegister(String email, String mobile, String countryISOCode ,String password, String pushId, int mobileOs, String deviceId,String nric) {
        this.Email = email;
        this.Mobile = mobile;
        this.CountryISOCode = countryISOCode;
        this.Password = password;
        this.PushId = pushId;
        this.MobileOs = mobileOs;
        this.DeviceId = deviceId;
        this.nric = nric;
    }
    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getMobile() {
        return  Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getPushId() {
        return PushId;
    }

    public void setPushId(String pushId) {
        PushId = pushId;
    }

    public String getDeviceId() {
        return DeviceId;
    }

    public void setDeviceId(String deviceId) {
        DeviceId = deviceId;
    }

    public int getMobileOs() {
        return MobileOs;
    }

    public void setMobileOs(int  mobileOs) {
        MobileOs = mobileOs;
    }

    public String getCountryISOCode() {
        return CountryISOCode;
    }

    public void setCountryISOCode(String countryISOCode) {
        CountryISOCode = countryISOCode;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String ObjecttoJson(RequestJsonRegister dataobject){
        String json ="{" + "\"Email\":\""+dataobject.getEmail()+ "\",\"CountryISOCode\":\""+dataobject.getCountryISOCode()+"\",\"Mobile\":"+
                dataobject.getMobile()+",\"Password\":\""+dataobject.getPassword()+ "\",\"PushId\":\""+
                dataobject.getPushId()+"\",\"MobileOs\":"+dataobject.getMobileOs()+ ",\"DeviceId\":\""+
                dataobject.getDeviceId() + "\",\"Nric\":"+ nric +
                "}";

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
