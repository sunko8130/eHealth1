package sg.com.ehealthassist.ehealthassist.eDocument;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.CustomUI.TouchImageView;
import sg.com.ehealthassist.ehealthassist.DB.DBCertsCapture;
import sg.com.ehealthassist.ehealthassist.DB.DBFinCapture;
import sg.com.ehealthassist.ehealthassist.DB.DBLABCapture;
import sg.com.ehealthassist.ehealthassist.DB.DBRADCapture;
import sg.com.ehealthassist.ehealthassist.Models.EDocument.CertsCapture;
import sg.com.ehealthassist.ehealthassist.Models.EDocument.FinCapture;
import sg.com.ehealthassist.ehealthassist.Models.EDocument.LABCapture;
import sg.com.ehealthassist.ehealthassist.Models.EDocument.RADCapture;
import sg.com.ehealthassist.ehealthassist.Models.EDocument.Rating;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityShowCaptureImage extends AppCompatActivity {
 //   ImageView img_showimage;
    TouchImageView img_showimage;
    Bitmap myBitmap = null;
    TextView main_toolbar_title;
    ImageButton toolbar_imgbutton_back;
    Context _context;
    int postion = 0;String path="";
    ArrayList<Rating> lst_rate;
    String[] arry_ques;
    RatingAdapter adapter;
    DBFinCapture dbfin;
    DBLABCapture dblad;
    DBRADCapture dbrad;
    DBCertsCapture dbcerts;
    String createdate;
    ImageButton imgbtn_ratedelete;
    String memberid = "";
    SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_show_capture_image);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText("Screen Capture");
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
        preferences = getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        memberid = preferences.getString(getString(R.string.pref_login_memberId), "");
        _context = this;
       // img_showimage = (ImageView) findViewById(R.id.img_showimage);
        img_showimage = (TouchImageView) findViewById(R.id.img_showimage);
        imgbtn_ratedelete = (ImageButton) findViewById(R.id.imgbtn_ratedelete);
        dbfin = new DBFinCapture(_context);
        dblad = new DBLABCapture(_context);
        dbcerts = new DBCertsCapture(_context);
        dbrad = new DBRADCapture(_context);
        Intent getIntent = getIntent();
        path = getIntent.getStringExtra("path");
        postion = getIntent.getIntExtra("Frag position", 0);
        createdate = getIntent.getStringExtra("ceratedate");

        File imgFile = new File(path);
        if (imgFile.exists()) {
            myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            Bitmap imgBitmap = rotateImage(path);
            img_showimage.setImageBitmap(imgBitmap);
        }
        img_showimage.setMaxZoom(4f);
        setEvent();
        callratingpage();
    }

    public void setEvent() {
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnback();
            }
        });
        imgbtn_ratedelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteDialog("Delete");
            }
        });
    }

    public Bitmap rotateImage(String photoPath) {
        Bitmap rotatedBitmap = null;
        try {
            ExifInterface ei = new ExifInterface(photoPath);
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(myBitmap, 90);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(myBitmap, 180);
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(myBitmap, 270);
                    break;
                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = myBitmap;
            }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return rotatedBitmap;
    }

    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }

    public void returnback() {
        Intent eintent = new Intent(_context, ActivityEDocument.class);
        eintent.putExtra("from", "ActivityShowCaptureImage");
        eintent.putExtra("Frag position", postion);
        startActivity(eintent);
        finish();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void callratingpage() {
        boolean isshow = false;
        switch (postion) {

            case 0:
                //fin
                FinCapture finobj = dbfin.getfinbyCreateDate(createdate);
                isshow = finobj.isreview();

                break;
            case 1:
                LABCapture labobj = dblad.getLabbyCreateDate(createdate);
               // isshow = labobj.isreview();
                isshow = true; // to hide rating
                //lab
                break;
            case 2:
                RADCapture radobj = dbrad.getRadbyCreateDate(createdate);
               // isshow = radobj.isreview();
                isshow = true;//to hide rating
                //rad
                break;
            case 3:
                CertsCapture certsobj = dbcerts.getCertbyCreateDate(createdate);
               // isshow = certsobj.isreview();
                isshow = true;//to hide rating
                //certs
                break;
            default:
                break;

        }
        if (!isshow) {
            Intent intent = new Intent(this, ActivityRating.class);
            intent.putExtra("createdate", createdate);
            intent.putExtra("update position", postion);
            intent.putExtra("from", "ActivityShowCaptureImage");
            startActivity(intent);
        }
    }

    public void showDeleteDialog(String title) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setMessage("Are you sure to delete?")
                .setTitle(title)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (postion) {
                            case 0:
                                dbfin.deleteRatebycreatedate(createdate,memberid);
                                break;
                            case 1:
                                dblad.deleteRatebycreatedate(createdate,memberid);
                                break;
                            case 2:
                                dbrad.deleteRatebycreatedate(createdate,memberid);
                                break;
                            case 3:
                                dbcerts.deleteRatebycreatedate(createdate,memberid);
                                break;
                            default:
                                break;

                        }
                        deleteimagefile();
                        returnback();
                    }
                });
        alertBuilder.create().show();
        return;
    }

    public void deleteimagefile(){
        File imgFile = new File(path);
        if (imgFile.exists()) {
          imgFile.delete();
        }
    }

}



