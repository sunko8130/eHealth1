package sg.com.ehealthassist.ehealthassist.Other;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Models.Profile.ResponseDownloadStatus;
import sg.com.ehealthassist.ehealthassist.Models.Reminder.ReminderTime;

/**
 * Created by User on 6/30/2017.
 */

public class JGson {
    Gson g = new Gson();

    public String toJson(List<ReminderTime> listqty){
        String gs = g.toJson(listqty);
        return gs;
    }

    public ArrayList<ReminderTime> fromjson(String json){
        ArrayList<ReminderTime> remdlist = g.fromJson(json,new TypeToken<List<ReminderTime>>() {}.getType() );
        return  remdlist;
    }

    public  String toJsonWeek(List<Integer> listweek){
        String gs = g.toJson(listweek);
        return  gs;
    }
    public ArrayList<Integer> fromjsonWeek(String jstr){
        ArrayList<Integer> weeklist = new ArrayList<Integer>();
        weeklist = g.fromJson(jstr,new TypeToken<List<Integer>>(){}.getType());

        return  weeklist;
    }

    public String toAlarmJson(List<String> listqty){
        String gs = g.toJson(listqty);
        return gs;
    }
    public ArrayList<String> fromalarmjson(String json){
        ArrayList<String> remdlist = g.fromJson(json,new TypeToken<List<String>>() {}.getType() );
        return  remdlist;
    }

    public String fromProfileObjectToJson(List<MedicalProfile> listprofile){
        Log.e("Convert json",listprofile.size()+"");

        String gs = g.toJson(listprofile);
        Log.e("Convert json String",gs);
        return gs;
    }

    public ArrayList<MedicalProfile> fromjsonToProfileObject(String json){

        Type collectionType = new TypeToken<ArrayList<MedicalProfile>>(){}.getType();
        ArrayList<MedicalProfile> pofilelist = g.fromJson(json, collectionType);

        //   ArrayList<MedicalProfile> pofilelist = g.fromJson(json,new TypeToken<MedicalProfile>(){}.getType());
        return  pofilelist;
    }

    public ArrayList<ResponseDownloadStatus> fromjsonToProfileDownload(String json){
        Type collectionType = new TypeToken<ArrayList<ResponseDownloadStatus>>(){}.getType();
        ArrayList<ResponseDownloadStatus> profiledownload = g.fromJson(json, collectionType);
        return  profiledownload;
    }
}
