package sg.com.ehealthassist.ehealthassist.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;

public class DBTestClinic {
    private DatabaseHandler mDbHelper;
    private Context mContext;

    public DBTestClinic(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }

    //region Update Clinicinfos()
    public long updateClinicInfos(ClinicInfo clinicInfo) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHandler.TABLE_TEST_CLINIC, new String[]{DatabaseHandler.COL_ID}, DatabaseHandler.COL_ID + "=?", new String[]{String.valueOf(clinicInfo.get_id())}, null, null, null, null);
        ContentValues values = setClinicInfoContentValues(clinicInfo);
        long affected;
        if (cursor.moveToFirst()) {
            affected = db.update(DatabaseHandler.TABLE_TEST_CLINIC, values, DatabaseHandler.COL_ID + "=?", new String[]{String.valueOf(clinicInfo.get_id())});
        } else {
            affected = db.insert(DatabaseHandler.TABLE_TEST_CLINIC, null, values);
        }
        cursor.close();
        db.close();
        return affected;
    }
    //endregion

    //region Select a Clinicinfos by clinicid()
    public ClinicInfo getClinicInfo(int clinicId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = null;
        ClinicInfo clinicInfo = null;
        cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_TEST_CLINIC + " WHERE " + DatabaseHandler.COL_ID + "=" + clinicId, null);
        if (cursor == null)
            return null;
        if (cursor.moveToFirst()) {
            clinicInfo = getClinicInfo(cursor);
        }
        cursor.close();
        db.close();
        return clinicInfo;
    }
    //endregion
    public List<ClinicInfo> getallclinicinfo(){
        String querys = "select * from " + DatabaseHandler.TABLE_TEST_CLINIC ;
        return  getClinicInfosByQuery(querys);
    }
    //region Return All Clinicinfos by Custom Query
    private List<ClinicInfo> getClinicInfosByQuery(String query) {
        List<ClinicInfo> clinicInfos = new ArrayList<>();
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (!cursor.equals(null)) {
            if (cursor.moveToFirst()) {
                do {
                    clinicInfos.add(getClinicInfo(cursor));
                } while (cursor.moveToNext());
            }
        }
        cursor.close();db.close();
        return clinicInfos;
    }
    //endregion
    //region Get a ClinicInfo From Cursor
    private ClinicInfo getClinicInfo(Cursor cursor) {
        ClinicInfo clinicInfo = new ClinicInfo();
        clinicInfo.set_id(cursor.getInt(0));
        clinicInfo.set_name(cursor.getString(1));
        clinicInfo.setClinicType(cursor.getString(2));
        clinicInfo.getClinic_address().setBlockno(cursor.getString(3));
        clinicInfo.getClinic_address().setStreet(cursor.getString(4));
        clinicInfo.getClinic_address().setUnitno(cursor.getString(5));
        clinicInfo.getClinic_address().setBuilding(cursor.getString(6));
        clinicInfo.getClinic_address().setPostal(cursor.getString(7));
        clinicInfo.getClinic_address().setRegion(cursor.getString(8));
        clinicInfo.getClinic_contact().set_tel1(cursor.getInt(9));
        clinicInfo.getClinic_contact().set_tel2(cursor.getInt(10));
        clinicInfo.getClinic_contact().set_fax(cursor.getInt(11));
        clinicInfo.getClinic_contact().set_website(cursor.getString(12));
        clinicInfo.getClinic_contact().set_email(cursor.getString(13));
        clinicInfo.getClinic_location().set_lat(cursor.getString(14));
        clinicInfo.getClinic_location().set_lng(cursor.getString(15));
        clinicInfo.set_is24Hours(cursor.getInt(16) > 0);
        clinicInfo.set_isChas(cursor.getInt(17) > 0);
        clinicInfo.set_toShow(cursor.getInt(18) > 0);
        clinicInfo.setLogoloid(cursor.getString(19));
        clinicInfo.getClinic_address().setNearestmrt(cursor.getString(20));
        clinicInfo.getClinic_address().setPlacetown(cursor.getString(21));
        clinicInfo.set_operatinghr(cursor.getString(22));
        clinicInfo.set_isBookmarked(cursor.getInt(23) > 0);
        clinicInfo.set_showPriority(cursor.getInt(24));
        clinicInfo.set_isQueueEnabled(cursor.getInt(25) > 0);
        clinicInfo.set_specialty(cursor.getString(26));
        clinicInfo.set_isapptEnabled(cursor.getInt(27)>0);
        clinicInfo.setClinic_note((cursor.getString(28)==null)?"":cursor.getString(28));
        clinicInfo.setHecode((cursor.getString(29)==null)?"":cursor.getString(29));
        clinicInfo.setAppt_mode((cursor.getString(30)==null)?"":cursor.getString(30));

        return clinicInfo;
    }
    //endregion
    //region Get ContentValues from clinicinfo object
    private ContentValues setClinicInfoContentValues(ClinicInfo clinicInfo) {
        ContentValues values = new ContentValues();
        values.put(DatabaseHandler.COL_ID, clinicInfo.get_id());
        values.put(DatabaseHandler.COL_NAME, clinicInfo.get_name());
        values.put(DatabaseHandler.COL_ClinicType, clinicInfo.getClinicType());
        values.put(DatabaseHandler.COL_BLOCK, clinicInfo.getClinic_address().getBlockno());
        values.put(DatabaseHandler.COL_STREET, clinicInfo.getClinic_address().getStreet());
        values.put(DatabaseHandler.COL_UNIT, clinicInfo.getClinic_address().getUnitno());
        values.put(DatabaseHandler.COL_BUILDING, clinicInfo.getClinic_address().getBuilding());
        values.put(DatabaseHandler.COL_POSTAL_CODE, clinicInfo.getClinic_address().getPostal());
        values.put(DatabaseHandler.COL_PLACE_TOWN, clinicInfo.getClinic_address().getPlacetown());
        values.put(DatabaseHandler.COL_NearestMrt, clinicInfo.getClinic_address().getNearestmrt());
        values.put(DatabaseHandler.COL_Region, clinicInfo.getClinic_address().getRegion());
        values.put(DatabaseHandler.COL_FAX, clinicInfo.getClinic_contact().get_fax());
        values.put(DatabaseHandler.COL_TEL1, clinicInfo.getClinic_contact().get_tel1());
        values.put(DatabaseHandler.COL_TEL2, clinicInfo.getClinic_contact().get_tel2());
        values.put(DatabaseHandler.COL_WEBSITE, clinicInfo.getClinic_contact().get_website());
        values.put(DatabaseHandler.COL_Email, clinicInfo.getClinic_contact().get_email());
        values.put(DatabaseHandler.COL_LAT, clinicInfo.getClinic_location().get_lat());
        values.put(DatabaseHandler.COL_LNG, clinicInfo.getClinic_location().get_lng());
        values.put(DatabaseHandler.COL_IS247, clinicInfo.is_is24Hours());
        values.put(DatabaseHandler.COL_IS_CHAS, clinicInfo.is_isChas());
        values.put(DatabaseHandler.COL_TO_SHOW, clinicInfo.is_toShow());
        values.put(DatabaseHandler.COL_SHOW_PRIORITY, clinicInfo.get_showPriority());
        values.put(DatabaseHandler.COL_IS_QUEUE_ENABLED, clinicInfo.is_isQueueEnabled());
        values.put(DatabaseHandler.COL_LogoId, clinicInfo.getLogoloid());
        values.put(DatabaseHandler.COL_OPERATING_HOURS, clinicInfo.get_operatinghr());
        values.put(DatabaseHandler.COL_Specialty, clinicInfo.get_specialty());
        values.put(DatabaseHandler.COL_IS_APPT_ENABLED, clinicInfo.is_isapptEnabled());
        values.put(DatabaseHandler.COL_Clinic_note, clinicInfo.getClinic_note());
        values.put(DatabaseHandler.COL_HECODE, clinicInfo.getHecode());
        values.put(DatabaseHandler.COL_ApptMode,clinicInfo.getAppt_mode());
        return values;
    }
    //endregion
}
