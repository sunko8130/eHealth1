package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalReminder;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class DBMedReminder {
    private DatabaseHandler mDbHelper;
    private Context mContext;

    public DBMedReminder(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }

    public long insertMedicalReminder(MedicalReminder med_reminder) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = setmedremindercontentvalues(med_reminder);
        long affected = db.insert(DatabaseHandler.TABLE_Medical_Reminder, null, values);
        db.close();

        return affected;
    }
    public ContentValues setmedremindercontentvalues(MedicalReminder reminder){
        ContentValues values = new ContentValues();
    //    values.put(DatabaseHandler.COL_ID, reminder.getId());
        values.put(DatabaseHandler.COL_VisitNo, reminder.getVisitno());
        values.put(DatabaseHandler.COL_UUID, reminder.getUuid());
        values.put(DatabaseHandler.COL_IDs, reminder.getIds());
        values.put(DatabaseHandler.COL_Message, reminder.getMessage());
        values.put(DatabaseHandler.COL_freqCode, reminder.getFreqcode());
        values.put(DatabaseHandler.COl_setdate, reminder.getSetdate());

        return values;
    }

    public void deletemedreminderbyvisitno(String visitno){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String query = "Delete  from " + DatabaseHandler.TABLE_Medical_Reminder + " where " + DatabaseHandler.COL_VisitNo + " ='"+visitno+"'";
        db.execSQL(query);
        db.close();
    }
    public void deleteALL(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String query = "Delete  from " + DatabaseHandler.TABLE_Medical_Reminder;
        db.execSQL(query);
        db.close();
    }

    public ArrayList<MedicalReminder> getmedicationReminderbyvisitno(String visitno){
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_Medical_Reminder +
                " where " + DatabaseHandler.COL_VisitNo + " = '" + visitno+"' ";
        return getmedicalreminderlist(query);
    }
    public  ArrayList<MedicalReminder> selectALL(){
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_Medical_Reminder;

        return getmedicalreminderlist(query);
    }

    private ArrayList<MedicalReminder> getmedicalreminderlist(String query) {
        ArrayList<MedicalReminder> meddipsen = new ArrayList<MedicalReminder>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.equals(null)) {
            if (cursor.moveToFirst()) {
                do {
                    meddipsen.add(getmedicalreminder(cursor));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return meddipsen;
    }

    public MedicalReminder getmedicalreminder(Cursor cursor){
        MedicalReminder obj_medDispens = new MedicalReminder();
        obj_medDispens.setId(cursor.getInt(0));
        obj_medDispens.setVisitno(cursor.getString(1));
        obj_medDispens.setUuid(cursor.getString(2));
        obj_medDispens.setIds(cursor.getString(3));
        obj_medDispens.setMessage(cursor.getString(4));
        obj_medDispens.setFreqcode(cursor.getString(5));
        obj_medDispens.setSetdate(cursor.getString(6));
        return obj_medDispens;
    }

}
