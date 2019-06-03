package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Models.Appointment.BookInfo;

/**
 * Created by User on 6/30/2017.
 */

public class DBBookInfo {
    public static final String TAG = "Book";
    private DatabaseHandler mDbHelper;
    private Context mContext;

    public DBBookInfo(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }

    //region Select all bookinfo list()
    public ArrayList<BookInfo> getRequestLog() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_BOOK + " ORDER BY " + DatabaseHandler.COL_Book_Date + " ASC ", null);
        ArrayList<BookInfo> getdata = new ArrayList<BookInfo>();
        if (cursor.moveToFirst()) {
            getdata = cursorgetObject(cursor);
        }
        cursor.close();
        db.close();
        return getdata;
    }

    //endregion
    //region Select a bookinfo by long_bookid()
    public BookInfo getBooklongId(String longid, String shortbook_id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = null;
        BookInfo medprofile = null;
        cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_BOOK + " WHERE " + DatabaseHandler.COL_Long_id + " = '" + longid + "' and " + DatabaseHandler.COL_Short_id + "=" +
                "'" + shortbook_id + "'", null);
        if (cursor == null){
            cursor.close();
            return null;
        }

        if (cursor.moveToFirst()) {
            medprofile = deserializRQL(cursor);

        }
        cursor.close();
        db.close();
        return medprofile;
    }

    //endregion
    //region Update bookinfo by Long_id()
    public long updatebookInfos(BookInfo info) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String sql = "select * from " + DatabaseHandler.TABLE_BOOK + " where " + DatabaseHandler.COL_Long_id + "='" + info.getLong_id() +
                "' and " + DatabaseHandler.COL_Short_id + " = '" + info.getShort_id() + "'";
        Cursor cursor = db.rawQuery(sql, null);
        ContentValues values = setBookInfoValues(info);
        long affected;
        if (cursor.moveToFirst()) {
            affected = db.update(DatabaseHandler.TABLE_BOOK, values, DatabaseHandler.COL_Long_id + "=? and " + DatabaseHandler.COL_Short_id + "=?",
                    new String[]{String.valueOf(info.getLong_id()), String.valueOf(info.getShort_id())});

        } else {
            affected = db.insert(DatabaseHandler.TABLE_BOOK, null, values);
        }
        cursor.close();
        db.close();
        return affected;
    }

    //endregion
    //region Delete bookinfo by long_bookid()
    public void deletebookById(String longid, String shortbook_id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_BOOK + " WHERE " + DatabaseHandler.COL_Long_id + " = '" + longid + "' and " + DatabaseHandler.COL_Short_id + "='" + shortbook_id + "'";
        db.execSQL(str);
        db.close();
    }

    //endregion
    //region Delete all bookinfos
    public void deleteAllBooks() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_BOOK;
        db.execSQL(str);
        db.close();
    }
    //endregion

    //region Get contentvalues()
    private ContentValues setBookInfoValues(BookInfo bookLog) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_Clinic_id, bookLog.getClinic_id());
        values.put(DatabaseHandler.COL_Clinic_name, bookLog.getClinic_name());
        values.put(DatabaseHandler.COL_Doctor_id, bookLog.getDoctor_id());
        values.put(DatabaseHandler.COL_Doctor_name, bookLog.getDoctor_name());
        values.put(DatabaseHandler.COL_Book_Date, bookLog.getBook_date());
        values.put(DatabaseHandler.COL_Short_id, bookLog.getShort_id());
        values.put(DatabaseHandler.COL_Long_id, bookLog.getLong_id());
        values.put(DatabaseHandler.COL_Profile, bookLog.getDoc_profilepic());
        values.put(DatabaseHandler.COL_Remark, bookLog.getDoc_remark());
        values.put(DatabaseHandler.COL_RequestorNric, bookLog.getRequestor_nric());
        values.put(DatabaseHandler.COL_RequestorName, bookLog.getRequestor_name());
        values.put(DatabaseHandler.COL_STATUS, bookLog.getApp_status());

        return values;
    }

    //endregion
    //region Get bookinfo list from cursor()
    public ArrayList<BookInfo> cursorgetObject(Cursor cursor) {
        ArrayList<BookInfo> reqlog = new ArrayList<BookInfo>();
        try {
            if (cursor.moveToFirst()) {
                do {
                    reqlog.add(deserializRQL(cursor));
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            cursor.close();
            ex.printStackTrace();
        } finally {
            cursor.close();
            return reqlog;
        }
    }

    //endregion
    //region Get a bookinfo from cursor()
    private BookInfo deserializRQL(Cursor cursor) {
        BookInfo book = new BookInfo();
        book.setId(cursor.getInt(0));
        book.setClinic_id(cursor.getInt(1));
        book.setClinic_name(cursor.getString(2));
        book.setDoctor_id(cursor.getInt(3));
        book.setDoctor_name(cursor.getString(4));
        book.setBook_date(cursor.getString(5));
        book.setShort_id(cursor.getString(6));
        book.setLong_id(cursor.getString(7));
        book.setDoc_profilepic(cursor.getString(8));
        book.setDoc_remark(cursor.getString(9));
        book.setRequestor_nric(cursor.getString(10));
        book.setRequestor_name(cursor.getString(11));
        book.setApp_status(cursor.getString(12));

        return book;
    }
    //endregion

    //region not use fun()
    public void addBooklog(BookInfo booklist) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = setBookInfoValues(booklist);
        db.insert(DatabaseHandler.TABLE_BOOK, null, values);
        db.close();
    }

    public ArrayList<BookInfo> getBookInfoByTime(String date) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_BOOK + "  where " + DatabaseHandler.COL_Book_Date + " >= '" + date + "' DESC", null);
        ArrayList<BookInfo> getdata = new ArrayList<BookInfo>();
        if (cursor.moveToFirst()) {
            getdata = cursorgetObject(cursor);
        }
        cursor.close();
        db.close();
        return getdata;
    }
    //endregion
}
