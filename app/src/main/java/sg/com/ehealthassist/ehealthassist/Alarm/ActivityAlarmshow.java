package sg.com.ehealthassist.ehealthassist.Alarm;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.io.IOException;

import sg.com.ehealthassist.ehealthassist.DB.DBApptAlarm;
import sg.com.ehealthassist.ehealthassist.DB.DBBookInfo;
import sg.com.ehealthassist.ehealthassist.Models.Appointment.ApptAlarmLog;
import sg.com.ehealthassist.ehealthassist.Models.Appointment.BookInfo;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityAlarmshow extends Activity {
    private static ActivityAlarmshow inst;
    public TextView alarmdate, alarmtime, alarmclinic, alarmdoctor, patientnric;
    public ImageButton btnalarmok;
    Button btnstopalarm;
    BookInfo book_object;
    ApptAlarmLog alarminfo;

    String doc_name = "Doctor Name : ";
    String patient_nric = "Patient Nric : ";
    DBBookInfo dbbookinfo = null;
    DBApptAlarm dbapptalarm = null;
    private MediaPlayer mMediaPlayer;
    public static Vibrator vibrator;
    public static boolean active = false;
    int unit = 0;
    String settime = "";
    //region init()
    public static ActivityAlarmshow instance() {
        return inst;
    }

    @Override
    public void onStart() {
        super.onStart();
        inst = this;
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
        vibrator.cancel();
    }

    //endregion
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alarmshow);
        String long_id = getIntent().getStringExtra("service_long_obj");
        String short_id = getIntent().getStringExtra("service_short_obj");
        unit = getIntent().getIntExtra("uniqueInt", 0);
        settime = getIntent().getStringExtra("settime");
        dbbookinfo = new DBBookInfo(this);
        dbapptalarm = new DBApptAlarm(this);
        book_object = dbbookinfo.getBooklongId(long_id, short_id);
        alarminfo = dbapptalarm.getalarmlogInfo(long_id, short_id);
        findViewByid();
        setEvent();
        LoadData();
        startvibrate();
    }

    //region findviewbyid()
    public void findViewByid() {
        alarmdate = (TextView) findViewById(R.id.alarmdate);
        alarmtime = (TextView) findViewById(R.id.alarmtime);
        alarmclinic = (TextView) findViewById(R.id.alarmclinic);
        alarmdoctor = (TextView) findViewById(R.id.alarmdoctor);
        patientnric = (TextView) findViewById(R.id.patientnric);
        btnalarmok = (ImageButton) findViewById(R.id.btnalarmok);
        btnstopalarm = (Button) findViewById(R.id.btnstopalarm);
    }

    //endregion
    //region setEvent()
    public void setEvent() {
        btnstopalarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean hasfound = false;
                if (settime.equals("1w")) {
                    alarminfo.set_1week(false);
                } else if (settime.equals("2h")) {
                    alarminfo.set_2hr(false);
                } else if (settime.equals("1d")) {
                    alarminfo.set_1day(false);
                } else if (settime.equals("2d")) {
                    alarminfo.set_2days(false);
                }
                if (alarminfo.is_1day()) {
                    hasfound = true;
                }
                if (alarminfo.is_1week()) {
                    hasfound = true;
                }
                if (alarminfo.is_2days()) {
                    hasfound = true;
                }
                if (alarminfo.is_2hr()) {
                    hasfound = true;
                }
                if (hasfound) {
                    dbapptalarm.updateAlarmInfoBylongid(alarminfo);
                } else {
                    dbapptalarm.deleteAlarmbyid(book_object.getLong_id(), book_object.getShort_id());
                }
                Utils.CancleAlarm(getApplicationContext(), unit);

                stopvibrate();
                finish();
            }
        });
    }

    //endregion
    //region loadData()
    public void LoadData() {
        alarmdate.setText(Utils.BookDateFormat(book_object.getBook_date(), "yyyy-MM-dd'T'HH:mm:ss", "EEE, dd-MMM-yyyy"));
        alarmtime.setText(Utils.BookDateFormat(book_object.getBook_date(), "yyyy-MM-dd'T'HH:mm:ss", "hh:mm aa"));
        alarmclinic.setText(book_object.clinic_name);
        alarmdoctor.setText(doc_name + book_object.getDoctor_name());
        patientnric.setText(patient_nric + book_object.getRequestor_nric());
    }

    //endregion
    //region sound()
    private void playSound(Context context, Uri alert) {
        mMediaPlayer = new MediaPlayer();
        try {
            mMediaPlayer.setDataSource(context, alert);
            final AudioManager audioManager = (AudioManager) context
                    .getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IOException e) {
            System.out.println("OOPS");
        }
    }

    //endregion
    //region virbrate()
    public void startvibrate() {
        long pattern[] = {0, 100, 200, 300, 400};
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(pattern, 0);
    }

    public void stopvibrate() {
        vibrator.cancel();
    }

    //endregion
    //region menu()
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_alarmshow, menu);
        return true;
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        stopvibrate();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }
    //endregion
}
