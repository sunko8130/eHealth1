package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalDispense;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class DBMedDispense {private DatabaseHandler mDbHelper;
    private Context mContext;

    public DBMedDispense(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }

    public long updatemedicalDispense(MedicalDispense medicalobj){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursor = db.query(DatabaseHandler.TABLE_MEDICAL_DISPENSE, new String[]{DatabaseHandler.COL_VisitNo
        }, DatabaseHandler.COL_VisitNo + "=?", new String[]{String.valueOf(medicalobj.getVisitNo())
        }, null, null, null, null);

        ContentValues values = setMedicaldispenseValues(medicalobj);
        long affected;
        if (cursor.moveToFirst()) {
            affected = db.update(DatabaseHandler.TABLE_MEDICAL_DISPENSE, values, DatabaseHandler.COL_VisitNo + "=?", new String[]{String.valueOf(medicalobj.getVisitNo())});
        } else {
            affected = db.insert(DatabaseHandler.TABLE_MEDICAL_DISPENSE, null, values);
        }
        cursor.close();
        db.close();
        return affected;
    }

    public ArrayList<MedicalDispense> getallmedicalDispense(){
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_MEDICAL_DISPENSE;
        return getmedDispenseByQuery(query);
    }

    private ArrayList<MedicalDispense> getmedDispenseByQuery(String query) {
        ArrayList<MedicalDispense> meddipsen = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.equals(null)) {
            if (cursor.moveToFirst()) {
                do {
                    meddipsen.add(getMedicalDispense(cursor));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return meddipsen;
    }
    private ContentValues setMedicaldispenseValues(MedicalDispense med) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_MEMBERID, med.getMemberid());
        values.put(DatabaseHandler.COL_NRIC, med.getNric());
        values.put(DatabaseHandler.COL_NRIC_Type, med.getNrictype());
        values.put(DatabaseHandler.COL_DOB, med.getDOB());
        values.put(DatabaseHandler.COL_QueueID, med.getQueueID());
        values.put(DatabaseHandler.COL_VisitNo, med.getVisitNo());
        values.put(DatabaseHandler.COL_VisitDate, med.getVisitDate());
        values.put(DatabaseHandler.COL_Clinic_id, med.getClinicId());
        values.put(DatabaseHandler.COL_Patientname, med.getPatientName());

   /*     values.put(DatabaseHandler.COL_IsReminder, med.getIsReminder());
        values.put(DatabaseHandler.COL_StartDate, med.getStartDate());
        values.put(DatabaseHandler.COL_StartTime, med.getStarttime());
        values.put(DatabaseHandler.COL_NumofDays, med.getNumofDays());*/

        return values;
    }


    private MedicalDispense getMedicalDispense(Cursor cursor) {
        MedicalDispense obj_medDispens = new MedicalDispense();
        obj_medDispens.setMemberid(cursor.getString(0));
        obj_medDispens.setNric(cursor.getString(1));
        obj_medDispens.setNrictype(cursor.getString(2));
        obj_medDispens.setDOB(cursor.getString(3));
        obj_medDispens.setQueueID(cursor.getString(4));
        obj_medDispens.setVisitNo(cursor.getString(5));
        obj_medDispens.setVisitDate(cursor.getString(6));
        obj_medDispens.setClinicId(cursor.getString(7));
       /* obj_medDispens.setIsReminder(cursor.getString(8));
        obj_medDispens.setStartDate(cursor.getString(9));
        obj_medDispens.setStarttime(cursor.getString(10));
        obj_medDispens.setNumofDays(cursor.getString(11));*/
        obj_medDispens.setPatientName(cursor.getString(8));

        return obj_medDispens;
    }

    public ArrayList<String> getclinicidbyvisitdate(){
        String query = "select "+DatabaseHandler.COL_Clinic_id+" from "+ DatabaseHandler.TABLE_MEDICAL_DISPENSE+"  group by "
                +DatabaseHandler.COL_Clinic_id+" order by "+DatabaseHandler.COL_VisitDate +" desc";
        ArrayList<String> clinicidlst = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.equals(null)) {
            if (cursor.moveToFirst()) {
                do {
                    clinicidlst.add(getidfrcursor(cursor));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return clinicidlst;
    }

    public String getidfrcursor(Cursor cursor){
        String id ="";
        id = cursor.getString(0);
        return  id;
    }

    public ArrayList<MedicalDispense> getMedicationbyLVisitDate(String clinicid){
        String query = "select m.* from "+ DatabaseHandler.TABLE_MEDICAL_DISPENSE + " m where "+ DatabaseHandler.COL_Clinic_id  + " = " +clinicid+
                " order by  "+DatabaseHandler.COL_VisitDate +"  desc";
        return getmedDispenseByQuery(query);
    }

    public void updateReminderEnable(String visitno,String isreminder){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String query = "update "+ DatabaseHandler.TABLE_MEDICAL_DISPENSE + " set " +
                DatabaseHandler.COL_IsReminder + " = '"+ isreminder+"' where " + DatabaseHandler.COL_VisitNo + "= '"+visitno+"'";

        Log.e("update rminder",query);
        db.execSQL(query);
        db.close();

    }
    public void updatestartdate(String visitno,String date){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String query = "update "+ DatabaseHandler.TABLE_MEDICAL_DISPENSE + " set "
                +DatabaseHandler.COL_StartDate+ "='"+date +"' where " + DatabaseHandler.COL_VisitNo + "= '"+visitno+"'";

        Log.e("update rminder",query);
        db.execSQL(query);
        db.close();

    }
    public void updatestarttime(String visitno,String time){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String query = "update "+ DatabaseHandler.TABLE_MEDICAL_DISPENSE + " set " +
                DatabaseHandler.COL_StartTime + " = '"+ time+"'"+
                " where " + DatabaseHandler.COL_VisitNo + "= '"+visitno+"'";

        Log.e("update rminder",query);
        db.execSQL(query);
        db.close();

    }
    public void updatenumberofdays(String visitno,String days){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String query = "update "+ DatabaseHandler.TABLE_MEDICAL_DISPENSE + " set " +
                DatabaseHandler.COL_NumofDays + " = '"+ days+"'"+
                " where " + DatabaseHandler.COL_VisitNo + "= '"+visitno+"'";

        Log.e("update rminder",query);
        db.execSQL(query);
        db.close();

    }
    public void deletemedicaldispense(String visitno){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String query = "Delete  from " + DatabaseHandler.TABLE_MEDICAL_DISPENSE + " where " + DatabaseHandler.COL_VisitNo + " ='"+visitno+"'";
        db.execSQL(query);
        db.close();
    }
    public void deleteallmedicaldispense(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String query = "Delete  from " + DatabaseHandler.TABLE_MEDICAL_DISPENSE ;
        db.execSQL(query);
        db.close();
    }



}

