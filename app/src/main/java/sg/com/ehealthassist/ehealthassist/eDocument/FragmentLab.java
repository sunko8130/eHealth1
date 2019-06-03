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

import sg.com.ehealthassist.ehealthassist.DB.DBLABCapture;
import sg.com.ehealthassist.ehealthassist.Models.EDocument.LABCapture;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

import static android.content.Context.MODE_PRIVATE;


public class FragmentLab extends Fragment {

    ImageButton btnimagecapture;
    int camera_requestcode = 1;
    static String imgPath, imagecreatedate;
    DBLABCapture dblab;
    Context _mcontext;
    LabCaptureAdapter labAdapter;
    ArrayList<LABCapture> list_LAB;
    ListView lv_labcapture;
    SharedPreferences preferences = null;
    String memberid = "", usertoken = "";
    boolean isLoggedIn = false;

    public FragmentLab() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View content = inflater.inflate(R.layout.fragment_fragment_lab, container, false);
        _mcontext = getActivity();
        dblab = new DBLABCapture(_mcontext);
        preferences = _mcontext.getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        memberid = preferences.getString(getString(R.string.pref_login_memberId), "");
        usertoken = preferences.getString(getString(R.string.pref_login_Access_token), "");
        isLoggedIn = preferences.getBoolean(getString(R.string.pref_is_logged_in), false);

        findViewbyId(content);
        setEvent();
        loadData();
        ((ActivityEDocument)getActivity()).setFragmentRefreshListener(new ActivityEDocument.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                Log.e("onrefesh","onRefresh lab");
                // Refresh Your Fragment
            }
        });
        return content;
    }

    public void findViewbyId(View view) {
        btnimagecapture = (ImageButton) view.findViewById(R.id.imgbtn_labcapture);
        lv_labcapture = (ListView) view.findViewById(R.id.lv_labcapture);
    }

    public void setEvent() {
        btnimagecapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoggedIn) {
                    final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                    intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    getActivity().startActivityForResult(intent, camera_requestcode);
                } else {
                    Utils.showAlertDialoglogin(_mcontext, "Message", getActivity().getString(R.string.loginmes));
                }

            }
        });
        lv_labcapture.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                LABCapture obj = list_LAB.get(i);
                String path = Environment.getExternalStorageDirectory() + "/ehealthassist/Lab/image_" + obj.getCreatedate() + ".png";
                Intent gointent = new Intent(_mcontext, ActivityShowCaptureImage.class);
                gointent.putExtra("Frag position", 1);
                gointent.putExtra("ceratedate", obj.getCreatedate());
                gointent.putExtra("path", path);
                startActivity(gointent);
                getActivity().finish();
            }
        });
    }

    public Uri setImageUri() {
        // Store image in dcim
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File imagesFolder = new File(Environment.getExternalStorageDirectory()+"/ehealthassist/", "Lab");
        imagesFolder.mkdirs();
        File image = new File(imagesFolder, "image_" + timeStamp + ".png");
     //   File file = new File(Environment.getExternalStorageDirectory() + "/ehealthassist/Lab/", "image_" + timeStamp + ".png");
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void loadData() {
        if (isLoggedIn) {
            list_LAB = dblab.getLabCaptureList(memberid);
            labAdapter = new LabCaptureAdapter(_mcontext, list_LAB);
            lv_labcapture.setAdapter(labAdapter);
            labAdapter.notifyDataSetChanged();

        }
    }


}
