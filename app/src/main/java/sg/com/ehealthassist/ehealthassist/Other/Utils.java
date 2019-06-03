package sg.com.ehealthassist.ehealthassist.Other;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.Account.ActivityLogIn;
import sg.com.ehealthassist.ehealthassist.Account.ActivityPIN;
import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.Alarm.AlarmReceiver;
import sg.com.ehealthassist.ehealthassist.Clinic.ActivityPreferredClinic;
import sg.com.ehealthassist.ehealthassist.Clinic.PreferredClinicAdapter;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.GDAA.GDAAConnect;
import sg.com.ehealthassist.ehealthassist.Profiles.ActivityMedProfileList;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Profiles.ActivityMedicalProfile;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 6/29/2017.
 */

public class Utils {
    //region Valid Email ()
    public static boolean isValidEmail(CharSequence email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    //endregion
    //region Valid NRIC ()
    public static boolean isValidNRIC(CharSequence nricIn, String nricType) {

        String nric = nricIn.toString().trim().toUpperCase();
        if (nric.length() != 9) return false;//
        if (!TextUtils.isDigitsOnly(nric.substring(1, 8))) return false;
        String firstChar = nric.substring(0, 1);
        /*if(!((firstChar.equals("G") || firstChar.equals("F")) && nricType.equals("FIN"))
                && !((firstChar.equals("S") || firstChar.equals("T")) && !(nricType.equals("FIN"))))*/
       /* if (
                (!((firstChar.equals("G") || firstChar.equals("F"))
                        && nricType.equals("FIN"))
                && !(firstChar.equals("S") &&
                        !(nricType.equals("FIN")))) ||
                        (firstChar.equals("T") && nricType.equals("OTHER"))
                )
            return false;*/
        if (!firstChar.equals("G") && !firstChar.equals("F") && !firstChar.equals("S") && !firstChar.equals("T"))
            return false;

        //if (firstChar.equals("T") && !nricType.equals("OTHER")) return false;

        int weight[] = {2, 7, 6, 5, 4, 3, 2};
        int[] TWeight = {2, 1, 2, 7, 6, 5, 4, 3, 2};
        String checkLetter[] = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "Z", "J"};
        String checkLetterFG[] = {"K", "L", "M", "N", "P", "Q", "R", "T", "U", "W", "X"};

        int i, weightTotal = 0, checkDigitNo;
        String validLastChar = "";
        if (firstChar.equals("S") || firstChar.equals("F")) {
            for (i = 0; i <= 6; i++) {
                weightTotal = weightTotal + (Integer.parseInt(nric.substring(i + 1, i + 2)) * weight[i]);/// 0(1,2),1(2,3),2(3,4),3(4,5),4(5,6),5(6,7),6(7,8)
            }
            checkDigitNo = 11 - (weightTotal % 11);
            if (checkDigitNo <= 0 || checkDigitNo >= 12) return false;
            validLastChar = (firstChar.equals("S")) ? checkLetter[checkDigitNo - 1] : checkLetterFG[checkDigitNo - 1];

        } else if (firstChar.equals("G") || firstChar.equals("T")) {
            Log.e("Check Nric TTTTTT", nric.substring(1, 8));

            if (TextUtils.isDigitsOnly(nric.substring(1, 8)) && (nric.length() == 8 || nric.length() == 9)) {
                String storenric = ("20" + nric.substring(1, 8));
                for (i = 0; i <= 8; i++) {
                    weightTotal = (weightTotal + (Integer.parseInt(storenric.substring(i, i + 1)) * TWeight[i]));//0{1,2},1{2,3},2{3,4},3{4,5},4{5,6},5{6,7},6{7,8},7{8,9},8{9,10}
                }
                checkDigitNo = (11 - (weightTotal % 11));
                if (((checkDigitNo > 0) && (checkDigitNo < 12))) {
                    validLastChar = (firstChar.equals("T")) ? checkLetter[checkDigitNo - 1] : checkLetterFG[checkDigitNo - 1];

                } else {
                    return false;
                }
            }
        }

        return (nric.substring(0, 8) + validLastChar).equals(nric);
    }

    public void isValidNrics() {

    }

    //endregion
    //region Valid SGMobile()
    public static boolean isValidSGMobile(CharSequence mobile) {
        return TextUtils.isDigitsOnly(mobile) && mobile.length() == 8 && (mobile.toString().startsWith("8") || mobile.toString().startsWith("9"));
    }

    //endregion
    //region Valid Network()
    public static boolean hasInternetConnection(final Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            return false;
        } else
            return true;
    }

    //endregion
    //region Valid Clinic ID ()
    public static boolean isValidClinic_id(CharSequence clinicid) {
        return TextUtils.isDigitsOnly(clinicid) && clinicid.length() == 4 && (clinicid.toString().startsWith("1") || clinicid.toString().startsWith("9"));
    }

    //endregion
    //region Valid SGNumber ()
    public static boolean isValidSGNumber(CharSequence phoneNumber) {
        return TextUtils.isDigitsOnly(phoneNumber) && phoneNumber.length() == 8;
    }

    //endregion
    //region GETIMei()
    public static String getImei(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return TODO;
        }
        return telephonyManager.getDeviceId();
    }
    //endregion
    //region Definition () CHAS,QUEUE,24Hr
   public static void showIcondefinition(final Context context, final String msg, final String from_activity) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_find_clinic);
        TextView textmsg = (TextView) dialog.findViewById(R.id.txtclinicdef);
        ImageView image = (ImageView) dialog.findViewById(R.id.imageclinicdefination);
        Button dialogButton = (Button) dialog.findViewById(R.id.btnfindClinic);
        switch (msg) {
            case "CHAS":
                dialog.setTitle("CHAS");
                textmsg.setText(R.string.msgbox_icon_CHAS);
                image.setImageResource(R.drawable.ic_chas);
                break;
            case "24HC":
                dialog.setTitle("24 Hours Clinic");
                textmsg.setText(R.string.msgbox_icon_24HC);
                image.setImageResource(R.drawable.ic_hours_24);
                break;
            case "QNow":
                dialog.setTitle("Queue Now");
                textmsg.setText(R.string.msgbox_icon_QNow);
                image.setImageResource(R.drawable.ic_supervisor_account_black_24dp);
                break;
        }
        if (from_activity.equals("ActivityNearbyDoctor")) {
            dialogButton.setText("OK");
        }
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                DBClinicInfo databaseHandlerClinicInfo = new DBClinicInfo(context);
                List<ClinicInfo> lstclinic = new ArrayList<ClinicInfo>();
                if (from_activity.equals("ActivityPreferredClinic")) {
                    if (ActivityPreferredClinic.clinicInfos.size() > 0) {
                        for (int i = 0; i < ActivityPreferredClinic.clinicInfos.size(); i++) {
                            switch (msg) {
                                case "CHAS":
                                    if (ActivityPreferredClinic.clinicInfos.get(i).is_isQueueEnabled()) {
                                        if (ActivityPreferredClinic.clinicInfos.get(i).is_isBookmarked())
                                            lstclinic.add(ActivityPreferredClinic.clinicInfos.get(i));
                                    }
                                    break;
                                case "24HC":
                                    if (ActivityPreferredClinic.clinicInfos.get(i).is_is24Hours()) {
                                        if (ActivityPreferredClinic.clinicInfos.get(i).is_isBookmarked())
                                            lstclinic.add(ActivityPreferredClinic.clinicInfos.get(i));
                                    }
                                    break;
                                case "QNow":
                                    if (ActivityPreferredClinic.clinicInfos.get(i).is_isQueueEnabled()) {
                                        if (ActivityPreferredClinic.clinicInfos.get(i).is_isBookmarked())
                                            lstclinic.add(ActivityPreferredClinic.clinicInfos.get(i));
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                    PreferredClinicAdapter clinicInfoAdapter = new PreferredClinicAdapter(ActivityPreferredClinic.activity, lstclinic, "ActivityPreferredClinic");

                }
                if (from_activity.equals("ActivitySearchClinic")) {

                    switch (msg) {
                        case "CHAS":
                            lstclinic = databaseHandlerClinicInfo.getAllClinicInfos(false, true, false);
                            break;
                        case "24HC":
                            lstclinic = databaseHandlerClinicInfo.getAllClinicInfos(true, false, false);
                            break;
                        case "QNow":
                            lstclinic = databaseHandlerClinicInfo.getAllClinicInfos(false, false, true);
                            break;
                        default:
                            break;
                    }
                    //  ActivitySearchClinic.lstclinic = lstclinic;
                    //ActivitySearchClinic.resetposition();
                }
            }
        });
        dialog.show();

    }

    //endregion
    //region DATE ()
    public static String getcurrenttime(String format) {
        Calendar newDate = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
        String s = dateFormatter.format(newDate.getTime());
        return s;
    }

    public static  int calculate_userage(String dob){
        Calendar newDate = Calendar.getInstance();
        int year = newDate.get(Calendar.YEAR);
        int age=0;
        String inputPattern = "dd-MMM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        Date date = null;
        try{
            date = inputFormat.parse(dob);
            int dob_year = date.getYear()+1900;
            age = year - dob_year;

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return age;

    }


    public static String ChangeDateFormat(String inputdate) { // use listappointmentslot
        String inputPattern = "dd-MM-yy hh:mm a";
        String outputPattern = "yyyyMMdd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputdate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    //endregion
    //region DateFormat() // use activityeapptcarddtl,dbHQueueRequest,appointmentview
    public static int comparedate(String strdate, String currentdate, String inputPattern, String outputPattern) { // compare less than
        int flag = 0;
        try {
            Date dlog = reminderTimeFormat(strdate, inputPattern, outputPattern);
            Date cdate = reminderTimeFormat(currentdate, inputPattern, outputPattern);
            if (dlog.getHours() == 0) {
                dlog.setHours(12);
            }
            if (cdate.getHours() == 0) {
                cdate.setHours(12);
            }
            if (dlog.compareTo(cdate) < 0) {
                flag = 1;

            } else {
                flag = 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    public static int eventcomparedate(String strdate, String currentdate) {
        int flag = 0;
        try {
            Date date1 = reminderTimeFormat(strdate, "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
            Date date2 = reminderTimeFormat(currentdate, "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
            if (date1.compareTo(date2) == 0) {
                flag = 1;
            } else {
                flag = 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }


    //use activitybookcontactdetil,eappointmnetcardadapter,eapptcardetail,eventdtls,eventviewadapter,volleyappointmnetview,volleyqueueview
    public static String BookDateFormat(String inputdate, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = "";
        try {
            date = inputFormat.parse(inputdate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String GridviewChangefromat(String inputdate, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = "";
        try {
            date = inputFormat.parse(inputdate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    //activityaddreminder,activitymedicinealarm,utils
    public static Date reminderTimeFormat(String inputtime, String inputPattern, String outputPattern) {
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;
        Date date2 = null;
        try {
            date = inputFormat.parse(inputtime);
            str = outputFormat.format(date);
            date2 = outputFormat.parse(str);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return date2;
    }

    //activityaddreminder,activitytimepicker
    public static String MedicalDateFormat(Date inputdate) {
        String inputPattern = "hh:mm a";
        String str = null;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            str = inputFormat.format(inputdate);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return str;
    }
    //endregion
    //region CancelAlarm()
    public static void CancleAlarm(Context _mcont, int alarm_id) {
        AlarmManager alarmManager = (AlarmManager) _mcont.getSystemService(_mcont.ALARM_SERVICE);
        Intent myIntent = new Intent(_mcont, AlarmReceiver.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(_mcont,alarm_id, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(_mcont, alarm_id, myIntent, 0);
        alarmManager.cancel(pendingIntent);
    }
    //endregion

    //region AlertDialog ()
    public static void showAlertDialog(Context context, String title, String message) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }
    public static void showAlertDialog(final Context context, String title, String message, final String activity) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (activity.equals("ActivityHome1")) {
                    Intent intent = new Intent(context, ActivityHome1.class);
                    context.startActivity(intent);
                }
                if (activity.equals("ActivityDoctor_On_Duty")) {
                    ((Activity) context).finish();
                }
                if (activity.equals("ActivityMedicalProfileList")) {
                    Intent intent = new Intent(context, ActivityMedProfileList.class);
                    context.startActivity(intent);
                    ((Activity) context).finish();

                }
                if (activity.equals("ActivityMedicalProfile")) {
                    Intent intent = new Intent(context, ActivityMedicalProfile.class);
                    intent.putExtra("from", "list");
                    context.startActivity(intent);
                }
                if (activity.equals("ActivityPIN")) {
                    Intent intent = new Intent(context, ActivityPIN.class);
                    context.startActivity(intent);
                }
            }
        });
        alertDialog.show();
    }

    public static void showInternetRequiredDialog(final Context context, String title, String message) {
        android.app.AlertDialog.Builder alertBuilder = new android.app.AlertDialog.Builder(context);
        alertBuilder.setMessage(message)
                .setTitle(title)
                .setPositiveButton("Settings", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(Settings.ACTION_SETTINGS));
                    }
                })
                .setNegativeButton("Later", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertBuilder.create().show();
    }

    public static void getGoogleMapsDialog(final Activity activity) {
        final String url_google_map = activity.getResources().getString(R.string.url_google_map_install);
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setMessage(R.string.msg_google_map_required);
        builder.setPositiveButton("Enable/Install", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_google_map));
                activity.startActivity(intent);
                activity.finish();
            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        android.app.AlertDialog dialog = builder.create();
        dialog.show();
    }

    //region Google Drive Dialogbox()
    public static void googledriveDialogbox(final String from_activity , final Activity activity,final String login_member_id){
        final Dialog drive_dialog = new Dialog(activity);
        drive_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        drive_dialog.setContentView(R.layout.dialog_google_drive_sync);
        drive_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        Button sync_to_drive = (Button)drive_dialog.findViewById(R.id.btnuploadtodrive) ;
        Button sync_from_drive = (Button)drive_dialog.findViewById(R.id.btndownloadfromdrive) ;
        Button sync_cancel = (Button)drive_dialog.findViewById(R.id.btndrive_cancel) ;
        final DBMedProfile dbmedprofile = new DBMedProfile(activity);
        sync_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drive_dialog.dismiss();
            }
        });
        sync_to_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ArrayList<MedicalProfile> list_profile = dbmedprofile.getallMedProfile();
                ArrayList<MedicalProfile> list_profile= dbmedprofile.getMedProfilebyMemberid(login_member_id);
                Log.e("Util to drive",list_profile.size() + "");
                GDAAConnect.uploadprofileToDrive(list_profile);
                if(from_activity.equals("profilelist")){
                    ActivityMedProfileList.LoadData();
                }
                drive_dialog.dismiss();

            }
        });
        sync_from_drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GDAAConnect.downloadprofileFromDrive();
                if(from_activity.equals("profilelist")){
                    ActivityMedProfileList.LoadData();
                }
                drive_dialog.dismiss();
            }
        });
        drive_dialog.show();
    }
    //endregion

    public static String[]  arr_nationality,arr_nationalitycode;

    public static String getnationalitycode( Context context,String nationalityname){
        arr_nationality = context.getResources().getStringArray(R.array.country_code);
        arr_nationalitycode = context.getResources().getStringArray(R.array.nationality_code);
        int position = -1;
        String nationalcode = "";
        if(!nationalityname.equals("")){

            for(int i =0;i<arr_nationality.length;i++){
                if(nationalityname.equals(arr_nationality[i])){
                    position= i;
                    break;
                }
            }
        }
        if(position!=-1){
            nationalcode = arr_nationalitycode[position];
        }
        return nationalcode;
    }

    public static void showAlertDialoglogin(final Context context, String title, String message) {
        final android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context);
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        alertDialog.setPositiveButton("Login/Sign Up", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              Intent  intent = new Intent(context, ActivityLogIn.class);
                intent.putExtra("from","eDocument");
                intent.putExtra("CID",0);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
        alertDialog.show();
    }

    //endregion






}
