package sg.com.ehealthassist.ehealthassist.Medication;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import sg.com.ehealthassist.ehealthassist.R;

public class ActivityAlarmReminder  extends Activity {
    Button btn_reminder_ok;
    TextView txtmedmessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_reminder);
    }
    public  void findViewById(){
        btn_reminder_ok = (Button)findViewById(R.id.btn_reminder_ok);
        txtmedmessage =(TextView)findViewById(R.id.txtmedmessage);
    }
    public void setEvent(){
        btn_reminder_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


}
