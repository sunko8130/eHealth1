package sg.com.ehealthassist.ehealthassist.eDocument;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import sg.com.ehealthassist.ehealthassist.DB.DBCertsCapture;
import sg.com.ehealthassist.ehealthassist.Models.EDocument.CertsCapture;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

import static android.content.Context.MODE_PRIVATE;


public class FragmentCert extends Fragment {

    ImageButton btnimagecapture;
    int camera_requestcode = 3;
    static String imgPath,imagecreatedate;
    DBCertsCapture dbCerts ;
    Context _mcontext;
    CertsCaptureAdapter certsAdapter ;
    ArrayList<CertsCapture> list_Certs;
    ListView lv_certscapture;
    SharedPreferences preferences = null;
    String memberid = "", usertoken = "";
    boolean isLoggedIn = false;

    public FragmentCert() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_cert, container, false);
        View content = inflater.inflate(R.layout.fragment_cert, container, false);
        _mcontext = getActivity();
        preferences = _mcontext.getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        memberid = preferences.getString(getString(R.string.pref_login_memberId), "");
        usertoken = preferences.getString(getString(R.string.pref_login_Access_token), "");
        isLoggedIn = preferences.getBoolean(getString(R.string.pref_is_logged_in), false);
        dbCerts = new DBCertsCapture(_mcontext);
        findViewbyId(content);
        setEvent();
        loadData();
        ((ActivityEDocument)getActivity()).setFragmentRefreshListener(new ActivityEDocument.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                Log.e("onrefesh","onRefresh certs");
                // Refresh Your Fragment
            }
        });
        return content;
    }
    public void findViewbyId(View view){
        btnimagecapture =(ImageButton)view.findViewById(R.id.imgbtn_certcapture);
        lv_certscapture = (ListView)view.findViewById(R.id.lv_certscapture);
    }

    public void setEvent(){
        btnimagecapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoggedIn) {
                    final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                    intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    getActivity().startActivityForResult(intent, camera_requestcode);
                }else {
                    Utils.showAlertDialoglogin(_mcontext,"Message",getActivity().getString(R.string.loginmes));
                }

            }
        });
        lv_certscapture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                CertsCapture obj = list_Certs.get(i);
                String path = Environment.getExternalStorageDirectory()+ "/ehealthassist/certs/image_"+obj.getCreatedate()+".png";
                Intent gointent = new Intent(_mcontext, ActivityShowCaptureImage.class);
                gointent.putExtra("Frag position",3);
                gointent.putExtra("ceratedate",obj.getCreatedate());
                gointent.putExtra("path", path);
                startActivity(gointent);
                getActivity().finish();
            }
        });
    }

    public Uri setImageUri() {
        // Store image in dcim
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
       // File file = new File(Environment.getExternalStorageDirectory() + "/ehealthassist/certs/", "image_" + timeStamp + ".png");
        File imagesFolder = new File(Environment.getExternalStorageDirectory()+"/ehealthassist/", "certs");
        imagesFolder.mkdirs();
        File image = new File(imagesFolder, "image_" + timeStamp + ".png");
        Uri imgUri = Uri.fromFile(image);
        this.imgPath = image.getAbsolutePath();
        this.imagecreatedate = timeStamp;
        return imgUri;
    }

    public static String getImagePath() {
        return imgPath;
    }
    public static String getImageCreatedate() {
        return imagecreatedate;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
       /* if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }
    public void loadData(){
        if(isLoggedIn){
            list_Certs = dbCerts.getCertsCaptureList(memberid);
            certsAdapter = new CertsCaptureAdapter(_mcontext,list_Certs);
            lv_certscapture.setAdapter(certsAdapter);
            certsAdapter.notifyDataSetChanged();
        }

    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
}
