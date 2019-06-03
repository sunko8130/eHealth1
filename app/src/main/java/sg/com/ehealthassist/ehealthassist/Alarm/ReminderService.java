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

import sg.com.ehealthassist.ehealthassist.Medication.ActivityMedicalDispense;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 6/30/2017.
 */

public class ReminderService extends IntentService {

    public ReminderService() {
        super("Medication Reminder Service");
    }
    private NotificationManager alarmNotificationManager;
    @Override
    public void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();

        String visitno = extras.getString("visitno");
        String ids = extras.getString("ids");
        String message = extras.getString("messages");
        String uuid = extras.getString("uuid");
        String freqcode = extras.getString("freqcode");

        sendNotification(message,visitno,ids,uuid,freqcode);
        //  gotoalarmpage(long_book_id,short_book_id,uniqueInt, settime);
    }

    private void sendNotification(String msg,String visitno,String ids,String uuid,String freqcode) {

        alarmNotificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(this,ActivityMedicalDispense.class);
        intent.putExtra("visitno",visitno);
        intent.putExtra("ids",ids);
        intent.putExtra("messages",msg);
        intent.putExtra("uuid",uuid);
        intent.putExtra("freqcode",freqcode);
        intent.putExtra("from_activity","ReminderService");

        Log.e("pill message",msg);

        int uniqueInt = Integer.parseInt(uuid) ;

        PendingIntent contentIntent = PendingIntent.getActivity(this, uniqueInt,
                intent,  PendingIntent.FLAG_UPDATE_CURRENT);

        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder alamNotificationBuilder = new NotificationCompat.Builder(
                this).setContentTitle("EHeatlthAssist")
                .setSmallIcon(R.drawable.ehealthassist)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setAutoCancel(true)
                .setSound(uri)
                .setContentText(msg)
                .setContentIntent(contentIntent);

        alarmNotificationManager.notify(uniqueInt, alamNotificationBuilder.build());

    }
}
