package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Models.EDocument.RADCapture;

/**
 * Created by User on 9/7/2017.
 */

public class DBRADCapture {
    public static final String TAG = "RADcapture";
    private DatabaseHandler mDbHelper;
    private Context mContext;
    public DBRADCapture(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }
    //region Insert
    public void addRABLog(RADCapture rad_list) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = setRADValues(rad_list);
        db.insert(DatabaseHandler.TABLE_Rad_Capture, null, values);

        db.close();
    }
    private ContentValues setRADValues(RADCapture queueLog) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_MemberId, queueLog.getMemberid());
        values.put(DatabaseHandler.COL_Title, queueLog.getTitle());
        values.put(DatabaseHandler.COL_CreateDate, queueLog.getCreatedate());
        values.put(DatabaseHandler.COL_Desc, queueLog.getDesc());
        values.put(DatabaseHandler.COL_IsReview,queueLog.isreview());

        return values;
    }
    //endregion
    public ArrayList<RADCapture> getRadCaptureList(String memberid) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql ="SELECT * FROM " + DatabaseHandler.TABLE_Rad_Capture + " where " + DatabaseHandler.COL_MemberId +" = '"+ memberid +"' order by " + DatabaseHandler.COL_RADID + " desc";
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<RADCapture> getdata_lst = new ArrayList<RADCapture>();
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
    public  RADCapture getRadbyCreateDate(String createdate){
        String sql = "SELECT * FROM " + DatabaseHandler.TABLE_Rad_Capture + " where " + DatabaseHandler.COL_CreateDate + " = '" + createdate + "'";
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        RADCapture rad = new RADCapture();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor == null){
            cursor.close();
            return null;
        }

        if (cursor.moveToFirst()) {
            rad = cursorgetObject(cursor);

        }
        cursor.close();
        db.close();
        return  rad;
    }
    public void updateRatebycreatedate(String createdDate,String memberid){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "update  " + DatabaseHandler.TABLE_Rad_Capture + "  set "+ DatabaseHandler.COL_IsReview + " =1 " +
                " WHERE " + DatabaseHandler.COL_CreateDate + " = '" + createdDate + "'  and " +
                DatabaseHandler.COL_MemberId + " = '" + memberid + "'";


        db.execSQL(str);
        db.close();
    }
    public void deleteRatebycreatedate(String createdDate,String memberid){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "delete from  " + DatabaseHandler.TABLE_Rad_Capture +
                " WHERE " + DatabaseHandler.COL_CreateDate + " = '" + createdDate + "'  and " +
                DatabaseHandler.COL_MemberId + " = '" + memberid + "'";
        db.execSQL(str);
        db.close();
    }
    private RADCapture cursorgetObject(Cursor cursor) {
        RADCapture qLog = new RADCapture();
        qLog.setId(cursor.getInt(0));
        qLog.setMemberid(cursor.getString(1));
        qLog.setTitle(cursor.getString(2));
        qLog.setCreatedate(cursor.getString(3));
        qLog.setDesc(cursor.getString(4));
        qLog.setIsreview(cursor.getInt(5)>0);

        return qLog;
    }
}
