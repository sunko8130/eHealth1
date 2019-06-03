package sg.com.ehealthassist.ehealthassist.Models.Clinic;

/**
 * Created by User on 6/29/2017.
 */

public class Contact {
    String _email="",_website="";
    int _fax=0 ,_tel1=0,_tel2=0;

    public Contact(int Tel1, int Tel2, int Fax, String Website, String Email) {
        this._email = Email;
        this._fax = Fax;
        this._tel1 = Tel1;
        this._tel2 = Tel2;
        this._website = Website;
    }
    public Contact() {
    }
    public String get_email() {
        return _email;
    }

    public void set_email(String _email) {
        this._email = _email;
    }

    public int get_fax() {
        return _fax;
    }

    public void set_fax(int _fax) {
        this._fax = _fax;
    }

    public int get_tel1() {
        return _tel1;
    }

    public void set_tel1(int _tel1) {
        this._tel1 = _tel1;
    }

    public int get_tel2() {
        return _tel2;
    }

    public void set_tel2(int _tel2) {
        this._tel2 = _tel2;
    }
    public String get_website() {
        return _website;
    }

    public void set_website(String _website) {
        this._website = _website;
    }
}
