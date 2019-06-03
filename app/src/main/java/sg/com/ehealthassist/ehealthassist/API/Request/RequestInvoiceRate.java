package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by User on 9/14/2017.
 */

public class RequestInvoiceRate {
    int ClinicID=0;
    String invoiceno="";

    public RequestInvoiceRate(int clinicID, String invoiceno) {
        ClinicID = clinicID;
        this.invoiceno = invoiceno;
    }
    public String objecttoJson(){
        String otpjson ="{\"ClinicID\":\""+ClinicID+"\"," + "\"InvoiceNo\":\""+invoiceno+"\"}";

        return otpjson;
    }
    public JSONObject StringtoJsonObject(String otpstr){
        JSONObject json = null;
        try{
            json= new JSONObject(otpstr);
        }
        catch (Exception ex){
            ex.toString();
        }
        return  json;
    }
}
