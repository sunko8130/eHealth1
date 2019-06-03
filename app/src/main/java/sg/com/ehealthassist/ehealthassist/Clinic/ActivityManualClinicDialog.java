package sg.com.ehealthassist.ehealthassist.Clinic;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityManualClinicDialog extends Activity {
    Button btnmanual;
    EditText edtclinicid;
    TextView txtcheck;
    DBClinicInfo databaseHandlerClinicInfo;
    ClinicInfo findclinic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_manual_clinic_dialog);
        databaseHandlerClinicInfo = new DBClinicInfo(getApplicationContext());
        findViewsById();
        setEvent();
    }
    //region findViewsById()
    public void findViewsById() {
        btnmanual = (Button) findViewById(R.id.btnmanual);
        edtclinicid = (EditText) findViewById(R.id.editTextClinicId);
        edtclinicid.setFilters(new InputFilter[]{new InputFilter.AllCaps()});
        txtcheck = (TextView) findViewById(R.id.txterror);
    }
    //endregion
    //region setEvent()
    public void setEvent() {
        btnmanual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean hasError = false;
                String manual_clinicid = edtclinicid.getText().toString();
                if (!(edtclinicid.length() > 0) ) {
                    edtclinicid.setError(getText((R.string.err_invalid_pin_clinic_hecode_length)));
                    hasError = true;

                } else {
                    findclinic= databaseHandlerClinicInfo.getClinicInfobyHECODE(manual_clinicid);
                    if(findclinic==null){
                        edtclinicid.setError(getString(R.string.err_invalid_clinic_id));
                        hasError = true;
                    }

                }
                if (hasError) {
                    return;
                }
                if (!manual_clinicid.equals("")) {

                    if (findclinic == null) {
                        txtcheck.setVisibility(View.VISIBLE);
                    }
                    else {
                        txtcheck.setVisibility(View.GONE);
                        Intent intent_clinic_detail = new Intent(getApplicationContext(), ActivityClinicDeatail.class);
                        intent_clinic_detail.putExtra("hecode", manual_clinicid);
                        startActivity(intent_clinic_detail);
                        finish();
                    }
                }
            }
        });
    }
    //endregion


}
