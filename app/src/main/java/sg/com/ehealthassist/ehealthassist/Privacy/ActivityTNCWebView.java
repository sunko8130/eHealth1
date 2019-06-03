package sg.com.ehealthassist.ehealthassist.Privacy;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import sg.com.ehealthassist.ehealthassist.Account.ActivitySingup;
import sg.com.ehealthassist.ehealthassist.Models.Constant;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;
import sg.com.ehealthassist.ehealthassist.ActivitySetting;

public class ActivityTNCWebView extends AppCompatActivity {
    WebView mwebview;
    public ProgressDialog progressBar;
    TextView txtwifioff,main_toolbar_title;
    ImageView img_wifioff;
    ImageButton toolbar_imgbutton_back;
    String from_activity="";
    Context _mcontenxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_activity_tncweb_view);
        _mcontenxt = this;
        findViewById();
        setEvents();
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        from_activity = intent.getStringExtra("from_activity");
        if (Utils.hasInternetConnection(getApplicationContext())) {
            txtwifioff.setVisibility(View.GONE);
            img_wifioff.setVisibility(View.GONE);
            progressBar = ProgressDialog.show(_mcontenxt, "", "Loading...");
            mwebview.setWebViewClient(new MyBrowser());
            if (url.equals("tnc")) {
                mwebview.loadUrl(Constant.URL_TNC);
            } else {
                mwebview.loadUrl(Constant.URL_PRIVACY);
                main_toolbar_title.setText(getString(R.string.activity_tnc_title_privacy));
            }
        } else {
            txtwifioff.setVisibility(View.VISIBLE);
            img_wifioff.setVisibility(View.VISIBLE);
            mwebview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
    }
    //region findviewbyid()
    public void findViewById() {
        txtwifioff = (TextView) findViewById(R.id.txtwifi_off);
        img_wifioff = (ImageView) findViewById(R.id.imgwifi_off);
        mwebview = (WebView) findViewById(R.id.tncwebview);
        main_toolbar_title = (TextView)findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.activity_tnc_title_term_conditions));
        toolbar_imgbutton_back = (ImageButton)findViewById(R.id.toolbar_imgbackbutton);
    }
    //endregion
    public void setEvents(){
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                returnback();
            }
        });
    }

    //region webviewclient()
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progressBar.show();
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, final String url) {
            progressBar.dismiss();
        }
    }
    //endregion
    //region onKeyDown()
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            returnback();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void returnback(){
        Intent intent = null;
        switch (from_activity){
            case "ActivitySignup" :
                intent = new Intent(getApplicationContext(), ActivitySingup.class);
                intent.putExtra("fromactivity", "ActivityTNCWebView");
                break;
            case "ActivitySetting":
                intent = new Intent(getApplicationContext(), ActivitySetting.class);
                intent.putExtra("fromactivity", "ActivityTNCWebView");
                break;
            default:
                break;
        }
        if (intent != null) {
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }

        this.finish();
    }


}
