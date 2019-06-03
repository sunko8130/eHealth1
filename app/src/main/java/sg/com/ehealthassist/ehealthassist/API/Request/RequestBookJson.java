package sg.com.ehealthassist.ehealthassist.API.Request;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import sg.com.ehealthassist.ehealthassist.Other.Utils;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class RequestBookJson {
    public String memberid,apptdatetime ,nric,name,email,mobile,session,
            apptTime,nric_type,patientRemark ,countryisocode = "";
    public int clinicid = 0;
    public int doctorid = 0;

    public RequestBookJson(String memberid, int clinicid,int doctorid,String apptdatetime,String spinnernric,
                           String spinnername,String spinneremail,String mobile,String nric_type,String session,
                           String apptTime,String patientRemark,String countryisocode) {
        this.memberid = memberid;
        this.clinicid = clinicid;
        this.doctorid = doctorid;
        this.apptdatetime = apptdatetime;
        this.nric = spinnernric;
        this.name = spinnername;
        this.email = spinneremail;
        this.mobile = mobile;
        this.nric_type = nric_type;
        this.session = session;
        this.apptTime= apptTime;
        this.patientRemark = patientRemark;
        this.countryisocode = countryisocode;

    }
    public String ObjecttoJson(){

        String  bookjson = "{\"MemberId\" : " + memberid + ",\"ClinicId\": " + clinicid + " ,\"DoctorId\":"+doctorid
                +",\"AppointmentDateTime\":\""+ apptdatetime + "\",\"Nric\":\""+nric+"\",\"FullName\":\""+name+"\",\"Email\":\""+email+"\",\"Mobile\":" +
                mobile + ",\"Session\":\""+session +"\",\"ApptTiming\":\""+apptTime + "\",\"NricType\":\""+nric_type+"\",\"PatientRemark\":\""+patientRemark +"\"," +
                "\"CountryISOCode\":\""+countryisocode+"\"}";

        return  bookjson;
    }

    public String changeApptDateTime(String apptdate){
        Calendar current_calendar = Calendar.getInstance();
        Date date =   Utils.reminderTimeFormat(apptdate, "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss");
        current_calendar.set(date.getYear()+1900,date.getMonth(),date.getDate());
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss:sss");
        String lastdate = outputFormat.format(current_calendar.getTime());
        return lastdate;

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

