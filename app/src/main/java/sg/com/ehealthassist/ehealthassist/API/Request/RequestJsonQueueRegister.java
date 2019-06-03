package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class RequestJsonQueueRegister {
    String PatientNric;
    String PatientDob;
    int nrictype;

    public RequestJsonQueueRegister() {

    }

    public RequestJsonQueueRegister(String patientNric, String patientDob,int nrictype) {
        this.PatientNric = patientNric;
        this.PatientDob = patientDob;
        this.nrictype = nrictype;
    }

    public String getPatientNric() {
        return PatientNric;
    }

    public void setPatientNric(String patientNric) {
        PatientNric = patientNric;
    }

    public String getPatientDob() {
        return PatientDob;
    }

    public void setPatientDob(String patientDob) {
        PatientDob = patientDob;
    }
    public String ObjecttoJson(RequestJsonQueueRegister dataobject){

        //String json=gson.toJson(dataobject);
        String json="{ "+ "\"PatientNric\" "+ ":\"" + dataobject.getPatientNric() +"\"," + "\"PatientDob\" " +":\""+dataobject.getPatientDob()+"\",\"NricType\":"+
                nrictype +"}";
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

