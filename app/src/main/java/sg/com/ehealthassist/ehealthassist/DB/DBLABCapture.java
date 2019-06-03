package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Models.EDocument.LABCapture;

/**
 * Created by User on 9/7/2017.
 */

public class DBLABCapture {
    public static final String TAG = "labcapture";
    private DatabaseHandler mDbHelper;
    private Context mContext;

    public DBLABCapture(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }
    //region Insert
    public void addLABLog(LABCapture lab) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = setLABValues(lab);
        db.insert(DatabaseHandler.TABLE_LAB_Capture, null, values);

        db.close();
    }

    private ContentValues setLABValues(LABCapture queueLog) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_MemberId, queueLog.getMemberid());
        values.put(DatabaseHandler.COL_Title, queueLog.getTitle());
        values.put(DatabaseHandler.COL_CreateDate, queueLog.getCreatedate());
        values.put(DatabaseHandler.COL_Desc, queueLog.getDesc());
        values.put(DatabaseHandler.COL_IsReview,queueLog.isreview());
        return values;
    }
    //endregion

    public ArrayList<LABCapture> getLabCaptureList(String memberid) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql ="SELECT * FROM " + DatabaseHandler.TABLE_LAB_Capture + " where " + DatabaseHandler.COL_MemberId +" = '"+ memberid +"' order by " + DatabaseHandler.COL_LabID + " desc";
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<LABCapture> getdata_lst = new ArrayList<LABCapture>();
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

    public  LABCapture getLabbyCreateDate(String createdate){
        String sql = "SELECT * FROM " + DatabaseHandler.TABLE_LAB_Capture + " where " + DatabaseHandler.COL_CreateDate + " = '" + createdate + "'";
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        LABCapture lab = new LABCapture();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor == null){
                cursor.close();
                return null;
        }

        if (cursor.moveToFirst()) {
            lab = cursorgetObject(cursor);

        }
        cursor.close();
        db.close();
        return  lab;
    }

    public void updateRatebycreatedate(String createdDate,String memberid){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "update  " + DatabaseHandler.TABLE_LAB_Capture + "  set "+ DatabaseHandler.COL_IsReview + " = 1" +
                " WHERE " + DatabaseHandler.COL_CreateDate + " = '" + createdDate + "'  and " +
                DatabaseHandler.COL_MemberId + " = '" + memberid + "'";

        Log.e("update lab",str);

        db.execSQL(str);
        db.close();
    }
    public void deleteRatebycreatedate(String createdDate,String memberid){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "delete from   " + DatabaseHandler.TABLE_LAB_Capture +
                " WHERE " + DatabaseHandler.COL_CreateDate + " = '" + createdDate + "'  and " +
                DatabaseHandler.COL_MemberId + " = '" + memberid + "'";

        Log.e("update lab",str);

        db.execSQL(str);
        db.close();
    }

    private LABCapture cursorgetObject(Cursor cursor) {
        LABCapture qLog = new LABCapture();
        qLog.setId(cursor.getInt(0));
        qLog.setMemberid(cursor.getString(1));
        qLog.setTitle(cursor.getString(2));
        qLog.setCreatedate(cursor.getString(3));
        qLog.setDesc(cursor.getString(4));
        qLog.setIsreview(cursor.getInt(5)>0);

        return qLog;
    }


}
