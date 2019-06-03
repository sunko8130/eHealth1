package sg.com.ehealthassist.ehealthassist.Models.Haze;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by thazinhlaing on 1/7/17.
 */
@Parcel
public class PSIHR_Region {
    String id;
    String latitude;
    String lngitude;
    PSIHR_Record record;

    public PSIHR_Region(){}

    public PSIHR_Region(String id, String latitude, String lngitude, PSIHR_Record record) {
        this.id = id;
        this.latitude = latitude;
        this.lngitude = lngitude;
        this.record = record;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLngitude() {
        return lngitude;
    }

    public void setLngitude(String lngitude) {
        this.lngitude = lngitude;
    }

    public PSIHR_Record getRecord() {
        return record;
    }

    public void setRecord(PSIHR_Record record) {
        this.record = record;
    }

    public static ArrayList<PSIHR_Region> parseJsonArray(JsonArray jsondata){
        ArrayList<PSIHR_Region> psi_hr_list = new ArrayList<PSIHR_Region>();
        Gson gson = new Gson();

        for(int i = 0;i < jsondata.size();i++ ){
            JsonObject jsonObject = jsondata.get(i).getAsJsonObject();
            PSIHR_Region psi_hr = gson.fromJson(jsonObject,PSIHR_Region.class);

            psi_hr_list.add(psi_hr);
        }
        return psi_hr_list;
    }
}
