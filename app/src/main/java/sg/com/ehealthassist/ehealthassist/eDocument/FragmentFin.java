package sg.com.ehealthassist.ehealthassist.eDocument;

import android.app.Dialog;
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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestInvoiceJson;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyInvoiceReceipt;
import sg.com.ehealthassist.ehealthassist.DB.DBFinCapture;
import sg.com.ehealthassist.ehealthassist.Models.EDocument.FinCapture;
import sg.com.ehealthassist.ehealthassist.Models.Payment.InvoiceReceipt;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.Payment.ActivityEReceiptDetails;
import sg.com.ehealthassist.ehealthassist.R;
import static android.content.Context.MODE_PRIVATE;


public class FragmentFin extends Fragment //implements ActivityEDocument.Updateable
{

    TextView txterrormessage;
    ImageButton  imgbtn_fincapture;
    ListView lv_invoice_receipt;
    Context _mcontext;
    SharedPreferences preferences = null;
    List<InvoiceReceipt> lstinvoice, lstlatest_load=new ArrayList<InvoiceReceipt>();
    List<String> lstsortName = new ArrayList<String>();
    ArrayList<FinCapture> list_FIN=new ArrayList<FinCapture>();
    DBFinCapture db_fin;
    int camera_requestcode = 4;
    static String imgPath, imagecreatedate;
    String memberid = "", usertoken = "";
    boolean isLoggedIn = false;
    List<InvoiceFin>  multiObj = new ArrayList<InvoiceFin>();
    InvoiceFinAdapter infinadapter;

    public FragmentFin() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View content = inflater.inflate(R.layout.fragment_fin, container, false);
        _mcontext = getActivity();
        preferences = _mcontext.getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        memberid = preferences.getString(getString(R.string.pref_login_memberId), "");
        usertoken = preferences.getString(getString(R.string.pref_login_Access_token), "");
        isLoggedIn = preferences.getBoolean(getString(R.string.pref_is_logged_in), false);
        db_fin = new DBFinCapture(_mcontext);
        findviewbyid(content);
        setEvent();
        LoadInvoiceReceipt();

        if (isLoggedIn) {
            list_FIN = db_fin.getFinCaptureList(memberid);
        }
        getInvoiceFin();

        ((ActivityEDocument)getActivity()).setFragmentRefreshListener(new ActivityEDocument.FragmentRefreshListener() {
            @Override
            public void onRefresh() {
                LoadInvoiceReceipt();
                if (isLoggedIn) {
                    list_FIN = db_fin.getFinCaptureList(memberid);
                }
                getInvoiceFin();
            }
        });
        return content;
    }
    //region find & event
    public void findviewbyid(View view) {
        txterrormessage = (TextView) view.findViewById(R.id.txterrormessage);
        lv_invoice_receipt = (ListView) view.findViewById(R.id.lv_invoicereceipt);
        imgbtn_fincapture = (ImageButton) view.findViewById(R.id.imgbtn_fincapture);
      }

    public void setEvent() {
        lv_invoice_receipt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               InvoiceFin obj = multiObj.get(position);
                if(obj.getIsObject().equals("I")){
                    InvoiceReceipt object  = (InvoiceReceipt)obj.getMultiobj();
                    Intent intent = new Intent(_mcontext, ActivityEReceiptDetails.class);
                    intent.putExtra("InvoiceObject", object);
                    startActivity(intent);
                    getActivity().finish();
                }else{
                    FinCapture objfin = (FinCapture)obj.getMultiobj();
                    String path = Environment.getExternalStorageDirectory() + "/ehealthassist/fin/image_" + objfin.getCreatedate() + ".png";
                    Intent gointent = new Intent(_mcontext, ActivityShowCaptureImage.class);
                    gointent.putExtra("Frag position", 0);
                    gointent.putExtra("ceratedate", objfin.getCreatedate());
                    gointent.putExtra("path", path);
                    startActivity(gointent);
                    getActivity().finish();
                }
            }
        });

        imgbtn_fincapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoggedIn) {
                    final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());
                    intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                    getActivity().startActivityForResult(intent, camera_requestcode);
                }
                else{
                    Utils.showAlertDialoglogin(_mcontext,"Message",getActivity().getString(R.string.loginmes));
                }
            }
        });
    }
    //endregion

    //region Invoice & receipt
    public void LoadInvoiceReceipt() {
        if (Utils.hasInternetConnection(_mcontext)) {
            if (isLoggedIn) {
                txterrormessage.setVisibility(View.GONE);

                RequestInvoiceJson param = new RequestInvoiceJson(memberid, "", "", "", "");
                String obj_param = param.objecttoJson();
                Log.e("param", obj_param);
                VolleyInvoiceReceipt v_receipt = new VolleyInvoiceReceipt(_mcontext);
                v_receipt.GetPaymentInvoiceJson(usertoken, param.StringtoJsonObject(obj_param), new VolleyInvoiceReceipt.VolleyCallback() {
                    @Override
                    public void onSuccess(ArrayList<InvoiceReceipt> listinvoice, ArrayList<String> lstnricname) {
                        lstinvoice = new ArrayList<InvoiceReceipt>();
                        lstlatest_load = new ArrayList<InvoiceReceipt>();
                        lstsortName.clear();
                        lstinvoice = listinvoice;
                        lstlatest_load = listinvoice;
                        lstsortName = lstnricname;
                        getInvoiceFin();
                        LoadData();
                    }

                    @Override
                    public void onFail(String errorcode, String errormessage) {
                        txterrormessage.setVisibility(View.VISIBLE);
                      }
                });
            } else {
                txterrormessage.setVisibility(View.VISIBLE);
               }
        } else {
            Utils.showInternetRequiredDialog(_mcontext, getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
            return;
        }

    }

    public Uri setImageUri() {
        // Store image in dcim
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        File imagesFolder = new File(Environment.getExternalStorageDirectory()+"/ehealthassist/", "fin");
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
    //endregion

    //region Load Sort Data
   public void getInvoiceFin(){
       try{
           multiObj = new ArrayList<InvoiceFin>();
           if(lstlatest_load.size()>0){
               for(InvoiceReceipt obj : lstlatest_load){
                   Date convertdate =  Utils.reminderTimeFormat(obj.getInvoicedate(),"yyyy-MM-dd","yyyyMMddHHmmss");
                   InvoiceFin objin = new InvoiceFin(obj,convertdate,"I");
                   multiObj.add(objin);
               }
           }
           if(list_FIN.size()>0){
               for(FinCapture obj : list_FIN){
                   Date convertdate =  Utils.reminderTimeFormat(obj.getCreatedate(),"yyyyMMddHHmmss","yyyyMMddHHmmss");
                   InvoiceFin objfin = new InvoiceFin(obj,convertdate,"F");
                   multiObj.add(objfin);
               }
           }
           if(multiObj.size()>0){
               Collections.sort(multiObj,Collections.reverseOrder());
           }

       }catch (Exception ex){
           ex.printStackTrace();
       }
   }

   public void LoadData(){
       infinadapter = new InvoiceFinAdapter(_mcontext,multiObj);
       lv_invoice_receipt.setAdapter(infinadapter);
       infinadapter.notifyDataSetChanged();
   }
   //endregion

    //region onAttach & onDetach
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    //endregion
}
