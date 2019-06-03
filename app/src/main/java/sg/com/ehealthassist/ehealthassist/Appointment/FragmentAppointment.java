package sg.com.ehealthassist.ehealthassist.Appointment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyAppointmentView;
import sg.com.ehealthassist.ehealthassist.ActivitySplashScreen;
import sg.com.ehealthassist.ehealthassist.DB.DBBookInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Appointment.BookInfo;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 3/31/2016.
 */
public class FragmentAppointment extends Fragment {
    ListView _appoint_card_list;
    TextView txtdownloadaccount;
    DBBookInfo dbbookinfo = null;
    EAppointmentCardAdapter ecard_adapter;
    List<BookInfo> book_list = new ArrayList<BookInfo>();
    String usertoken, memberid;
    SharedPreferences pref_name = null;
    SharedPreferences pref_constant = null;
    RelativeLayout appt_loadingPanel;
    DBClinicInfo dbclinic = null;
    ImageButton btn_appt_refresh;
    Context _mcontext;
    final Handler handler = new Handler();
    int i =0;
    public FragmentAppointment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragmentappointment, container, false);
        init();
        findViewbyId(content);
        setEvent();
        loadData();
        handler.postDelayed(r, 1000);
        return content;
    }
    final Runnable r = new Runnable() {
            public void run() {
              //  if(ActivitySplashScreen.download_flag){
                if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                    appt_loadingPanel.setVisibility(View.GONE);
                    handler.removeCallbacks(r);
                }
                else {
                    appt_loadingPanel.setVisibility(View.VISIBLE);
                    txtdownloadaccount.setText(ActivitySplashScreen.download_count);
                }
                handler.postDelayed(this, 1000);
            }
        };

    //region init()
    public void init() {
        pref_name = getActivity().getSharedPreferences(getActivity().getString(R.string.preference_name), getActivity().MODE_PRIVATE);
        pref_constant = getActivity().getSharedPreferences(getString(R.string.preference_constant), getActivity().MODE_PRIVATE);
        usertoken = pref_name.getString(getString(R.string.pref_login_Access_token), "");
        memberid = pref_name.getString(getString(R.string.pref_login_memberId), "");
        _mcontext = getActivity();
        dbclinic = new DBClinicInfo(_mcontext);
    }

    //endregion
    //region findviewbyid()
    public void findViewbyId(View content) {
        _appoint_card_list = (ListView) content.findViewById(R.id._appoint_card_list);
        btn_appt_refresh = (ImageButton) content.findViewById(R.id.btn_appt_refresh);
        appt_loadingPanel = (RelativeLayout)content.findViewById(R.id.appt_loadingPanel);
        txtdownloadaccount = (TextView)content.findViewById(R.id.txtdownloadaccount);
    }

    //endregion
    //region setEvent()
    public void setEvent() {
        _appoint_card_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // if(ActivitySplashScreen.download_flag){
                if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                    appt_loadingPanel.setVisibility(View.GONE);
                    BookInfo binfo = book_list.get(position);
                    /* ClinicInfo info = dbclinic.getClinicInfo(binfo.getClinic_id());
                    if(info.get_id()>0){*/
                        Intent intent = new Intent(getActivity(), ActivityEApptCardDetail.class);
                        intent.putExtra("book", binfo);
                     //   startActivityForResult(intent, 1);
                        startActivity(intent);
                        getActivity().finish();

                   /* }
                    else{
                        Utils.showAlertDialog(_mcontext,"Add Clinic","Please insert Test Clinic via Setting");
                    }*/

                }
                else{
                    appt_loadingPanel.setVisibility(View.VISIBLE);
                    txtdownloadaccount.setText(ActivitySplashScreen.download_count);
                }


            }
        });
        btn_appt_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadData();
            }
        });
    }

    //endregion
    //region loadData()
    public void loadData() {
        if (!usertoken.equals("")) {
            if (Utils.hasInternetConnection(getActivity())) {
                if (pref_name.getBoolean(getString(R.string.pref_is_account_verified), false)) {
                   // RequestViewJson requestview = new RequestViewJson(memberid);
                    VolleyAppointmentView appt_view = new VolleyAppointmentView(_mcontext);
                    appt_view.GetApptViewJson(usertoken, new VolleyAppointmentView.VolleyCallback() {
                        @Override
                        public void onSuccess(List<BookInfo> lstbook) {
                            book_list = lstbook;
                            LoadListData();
                        }

                        @Override
                        public void onFail(String errrorcode, String message) {
                            // show data exits
                            Utils.showAlertDialog(getActivity(), errrorcode, message);
                        }
                    });
                } else {
                    Utils.showAlertDialog(getActivity(), getString(R.string.error_response_invalidtoken_message), getString(R.string.error_response_upload_profile_status_InvalidUserToken));
                    return;
                }
            } else {
                Utils.showInternetRequiredDialog(getActivity(), getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                return;
            }
        }
    }

    public void LoadListData() {
       // dbbookinfo = new DatabaseHandlerBookInfo(getActivity());
        //book_list = dbbookinfo.getRequestLog();
        ecard_adapter = new EAppointmentCardAdapter(getActivity(), book_list);
        _appoint_card_list.setAdapter(ecard_adapter);
        ecard_adapter.notifyDataSetChanged();

    }
    //endregion

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 1) {
            loadData();
        }
    }*/

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(r);
    }
}
