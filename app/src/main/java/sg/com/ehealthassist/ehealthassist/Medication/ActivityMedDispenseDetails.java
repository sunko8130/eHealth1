package sg.com.ehealthassist.ehealthassist.Medication;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.CustomUI.CustomListView;
import sg.com.ehealthassist.ehealthassist.DB.DBMedDispense;
import sg.com.ehealthassist.ehealthassist.DB.DBMedReminder;
import sg.com.ehealthassist.ehealthassist.DB.DBMedicalItem;
import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalDispense;
import sg.com.ehealthassist.ehealthassist.Models.Medical.MedicalItem;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityMedDispenseDetails extends AppCompatActivity {
    TextView main_toolbar_title, txt_dtlpatinetname, txt_dtlpatinetnrictype, txt_dtlpatinetnric,
            txt_dtlpatinetdob, txt_dtlvisitno, txt_dtlvisitdate, txt_dtlclinicname;
    ImageButton toolbar_imgbutton_back;
    public static CustomListView lvmedicationitems;
   public static DBMedicalItem dbmeditem;
    DBMedDispense dbmeddispense;
    DBMedReminder dbmedreminder;
    public static Context _mcontext;
    public static ArrayList<MedicalItem> lstmeditems;
    MedicalDispense medDispenseObj;
    public static MedicationItemsAdapter adapter;
    public  static  ScrollView scrollitems;
    String[] nric_type;
    ImageButton btnimg_deletemedical;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_med_dispense_details);
        _mcontext = this;
         dbmeditem = new DBMedicalItem(_mcontext);
        dbmeddispense = new DBMedDispense(_mcontext);
        dbmedreminder = new DBMedReminder(_mcontext);
        Intent getobject = getIntent();
        medDispenseObj = getobject.getParcelableExtra("dispense");
        findviewById();
        getmedicalItems(medDispenseObj.getVisitNo());
        nric_type = getResources().getStringArray(R.array.array_nric_type);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
             // if(Thread.interrupted()){
             //      Thread.yield();
             // }else{
                   MedicalReminderTime medReminder = new MedicalReminderTime(_mcontext);
                   medReminder.refreshMedicalReminder();
              // }
            }
        };
        new Thread(runnable).start();
        loadData();
        setEvent();
    }

    public void findviewById() {
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText("Medication Details");
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        lvmedicationitems = (CustomListView) findViewById(R.id.lvmedication_item);
        scrollitems = (ScrollView) findViewById(R.id.scrollitems);
        txt_dtlpatinetname = (TextView) findViewById(R.id.txt_dtlpatinetname);
        txt_dtlpatinetnrictype = (TextView) findViewById(R.id.txt_dtlpatinetnrictype);
        txt_dtlpatinetnric = (TextView) findViewById(R.id.txt_dtlpatinetnric);
        txt_dtlpatinetdob = (TextView) findViewById(R.id.txt_dtlpatinetdob);
        txt_dtlvisitno = (TextView) findViewById(R.id.txt_dtlvisitno);
        txt_dtlvisitdate = (TextView) findViewById(R.id.txt_dtlvisitdate);
        txt_dtlclinicname = (TextView) findViewById(R.id.txt_dtlclinicname);
        btnimg_deletemedical = (ImageButton) findViewById(R.id.btnimg_deletemedical);
    }

    public void loadData() {
        try {
            String dob = Utils.GridviewChangefromat(medDispenseObj.getDOB(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
            String visitdate = Utils.GridviewChangefromat(medDispenseObj.getVisitDate(), "yyyy-MM-dd'T'HH:mm:ss", "dd-MMM-yyyy");
            String str_nrictype = nric_type[Integer.parseInt(medDispenseObj.getNrictype())];
            txt_dtlpatinetname.setText(medDispenseObj.getPatientName());
            txt_dtlpatinetnrictype.setText(str_nrictype);
            txt_dtlpatinetnric.setText(medDispenseObj.getNric());
            txt_dtlpatinetdob.setText(dob);
            txt_dtlvisitno.setText(medDispenseObj.getVisitNo());
            txt_dtlvisitdate.setText(visitdate);
            txt_dtlclinicname.setText(medDispenseObj.getClinicName());

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void getmedicalItems(String visitno) {
        lstmeditems = dbmeditem.getmedicalItems(visitno);
        adapter = new MedicationItemsAdapter(_mcontext, lstmeditems);
        lvmedicationitems.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        lvmedicationitems.setExpanded(true);
        scrollitems.smoothScrollTo(0, 0);
    }

    public void setEvent() {
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });
        //region setReminder

        btnimg_deletemedical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //delete dispense & item & reminder
                showdialog();
            }
        });
        //endregoin
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        returnback();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void returnback() {
        Intent intent = new Intent(getApplicationContext(), ActivityMedicalDispense.class);
        intent.putExtra("from_activity", "ActivityMedDispenseDetails");
        startActivity(intent);
        finish();
    }

    public void showdialog(){
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage(getString(R.string.msgbox_delete_medication_msg))
                .setTitle("Delete Medication")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try{
                            String visitno = txt_dtlvisitno.getText().toString();
                            dbmeditem.deletemedicalitem(visitno);
                            dbmeddispense.deletemedicaldispense(visitno);
                            returnback();

                        }catch (Exception ex){
                            ex.printStackTrace();
                        }
                    }
                });
        alertBuilder.create().show();
        return;
    }
}

