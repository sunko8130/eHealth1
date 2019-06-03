package sg.com.ehealthassist.ehealthassist.GDAA;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class GDAAUT {
    public static final String L_TAG = "_X_";
    public static SharedPreferences pfs;
    public static Context acx;

    public static void init(Context ctx) {
        acx = ctx.getApplicationContext();
        pfs = PreferenceManager.getDefaultSharedPreferences(acx);
    }

    public static class AM { private AM(){}
        public static final String ACC_NAME = "account_name";
        public static String mEmail = null;

        public static void setEmail(String email) {
            GDAAUT.pfs.edit().putString(ACC_NAME, (mEmail = email)).apply();
        }
        public static String getEmail() {
            return mEmail != null ? mEmail : (mEmail = GDAAUT.pfs.getString(ACC_NAME, null));
        }
    }
    public static void le(Throwable ex) {  Log.e(L_TAG, Log.getStackTraceString(ex));  }
}

