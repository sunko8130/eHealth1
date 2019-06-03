package sg.com.ehealthassist.ehealthassist.CustomUI;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import java.io.File;
import java.io.IOException;

/**
 * Created by thazinhlaing on 2/7/17.
 */

public class DownloadPDF extends AsyncTask<String, Void, Void> {

    Context _mcontext;
    String folderpath;
    String filename;
    String usertoken;

    public DownloadPDF(Context _mcontext, String folder,String token) {
        this._mcontext = _mcontext;
        this.folderpath = folder;
        this.usertoken = token;
    }

    @Override
    protected Void doInBackground(String... strings) {
        String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
        String fileName = strings[1];  // -> maven.pdf
        String extStorageDirectory = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
        File folder = new File(extStorageDirectory, "ehealthassist/" + folderpath);
        folder.mkdirs();

        File pdfFile = new File(folder, fileName);
        this.filename = fileName;
        try {
            pdfFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileDownloader.downloadFile(fileUrl, pdfFile,usertoken);

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        view();
    }

    public void view() {
        File pdfFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ehealthassist/" + folderpath + "/" + filename);  // -> filename = maven.pdf
        Uri path = null;
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= 24) {
             path = FileProvider.getUriForFile(
                    _mcontext,
                    _mcontext.getApplicationContext()
                            .getPackageName() + ".provider", pdfFile);

        }else{
             path = Uri.fromFile(pdfFile);
        }
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        try {
            _mcontext.startActivity(pdfIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

