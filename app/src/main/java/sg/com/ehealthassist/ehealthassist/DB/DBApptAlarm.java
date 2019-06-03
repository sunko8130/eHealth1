package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Appointment.ApptAlarmLog;

/**
 * Created by User on 6/30/2017.
 */

public class DBApptAlarm {
    public static final String TAG = "Alarm";
    private DatabaseHandler mDbHelper;
    private Context mContext;

    public DBApptAlarm(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }
    //region Select alarmInfo By long_bookid()
    public ApptAlarmLog getalarmlogInfo(String long_bookid, String short_id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = null;
        ApptAlarmLog alarm = new ApptAlarmLog();
        cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_APPT_ALARM + " WHERE " + DatabaseHandler.COL_Long_id + "='" + long_bookid + "' and " +
                DatabaseHandler.COL_Short_id + " = '"+short_id+"'", null);
        if (cursor == null){
            cursor.close();
            return null;
        }

        if (cursor.moveToFirst()) {
            alarm = getAlarmInfo(cursor);
        }
        cursor.close();
        db.close();
        return alarm;
    }
    //endregion
    //region Update alarm by long_bookid()
    public long updateAlarmInfoBylongid(ApptAlarmLog alarminfo) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHandler.TABLE_APPT_ALARM, new String[]{DatabaseHandler.COL_Long_id,DatabaseHandler.COL_Short_id}, DatabaseHandler.COL_Long_id + "=? and "
                        + DatabaseHandler.COL_Short_id + "=?" ,
                new String[]{String.valueOf(alarminfo.getLong_book_id()),String.valueOf(alarminfo.getShort_book_id())}, null, null, null, null);
        ContentValues values = setAlarmInfoValues(alarminfo);
        long affected;
        if (cursor.moveToFirst()) {
            affected = db.update(DatabaseHandler.TABLE_APPT_ALARM, values, DatabaseHandler.COL_Long_id + "=? and " + DatabaseHandler.COL_Short_id +
                    " =?", new String[]{String.valueOf(alarminfo.getLong_book_id()),String.valueOf(alarminfo.getShort_book_id())});
        } else {
            affected = db.insert(DatabaseHandler.TABLE_APPT_ALARM, null, values);
        }
        cursor.close();
        db.close();
        return affected;
    }
    //endregion
    //region Delete alarm by long_bookid()
    public void deleteAlarmbyid(String long_id,String short_id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_APPT_ALARM + " WHERE " + DatabaseHandler.COL_Long_id + " = '" + long_id + "' and "+
                DatabaseHandler.COL_Short_id + " ='"+short_id+"'";
        db.execSQL(str);
        db.close();

    }
    //endregion
    //region Get contentvalues()
    private ContentValues setAlarmInfoValues(ApptAlarmLog alarmLog) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_Long_id, alarmLog.getLong_book_id());
        values.put(DatabaseHandler.COL_Short_id, alarmLog.getShort_book_id());
        values.put(DatabaseHandler.COL_2hours, alarmLog.is_2hr());
        values.put(DatabaseHandler.COL_1day, alarmLog.is_1day());
        values.put(DatabaseHandler.COL_2days, alarmLog.is_2days());
        values.put(DatabaseHandler.COL_1week, alarmLog.is_1week());
        values.put(DatabaseHandler.COL_AlarmID,alarmLog.getAlarm_id());
        values.put(DatabaseHandler.COL_alarm_unit,alarmLog.getAlarm_unit());
        return values;
    }
    //endregion
    //region Get alarm info from cursor()
    private ApptAlarmLog getAlarmInfo(Cursor cursor) {
        ApptAlarmLog alarminfo = new ApptAlarmLog();
        alarminfo.setLong_book_id(cursor.getString(0));
        alarminfo.setShort_book_id(cursor.getString(1));
        alarminfo.set_2hr(cursor.getInt(2) > 0);
        alarminfo.set_1day(cursor.getInt(3) > 0);
        alarminfo.set_2days(cursor.getInt(4) > 0);
        alarminfo.set_1week(cursor.getInt(5) > 0);
        alarminfo.setAlarm_id(cursor.getInt(6));
        alarminfo.setAlarm_unit((cursor.getString(7)==null)?"":cursor.getString(7));

        return alarminfo;
    }
    //endregion


    //region not use fun()
    public long updateAlarmInfos(ApptAlarmLog alarminfo) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHandler.TABLE_APPT_ALARM, new String[]{DatabaseHandler.COL_Long_id,DatabaseHandler.COL_Short_id}, DatabaseHandler.COL_Long_id + "=? and "+
                        DatabaseHandler.COL_Short_id + " =?"
                , new String[]{String.valueOf(alarminfo.getLong_book_id()),String.valueOf(alarminfo.getShort_book_id())}, null, null, null, null);
        ContentValues values = setAlarmInfoValues(alarminfo);
        long affected;
        if (cursor.moveToFirst()) {
            affected = db.update(DatabaseHandler.TABLE_APPT_ALARM, values, DatabaseHandler.COL_Long_id + "=? and " + DatabaseHandler.COL_Short_id + " =?",
                    new String[]{String.valueOf(alarminfo.getLong_book_id()),String.valueOf(alarminfo.getShort_book_id())});
            // affected = 1;
        } else {
            affected = db.insert(DatabaseHandler.TABLE_APPT_ALARM, null, values);
        }
        cursor.close();
        db.close();
        return affected;
    }


    public void deleteAllAlarmby() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_APPT_ALARM;
        db.execSQL(str);
        db.close();
    }
    private List<ApptAlarmLog> getalarmLogsByQuery(String query) {
        List<ApptAlarmLog> clinicInfos = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.equals(null)) {
            if (cursor.moveToFirst()) {
                do {
                    clinicInfos.add(getAlarmInfo(cursor));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return clinicInfos;
    }
    public void updatealarm_id(String long_id,String short_id,int alarm){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "UPDATE  " + DatabaseHandler.TABLE_APPT_ALARM +  " SET "+ DatabaseHandler.COL_AlarmID +"= "+alarm+" WHERE " + DatabaseHandler.COL_Long_id + " = '" + long_id + "' and" +
                DatabaseHandler.COL_Short_id + " = '"+short_id+"'";
        db.execSQL(str);
        db.close();
    }
    //endregion

}
