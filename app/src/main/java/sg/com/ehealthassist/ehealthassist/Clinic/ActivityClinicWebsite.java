package sg.com.ehealthassist.ehealthassist.Clinic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityClinicWebsite extends AppCompatActivity {
    WebView mwebview;
    public ProgressDialog progressBar;
    TextView txtwifioff, main_toolbar_title;
    ImageView img_wifioff;
    ImageButton toolbar_imgbutton_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_clinic_website);
        main_toolbar_title = (TextView) findViewById(R.id.main_toolbar_title);
        main_toolbar_title.setText(getString(R.string.title_activity_activity_clinic_website));

        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        findViewById();

        if (Utils.hasInternetConnection(getApplicationContext())) {
            txtwifioff.setVisibility(View.GONE);
            img_wifioff.setVisibility(View.GONE);
            progressBar = ProgressDialog.show(ActivityClinicWebsite.this, "", "Loading...");
            mwebview.setWebViewClient(new MyBrowser());
            mwebview.loadUrl("http://" + url);
        } else {
            txtwifioff.setVisibility(View.VISIBLE);
            img_wifioff.setVisibility(View.VISIBLE);
            mwebview.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }
        setEvent();
    }
    //region findViewbyid()
    public void findViewById() {
        txtwifioff = (TextView) findViewById(R.id.txtwifi_off);
        img_wifioff = (ImageView) findViewById(R.id.imgwifi_off);
        mwebview = (WebView) findViewById(R.id.clinicwebview);
        toolbar_imgbutton_back = (ImageButton) findViewById(R.id.toolbar_imgbackbutton);
    }
    //endregion
    public void setEvent() {
        toolbar_imgbutton_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    //region webview()
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
}
