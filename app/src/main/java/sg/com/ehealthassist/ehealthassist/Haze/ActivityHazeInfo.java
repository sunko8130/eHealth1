package sg.com.ehealthassist.ehealthassist.Haze;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyPM2_5Update;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyPSIUpdate;
import sg.com.ehealthassist.ehealthassist.ActivityHome;
import sg.com.ehealthassist.ehealthassist.ActivityHome1;
import sg.com.ehealthassist.ehealthassist.Models.Haze.PM2_5Region;
import sg.com.ehealthassist.ehealthassist.Models.Haze.PSIHR_Region;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityHazeInfo extends AppCompatActivity {

    Context context;
    RelativeLayout rlnorth, rlcenter, rlsouth, rleast, rlwest;
    TextView main_toolbar_title, txthazeheader, txttimestamp;
    TextView txt_north_value, txt_east_value, txt_center_value, txt_south_value, txt_west_value;
    ImageButton btn_psi3hr, btn_psi24hr, btn_pm2_5hr;
    LinearLayout llpis, llpm;
    ImageButton toolbar_imgbutton_back;
    int hr = 24;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_haze_info);
        context = this;
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_haze_info));

        findviewbyid();
        setEvent();
        setviewBackground("24hr");
        getpsi();

    }
    //region findviewbyid()
    public void findviewbyid() {
        txthazeheader = (TextView) findViewById(R.id.txthazeheader);
        txttimestamp = (TextView) findViewById(R.id.txttimestamp);
        rlnorth = (RelativeLayout) findViewById(R.id.rlnorth);
        rlsouth = (RelativeLayout) findViewById(R.id.rlsouth);
        rlcenter = (RelativeLayout) findViewById(R.id.rlcenter);
        rleast = (RelativeLayout) findViewById(R.id.rleast);
        rlwest = (RelativeLayout) findViewById(R.id.rlwest);
        btn_psi3hr = (ImageButton) findViewById(R.id.button_psi3hr);
        btn_psi24hr = (ImageButton) findViewById(R.id.button_psi24hr);
        btn_pm2_5hr = (ImageButton) findViewById(R.id.button_pm2_5hr);
        txt_north_value = (TextView) findViewById(R.id.txt_north_value);
        txt_east_value = (TextView) findViewById(R.id.txt_east_value);
        txt_center_value = (TextView) findViewById(R.id.txt_center_value);
        txt_south_value = (TextView) findViewById(R.id.txt_south_value);
        txt_west_value = (TextView) findViewById(R.id.txt_west_value);
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        llpis = (LinearLayout) findViewById(R.id.lly_psi_description);
        llpm = (LinearLayout) findViewById(R.id.lly_pm2_5_description);
    }

    //endregion
    //region setEvent
    public void setviewBackground(String color) {
        RoundRectShape rect = new RoundRectShape(
                new float[]{15, 15, 15, 15, 15, 15, 15, 15},
                null,
                null);
        ShapeDrawable bg = new ShapeDrawable(rect);
        switch (color) {
            case "pm":
                bg.getPaint().setColor(getResources().getColor(R.color.bg_pm25_color));
                break;
            case "3hr":
                bg.getPaint().setColor(getResources().getColor(R.color.bg_psi3hr_color));
                break;
            case "24hr":
                bg.getPaint().setColor(getResources().getColor(R.color.bg_psi24hr_color));
                break;
        }
        rlnorth.setBackgroundDrawable(bg);
        rlsouth.setBackgroundDrawable(bg);
        rlcenter.setBackgroundDrawable(bg);
        rlwest.setBackgroundDrawable(bg);
        rleast.setBackgroundDrawable(bg);
    }

    public void setEvent() {
        btn_pm2_5hr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llpis.setVisibility(View.GONE);
                llpm.setVisibility(View.VISIBLE);
                txthazeheader.setText("Haze PM 2.5");
                setviewBackground("pm");
               /* AsyncPM2_5 async = new AsyncPM2_5(context);
                async.execute("");*/
                getpm2_5();
            }
        });
        btn_psi3hr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txthazeheader.setText("Haze PSI 3 Hours");
                hr = 3;
                getpsi();
                setviewBackground("3hr");
            }
        });
        btn_psi24hr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txthazeheader.setText("Haze PSI 24 Hours");
                hr = 24;
                getpsi();
                setviewBackground("24hr");
            }
        });
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backpage();
            }
        });
    }
    //endregion

    public void getpsi() {
        llpis.setVisibility(View.VISIBLE);
        llpm.setVisibility(View.GONE);
        if (Utils.hasInternetConnection(context)) {
            VolleyPSIUpdate _vpsi = new VolleyPSIUpdate(context);
            _vpsi.GetPSIUpdateRequest(new VolleyPSIUpdate.VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<PSIHR_Region> listpsi) {
                    processFinishPSI(listpsi);
                }
                @Override
                public void onFail(String errorcode, String errormessage) {
                    Utils.showAlertDialog(context,errorcode,errormessage);
                }
            });
        } else {
            Utils.showInternetRequiredDialog(this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
    }
    public void getpm2_5(){
        if (Utils.hasInternetConnection(context)) {
            VolleyPM2_5Update _vpm2_5 = new VolleyPM2_5Update(context);
            _vpm2_5.GetPM2_5UpdateRequest(new VolleyPM2_5Update.VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<PM2_5Region> listpm2_5) {
                    processFinish(listpm2_5);
                }
                @Override
                public void onFail(String errorcode, String errormessage) {
                    Utils.showAlertDialog(context,errorcode,errormessage);
                }
            });
        } else {
            Utils.showInternetRequiredDialog(this, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }
    }
    public void processFinish(ArrayList<PM2_5Region> result) {
        Log.e("enter","processFinish");
        if (result.size() > 0) {
            String timestamp = result.get(0).getRecord().getTimestamp();
            String changedateformat = ChangeDateFormat(timestamp);
            txttimestamp.setText("at " + changedateformat);
            for (int i = 0; i < result.size(); i++) {
                PM2_5Region pm2_5object = result.get(i);
                int values = Integer.parseInt(pm2_5object.getRecord().getReading().getValue());
                switch (pm2_5object.getId()) {
                    case "rNO":
                        txt_north_value.setText(pm2_5object.getRecord().getReading().getValue());

                        break;
                    case "rCE":
                        txt_center_value.setText(pm2_5object.getRecord().getReading().getValue());
                        break;
                    case "rEA":
                        txt_east_value.setText(pm2_5object.getRecord().getReading().getValue());
                        break;
                    case "rSO":
                        txt_south_value.setText(pm2_5object.getRecord().getReading().getValue());
                        break;
                    case "rWE":
                        txt_west_value.setText(pm2_5object.getRecord().getReading().getValue());
                        break;
                    default:
                        break;
                }
            }
        }
    }

    public void processFinishPSI(ArrayList<PSIHR_Region> result) {
        if (result.size() > 0) {
            String timestamp = result.get(0).getRecord().getTimestamp();

            String changedateformat = ChangeDateFormat(timestamp);
            txttimestamp.setText("at " + changedateformat);

            for (int i = 0; i < result.size(); i++) {
                PSIHR_Region psi_object = result.get(i);
                String values_data = "";
                String values_type = "";
                int position = -1;

                for (int r = 0; r < psi_object.getRecord().getReading().length;r++){
                    String type = psi_object.getRecord().getReading()[r].getType();

                    if(hr == 3){
                        txthazeheader.setText("Haze PSI 3 Hours");
                        if(type.equals("NPSI_NP25_3HR")){
                            position = r;
                            break;
                        }
                    }else {
                        txthazeheader.setText("Haze PSI 24 Hours");
                        if(type.equals("NPSI")){
                            position = r;

                            break;
                        }
                    }
                }
                if(position >= 0){
                    values_data = psi_object.getRecord().getReading()[position].getValue();
                    values_type = psi_object.getRecord().getReading()[position].getType();
                }
                switch (psi_object.getId()) {
                    case "rNO":
                        txt_north_value.setText(values_data);

                        break;
                    case "rCE":
                        txt_center_value.setText(values_data);

                        break;
                    case "rEA":
                        txt_east_value.setText(values_data);

                        break;
                    case "rSO":
                        txt_south_value.setText(values_data);

                        break;
                    case "rWE":
                        txt_west_value.setText(values_data);

                        break;
                    default:
                        break;
                }
            }
        }
    }
    //region ChangeDate format
    public static String ChangeDateFormat(String inputdate) {

        String inputPattern = "yyyyMMddHHmmss";
        String outputPattern = "hh:mm a";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(inputdate);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }

    //endregion
    public void backpage() {
        finish();
        Intent intent_scan = new Intent(context, ActivityHome.class);
        intent_scan.putExtra("successRecord", 0);
        context.startActivity(intent_scan);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backpage();
    }
    //endregion
}
