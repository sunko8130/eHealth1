package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalItem;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class DBMedicalItem { private DatabaseHandler mDbHelper;
    private Context mContext;

    public DBMedicalItem(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }

    public long updatemedicalItem(MedicalItem meditemobj){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Log.e("save medical item",meditemobj.getMedicalFreq() + " ," +meditemobj.getMedicalFreqCode());
        Cursor cursor = db.query(DatabaseHandler.TABLE_MEDICAL_Item, new String[]{DatabaseHandler.COL_ID}, DatabaseHandler.COL_ID + "=? "
                , new String[]{String.valueOf(meditemobj.getId())}, null, null, null, null);

        ContentValues values = setMedicalItemContentValues(meditemobj);
        long affected;
        if (cursor.moveToFirst()) {

            affected = db.update(DatabaseHandler.TABLE_MEDICAL_Item, values, DatabaseHandler.COL_ID + "=?", new String[]{String.valueOf(meditemobj.getId())});
        } else {
            affected = db.insert(DatabaseHandler.TABLE_MEDICAL_Item, null, values);
        }
        cursor.close();
        db.close();
        return affected;
    }

    public ArrayList<MedicalItem> getmedicalItems(String visitno){
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_MEDICAL_Item + " where " + DatabaseHandler.COL_VisitNo + " = '" + visitno+"'";
        return getmedDispenseByQuery(query);
    }

    public ArrayList<MedicalItem> getallmedicalItems(){
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_MEDICAL_Item ;
        return getmedDispenseByQuery(query);
    }
    public ArrayList<MedicalItem> getvalidallmedicalitem(){
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_MEDICAL_Item + " where " + DatabaseHandler.COL_isremider+ " = 1 ";
        return getmedDispenseByQuery(query);
    }
    public ArrayList<MedicalItem> getmedicalitemsbyAsc(){
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_MEDICAL_Item + " where " + DatabaseHandler.COL_isremider+ " = 1 and " + DatabaseHandler.COL_NextDateTime
                +" !='' order by " + DatabaseHandler.COL_NextDateTime ;
        return getmedDispenseByQuery(query);
    }

    public ArrayList<MedicalItem> selectAllValidReminder(Integer startdate){
       /* String query = "SELECT * FROM "+ DatabaseHandler.TABLE_MEDICAL_Item +" WHERE "+DatabaseHandler.COL_isremider+"= 1 AND " +
                "(" +
                " (("+DatabaseHandler.COL_StartDate+"<= "+startdate+" And "+DatabaseHandler.COL_StartDate+"!=0) AND " +
                " ("+DatabaseHandler.COL_EndDate+">= "+startdate+" And "+DatabaseHandler.COL_EndDate+"!=0)) " +
                "OR " +
                "(("+DatabaseHandler.COL_StartDate+"<= "+startdate+" AND "+DatabaseHandler.COL_StartDate+"!=0) And "+DatabaseHandler.COL_EndDate+"=0)" +
                " OR " +
                "(("+DatabaseHandler.COL_StartDate+">="+startdate+" AND "+DatabaseHandler.COL_StartDate+"!=0) AND "+DatabaseHandler.COL_EndDate+" = 0) " +
                " OR" +
                 " (("+DatabaseHandler.COL_StartDate+">="+startdate+" AND "+DatabaseHandler.COL_StartDate+"!=0) AND ("+DatabaseHandler.COL_EndDate+" !=0 " +
                " And "+DatabaseHandler.COL_EndDate+" >= "+startdate+"))" +
                " )"
                ;*/
        String query = "SELECT * FROM "+ DatabaseHandler.TABLE_MEDICAL_Item +" WHERE "+DatabaseHandler.COL_isremider+"= 1 ";
        return getmedDispenseByQuery(query);
    }
    public ArrayList<MedicalItem> selectSmallestValidReminder(){
        String query = "SELECT * FROM "+DatabaseHandler.TABLE_MEDICAL_Item +" WHERE "+DatabaseHandler.COL_isremider+"=1 AND "+DatabaseHandler.COL_NextDateTime+"!=0 order by " +
                DatabaseHandler.COL_NextDateTime+" asc Limit 1";
        return getmedDispenseByQuery(query);
    }
    public ArrayList<MedicalItem> selectMoreSmallestValidReminder(Long nextDateTime){
        String query = "SELECT * FROM "+DatabaseHandler.TABLE_MEDICAL_Item +" WHERE "+DatabaseHandler.COL_isremider+"=1 AND "+DatabaseHandler.COL_NextDateTime+"!=0 " +
                "AND "+DatabaseHandler.COL_NextDateTime+"=" + nextDateTime;
        return getmedDispenseByQuery(query);
    }

    public ArrayList<MedicalItem> getmedItemsbyGroup(String visitno){
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_MEDICAL_Item +
                " where " + DatabaseHandler.COL_VisitNo + " = '" + visitno+"' group by "+ DatabaseHandler.COL_medicalFreqCode;
        return getmedDispenseByQuery(query);
    }
    public ArrayList<MedicalItem> getmedItemsbymedfreqCode(String visitno,String freqcode){
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_MEDICAL_Item +
                " where " + DatabaseHandler.COL_VisitNo + " = '" + visitno+"' and " + DatabaseHandler.COL_medicalFreqCode + "= '"+freqcode+"'";

        return getmedDispenseByQuery(query);
    }
    public  ArrayList<MedicalItem> select(String id, String  itmeno){
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_MEDICAL_Item +
                " where " + DatabaseHandler.COL_ID + " = '" + id+"' and " + DatabaseHandler.COL_itemno + " = '"+itmeno+"'";
        return getmedDispenseByQuery(query);
    }
    public  void updateAllnextDateTime(Long nextDateTime){
        String query = "update " + DatabaseHandler.TABLE_MEDICAL_Item +
                " set " + DatabaseHandler.COL_NextDateTime + " = " + nextDateTime;
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(query);
        db.close();
    }

    public  long updateMedicalItemReminder(MedicalItem medical){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        Cursor cursor = db.query(DatabaseHandler.TABLE_MEDICAL_Item, new String[]{DatabaseHandler.COL_ID}, DatabaseHandler.COL_ID + "=? "
                , new String[]{String.valueOf(medical.getId())}, null, null, null, null);

        ContentValues values = setMedicalItemContentValues(medical);
        long affected = 0L;
        if (cursor.moveToFirst()) {
            affected = db.update(DatabaseHandler.TABLE_MEDICAL_Item, values, DatabaseHandler.COL_ID + "=?", new String[]{String.valueOf(medical.getId())});
        }
        cursor.close();
        db.close();
        return affected;
    }

    public void deletemedicalitem(String visitno){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String query = "Delete  from " + DatabaseHandler.TABLE_MEDICAL_Item + " where " + DatabaseHandler.COL_VisitNo + " ='"+visitno+"'";
        db.execSQL(query);
        db.close();
    }
    public void deleteallmedicalitem(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String query = "Delete  from " + DatabaseHandler.TABLE_MEDICAL_Item ;
        db.execSQL(query);
        db.close();
    }
    public void deletemeditembyid(String id,String visitno){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String query = "Delete  from " + DatabaseHandler.TABLE_MEDICAL_Item + " where " + DatabaseHandler.COL_VisitNo + " = '"+ visitno+"' and "+
                DatabaseHandler.COL_ID + " = '"+id + "'";
        db.execSQL(query);
        db.close();
    }

    private ArrayList<MedicalItem> getmedDispenseByQuery(String query) {
        ArrayList<MedicalItem> meddipsen = new ArrayList<MedicalItem>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.equals(null)) {
            if (cursor.moveToFirst()) {
                do {
                    meddipsen.add(getMedicalItem(cursor));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();
        db.close();
        return meddipsen;
    }

    private MedicalItem getMedicalItem(Cursor cursor) {
        MedicalItem obj_medDispens = new MedicalItem();
        obj_medDispens.setVisitno(cursor.getString(0));
        obj_medDispens.setClinicid(cursor.getString(1));
        obj_medDispens.setId(cursor.getString(2));
        obj_medDispens.setItemno(cursor.getString(3));
        obj_medDispens.setMedicalType(cursor.getString(4));
        obj_medDispens.setMedicalCode(cursor.getString(5));
        obj_medDispens.setMedicalName(cursor.getString(6));
        obj_medDispens.setMedicalUsage(cursor.getString(7));
        obj_medDispens.setMedicalDosage(cursor.getString(8));
        obj_medDispens.setMedicalDosageUnit(cursor.getString(9));
        obj_medDispens.setMedicalFreq(cursor.getString(10));
        obj_medDispens.setMedicalFreqCode(cursor.getString(11));
        obj_medDispens.setMedicalTotalQty(cursor.getString(12));
        obj_medDispens.setMedicalTotalQtyUnit(cursor.getString(13));
        obj_medDispens.setPreCaution1(cursor.getString(14));
        obj_medDispens.setPreCaution2(cursor.getString(15));
        obj_medDispens.setPreCaution3(cursor.getString(16));
        obj_medDispens.setDownloaded(cursor.getString(17));
        obj_medDispens.setIs_reminder(cursor.getInt(18));
        obj_medDispens.setStartdate(cursor.getInt(19)) ;
        obj_medDispens.setEnddate(cursor.getInt(20));
        obj_medDispens.setNextdate(cursor.getLong(21));
        obj_medDispens.setNumberofdays(cursor.getInt(22));
        obj_medDispens.setSlotinterval(cursor.getString(23));

        return obj_medDispens;
    }

    public  ContentValues setMedicalItemContentValues(MedicalItem medItem){
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_VisitNo, medItem.getVisitno());
        values.put(DatabaseHandler.COL_Clinic_id, medItem.getClinicid());
        values.put(DatabaseHandler.COL_ID, medItem.getId());
        values.put(DatabaseHandler.COL_itemno, medItem.getItemno());
        values.put(DatabaseHandler.COL_medicalType, medItem.getMedicalType());
        values.put(DatabaseHandler.COL_medicalCode, medItem.getMedicalCode());
        values.put(DatabaseHandler.COL_medicalName, medItem.getMedicalName());
        values.put(DatabaseHandler.COL_medicalUsage, medItem.getMedicalUsage());
        values.put(DatabaseHandler.COL_medicalDosage, medItem.getMedicalDosage());
        values.put(DatabaseHandler.COL_medicalDosageUnit, medItem.getMedicalDosageUnit());
        values.put(DatabaseHandler.COL_medicalFreq, medItem.getMedicalFreq());
        values.put(DatabaseHandler.COL_medicalFreqCode, medItem.getMedicalFreqCode());
        values.put(DatabaseHandler.COL_medicalTotalQty, medItem.getMedicalTotalQty());
        values.put(DatabaseHandler.COL_medicalTotalQtyUnit, medItem.getMedicalTotalQtyUnit());
        values.put(DatabaseHandler.COL_PreCaution1, medItem.getPreCaution1());
        values.put(DatabaseHandler.COL_PreCaution2, medItem.getPreCaution2());
        values.put(DatabaseHandler.COL_PreCaution3, medItem.getPreCaution3());
        values.put(DatabaseHandler.COL_downloaded, medItem.getDownloaded());
        values.put(DatabaseHandler.COL_isremider,medItem.getIs_reminder());
        values.put(DatabaseHandler.COL_StartDate,medItem.getStartdate());
        values.put(DatabaseHandler.COL_EndDate,medItem.getEnddate());
        values.put(DatabaseHandler.COL_NextDateTime,medItem.getNextdate());
        values.put(DatabaseHandler.COL_NumberofDays,medItem.getNumberofdays());
        values.put(DatabaseHandler.COL_SlotInterval,medItem.getSlotinterval());

        return  values;

    }
}

