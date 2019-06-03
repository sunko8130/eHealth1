package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class DBSlot { public static final String TAG = "SlotNo";
    private DatabaseHandler mDbHelper;
    private Context mContext;

    public DBSlot(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }
    //region Insert Slot log
    public void addSlotlog(List<String> slotlist) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        for (int i = 0; i < slotlist.size(); i++) {
            ContentValues values = setQueueLogValues(slotlist.get(i));
            db.insert(DatabaseHandler.TABLE_Slot, null, values);
        }
        db.close();
    }
    //endregion

    //region Select last slot
    public String getLastSlot() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_Slot + " ORDER BY " + DatabaseHandler.COL_slottime + " DESC LIMIT 1", null);
        String result = "";
        try {
            if (cursor.moveToFirst()) {
                do {
                    result = deserializRQL(cursor);
                }
                while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            cursor.close();
            db.close();
            return result;
        }
    }
    //endregion
    //region Select FirstSlot
    public String getFirstSlot() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_Slot + " ORDER BY " + DatabaseHandler.COL_slottime + " ASC LIMIT 1", null);
        String result = "";
        try {
            if (cursor.moveToFirst()) {
                do {
                    result = deserializRQL(cursor);
                }
                while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            cursor.close();
            db.close();
            return result;
        }
    }
    //endregion
    //region Select Slotlog list by slottime
    public ArrayList<String> getRequestLog(String request_str) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_Slot + " WHERE " + DatabaseHandler.COL_slottime + " LIKE '" + request_str + "%'", null);
        ArrayList<String> reqlog = new ArrayList<String>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    reqlog.add(deserializRQL(cursor));
                } while (cursor.moveToNext());
            } else {
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

    //region Delete all slot
    public void deleteAllSlot() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_Slot;
        db.execSQL(str);
        db.close();
    }
    //endregion

    //region contentvalues from object
    private ContentValues setQueueLogValues(String queueLog) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_slottime, queueLog);
        return values;
    }
    //endregion
    //region  Get slot from cursor
    private String deserializRQL(Cursor cursor) {
        String result = cursor.getString(0);
        return result;
    }
    //endregion

    //region not use fun
    public boolean updateRQL(String old_data, String new_data) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(DatabaseHandler.COL_slottime, new_data);
        return db.update(DatabaseHandler.TABLE_Slot, args, DatabaseHandler.COL_slottime + "=" + old_data, null) > 0;
    }
    //endregion
}

