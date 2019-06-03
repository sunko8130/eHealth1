package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Models.Services.Services_Items;

/**
 * Created by User on 6/29/2017.
 */

public class DBService_items {
    public static final String TAG = "Services_Item";
    private DatabaseHandler mDbHelper;
    private Context mContext;

    public DBService_items(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }

    //region Insert Services_item
    public void addServices_item(Services_Items service_item_obj) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = setServicesItmeValues(service_item_obj);
        db.insert(DatabaseHandler.TABLE_Services_Item, null, values);
        db.close();
    }
    //endregion

    //region Get contentvalues from RequestQueuelog
    private ContentValues setServicesItmeValues(Services_Items queueLog) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_Services_Id, queueLog.getId());
        values.put(DatabaseHandler.COL_ServiceName, queueLog.getServices_name());
        return values;
    }
    //endregion
    //region Delete all clinic Sevices
    public void deleteAllServicesItem() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_Services_Item;
        db.execSQL(str);
        db.close();
    }
    //endregion

    //region Select all services name
    public ArrayList<Services_Items> getallservicesname(){
        ArrayList<Services_Items> service_info = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "select DISTINCT " + DatabaseHandler.COL_Services_Id + ","+
                DatabaseHandler.COL_ServiceName + " from " + DatabaseHandler.TABLE_Services_Item;
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.equals(null)) {
            if (cursor.moveToFirst()) {
                do {
                    service_info.add(getServicesInfo(cursor));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return service_info;
    }

    public ArrayList<Services_Items> getservicenamebyspecialty(String clinic_type,String specialty){
        ArrayList<Services_Items> service_info = new ArrayList<Services_Items>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String query = "select DISTINCT " + DatabaseHandler.COL_Services_Id +"," + DatabaseHandler.COL_ServiceName   + " from "+ DatabaseHandler.TABLE_Services_Item + " where " + DatabaseHandler.COL_Services_Id + " in "+
                "( select "+ DatabaseHandler.COL_Services_Id+ " from "+ DatabaseHandler.TABLE_Services+" where "+ DatabaseHandler.COL_ServiceClinicType
                +"= '"+clinic_type+"' and "+ DatabaseHandler.COL_Specialty_Name+" = '"+specialty+"')";
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.equals(null)) {
            if (cursor.moveToFirst()) {
                do {
                    service_info.add(getServicesInfo(cursor));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return service_info;
    }

    private Services_Items getServicesInfo(Cursor cursor) {
        Services_Items items_info = new Services_Items();
        items_info.setId(cursor.getInt(0));
        items_info.setServices_name(cursor.getString(1));
        return  items_info;
    }
}
