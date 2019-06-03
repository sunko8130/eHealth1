package sg.com.ehealthassist.ehealthassist.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import sg.com.ehealthassist.ehealthassist.Account.ActivityCreateAccount;
import sg.com.ehealthassist.ehealthassist.Account.ActivitySingup;
import sg.com.ehealthassist.ehealthassist.ActivityHome;
import sg.com.ehealthassist.ehealthassist.R;

/**
 * Created by User on 12/1/2017.
 */

public class FragCreateAccount extends Fragment {

    Button button_new_account,button_login,button_skip;
    CardView cardview_create_account;
    PushFromFragCreateAcccount pushFromFragCreateAcccount;
    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_create_account, container, false);
        findViewByid();
        setEvent();
        return view;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void windowTransition() {
        Slide slide = new Slide();
        slide.setSlideEdge(Gravity.END);
        slide.setDuration(500);
        slide.setInterpolator(new LinearInterpolator());
        /*getWindow().setReenterTransition(slide);
        getWindow().setExitTransition(slide);*/
    }
/*
    private void callAnimation() {
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.top_to_center);
        cardview_create_account.startAnimation(animation);
    }*/

    public void findViewByid(){
        button_login = (Button)view.findViewById(R.id.btn_frag_goto_login);
        button_new_account = (Button)view.findViewById(R.id.btn_frag_create_account);
        button_skip = (Button)view.findViewById(R.id.btn_frag_skip);
        cardview_create_account = (CardView)view.findViewById(R.id.card_view_frag_create_account);
    }
    public void setEvent(){
        View.OnClickListener button_OnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                String caller = v.getResources().getResourceEntryName(v.getId());
                pushFromFragCreateAcccount = (PushFromFragCreateAcccount) getActivity();
                switch (caller){

                    case "btn_frag_goto_login":
                        /*Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.right_to_left);
                        button_login.startAnimation(animation2);*/
                        pushFromFragCreateAcccount.sendForFragLogin("ActivityCreateAccount", 0);
                        /*FragLogIn fragLogIn = new FragLogIn();
                        getFragmentManager().beginTransaction().replace(R.id.frag_holder, fragLogIn).commit();*/

                        /*intent = new Intent(getApplicationContext(), ActivityLogIn.class);
                        intent.putExtra("from", "ActivityCreateAccount");
                        intent.putExtra("CID", 0);*/
                        break;
                    case "btn_frag_create_account":
                        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.right_to_left);
                        button_new_account.startAnimation(animation);
                        intent = new Intent(getActivity(), ActivitySingup.class);
                        intent.putExtra("fromactivity","ActivityCreateAccount");
                        break;
                    case "btn_frag_skip":
                        intent = new Intent(getActivity(), ActivityHome.class);
                        intent.putExtra("successRecord", 0);
                        Animation animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.right_to_left);
                        button_skip.startAnimation(animation1);
                        break;
                    default:
                        break;
                }
                if (intent != null) {
                    startActivity(intent);
                    getActivity().finish();

                    /*ActivityOptions activityOptions = null;
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                        activityOptions = ActivityOptions.makeSceneTransitionAnimation(ActivityCreateAccount.this);
                        startActivity(intent, activityOptions.toBundle());
                        finish();
                    }
                    else {
                        startActivity(intent);
                        finish();
                    }*/
                }
            }
        };
        button_login.setOnClickListener(button_OnClickListener);
        button_new_account.setOnClickListener(button_OnClickListener);
        button_skip.setOnClickListener(button_OnClickListener);
    }

    /*public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //finish();
            getActivity().finish();
        }
        return super.onKeyDown(keyCode, event);
    }*/

    public interface PushFromFragCreateAcccount{
        void sendForFragLogin(String fromactivity, int cid);
    }
}
