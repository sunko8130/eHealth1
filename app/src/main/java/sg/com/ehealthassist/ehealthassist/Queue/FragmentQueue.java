package sg.com.ehealthassist.ehealthassist.Queue;


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

import java.util.List;


import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyQueueView;
import sg.com.ehealthassist.ehealthassist.ActivitySplashScreen;
import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.DB.DBQueueRequest;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Queue.QueuelistView;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class FragmentQueue extends Fragment {
    SharedPreferences preferences = null;
    ListView lvtcontent;
    public  static List<QueuelistView> lstqueueview;
    DBQueueRequest upRQL;
    public static QueueAdapter adapter;
    ImageButton btn_queue_refresh;
    String usertoken;
    RelativeLayout queue_loadingPanel;
    Context _mcontext;
    final Handler handler = new Handler();
    DBClinicInfo dbclinic = null;
    SharedPreferences pref_constant = null;

    TextView txtdownloadaccount;

    public FragmentQueue() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragmentqueue, container, false);
        init();
        findViewById(content);
        setEvent();
        loadData();

        handler.postDelayed(r, 1000);
        return content;
    }
    final Runnable r = new Runnable() {
            public void run() {
               // if(ActivitySplashScreen.download_flag){
                if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                    queue_loadingPanel.setVisibility(View.GONE);
                    handler.removeCallbacks(r);
                }
                else {
                    queue_loadingPanel.setVisibility(View.VISIBLE);
                    txtdownloadaccount.setText(ActivitySplashScreen.download_count);
                }
                handler.postDelayed(this, 1000);
            }
    };
    //region init()
    public void init() {
        preferences = getActivity().getSharedPreferences(getActivity().getString(R.string.preference_name), _mcontext.MODE_PRIVATE);
        pref_constant = getActivity().getSharedPreferences(getString(R.string.preference_constant), getActivity().MODE_PRIVATE);
        usertoken = preferences.getString(getString(R.string.pref_login_Access_token), "");
        upRQL = new DBQueueRequest(getActivity());
        this.dbclinic = new DBClinicInfo(getActivity());
        _mcontext = getActivity();
    }
    //endregion
    //region findviewbyid()
    public void findViewById(View _vcontent) {
        lvtcontent = (ListView) _vcontent.findViewById(R.id.listcontent);
        btn_queue_refresh = (ImageButton) _vcontent.findViewById(R.id.btn_queue_refresh);
        queue_loadingPanel = (RelativeLayout)_vcontent.findViewById(R.id.queue_loadingPanel);
        txtdownloadaccount = (TextView)_vcontent.findViewById(R.id.txtdownloadaccount);
        deletebycurrentdate();
    }

    //endregion
    //region setEvent()
    public void setEvent() {
        lvtcontent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
             //   if(ActivitySplashScreen.download_flag){
                if(pref_constant.getBoolean(getString(R.string.pref_permanent_clinic_download_flag),false)){
                    queue_loadingPanel.setVisibility(View.GONE);
                    QueuelistView obj = lstqueueview.get(position);
                    ClinicInfo info = dbclinic.getClinicInfo(obj.getClinicid());
                    //if(info.get_id()>0){
                        Intent intent = new Intent(getActivity(), ActivityQueueDetailDialogs.class);
                        intent.putExtra("queue_object", obj);
                        intent.putExtra("list_position",position);
                        startActivity(intent);
                        getActivity().finish();
                   // }
                   /* else{
                        Utils.showAlertDialog(_mcontext,"Add Clinic","Please insert Test Clinic via Setting");
                    }*/

                }else{
                    queue_loadingPanel.setVisibility(View.VISIBLE);
                    txtdownloadaccount.setText(ActivitySplashScreen.download_count);
                }
            }

        });

        btn_queue_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }
    //endregion
    //region LoadData()
    public void loadData() {
        if (!usertoken.equals("")) {
            if (Utils.hasInternetConnection(getActivity())) {
                if (preferences.getBoolean(getString(R.string.pref_is_account_verified), false)) {

                    VolleyQueueView queue_view = new VolleyQueueView(getActivity());
                    queue_view.GetQueueViewJsonData(usertoken, new VolleyQueueView.VolleyCallback() {
                        @Override
                        public void onSuccess(List<QueuelistView> queuelist) {
                            lstqueueview = queuelist;
                            LoadDataList();
                        }

                        @Override
                        public void onFail(String errorcode, String errormessage) {
                            Utils.showAlertDialog(getActivity(), errorcode, errormessage);
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

    public void LoadDataList() {
        adapter = new QueueAdapter(getActivity(), lstqueueview);
        adapter.notifyDataSetChanged();
        lvtcontent.setAdapter(adapter);
       // lvtcontent.setItemsCanFocus(true);
    }
    //endregion
    //region deleterecord by Currentdate()
    public void deletebycurrentdate() {
        String currentdate = Utils.getcurrenttime("dd-MMM-yyyy hh:mm a");
        upRQL.deleteQueuebyCurrentDate(currentdate);
    }
    //endregion

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
