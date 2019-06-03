package sg.com.ehealthassist.ehealthassist.Alarm;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;

import android.support.v4.app.NotificationCompat;
import android.util.Log;

import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 6/30/2017.
 */

public class AlarmService extends IntentService {

    private NotificationManager alarmNotificationManager;

    public AlarmService() {
        super("MyService");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        String long_book_id = extras.getString("long_book_id");
        String short_book_id = extras.getString("short_book_id");
        int uniqueInt = extras.getInt("uniqueInt");
        String settime = extras.getString("settime");

        // sendNotification("Appointment Alarm",long_book_id,short_book_id,uniqueInt);
        gotoalarmpage(long_book_id,short_book_id,uniqueInt, settime);
    }

    private void sendNotification(String msg,String book_id,String short_book_id,int unit) {
        Log.d("AlarmService", "Preparing to send notification...: " + msg);
        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this,ActivityAlarmshow.class);
        intent.putExtra("service_long_obj",book_id);
        intent.putExtra("service_short_obj",short_book_id);
        intent.putExtra("uniqueInt",unit);

        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);

        PendingIntent contentIntent = PendingIntent.getActivity(this, uniqueInt,
                intent,  PendingIntent.FLAG_UPDATE_CURRENT);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("EHeatlthAssist")
                .setSmallIcon(R.drawable.ehealthassist)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setAutoCancel(true)
                .setSound(uri)
                .setContentText(msg);

        if(!ActivityAlarmshow.active){
            alamNotificationBuilder.setContentIntent(contentIntent);
        }
        else{
            ActivityAlarmshow.instance().stopvibrate();
            ActivityAlarmshow.instance().finish();

        }
        alarmNotificationManager.notify(uniqueInt, alamNotificationBuilder.build());
        Log.d("MyService", "Notification sent.");
    }

    public void gotoalarmpage(String book_id,String short_id,int unit,String settime){
        Intent intent = new Intent(this,ActivityAlarmshow.class);
        intent.putExtra("service_long_obj",book_id);
        intent.putExtra("service_short_obj",short_id);
        intent.putExtra("uniqueInt",unit);
        intent.putExtra("settime",settime);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
    }

}
