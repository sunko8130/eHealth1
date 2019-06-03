package sg.com.ehealthassist.ehealthassist.GDAA;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.JGson;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

import static android.content.Context.MODE_PRIVATE;

public class GDAAConnect {
    static String TAG = "Error";
    private static GoogleApiClient mGoogleApiClient;
    private static GDAAConnect.ConnectCBs mConnCBs;
    public   static String contentsAsString ="";
    public static String createjsonfileString = "";
    public static  String login_memberid = "";
    public static SharedPreferences preferences;
    public static DBMedProfile dbHandlerMedProfile;
    public static Activity _mactivity_show;
    public static String _mactivityname ;

    GDAAConnect(){}
    public interface  ConnectCBs{
        void onConnFail(ConnectionResult connResult);
        void onConnOK();
    }

    public static boolean init(Activity act){
        if (act != null) {
            String email = GDAAUT.AM.getEmail();
            preferences = GDAAUT.acx.getSharedPreferences(GDAAUT.acx.getString(R.string.preference_name), MODE_PRIVATE);
            login_memberid = preferences.getString(GDAAUT.acx.getString(R.string.pref_login_memberId), "");
            dbHandlerMedProfile = new DBMedProfile(GDAAUT.acx);//UT.lg("emil " + email);
            if (email != null) try {
                mConnCBs = (GDAAConnect.ConnectCBs) act;
                mGoogleApiClient = new GoogleApiClient.Builder(act)
                        .addApi(Drive.API)
                        .addScope(Drive.SCOPE_FILE)
                        .addScope(Drive.SCOPE_APPFOLDER)
                        .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                            @Override
                            public void onConnectionSuspended(int i) {
                            }
                            @Override
                            public void onConnected(Bundle bundle) {
                                mConnCBs.onConnOK();
                            }
                        })
                        .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(ConnectionResult connectionResult) {
                                mConnCBs.onConnFail(connectionResult);
                            }
                        })
                        .setAccountName(email)
                        .build();
                return true;
            } catch (Exception e) {GDAAUT.le(e);}
        }
        return false;
    }
    public static void connect() {
        if (GDAAUT.AM.getEmail() != null && mGoogleApiClient != null && !mGoogleApiClient.isConnecting() && !mGoogleApiClient.isConnected()) {  //UT.lg("conn");
            mGoogleApiClient.connect();
        }

    }
    public static void disconnect() {
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }
    //RequestSync
    public static void onrequestSync(){
        try{
            Drive.DriveApi.requestSync(mGoogleApiClient).setResultCallback(syncCallback);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }

    }

    public static ResultCallback<Status> syncCallback = new ResultCallback<Status>() {
        @Override
        public void onResult(Status status) {
            if (!status.isSuccess()) {
                Log.e("sync error",status.getStatusMessage().toString());
                return;
            }

            // ReadContentFile("home",null);
        }
    };
    //CreateFile
    public static void onClickCreateFile(String jsonstr) {
        createjsonfileString = jsonstr;
        Drive.DriveApi.newDriveContents(mGoogleApiClient).setResultCallback(driveContentsCallback);
    }
    public static ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback = new ResultCallback<DriveApi.DriveContentsResult>() {
        @Override
        public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
            if (driveContentsResult.getStatus().isSuccess()) {
                CreateFileOnGoogleDrive(driveContentsResult);
            }else{
                Log.e("sync error",driveContentsResult.getStatus().getStatusMessage().toString());
            }
        }
    };
    public static void CreateFileOnGoogleDrive(DriveApi.DriveContentsResult result) {
        final DriveContents driveContents = result.getDriveContents();
        new Thread() {
            @Override
            public void run() {
                String title_name = "appconfig"+login_memberid+".txt";
                DeleteContentFile();
                OutputStream outputStream = driveContents.getOutputStream();
                Writer writer = new OutputStreamWriter(outputStream);
                try {
                    writer.write(createjsonfileString);
                    writer.close();
                } catch (IOException e) {
                    Log.e(TAG, e.getMessage());
                }
                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                        .setTitle(title_name)
                        .setMimeType("text/plain")
                        .build();

                Drive.DriveApi.getAppFolder(mGoogleApiClient)
                        .createFile(mGoogleApiClient, changeSet, driveContents)
                        .setResultCallback(fileCallback);
            }
        }.start();
    }
    public  static ResultCallback<DriveFolder.DriveFileResult> fileCallback = new ResultCallback<DriveFolder.DriveFileResult>() {
        @Override
        public void onResult(@NonNull DriveFolder.DriveFileResult driveFileResult) {
            if (driveFileResult.getStatus().isSuccess()) {
                // ReadContentFile();
                preferences.edit().putBoolean(GDAAUT.acx.getString(R.string.pref_update_profile_flag),false);
                //Toast.makeText(GDAAUT.acx, "file created: " + "" +
                //      driveFileResult.getDriveFile().getDriveId(), Toast.LENGTH_LONG).show();
                Log.e("driverid",  driveFileResult.getDriveFile().getDriveId()+"");
            }
            else{
                Log.e("sync error",driveFileResult.getStatus().getStatusMessage().toString());
            }
            return;
        }
    };
    //ReadContentFile
    public static void ReadContentFile(String _from_act,Activity _from_context){
        Log.e("Read","Read Content");
        _mactivity_show = _from_context;
        _mactivityname = _from_act;
        Drive.DriveApi.query(mGoogleApiClient,FileQuerySearch()).setResultCallback(metadataReadCallback);

    }
    public static Query FileQuerySearch(){
        String title_name = "appconfig"+login_memberid+".txt";
        Query query = new Query.Builder().addFilter(Filters.and(
                Filters.eq(SearchableField.MIME_TYPE, "text/plain"),
                Filters.contains(SearchableField.TITLE, title_name))).build();
        return  query;
    }
    public static ResultCallback<DriveApi.MetadataBufferResult> metadataReadCallback = new ResultCallback<DriveApi.MetadataBufferResult>() {
        @Override
        public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
            Log.e("arrived","status");
            if(metadataBufferResult != null && metadataBufferResult.getStatus().isSuccess()){
                MetadataBuffer mdb = null;
                mdb = metadataBufferResult.getMetadataBuffer();
                if(mdb != null)
                    if(mdb.getCount()>0){
                        Metadata md = mdb.get(0);
                        if(md != null || md.isDataValid() ){
                            Log.e("md driveid", md.getDriveId().toString() + ","+ md.getTitle());
                            DriveFile file = Drive.DriveApi.getFile(mGoogleApiClient, md.getDriveId());
                            file.open(mGoogleApiClient,DriveFile.MODE_READ_ONLY,null).setResultCallback(contentsOpenCallback);
                        }

                    }
                 for (Metadata md : mdb){
                        Log.e("status message","success");
                        if(md == null ||!md.isDataValid() ) continue;
                        Log.e("md driveid", md.getDriveId().toString() + ","+ md.getTitle());
                        DriveFile file = Drive.DriveApi.getFile(mGoogleApiClient, md.getDriveId());
                        file.open(mGoogleApiClient,DriveFile.MODE_READ_ONLY,null).setResultCallback(contentsOpenCallback);
                  }
                mdb.close();
            }
            else{
                GDAAConnect.connect();
                ReadContentFile(_mactivityname,_mactivity_show);

                String status = metadataBufferResult.getStatus().getStatusMessage();
                Log.e("status message",status);
            }
        }
    };
    public static  ResultCallback<DriveApi.DriveContentsResult> contentsOpenCallback = new ResultCallback<DriveApi.DriveContentsResult>() {
        @Override
        public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
            if(driveContentsResult.getStatus().isSuccess()){
                DriveContents contents = driveContentsResult.getDriveContents();
                Log.e("arrived content","content");
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(contents.getInputStream()));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    contentsAsString = builder.toString();
                    Log.e("contentsAsString",contentsAsString);
                    if(matchProfilewithDrive()){
                        if(_mactivityname.equals("profilelist") || _mactivityname.equals("setting")){
                            Utils.googledriveDialogbox(_mactivityname,_mactivity_show,login_memberid);
                        }
                    }

                }catch (Exception ex){
                    ex.printStackTrace();
                }

            }else{
                Log.e("file read message", driveContentsResult.getStatus().getStatusMessage() );
            }
        }
    };
    //DeleteFile
    public static boolean DeleteContentFile(){
        Drive.DriveApi.getAppFolder(mGoogleApiClient).queryChildren(mGoogleApiClient,FileQuerySearch()).setResultCallback(metadataDeleteCallback);

        //  Drive.DriveApi.getAppFolder(mGoogleApiClient).listChildren(mGoogleApiClient).setResultCallback(metadataDeleteCallback);
        return false;
    }
    public static ResultCallback<DriveApi.MetadataBufferResult> metadataDeleteCallback = new ResultCallback<DriveApi.MetadataBufferResult>() {
        @Override
        public void onResult(@NonNull DriveApi.MetadataBufferResult metadataBufferResult) {
            if(metadataBufferResult != null && metadataBufferResult.getStatus().isSuccess()){
                MetadataBuffer mdb = null;
                mdb = metadataBufferResult.getMetadataBuffer();
                if(mdb != null)
                    for (Metadata md : mdb){
                        if(md == null ||!md.isDataValid() ) continue;
                        Log.e("delete file", md.getDriveId().toString() + ","+ md.getTitle());
                        DriveFile file = Drive.DriveApi.getFile(mGoogleApiClient, md.getDriveId());
                        file.delete(mGoogleApiClient);
                    }
                mdb.close();
            }
            else{
                String status = metadataBufferResult.getStatus().getStatusMessage();
                Log.e("status message",status);
            }
        }
    };

    public  static  boolean matchProfilewithDrive() {
        boolean is_sync = false;
        int data_count  = 0;
        JGson json = new JGson();

        ArrayList<MedicalProfile> loacal_profilelist = dbHandlerMedProfile.getMedProfilebyMemberid(login_memberid);
        ArrayList<MedicalProfile> drive_profilelist = json.fromjsonToProfileObject(contentsAsString);

        if(contentsAsString.equals("")){
            if(loacal_profilelist.size()>0){
                // is_sync=true;
            }
        }
        else {
            for (MedicalProfile _adriveprofile : drive_profilelist) { // not include id,flag upload columns
                if (_adriveprofile.getMemberid().equals(login_memberid)) {
                    data_count++;
                    MedicalProfile getprofilefromdb = dbHandlerMedProfile.getMedProfilebyNRIC_memberid(_adriveprofile.getNric_type(),
                            _adriveprofile.getNric(), _adriveprofile.getMemberid());

                    if (_adriveprofile.getClinic_id() != getprofilefromdb.getClinic_id()) {
                        is_sync = true;
                    }
                    if (_adriveprofile.getNric_type() != getprofilefromdb.getNric_type()) {
                        is_sync = true;
                    }
                    if (!_adriveprofile.getNric().equals(getprofilefromdb.getNric())) {
                        is_sync = true;
                    }
                    if (!_adriveprofile.getNric_name().equals(getprofilefromdb.getNric_name())) {
                        is_sync = true;
                    }
                    if (_adriveprofile.getGender() != getprofilefromdb.getGender()) {
                        is_sync = true;
                    }
                    if (_adriveprofile.getLanguage() != getprofilefromdb.getLanguage()) {
                        is_sync = true;
                    }
                    if (!_adriveprofile.getNationality().equals(getprofilefromdb.getNationality())) {
                        is_sync = true;
                    }
                    if (!_adriveprofile.getDob().equals(getprofilefromdb.getDob())) {
                        is_sync = true;
                    }
                    if (!_adriveprofile.getBlock_no().equals(getprofilefromdb.getBlock_no())) {
                        is_sync = true;
                    }
                    if (!_adriveprofile.getStreet().equals(getprofilefromdb.getStreet())) {
                        is_sync = true;
                    }
                    if (!_adriveprofile.getBuilding_name().equals(getprofilefromdb.getBuilding_name())) {
                        is_sync = true;
                    }
                    if (!_adriveprofile.getUnit_no().equals(getprofilefromdb.getUnit_no())) {
                        is_sync = true;
                    }
                    if (!_adriveprofile.getPostal_code().equals(getprofilefromdb.getPostal_code())) {
                        is_sync = true;
                    }
                    if (!_adriveprofile.getTel1().equals(getprofilefromdb.getTel1())) {
                        is_sync = true;
                    }
                    if (!_adriveprofile.getTel2().equals(getprofilefromdb.getTel2())) {
                        is_sync = true;
                    }
                    if (!_adriveprofile.getEmail().equals(getprofilefromdb.getEmail())) {
                        is_sync = true;
                    }
                    if (!_adriveprofile.getAllergy().equals(getprofilefromdb.getAllergy())) {
                        is_sync = true;
                    }
                    if (!_adriveprofile.getMemberid().equals(getprofilefromdb.getMemberid())) {
                        is_sync = true;
                    }
                    if (_adriveprofile.getRelation() != getprofilefromdb.getRelation()) {
                        is_sync = true;
                    }
                    if (_adriveprofile.getMarried_staus() != getprofilefromdb.getMarried_staus()) {
                        is_sync = true;
                    }
                    if (_adriveprofile.getMember() != getprofilefromdb.getMember()) {
                        is_sync = true;
                    }
                }
            }
        }
        if(!is_sync){
            if(data_count > 0){
                if(data_count != loacal_profilelist.size()){
                    is_sync = true;
                }
            }
        }

        return  is_sync;
    }

    public static void uploadprofileToDrive(ArrayList<MedicalProfile> uploadlist){
        JGson str_jsong = new JGson();
        String profile_json = str_jsong.fromProfileObjectToJson(uploadlist);
        Log.e("upload profile",profile_json);
        onClickCreateFile(profile_json);
    }

    public static void downloadprofileFromDrive(){
        JGson json = new JGson();
        ReadContentFile("download",null);
        ArrayList<MedicalProfile> drive_profilelist = json.fromjsonToProfileObject(contentsAsString);
        if(!contentsAsString.equals("")){
            Log.e("medprofile list", drive_profilelist.size()+"" );
            dbHandlerMedProfile.deleteprofilebyMemberid(login_memberid);
            for (MedicalProfile _adriveprofile : drive_profilelist){
                if (_adriveprofile.getMemberid().equals(login_memberid)) {
                    _adriveprofile.getNric_name();
                    Log.e("medprofile name",  _adriveprofile.getNric_name()+" , " + _adriveprofile.getId() );
                    dbHandlerMedProfile.updateMedicalProfilebydrive(_adriveprofile);
                }
            }
        }
    }
}
