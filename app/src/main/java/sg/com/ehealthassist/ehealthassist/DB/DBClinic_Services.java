package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Models.Clinic.Clinic_Services;

/**
 * Created by User on 6/29/2017.
 */

public class DBClinic_Services {
    public static final String TAG = "Clinic_Services";
    private DatabaseHandler mDbHelper;
    private Context mContext;

    public DBClinic_Services(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }


    //region Insert RequestQueuelog
    public void addServices(Clinic_Services clinic_service_obj) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = setServicesValues(clinic_service_obj);
        db.insert(DatabaseHandler.TABLE_Clinic_Services, null, values);
        db.close();

    }
    //endregion
    //region get clinic services by id
    public ArrayList<Clinic_Services> getclinicservicesLog(int clinicid) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql ="SELECT * FROM " + DatabaseHandler.TABLE_Clinic_Services + "  where " + DatabaseHandler.COL_Clinic_id +" = "+ clinicid;
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<Clinic_Services> getdata_lst = new ArrayList<Clinic_Services>();
        if (!cursor.equals(null)) {
            if (cursor.moveToFirst()) {
                do {
                    getdata_lst.add(cursorgetObject(cursor));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return getdata_lst;
    }
    //endregion

    //region Get Clinic Service from cursor
    private Clinic_Services cursorgetObject(Cursor cursor) {
        Clinic_Services qLog = new Clinic_Services();
        qLog.setClinic_id(cursor.getInt(0));
        qLog.setSerivices_name(cursor.getString(1));

        return qLog;
    }
    //endregion

    //region Get contentvalues from RequestQueuelog
    private ContentValues setServicesValues(Clinic_Services queueLog) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_Clinic_id, queueLog.getClinic_id());
        values.put(DatabaseHandler.COL_ClinicService, queueLog.getSerivices_name());
        return values;
    }
    //endregion
    //region Delete all clinic Sevices
    public void deleteClinicServicesbyclinicid(int clinicid) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_Clinic_Services + " where " + DatabaseHandler.COL_Clinic_id + "=" + clinicid;
        db.execSQL(str);
        db.close();
    }
    //endregion
    //region Delete all clinic Sevices
    public void deleteAllClinicServices() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_Clinic_Services;
        db.execSQL(str);
        db.close();
    }
    //endregion
}
