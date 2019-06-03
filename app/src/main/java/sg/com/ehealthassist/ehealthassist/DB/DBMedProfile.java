package sg.com.ehealthassist.ehealthassist.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;

/**
 * Created by User on 6/30/2017.
 */

public class DBMedProfile {
    private DatabaseHandler mDbHelper;
    private Context mContext;

    public DBMedProfile(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }

    //region Select MedicalProfile lists by memberid
    public ArrayList<MedicalProfile> getMedProfilebyMemberid(String memberid) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        String sql = "SELECT * FROM " + DatabaseHandler.TABLE_MEDPROFILE + " where " + DatabaseHandler.COL_MEMBERID + " = '" + memberid + "' ORDER BY "+
                DatabaseHandler.COL_Member + " DESC ";
        Log.e("getprofile",sql);
        Cursor cursor = db.rawQuery(sql, null);
        ArrayList<MedicalProfile> medProfile = new ArrayList<MedicalProfile>();
        Log.e("cursor count",cursor.getCount()+ "");

        if (cursor.moveToFirst()) {
            medProfile = cursortoObject(cursor);
        }
        cursor.close();
        db.close();
        return medProfile;
    }

    //endregion

    //region select a MedicalProfile object by memberid
    public MedicalProfile getMedProfilebyNRIC(int nric_type,String nric ) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_MEDPROFILE +
                " where " + DatabaseHandler.COL_NRIC_Type + " = " + nric_type + " and " + DatabaseHandler.COL_NRIC + "= '"+nric+"'", null);
        MedicalProfile reqlog = new MedicalProfile();
        if (cursor.moveToFirst()) {
            reqlog = deserializRQL(cursor);
        }
        cursor.close();
        db.close();
        return reqlog;
    }

    public MedicalProfile getMedProfilebyNRIC_memberid(int nric_type,String nric,String memberid ) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_MEDPROFILE +
                " where " + DatabaseHandler.COL_NRIC_Type + " = " + nric_type + " and " + DatabaseHandler.COL_NRIC + "= '"+nric+"'  and "
                + DatabaseHandler.COL_MEMBERID + " = '"+ memberid+"'", null);
        MedicalProfile reqlog = new MedicalProfile();
        if (cursor.moveToFirst()) {
            reqlog = deserializRQL(cursor);
        }
        cursor.close();
        db.close();
        return reqlog;
    }

    //endregion
    //region Update MedicalProfile by Drive
    public long updateMedicalProfilebydrive(MedicalProfile upload_profile) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHandler.TABLE_MEDPROFILE, new String[]{DatabaseHandler.COL_MEMBERID,DatabaseHandler.COL_NRIC_Type,DatabaseHandler.COL_NRIC},
                DatabaseHandler.COL_MEMBERID + "=? and "+ DatabaseHandler.COL_NRIC_Type + "=? and "+DatabaseHandler.COL_NRIC + "=?"
                , new String[]{ upload_profile.getMemberid(),upload_profile.getNric_type()+"",upload_profile.getNric()}, null, null, null, null);
        // String.valueOf(upload_profile.getMemberid()+"",upload_profile.getNric(),upload_profile.getNric_type()+"")
        ContentValues values = setClinicInfoContentValues(upload_profile);
        long affected;
        if (cursor.moveToFirst()) {
            Log.e("update",upload_profile.getNric_name() +" , "+ upload_profile.getId());
            affected = db.update(DatabaseHandler.TABLE_MEDPROFILE, values, DatabaseHandler.COL_ID + "=?", new String[]{String.valueOf(upload_profile.getId())});
        } else {
            Log.e("insert",upload_profile.getNric_name() + "," + upload_profile.getId());
            affected = db.insert(DatabaseHandler.TABLE_MEDPROFILE, null, values);

        }
        cursor.close();
        db.close();
        Log.e("affected",affected+ "");
        return affected;
    }
    //endregion
    //region UpdateMedicalProfile by ColId
    public long updateMedicalProfile(MedicalProfile upload_profile) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHandler.TABLE_MEDPROFILE, new String[]{DatabaseHandler.COL_ID},
                DatabaseHandler.COL_ID + "=?"
                , new String[]{String.valueOf(upload_profile.getId())}, null, null, null, null);

        ContentValues values = setClinicInfoContentValues(upload_profile);
        long affected;
        if (cursor.moveToFirst()) {
            affected = db.update(DatabaseHandler.TABLE_MEDPROFILE, values, DatabaseHandler.COL_ID + "=?", new String[]{String.valueOf(upload_profile.getId())});
        } else {

            affected = db.insert(DatabaseHandler.TABLE_MEDPROFILE, null, values);

        }
        cursor.close();
        db.close();

        return affected;
    }
    //endregion

    //region Delete MedicalProfile by id
    public void deleteMedProfile(int med_id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_MEDPROFILE + " where " + DatabaseHandler.COL_ID + " = " + med_id + "";
        db.execSQL(str);
        db.close();
    }
    //endregion

    //endregion
    public int existNRIC(String nricid,int nrictype,String dob,String memberid) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = null;
        String str = "";
        if(memberid.equals("")){
            str = "SELECT * FROM " + DatabaseHandler.TABLE_MEDPROFILE + " WHERE " + DatabaseHandler.COL_NRIC + " = '" + nricid + "' and "+ DatabaseHandler.COL_DOB
                    + " = '"+dob+"'";

        }else{
            str = "SELECT * FROM " + DatabaseHandler.TABLE_MEDPROFILE + " WHERE " + DatabaseHandler.COL_NRIC + " = '" + nricid + " ' and "+ DatabaseHandler.COL_DOB
                    + " = '"+dob+"' and " + DatabaseHandler.COL_MEMBERID + " = " + memberid;
        }
        int result = 0;
        cursor = db.rawQuery(str, null);
        ArrayList<MedicalProfile> medProfile = new ArrayList<MedicalProfile>();
        if (cursor == null) {
            result = 0;
        }
        if (cursor.moveToFirst()) {
            medProfile = cursortoObject(cursor);
            if(medProfile.size()<2){
                for (int i=0;i<medProfile.size();i++){
                    int med_type = medProfile.get(i).getNric_type();

                    if(med_type > 0 && nrictype > 0 ){
                        result=1;
                    }
                    else if (med_type > 0 && nrictype <1 ){
                        result = 0;
                    }
                    else if (med_type < 1 && nrictype >0){
                        result = 0;
                    }
                    else {
                        result = 1;
                    }
                }
            }
            else{
                result = 1;
            }

        }
        cursor.close();
        db.close();
        return result;
    }

    //region Get contenview from Object
    private ContentValues setClinicInfoContentValues(MedicalProfile uploadMed) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_Clinic_id, uploadMed.getClinic_id());
        values.put(DatabaseHandler.COL_NRIC_Type, uploadMed.getNric_type());
        values.put(DatabaseHandler.COL_NRIC, uploadMed.getNric());
        values.put(DatabaseHandler.COL_NAME, uploadMed.getNric_name());
        values.put(DatabaseHandler.COL_Nationality, uploadMed.getNationality());
        values.put(DatabaseHandler.COL_Gender, uploadMed.getGender());
        values.put(DatabaseHandler.COL_DOB, uploadMed.getDob());
        values.put(DatabaseHandler.COL_BLOCK, uploadMed.getBlock_no());
        values.put(DatabaseHandler.COL_STREET, uploadMed.getStreet());
        values.put(DatabaseHandler.COL_UNIT, uploadMed.getUnit_no());
        values.put(DatabaseHandler.COL_BUILDING, uploadMed.getBuilding_name());
        values.put(DatabaseHandler.COL_POSTAL_CODE, uploadMed.getPostal_code());
        values.put(DatabaseHandler.COL_TEL1, uploadMed.getTel1());
        values.put(DatabaseHandler.COL_TEL2, uploadMed.getTel2());
        values.put(DatabaseHandler.COL_Email, uploadMed.getEmail());
        values.put(DatabaseHandler.COL_Allergy, uploadMed.getAllergy());
        values.put(DatabaseHandler.COL_MEMBERID, uploadMed.getMemberid());
        values.put(DatabaseHandler.COL_RELATION, uploadMed.getRelation());
        values.put(DatabaseHandler.COL_Member, uploadMed.getMember());
        values.put(DatabaseHandler.COL_Flag_Upload, uploadMed.getFlag_upload());
        values.put(DatabaseHandler.COL_Married_Status, uploadMed.getMarried_staus());
        values.put(DatabaseHandler.COL_Language, uploadMed.getLanguage());
        values.put(DatabaseHandler.COL_Tel1_iso, uploadMed.getTel1_iso());
        values.put(DatabaseHandler.COL_Tel1_code, uploadMed.getTel1_code());
        values.put(DatabaseHandler.COL_Address1,uploadMed.getAddress1());
        values.put(DatabaseHandler.COL_Address2,uploadMed.getAddress2());
        values.put(DatabaseHandler.COL_Address3,uploadMed.getAddress3());
        values.put(DatabaseHandler.COL_Address4,uploadMed.getAddress4());
        return values;
    }

    //endregion
    //region Get MedicalProfile lists from cursor
    public ArrayList<MedicalProfile> cursortoObject(Cursor cursor) {
        ArrayList<MedicalProfile> reqlog = new ArrayList<MedicalProfile>();
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

            return reqlog;
        }
    }

    //endregion
    //region Get a MedicalProfile from cursor
    private MedicalProfile deserializRQL(Cursor cursor) {
        MedicalProfile qLog = new MedicalProfile();
        qLog.setId(cursor.getInt(0));
        qLog.setClinic_id(cursor.getInt(1));
        qLog.setNric_type(cursor.getInt(2));
        qLog.setNric(cursor.getString(3));
        qLog.setNric_name(cursor.getString(4).equals("null")?"":cursor.getString(4));
        qLog.setGender(cursor.getInt(5));
        qLog.setNationality(cursor.getString(6));
        qLog.setDob(cursor.getString(7));
        qLog.setBlock_no(cursor.getString(8));
        qLog.setStreet(cursor.getString(9));
        qLog.setBuilding_name(cursor.getString(10));
        qLog.setUnit_no(cursor.getString(11));
        qLog.setPostal_code(cursor.getString(12));
        qLog.setTel1(cursor.getString(13));
        qLog.setTel2(cursor.getString(14));
        qLog.setEmail(cursor.getString(15));
        qLog.setAllergy(cursor.getString(16));
        qLog.setMemberid(cursor.getString(17));
        qLog.setRelation(cursor.getInt(18));
        qLog.setMember(cursor.getInt(19));
        qLog.setFlag_upload(cursor.getInt(20));
        qLog.setMarried_staus(cursor.getInt(21));
        qLog.setLanguage(cursor.getInt(22));
        qLog.setTel1_iso(cursor.getString(23));
        qLog.setTel1_code(cursor.getInt(24));
        qLog.setAddress1(cursor.getString(25));
        qLog.setAddress2(cursor.getString(26));
        qLog.setAddress3(cursor.getString(27));
        qLog.setAddress4(cursor.getString(28));
        return qLog;
    }
    //endregion

    //region not use fun()
    public void deleteAllMedProfile() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_MEDPROFILE;
        db.execSQL(str);
        db.close();
    }

    public MedicalProfile getMedicalProfile(int id) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = null;
        MedicalProfile medprofile = null;
        cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_MEDPROFILE + " WHERE " + DatabaseHandler.COL_ID + "=" + id, null);
        if (cursor == null)
            return null;
        if (cursor.moveToFirst()) {
            medprofile = deserializRQL(cursor);
        }
        cursor.close();
        db.close();
        return medprofile;
    }

    public ArrayList<MedicalProfile> getallMedProfile() {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_MEDPROFILE + " order by " + DatabaseHandler.COL_ID  + " , " + DatabaseHandler.COL_MEMBERID, null);
        ArrayList<MedicalProfile> reqlog = new ArrayList<MedicalProfile>();
        if (cursor.moveToFirst()) {
            reqlog = cursortoObject(cursor);
        }
        cursor.close();
        db.close();

        return reqlog;
    }
    //region Select MedicalProfile list by flag
    public MedicalProfile getMedProfilebyflag( String memberid) {
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_MEDPROFILE + " where " + DatabaseHandler.COL_MEMBERID + " = '" + memberid
                + "' and " + DatabaseHandler.COL_Member + " = 1" , null);
        MedicalProfile reqlog = new MedicalProfile();
        if (cursor.moveToFirst()) {
            reqlog = deserializRQL(cursor);
        }
        cursor.close();
        db.close();
        return reqlog;
    }

    //endregion
    //region Exist/NotExist MedicalProfile by memberid
    public int existmemberprofile(String memberid) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = null;
        int result = 0;
        cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_MEDPROFILE + " WHERE " + DatabaseHandler.COL_Member + "= 1 and "
                + DatabaseHandler.COL_MEMBERID + " = '" + memberid + "'", null);
        if (cursor == null) {
            result = 0;
        }
        if (cursor.moveToFirst()) {
            result = 1;
        }
        cursor.close();
        db.close();
        return result;
    }
    //endregion

    public void deleteprofilebyMemberid(String memberid){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_MEDPROFILE + " WHERE " + DatabaseHandler.COL_MEMBERID + " = '" + memberid + "'";
        db.execSQL(str);
        db.close();
    }

  /*  public void deleteprofilebynric(String nric,){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_MEDPROFILE + " WHERE " + DatabaseHandler.COL_MEMBERID + " = '" + memberid + "'";
        db.execSQL(str);
        db.close();
    }*/

    public void deleteprofileNotMember(String memberid){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String str = "DELETE FROM " + DatabaseHandler.TABLE_MEDPROFILE + " WHERE " + DatabaseHandler.COL_MEMBERID + " = '" + memberid + "' and "+
                DatabaseHandler.COL_Member + " = 0";
        db.execSQL(str);
        db.close();
    }
    //endregion


}
