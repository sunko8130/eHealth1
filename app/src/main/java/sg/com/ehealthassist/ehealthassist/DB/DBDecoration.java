package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Models.PHDay.PublicHoliday;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class DBDecoration {
    public static final String TAG = "decoration";
    private DatabaseHandler mDbHelper;
    private Context mContext;

    public DBDecoration(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }

    //region Insert Decoration
    public void addPublicLog(PublicHoliday holiday_list) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = setPublicHolidayValues(holiday_list);
        db.insert(DatabaseHandler.TABLE_HOLIDAY, null, values);

        db.close();
    }
    //endregion

    //region Select last slot
    public ArrayList<PublicHoliday> getallholiday() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseHandler.TABLE_HOLIDAY ;

        ArrayList<PublicHoliday> reqlog = new ArrayList<PublicHoliday>();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    reqlog.add(deserializRQL(cursor));
                }
                while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            cursor.close();
            db.close();
            return reqlog;
        }
    }
    //endregion

    public ArrayList<PublicHoliday> getholidaybyMonth(String date){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseHandler.TABLE_HOLIDAY + " where " + DatabaseHandler.COL_holiday_Date +" like '"+date+"%'" ;

        ArrayList<PublicHoliday> reqlog = new ArrayList<PublicHoliday>();
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    reqlog.add(deserializRQL(cursor));
                }
                while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            cursor.close();
            db.close();
            return reqlog;
        }
    }

    public void deleteallHoliday(){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "Delete from " + DatabaseHandler.TABLE_HOLIDAY;
        db.execSQL(sql);
        db.close();
    }


    private ContentValues setPublicHolidayValues(PublicHoliday queueLog) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_holiday_des, queueLog.getHoliday_description());
        values.put(DatabaseHandler.COL_holiday_Date, queueLog.getHoliday_date());
        return values;
    }

    //region Get a RequestQueuelog from cursor
    private PublicHoliday deserializRQL(Cursor cursor) {
        PublicHoliday qLog = new PublicHoliday();
        qLog.setHoliday_description(cursor.getString(0));
        qLog.setHoliday_date(cursor.getString(1));
        return qLog;
    }
    //endregion
}
