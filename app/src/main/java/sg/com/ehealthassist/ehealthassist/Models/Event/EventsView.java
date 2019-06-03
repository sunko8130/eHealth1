package sg.com.ehealthassist.ehealthassist.Models.Event;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thazinhlaing on 1/7/17.
 */
public class EventsView  implements Parcelable {
    int EventId,Maxpeoplereg;
    String EventName,EventDesc,FromDate,ToDate,FromTime,
            ToTime,Venue,ContactNo,
            Email,CreatedBy,CreatedDate,Status,RegisterEndDate,isRegister,isMemberRegister;

    public EventsView(){}
    public EventsView(int eventId, String eventName, String eventDesc,String fromdate,String todate, String fromTime, String toTime,
                      String venue, String contactNo, String email, String createdBy, String createdDate,String status,String registerEndDate,
                      int maxpeoplereg,String isReg,String isMemberReg) {
        EventId = eventId;
        EventName = eventName;
        EventDesc = eventDesc;
        FromDate = fromdate;
        ToDate = todate;
        FromTime = fromTime;
        ToTime = toTime;
        Venue = venue;
        ContactNo = contactNo;
        Email = email;
        CreatedBy = createdBy;
        CreatedDate = createdDate;
        Status = status;
        RegisterEndDate = registerEndDate;
        Maxpeoplereg = maxpeoplereg;
        isRegister = isReg;
        isMemberRegister=isMemberReg ;
    }

    public String getIsRegister() {
        return isRegister;
    }

    public void setIsRegister(String isRegister) {
        this.isRegister = isRegister;
    }

    public String getIsMemberRegister() {
        return isMemberRegister;
    }

    public void setIsMemberRegister(String isMemberRegister) {
        this.isMemberRegister = isMemberRegister;
    }

    public int getEventId() {
        return EventId;
    }

    public void setEventId(int eventId) {
        EventId = eventId;
    }

    public String getEventName() {
        return EventName;
    }

    public void setEventName(String eventName) {
        EventName = eventName;
    }

    public String getEventDesc() {
        return EventDesc;
    }

    public void setEventDesc(String eventDesc) {
        EventDesc = eventDesc;
    }

    public String getFromTime() {
        return FromTime;
    }

    public void setFromTime(String fromTime) {
        FromTime = fromTime;
    }

    public String getToTime() {
        return ToTime;
    }

    public void setToTime(String toTime) {
        ToTime = toTime;
    }

    public String getVenue() {
        return Venue;
    }

    public void setVenue(String venue) {
        Venue = venue;
    }

    public String getContactNo() {
        return ContactNo;
    }

    public void setContactNo(String contactNo) {
        ContactNo = contactNo;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(String createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDate() {
        return CreatedDate;
    }

    public void setCreatedDate(String createdDate) {
        CreatedDate = createdDate;
    }

    public String getFromDate() {
        return FromDate;
    }

    public void setFromDate(String fromDate) {
        FromDate = fromDate;
    }

    public String getToDate() {
        return ToDate;
    }

    public void setToDate(String toDate) {
        ToDate = toDate;
    }

    public int getMaxpeoplereg() {
        return Maxpeoplereg;
    }

    public void setMaxpeoplereg(int maxpeoplereg) {
        Maxpeoplereg = maxpeoplereg;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getRegisterEndDate() {
        return RegisterEndDate;
    }

    public void setRegisterEndDate(String registerEndDate) {
        RegisterEndDate = registerEndDate;
    }

    //region Parcelable()
    public EventsView(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this.EventId = in.readInt();
        this.EventName = in.readString();
        this.EventDesc = in.readString();
        this.FromTime = in.readString();
        this.ToTime = in.readString();
        this.FromDate = in.readString();
        this.ToDate = in.readString();
        this.Venue = in.readString();
        this.ContactNo = in.readString();
        this.Email = in.readString();
        this.CreatedBy = in.readString();
        this.CreatedDate = in.readString();
        this.Status = in.readString();
        this.Maxpeoplereg = in.readInt();
        this.RegisterEndDate = in.readString();
        this.isMemberRegister = in.readString();
        this.isRegister = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(EventId);
        dest.writeString(EventName);
        dest.writeString(EventDesc);
        dest.writeString(FromTime);
        dest.writeString(ToTime);
        dest.writeString(FromDate);
        dest.writeString(ToDate);
        dest.writeString(Venue);
        dest.writeString(ContactNo);
        dest.writeString(Email);
        dest.writeString(CreatedBy);
        dest.writeString(CreatedDate);
        dest.writeString(Status);
        dest.writeInt(Maxpeoplereg);
        dest.writeString(RegisterEndDate);
        dest.writeString(isMemberRegister);
        dest.writeString(isRegister);
    }

    public static Creator<EventsView> CREATOR = new Creator<EventsView>() {

        @Override
        public EventsView createFromParcel(Parcel source) {
            return new EventsView(source);
        }

        @Override
        public EventsView[] newArray(int size) {
            return new EventsView[size];
        }

    };
    //endregion
}
