package sg.com.ehealthassist.ehealthassist.Models.Medical;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class MedicalReminder {
    String visitno ="";
    String uuid = "";
    String ids ="";
    String message = "";
    String freqcode = "";
    int id =0;
    String setdate="";

    public MedicalReminder(){}

    public MedicalReminder(String visitno,String uuid,String ids,String message, String freqcode,int id ,String setdate){
        this.visitno = visitno;
        this.uuid = uuid;
        this.uuid = uuid;
        this.ids = ids;
        this.message = message;
        this.freqcode = freqcode;
        this.id = id;
        this.setdate = setdate;
    }

    public String getVisitno() {
        return visitno;
    }

    public void setVisitno(String visitno) {
        this.visitno = visitno;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFreqcode() {
        return freqcode;
    }

    public void setFreqcode(String freqcode) {
        this.freqcode = freqcode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSetdate() {
        return setdate;
    }

    public void setSetdate(String setdate) {
        this.setdate = setdate;
    }
}

