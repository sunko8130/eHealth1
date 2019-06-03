package sg.com.ehealthassist.ehealthassist.Models;



import android.content.res.Resources;

import sg.com.ehealthassist.ehealthassist.Other.AppController;

/**
 * Created by User on 6/29/2017.
 */

public class Constant {
    public static final String NODE_STATUS="Status";
    public static final String NODE_DATA="Data";
    public static final String NODE_ID="Id";
    public static final String NODE_NAME="Name";
    public static final String NODE_ADDRESS="Address";
    public static final String NODE_BLOCKNO="BlockNo";
    public static final String NODE_STREET="Street";
    public static final String NODE_UNITNO="UnitNo";
    public static final String NODE_BUILDING="Building";
    public static final String NODE_POSTAL="PostalCode";
    public static final String NODE_REGION="Region";
    public static final String NODE_PLACETOWN="PlaceTown";
    public static final String NODE_NEARESTMRT="NearestMrt";
    public static final String NODE_CONTACT="Contact";
    public static final String NODE_TEL1="Tel1";
    public static final String NODE_TEL2="Tel2";
    public static final String NODE_FAX="Fax";
    public static final String NODE_WEBSITE="Website";
    public static final String NODE_EMAIL="Email";
    public static final String NODE_MobileNo="Mobile";
    public static final String NODE_CountryISOCode="CountryISOCode";
    public static final String NODE_CountryCode="CountryCode";
    public static final String NODE_NRIC="Nric";
    public static final String NODE_CLINICTYPE="ClinitType";
    public static final String NODE_OPERATINGHOURS="OperatingHours";
    public static final String NODE_LOCATION="Location";
    public static final String NODE_LAT="Lat";
    public static final String NODE_LNG="Lng";
    public static final String NODE_IS247="Is247";
    public static final String NODE_ISCHAS="IsChas";

    public static final String NODE_ISQUEUEENABLED="IsQueueEnabled";
    public static final String NODE_ISAPPTENABLED="IsApptEnabled";
    public static final String NODE_TOSHOW="ToShow";
    public static final String NODE_LOGOID="LogoId";
    public static final String NODE_PAGINATION="Pagination";
    public static final String NODE_HASNEXT="HasNext";
    public static final String NODE_NEXTFROM="NextFrom";
    public static final String NODE_Specialty="Specialty";
    public static final String NODE_Clinic_note="Note";
    public static final String NODE_Clinic_HECODE="HECode";
    // Auth
    public static final String NODE_MemberId="MemberId";
    public static final String NODE_AccessToken="AccessToken";
    public static final String NODE_AccountVerified="AccountVerified";
    // Queue Request
    public static final String NODE_RequestId="RequestId";
    public static final String NODE_QMessage="QMessage";
    public static final String NODE_RejectReason="RejectReason";

    // Queue Watch
    public static final String NODE_IsOpen="IsOpen";
    public static final String NODE_NoOfPeopleInQueue="NoOfPeopleInQueue";
    public static final String NODE_CurrentQueueNo="CurrentQueueNo";
    // Error
    public static final String NODE_ErrorCode="ErrorCode";
    public static final String NODE_ErrorMessage="ErrorMessage";
    // Doctor ON Duty
    public static final String NODE_DUTYDATE="DutyDate";
    public static final String NODE_DOCTORS="Doctors";

    // public static final String NODE_ID="Id";
    //public static final String NODE_DODNAME="Name";

    public static final String CHECK_ALLDAY="ALL Day";
    public static final String CHECK_MORNING="Morning";
    public static final String CHECK_AFTERNOON="AfterNoon";
    public static final String CHECK_EVENING="Evening";
    public static final String CHECK_NIGHT="Night";

    public static final String NODE_ApptDoctor= "Doctor";
    public static final String NODE_ApptId= "Id";
    public static final String NODE_ApptClinicId= "ClinicId";
    public static final String NODE_ApptCasDoctorId= "CasDoctorId";
    public static final String NODE_ApptFullName= "FullName";
    public static final String NODE_ApptDisplayName= "DisplayName";
    public static final String NODE_ApptProfilePic= "ProfilePicture";
    public static final String NODE_ApptSmc= "Smc";
    public static final String NODE_ApptStatus= "Status";
    public static final String NODE_ApptEditor= "Editor";
    public static final String NODE_ApptCreator= "Creator";
    public static final String NODE_ApptAvailableSlots= "AvailableSlots";
    public static final String NODE_ApptSession= "ApptSession";
    public static final String NODE_IsSuccess="IsSuccess";
    public static final String NODE_LongBookId="LongBookingId";
    public static final String NODE_ShortBookId="ShortBookingId";
    public static final String NODE_ClinicName = "ClinicName";
    public static final String NODE_DoctorId = "DoctorId";
    public static final String NODE_DoctorDisplayName = "DoctorDisplayName";
    public static final String NODE_BookingTime = "BookingTime";
    public static final String NODE_Remarks = "Remarks";
    public static final String NODE_RequestorNric = "RequestorNric";
    public static final String NODE_RequestorName = "RequestorName";
    public static final String NODE_RequestorNricType = "NricType";
    public static final String NODE_MESSAGE="Message";
    //Event
    public static final String NODE_EventId="EventID";
    public static final String NODE_EventName="EventName";
    public static final String NODE_EventDesc="EventDesc";
    public static final String NODE_FromTime="FromTime";
    public static final String NODE_ToTime="ToTime";
    public static final String NODE_FromDate="FromDate";
    public static final String NODE_ToDate="ToDate";
    public static final String NODE_Venue="Venue";
    public static final String NODE_ContactNo="ContactNo";
    public static final String NODE_CreatedBy="CreatedBy";
    public static final String NODE_CreatedDate="CreatedDate";
    //Queue
    public static final String NODE_STATUSCODE="StatusCode";
    public static final String NODE_PATIENTNRIC="PatientNric";
    public static final String NODE_PATIENTNAME="PatientName";
    public static final String NODE_PATIENTDOB="PatientDob";
    public static final String NODE_REQUESTDATETIME="RequestDateTime";
    public static final String NODE_QUEUENO="QueueNo";
    public static final String NODE_CURRENTQUEUE="CurrentQueue";
    public static final String NODE_QSTATUS="QStatus";
    public static final String NODE_NRICTYPE="NricType";
    //publicholiday
    public static final String NODE_HolidayDesc="HolidayDesc";
    public static final String NODE_HolidayDate="HolidayDate";

    public static final String NODE_VersionNo="VersionNo";
    public static final String NODE_ForceUpdate="ForceUpdate";
    public static final String NODE_URL="Url";

    public static final String CHECK_STATUS_OK="Ok";
    public static final String CHECK_STATUS_ERROR="Errors";

    //============================================================
    //		Volley Get	API UrL
    //============================================================

    //  public static final String main_URL = "http://ehealthassist-uat.southeastasia.cloudapp.azure.com/eha_dev";
    //public static final String main_URL = "http://ehealthassist.southeastasia.cloudapp.azure.com/eha"; // ***********
    // public static final String main_URL = "http://ehealthassist-uat.southeastasia.cloudapp.azure.com/eha"; // production

    //uat url

    public static final String main_URL = "http://ehealthassist-uat.southeastasia.cloudapp.azure.com/eha_jwt/v1"; // uat jwt url
    public static final String url_term_privacy = "http://ehealthassist.southeastasia.cloudapp.azure.com/"; // uat
    public static final String medicaldispense_URL = "http://ehealthassist-uat.southeastasia.cloudapp.azure.com/MedicationServices";//uat jwt url
    public static String URL_Authtoken= "http://ehealthassist-uat.southeastasia.cloudapp.azure.com/AuthenticationService/oauth2/token";// jwt uat
    public static String URL_event= "http://ehealthassist-uat.southeastasia.cloudapp.azure.com/EventServices/api/event/";

    // http://ehealthassist-uat.southeastasia.cloudapp.azure.com/EventServices/api/event/ViewEvent

    // production url
   /* public static final String main_URL = "https://www.ehealthassist.com.sg/eha_v2_1/v1"; // jwt production
    public static final String url_term_privacy = "https://www.ehealthassist.com.sg/"; // production
    public static final String medicaldispense_URL = "https://www.ehealthassist.com.sg/medicationservices_v2_1"; // jwt production
    public static final String URL_Authtoken= "https://www.ehealthassist.com.sg/authenticationservice_v2_1/oauth2/token"; // jwt production
    public static final String URL_event= "https://www.ehealthassist.com.sg/eventServices_v2_1/api/event/";*/

    public static final  String URL_CLINICINFO=main_URL +"/clinics/";  // ***GetClinicListingUpdate
    public static final  String URL_DOCTORONDUTY=main_URL+"/clinic/";
    public static final  String URL_CLINICDETAILINFO=main_URL+"/clinic/";

    public static final  String URL_REGISTER=main_URL+"/member/register"; // *** RegisterMember
    public static final  String URL_AUTH= main_URL + "/member/auth"; // *** MemberLogin
    public static final  String URL_MemberQuery=main_URL+"/member/q/";
    public static final  String URL_LoginView= main_URL +"/member/loginView";
    public static final  String URL_REVERIFYMEMBER=main_URL+"/member/reverify/"; // *** ReVerifyMember
    public static final  String URL_VERIFYMEMBER=main_URL+"/member/verify/";
    public static final  String URL_SMSOTPtoken=main_URL+"/member/verifysms";
    public static final  String URL_AccountUpdate=main_URL +"/member/update";
    public static final  String URL_ChangePassword=main_URL+"/member/changepwd";
    public static final  String URL_ForgotPassword=main_URL+"/member/resetpwd";
    public static final  String URL_Logout=main_URL+"/member/logout";

    public static final  String URL_MEDICALPROFILEUPLOAD=main_URL+"/medicalprofile/upload"; // ** UploadMedicalProfile
    public static final  String URL_insertmemberprofile=main_URL+"/medicalprofile/InsertMemberListingProfile";
    public static final  String URL_deletememberprofile=main_URL+"/medicalprofile/deleteMemberListingProfile";
    public static final  String URL_updatememberprofile=main_URL+"/medicalprofile/UpdateMemberListingProfile";
    public static final  String URL_viewmemberprofile=main_URL+"/medicalprofile/ViewAllMemberListingProfile";
    public static final  String URL_PatientProfileDownloadStatus = main_URL+"/medicalprofile/getPatientProfileDownloadedStatus";

    public static final  String URL_QUEUEREGISTER=main_URL+"/queue/register/"; // ** QueueRegistration
    public static final  String URL_QUEUEWATCH=main_URL+"/queue/watch/"; // *** QueueWatch
    public static final  String URL_QUEUEVIEW=main_URL+"/queue/q";
    public static final  String URL_QUEUECANCEL=main_URL+"/queue/cancel/";

   /* public static final  String URL_SMS="http://www.ehealthassist.com.sg/member/verifysms.aspx?";*/

    public static final  String URL_LISTAppointmentSlot=main_URL+"/appt/"; // *** LISTAppointmentSlot
    public static final  String URL_LISTBookSlot=main_URL+"/appt/Book_Mobile"; // *** BookAppointmentSlot
    public static final  String URL_CANCEL=main_URL+"/appt/cancel";
    public static final  String URL_APPTCONFIRM=main_URL+"/appt/confirm";
    public static final  String URL_VIEW=main_URL+"/appt/view";

    public static final  String URL_CountryCode=main_URL+"/CountryCode/getcountrycode";

  //  public static final  String URL_EventView=main_URL+"/event/view";
    public static final  String URL_EventView=URL_event+"ViewEvent";
    public static final  String URL_EventRegistraion=URL_event+"EventRegistration";
    public static final  String URL_EventRegView=main_URL+"/event/EventRegistrationView";

    public static final  String URL_Rating=main_URL+"/rating/insertRating";

    public static final  String URL_TNC= url_term_privacy +"terms/";
    public static final  String URL_PRIVACY= url_term_privacy+"private/";

    public static final  String URL_PCODE=main_URL+"/address/search";

    public static final  String URL_PUBLICHOLIDAY=main_URL+"/holiday";

    public static final  String URL_VERSIONCHANGES=main_URL+"/version/latest";

    public static final  String URL_Clinic_Services = main_URL+"/ClinicServicesLK";

    public static final  String URL_PaymentInvoice = main_URL+"/PaymentInvoice/getInvoice";
    public static final  String URL_InvoiceRate = main_URL+"/PaymentInvoice/updateInvoiceRating";

    public static final  String URL_ReceiptPDF =  main_URL+"/eReceipt.ashx?";

  //  public static final  String URL_MedDispenselist=medicaldispense_URL+"/MedicationServices/api/medicaldispense/getMedicalDispense"; // uat
  //  public static final  String URL_MedDispensecount=medicaldispense_URL+"/MedicationServices/api/medicaldispense/getcount"; // uat
   // public static final  String URL_MedDispenseupdatedownload=medicaldispense_URL+"/MedicationServices/api/medicaldispense/updateDownloadedMedItem"; // uat

    public static final  String URL_MedDispenselist=medicaldispense_URL+"/api/medicaldispense/getMedicalDispense";
    public static final  String URL_MedDispensecount=medicaldispense_URL+"/api/medicaldispense/getcount";
    public static final  String URL_MedDispenseupdatedownload=medicaldispense_URL+"/api/medicaldispense/updateDownloadedMedItem";

    public static final  String URL_GetMessage=main_URL+"/Message/getMessages";
    public static final  String URL_UpdateMessage=main_URL+"/Message/updateMessage";

    public static final String pm2_5_update = "http://api.nea.gov.sg/api/WebAPI/?dataset=pm2.5_update&keyref=781CF461BB6606AD1260F4D81345157FC88237B4C07B9CA2";
    public static final String psi_update = "http://api.nea.gov.sg/api/WebAPI/?dataset=psi_update&keyref=781CF461BB6606AD1260F4D81345157FC88237B4C07B9CA2";



    //www.ehaapis.com  202.69.146.254:8080
    //http://www.ehealthassist.sg/public/policies/terms.html

}
