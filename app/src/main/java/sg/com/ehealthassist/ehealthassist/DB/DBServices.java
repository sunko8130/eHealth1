package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Models.Services.Services;

/**
 * Created by User on 6/29/2017.
 */

public class DBServices {
    public static final String TAG = "Services";
    private DatabaseHandler mDbHelper;
    private Context mContext;

    public DBServices(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }
    //region Insert Services_item
    public void addServices(Services serviceobj) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = setServicesValues(serviceobj);
        db.insert(DatabaseHandler.TABLE_Services, null, values);
        db.close();
    }
    //endregion
    //region Get contentvalues from RequestQueuelog
    private ContentValues setServicesValues(Services queueLog) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_Services_Id, queueLog.getId());
        values.put(DatabaseHandler.COL_ServiceClinicType, queueLog.getClinic_type());
        values.put(DatabaseHandler.COL_Specialty_Name, queueLog.getSpeciality());
        return values;
    }
    //endregion
    public ArrayList<Services> getspeciltybyclinictype(String clinictype){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = null;
        ArrayList<Services>  arraylst_service = new ArrayList<Services>();
        String sql1 = "SELECT * FROM " + DatabaseHandler.TABLE_Services +
                " WHERE " + DatabaseHandler.COL_ServiceClinicType + " = '" + clinictype +"' ORDER BY "  + DatabaseHandler.COL_Specialty_Name;

        String sql2 = "SELECT distinct * FROM " + DatabaseHandler.TABLE_Services +" ORDER BY "  + DatabaseHandler.COL_Specialty_Name;
        if(clinictype.equals("")){
            cursor = db.rawQuery(sql2,null);
        }
        else{
            cursor = db.rawQuery(sql1,null);
        }
      /*  cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_Services +
                " WHERE " + DatabaseHandler.COL_ServiceClinicType + " = '" + clinictype +"' ORDER BY "  + DatabaseHandler.COL_Specialty_Name, null);*/
        if (!cursor.equals(null)) {
            if (cursor.moveToFirst()) {
                do {
                    arraylst_service.add(getSpecialty(cursor));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return arraylst_service;
    }

    public Services getSpecialty(Cursor cursor){
        Services serviceinfo = new Services();
        serviceinfo.setId(cursor.getInt(0));
        serviceinfo.setClinic_type(cursor.getString(1));
        serviceinfo.setSpeciality(cursor.getString(2));
        return serviceinfo;
    }

    //region Delete all clinic Sevices
    public void deleteAllServices() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_Services;
        db.execSQL(str);
        db.close();
    }
}
