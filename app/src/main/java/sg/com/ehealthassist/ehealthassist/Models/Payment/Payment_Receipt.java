package sg.com.ehealthassist.ehealthassist.Models.Payment;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class Payment_Receipt implements Parcelable{
    int id = 0;
    int clinicid = 0 ;
    String receiptno = "";
    String invoiceno = "";
    String receiptamount = "0.00";
    String receiptdate = "";
    String modifieddate = "";
    String receipturl = "";
    ArrayList<PaymentType> paymentlist = new ArrayList<PaymentType>();
    String status = "";

    public  Payment_Receipt(){}
    public Payment_Receipt(int id, int clinicid, String receiptno, String invoiceno, String receiptamount, String receiptdate,
                           String receipturl, ArrayList<PaymentType> paymentlist,String status) {
        this.id = id;
        this.clinicid = clinicid;
        this.receiptno = receiptno;
        this.invoiceno = invoiceno;
        this.receiptamount = receiptamount;
        this.receiptdate = receiptdate;
        this.receipturl = receipturl;
        this.paymentlist = paymentlist;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getClinicid() {
        return clinicid;
    }

    public void setClinicid(int clinicid) {
        this.clinicid = clinicid;
    }

    public String getReceiptno() {
        return receiptno;
    }

    public void setReceiptno(String receiptno) {
        this.receiptno = receiptno;
    }

    public String getInvoiceno() {
        return invoiceno;
    }

    public void setInvoiceno(String invoiceno) {
        this.invoiceno = invoiceno;
    }

    public String getReceiptamount() {
        return receiptamount;
    }

    public void setReceiptamount(String receiptamount) {
        this.receiptamount = receiptamount;
    }

    public String getReceiptdate() {
        return receiptdate;
    }

    public void setReceiptdate(String receiptdate) {
        this.receiptdate = receiptdate;
    }

    public String getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(String modifieddate) {
        this.modifieddate = modifieddate;
    }

    public String getReceipturl() {
        return receipturl;
    }

    public void setReceipturl(String receipturl) {
        this.receipturl = receipturl;
    }

    public ArrayList<PaymentType> getPaymentlist() {
        return paymentlist;
    }

    public void setPaymentlist(ArrayList<PaymentType> paymentlist) {
        this.paymentlist = paymentlist;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Payment_Receipt(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.id = in.readInt();
        this.clinicid = in.readInt();
        this.receiptno = in.readString();
        this.invoiceno = in.readString();
        this.receiptamount = in.readString();
        this.receiptdate = in.readString();
        this.modifieddate = in.readString();
        this.receipturl = in.readString();
        this.paymentlist = in.readArrayList(PaymentType.class.getClassLoader());
        this.status = in.readString();

    }
    @Override
    public int describeContents() {
        return 0;
    }
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(clinicid);
        dest.writeString(receiptno);
        dest.writeString(invoiceno);
        dest.writeString(receiptamount);
        dest.writeString(receiptdate);
        dest.writeString(modifieddate);
        dest.writeString(receipturl);
        dest.writeList(paymentlist);
        dest.writeString(status);
    }

    public static Creator<Payment_Receipt> CREATOR = new Parcelable.Creator<Payment_Receipt>() {

        @Override
        public Payment_Receipt createFromParcel(Parcel source) {
            return new Payment_Receipt(source);
        }

        @Override
        public Payment_Receipt[] newArray(int size) {
            return new Payment_Receipt[size];
        }

    };
}
