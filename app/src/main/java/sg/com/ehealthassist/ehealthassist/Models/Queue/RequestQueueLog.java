package sg.com.ehealthassist.ehealthassist.Models.Queue;

import java.io.Serializable;

/**
 * Created by User on 6/30/2017.
 */

public class RequestQueueLog implements Serializable {
    int id = 0;
    String request_nric = "";
    String request_name = "";
    String user_token = "";
    String request_date = "";
    int clinic_id = 0;
    String clinic_name = "";
    String dob = "";
    String request_id;
    int queue_no = 0;
    String description = "";
    String qmessage = "";
    String qstatus = "";


    public RequestQueueLog() {
    }

    public RequestQueueLog(int id, String request_nric, String user_token, String request_date, int clinic_id, String clinic_name, String dob, String request_id,
                           int queue_no, String description,String qmessage ,String requestname,String qstatus) {
        this.id = id;
        this.request_nric = request_nric;
        this.user_token = user_token;
        this.request_date = request_date;
        this.clinic_id = clinic_id;
        this.clinic_name = clinic_name;
        this.dob = dob;
        this.request_id = request_id;
        this.queue_no = queue_no;
        this.description = description;
        this.qmessage = qmessage;
        this.request_name = requestname;
        this.qstatus = qstatus;

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRequest_nric(String request_nric) {
        this.request_nric = request_nric;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public void setRequest_date(String request_date) {
        this.request_date = request_date;
    }

    public void setClinic_id(int clinic_id) {
        this.clinic_id = clinic_id;
    }

    public void setClinic_name(String clinic_name) {
        this.clinic_name = clinic_name;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public void setQueue_no(int queue_no) {
        this.queue_no = queue_no;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    public String getRequest_nric() {
        return this.request_nric;
    }

    public String getUser_token() {
        return this.user_token;
    }

    public String getRequest_date() {
        return this.request_date;
    }

    public int getClinic_id() {
        return this.clinic_id;
    }

    public String getClinic_name() {
        return this.clinic_name;
    }

    public String getDob() {
        return this.dob = dob;
    }

    public String getRequest_id() {
        return this.request_id;
    }

    public int getQueue_no() {
        return this.queue_no;
    }

    public String getDescription() {
        return this.description;
    }

    public String getQmessage() {
        return qmessage;
    }

    public String getRequest_name() {
        return request_name;
    }

    public void setRequest_name(String request_name) {
        this.request_name = request_name;
    }

    public void setQmessage(String qmessage) {
        this.qmessage = qmessage;
    }

    public String getQstatus() {
        return qstatus;
    }

    public void setQstatus(String qstatus) {
        this.qstatus = qstatus;
    }
}
