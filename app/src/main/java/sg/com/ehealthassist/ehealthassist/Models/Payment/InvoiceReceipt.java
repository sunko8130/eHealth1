package sg.com.ehealthassist.ehealthassist.Models.Payment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class InvoiceReceipt implements Parcelable {
    int id = 0;
    int memberid = 0;
    String nric = "";
    String nric_name = "";
    int clinicid = 0;
    String clinicname = "";
    String clinicgstno  = "";
    String invoiceno = "";
    String beforegstamount = "";
    String outstandingamount = "0.00";
    String invoiceamount = "0.00";
    String invoiceurl = "";
    String invoicedate = "";
    String modifieddate = "";
    ArrayList<Payment_Receipt> pay_receiptList = new ArrayList<Payment_Receipt>();
    String status="";String isRate="";

    public  InvoiceReceipt(){}
    public InvoiceReceipt(int id, int memberid, String nric, String nric_name, int clinicid, String clinicname, String clinicgstno,
                          String invoiceno, String beforegstamount, String outstandingamount,
                          String invoiceamount, String invoiceurl, String invoicedate, ArrayList<Payment_Receipt> pay_receiptList,
                          String status,String isRate) {
        this.id = id;
        this.memberid = memberid;
        this.nric = nric;
        this.nric_name = nric_name;
        this.clinicid = clinicid;
        this.clinicname = clinicname;
        this.clinicgstno = clinicgstno;
        this.invoiceno = invoiceno;
        this.beforegstamount = beforegstamount;
        this.outstandingamount = outstandingamount;
        this.invoiceamount = invoiceamount;
        this.invoiceurl = invoiceurl;
        this.invoicedate = invoicedate;
        this.pay_receiptList = pay_receiptList;
        this.status = status;
        this.isRate= isRate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMemberid() {
        return memberid;
    }

    public void setMemberid(int memberid) {
        this.memberid = memberid;
    }

    public String getNric() {
        return nric;
    }

    public void setNric(String nric) {
        this.nric = nric;
    }

    public String getNric_name() {
        return nric_name;
    }

    public void setNric_name(String nric_name) {
        this.nric_name = nric_name;
    }

    public int getClinicid() {
        return clinicid;
    }

    public void setClinicid(int clinicid) {
        this.clinicid = clinicid;
    }

    public String getClinicname() {
        return clinicname;
    }

    public void setClinicname(String clinicname) {
        this.clinicname = clinicname;
    }

    public String getClinicgstno() {
        return clinicgstno;
    }

    public void setClinicgstno(String clinicgstno) {
        this.clinicgstno = clinicgstno;
    }

    public String getInvoiceno() {
        return invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }

    public String getBeforegstamount() {
        return beforegstamount;
    }

    public void setBeforegstamount(String beforegstamount) {
        this.beforegstamount = beforegstamount;
    }

    public String getOutstandingamount() {
        return outstandingamount;
    }

    public void setOutstandingamount(String outstandingamount) {
        this.outstandingamount = outstandingamount;
    }

    public String getInvoiceamount() {
        return invoiceamount;
    }

    public void setInvoiceamount(String invoiceamount) {
        this.invoiceamount = invoiceamount;
    }

    public String getInvoiceurl() {
        return invoiceurl;
    }

    public void setInvoiceurl(String invoiceurl) {
        this.invoiceurl = invoiceurl;
    }

    public String getInvoicedate() {
        return invoicedate;
    }

    public String getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(String modifieddate) {
        this.modifieddate = modifieddate;
    }

    public void setInvoicedate(String invoicedate) {
        this.invoicedate = invoicedate;
    }

    public ArrayList<Payment_Receipt> getPay_receiptList() {
        return pay_receiptList;
    }

    public void setPay_receiptList(ArrayList<Payment_Receipt> pay_receiptList) {
        this.pay_receiptList = pay_receiptList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsRate() {
        return isRate;
    }

    public void setIsRate(String isRate) {
        this.isRate = isRate;
    }
    //region Parcel

    public InvoiceReceipt(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in){
        this.id = in.readInt();
        this.memberid = in.readInt();
        this.nric = in.readString();
        this.nric_name = in.readString();
        this.clinicid = in.readInt();
        this.clinicname = in.readString();
        this.clinicgstno = in.readString();
        this.invoiceno = in.readString();
        this.beforegstamount = in.readString();
        this.outstandingamount = in.readString();
        this.invoiceamount = in.readString();
        this.invoiceurl = in.readString();
        this.invoicedate = in.readString();
        this.modifieddate = in.readString();
        this.pay_receiptList = in.readArrayList(Payment_Receipt.class.getClassLoader());
        this.status = in.readString();
        this.isRate = in.readString();

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.memberid);
        dest.writeString(this.nric);
        dest.writeString(this.nric_name);
        dest.writeInt(this.clinicid);
        dest.writeString(this.clinicname);
        dest.writeString(this.clinicgstno);
        dest.writeString(this.invoiceno);
        dest.writeString(this.beforegstamount);
        dest.writeString(this.outstandingamount);
        dest.writeString(this.invoiceamount);
        dest.writeString(this.invoiceurl);
        dest.writeString(this.invoicedate);
        dest.writeString(this.modifieddate);
        dest.writeList(this.pay_receiptList);
        dest.writeString(this.status);
        dest.writeString(this.isRate);
    }
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public InvoiceReceipt createFromParcel(Parcel in) {
            return new InvoiceReceipt(in);
        }

        public InvoiceReceipt[] newArray(int size) {
            return new InvoiceReceipt[size];
        }
    };

    //endregion
}
