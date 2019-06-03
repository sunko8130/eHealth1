package sg.com.ehealthassist.ehealthassist.GCM;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

/**
 * Created by thazinhlaing on 1/7/17.
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    public GcmBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmIntentService.class.getName());

        startWakefulService(context,(intent.setComponent(comp)));
        // setResultCode(Activity.RESULT_OK);

    }
}

