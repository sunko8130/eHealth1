package sg.com.ehealthassist.ehealthassist.Appointment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestBookJson;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyBookApptSlot;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyListAppointmentSlot;
import sg.com.ehealthassist.ehealthassist.CustomUI.CustomGridView;
import sg.com.ehealthassist.ehealthassist.CustomUI.GridAdapter;
import sg.com.ehealthassist.ehealthassist.DB.DBMedDispense;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Doctors.DoctorAdapter;
import sg.com.ehealthassist.ehealthassist.Models.Appointment.AppointmentSlot;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Models.Doctor.Doctors;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityBookAppt extends AppCompatActivity {
    SharedPreferences pref = null;
    Context _mcontext;
    ImageButton toolbar_imgbutton_back, imgbook_close;
    TextView main_toolbar_title, txt_clinicname_value, txtappt_booksession_value, txtappt_doctor_name_value, txtprofilename_value,
            txt_dialog_title, txtage_value, txtdate_value, txtnric_value, txtnrictype_value, txtappt_bookdate, txtnodata_value,
            txtslotnodata_value, txt_apptbooktime_label;
    ListView lvdialogAppt;
    ArrayList<MedicalProfile> med_profile = new ArrayList<MedicalProfile>();
    DBMedProfile dbmedicalprofile = null;
    BookProfileAdapter profileadapt;
    DoctorAdapter doctoradapt;
    ApptSessionAdapter apptsessionadapt;
    ScrollView scroll_bookappt;
    ArrayList<Doctors> doctor_lst;
    List<String> lsttempsession, lsttempslot;
    EditText editext_patientRemark;
    String selected_date, clinic_name, doctor_name = "", appt_mode = "";
    Button btn_bookappt_confirm;
    int clinicid, nric_type, doctorid, countrycode = 0;
    String memberid, email, usertoken, nricname, mobilephno, session,
            countryisocode = "";
    ArrayList<AppointmentSlot> apptListslot = new ArrayList<AppointmentSlot>();
    ArrayList<AppointmentSlot> slotlsit = new ArrayList<AppointmentSlot>();

    ImageButton img_session, imgappt_doctor_name, imgprofileinfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_book_appt);
        init();
        findviewbyid();
        setEvent();
        LoadData();
    }
    public void init() {
        pref = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        _mcontext = this;
        this.dbmedicalprofile = new DBMedProfile(_mcontext);
        Intent get_apptslot = getIntent();
        clinicid = get_apptslot.getIntExtra("CID", 0);
        selected_date = get_apptslot.getStringExtra("selecteddate");
        doctor_name = get_apptslot.getStringExtra("doctorname");
        doctorid = get_apptslot.getIntExtra("doctorid", 0);
        clinic_name = get_apptslot.getStringExtra("CName");
        appt_mode = get_apptslot.getStringExtra("appt_mode");
        apptListslot = ActivityAppointment.lstapptsession;
        usertoken = pref.getString(getString(R.string.pref_login_Access_token), "");
        email = pref.getString(getString(R.string.pref_main_user_data_email), "");
        memberid = pref.getString(getString(R.string.pref_login_memberId), "");
        if (appt_mode.equals("B")) {
            getappointmentslot();
        }
    }

    public void findviewbyid() {
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_appointment_page));
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        txt_clinicname_value = (TextView) findViewById(R.id.txt_clinicname_value);
        txtappt_bookdate = (TextView) findViewById(R.id.txtappt_bookdate);
        txtappt_booksession_value = (TextView) findViewById(R.id.txtappt_booksession_value);
        txtappt_doctor_name_value = (TextView) findViewById(R.id.txtappt_doctor_name_value);
        txtprofilename_value = (TextView) findViewById(R.id.txtprofilename_value);
        txt_apptbooktime_label = (TextView) findViewById(R.id.txt_apptbooktime_value);
        txtage_value = (TextView) findViewById(R.id.txtage_value);
        txtdate_value = (TextView) findViewById(R.id.txtdate_value);
        txtnric_value = (TextView) findViewById(R.id.txtnric_value);
        txtnrictype_value = (TextView) findViewById(R.id.txtnrictype_value);
        scroll_bookappt = (ScrollView) findViewById(R.id.scroll_bookappt);
        btn_bookappt_confirm = (Button) findViewById(R.id.btn_bookappt_confirm);
        editext_patientRemark = (EditText) findViewById(R.id.editext_patientRemark);
        img_session = (ImageButton) findViewById(R.id.img_session);
        imgappt_doctor_name = (ImageButton) findViewById(R.id.imgappt_doctor_name);
        imgprofileinfo = (ImageButton) findViewById(R.id.imgprofileinfo);
    }

    public void LoadData() {
        txt_clinicname_value.setText(clinic_name);
        String changefmt_selectedDate = Utils.BookDateFormat(selected_date, "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
        txtappt_bookdate.setText(changefmt_selectedDate);
        txtappt_doctor_name_value.setText(doctor_name);
        if (appt_mode.equals("B")) {
            txt_apptbooktime_label.setText("Slot ");
        } else {
            txt_apptbooktime_label.setText("Session ");
        }
    }

    public void setEvent() {
        txtprofilename_value.setOnClickListener(rlprofileOnClickListener);
        txtappt_doctor_name_value.setOnClickListener(rldoctorOnClickListener);
        txtappt_booksession_value.setOnClickListener(rlsessionOnClickListener);
        img_session.setOnClickListener(rlsessionOnClickListener);
        imgappt_doctor_name.setOnClickListener(rldoctorOnClickListener);
        imgprofileinfo.setOnClickListener(rlprofileOnClickListener);
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_bookappt_confirm.setOnClickListener(btnAppointmentOnClickListener);
    }

    View.OnClickListener rlprofileOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogprofilelist();
        }
    };
    View.OnClickListener rlsessionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (appt_mode.equals("B")) {
                dialogslot();
            } else {
                dialogSessionList();
            }
        }
    };
    View.OnClickListener rldoctorOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dialogDoctorList();
        }
    };
    View.OnClickListener btnAppointmentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (!checkvalidField()) {
                    if (Utils.hasInternetConnection(_mcontext)) {
                        if ((pref.getBoolean(getString(R.string.pref_is_account_verified), false)) &&
                                (usertoken.length() > 0)) {

                            String nric = txtnric_value.getText().toString();
                            String patientremark = editext_patientRemark.getText().toString();

                            RequestBookJson requestslot = new RequestBookJson(memberid, clinicid, doctorid, selected_date, nric, nricname, email,
                                    countrycode + mobilephno, nric_type + "", session, "", patientremark, countryisocode);
                            Log.e("time", session);
                            String book_request = requestslot.ObjecttoJson();

                            VolleyBookApptSlot v_book = new VolleyBookApptSlot(_mcontext);
                            v_book.GetListApptSlotJson(usertoken, requestslot.StringtoJsonObject(requestslot.ObjecttoJson()), new VolleyBookApptSlot.VolleyCallback() {
                                @Override
                                public void onSuccess(String bookid, String longbookid, String status, String statusdesc) {
                                    Intent intent = new Intent(_mcontext, ActivityDialogAppointment.class);
                                    intent.putExtra("result", 1);
                                    intent.putExtra("message", bookid);
                                    intent.putExtra("returnmessage", status);
                                    intent.putExtra("returnmesdesc", statusdesc);
                                    startActivity(intent);
                                }

                                @Override
                                public void onFail(String errorcode, String errormsg) {
                                    Intent intent = new Intent(_mcontext, ActivityDialogAppointment.class);
                                    intent.putExtra("result", 0);
                                    intent.putExtra("message", errormsg);
                                    intent.putExtra("returnmessage", "");
                                    intent.putExtra("returnmesdesc", "");
                                    startActivity(intent);
                                }
                            });

                        } else {
                            Utils.showAlertDialog(_mcontext, "Booking Fail", getString(R.string.error_response_upload_profile_status_InvalidUserToken));
                        }

                    } else {
                        Utils.showInternetRequiredDialog(_mcontext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                        return;
                    }
                } else {
                    Utils.showAlertDialog(_mcontext, "Appointment Requested", "Please select  Session , Doctor and Profile for Appointment Request");
                    return;
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    };

    //region dialog profile
    public void dialogprofilelist() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bookprofile);
        txt_dialog_title = (TextView) dialog.findViewById(R.id.tool);
        txt_dialog_title.setText("Profile list");
        lvdialogAppt = (ListView) dialog.findViewById(R.id.lvbookappt);
        imgbook_close = (ImageButton) dialog.findViewById(R.id.img_close);
        LoadProfileAdapter();

        imgbook_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        lvdialogAppt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtprofilename_value.setTextColor(getResources().getColor(R.color.blue_primary_900));
                MedicalProfile select_object = med_profile.get(position);
                txtnric_value.setText(select_object.getNric());
                txtdate_value.setText(select_object.getDob());
                txtage_value.setText(Utils.calculate_userage(select_object.getDob()) + "");
                txtprofilename_value.setText(select_object.getNric_name());
                nricname = select_object.getNric_name();
                email = select_object.getEmail();
                mobilephno = select_object.getTel1();
                countrycode = select_object.getTel1_code();
                countryisocode = select_object.getTel1_iso();
                Log.e("country", countryisocode + "," + countrycode);
                nric_type = select_object.getNric_type();
                if (nric_type == 0) {
                    txtnrictype_value.setText("OTHER");
                } else if (nric_type == 1) {
                    txtnrictype_value.setText("PINK");
                } else if (nric_type == 2) {
                    txtnrictype_value.setText("BLUE");
                } else if (nric_type == 3) {
                    txtnrictype_value.setText("FIN");
                }
                scroll_bookappt.scrollTo(0, scroll_bookappt.getBottom());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void LoadProfileAdapter() {
        med_profile = dbmedicalprofile.getMedProfilebyMemberid(pref.getString(getString(R.string.pref_login_memberId), ""));
        profileadapt = new BookProfileAdapter(_mcontext, med_profile);
        profileadapt.notifyDataSetChanged();
        lvdialogAppt.setAdapter(profileadapt);
    }
    //endregion

    //region dialog Doctor
    public void dialogDoctorList() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bookprofile);
        txt_dialog_title = (TextView) dialog.findViewById(R.id.tool);
        txt_dialog_title.setText("Doctor list");
        lvdialogAppt = (ListView) dialog.findViewById(R.id.lvbookappt);
        imgbook_close = (ImageButton) dialog.findViewById(R.id.img_close);
        if (appt_mode.equals("B")) {
            LoadSlotDoctorAdapter();
        } else {
            LoadDoctorAdapter();
        }
        imgbook_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        lvdialogAppt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtappt_doctor_name_value.setTextColor(getResources().getColor(R.color.blue_primary_900));
                txtappt_doctor_name_value.setText(doctor_lst.get(position).getDr_name());
                doctorid = doctor_lst.get(position).getDr_id();
                String selected_session = txtappt_booksession_value.getText().toString();
                List<String> availablesession = doctor_lst.get(position).getDr_session();

                if (!availablesession.contains(selected_session)) {
                    txtappt_booksession_value.setText("None");
                }

                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void LoadDoctorAdapter() {
        doctoradapt = new DoctorAdapter(_mcontext, getDoctList());
        doctoradapt.notifyDataSetChanged();
        lvdialogAppt.setAdapter(doctoradapt);
    }

    public void LoadSlotDoctorAdapter() {
        doctoradapt = new DoctorAdapter(_mcontext, getSlotDoctorList());
        doctoradapt.notifyDataSetChanged();
        lvdialogAppt.setAdapter(doctoradapt);
    }

    public ArrayList<Doctors> getDoctList() {
        doctor_lst = new ArrayList<Doctors>();
        if (apptListslot.size() > 0) {
            Log.e("slot size", apptListslot.size() + "");
            for (AppointmentSlot getdoctor : apptListslot) {
                if (getdoctor.getAvailabeleSlots().contains(selected_date)) {
                    Doctors obj_doctors = new Doctors();
                    obj_doctors.setDr_id(getdoctor.getId());
                    obj_doctors.setDr_name(getdoctor.getDisplayname());
                    obj_doctors.setDr_session(getdoctor.getApptSession());
                    doctor_lst.add(obj_doctors);
                }
            }
        }
        Log.e("latest", apptListslot.size() + "");
        return doctor_lst;
    }

    public ArrayList<Doctors> getSlotDoctorList() {
        doctor_lst = new ArrayList<Doctors>();
        if (slotlsit.size() > 0) {
            for (AppointmentSlot getdoctor : slotlsit) {
                Doctors obj_doctor = new Doctors();
                obj_doctor.setDr_id(getdoctor.getId());
                obj_doctor.setDr_name(getdoctor.getDisplayname());
                obj_doctor.setDr_session(getdoctor.getApptSession());
                doctor_lst.add(obj_doctor);
            }
        }
        return doctor_lst;
    }

    //endregion

    //region dialog session
    public void dialogSessionList() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_bookprofile);
        txt_dialog_title = (TextView) dialog.findViewById(R.id.tool);
        txtnodata_value = (TextView) dialog.findViewById(R.id.txtnodata_value);
        txt_dialog_title.setText("Appointment Session");
        lvdialogAppt = (ListView) dialog.findViewById(R.id.lvbookappt);
        imgbook_close = (ImageButton) dialog.findViewById(R.id.img_close);

        LoadSessionAdapter();

        imgbook_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        lvdialogAppt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtappt_booksession_value.setTextColor(getResources().getColor(R.color.blue_primary_900));
                String selectedlst_session = lsttempsession.get(position);
                txtappt_booksession_value.setText(selectedlst_session);
                //  String[] cutsession = selectedlst_session.split(" ");
                // session = cutsession[0];
                session = selectedlst_session;
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void LoadSessionAdapter() {
        ArrayList<Doctors> session_lst = getDoctList();
        String doctorname = txtappt_doctor_name_value.getText().toString();
        lsttempsession = new ArrayList<String>();

        if (doctorname.equals("None")) {
            int temp = 0;
            for (int i = 0; i < session_lst.size(); i++) {
                if (i != session_lst.size()) {
                    int first = session_lst.get(i).getDr_session().size();
                    int second = session_lst.get(i++).getDr_session().size();
                    if (first < second) {
                        temp = i++;
                    } else {
                        temp = i;
                    }
                }
            }
            lsttempsession.addAll(session_lst.get(temp).getDr_session());
        } else {
            for (Doctors getdoctor : session_lst) {
                if (getdoctor.getDr_name().equals(doctorname)) {
                    lsttempsession.addAll(getdoctor.getDr_session());
                }
            }
        }
        if (lsttempsession.size() > 0) {
            txtnodata_value.setVisibility(View.GONE);
        } else {
            txtnodata_value.setText(_mcontext.getResources().getString(R.string.msgsessiondata));
            txtnodata_value.setVisibility(View.VISIBLE);
        }
        apptsessionadapt = new ApptSessionAdapter(_mcontext, lsttempsession);
        apptsessionadapt.notifyDataSetChanged();
        lvdialogAppt.setAdapter(apptsessionadapt);
    }
    //endregion

    //region dialog slot
    public void getappointmentslot() {
        if (Utils.hasInternetConnection(_mcontext)) {
            String changefmt_selectedDate = Utils.BookDateFormat(selected_date, "yyyy-MM-dd'T'HH:mm:ss", "yyyyMMdd");
            VolleyListAppointmentSlot v_listslot = new VolleyListAppointmentSlot(_mcontext);

            v_listslot.GetListApptSlotJson(Constant.URL_LISTAppointmentSlot, clinicid, changefmt_selectedDate, usertoken, new VolleyListAppointmentSlot.VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<AppointmentSlot> result) {
                    slotlsit = result;
                }

                @Override
                public void onFail(String errorcode, String errormsg) {
                    Utils.showAlertDialog(_mcontext, errorcode, errormsg);
                }
            });

        } else {
            Utils.showInternetRequiredDialog(_mcontext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
    }

    CustomGridView mgridslot;
    public static GridAdapter gridAdapt;

    public void dialogslot() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_listslot);
        txtslotnodata_value = (TextView) dialog.findViewById(R.id.txtnoslot_value);
        imgbook_close = (ImageButton) dialog.findViewById(R.id.img_close);
        mgridslot = (CustomGridView) dialog.findViewById(R.id.GridView_toolbar);
        mgridslot.setNumColumns(3);
        mgridslot.setHorizontalSpacing(10);
        LoadSlotAdapter();
        imgbook_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        mgridslot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtappt_booksession_value.setTextColor(getResources().getColor(R.color.blue_primary_900));
                String btime = lsttempslot.get(position);
                String slot_time = Utils.BookDateFormat(btime, "yyyy-MM-dd'T'HH:mm:ss", "HH:mm a");
                txtappt_booksession_value.setText(slot_time);
                selected_date = btime;
                session = slot_time;
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void LoadSlotAdapter() {
        String doctorname = txtappt_doctor_name_value.getText().toString();
        lsttempslot = new ArrayList<String>();

        if (doctorname.equals("Any Doctor")) {
            for (int i = 0; i < slotlsit.size(); i++) {
                int count = slotlsit.get(i).getAvailabeleSlots().size();
                for (int y = 0; y < count; y++) {
                    String available_slot = slotlsit.get(i).getAvailabeleSlots().get(y);
                    //  String slot_time = Utils.BookDateFormat(available_slot, "yyyy-MM-dd'T'HH:mm:ss", "HH:mm a");
                    if (!lsttempslot.contains(available_slot)) {
                        lsttempslot.add(available_slot);
                    }
                }
            }
        } else {
            for (int i = 0; i < slotlsit.size(); i++) {
                String doc_name = slotlsit.get(i).getDisplayname();
                if (doctorname.equals(doc_name)) {
                    int count = slotlsit.get(i).getAvailabeleSlots().size();
                    Log.e("availableslot count", count + "");
                    for (int y = 0; y < count; y++) {
                        String available_slot = slotlsit.get(i).getAvailabeleSlots().get(y);
                        // String slot_time = Utils.BookDateFormat(available_slot, "yyyy-MM-dd'T'HH:mm:ss", "HH:mm a");
                        if (!lsttempslot.contains(available_slot)) {
                            lsttempslot.add(available_slot);
                        }
                    }

                    break;
                }
            }
        }
        if (lsttempslot.size() > 0) {
            txtslotnodata_value.setVisibility(View.GONE);
        } else {
            txtslotnodata_value.setText(_mcontext.getResources().getString(R.string.msgsessiondata));
            txtslotnodata_value.setVisibility(View.VISIBLE);
        }
        gridAdapt = new GridAdapter(_mcontext, lsttempslot);
        gridAdapt.notifyDataSetChanged();
        mgridslot.setAdapter(gridAdapt);
        int totalHeight = -15;
        for (int size = 0; size < gridAdapt.getCount(); size++) {
            RelativeLayout relativeLayout = (RelativeLayout) gridAdapt.getView(
                    size, null, mgridslot);
            TextView textView = (TextView) relativeLayout.getChildAt(0);
            textView.measure(0, 0);
            totalHeight += textView.getMeasuredHeight();
        }
        mgridslot.SetHeight(totalHeight);
    }
    //endregion

    //region validField
    public boolean checkvalidField() {
        boolean haserror = false;
        String session_value = txtappt_booksession_value.getText().toString();
        String doctor_value = txtappt_doctor_name_value.getText().toString();
        String profile_value = txtprofilename_value.getText().toString();
        String nric_value = txtnric_value.getText().toString();
        String nric_type = txtnrictype_value.getText().toString();

        if (session_value.equals("None")) {
            txtappt_booksession_value.setTextColor(getResources().getColor(R.color.colorred));
            haserror = true;
        } else {
            txtappt_booksession_value.setTextColor(getResources().getColor(R.color.blue_primary_900));
        }
        if (doctor_value.equals("None")) {
            txtappt_doctor_name_value.setTextColor(getResources().getColor(R.color.colorred));
            haserror = true;
        } else {
            txtappt_doctor_name_value.setTextColor(getResources().getColor(R.color.blue_primary_900));
        }
        if (profile_value.equals("None")) {
            txtprofilename_value.setTextColor(getResources().getColor(R.color.colorred));
            haserror = true;
        } else {
            txtprofilename_value.setTextColor(getResources().getColor(R.color.blue_primary_900));
        }
        if (nric_value.equals("---")) {
            haserror = true;
        }
        if (nric_type.equals("---")) {
            haserror = true;
        }
        return haserror;
    }
    //endregion

}
