package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;

import sg.com.ehealthassist.ehealthassist.Models.EDocument.CertsCapture;

/**
 * Created by User on 9/7/2017.
 */

public class DBCertsCapture {

    public static final String TAG = "Certscapture";
    private DatabaseHandler mDbHelper;
    private Context mContext;

    public DBCertsCapture(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }
    //region Insert
    public void addCertsLog(CertsCapture certs_list) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = setCertsValues(certs_list);
        db.insert(DatabaseHandler.TABLE_Certs_Capture, null, values);

        db.close();
    }
    private ContentValues setCertsValues(CertsCapture queueLog) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_MemberId, queueLog.getMemberid());
        values.put(DatabaseHandler.COL_Title, queueLog.getTitle());
        values.put(DatabaseHandler.COL_CreateDate, queueLog.getCreatedate());
        values.put(DatabaseHandler.COL_Desc, queueLog.getDesc());
        values.put(DatabaseHandler.COL_IsReview,queueLog.isreview());
        return values;
    }

    public ArrayList<CertsCapture> getCertsCaptureList(String memberid) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        String sql ="SELECT * FROM " + DatabaseHandler.TABLE_Certs_Capture + " where " + DatabaseHandler.COL_MemberId +" = '"+ memberid +"' order by " + DatabaseHandler.COL_CertsID + " desc";
        Cursor cursor = db.rawQuery(sql, null);

        ArrayList<CertsCapture> getdata_lst = new ArrayList<CertsCapture>();
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
    public  CertsCapture getCertbyCreateDate(String createdate){
        String sql = "SELECT * FROM " + DatabaseHandler.TABLE_Certs_Capture + " where " + DatabaseHandler.COL_CreateDate + " = '" + createdate + "'";
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        CertsCapture certs = new CertsCapture();
        Cursor cursor = db.rawQuery(sql,null);

        if (cursor == null){
            cursor.close();
            return null;
        }

        if (cursor.moveToFirst()) {
            certs = cursorgetObject(cursor);

        }
        cursor.close();
        db.close();
        return  certs;
    }
    public void updateRatebycreatedate(String createdDate,String memberid){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "update  " + DatabaseHandler.TABLE_Certs_Capture + "  set "+ DatabaseHandler.COL_IsReview + " = 1"  +
                " WHERE " + DatabaseHandler.COL_CreateDate + " = '" + createdDate + "'  and " +
                DatabaseHandler.COL_MemberId + " = '" + memberid + "'";

        db.execSQL(str);
        db.close();
    }
    public void deleteRatebycreatedate(String createdate,String memberid){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "delete from " + DatabaseHandler.TABLE_Certs_Capture + " WHERE " + DatabaseHandler.COL_CreateDate + " = '" + createdate + "'  and " +
                DatabaseHandler.COL_MemberId + " = '" + memberid + "'";

        db.execSQL(str);
        db.close();
    }
    private CertsCapture cursorgetObject(Cursor cursor) {
        CertsCapture qLog = new CertsCapture();
        qLog.setId(cursor.getInt(0));
        qLog.setMemberid(cursor.getString(1));
        qLog.setTitle(cursor.getString(2));
        qLog.setCreatedate(cursor.getString(3));
        qLog.setDesc(cursor.getString(4));
        qLog.setIsreview(cursor.getInt(5)>0);

        return qLog;
    }
}
