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
public class PM2_5Region {
    String id;
    String latitude;
    String lngitude;
    PM2_5Record record;

    public PM2_5Region(){}
    public PM2_5Region(String id, String latitude, String lngitude, PM2_5Record record) {
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

    public PM2_5Record getRecord() {
        return record;
    }

    public void setRecord(PM2_5Record record) {
        this.record = record;
    }


    public static ArrayList<PM2_5Region> parseJsonArray(JsonArray jsondata){
        ArrayList<PM2_5Region> pm2_5 = new ArrayList<PM2_5Region>();
        Gson gson = new Gson();

        for(int i = 0;i < jsondata.size();i++ ){
            JsonObject jsonObject = jsondata.get(i).getAsJsonObject();
            PM2_5Region pm2_5obj = gson.fromJson(jsonObject,PM2_5Region.class);

            pm2_5.add(pm2_5obj);
        }
        return pm2_5;
    }

}
