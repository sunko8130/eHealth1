package sg.com.ehealthassist.ehealthassist.Models.Profile;

/**
 * Created by User on 6/30/2017.
 */

public class CountryCode {
    String CountryName = "";
    int CountryCode =0;
    String ISOCode = "";
    boolean Active = false;

    public CountryCode(){

    }
    public CountryCode(String countryName, int countryCode, String ISOCode, boolean active) {
        CountryName = countryName;
        CountryCode = countryCode;
        this.ISOCode = ISOCode;
        Active = active;
    }

    public String getCountryName() {
        return CountryName;
    }

    public void setCountryName(String countryName) {
        CountryName = countryName;
    }

    public int getCountryCode() {
        return CountryCode;
    }

    public void setCountryCode(int countryCode) {
        CountryCode = countryCode;
    }

    public String getISOCode() {
        return ISOCode;
    }

    public void setISOCode(String ISOCode) {
        this.ISOCode = ISOCode;
    }

    public boolean isActive() {
        return Active;
    }

    public void setActive(boolean active) {
        Active = active;
    }
}
