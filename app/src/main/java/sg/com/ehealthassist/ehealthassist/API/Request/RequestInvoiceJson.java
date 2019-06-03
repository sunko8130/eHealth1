package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class RequestInvoiceJson {
    String memberid,ClinicID,InvoiceNo,NRIC,ReceiptNo = "" ;

    public RequestInvoiceJson(String memberid, String ClinicID, String InvoiceNo,String NRIC,String ReceiptNo) {
        this.memberid = memberid;
        this.ClinicID = ClinicID;
        this.InvoiceNo = InvoiceNo;
        this.NRIC = NRIC;
        this.ReceiptNo = ReceiptNo;
    }
    public String objecttoJson(){
        String otpjson = "{\"MemberId\":"+ memberid +" ,\"ClinicID\":\""+ClinicID+"\",\"InvoiceNo\":\""+InvoiceNo+"\",\"NRIC\" : \"" +NRIC+
                "\",\"ReceiptNo\": \"" +ReceiptNo + "\" }";
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

