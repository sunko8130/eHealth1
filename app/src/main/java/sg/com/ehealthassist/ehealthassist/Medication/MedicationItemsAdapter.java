package sg.com.ehealthassist.ehealthassist.Medication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import sg.com.ehealthassist.ehealthassist.CustomUI.CustomListView;

import sg.com.ehealthassist.ehealthassist.DB.DBMedicalItem;
import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalItem;
import sg.com.ehealthassist.ehealthassist.Models.Medical.WeekSlot;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 2/7/17.
 */

public class MedicationItemsAdapter extends BaseAdapter {
    private Context _mcont;
    private List<MedicalItem> meditemslist;
    String[] arrfreqcode, arrweekslotcode;
    ListSlotTimeAdapter lstslotadapter;
    WeekDayTimeAdapter lstweekadapter;
    SimpleDateFormat simple24TimeFormat = new SimpleDateFormat("HH:mm", Locale.US);
    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("hh:mm a", Locale.US);
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
    TextView txtstartdate_value, txtenddate_value, txt_medicine_name, txt_medicine_freqname,
            week_timeslot, week_Take_tap;
    Button btn_cancel, btn_save;
    String format_time = "";
    ArrayList<String> slotlist = new ArrayList<String>();
    ArrayList<WeekSlot> wslotlist = new ArrayList<WeekSlot>();
    CustomListView lv_slottime, lv_weekday;
    RelativeLayout rlstarddate, rlenddate, rl_weektime;
    DBMedicalItem dbmedicalitem;
    Switch switch_endate;
    String take = "";

    public MedicationItemsAdapter(Context context, List<MedicalItem> lstmedication) {
        this._mcont = context;
        this.meditemslist = lstmedication;
        arrfreqcode = _mcont.getResources().getStringArray(R.array.freqcode);
        arrweekslotcode = _mcont.getResources().getStringArray(R.array.weekslotname);
        dbmedicalitem = new DBMedicalItem(_mcont);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return meditemslist.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder v;
        if (convertView == null) {
            v = new ViewHolder();
            convertView = LayoutInflater.from(_mcont).inflate(R.layout.row_medicationitems, null);
            //region  holder findeviewbyid
            v.label_medicationname = (TextView) convertView.findViewById(R.id.label_medicationname);
            v.label_usage = (TextView) convertView.findViewById(R.id.label_usage);
            v.label_dosage = (TextView) convertView.findViewById(R.id.label_dosage);
            v.label_dosageunit = (TextView) convertView.findViewById(R.id.label_dosageunit);
            v.label_frequency = (TextView) convertView.findViewById(R.id.label_frequency);
            v.label_totalQty = (TextView) convertView.findViewById(R.id.label_totalQty);
            v.label_totalQtyunit = (TextView) convertView.findViewById(R.id.label_totalQtyunit);
            v.label_precaution1 = (TextView) convertView.findViewById(R.id.label_precaution1);
            v.label_precaution2 = (TextView) convertView.findViewById(R.id.label_precaution2);
            v.label_precaution3 = (TextView) convertView.findViewById(R.id.label_precaution3);
            v.txt_dtlmedicationname = (TextView) convertView.findViewById(R.id.txt_dtlmedicationname);
            v.txt_usage = (TextView) convertView.findViewById(R.id.txt_usage);
            v.txt_dosage = (TextView) convertView.findViewById(R.id.txt_dosage);
            v.txt_dosageunit = (TextView) convertView.findViewById(R.id.txt_dosageunit);
            v.txt_frequency = (TextView) convertView.findViewById(R.id.txt_frequency);
            v.txt_totalQty = (TextView) convertView.findViewById(R.id.txt_totalQty);
            v.txt_totalQtyunit = (TextView) convertView.findViewById(R.id.txt_totalQtyunit);
            v.txt_precaution1 = (TextView) convertView.findViewById(R.id.txt_precaution1);
            v.txt_precaution2 = (TextView) convertView.findViewById(R.id.txt_precaution2);
            v.txt_precaution3 = (TextView) convertView.findViewById(R.id.txt_precaution3);
            v.rlschedule = (RelativeLayout) convertView.findViewById(R.id.rlschedule);
            v.reminder_freqcode = (TextView) convertView.findViewById(R.id.reminder_freqcode);
            v.reminder_time = (TextView) convertView.findViewById(R.id.reminder_time);

            v.reminder_startdate_value = (TextView) convertView.findViewById(R.id.reminder_startdate_value);
            v.reminder_enddate_value = (TextView) convertView.findViewById(R.id.reminder_enddate_value);
            v.reminder_startdate_label = (TextView) convertView.findViewById(R.id.reminder_startdate_label);
            v.reminder_enddate_label = (TextView) convertView.findViewById(R.id.reminder_enddate_label);
            // v.switch_reminder = (Switch) convertView.findViewById(R.id.switch_reminder);

            v.toggle = (ToggleButton) convertView.findViewById(R.id.toggleBtn);

            //endregion

            convertView.setTag(v);
        } else {
            v = (ViewHolder) convertView.getTag();
        }

        final MedicalItem item = meditemslist.get(position);

        //region checkdata
        checkdata(item.getMedicalName(), v.label_medicationname, v.txt_dtlmedicationname);
        checkdata(item.getMedicalUsage(), v.label_usage, v.txt_usage);
        checkdata(item.getMedicalDosage(), v.label_dosage, v.txt_dosage);
        checkdata(item.getMedicalDosageUnit(), v.label_dosageunit, v.txt_dosageunit);
        checkdata(item.getMedicalFreq(), v.label_frequency, v.txt_frequency);
        checkdata(item.getMedicalTotalQty(), v.label_totalQty, v.txt_totalQty);
        checkdata(item.getMedicalTotalQtyUnit(), v.label_totalQtyunit, v.txt_totalQtyunit);
        checkdata(item.getPreCaution1(), v.label_precaution1, v.txt_precaution1);
        checkdata(item.getPreCaution2(), v.label_precaution2, v.txt_precaution2);
        checkdata(item.getPreCaution3(), v.label_precaution3, v.txt_precaution3);
        //endregion

        //region check for remider layout
        boolean contains = Arrays.asList(arrfreqcode).contains(item.getMedicalFreqCode());

        if (contains) {
            v.rlschedule.setVisibility(View.VISIBLE);
        } else {
            v.rlschedule.setVisibility(View.GONE);
        }

        if (item.getIs_reminder() == 1) {
            // v.switch_reminder.setChecked(true);
            v.toggle.setChecked(true);
        } else {
            //  v.switch_reminder.setChecked(false);
            v.toggle.setChecked(false);

        }
        //endregion

        //region loadData
        v.txt_dtlmedicationname.setText(item.getMedicalName());
        v.txt_usage.setText(item.getMedicalUsage());
        v.txt_dosage.setText(item.getMedicalDosage());
        v.txt_dosageunit.setText(item.getMedicalDosageUnit());
        v.txt_frequency.setText(item.getMedicalFreq());
        v.txt_totalQty.setText(item.getMedicalTotalQty());
        v.txt_totalQtyunit.setText(item.getMedicalTotalQtyUnit());
        v.txt_precaution1.setText(item.getPreCaution1());
        v.txt_precaution2.setText(item.getPreCaution2());
        v.txt_precaution3.setText(item.getPreCaution3());
        v.reminder_freqcode.setText(item.getMedicalFreq());
        //endregion

        //region startdate & enddate  & slot time for listview
        String sdate = "", edate = "";
        if (item.getStartdate() > 0) {
            sdate = Utils.BookDateFormat(item.getStartdate().toString(), "yyyyMMdd", "dd-MMM-yyyy");
        } else {
            sdate = Utils.getcurrenttime("dd-MMM-yyyy");
        }
        if (item.getEnddate() > 0) {
            edate = Utils.BookDateFormat(item.getEnddate().toString(), "yyyyMMdd", "dd-MMM-yyyy");
            v.reminder_enddate_label.setVisibility(View.VISIBLE);
        } else {
            v.reminder_enddate_label.setVisibility(View.GONE);
            v.reminder_enddate_value.setText("");
        }

        if (item.getIs_reminder() > 0) {
            v.reminder_startdate_value.setText(sdate);
            v.reminder_enddate_value.setText(edate);
            String rtime = getreminderTime(item.getMedicalFreqCode(), item.getSlotinterval());
            v.reminder_time.setText(rtime);
            //  v.reminder_enddate_label.setVisibility(View.VISIBLE);
            v.reminder_startdate_label.setVisibility(View.VISIBLE);
        } else {
            v.reminder_startdate_value.setText("");
            v.reminder_enddate_value.setText("");
            v.reminder_time.setText("");
            v.reminder_enddate_label.setVisibility(View.GONE);
            v.reminder_startdate_label.setVisibility(View.GONE);
        }

        //endregion

        v.toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (v.toggle.isChecked()) {
                    if (item.getIs_reminder() > 0) {
                    } else {
                        reminderDialog(item, position, v);
                    }
                } else {
                    item.setIs_reminder(0);
                    item.setSlotinterval("");
                    item.setStartdate(0);
                    item.setEnddate(0);
                    item.setNextdate(0L);
                    item.setNumberofdays(0);
                    v.reminder_startdate_value.setText("");
                    v.reminder_enddate_value.setText("");
                    v.reminder_time.setText("");
                    v.reminder_enddate_label.setVisibility(View.GONE);
                    v.reminder_startdate_label.setVisibility(View.GONE);
                    notifyDataSetChanged();
                    dbmedicalitem.updateMedicalItemReminder(item);
                    callReminderTime();
                }
            }
        });

        return convertView;
    }

    public void checkdata(String value, View vlabel, View vdata) {
        if (value.equals("")) {
            vlabel.setVisibility(View.GONE);
            vdata.setVisibility(View.GONE);
        } else {
            vlabel.setVisibility(View.VISIBLE);
            vdata.setVisibility(View.VISIBLE);
        }
    }

    class ViewHolder {
        TextView label_medicationname, label_usage, label_dosage, label_dosageunit, label_frequency,
                label_totalQty, label_totalQtyunit, label_precaution1, label_precaution2, label_precaution3,
                txt_dtlmedicationname, txt_usage, txt_dosage, txt_dosageunit, txt_frequency, txt_totalQty,
                txt_totalQtyunit, txt_precaution1, txt_precaution2, txt_precaution3, reminder_freqcode,
                reminder_time, reminder_startdate_value, reminder_enddate_value,
                reminder_startdate_label, reminder_enddate_label;

        ToggleButton toggle;
        RelativeLayout rlschedule;
        //  Switch switch_reminder;
    }

    public String getreminderTime(String freqcode, String interval) {
        String slot = "";
        if (!interval.equals("")) {
            if (!freqcode.equals("W")) {
                String[] times = interval.split(",");
                String stime = Loadslotstring(times);// hh:mm a
                slot = stime;
            } else {
                String[] wslot = interval.split(",");
                String wstime = wslot[0];
                String wshowtime = Utils.BookDateFormat(wstime, "HHmm", "hh:mm a");
                ArrayList<WeekSlot> week = weekdaySlot(interval, true);
                String sweek = "";
                for (int i = 0; i < week.size(); i++) {
                    if (week.get(i).getChecked()) {
                        if (sweek.equals("")) {
                            sweek = week.get(i).getSlotname();
                        } else {
                            sweek = sweek + "," + week.get(i).getSlotname();
                        }
                    }
                }
                slot = wshowtime + " , " + sweek;
            }
        } else {
            if (!freqcode.equals("W")) {
                ArrayList<String> lstslot = findslotlist(freqcode);
                String strslot = "";
                for (String slottime : lstslot) {
                    if (strslot.equals("")) {
                        strslot = slottime;
                    } else {
                        strslot = strslot + "," + slottime;
                    }
                }
                slot = strslot;
            } else {
                if (!interval.equals("")) {
                    slot = interval;
                } else {
                    slot = "";
                }
            }
        }

        return slot;
    }

    public void reminderDialog(final MedicalItem medical, final int position, final ViewHolder v) {
        final Dialog slot_dialog = new Dialog(_mcont);
        slot_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        slot_dialog.setContentView(R.layout.dialog_set_reminder);
        slot_dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        //region findviewbyid
        lv_slottime = (CustomListView) slot_dialog.findViewById(R.id.lv_timeslot);
        lv_weekday = (CustomListView) slot_dialog.findViewById(R.id.lv_weekday);
        txtstartdate_value = (TextView) slot_dialog.findViewById(R.id.txtstartdate_value);
        txtenddate_value = (TextView) slot_dialog.findViewById(R.id.txtenddate_value);
        txt_medicine_name = (TextView) slot_dialog.findViewById(R.id.txt_medicine_name);
        txt_medicine_freqname = (TextView) slot_dialog.findViewById(R.id.txt_medicine_freqname);
        btn_cancel = (Button) slot_dialog.findViewById(R.id.btn_cancel);
        btn_save = (Button) slot_dialog.findViewById(R.id.btn_save);
        rlstarddate = (RelativeLayout) slot_dialog.findViewById(R.id.rlstarddate);
        rlenddate = (RelativeLayout) slot_dialog.findViewById(R.id.rlenddate);
        switch_endate = (Switch) slot_dialog.findViewById(R.id.switch_endate);
        rl_weektime = (RelativeLayout) slot_dialog.findViewById(R.id.rl_weektime);
        week_timeslot = (TextView) slot_dialog.findViewById(R.id.week_timeslot);
        week_Take_tap = (TextView) slot_dialog.findViewById(R.id.week_Take_tap);

        week_Take_tap.setText("Time 1 (" + medical.getMedicalUsage() + " " +
                medical.getMedicalDosage() + " " + medical.getMedicalDosageUnit() + ")");
        //endregion

        //region Load start date & end date
        String stdate = "", endate = "";
        if (medical.getStartdate() > 0) {
            stdate = Utils.BookDateFormat(medical.getStartdate().toString(), "yyyyMMdd", "dd-MMM-yyyy");
        } else {
            stdate = Utils.getcurrenttime("dd-MMM-yyyy");
        }
        if (medical.getEnddate() > 0) {
            endate = Utils.BookDateFormat(medical.getStartdate().toString(), "yyyyMMdd", "dd-MMM-yyyy");
        }
        txtstartdate_value.setText(stdate);
        txtenddate_value.setText(endate);
        //endregion

        //region load freqcode
        txt_medicine_freqname.setText(medical.getMedicalFreq());
        txt_medicine_name.setText(medical.getMedicalName());

        if (medical.getMedicalFreqCode().equals("W")) {
            lv_slottime.setVisibility(View.GONE);
            lv_weekday.setVisibility(View.VISIBLE);
            rl_weektime.setVisibility(View.VISIBLE);

            if (!medical.getSlotinterval().equals("")) { // Load Data
                String[] wslot = medical.getSlotinterval().split(",");
                String wstime = wslot[0];
                String wshowtime = Utils.BookDateFormat(wstime, "HHmm", "hh:mm a");
                week_timeslot.setText(wshowtime);
                loadDataWeekData(medical.getSlotinterval(), true);
            } else { // default show W
                String default_interval = _mcont.getResources().getString(R.string.default_weekslot);
                String[] wslot = default_interval.split(",");
                String wstime = wslot[0];
                String wshowtime = Utils.BookDateFormat(wstime, "HHmm", "hh:mm a");
                week_timeslot.setText(wshowtime);
                loadDataWeekData(default_interval, false);
            }
        } else {
            lv_slottime.setVisibility(View.VISIBLE);
            lv_weekday.setVisibility(View.GONE);
            rl_weektime.setVisibility(View.GONE);
            if (medical.getSlotinterval().equals("")) {
                slotlist = findslotlist(medical.getMedicalFreqCode()); // to show format  hh:mm a
            } else {
                //need to find slotlist depend on medical.getSlotingerval
                String gsinterval = medical.getSlotinterval();
                slotlist = getslotlist(gsinterval);
            }
            take = medical.getMedicalUsage() + " " + medical.getMedicalDosage() + " " + medical.getMedicalDosageUnit();
            loadData(take);
        }
        //endregion

        //region setEvent
        lv_slottime.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showTimepickerDialog(i);
            }
        });

        txtstartdate_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatepickerDialog();
            }
        });
        rlstarddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showDatepickerDialog();
            }
        });
        txtenddate_value.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String startdate = txtstartdate_value.getText().toString();
                    if (!startdate.equals("")) {
                        Date date = simpleDateFormat.parse(startdate);
                        endDatepickerDialog(date);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        rlenddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String startdate = txtstartdate_value.getText().toString();
                    if (!startdate.equals("")) {
                        Date date = simpleDateFormat.parse(startdate);
                        endDatepickerDialog(date);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medical.setIs_reminder(0);
                notifyDataSetChanged();
                slot_dialog.dismiss();
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rstartdate = txtstartdate_value.getText().toString();
                String rendate = txtenddate_value.getText().toString();
                String wdayslottime = week_timeslot.getText().toString();
                Integer sdate1 = 0, edate1 = 0;
                String rtime = "";

                if (!rstartdate.equals("")) {
                    sdate1 = Integer.parseInt(Utils.BookDateFormat(rstartdate, "dd-MMM-yyyy", "yyyyMMdd"));
                }
                if (!rendate.equals("")) {
                    edate1 = Integer.parseInt(Utils.BookDateFormat(rendate, "dd-MMM-yyyy", "yyyyMMdd"));
                }
                if (medical.getMedicalFreqCode().equals("W")) {
                    String winterval = "";
                    wslotlist = lstweekadapter.wslot;
                    if (wslotlist.size() > 0) {
                        for (int i = 0; i < wslotlist.size(); i++) {
                            if (wslotlist.get(i).getChecked()) {
                                if (!winterval.equals("")) {
                                    winterval = winterval + "," + wslotlist.get(i).getSlotid();
                                } else {
                                    winterval = wslotlist.get(i).getSlotid() + "";
                                }
                            }
                        }
                    }
                    if (winterval.equals("")) {
                        Utils.showAlertDialog(_mcont, "Alert", "Please select at least one day to set reminder");
                        return;
                    } else {
                        if (!wdayslottime.equals("")) {
                            String wtime = Utils.BookDateFormat(wdayslottime, "hh:mm a", "HHmm");
                            medical.setSlotinterval(wtime + "," + winterval);
                        } else {
                            medical.setSlotinterval("0800" + "," + winterval);
                        }

                        String default_interval = medical.getSlotinterval();
                        ArrayList<WeekSlot> week = weekdaySlot(default_interval, true);
                        String sweek = "";
                        for (int i = 0; i < week.size(); i++) {
                            if (week.get(i).getChecked()) {
                                if (sweek.equals("")) {
                                    sweek = week.get(i).getSlotname();
                                } else {
                                    sweek = sweek + "," + week.get(i).getSlotname();
                                }
                            }
                        }
                        rtime = wdayslottime + "," + sweek;

                    }
                } else { // othere freqcode except 'W'
                    String sinterval = "";
                    if (slotlist.size() > 0) {
                        sinterval = gettimeslotstr();
                        medical.setSlotinterval(sinterval);
                        String reminder_time = sinterval;
                        String[] rinterval = reminder_time.split(",");
                        String freminder_time = Loadslotstring(rinterval);
                        rtime = freminder_time;
                    }
                }
                medical.setIs_reminder(1);
                medical.setStartdate(sdate1);
                medical.setEnddate(edate1);


                dbmedicalitem.updateMedicalItemReminder(medical);

                v.reminder_startdate_value.setText(rstartdate);
                v.reminder_enddate_value.setText(rendate);
                v.reminder_time.setText(rtime);
                v.reminder_enddate_label.setVisibility(View.VISIBLE);
                v.reminder_startdate_label.setVisibility(View.VISIBLE);
                notifyDataSetChanged();
                // ActivityMedDispenseDetails.lstmeditems.set(position, medical);
                //ActivityMedDispenseDetails.getmedicalItems(medical.getVisitno());

                callReminderTime();

                slot_dialog.dismiss();

            }
        });
        switch_endate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    if (medical.getIs_reminder() == 0) {
                        rlenddate.setVisibility(View.VISIBLE);
                        String endate = Utils.getcurrenttime("dd-MMM-yyyy");
                        txtenddate_value.setText(endate);
                    }
                } else {
                    rlenddate.setVisibility(View.GONE);
                    //  String endate = Utils.getcurrenttime("dd-MMM-yyyy");
                    txtenddate_value.setText("");
                    // v.reminder_enddate_value.setText("");
                   /* v.reminder_enddate_label.setVisibility(View.GONE);
                    v.reminder_enddate_value.setVisibility(View.GONE);*/
                    medical.setEnddate(0);
                    dbmedicalitem.updateMedicalItemReminder(medical);
                }
            }
        });
        rl_weektime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                weekTimepickerDialog();
            }
        });
        //endregion

        slot_dialog.show();
    }

    //region week
    public void loadDataWeekData(String strname, boolean check) {
        wslotlist = weekdaySlot(strname, check);
        lstweekadapter = new WeekDayTimeAdapter(_mcont, wslotlist);
        lv_weekday.setAdapter(lstweekadapter);
        lv_weekday.setExpanded(true);
        lstweekadapter.notifyDataSetChanged();
    }

    public ArrayList<WeekSlot> weekdaySlot(String name, boolean checked) {
        ArrayList<WeekSlot> wlist = new ArrayList<WeekSlot>();
        if (!name.equals("")) {
            String[] wday = name.split(",");
            if (wday.length > 0) {
                for (int i = 1; i < wday.length; i++) {
                    int slotcode = Integer.parseInt(wday[i]);
                    String wdayname = arrweekslotcode[slotcode - 1];
                    WeekSlot new_slot = new WeekSlot();
                    new_slot.setSlotname(wdayname);
                    new_slot.setSlotid(slotcode);
                    new_slot.setChecked(checked);
                    wlist.add(new_slot);
                }
            }
        }
        return wlist;
    }

    //endregion
    //region slot
    public String Loadslotstring(String[] slotinterval) {
        String slotstr = "";
        try {
            if (slotinterval.length > 0) {
                for (String slot : slotinterval) {
                    String slotdate = Utils.BookDateFormat(slot, "HHmm", "hh:mm a");
                    if (!slotstr.equals("")) {
                        slotstr = slotstr + "," + slotdate;
                    } else {
                        slotstr = slotdate;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return slotstr;
    }

    public ArrayList<String> getslotlist(String slotinterval) {
        ArrayList<String> gslot = new ArrayList<String>();
        String[] stime = slotinterval.split(",");
        Arrays.sort(stime);
        if (stime.length > 0) {
            for (String s : stime) {
                String finaltime = Utils.BookDateFormat(s, "HHmm", "hh:mm a");
                gslot.add(finaltime);
            }
        }
        return gslot;
    }

    public String gettimeslotstr() {
        String strtime = "";
        ArrayList<String> timeslot = new ArrayList<String>();
        if (slotlist.size() > 0) {
            for (int i = 0; i < slotlist.size(); i++) {
                String str = Utils.BookDateFormat(slotlist.get(i), "hh:mm a", "HHmm");
                timeslot.add(str);
            }
        }
        Collections.sort(timeslot);
        for (int i = 0; i < timeslot.size(); i++) {
            if (strtime.equals("")) {
                strtime = timeslot.get(i);
            } else {
                strtime = strtime + "," + timeslot.get(i);
            }
        }
        return strtime;
    }

    public void loadData(String take) {
        lstslotadapter = new ListSlotTimeAdapter(_mcont, slotlist, take);
        lv_slottime.setAdapter(lstslotadapter);
        lv_slottime.setExpanded(true);
        lstslotadapter.notifyDataSetChanged();
    }

    public ArrayList<String> findslotlist(String freqname) {
        ArrayList<String> finalslotlist = new ArrayList<String>();
        try {
            ArrayList<String> stringslotlist = new ArrayList<String>();
            switch (freqname) {
                case "1":
                    break;
                case "1A":
                    break;
                case "2":
                    break;
                case "3":
                    break;
                case "4":
                    break;
                case "6":
                    break;
                case "8":
                    break;
                case "B":
                    stringslotlist = morethan2timesslot(2, 8, "8:00");
                    break;
                case "D":
                    stringslotlist.add("8:00");
                    break;
                case "E":
                    stringslotlist.add("8:00");
                    break;
                case "F":
                    stringslotlist = morethan2timesslot(5, 4, "8:00");
                    break;
                case "M":
                    stringslotlist.add("8:00");
                    break;
                case "N":
                    stringslotlist.add("20:00");
                    break;
                case "O":
                    break;
                case "P":
                    break;
                case "Q":
                    stringslotlist = morethan2timesslot(4, 4, "8:00");
                    break;
                case "S":
                    break;
                case "T":
                    stringslotlist = morethan2timesslot(3, 6, "8:00");
                    break;
                case "W":
                case "W1":
                    break;
                default:
                    break;
            }
            if (stringslotlist.size() > 0) {
                for (String slot : stringslotlist) {
                    String slotdate = simpleTimeFormat.format(simple24TimeFormat.parse(slot));
                    finalslotlist.add(slotdate);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return finalslotlist;
    }

    public ArrayList<String> morethan2timesslot(int count, int increment, String starttime) {
        ArrayList<String> timeslot = new ArrayList<String>();
        int time = 8;
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                if (i == 0) {
                    timeslot.add(starttime);
                } else {
                    time = (time + increment);
                    String strtime = time + ":00";
                    timeslot.add(strtime);
                }
            }
        }
        return timeslot;
    }

    //endregion
    //region DateTime picker
    public String showTimepickerDialog(final int position) {
        Calendar newCalendar = Calendar.getInstance();
        format_time = "";
        TimePickerDialog timepickerdialog = new TimePickerDialog(_mcont, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                try {
                    String new_time = hourOfDay + ":" + minute;
                    format_time = Utils.BookDateFormat(new_time, "H:mm", "hh:mm a");
                    slotlist.remove(position);
                    slotlist.add(position, format_time);

                    loadData(take);
                    Log.e("format time ", format_time);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, newCalendar.getTime().getHours(), newCalendar.getTime().getMinutes(), false);
        timepickerdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timepickerdialog.setTitle("Time");
        timepickerdialog.show();
        return format_time;
    }

    public void showDatepickerDialog() {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(_mcont, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        txtstartdate_value.setText(simpleDateFormat.format(newDate.getTime()));
                        txtenddate_value.setText("");
                    }
                },
                newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setTitle("Date");
        dialog.getDatePicker().setMinDate(newCalendar.getTime().getDate());
        dialog.show();
    }

    public void endDatepickerDialog(Date mindate) {
        Calendar newCalendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(_mcont, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        newDate.set(year, monthOfYear, dayOfMonth);
                        txtenddate_value.setText(simpleDateFormat.format(newDate.getTime()));
                    }
                },
                newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setTitle("Date");
        dialog.getDatePicker().setMinDate(mindate.getTime());
        dialog.show();
    }

    public String weekTimepickerDialog() {
        Calendar newCalendar = Calendar.getInstance();
        format_time = "";
        TimePickerDialog timepickerdialog = new TimePickerDialog(_mcont, android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                try {
                    String new_time = hourOfDay + ":" + minute;
                    format_time = Utils.BookDateFormat(new_time, "H:mm", "hh:mm a");
                    week_timeslot.setText(format_time);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }, newCalendar.getTime().getHours(), newCalendar.getTime().getMinutes(), false);
        timepickerdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        timepickerdialog.setTitle("Time");
        timepickerdialog.show();
        return format_time;
    }
    //endregion

    public void callReminderTime() {
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                MedicalReminderTime medReminder = new MedicalReminderTime(_mcont);
                medReminder.refreshMedicalReminder();
               /* if (Thread.interrupted()) {*/
               /*     Thread.yield();*/
               /* } else {*/
               /*     MedicalReminderTime medReminder = new MedicalReminderTime(_mcont);*/
               /*     medReminder.refreshMedicalReminder();*/
               /* }*/
            }
        };
        new Thread(runnable).start();
    }

}

