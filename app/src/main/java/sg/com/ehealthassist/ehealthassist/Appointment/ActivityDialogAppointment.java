package sg.com.ehealthassist.ehealthassist.Appointment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import sg.com.ehealthassist.ehealthassist.Queue_Appt.ActivityQueue_Appoint;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityDialogAppointment extends Activity {
    TextView txtappt_result, txtappt_message,txt_appt_bookmessage;
    ImageView img_appt;
    Button btn_appt_result;
    Context _mcontext;
    int result;
    String message,message_desc,message_status="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_appointment);
        _mcontext = this;
        findViewById();
        Intent i = getIntent();
        result = i.getIntExtra("result", 0);
        message = i.getStringExtra("message");
        message_desc = i.getStringExtra("returnmessage");
        message_status = i.getStringExtra("returnmesdesc");
        LoadData();
        setEvent();
    }
    //region findviewbyid()
    public void findViewById() {
        txtappt_result = (TextView) findViewById(R.id.txt_appt_result);
        txtappt_message = (TextView) findViewById(R.id.txt_appt_bookid);
        txt_appt_bookmessage = (TextView) findViewById(R.id.txt_appt_bookmessage);
        img_appt = (ImageView) findViewById(R.id.img_appt_result);
        btn_appt_result = (Button) findViewById(R.id.btn_appt_result);
        txt_appt_bookmessage.setVisibility(View.INVISIBLE);
    }

    //endregion
    //region setEvent()
    public void setEvent() {
        btn_appt_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (result) {
                    case 0:
                        finish();
                        break;
                    case 1:
                        finish();
                        Intent intent = new Intent(_mcontext, ActivityQueue_Appoint.class);
                        intent.putExtra("from","ActivityDialogAppointment");
                        startActivity(intent);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //endregion
    //region loaddata()
    public void LoadData() {
        switch (result) {
            case 0:
                txtappt_result.setText("Appointment Fail");
                img_appt.setImageResource(R.drawable.ic_clear_white_24dp);
                txtappt_message.setText(message);
                txt_appt_bookmessage.setVisibility(View.GONE);
                break;
            case 1:
                txtappt_result.setText("Appointment Requested");
                img_appt.setImageResource(R.drawable.ic_done_white_24dp);
                txtappt_message.setText("Appointment ID : " + message);
                //txt_appt_bookmessage.setText(getString(R.string.appt_appointment_message));
                txt_appt_bookmessage.setText(message_desc);
                txt_appt_bookmessage.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    //endregion
    //region menu()
   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_dialog_appointment, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //endregion
}
