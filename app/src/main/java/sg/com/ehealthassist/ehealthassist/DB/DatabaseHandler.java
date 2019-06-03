package sg.com.ehealthassist.ehealthassist.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;

/**
 * Created by User on 6/29/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 19;
    private static final String DATABASE_NAME = "cas";
    public static final String TAG = "DatabaseHandler";

    //region Clinic Table
    public static final String TABLE_CLINIC = "clinic";
    public static final String
            COL_ID = "id", COL_NAME = "name", COL_BLOCK = "block", COL_STREET = "street", COL_UNIT = "unit",
            COL_BUILDING = "building", COL_Region = "region", COL_ClinicType = "clinictype", COL_POSTAL_CODE = "pcode", COL_LogoId = "logoid",
            COL_NearestMrt = "nearestmrt", COL_TEL1 = "tel1", COL_TEL2 = "tel2", COL_FAX = "fax", COL_WEBSITE = "website",
            COL_Email = "email", COL_LAT = "lat", COL_LNG = "lng", COL_IS247 = "is247", COL_IS_CHAS = "ischas", COL_TO_SHOW = "toshow",
            COL_PLACE_TOWN = "placetown", COL_OPERATING_HOURS = "operatinghours", COL_IS_BOOKMARKED = "isbookmarked",
            COL_SHOW_PRIORITY = "showpriority", COL_IS_QUEUE_ENABLED = "isqueueenabled",COL_Specialty="specialty",COL_IS_APPT_ENABLED = "isapptenabled",
            COL_Clinic_note = "note",COL_HECODE = "HECode",COL_ApptMode = "appt_mode";

    public static final String CREATE_TABLE_CLINIC = "CREATE TABLE " + TABLE_CLINIC + "(" + COL_ID + " INTEGER PRIMARY KEY," + COL_NAME + " TEXT," + COL_ClinicType + " TEXT," +
            COL_BLOCK + " TEXT," + COL_STREET + " TEXT," + COL_UNIT + " TEXT," + COL_BUILDING + " TEXT," + COL_POSTAL_CODE + " TEXT," + COL_Region + " TEXT ," +
            COL_TEL1 + " INTEGER," + COL_TEL2 + " INTEGER," + COL_FAX + " INTEGER," + COL_WEBSITE + " TEXT," + COL_Email + " TEXT," + COL_LAT + " TEXT," + COL_LNG + " TEXT," +
            COL_IS247 + " BOOLEAN," + COL_IS_CHAS + " BOOLEAN," + COL_TO_SHOW + " BOOLEAN," + COL_LogoId + " TEXT," + COL_NearestMrt + " TEXT, " +
            COL_PLACE_TOWN + " TEXT," + COL_OPERATING_HOURS + " TEXT," + COL_IS_BOOKMARKED + " BOOLEAN," +
            COL_SHOW_PRIORITY + " INTEGER," + COL_IS_QUEUE_ENABLED + " BOOLEAN," + COL_Specialty  +" TEXT , "+COL_IS_APPT_ENABLED + " TEXT ,"+ COL_Clinic_note + " TEXT ," +
            COL_HECODE + " TEXT ," + COL_ApptMode  + " TEXT );";
    //endregion
    //region Text Clinic table
    public static final String TABLE_TEST_CLINIC = "test_clinic";
    public static final String CREATE_TABLE_TEST_CLINIC = "CREATE TABLE " + TABLE_TEST_CLINIC + "(" + COL_ID + " INTEGER PRIMARY KEY," + COL_NAME + " TEXT," + COL_ClinicType + " TEXT," +
            COL_BLOCK + " TEXT," + COL_STREET + " TEXT," + COL_UNIT + " TEXT," + COL_BUILDING + " TEXT," + COL_POSTAL_CODE + " TEXT," + COL_Region + " TEXT ," +
            COL_TEL1 + " INTEGER," + COL_TEL2 + " INTEGER," + COL_FAX + " INTEGER," + COL_WEBSITE + " TEXT," + COL_Email + " TEXT," + COL_LAT + " TEXT," + COL_LNG + " TEXT," +
            COL_IS247 + " BOOLEAN," + COL_IS_CHAS + " BOOLEAN," + COL_TO_SHOW + " BOOLEAN," + COL_LogoId + " TEXT," + COL_NearestMrt + " TEXT, " +
            COL_PLACE_TOWN + " TEXT," + COL_OPERATING_HOURS + " TEXT," + COL_IS_BOOKMARKED + " BOOLEAN," +
            COL_SHOW_PRIORITY + " INTEGER," + COL_IS_QUEUE_ENABLED + " BOOLEAN," + COL_Specialty  +" TEXT , "+COL_IS_APPT_ENABLED + " TEXT ,"+ COL_Clinic_note + " TEXT ," +
            COL_HECODE + " TEXT , " + COL_ApptMode + " TEXT );";
    //endregion
    //region Queue Log Table
    public static final String TABLE_Queue_Request_Log = "queuerequest_log";

    public static final String  COL_Request_Nric = "request_nric", COL_Usertoken = "usertoken", COL_Request_Date = "request_date",
            COL_Clinic_id = "clinic_id", COL_Clinic_name = "clinic_name", COL_DOB = "dob", COL_Request_ID = "request_id", COL_Queue_no = "quesue_no",
            COL_Description = "description",COL_QMessage = "qmessage",COL_QSTATUS="qstatus";

    public final String CREATE_TABLE_RQUEUE_LOG = "CREATE TABLE " + TABLE_Queue_Request_Log + "(" + COL_ID + " INTEGER PRIMARY KEY," + COL_Request_Nric + " TEXT," +
            COL_Usertoken + " TEXT," + COL_Request_Date + " TEXT," + COL_Clinic_id + " INTEGER," + COL_DOB + " TEXT," + COL_Clinic_name + " TEXT," +
            COL_Request_ID + " TEXT," + COL_Queue_no + " INTEGER," + COL_Description + " TEXT," + COL_QMessage + " TEXT," + COL_QSTATUS + " TEXT, "+COL_RequestorName
            +" TEXT );";
    //endregion
    //region Slot table
    public static final String TABLE_Slot = "slot";
    public static final String COL_slottime = "slottime";
    public final String CREATE_TABLE_SLOT_LOG = "CREATE TABLE " + TABLE_Slot + "(" + COL_slottime + " TEXT )";
    //endregion
    //region Book table
    public static final String TABLE_BOOK = "book", COL_Doctor_id = "doctor_id", COL_Doctor_name = "doctor_name",
            COL_Book_Date = "book_date", COL_Short_id = "short_id", COL_Long_id = "long_id", COL_Profile = "Profile_Pic",
            COL_Remark = "remark",COL_RequestorNric = "requestornric",COL_RequestorName = "requestorname",COL_STATUS = "status";

    public final String CREATE_TABLE_Book = "CREATE TABLE " + TABLE_BOOK + "(" + COL_ID + " INTEGER PRIMARY KEY autoincrement," + COL_Clinic_id + " INTEGER ,"
            + COL_Clinic_name + " TEXT ," + COL_Doctor_id + " INTEGER ," + COL_Doctor_name + " TEXT ," + COL_Book_Date + " TEXT, " + COL_Short_id + " TEXT ,"
            + COL_Long_id + " TEXT , " + COL_Profile + " TEXT, " + COL_Remark + " TEXT, " + COL_RequestorNric + " TEXT, " + COL_RequestorName + " TEXT," + COL_STATUS + " TEXT )";
    //endregion
    //region medical profile
    public static final String TABLE_MEDPROFILE = "medprofile";

    public static final String COL_NRIC_Type = "nric_type", COL_NRIC = "nricid", COL_Gender = "gender", COL_Nationality = "nationality",
            COL_Allergy = "allergy", COL_MEMBERID = "memberid", COL_RELATION = "relation",COL_Member = "member",
            COL_Flag_Upload = "flag_upload",COL_Married_Status="married_status",COL_Language = "language",COL_Tel1_iso = "tel1_iso",
            COL_Tel1_code= "tel1_code",COL_Address1= "address1",COL_Address2 = "address2",COL_Address3="address3",COL_Address4="address4";

    public static final String CREATE_TABLE_MEDPROFILE = "CREATE TABLE " + TABLE_MEDPROFILE + " ( " + COL_ID + " INTEGER PRIMARY KEY autoincrement," + COL_Clinic_id + " INTEGER," +
            COL_NRIC_Type + " INTEGER , " + COL_NRIC + " TEXT ," + COL_NAME + " TEXT," + COL_Gender + " INTEGER, " + COL_Nationality + " TEXT," + COL_DOB + " TEXT," +
            COL_BLOCK + " TEXT," + COL_STREET + " TEXT, " + COL_BUILDING + " TEXT," + COL_UNIT + " TEXT," + COL_POSTAL_CODE + " TEXT," + COL_TEL1 + " TEXT," +
            COL_TEL2 + " TEXT ," + COL_Email + " TEXT, " + COL_Allergy + " TEXT ," + COL_MEMBERID + " TEXT , " + COL_RELATION + " TEXT , "
            + COL_Member+ " INTEGER ," + COL_Flag_Upload + " INTEGER  , "+ COL_Married_Status + " INTEGER , "+ COL_Language+ " INTEGER , " + COL_Tel1_iso + " TEXT ,"
            + COL_Tel1_code + " INTEGER , "+ COL_Address1 + " TEXT ," +COL_Address2 +" TEXT, "+ COL_Address3 +" TEXT, "+ COL_Address4 +" TEXT )" ;


    //endregion
    //region  alarm_table
    public static final String TABLE_APPT_ALARM = "appointment_alarm";
    public static final String COL_2hours = "_2hours", COL_1day = "_1day", COL_2days = "_2days", COL_1week = "_1week",COL_AlarmID = "alarm_id",
            COL_alarm_unit  = "unit";

    public static final String CREATE_TABLE_APPT_ALARM = "CREATE TABLE " + TABLE_APPT_ALARM + "(" + COL_Long_id + " TEXT, " + COL_Short_id + " TEXT ," + COL_2hours + " Boolean, " +
            COL_1day + " Boolean, " + COL_2days + " Boolean ," + COL_1week + " Boolean, "+ COL_AlarmID+" Integer ," + COL_alarm_unit + " TEXT " +
            ")";
    //endregion
    //region fin capture table
    public static final String TABLE_FIN_Capture = "Fin_Capture";
    public static final String COL_FinId = "fin_ID",COL_MemberId= "memberId",COL_Title = "Title",
                                COL_CreateDate = "CreateDate",COL_Desc = "Desc",COL_IsReview = "isReview";
    public static final String CREATE_TABLE_FIN_Capture = "CREATE TABLE "+ TABLE_FIN_Capture + " ( "+
            COL_FinId+" INTEGER PRIMARY KEY autoincrement,  " + COL_MEMBERID + " TEXT , " + COL_Title + " TEXT ," +
            COL_CreateDate + " TEXT, " +  COL_Desc + "  TEXT , " + COL_IsReview+" Boolean )";
    //endregion
    //region lab capture table
    public static final String TABLE_LAB_Capture = "Lab_Capture";
    public static final String COL_LabID= "Lab_ID";

    public static final String CREATE_TABLE_LAB_Capture = "CREATE TABLE " + TABLE_LAB_Capture + " ( "+ COL_LabID +" INTEGER PRIMARY KEY autoincrement ," +
            COL_MemberId +" TEXT, " + COL_Title + " TEXT, " + COL_CreateDate + " TEXT, " + COL_Desc +" TEXT , " + COL_IsReview+" Boolean )";
    //endregion
    //region Rad capture table
    public static final String TABLE_Rad_Capture = "Rab_Capture";
    public static final String COL_RADID= "Rad_ID";

    public static final String CREATE_TABLE_RAD_Capture = "CREATE TABLE " + TABLE_Rad_Capture + " ( "+ COL_RADID +" INTEGER PRIMARY KEY autoincrement ," +
            COL_MemberId +" TEXT, " + COL_Title + " TEXT, " + COL_CreateDate + " TEXT, " + COL_Desc +" TEXT , " + COL_IsReview+" Boolean )";
    //endregion
    //region Certs capture table
    public static final String TABLE_Certs_Capture = "Certs_Capture";
    public static final String COL_CertsID= "Certs_ID";

    public static final String CREATE_TABLE_Certs_Capture = "CREATE TABLE " + TABLE_Certs_Capture + " ( "+ COL_CertsID +" INTEGER PRIMARY KEY autoincrement ," +
            COL_MemberId +" TEXT, " + COL_Title + " TEXT, " + COL_CreateDate + " TEXT, " + COL_Desc +" TEXT , " + COL_IsReview+" Boolean )";

    //endregion
    //region medicine_reminder
  /*  public static final String TABLE_REMINDER = "medicines_reminder";
    public static final String COL_Med_Name = "medicine_name", COL_Take_times = "take_medicines",COL_Days = "days" , COL_Interval = "interval",
            COL_Weeks = "weeks" , COL_StartDates = "startdate", COL_EndDate = "enddate", COL_RemainTime = "remain_times" ,
            COL_Descirption = "description",COL_Flag_take="flag_take",COL_TempDate = "Temp_date";

    public static final String CREATE_TABLE_REMINDER = "CREATE TABLE " + TABLE_REMINDER + " ( "+ COL_ID +" INTEGER PRIMARY KEY autoincrement," + COL_Med_Name+" TEXT," +
            COL_Take_times + " TEXT , "+ COL_Days+ " INTEGER , " + COL_Interval +" INTEGER , "+ COL_Weeks +" TEXT , "+ COL_StartDates+ " TEXT , "+ COL_EndDate +" TEXT," +
            COL_RemainTime + " INTEGER , "+ COL_Descirption + " TEXT ," + COL_Flag_take + " INTEGER , "+ COL_TempDate + " TEXT )";*/
    //endregion


    //region medical_dispense
    public static  final String TABLE_MEDICAL_DISPENSE = "medical_dispense";
    public static final String  COL_QueueID="QueueID",COL_VisitNo="VisitNo",COL_VisitDate="VisitDate",COL_IsReminder = "IsReminder",
            COL_StartDate = "startDate",COL_StartTime= "startTime",COL_NumofDays = "NumofDays",COL_Patientname = "Name";

    public static final String CREATE_TABLE_MEDICAL_DISPENSE = "CREATE TABLE " + TABLE_MEDICAL_DISPENSE + "(" +
            COL_MEMBERID + " TEXT, "+ COL_NRIC + " TEXT ,"+ COL_NRIC_Type + " TEXT, " +COL_DOB + " TEXT," +
            COL_QueueID + " TEXT ," + COL_VisitNo + " TEXT , "+ COL_VisitDate +" TEXT , " + COL_Clinic_id +" TEXT ," +
           // COL_IsReminder + " TEXT ," + COL_StartDate + " TEXT ," + COL_StartTime + " TEXT ," + COL_NumofDays + " TEXT , "
            COL_Patientname + " TEXT )";

    public static  final String TABLE_MEDICAL_Item = "medical_item";

    public static  final String COL_itemno="itemno",COL_medicalType="medicalType",COL_medicalCode="medicalCode",
            COL_medicalName="medicalName",COL_medicalUsage="medicalUsage",COL_medicalDosage= "medicalDosage",
            COL_medicalDosageUnit= "medicalDosageUnit",COL_medicalFreq="medicalFreq",COL_medicalFreqCode="medicalFreqCode",
            COL_medicalTotalQty="medicalTotalQty",COL_medicalTotalQtyUnit="medicalTotalQtyUnit",COL_PreCaution1="PreCaution1",
            COL_PreCaution2="PreCaution2",COL_PreCaution3="PreCaution3",COL_downloaded="downloaded",COL_isremider ="isReminder",
            COL_EndDate="endDate",COL_NextDateTime="nextDate",COL_NumberofDays = "numberofDays", COL_SlotInterval= "SlotInterval";

    public static  final  String CREATE_TABLE_MEDICAL_ITEM = "CREATE TABLE " + TABLE_MEDICAL_Item + " (" +
            COL_VisitNo + " TEXT," + COL_Clinic_id + " TEXT,"+ COL_ID + " TEXT," + COL_itemno + " TEXT,"+
            COL_medicalType + " TEXT ," + COL_medicalCode + " TEXT," + COL_medicalName + " TEXT ," + COL_medicalUsage + " TEXT,"+
            COL_medicalDosage + " TEXT," + COL_medicalDosageUnit + " TEXT," + COL_medicalFreq + " TEXT," + COL_medicalFreqCode + " TEXT,"+
            COL_medicalTotalQty + " TEXT," + COL_medicalTotalQtyUnit + " TEXT," + COL_PreCaution1+ " TEXT," + COL_PreCaution2 + " TEXT,"+
            COL_PreCaution3 + " TEXT," + COL_downloaded + " TEXT , " + COL_isremider +" Integer, " + COL_StartDate+ " Integer , "+COL_EndDate +
            " Integer , "+ COL_NextDateTime+" Integer , "+ COL_NumberofDays +" Integer ," + COL_SlotInterval+" Text )";

    public static final String TABLE_Medical_Reminder = "medical_remider";

    public static final String COl_setdate = "setDate" ,COL_UUID = "uuid", COL_IDs = "IDs",COL_Message = "Messages",COL_freqCode = "freqCode";

    public static final  String CREATE_TABLE_Medical_Reminder = "CREATE TABLE " + TABLE_Medical_Reminder + "(" + COL_ID +" Integer  PRIMARY KEY autoincrement," +
            COL_VisitNo + " TEXT ," + COL_UUID  + " TEXT ," + COl_setdate + " TEXT," + COL_IDs + " TEXT ," + COL_Message + " TEXT ," +
            COL_freqCode + " TEXT )";

    //endregion
    //region public holiday
    public static final String TABLE_HOLIDAY = "public_holiday";
    public static final String COL_holiday_des = "holidaydesc" , COL_holiday_Date = "HolidayDate";
    public static final String CREATE_TABLE_HOLIDQY = "CREATE TABLE " + TABLE_HOLIDAY + " ("+ COL_holiday_des + " TEXT, "+ COL_holiday_Date + " TEXT  )";
    //endregion

    //region Specialty
    public static final String TABLE_SPECIALTY = "specialty";
    /*  public static final String COL_Specialty_name = "specialty_name" , COL_Specialty_type = "specialty_type";
      public static final String CREATE_TABLE_SPECIALTY = "CREATE TABLE " + TABLE_SPECIALTY + " ("+ COL_Specialty_name + " TEXT , " + COL_Specialty_type +" TEXT )";
      //endregion*/
    //region Services
    public static final String TABLE_Services = "Services";
    public static final String COL_Services_Id = "ID" , COL_ServiceClinicType = "Clinic_Type",COL_Specialty_Name= "Specialty_name";
    public static final String CREATE_TABLE_Services = "CREATE TABLE " + TABLE_Services + " ( " + COL_Services_Id + " Integer , " +
            COL_ServiceClinicType + " TEXT ," + COL_Specialty_Name + " TEXT )";
    //endregion
    //region Services_item
    public static final String TABLE_Services_Item = "Service_Items";
    public static final String  COL_ServiceName = "Service_name";
    public static final String CREATE_TABLE_Services_Item = "CREATE TABLE " + TABLE_Services_Item + " ( " + COL_Services_Id + " Integer , " +
            COL_ServiceName + " TEXT   )";
    //endregion
    //region Clinic Services
    public static final String TABLE_Clinic_Services = "Clinic_Services";
    public static final String  COL_ClinicService= "Clinic_Service_name";
    public static final String CREATE_TABLE_Clinic_Services = "CREATE TABLE " + TABLE_Clinic_Services + " ( " + COL_Clinic_id + " Integer , " +
            COL_ClinicService + " TEXT   )";
    //endregion

    //region drop specilty table
    public static final String  Drop_Specilty_table= "Drop table if exists " + TABLE_SPECIALTY;
    //endregion
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_TABLE_CLINIC);
        database.execSQL(CREATE_TABLE_RQUEUE_LOG);
        database.execSQL(CREATE_TABLE_SLOT_LOG);
        database.execSQL(CREATE_TABLE_Book);
        database.execSQL(CREATE_TABLE_MEDPROFILE);
        database.execSQL(CREATE_TABLE_APPT_ALARM);
       // database.execSQL(CREATE_TABLE_REMINDER);
        database.execSQL(CREATE_TABLE_HOLIDQY);
        database.execSQL(CREATE_TABLE_Services);
        database.execSQL(CREATE_TABLE_Services_Item);
        database.execSQL(CREATE_TABLE_Clinic_Services);
        database.execSQL(CREATE_TABLE_TEST_CLINIC);
        database.execSQL(CREATE_TABLE_MEDICAL_DISPENSE);
        database.execSQL(CREATE_TABLE_MEDICAL_ITEM);
        database.execSQL(CREATE_TABLE_Medical_Reminder);
        database.execSQL(CREATE_TABLE_FIN_Capture);
        database.execSQL(CREATE_TABLE_LAB_Capture);
        database.execSQL(CREATE_TABLE_RAD_Capture);
        database.execSQL(CREATE_TABLE_Certs_Capture);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // clear all data

        if(oldVersion == 1){
            db.execSQL("ALTER TABLE " + TABLE_BOOK + " ADD " + DatabaseHandler.COL_RequestorNric + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_BOOK + " ADD " + DatabaseHandler.COL_RequestorName + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_Queue_Request_Log + " ADD " + DatabaseHandler.COL_QMessage + " TEXT");
        } if(oldVersion < 2){
            db.execSQL("ALTER TABLE " + TABLE_Queue_Request_Log + " ADD " + DatabaseHandler.COL_QMessage + " TEXT");
        }
        if(oldVersion <3 ){
            db.execSQL("ALTER TABLE " + TABLE_BOOK + " ADD " + DatabaseHandler.COL_STATUS + " TEXT");
        }
        if(oldVersion < 4) {
            db.execSQL("ALTER TABLE " + TABLE_APPT_ALARM + " ADD " + DatabaseHandler.COL_Short_id + " TEXT");
            db.execSQL("ALTER TABLE " + TABLE_CLINIC + " ADD " + DatabaseHandler.COL_IS_APPT_ENABLED + " TEXT");
        }
        if(oldVersion < 5){
            db.execSQL("ALTER TABLE " + TABLE_Queue_Request_Log + " ADD " + DatabaseHandler.COL_QSTATUS + " TEXT");
        }
        if(oldVersion < 6){
            db.execSQL("ALTER TABLE " + TABLE_Queue_Request_Log + " ADD " + DatabaseHandler.COL_RequestorName + " TEXT");
        }
        if(oldVersion < 7){
            db.execSQL("ALTER TABLE " + TABLE_MEDPROFILE + " ADD " + DatabaseHandler.COL_Married_Status + " INTEGER");
        }
        if(oldVersion < 8){
            db.execSQL("ALTER TABLE " + TABLE_CLINIC + " ADD " + DatabaseHandler.COL_Clinic_note + " TEXT");
        }
        if(oldVersion < 9){
            db.execSQL("ALTER TABLE " + TABLE_APPT_ALARM + " ADD " + DatabaseHandler.COL_alarm_unit + " TEXT");
        }
        if(oldVersion  <10){
            db.execSQL("ALTER TABLE " + TABLE_CLINIC + " ADD " + DatabaseHandler.COL_HECODE + " TEXT");
        }
        if(oldVersion < 11){
            db.execSQL(CREATE_TABLE_HOLIDQY);
        }
        if(oldVersion < 12){
            //  db.execSQL(CREATE_TABLE_SPECIALTY);
        }
        if(oldVersion < 13){
            db.execSQL(CREATE_TABLE_Services);
            db.execSQL(CREATE_TABLE_Services_Item);
            db.execSQL(CREATE_TABLE_Clinic_Services);
            db.execSQL(Drop_Specilty_table);
        }
        if(oldVersion < 14){
            db.execSQL(CREATE_TABLE_TEST_CLINIC);
            db.execSQL("ALTER TABLE " + TABLE_MEDPROFILE + " ADD " + DatabaseHandler.COL_Language + " INTEGER");
        }
        if(oldVersion < 15 ){
            db.execSQL("ALTER TABLE " + TABLE_CLINIC + " ADD " + DatabaseHandler.COL_ApptMode + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_TEST_CLINIC + " ADD " + DatabaseHandler.COL_ApptMode + " TEXT ");
        }
        if(oldVersion < 16){
            db.execSQL("ALTER TABLE " + TABLE_MEDPROFILE + " ADD " + DatabaseHandler.COL_Tel1_iso + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_MEDPROFILE + " ADD " + DatabaseHandler.COL_Tel1_code + " INTEGER ");
            db.execSQL("ALTER TABLE " + TABLE_MEDPROFILE + " ADD " + DatabaseHandler.COL_Address1 + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_MEDPROFILE + " ADD " + DatabaseHandler.COL_Address2 + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_MEDPROFILE + " ADD " + DatabaseHandler.COL_Address3 + " TEXT ");
            db.execSQL("ALTER TABLE " + TABLE_MEDPROFILE + " ADD " + DatabaseHandler.COL_Address4 + " TEXT ");

        }
        if(oldVersion<17){
            db.execSQL(CREATE_TABLE_MEDICAL_DISPENSE);
            db.execSQL(CREATE_TABLE_MEDICAL_ITEM);
            db.execSQL(CREATE_TABLE_Medical_Reminder);
        }
        if(oldVersion<18){
            db.execSQL(CREATE_TABLE_FIN_Capture);
            db.execSQL(CREATE_TABLE_LAB_Capture);
            db.execSQL(CREATE_TABLE_RAD_Capture);
            db.execSQL(CREATE_TABLE_Certs_Capture);
        }
        // recreate the tables dsf
        //  onCreate(db);

    }
}
