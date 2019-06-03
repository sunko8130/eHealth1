package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class RequestJsonProfileDownload {
    String memberid="";
    int clinicid= 0;
    String hecode = "";

    public RequestJsonProfileDownload(){}

    public RequestJsonProfileDownload(String memberid, int clinicid,String hedcode) {
        this.memberid = memberid;
        this.clinicid = clinicid;
        this.hecode = hedcode;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public int getClinicid() {
        return clinicid;
    }

    public void setClinicid(int clinicid) {
        this.clinicid = clinicid;
    }

    public String getHecode() {
        return hecode;
    }

    public void setHecode(String hecode) {
        this.hecode = hecode;
    }

    public String ObjecttoJson(RequestJsonProfileDownload dataobject){
        //String json=gson.toJson(dataobject);
        String json="{ "+ "\"MemberID\" "+ ":\"" + dataobject.getMemberid() +"\"," + "\"ClinicID\" " +":"+dataobject.getClinicid()+",\"HECode\":\""+
                dataobject.getHecode() +"\"}";

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

