package sg.com.ehealthassist.ehealthassist.Models.Clinic;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 6/29/2017.
 */

public  class ClinicInfo  implements Serializable, Parcelable {
    private Address clinic_address = new Address();
    private Contact clinic_contact = new Contact();
    private Locations clinic_location = new Locations();
    private List<String> _operatinghours = new ArrayList<String>();
    private String _operatinghr = "";
    private String ClinicType = "";
    private int _id = 0, _showPriority = 999;
    private boolean _is24Hours = false, _isChas = false, _isQueueEnabled = false, _toShow = true, _isBookmarked = false,_isapptEnabled=false;
    private String _name = "", logoloid = "";
    private String _rowVersion ,_specialty ,clinic_note ,hecode = "",appt_mode = "";

    public ClinicInfo() {
    }

    public ClinicInfo(Address clinic_address, Contact clinic_contact, Locations clinic_location, List<String> _operatinghours,
                      int _id, int _showPriority, boolean _is24Hours, boolean _isChas,
                      boolean _isQueueEnabled, boolean _toShow, boolean _isBookmarked, String _name,
                      String logoloid, String _rowVersion, String _operatinghr,String specialty,boolean _isapptEnabled,
                      String clinic_note,String hecode) {
        this.clinic_address = clinic_address;
        this.clinic_contact = clinic_contact;
        this.clinic_location = clinic_location;
        this._operatinghours = _operatinghours;
        this._id = _id;
        this._showPriority = _showPriority;
        this._is24Hours = _is24Hours;
        this._isChas = _isChas;
        this._isQueueEnabled = _isQueueEnabled;
        this._toShow = _toShow;
        this._isBookmarked = _isBookmarked;
        this._name = _name;
        this.logoloid = logoloid;
        this._rowVersion = _rowVersion;
        this._operatinghr = _operatinghr;
        this._specialty = specialty;
        this._isapptEnabled = _isapptEnabled;
        this.clinic_note = clinic_note;
        this.hecode = hecode;
        this.appt_mode = appt_mode;
    }

  /*  public ClinicInfo(int _id, String _name, Address clinic_address, Contact clinic_contact, String clinicType,
                      String _operatinghr, Locations clinic_location, boolean _is24Hours, boolean _isChas, boolean _isQueueEnabled,
                      boolean _toShow, String logoloid) {
        this.clinic_address = clinic_address;
        this.clinic_contact = clinic_contact;
        this.clinic_location = clinic_location;
        this._operatinghr = _operatinghr;
        this._id = _id;
        this._is24Hours = _is24Hours;

        this._isChas = _isChas;
        this._isQueueEnabled = _isQueueEnabled;
        this._toShow = _toShow;
        this._name = _name;
        this.logoloid = logoloid;
        this.ClinicType = clinicType;
    }*/

    public Address getClinic_address() {
        return clinic_address;
    }

    public void setClinic_address(Address clinic_address) {
        this.clinic_address = clinic_address;
    }

    public Contact getClinic_contact() {
        return clinic_contact;
    }

    public void setClinic_contact(Contact clinic_contact) {
        this.clinic_contact = clinic_contact;
    }

    public Locations getClinic_location() {
        return clinic_location;
    }

    public void setClinic_location(Locations clinic_location) {
        this.clinic_location = clinic_location;
    }

    public List<String> get_operatinghours() {
        return _operatinghours;
    }

    public void set_operatinghours(List<String> _operatinghours) {
        this._operatinghours = _operatinghours;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int get_showPriority() {
        return _showPriority;
    }

    public void set_showPriority(int _showPriority) {
        this._showPriority = _showPriority;
    }

    public boolean is_is24Hours() {
        return _is24Hours;
    }

    public void set_is24Hours(boolean _is24Hours) {
        this._is24Hours = _is24Hours;
    }

    public boolean is_isChas() {
        return _isChas;
    }

    public void set_isChas(boolean _isChas) {
        this._isChas = _isChas;
    }

    public boolean is_isQueueEnabled() {
        return _isQueueEnabled;
    }

    public void set_isQueueEnabled(boolean _isQueueEnabled) {
        this._isQueueEnabled = _isQueueEnabled;
    }

    public boolean is_isapptEnabled() {
        return _isapptEnabled;
    }

    public void set_isapptEnabled(boolean _isapptEnabled) {
        this._isapptEnabled = _isapptEnabled;
    }

    public boolean is_toShow() {
        return _toShow;
    }

    public void set_toShow(boolean _toShow) {
        this._toShow = _toShow;
    }

    public boolean is_isBookmarked() {
        return _isBookmarked;
    }

    public void set_isBookmarked(boolean _isBookmarked) {
        this._isBookmarked = _isBookmarked;
    }

    public String get_name() {
        return _name;
    }

    public void set_name(String _name) {
        this._name = _name;
    }

    public String getLogoloid() {
        return logoloid;
    }

    public String get_operatinghr() {
        return _operatinghr;
    }

    public void set_operatinghr(String _operatinghr) {
        this._operatinghr = _operatinghr;
    }

    public void setLogoloid(String logoloid) {
        this.logoloid = logoloid;
    }

    public String get_rowVersion() {
        return _rowVersion;
    }

    public void set_rowVersion(String _rowVersion) {
        this._rowVersion = _rowVersion;
    }

    public String getClinicType() {
        return ClinicType;
    }

    public void setClinicType(String clinicType) {
        ClinicType = clinicType;
    }

    public String get_specialty() {
        return _specialty;
    }

    public void set_specialty(String _specialty) {
        this._specialty = _specialty;
    }

    public String getClinic_note() {
        return clinic_note;
    }

    public void setClinic_note(String clinic_note) {
        this.clinic_note = clinic_note;
    }

    public String getHecode() {
        return hecode;
    }

    public void setHecode(String hecode) {
        this.hecode = hecode;
    }
    public String getAppt_mode() {
        return appt_mode;
    }

    public void setAppt_mode(String appt_mode) {
        this.appt_mode = appt_mode;
    }

    //region Parcel
    public ClinicInfo(Parcel in) {
        readFromParcel(in);
    }

    public void readFromParcel(Parcel in) {
        this._id = in.readInt();
        this._name = in.readString();
        this.clinic_address.nearestmrt = in.readString();
        this.clinic_address.placetown = in.readString();
        this.clinic_address.blockno = in.readString();
        this.clinic_address.postal = in.readString();
        this.clinic_address.region = in.readString();
        this.clinic_address.building = in.readString();
        this.clinic_address.street = in.readString();
        this.clinic_address.unitno = in.readString();
        this.clinic_contact._website = in.readString();
        this.clinic_contact._email = in.readString();
        this.clinic_contact._fax = in.readInt();
        this.clinic_contact._tel1 = in.readInt();
        this.clinic_contact._tel2 = in.readInt();
        this.clinic_location._lat = in.readString();
        this.clinic_location._lng = in.readString();
        this._showPriority = in.readInt();
        this.ClinicType = in.readString();
        this._operatinghr = in.readString();
        this.logoloid = in.readString();
        this._rowVersion = in.readString();
        this._toShow = Boolean.valueOf(in.readString() == "null" ? false : true);
        this._is24Hours = Boolean.valueOf(in.readString() == "null" ? false : true);
        this._isChas = Boolean.valueOf(in.readString() == "null" ? false : true);
        this._isBookmarked = Boolean.valueOf(in.readString() == "null" ? false : true);
        this._isQueueEnabled = Boolean.valueOf(in.readString() == "null" ? false : true);
        this._specialty = in.readString();
        this._isapptEnabled = Boolean.valueOf(in.readString() == "null" ? false : true);
        this.clinic_note = in.readString();
        this.hecode = in.readString();
        this.appt_mode = in.readString();
        //  in.readStringList(_operatinghours);

    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this._id);
        out.writeString(this._name);
        out.writeString(this.getClinic_address().nearestmrt);
        out.writeString(this.getClinic_address().placetown);
        out.writeString(this.getClinic_address().blockno);
        out.writeString(this.getClinic_address().postal);
        out.writeString(this.getClinic_address().region);
        out.writeString(this.getClinic_address().building);
        out.writeString(this.getClinic_address().street);
        out.writeString(this.getClinic_address().unitno);
        out.writeString(this.getClinic_contact()._website);
        out.writeString(this.getClinic_contact()._email);
        out.writeInt(this.getClinic_contact()._fax);
        out.writeInt(this.getClinic_contact()._tel1);
        out.writeInt(this.getClinic_contact()._tel2);
        out.writeString(this.getClinic_location()._lat);
        out.writeString(this.getClinic_location()._lng);
        out.writeInt(this._showPriority);
        out.writeString(this.ClinicType);
        out.writeString(this._operatinghr);
        out.writeString(this.logoloid);
        out.writeString(this._rowVersion);
        out.writeString(String.valueOf(this._toShow));
        out.writeString(String.valueOf(this._is24Hours));
        out.writeString(String.valueOf(this._isChas));
        out.writeString(String.valueOf(this._isBookmarked));
        out.writeString(String.valueOf(this._isQueueEnabled));
        out.writeString(String.valueOf(this._specialty));
        out.writeString(String.valueOf(this._isapptEnabled));
        out.writeString(String.valueOf(this.clinic_note));
        out.writeString(String.valueOf(this.hecode));
        out.writeString(String.valueOf(this.appt_mode));
        //  out.writeStringList(this._operatinghours);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ClinicInfo createFromParcel(Parcel in) {
            return new ClinicInfo(in);
        }

        public ClinicInfo[] newArray(int size) {
            return new ClinicInfo[size];
        }
    };
    //endregion
}
