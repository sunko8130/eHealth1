package sg.com.ehealthassist.ehealthassist.GDAA;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;


import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 12/16/2016.
 */

public class GDAAService extends Service{

    public static final long NOTIFY_INTERVAL = 60 * 1000 * 5;
    SharedPreferences preferences = null;
    DBMedProfile dbmedprofile ;
    ArrayList<MedicalProfile> medlist = new ArrayList<MedicalProfile>();
    boolean backup_drive = true,update_profile_flag = false;
    public  String login_member_id="";
    // run on another Thread to avoid crash
    private Handler mHandler = new Handler();

    // timer handling
    private Timer mTimer = null;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        // cancel if already existed
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        dbmedprofile = new DBMedProfile(this);

        if (mTimer != null) {
            mTimer.cancel();
        } else {
            // recreate new
            mTimer = new Timer();
        }
        // schedule task
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(), 0, NOTIFY_INTERVAL);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class TimeDisplayTimerTask  extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        update_profile_flag = preferences.getBoolean(getString(R.string.pref_update_profile_flag),false);
                        backup_drive = preferences.getBoolean(getString(R.string.pref_backup_drive_checked), true);
                        login_member_id = preferences.getString(getString(R.string.pref_login_memberId), "");
                        Log.e("services", "backup drive : " + backup_drive + "login merber id" + login_member_id);
                        if(update_profile_flag){
                            Log.e("services", "profile flag : " + update_profile_flag );
                            if (backup_drive) {
                                if (preferences.getBoolean(getString(R.string.pref_gdaa_connect_sync), false)) {
                                    if (!login_member_id.equals("")) {
                                        medlist = dbmedprofile.getMedProfilebyMemberid(login_member_id);
                                        Log.e("service", "profile size " + medlist.size());
                                        if (medlist.size() > 1 ) {
                                            //   if (!medlist.get(0).getNric().equals("") && !medlist.get(0).getNric_name().equals("") ) {
                                            Log.e("service", "GDAA " + medlist.size());
                                            GDAAConnect.connect();
                                            GDAAConnect.onrequestSync();
                                            GDAAConnect.uploadprofileToDrive(medlist);

                                            //  }
                                        }
                                        else if (medlist.size()==1){
                                            if (!medlist.get(0).getNric().equals("") && !medlist.get(0).getNric_name().equals("") ) {
                                                Log.e("service", "GDAA " + medlist.size());
                                                GDAAConnect.connect();
                                                GDAAConnect.onrequestSync();
                                                GDAAConnect.uploadprofileToDrive(medlist);
                                            }
                                        }
                                    }
                                }
                            }
                            preferences.edit().putBoolean(getString(R.string.pref_update_profile_flag),false).apply();
                        }
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                    // CheckAppisRunning();
                    // Toast.makeText(getApplicationContext(), "Ehealthassist Uploaded" + getDateTime(),
                    //       Toast.LENGTH_SHORT).show();
                }
            });
        }

        private String getDateTime() {
            // get date time in custom format
            SimpleDateFormat sdf = new SimpleDateFormat("[yyyy/MM/dd - HH:mm:ss]");
            return sdf.format(new Date());
        }

      /*  private void CheckAppisRunning() {
            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
            for (int i = 0; i < procInfos.size(); i++) {
                if (procInfos.get(i).processName.equals("sg.com.ehealthassist.ehealthassist")) {
                    Log.e("Result", "App is running - Doesn't need to reload");

                } else {
                    Log.e("Result", "App is not running - Needs to reload");
                }
            }

        }*/


    }
}
