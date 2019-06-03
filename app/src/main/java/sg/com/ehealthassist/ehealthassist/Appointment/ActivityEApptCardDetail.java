package sg.com.ehealthassist.ehealthassist.Appointment;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestApptConfirmJson;
import sg.com.ehealthassist.ehealthassist.API.Request.RequestCancelJson;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyAppointmentCancel;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyApptConfirm;
import sg.com.ehealthassist.ehealthassist.Alarm.AlarmReceiver;
import sg.com.ehealthassist.ehealthassist.DB.DBApptAlarm;
import sg.com.ehealthassist.ehealthassist.DB.DBBookInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Appointment.ApptAlarmLog;
import sg.com.ehealthassist.ehealthassist.Models.Appointment.BookInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.Address;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Other.JGson;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Queue_Appt.ActivityQueue_Appoint;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityEApptCardDetail extends AppCompatActivity {
    Context _mcont;
    TextView appt_dtl_clinic, _appt_date_time, _appt_dr_name, _appt_address,
            _appt_remark, btnsetreminder, alarm_string, _appt_name, _appt_nric,
            txt_appt_status, main_toolbar_title, _appt_session, _patient_remark;
    ImageButton _appt_direction, toolbar_imgbutton_back;
    Button btnset, btncancel, btnappt_cancel, btn_appt_reschedule_confirm;
    ClinicInfo info = new ClinicInfo();
    BookInfo bookinfo = new BookInfo();
    ApptAlarmLog appt_log = new ApptAlarmLog();
    DBClinicInfo dbclinic = null;
    DBApptAlarm dbalarmlog = null;
    String clinic_name, clinic_address, usertoken;
    SharedPreferences pref = null;
    CheckBox chk2hour, chk1day, chk2day, chk1week;
    ArrayList<String> lst_unit;
    RelativeLayout rlremark, rladdress, rlsession_appt, rlpatient_remark;
    JGson gson = new JGson();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_eappt_card_detail);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_activity_eappt_card_detail));
        init();
        findViewbyId();
        Intent inbook = getIntent();
        bookinfo = inbook.getParcelableExtra("book");
        LoadData();
        setEvent();
    }  //region init()
    public void init() {
        pref = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        _mcont = this;
        this.dbclinic = new DBClinicInfo(_mcont);
        this.dbalarmlog = new DBApptAlarm(_mcont);
        usertoken = pref.getString(getString(R.string.pref_login_Access_token), "");
    }
    //endregion

    //region findViewbyid()
    public void findViewbyId() {
        appt_dtl_clinic = (TextView) findViewById(R.id.appt_dtl_clinic);
        _appt_date_time = (TextView) findViewById(R.id._appt_date_time);
        _appt_dr_name = (TextView) findViewById(R.id._appt_dr_name);
        _appt_address = (TextView) findViewById(R.id._appt_address);
        _appt_remark = (TextView) findViewById(R.id._appt_remark);
        alarm_string = (TextView) findViewById(R.id.alarm_string);
        btnsetreminder = (TextView) findViewById(R.id.btnsetreminder);
        btnappt_cancel = (Button) findViewById(R.id.btnappt_cancel);
        _appt_direction = (ImageButton) findViewById(R.id._appt_direction);
        _appt_name = (TextView) findViewById(R.id._appt_name);
        _appt_nric = (TextView) findViewById(R.id._appt_nric);
        txt_appt_status = (TextView) findViewById(R.id.txt_appt_status);
        btn_appt_reschedule_confirm = (Button) findViewById(R.id.btn_appt_reschedule_confirm);
        btn_appt_reschedule_confirm.setVisibility(View.INVISIBLE);
        btnappt_cancel.setVisibility(View.VISIBLE);
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        rlremark = (RelativeLayout) findViewById(R.id.rlremark);
        rladdress = (RelativeLayout) findViewById(R.id.rladdress);
        rlsession_appt = (RelativeLayout) findViewById(R.id.rlsession_appt);
        _appt_session = (TextView) findViewById(R.id._appt_session);
        rlpatient_remark = (RelativeLayout) findViewById(R.id.rlpatient_remark);
        _patient_remark = (TextView) findViewById(R.id._patient_remark);

    }
    //endregion

    //region setEvent()
    public void setEvent() {
        btnsetreminder.setOnClickListener(btnSetReminderOnClickListener);
        _appt_direction.setOnClickListener(btnLocateUsOnClickListener);
        rladdress.setOnClickListener(btnLocateUsOnClickListener);
        btnappt_cancel.setOnClickListener(btnAppointmentCancelOnClickListener);
        btn_appt_reschedule_confirm.setOnClickListener(btnAppointmentConfirmOnClickListener);
        toolbar_imgbutton_back.setOnClickListener(toolbarbackOnClickLisener);
    }

    //region toolbar back button
    View.OnClickListener toolbarbackOnClickLisener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            backpage();
        }
    };
    //endregion

    //region btnAppointmentOnClickListener
    View.OnClickListener btnAppointmentCancelOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(_mcont);
            alertDialogBuilder.setMessage(_mcont.getString(R.string.error_response_cancel_appointment));
            //  alertDialogBuilder.setTitle("Confirmation");
            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    confrimCancelAppointment();
                }
            });

            alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }
    };

    //endregion
    //region btnAppointmentConfirmOnClickListener
    View.OnClickListener btnAppointmentConfirmOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (Utils.hasInternetConnection(_mcont)) {
                    if ((pref.getBoolean(getString(R.string.pref_is_account_verified), false)) && (pref.getString(getString(R.string.pref_login_Access_token), "").length() > 0)) {

                        RequestApptConfirmJson req_confirm = new RequestApptConfirmJson(bookinfo.getLong_id(), bookinfo.getShort_id());
                        VolleyApptConfirm v_confirm = new VolleyApptConfirm(_mcont);
                        v_confirm.GetApptConfirmJson(usertoken, req_confirm.StringtoJsonObject(req_confirm.ObjecttoJson()), new VolleyApptConfirm.VolleyCallback() {
                            @Override
                            public void onSuccess(String message) {
                                DBBookInfo dbook = new DBBookInfo(_mcont);
                                dbook.deletebookById(bookinfo.getLong_id(), bookinfo.getShort_id());
                                Utils.CancleAlarm(_mcont, appt_log.getAlarm_id());
                                showdialog(1, message);
                            }
                            @Override
                            public void onFail(String eroorcode, String message) {

                            }
                        });

                    } else {
                        //Utils.showAlertDialog(_mcont, "Comfirm Fail", getString(R.string.errror_response_cancel_appointment_Fail));
                    }
                } else {
                    Utils.showInternetRequiredDialog(ActivityEApptCardDetail.this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                    return;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };
    //endregion
    //region btnLocateUsOnClickListener
    View.OnClickListener btnLocateUsOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                ApplicationInfo mapinfo = getPackageManager().getApplicationInfo(getString(R.string.google_maps_package_name), 0);
                if (!mapinfo.enabled) {
                    getGoogleMapsDialog();
                    return;
                }
                String url;
                if (info.getClinic_location().get_lat().equals("") || info.getClinic_location().get_lng().equals(""))
                    url = "geo:0,0?q=SINGAPORE+" + info.getClinic_address().getPostal();
                else {
                    clinic_name = clinic_name.replace(" ", "+");
                    url = "geo:0,0?q=" + info.getClinic_location().get_lat() + "," + info.getClinic_location().get_lng() + "(" + clinic_name + ")";
                }
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
                intent.setPackage(getString(R.string.google_maps_package_name));
                startActivity(intent);
            } catch (PackageManager.NameNotFoundException e) {
                getGoogleMapsDialog();
            }
        }
    };
    //endregion
    //region btnSetReminderOnClickListener
    View.OnClickListener btnSetReminderOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final Dialog dialog = new Dialog((_mcont));
            dialog.setTitle("Set Appointment Reminder");
            dialog.setContentView(R.layout.dialogapptreminder);
            btnset = (Button) dialog.findViewById(R.id.btnnoset);
            btncancel = (Button) dialog.findViewById(R.id.btnnocancel);
            chk2hour = (CheckBox) dialog.findViewById(R.id.chk_2hours);
            chk1day = (CheckBox) dialog.findViewById(R.id.chk_1day);
            chk2day = (CheckBox) dialog.findViewById(R.id.chk_2day);
            chk1week = (CheckBox) dialog.findViewById(R.id.chk_1week);

            if (settimeenable("1w")) {
                chk1week.setEnabled(true);
            } else {
                chk1week.setEnabled(false);
            }
            if (settimeenable("2d")) {
                chk2day.setEnabled(true);
            } else {
                chk2day.setEnabled(false);
            }
            if (settimeenable("1d")) {
                chk1day.setEnabled(true);
            } else {
                chk1day.setEnabled(false);
            }
            appt_log = dbalarmlog.getalarmlogInfo(bookinfo.getLong_id(), bookinfo.getShort_id());

            if (!appt_log.getLong_book_id().equals("")) {// if exist alarm,check alarm time
                setcheckbox();
            }

            btncancel.setOnClickListener(new View.OnClickListener() { // delete existing alarm  and then set default alarm(2hr)
                @Override
                public void onClick(View v) {
                    //  setAlarm(1,"2h");
                    cancelalarm();

                    dialog.dismiss();
                }
            });
            btnset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setappointmenttime();
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    };

    //endregion

    public boolean settimeenable(String time) {
        boolean falg = false;
        String bookdate = bookinfo.getBook_date();
        String currentdate = Utils.getcurrenttime("yyyy-MM-dd'T'HH:mm:ss");
        Date bkdate = Utils.reminderTimeFormat(bookdate, "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss");
        Date curdate = Utils.reminderTimeFormat(currentdate, "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(bkdate);

        if (time.equals("1w")) {
            cal.add(Calendar.DATE, -7);
            Date datebefore7 = cal.getTime();

            if (datebefore7.compareTo(curdate) > 0) {
                falg = true;
            }

        } else if (time.equals("2d")) {
            cal.add(Calendar.DATE, -2);
            Date datebefore2 = cal.getTime();

            if (datebefore2.compareTo(curdate) > 0) {
                falg = true;
            }
        } else if (time.equals("1d")) {
            cal.add(Calendar.DATE, -1);
            Date datebefore7 = cal.getTime();

            if (datebefore7.compareTo(curdate) > 0) {
                falg = true;
            }
        }
        return falg;
    }
    //endregion

    //region loaddata()
    public void LoadData() {
        info = dbclinic.getClinicInfo(bookinfo.getClinic_id());
        clinic_name = bookinfo.clinic_name;
        clinic_address = getAddress();
        if (bookinfo.getDoctor_name().equals("")) {
            _appt_dr_name.setText("Any Doctor");
        } else {
            _appt_dr_name.setText("Dr. " + bookinfo.getDoctor_name());
        }

        appt_dtl_clinic.setText(clinic_name);
        _appt_address.setText(clinic_address);
        _appt_remark.setText(bookinfo.getDoc_remark());
        _patient_remark.setText(bookinfo.getPatientremark());
        _appt_name.setText(bookinfo.getRequestor_name());
        _appt_nric.setText(bookinfo.getRequestor_nric());
        String session = bookinfo.getApptsession() + " " + bookinfo.getApptsessiontime();
        _appt_session.setText(session);
        txt_appt_status.setText(bookinfo.getApp_status());

        if (bookinfo.getDoc_remark().equals("")) {
            rlremark.setVisibility(View.GONE);
        } else {
            rlremark.setVisibility(View.VISIBLE);
        }
        if (bookinfo.getPatientremark().equals("")) {
            rlpatient_remark.setVisibility(View.GONE);
        } else {
            rlpatient_remark.setVisibility(View.VISIBLE);
        }

        if (bookinfo.getApp_status().equals("Pending")) {
            txt_appt_status.setTextColor(_mcont.getResources().getColor(R.color.cas_color_tab_yellow));
            btn_appt_reschedule_confirm.setVisibility(View.INVISIBLE);
            btnsetreminder.setVisibility(View.GONE);
            alarm_string.setVisibility(View.GONE);
            _appt_date_time.setText(Utils.BookDateFormat(bookinfo.getBook_date(), "yyyy-MM-dd'T'HH:mm:ss", "EEE, dd-MMM-yyyy"));
            rlsession_appt.setVisibility(View.VISIBLE);
        } else if (bookinfo.getApp_status().equals("Accepted")) {
            txt_appt_status.setTextColor(_mcont.getResources().getColor(R.color.cas_success_green));
            btn_appt_reschedule_confirm.setVisibility(View.INVISIBLE);
            btnappt_cancel.setVisibility(View.GONE);
            String currentdate = Utils.getcurrenttime("dd-MMM-yyyy hh:mm aa");
            String bookdate = Utils.BookDateFormat(bookinfo.getBook_date(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy hh:mm aa");

            if (Utils.comparedate(bookdate, currentdate, "dd-MMM-yyyy hh:mm aa", "dd-MMM-yyyy hh:mm aa") < 1) {
                btnsetreminder.setVisibility(View.VISIBLE);
                alarm_string.setVisibility(View.VISIBLE);
            } else {
                btnsetreminder.setVisibility(View.GONE);
                alarm_string.setVisibility(View.GONE);
            }
            _appt_date_time.setText(Utils.BookDateFormat(bookinfo.getBook_date(), "yyyy-MM-dd'T'HH:mm:ss", "EEE, dd-MMM-yyyy hh:mm aa"));
            rlsession_appt.setVisibility(View.GONE);

        } else if (bookinfo.getApp_status().equals("Confirmed")) {
            txt_appt_status.setTextColor(_mcont.getResources().getColor(R.color.cas_success_green));
            btn_appt_reschedule_confirm.setVisibility(View.INVISIBLE);
            btnsetreminder.setVisibility(View.GONE);
            alarm_string.setVisibility(View.GONE);
        } else if (bookinfo.getApp_status().equals("Rescheduled")) {
            txt_appt_status.setTextColor(_mcont.getResources().getColor(R.color.pink_accent_900));
            btnsetreminder.setVisibility(View.GONE);
            alarm_string.setVisibility(View.GONE);
            if (comparedate() > 0) {
                btn_appt_reschedule_confirm.setVisibility(View.VISIBLE);
            } else {
                btn_appt_reschedule_confirm.setVisibility(View.INVISIBLE);
            }
        } else //if(bookinfo.getApp_status().equals("Cancelled"))
        {
            txt_appt_status.setTextColor(_mcont.getResources().getColor(R.color.colorred));
            btn_appt_reschedule_confirm.setVisibility(View.INVISIBLE);
            btnappt_cancel.setVisibility(View.GONE);
            btnsetreminder.setVisibility(View.GONE);
            alarm_string.setVisibility(View.GONE);
            _appt_date_time.setText(Utils.BookDateFormat(bookinfo.getBook_date(), "yyyy-MM-dd'T'HH:mm:ss", "EEE, dd-MMM-yyyy"));
            rlsession_appt.setVisibility(View.VISIBLE);
        }

        LoadAppt_Log();
    }

    public void LoadAppt_Log() {
        appt_log = dbalarmlog.getalarmlogInfo(bookinfo.getLong_id(), bookinfo.getShort_id());

        if (!appt_log.getLong_book_id().equals("")) {
            loadAlarmString(appt_log);
        }
    }

    public int comparedate() {
        String bookdate = bookinfo.getBook_date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date firstdate = null;
        Date seconddate = null;
        Calendar cal = Calendar.getInstance();
        int flag = 0;
        try {
            firstdate = sdf.parse(bookdate);
            //seconddate = sdf.parse(cal.getTime().toString());
            if (firstdate.compareTo(cal.getTime()) > 0) {
                flag = 1;
            } else {
                flag = 0;

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return flag;
    }

    public void loadAlarmString(ApptAlarmLog alarm) {
        String al_str = "";
        if (alarm.is_2hr()) {
            al_str += "2 hour" + ",";
        }
        if (alarm.is_1day()) {
            al_str += "1 day" + ",";
        }
        if (alarm.is_2days()) {
            al_str += "2 days" + ",";
        }
        if (alarm.is_1week()) {
            al_str += "1 week" + ",";
        }
        if (al_str.length() > 0) {
            al_str = al_str.substring(0, al_str.length() - 1);
        }
        alarm_string.setText(al_str);
    }

    public String getAddress() { // load address
        Address address = info.getClinic_address();
        String str_address = address.getBlockno() + " " +
                address.getUnitno() + " " +
                address.getStreet() + " " +
                "S(" + address.getPostal() + ")";
        return str_address;
    }
    //endregion

    //region GoogleMapDialog()
    private void getGoogleMapsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityEApptCardDetail.this);
        builder.setMessage(R.string.msg_google_map_required);
        builder.setPositiveButton("Enable/Install", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.url_google_map_install)));
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //endregion

    //region CancelAppointment()
    public void confrimCancelAppointment() {
        try {
            if (Utils.hasInternetConnection(_mcont)) {
                if ((pref.getBoolean(getString(R.string.pref_is_account_verified), false)) && (pref.getString(getString(R.string.pref_login_Access_token), "").length() > 0)) {

                    RequestCancelJson req_cancel = new RequestCancelJson(bookinfo.getLong_id(), bookinfo.getShort_id());
                    VolleyAppointmentCancel v_cancel = new VolleyAppointmentCancel(_mcont);
                    v_cancel.GetApptCancelJson(usertoken, req_cancel.StringtoJsonObject(req_cancel.ObjecttoJson()), new VolleyAppointmentCancel.VolleyCallback() {
                        @Override
                        public void onSuccess(String message) {
                            DBBookInfo dbook = new DBBookInfo(_mcont);
                            dbook.deletebookById(bookinfo.getLong_id(), bookinfo.short_id);
                            // Utils.CancleAlarm(_mcont,appt_log.getAlarm_id());
                            cancelalarm();
                            showdialog(1, message);
                        }

                        @Override
                        public void onFail(String eroorcode, String message) {
                            showdialog(0, message);
                        }
                    });

                } else {
                    Utils.showAlertDialog(_mcont, "Cancel Fail", getString(R.string.errror_response_cancel_appointment_Fail));
                }
            } else {
                Utils.showInternetRequiredDialog(ActivityEApptCardDetail.this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                return;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void cancelappointment() {
        String chk_str = "";
        dbalarmlog.deleteAlarmbyid(bookinfo.getLong_id(), bookinfo.getShort_id());
        alarm_string.setText(chk_str);
    }

    public void showdialog(final int result, String message) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(_mcont);
        alertDialog.setTitle("Appointment");
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (result) {
                    case 0:
                        dialog.dismiss();
                        break;
                    case 1:
                        // Intent i = getIntent();
                        //setResult(1, i);
                        dialog.dismiss();
                        backpage();
                        break;
                    default:
                        break;
                }
            }
        });
        alertDialog.show();
    }
    // endregion

    //region Alarm()
    public void setcheckbox() {
        if (appt_log.is_2hr()) {
            chk2hour.setChecked(true);
        }
        if (appt_log.is_1day()) {
            chk1day.setChecked(true);
        }
        if (appt_log.is_2days()) {
            chk2day.setChecked(true);
        }
        if (appt_log.is_1week()) {
            chk1week.setChecked(true);
        }
    }

    public void setAlarm(int result, String time) {
        int i = 0;
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            date = format.parse(bookinfo.getBook_date());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        Intent myIntent = new Intent(ActivityEApptCardDetail.this, AlarmReceiver.class);
        myIntent.putExtra("long_book_id", bookinfo.getLong_id());
        myIntent.putExtra("short_book_id", bookinfo.getShort_id());
        myIntent.putExtra("uniqueInt", uniqueInt);
        myIntent.putExtra("settime", time);
        lst_unit.add(uniqueInt + "");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ActivityEApptCardDetail.this, uniqueInt, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (result > 0) {
            switch (time) {
                case "2h":
                    i = 2 * 60 * 60 * 1000;
                    break;
                case "1d":
                    i = 24 * 60 * 60 * 1000;
                    break;
                case "2d":
                    i = 2 * 24 * 60 * 60 * 1000;
                    break;
                case "1w":
                    i = 7 * 24 * 60 * 60 * 1000;
                    break;
                default:
                    //  i = 10 * 60 * 1000;
                    break;

            }
            alarmManager.set(AlarmManager.RTC, date.getTime() - i, pendingIntent);
        } else {
            pendingIntent.cancel();
            alarmManager.cancel(pendingIntent);
        }
        // update alarm Appt table
    }

    public void setappointmenttime() {
        String chk_str = "";
        lst_unit = new ArrayList<String>();
        ApptAlarmLog new_log = new ApptAlarmLog();
        new_log.setLong_book_id(bookinfo.getLong_id());
        if (chk2hour.isChecked()) {
            new_log.set_2hr(true);
            chk_str += "2 hour" + ",";
            setAlarm(1, "2h");
        }
        if (chk1day.isChecked()) {
            new_log.set_1day(true);
            chk_str += "1 day" + ",";
            setAlarm(1, "1d");
        }
        if (chk2day.isChecked()) {
            new_log.set_2days(true);
            chk_str += "2 days" + ",";
            setAlarm(1, "2d");
        }
        if (chk1week.isChecked()) {
            new_log.set_1week(true);
            chk_str += "1 week" + ",";
            setAlarm(1, "1w");
        }
        new_log.setAlarm_id(bookinfo.getId());
        new_log.setShort_book_id(bookinfo.getShort_id());

        String unit_string = gson.toAlarmJson(lst_unit);
        new_log.setAlarm_unit(unit_string);

        long i = dbalarmlog.updateAlarmInfoBylongid(new_log);

        if (chk_str.length() > 0) {
            chk_str = chk_str.substring(0, chk_str.length() - 1);
        }
        alarm_string.setText(chk_str);
    }

    public void cancelalarm() {
        // only update in db alarmlog table
        String unit_alarm = appt_log.getAlarm_unit();
        if (!unit_alarm.equals("")) {
            ArrayList<String> arry_unit_alarm = new ArrayList<String>();
            arry_unit_alarm = gson.fromalarmjson(unit_alarm);

            if (arry_unit_alarm.size() > 0) {
                for (int i = 0; i < arry_unit_alarm.size(); i++) {

                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    Intent myIntent = new Intent(this, AlarmReceiver.class);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(this, Integer.parseInt(arry_unit_alarm.get(i)), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    alarmManager.cancel(pendingIntent);
                }
            }
            dbalarmlog.deleteAlarmbyid(appt_log.getLong_book_id(), appt_log.getShort_book_id());
            alarm_string.setText("");
        }
     /*   ApptAlarmLog new_log = new ApptAlarmLog();
        new_log.setLong_book_id(bookinfo.getLong_id());
        new_log.setAlarm_id(bookinfo.getId());// autoincrement bookid
        new_log.setShort_book_id(bookinfo.getShort_id());
        new_log.set_2hr(false);
        new_log.set_2days(false);
        new_log.set_1day(false);
        new_log.set_1week(false);

        long i = dbalarmlog.updateAlarmInfoBylongid(new_log);*/
    }

    //endregion

    //region menu()
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_eappt_card_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                supportFinishAfterTransition();
               *//* if(ActivityQueue_Appoint.activity_detect){
                    finish();
                }
                else{*//*
                    *//*Intent intent   = new Intent(getApplicationContext(),ActivityQueue_Appoint.class);
                    intent.putExtra("from","ActivityEApptCardDetail");
                    startActivity(intent);
                    finish();*//*

               // }
                backpage();

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       /* if(ActivityQueue_Appoint.activity_detect){
            finish();
        }
        else{*/
          /*  Intent intent   = new Intent(getApplicationContext(),ActivityQueue_Appoint.class);
            intent.putExtra("from","ActivityEApptCardDetail");
            startActivity(intent);
            finish();*/
        //  }

        backpage();
    }


    public void backpage() {
        Intent intent = new Intent(getApplicationContext(), ActivityQueue_Appoint.class);
        intent.putExtra("from", "ActivityEApptCardDetail");
        startActivity(intent);
        finish();
    }
    //endregion
}
