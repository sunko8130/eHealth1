package sg.com.ehealthassist.ehealthassist.Medication;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import sg.com.ehealthassist.ehealthassist.Alarm.ReminderReceiver;
import sg.com.ehealthassist.ehealthassist.DB.DBMedReminder;
import sg.com.ehealthassist.ehealthassist.DB.DBMedicalItem;
import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalItem;
import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalReminder;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by User on 8/1/2017.
 */

public class MedicalReminderTime {
    DBMedicalItem dbmeditem;
    DBMedReminder dbmedreminder;
    Context _context;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.US);
    SimpleDateFormat simpleDateTimeFormat= new SimpleDateFormat("yyyyMMddHHmm", Locale.US);
    int Flag = 0;
    public MedicalReminderTime(Context _mcontext){
        this._context = _mcontext;
        dbmeditem = new DBMedicalItem(_mcontext);
        dbmedreminder= new DBMedReminder(_mcontext);
    }
    public void refreshMedicalReminder(){
        try{
            int limitNotification = 30;
            //step 1 & step 2 remove all notification  & deleteall reminder table
            cancelreminderAlarm();
            //step 3 (reset nextDateTime to default 0 for medical table )
            dbmeditem.updateAllnextDateTime(0L);
            //step 4 (generate new nextDatetime base on current datetime)
            regenerateAllNextDatetime(CurrentDate());
            //generate nearest 64 local notification
            for (int i =0;i<limitNotification;i++){

                //step 5 (select the smaller nextdatetime  and set local notification and update nextdatetime)
                ArrayList<MedicalItem> smallestMedicals = dbmeditem.selectSmallestValidReminder();//
                ArrayList<MedicalItem> moresmallestMedicals;

                if(smallestMedicals.size()>0){
                    Long nextDatetime = smallestMedicals.get(0).getNextdate();//201708020800
                    moresmallestMedicals = dbmeditem.selectMoreSmallestValidReminder(nextDatetime);
                    // Combine Notification Message
                    String visitNo = "";
                    String uuID = "";
                    String IDs = "";
                    String title = "";
                    String message = "";

                    for (MedicalItem moresmallestMedical : moresmallestMedicals){// 1 count
                        visitNo = moresmallestMedical.getVisitno();

                        Random rand = new Random();
                        int Rnumber = rand.nextInt(10000);
                        int uniqueInt = Rnumber;

                        uuID =  uniqueInt+"";
                        IDs =   moresmallestMedical.getId()+",";

                        if ( i < limitNotification) {// 0 < 100
                            title = "Medicine Reminder";
                            message = message +   moresmallestMedical.getMedicalName() + " "
                                    + moresmallestMedical.getMedicalUsage() + " " + moresmallestMedical.getMedicalDosage()
                                    + " " + moresmallestMedical.getMedicalDosageUnit() + "\n";
                        }
                    else{
                            title = "Important! \nYou've reached your notification limit";
                            message = message + " Please launch the app to keep receiving notifications for your medications.Thank you";
                        }
                    }

                    //set reminder table to db
                    MedicalReminder medlocalnotification = new MedicalReminder();
                    medlocalnotification.setVisitno(visitNo);
                    medlocalnotification.setUuid(uuID+i);
                    medlocalnotification.setIds(IDs);
                    medlocalnotification.setMessage(message);
                    medlocalnotification.setSetdate(nextDatetime.toString());
                  //  medlocalnotification.setFreqcode(medreminderobject.getFreqcode());
                    dbmedreminder.insertMedicalReminder(medlocalnotification);
                    //set to alarm
                    Intent myIntent = new Intent(_context, ReminderReceiver.class);
                    myIntent.putExtra("visitno", medlocalnotification.getVisitno());
                    myIntent.putExtra("ids", medlocalnotification.getIds());
                    myIntent.putExtra("messages",message);
                    myIntent.putExtra("uuid", medlocalnotification.getUuid());
                    myIntent.putExtra("freqcode", medlocalnotification.getFreqcode());
                    int uniqueInt = Integer.parseInt(medlocalnotification.getUuid().toString().trim());
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(_context, uniqueInt, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                    Date nexthourDateTime = simpleDateTimeFormat.parse(nextDatetime.toString());
                    AlarmManager alarmManager = (AlarmManager) _context.getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC, nexthourDateTime.getTime(), pendingIntent);

                    //update new nextDateTime
                    Long nextstratdate =Long.parseLong( simpleDateTimeFormat.format(nexthourDateTime)); // 20170802

                    for(MedicalItem moresmallestMedical : moresmallestMedicals){
                        updateNextDateTime(nextstratdate,moresmallestMedical.getId(),moresmallestMedical.getItemno());
                    }

                }else{
                    break;
                }
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    //region New Reminder logic
    public void regenerateAllNextDatetime(Long startdate){
        try{
            Integer intstartdate = Integer.parseInt(simpleDateFormat.format(simpleDateTimeFormat.parse(startdate.toString())));
            ArrayList<MedicalItem> meditems = dbmeditem.selectAllValidReminder(intstartdate);
            for (MedicalItem item: meditems) {

                Long  nextDatetime = getNextDateTime(item.getMedicalFreqCode(),startdate,item.getEnddate(),item.getSlotinterval());
                item.setNextdate(nextDatetime);
                dbmeditem.updatemedicalItem(item);
            }
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public  void updateNextDateTime(Long startdate,String medicationid,String itemno){
        try{
            ArrayList<MedicalItem> medicals = dbmeditem.select(medicationid,itemno);
            for(MedicalItem item : medicals){

                Long  nextDatetime = getNextDateTime(item.getMedicalFreqCode(),startdate,item.getEnddate(),item.getSlotinterval());
                item.setNextdate(nextDatetime);
                dbmeditem.updatemedicalItem(item);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
    public Long getNextDateTime(String freqcode,Long startdate,Integer enddate,String slotInterval){
        switch (freqcode){
            case "1": break;
            case "1A":break;
            case "2": break;
            case "3": break;
            case "4": break;
            case "6": break;
            case "8": break;
            case "B": return getNextDatetimeLogic( 2,startdate,enddate,slotInterval, 1); // 2 times a day
            case "D": return getNextDatetimeLogic( 1,startdate,enddate,slotInterval, 1); // 1 times a day
            case "E": return getNextDatetimeLogic( 1,startdate,enddate,slotInterval, 2) ;// 1 times 2 days
            case "F": return getNextDatetimeLogic( 5,startdate,enddate,slotInterval, 1); // 5 times a day
            case "M": return getNextDatetimeLogic( 1,startdate,enddate,slotInterval, 1); // 1 times morning
            case "N": return getNextDatetimeLogic( 1,startdate,enddate,slotInterval, 1); // 1 times night
            case "O": break;
            case "P": break;
            case "Q": return getNextDatetimeLogic( 4,startdate,enddate,slotInterval, 1); // 4 times a day
            case "S": break;
            case "T": return getNextDatetimeLogic( 3,startdate,enddate,slotInterval, 1); // 3 times a day
            case "W":
                Flag = 0;
                return getNextWeekdayLogic( 1, startdate,enddate,slotInterval); //
            case "W1":break;
            default: break;

        }
        return  0L;
    }
    //find next date time
    public Long getNextDatetimeLogic(int slotintervalcount,Long startdate,Integer enddate,String slotinterval,int dayofinterval){
        String[] slots = slotinterval.split(",");
        try{
            if(slots.length == slotintervalcount){
                String nextDatestr = simpleDateFormat.format(simpleDateTimeFormat.parse(startdate.toString()));
                Integer newNextDate = Integer.parseInt(nextDatestr);
                Integer newEndDate = enddate;
                if (newEndDate==0) {
                    newEndDate = newNextDate;
                }
                while (newNextDate <=  newEndDate){
                    Long currentdate = startdate;

                    for (String slot: slots) {
                        String slotstr = newNextDate + slot;
                        Long nextslot = Long.parseLong(slotstr);
                        if (currentdate < nextslot ){
                            return  nextslot;
                        }
                    }
                    newNextDate = addIntervalDate(newNextDate.toString(),dayofinterval);
                    if (enddate ==0) {
                        newEndDate = newNextDate;
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return  0L;
    }
    //endregion
    //region week day login
    public  Long getNextWeekdayLogic(int slotintervalcount,Long startdate,Integer enddate,String slotinterval){
        try{
            String[] slots = slotinterval.split(",");
            if (slots.length > slotintervalcount) {

                Integer newNextDate = Integer.parseInt(nextWeekDay(startdate.toString(), slotintervalcount, slots));//

                Integer newEndDate = enddate;

                if (newEndDate == 0) {
                    newEndDate = newNextDate;
                }

                while (newNextDate <= newEndDate) {
                    Long currentdate = startdate;
                    int i = 1;
                    for (String slot : slots) {
                        if (i <= slotintervalcount) {
                            String slotstr = newNextDate + slot;
                            Long nextslot = Long.parseLong(slotstr);
                            if (currentdate < nextslot) {
                                return nextslot;
                            }
                        }
                        i = i + 1;
                    }


                    newNextDate = Integer.parseInt(nextWeekDay(startdate.toString(), slotintervalcount, slots));
                    if (enddate == 0) {
                        newEndDate = newNextDate;
                    }
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return  0L;
    }

    private String nextWeekDay(String currentdate,int slotTimeIntervalCount,String[] slots)  {
        String nextDate = "";

        try{
            Calendar cal = Calendar.getInstance();
            Date cdate = simpleDateTimeFormat.parse(currentdate);
            cal.setTime(cdate);
            int weekday = cal.get(Calendar.DAY_OF_WEEK);
            int nextweekday=0;
            int slotwrow =1;
            if(slots.length>0){
                for(int i = 1;i<slots.length;i++){
                    int slotweekday = Integer.parseInt(slots[i]);//5

                         if (slotweekday > weekday ){
                             nextweekday = slotweekday;
                             slotwrow = i;
                             break;
                         }
                }
                if(nextweekday>0){
                    cal.add(Calendar.WEEK_OF_YEAR,0);

                }else{
                    List valid = Arrays.asList(slots);
                    if(Flag ==0){
                        if(valid.contains(weekday+"")){

                            nextweekday = weekday;
                            cal.add(Calendar.WEEK_OF_YEAR, 0);
                            Flag = 1;
                        }
                    }
                    else{
                        nextweekday = Integer.parseInt(slots[slotwrow]);
                        cal.add(Calendar.WEEK_OF_YEAR,1);
                    }
                }

                cal.set(Calendar.DAY_OF_WEEK,nextweekday);
                nextDate = simpleDateFormat.format(cal.getTime());


            }
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return nextDate;
    }
    //endregion
    public Long CurrentDate(){
        Calendar newCalendar = Calendar.getInstance();
        String currentDate = simpleDateTimeFormat.format(newCalendar.getTime());
        return Long.parseLong(currentDate);
    }
    public Integer addIntervalDate(String date ,int dayinterval){
        try{
            Calendar newCalendar = Calendar.getInstance();
            Date customdate =  simpleDateFormat.parse(date);
            newCalendar.setTime(customdate);
            newCalendar.add(Calendar.DAY_OF_MONTH , dayinterval);
            String customstr = simpleDateFormat.format(newCalendar.getTime());
            Integer customdateInt = Integer.parseInt(customstr);
            return customdateInt;

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return 0;
    }
    public void cancelreminderAlarm(){
        ArrayList<MedicalReminder> lstreminder  = dbmedreminder.selectALL();
        if(lstreminder.size()>0){
            for(int i =0;i<lstreminder.size();i++){
                AlarmManager alarmManager = (AlarmManager) _context.getSystemService(ALARM_SERVICE);
                Intent myIntent = new Intent(_context, ReminderReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(_context, Integer.parseInt(lstreminder.get(i).getUuid()), myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.cancel(pendingIntent);
            }
            dbmedreminder.deleteALL();
        }
    }
    //endregion
}
