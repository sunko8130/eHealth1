package sg.com.ehealthassist.ehealthassist.Account;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import sg.com.ehealthassist.ehealthassist.Fragment.FragCreateAccount;
import sg.com.ehealthassist.ehealthassist.Fragment.FragLogIn;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityCreateAccount extends AppCompatActivity implements FragCreateAccount.PushFromFragCreateAcccount {
    Button button_new_account,button_login,button_skip;
    Toolbar toolbar;
    LinearLayout frag_Holder;
    FragCreateAccount fragCreateAccount;
    FragmentManager fragmentManager;
    CardView cardview_create_account;
    String from = "";
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_account);
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        frag_Holder = (LinearLayout)findViewById(R.id.frag_holder);
        fragmentManager = getSupportFragmentManager();

      /*  findViewByid();
        callAnimation();
        setEvent();*/
        //windowTransition();
        Intent intent = getIntent();
        from = intent.getStringExtra("from");
        /*if (from.equals("splash_screen"))
        {
            callFragCreateAccount();
        }
        else if (from.equals("forgotpwd"))
        {
            callFragLogin();
        }*/
        if (from.equals("forgotpwd") || from.equals("login"))
        {
            callFragLogin();
        }
        else {
            callFragCreateAccount();
        }

    }

    private void callAnimation() {
        Animation animation = AnimationUtils.loadAnimation(ActivityCreateAccount.this, R.anim.top_to_center);
        frag_Holder.startAnimation(animation);
    }

    private void callAnimationLogin() {
        Animation animation = AnimationUtils.loadAnimation(ActivityCreateAccount.this, R.anim.right_to_left_login);
        frag_Holder.startAnimation(animation);
    }

    private void callFragLogin() {
        FragLogIn fragLogIn = (FragLogIn)fragmentManager.findFragmentByTag("FragLogIn");
        if (fragLogIn == null)
        {
            fragLogIn = new FragLogIn();
        }
        callAnimationLogin();
        fragmentManager.beginTransaction().replace(R.id.frag_holder, fragLogIn, "FragLogIn").commit();
    }

    private void callFragCreateAccount() {
        fragCreateAccount = (FragCreateAccount) fragmentManager.findFragmentByTag("FragCreateAccount");
        if (fragCreateAccount == null)
        {
            fragCreateAccount = new FragCreateAccount();
        }
        callAnimation();
        fragmentManager.beginTransaction().replace(R.id.frag_holder, fragCreateAccount, "FragCreateAccount").commit();
    }

    @Override
    public void sendForFragLogin(String fromactivity, int cid) {
        FragLogIn fragLogIn = new FragLogIn();
        callAnimationLogin();
        fragmentManager.beginTransaction().replace(R.id.frag_holder, fragLogIn, "FragLogIn").commit();
        fragLogIn.receiveDatafromFragCreateAccount(fromactivity, cid);
    }

    /*@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void windowTransition() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.setDuration(500);
        slide.setInterpolator(new LinearInterpolator());
        getWindow().setReenterTransition(slide);
        getWindow().setExitTransition(slide);
    }

    private void callAnimation() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        animation.setDuration(2000);
        cardview_create_account.startAnimation(animation);
    }

    public void findViewByid(){
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        button_login = (Button)findViewById(R.id.btn_gotologin);
        button_new_account = (Button)findViewById(R.id.btn_create_account);
        button_skip = (Button)findViewById(R.id.btn_skip);
        cardview_create_account = (CardView)findViewById(R.id.card_view_activity_create_account);
    }
    public void setEvent(){
        View.OnClickListener button_OnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                String caller = v.getResources().getResourceEntryName(v.getId());
                switch (caller){
                    case "btn_gotologin":
                        intent = new Intent(getApplicationContext(), ActivityLogIn.class);
                        intent.putExtra("from", "ActivityCreateAccount");
                        intent.putExtra("CID", 0);
                        break;
                    case "btn_create_account":
                        intent = new Intent(getApplicationContext(), ActivitySingup.class);
                        intent.putExtra("fromactivity","ActivityCreateAccount");
                        break;
                    case "btn_skip":
                        intent = new Intent(getApplicationContext(), ActivityHome1.class);
                        intent.putExtra("successRecord", 0);
                        break;
                    default:
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                    finish();
                    *//*ActivityOptions activityOptions = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        activityOptions = ActivityOptions.makeSceneTransitionAnimation(ActivityCreateAccount.this);
                        startActivity(intent, activityOptions.toBundle());
                        finish();
                    }
                    else {
                        startActivity(intent);
                        finish();
                    }*//*
                }
            }
        };
        button_login.setOnClickListener(button_OnClickListener);
        button_new_account.setOnClickListener(button_OnClickListener);
        button_skip.setOnClickListener(button_OnClickListener);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/
}
