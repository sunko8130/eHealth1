package sg.com.ehealthassist.ehealthassist.Alarm;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by User on 6/30/2017.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {

        // Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        //if (alarmUri == null) {
        //   alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        // }
        //Ringtone ringtone = RingtoneManager.getRingtone(context, alarmUri);
        //ringtone.play();

        ComponentName comp = new ComponentName(context.getPackageName(),
                AlarmService.class.getName());

        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}