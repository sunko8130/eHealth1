package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Models.Queue.RequestQueueLog;
import sg.com.ehealthassist.ehealthassist.Other.Utils;

/**
 * Created by User on 6/30/2017.
 */

public class DBQueueRequest {
    public static final String TAG = "QueueRequestNo";
    // Database fields
    private DatabaseHandler mDbHelper;
    private Context mContext;
    public DBQueueRequest(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }
    //region Insert RequestQueuelog
    public void addRequestLog(RequestQueueLog request_log) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = setQueueLogValues(request_log);
        db.insert(DatabaseHandler.TABLE_Queue_Request_Log, null, values);
        db.close();
    }
    //endregion

    //region Select All Requestlog lists
    public ArrayList<RequestQueueLog> getRequestLog(String usertoken){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sqlstr = "";
        if(!usertoken.equals("")){
            sqlstr = "SELECT * FROM " + DatabaseHandler.TABLE_Queue_Request_Log + " where " + DatabaseHandler.COL_Usertoken +
                    " ='"+usertoken+"' order by " + DatabaseHandler.COL_Request_ID + "  DESC";
        }
        else{
            sqlstr = "SELECT * FROM " + DatabaseHandler.TABLE_Queue_Request_Log +  " order by " + DatabaseHandler.COL_Request_ID + "  DESC";
        }
        Cursor cursor = db.rawQuery(sqlstr, null);
        ArrayList<RequestQueueLog> reqlog = new ArrayList<RequestQueueLog>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    reqlog.add(deserializRQL(cursor));
                } while (cursor.moveToNext());
            } else {
            }
        } catch (Exception ex) {

        } finally {
            cursor.close();
            db.close();
            return reqlog;
        }
    }
    //endregion
    //region Select a RequestQueuelog by requestid
    public RequestQueueLog getResponseQueue(String requestid) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        RequestQueueLog queueLog = new RequestQueueLog();

        Cursor  cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_Queue_Request_Log + " WHERE " + DatabaseHandler.COL_Request_ID + "='" + requestid +"'", null);
        try{
            if(cursor.moveToFirst()){
                queueLog = deserializRQL(cursor);
            }
        }catch (Exception ex){

        }
        finally {
            cursor.close();
            db.close();
            return queueLog;
        }

    }
    //endregion
    //region Delete RequestQueuelog by request_id
    public void deleteQueuebyRequestId(String requests_id ){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_Queue_Request_Log + " where " + DatabaseHandler.COL_Request_ID +" = '" + requests_id +"'";
        db.execSQL(str);
        db.close();
    }
    //endregion
    //region Delete RequestQueuelog by Current Date
    public void deleteQueuebyCurrentDate(String curdate){
        ArrayList<RequestQueueLog> requestqlog = getRequestLog("");
        for(int i = 0; i< requestqlog.size();i++){
            String logdate = requestqlog.get(i).getRequest_date();
            if(Utils.comparedate(logdate,curdate,"dd-MMM-yyyy hh:mm a","dd-MMM-yyyy")>0){
                deleteQueuebyRequestId(requestqlog.get(i).getRequest_id());
            }

        }
    }

    //endregion

    //region Get a RequestQueuelog from cursor
    private RequestQueueLog deserializRQL(Cursor cursor) {
        RequestQueueLog qLog = new RequestQueueLog();
        qLog.setId(cursor.getInt(0));
        qLog.setRequest_nric(cursor.getString(1));
        qLog.setUser_token(cursor.getString(2));
        qLog.setRequest_date(cursor.getString(3));
        qLog.setClinic_id(cursor.getInt(4));
        qLog.setDob(cursor.getString(5));
        qLog.setClinic_name(cursor.getString(6));
        qLog.setRequest_id(cursor.getString(7));
        qLog.setQueue_no(cursor.getInt(8));
        qLog.setDescription(cursor.getString(9));
        qLog.setQmessage(cursor.getString(10));
        qLog.setQstatus(cursor.getString(11));
        qLog.setRequest_name(cursor.getString(12));
        return qLog;
    }
    //endregion
    //region Get contentvalues from RequestQueuelog
    private ContentValues setQueueLogValues(RequestQueueLog queueLog) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_Clinic_id, queueLog.getClinic_id());
        values.put(DatabaseHandler.COL_Request_Nric, queueLog.getRequest_nric());
        values.put(DatabaseHandler.COL_RequestorName,queueLog.getRequest_name());
        values.put(DatabaseHandler.COL_Usertoken, queueLog.getUser_token());
        values.put(DatabaseHandler.COL_Request_Date, queueLog.getRequest_date());
        values.put(DatabaseHandler.COL_Clinic_id, queueLog.getClinic_id());
        values.put(DatabaseHandler.COL_DOB, queueLog.getDob());
        values.put(DatabaseHandler.COL_Clinic_name, queueLog.getClinic_name());
        values.put(DatabaseHandler.COL_Request_ID,queueLog.getRequest_id());
        values.put(DatabaseHandler.COL_Queue_no, queueLog.getQueue_no());
        values.put(DatabaseHandler.COL_Description, queueLog.getDescription());
        values.put(DatabaseHandler.COL_QMessage, queueLog.getQmessage());
        values.put(DatabaseHandler.COL_QSTATUS, queueLog.getQstatus());

        return values;
    }
    //endregion
    //region Exist/NotExist queuelog by request_id
    public int CheckRequestid(String requestid){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        int valid=0;
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_Queue_Request_Log + " where "+ DatabaseHandler.COL_Request_ID + "= '" + requestid + "'", null);
        try{
            if(cursor.moveToFirst()){
                valid=cursor.getCount();
            }
        }catch (Exception ex){
            ex.toString();
        }finally {
            cursor.close();
            db.close();
        }
        return valid;
    }
    //endregion

}
