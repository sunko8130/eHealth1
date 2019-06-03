package sg.com.ehealthassist.ehealthassist.GCM;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Random;

import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.Messages.ActivityMessageList;
import sg.com.ehealthassist.ehealthassist.Queue_Appt.ActivityQueue_Appoint;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by thazinhlaing on 1/7/17.
 */
public class GcmIntentService  extends IntentService {
    public static final int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;
    public static final String TAG = "GCM IntentService";

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.e("error", "GCM error msg");
                sendNotification(extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.e("deleted", "GCM delete msg");
                sendNotification(extras);
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                Log.e("message", "GCM type msg");

                sendNotification(extras);
            }
        }
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(Bundle extra) {
        Log.e("GCM Ehealthassist",extra.toString());
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        String contextTitle = extra.getString("ContentTitle", "eHealthAssist");
        String contextMessage = extra.getString("ContentText", "Register Completed");
        String action = extra.getString("Action", "");
        PendingIntent contentIntent = null;
        int requestCode = new Random().nextInt();
        switch (action) {
            case "AccountVerified":
                contentIntent = PendingIntent.getActivity(this, 0, new Intent(this,ActivityHome1.class), 0);
                break;
            case "QueueResponse":
                String queueNo = extra.getString("QueueNo", "-1");//exist only if action = QueueResponse
                String requestID = extra.getString("RequestID", "");
                String reject_description = extra.getString("RejectDesc", "");
                String qstatus = extra.getString("Status","");
                Intent  intent = new Intent(this,ActivityQueue_Appoint.class);
                intent.putExtra("from","GCMIntentService");
                contentIntent = PendingIntent.getActivity(this, requestCode,intent, PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            case "AppointmentResponse":
                Intent  intents = new Intent(this,ActivityQueue_Appoint.class);
                intents.putExtra("from","ActivityEApptCardDetail");
                contentIntent = PendingIntent.getActivity(this, requestCode,intents, PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            case "MessageResponse":
                String MessageId = extra.getString("MessageID","-1");
                Intent intent2 = new Intent(this, ActivityMessageList.class);
                intent2.putExtra("from","GCMIntentService");
                intent2.putExtra("MessageID",MessageId);
                contentIntent = PendingIntent.getActivity(this, requestCode,intent2, PendingIntent.FLAG_UPDATE_CURRENT);
                break;
            default:
                contentIntent = PendingIntent.getActivity(this, requestCode, new Intent(this,ActivityHome1.class) , 0);
                break;
        }
        Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ehealthassist)
                        .setContentTitle(contextTitle)
                        .setContentText(contextMessage)
                        .setAutoCancel(true)
                        .setSound(uri)
                        .setContentIntent(contentIntent);
        mNotificationManager.notify(requestCode, mBuilder.build());
        Vibrator vibrator = (Vibrator) getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }

    /*public void UpdateQLog(int queue_no, String request_id, String description,String qstatus) {
        DatabaseHandlerQueueRequest upRQL = new DatabaseHandlerQueueRequest(this);
        if (upRQL.CheckRequestid(request_id) > 0) {
            upRQL.updateRQL(request_id, queue_no, description,qstatus);
            Log.e("successfully update>>>", request_id + " qstatus :" + qstatus);

        } else {
            Log.e("NO RequestId>>>", request_id);

        }
    }*/
}


