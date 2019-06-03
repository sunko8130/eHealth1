package sg.com.ehealthassist.ehealthassist.DB;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;

public class DBClinicInfo {
    private DatabaseHandler mDbHelper;
    private Context mContext;
    public DBClinicInfo(Context context) {
        mDbHelper = new DatabaseHandler(context);
        this.mContext = context;
    }

    //region Select a Clinicinfos by clinicid()
    public ClinicInfo getClinicInfo(int clinicId) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = null;
        ClinicInfo clinicInfo = new ClinicInfo();
        cursor = db.rawQuery("SELECT * FROM " + DatabaseHandler.TABLE_CLINIC + " WHERE " + DatabaseHandler.COL_ID + "=" + clinicId, null);
        if (cursor == null){
            cursor.close();
            return null;
        }
        if (cursor.moveToFirst()) {
            clinicInfo = getClinicInfo(cursor);
        }
        cursor.close();
        db.close();
        return clinicInfo;
    }
    //endregion

    public ClinicInfo getClinicInfobyHECODE(String hecode) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = null;
        ClinicInfo clinicInfo = null;
        String sql = "SELECT * FROM " + DatabaseHandler.TABLE_CLINIC + " WHERE " + DatabaseHandler.COL_HECODE + "='" + hecode + "'";
        cursor = db.rawQuery(sql, null);
        if (cursor == null){
            return null;
        }
        if (cursor.moveToFirst()) {
            clinicInfo = getClinicInfo(cursor);
        }
        cursor.close();
        db.close();
        return clinicInfo;
    }
    //region Select All Clinicinfos() order by showpriority,queue_enabled,name
    public List<ClinicInfo> getAllClinicInfos(boolean filter24hours, boolean filterChas, boolean filterQNow) {
        String selectQuery = "SELECT * FROM " + DatabaseHandler.TABLE_CLINIC;

        String result = PrepareQuery(selectQuery, filter24hours, filterChas, filterQNow);
        result = result + " ORDER BY " + DatabaseHandler.COL_SHOW_PRIORITY + " , " + DatabaseHandler.COL_IS_QUEUE_ENABLED  + " , " + DatabaseHandler.COL_IS_APPT_ENABLED+ " DESC , " + DatabaseHandler.COL_NAME;

        return getClinicInfosByQuery(result);
    }
    //endregion

    //region Select All Clinicinfos() order by showpriority,queue_enabled,name
    public List<ClinicInfo> getAllClinicInfosbyNearest(Location loc) {
        String querys = "SELECT *  FROM " + DatabaseHandler.TABLE_CLINIC + "  ORDER BY (" + DatabaseHandler.COL_LAT + "- (" + loc.getLatitude() + ")) * "
                + "(" + DatabaseHandler.COL_LAT + "-(" + loc.getLatitude() + ")) + (( " + DatabaseHandler.COL_LNG + "-(" + loc.getLongitude() + "))*" + 2 + ") * (("
                + DatabaseHandler.COL_LNG + "-(" + loc.getLongitude() + ")) * " + 2 + ") ASC ";
        return getClinicInfosByQuery(querys);
    }
    //endregion

    //region Select All Clinicinfos by 24hr,chas,qnow,postalcode order by show_priority,queue_enabled,name
    public List<ClinicInfo> getClinicInfos(boolean filter24hours, boolean filterChas, boolean filterQNow, int postalCode) {
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_CLINIC + " WHERE (" + DatabaseHandler.COL_POSTAL_CODE + "=" + postalCode + ")";
        String resultquery = PrepareQuery(query, filter24hours, filterChas, filterQNow);
        resultquery = resultquery + " ORDER BY " + DatabaseHandler.COL_SHOW_PRIORITY + " , " + DatabaseHandler.COL_IS_QUEUE_ENABLED +  " , " + DatabaseHandler.COL_IS_APPT_ENABLED+" DESC , " + DatabaseHandler.COL_NAME;

        return getClinicInfosByQuery(resultquery);
    }
    //endregion
    //region Select All Clinicinfos by 24hr,chas,qnow ,name order by show_priority,queue_enable,name
    public List<ClinicInfo> getClinicInfos(boolean filter24hours, boolean filterChas, boolean filterQNow, String searchname) {
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_CLINIC + " WHERE (" + DatabaseHandler.COL_PLACE_TOWN + " like '%" + searchname +
                "%' OR  " + DatabaseHandler.COL_STREET + " like '%" + searchname + "%' OR " + DatabaseHandler.COL_NAME + " like '%" + searchname + "%')";
        String result = PrepareQuery(query, filter24hours, filterChas, filterQNow);
        result = result +
                " ORDER BY " + DatabaseHandler.COL_SHOW_PRIORITY + " , " +
                DatabaseHandler.COL_IS_QUEUE_ENABLED +  " , " +
                DatabaseHandler.COL_IS_APPT_ENABLED+" DESC , " + DatabaseHandler.COL_NAME;

        return getClinicInfosByQuery(result);
    }
    //endregion
    //region Select All Clinicinfos by bookmared = 1
    public List<ClinicInfo> getFavoriteClinicInfos() {
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_CLINIC + " WHERE " + DatabaseHandler.COL_IS_BOOKMARKED + "=1";
        return getClinicInfosByQuery(query);
    }
    //endregion
    //region Select ClinicInfo list  near by location
    public List<ClinicInfo> getNearClinicInfo(Location loc) {
        String querys = "SELECT *  FROM " + DatabaseHandler.TABLE_CLINIC + "  ORDER BY (" + DatabaseHandler.COL_LAT + "-" + loc.getLatitude() + ") * "
                + "(" + DatabaseHandler.COL_LAT + "-" + loc.getLatitude() + ") + (( " + DatabaseHandler.COL_LNG + "-" + loc.getLongitude() + ")*" + 2 + ") * (("
                + DatabaseHandler.COL_LNG + "-" + loc.getLongitude() + ") * " + 2 + ") ASC LIMIT 100";

        return getClinicInfosByQuery(querys);
    }
    //endregion
    //region Select ClinicInfo lists by clinic type & specialty
    public List<ClinicInfo> getNearClinicInfoByType(String clinictype,String specialty,String search_string,boolean filter24hours,boolean filterChas,boolean filterQNow){
        String querys = "";
        String query2 = "";
        String query1 = "SELECT *  FROM " + DatabaseHandler.TABLE_CLINIC ;
        if (search_string.matches("^\\d{6}$")) {
            query2 =  " WHERE (" + DatabaseHandler.COL_POSTAL_CODE + "=" + Integer.parseInt(search_string) + ")";
        }else if (!search_string.equals("")){
            query2 = " WHERE (" + DatabaseHandler.COL_PLACE_TOWN + " like '%" + search_string +
                    "%' OR  " + DatabaseHandler.COL_STREET + " like '%" + search_string + "%' OR " + DatabaseHandler.COL_NAME + " like '%" + search_string + "%')";
        }
        String query2_1 = PrepareQuery(query2,filter24hours,filterChas,filterQNow);
        String query3 =  " ORDER BY " + DatabaseHandler.COL_SHOW_PRIORITY + " , " +
                DatabaseHandler.COL_IS_QUEUE_ENABLED +  " , " + DatabaseHandler.COL_IS_APPT_ENABLED+" DESC , " + DatabaseHandler.COL_NAME;
        String query4 =  DatabaseHandler.COL_Specialty+ " = '"+ specialty+"'";
        String query5 =   DatabaseHandler.COL_ClinicType + "= '"+clinictype+"' and "+DatabaseHandler.COL_Specialty+ " = '"+ specialty+"'";
        String query6 =  DatabaseHandler.COL_ClinicType+ " = '"+ clinictype+"'";
        if(!search_string.equals("")){

            if(clinictype.equals("")){
                if(!specialty.equals("All")){
                    querys = query1 + query2_1 + " and " + query4 + query3;
                }else{
                    querys = query1 + query2_1 + query3;
                }

            }else{
                if(!specialty.equals("All")){
                    querys = query1 + query2_1 + " and " + query5 + query3;

                }else{
                    querys = query1 + query2_1 + "and "+ query6+ query3;
                }
            }
        }else{
            if(clinictype.equals("")){
                if(!specialty.equals("All")){
                    querys = query1  + " where " + query4 + query3;
                }else{
                    querys = query1  + query3;
                }

            }else{
                if(!specialty.equals("All")){
                    querys = query1  + " where " + query5 + query3;
                }else{
                    querys = query1  + " where "+ query6+ query3;
                }
            }
        }
        Log.e("special clinic",querys);

        return  getClinicInfosByQuery(querys);
    }
    //endregion
    //region Select ClinicInfo lists by clinic type & specialty
    public List<ClinicInfo> getNearClinicInfoByType(String clinictype,String search_string,boolean filter24hours,boolean filterChas,boolean filterQNow){
        String querys = "";
        String query2 = "";
        String query1 = "SELECT *  FROM " + DatabaseHandler.TABLE_CLINIC;
        if (search_string.matches("^\\d{6}$")) {
            query2 =  " WHERE (" + DatabaseHandler.COL_POSTAL_CODE + "=" + Integer.parseInt(search_string) + ")";
        }else if (!search_string.equals("")){
            query2 = " WHERE (" + DatabaseHandler.COL_PLACE_TOWN + " like '%" + search_string +
                    "%' OR  " + DatabaseHandler.COL_STREET + " like '%" + search_string + "%' OR " + DatabaseHandler.COL_NAME + " like '%" + search_string + "%')";
        }

        String query2_1 = PrepareQuery(query2,filter24hours,filterChas,filterQNow);
        String query3 =  " ORDER BY " + DatabaseHandler.COL_SHOW_PRIORITY + " , " +
                DatabaseHandler.COL_IS_QUEUE_ENABLED +  " , " +
                DatabaseHandler.COL_IS_APPT_ENABLED+" DESC , " + DatabaseHandler.COL_NAME;
        String query4 =  DatabaseHandler.COL_ClinicType  + "= '"+ clinictype + "'";

        if(!search_string.equals("")){
            if(clinictype.equals("")){
                querys = query1  + query2_1 + query3;
            }
            else{
                querys = query1  +query2_1 + " and "+ query4+ query3;
            }

        }else{
            if(clinictype.equals("")){
                querys = query1 + query3;
            }
            else{
                querys = query1+ " where "+query4 + query3;
            }
        }

        Log.e("clinic type",querys);


        return  getClinicInfosByQuery(querys);
    }
    //endregion

    //region only services
    public List<ClinicInfo> getClinicInfoByServices(String services_name){
        String querys = "select * from " + DatabaseHandler.TABLE_CLINIC + " where  " + DatabaseHandler.COL_ID +
                " in ( select "+DatabaseHandler.COL_Clinic_id+"  from "+ DatabaseHandler.TABLE_Clinic_Services+"  where "+DatabaseHandler.COL_ClinicService+" = '"
                +services_name+"' ) ORDER BY  "+ DatabaseHandler.COL_NAME + " ASC ";

        return  getClinicInfosByQuery(querys);
    }
    //endregion
    //region type & services
    public List<ClinicInfo> getClinicInfoBytype_Services(String services_name,String clinictype,String specialty,String search_string,boolean filter24hours,boolean filterChas,boolean filterQNow){
        String querys = "";
        String query9 = "";
        String query1 =  "select * from " + DatabaseHandler.TABLE_CLINIC + " where  " + DatabaseHandler.COL_ID + " in " ;

        String query2 = "( select "+DatabaseHandler.COL_Clinic_id+"  " + "from "+ DatabaseHandler.TABLE_Clinic_Services+"  where "+DatabaseHandler.COL_ClinicService+" = '"
                +services_name+"' ) and "+ DatabaseHandler.COL_ClinicType + " = '"+clinictype +"' and " + DatabaseHandler.COL_Specialty + "='"+specialty+"'";

        String query3 = "( select "+DatabaseHandler.COL_Clinic_id+"  from "+ DatabaseHandler.TABLE_Clinic_Services+")" +
                " and "+ DatabaseHandler.COL_ClinicType + " = '"+clinictype + "'";

        String query4 =" ( select "+DatabaseHandler.COL_Clinic_id+"  from "+ DatabaseHandler.TABLE_Clinic_Services+" )" +
                " and "+ DatabaseHandler.COL_Specialty + " = '"+specialty + "'";

        String query5 = "( select "+DatabaseHandler.COL_Clinic_id+"  from "+ DatabaseHandler.TABLE_Clinic_Services+"  where "+DatabaseHandler.COL_ClinicService+" = '" +services_name+"' ) ";

        String query7 = "select * from " + DatabaseHandler.TABLE_CLINIC ;

        String query8=  " ORDER BY  "+ DatabaseHandler.COL_NAME + " ASC ";

        String query10 = "( select "+DatabaseHandler.COL_Clinic_id+"  " + "from "+ DatabaseHandler.TABLE_Clinic_Services +" )" +
                " and "+ DatabaseHandler.COL_ClinicType + " = '"+clinictype +"' and " + DatabaseHandler.COL_Specialty + "='"+specialty+"'";


        if (search_string.matches("^\\d{6}$")) {
            query9 =  " and  (" + DatabaseHandler.COL_POSTAL_CODE + "=" + Integer.parseInt(search_string) + ")";
        }else if (!search_string.equals("")){
            query9 = " and (" + DatabaseHandler.COL_PLACE_TOWN + " like '%" + search_string +
                    "%' OR  " + DatabaseHandler.COL_STREET + " like '%" + search_string + "%' OR " + DatabaseHandler.COL_NAME + " like '%" + search_string + "%')";
        }
        String query9_1 = PrepareQuery(query9,filter24hours,filterChas,filterQNow);

        if(!search_string.equals("")) {
            if (clinictype.equals("") && specialty.equals("All") && services_name.equals("All")) {
                querys = query1 + query9_1 + query8;
            }
            else if (clinictype.equals("") && !specialty.equals("All") && services_name.equals("All")) {
                querys = query1 + query4 + query9_1 + query8;
            }
            else if (!clinictype.equals("") && specialty.equals("All") && services_name.equals("All")) {
                querys = query1 + query3 + query9_1 + query8;
            }

            else if(clinictype.equals("") && specialty.equals("All") && !services_name.equals("All")) {
                querys = query1 + query5 + query9_1 + query8;
            }
            else if (!clinictype.equals("") && !specialty.equals("All") && services_name.equals("All")){
                querys = query1 + query10 + query9_1 + query8;
            }
            else if(clinictype.equals("") && !specialty.equals("All") && !services_name.equals("All"))
            {
                querys = query1 + query5 +"  and " + DatabaseHandler.COL_Specialty + "= '"+specialty + "'" + query8;
            }else{
                querys = query1 + query2 + query9_1 + query8;
            }
        }

        else{
            if (clinictype.equals("") && specialty.equals("All") && services_name.equals("All")) {
                querys = query7 +  query8;
            }
            else if (clinictype.equals("") && !specialty.equals("All") && services_name.equals("All")) {
                querys = query1 + query4  + query8;
            }
            else if (!clinictype.equals("") && specialty.equals("All") && services_name.equals("All")) {
                querys = query1 + query3  + query8;
            }
            else if(clinictype.equals("") && specialty.equals("All") && !services_name.equals("All")) {
                querys = query1 + query5  + query8;
            }
            else if (!clinictype.equals("") && !specialty.equals("All") && services_name.equals("All")){
                querys = query1 + query10  + query8;
            }
            else if(clinictype.equals("") && !specialty.equals("All") && !services_name.equals("All"))
            {
                querys = query1 + query5 +"  and "+ DatabaseHandler.COL_Specialty +"= '"+specialty + "'" + query8;
            }
            else{
                querys = query1 + query2  + query8;
            }
        }

        Log.e("serveice clinic ",querys);


        return  getClinicInfosByQuery(querys);
    }
    //endregion


    //region Update Clinicinfos()
    public long updateClinicInfos(ClinicInfo clinicInfo) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        Cursor cursor = db.query(DatabaseHandler.TABLE_CLINIC, new String[]{DatabaseHandler.COL_ID}, DatabaseHandler.COL_ID + "=?", new String[]{String.valueOf(clinicInfo.get_id())}, null, null, null, null);
        ContentValues values = setClinicInfoContentValues(clinicInfo);
        long affected;
        if (cursor.moveToFirst()) {
            affected = db.update(DatabaseHandler.TABLE_CLINIC, values, DatabaseHandler.COL_ID + "=?", new String[]{String.valueOf(clinicInfo.get_id())});
        } else {
            affected = db.insert(DatabaseHandler.TABLE_CLINIC, null, values);
        }
        cursor.close();
        db.close();
        return affected;
    }
    //endregion

    //region PrepareQuery
    public String PrepareQuery(String query, boolean filter24hours, boolean filterChas, boolean filterQNow) {

        if (filter24hours && !filterChas && !filterQNow) {
            if (query.contains("(")) {
                query = query + " AND " + DatabaseHandler.COL_IS247 + "=" + 1;
            } else {
                query = query + " WHERE " + DatabaseHandler.COL_IS247 + "=" + 1;
            }
        } else if (filterChas && !filter24hours && !filterQNow) {
            if (query.contains("(")) {
                query = query + " AND " + DatabaseHandler.COL_IS_CHAS + "=" + 1;
            } else {
                query = query + " WHERE " + DatabaseHandler.COL_IS_CHAS + "=" + 1;
            }
        } else if (!filterChas && !filter24hours && filterQNow) {
            if (query.contains("(")) {
                query = query + " AND " + DatabaseHandler.COL_IS_QUEUE_ENABLED + "=" + 1;
            } else {
                query = query + " WHERE " + DatabaseHandler.COL_IS_QUEUE_ENABLED + "=" + 1;
            }
        } else if (filter24hours && filterChas && filterQNow) {
            if (query.contains("(")) {
                query = query + " AND " + DatabaseHandler.COL_IS247 + "=" + 1 +
                        " AND " + DatabaseHandler.COL_IS_CHAS + "=" + 1 + " AND " + DatabaseHandler.COL_IS_QUEUE_ENABLED + " = 1";
            } else {
                query = query + " WHERE " + DatabaseHandler.COL_IS247 + "=" + 1 +
                        " AND " + DatabaseHandler.COL_IS_CHAS + "=" + 1 + " AND " + DatabaseHandler.COL_IS_QUEUE_ENABLED + " = 1";
            }
        } else if (filter24hours && !filterChas && filterQNow) {
            if (query.contains("(")) {
                query = query + " AND " + DatabaseHandler.COL_IS247 + "=" + 1 +
                        " AND " + DatabaseHandler.COL_IS_QUEUE_ENABLED + " = 1";
            } else {
                query = query + " WHERE " + DatabaseHandler.COL_IS247 + "=" + 1 +
                        " AND " + DatabaseHandler.COL_IS_QUEUE_ENABLED + " = 1";
            }

        } else if (!filter24hours && filterChas && filterQNow) {
            if (query.contains("(")) {
                query = query + " AND "
                        + DatabaseHandler.COL_IS_CHAS + "=" + 1 + " AND " + DatabaseHandler.COL_IS_QUEUE_ENABLED + " = 1";
            } else {
                query = query + " WHERE "
                        + DatabaseHandler.COL_IS_CHAS + "=" + 1 + " AND " + DatabaseHandler.COL_IS_QUEUE_ENABLED + " = 1";
            }

        } else if (filter24hours && filterChas && !filterQNow) {
            if (query.contains("(")) {
                query = query + " AND "
                        + DatabaseHandler.COL_IS_CHAS + "=" + 1 + " AND " + DatabaseHandler.COL_IS247 + " = 1";
            } else {
                query = query + " WHERE "
                        + DatabaseHandler.COL_IS_CHAS + "=" + 1 + " AND " + DatabaseHandler.COL_IS247 + " = 1";
            }
        }

        return query;
    }
    //endregion
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
        cursor.close();
        db.close();
        return clinicInfos;
    }
    //endregion
    //region Check ToggleClinicAsFavourite
    public int toggleClinicAsFavourite(int clinicId, boolean currentState) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int rowAffected;
        try {
            ContentValues values = new ContentValues();
            values.put(DatabaseHandler.COL_IS_BOOKMARKED, !currentState);
            rowAffected = db.update(DatabaseHandler.TABLE_CLINIC, values, DatabaseHandler.COL_ID + "=?", new String[]{String.valueOf(clinicId)});
        } catch (Exception ex) {
            rowAffected = 0;
        }
        db.close();

        return rowAffected;
    }
    //endregion
    public List<ClinicInfo> getAllClinicList(){
        List<ClinicInfo> lstclinic = new ArrayList<ClinicInfo>();
        String sql = "select * from " + DatabaseHandler.TABLE_CLINIC + " order by " + DatabaseHandler.COL_NAME;
        lstclinic = getClinicInfosByQuery(sql);
        return lstclinic;
    }
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

    //region not use fun()
    public void addClinic(ClinicInfo clinicInfo) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = setClinicInfoContentValues(clinicInfo);
        db.insert(DatabaseHandler.TABLE_CLINIC, null, values);
        db.close();
    }
    public void deleteclinicbyId(int clinicid){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String sql = "delete from " + DatabaseHandler.TABLE_CLINIC + " where " + DatabaseHandler.COL_ID  + " = " + clinicid;
        db.execSQL(sql);
        db.close();
    }
    public void deletetestclinic(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        String sql = "delete from " + DatabaseHandler.TABLE_CLINIC + " where " +DatabaseHandler.COL_ID + " in ( select " + DatabaseHandler.COL_ID +
                " from " + DatabaseHandler.TABLE_TEST_CLINIC + " )";
        db.execSQL(sql);
        db.close();
    }

     /* public List<ClinicInfo> getClinicInfos(boolean filter24hours, boolean filterChas, boolean checkqueue) {
        String query = "SELECT * FROM " + DatabaseHandler.TABLE_CLINIC;
        String result = PrepareQuery(query, filter24hours, filterChas, checkqueue);
        result = result + " ORDER BY " + DatabaseHandler.COL_SHOW_PRIORITY + " , " + DatabaseHandler.COL_IS_QUEUE_ENABLED + " DESC , " + DatabaseHandler.COL_NAME;

        return getClinicInfosByQuery(result);
    }*/
    //endregion
}
