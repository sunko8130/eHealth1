package sg.com.ehealthassist.ehealthassist.eDocument;

import java.util.Comparator;
import java.util.Date;

/**
 * Created by User on 9/18/2017.
 */

public  class InvoiceFin implements Comparable<InvoiceFin> {
    public Object multiobj;
    public Date createDate;
    public String isObject;

    public InvoiceFin(Object multiobj, Date createDate,String isObject) {
        this.multiobj = multiobj;
        this.createDate = createDate;
        this.isObject= isObject;
    }

    public Object getMultiobj() {
        return multiobj;
    }

    public void setMultiobj(Object multiobj) {
        this.multiobj = multiobj;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public String getIsObject() {
        return isObject;
    }

    public void setIsObject(String isObject) {
        this.isObject = isObject;
    }

    @Override
    public int compareTo(InvoiceFin o) {
        return getCreateDate().compareTo(o.getCreateDate());
    }

   /* @Override
    public int compare(InvoiceFin o, InvoiceFin t1) {
        return  o.getCreateDate().compareTo(t1.getCreateDate());

       // return  o.getCreateDate().after(t1.getCreateDate());
    }*/
}
