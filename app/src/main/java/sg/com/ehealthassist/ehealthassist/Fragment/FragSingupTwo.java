package sg.com.ehealthassist.ehealthassist.Fragment;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestPcodejson;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyCountryCode;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyPCodeAddress;
import sg.com.ehealthassist.ehealthassist.Account.ActivitySingup;
import sg.com.ehealthassist.ehealthassist.CountryCode.CountryCodeAdapter;
import sg.com.ehealthassist.ehealthassist.CustomUI.NothingSelectedSpinnerAdapter;
import sg.com.ehealthassist.ehealthassist.Models.Profile.CountryCode;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by User on 12/4/2017.
 */

public class FragSingupTwo extends Fragment implements View.OnClickListener {

    String check_ph = "";
    ImageView img_frag_leftarrow, img_frag_rightarrow, img_postal_search;
    Button btn_fragtwo_continue, btn_dropdown_code;
    EditText et_fragtwo_mobileno, TextViewEmail, editTextBlockNo, editTextUnitNo,
            Textviewpostalcode, editTextStreet, editTextBuildingName,
            edt_address1, edt_address2, edt_address3, edt_address4;
    LinearLayout linearLayout, llmultiaddressView, rlpostalcode;

    String[] nric_type,nric_type_passport, marrital_status,arr_nationality;
    ListView lvcountrycode;
    ArrayList<CountryCode> lst_countrycode = new ArrayList<>();
    CountryCodeAdapter adpt_countrycode;
    ArrayAdapter<CharSequence> adapterMarried, adapterNricType, adapterGender,adapterNationality;

    SharedPreferences preferences = null;

    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_singup_two, container, false);
        preferences = getActivity().getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        findViewById();
        //callvolleyCountryCode();
        checkLayout();
        setEvent();
        setControlEvent();
        return view;
    }

    private void checkLayout() {
        String mobile_iso = preferences.getString(getString(R.string.pref_signup_mobile_iso), "");
        if (mobile_iso.equals("SG") || mobile_iso.equals(""))
        {
            linearLayout.setVisibility(View.VISIBLE);
            rlpostalcode.setVisibility(View.VISIBLE);
            img_postal_search.setVisibility(View.VISIBLE);
            llmultiaddressView.setVisibility(View.GONE);
        }
        else {
            llmultiaddressView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            rlpostalcode.setVisibility(View.GONE);
        }
    }

    public void receiveFromOne(String phno){
        check_ph = phno;
    }

    private void findViewById() {
        /*et_fragtwo_mobileno = view.findViewById(R.id.et_fragtwo_mobileno2);*/

        editTextBlockNo = view.findViewById(R.id.et_fragtwo_block2);
        editTextUnitNo = view.findViewById(R.id.et_fragtwo_unitno2);
        Textviewpostalcode = view.findViewById(R.id.et_fragtwo_postal_code2);
        editTextStreet = view.findViewById(R.id.et_fragtwo_streetname2);
        editTextBuildingName = view.findViewById(R.id.et_fragtwo_building_name2);
        edt_address1 = view.findViewById(R.id.edt_address1);
        edt_address2 = view.findViewById(R.id.edt_address2);
        edt_address3 = view.findViewById(R.id.edt_address3);
        edt_address4 = view.findViewById(R.id.edt_address4);
        linearLayout = view.findViewById(R.id.llContactView);
        llmultiaddressView = view.findViewById(R.id.llmultiaddressView);
        rlpostalcode = view.findViewById(R.id.rlpostalcode);
        img_postal_search = view.findViewById(R.id.img_postal_search);

        img_frag_leftarrow = view.findViewById(R.id.img_fragtwo_left_arrow);
        img_frag_rightarrow = view.findViewById(R.id.img_fragtwo_right_arrow);
        btn_fragtwo_continue = view.findViewById(R.id.btn_fragtwo_continue2);
        /*btn_dropdown_code = view.findViewById(R.id.btn_fragtwo_dropdown_code);*/

    }

    private void setEvent() {
        img_frag_rightarrow.setOnClickListener(this);
        img_frag_leftarrow.setOnClickListener(this);
        btn_fragtwo_continue.setOnClickListener(this);
        //btn_dropdown_code.setOnClickListener(this);
    }

    private void setControlEvent(){

        //region postalcode textview action listener
        Textviewpostalcode.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_GO) {
                    getAddress();
                }
                return false;
            }

        });
        //endregion

        //region image postal search action listener
        img_postal_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddress();
            }
        });
        //endregion

        //region view Address Text onFocus Change Listener
        View.OnFocusChangeListener txtFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String controlKey = null;
                String controlValue = null;
                boolean isValid = true;
                switch (v.getId()) {
                    case R.id.et_fragtwo_block2:
                        controlKey = getString(R.string.pref_signup_blockno);
                        controlValue = editTextBlockNo.getText().toString();
                        break;
                    case R.id.et_fragtwo_streetname2:
                        controlKey = getString(R.string.pref_signup_street);
                        controlValue = editTextStreet.getText().toString();
                        break;
                    case R.id.et_fragtwo_building_name2:
                        controlKey = getString(R.string.pref_signup_building);
                        controlValue = editTextBuildingName.getText().toString();
                        break;
                    case R.id.et_fragtwo_unitno2:
                        controlKey = getString(R.string.pref_signup_unitno);
                        controlValue = editTextUnitNo.getText().toString();
                        break;
                    case R.id.edt_address1 :
                        controlKey = getString(R.string.pref_signup_address1);
                        controlValue = edt_address1.getText().toString();
                        break;
                    case R.id.edt_address2 :
                        controlKey = getString(R.string.pref_signup_address2);
                        controlValue = edt_address2.getText().toString();
                        break;
                    case R.id.edt_address3 :
                        controlKey = getString(R.string.pref_signup_address3);
                        controlValue = edt_address3.getText().toString();
                        break;
                    case R.id.edt_address4 :
                        controlKey = getString(R.string.pref_signup_address4);
                        controlValue = edt_address4.getText().toString();
                        break;
                    case R.id.TextViewEmail:
                        controlKey = getString(R.string.pref_signup_email);
                        controlValue = TextViewEmail.getText().toString();
                        if (!controlValue.equals("")) {
                            if (!Utils.isValidEmail(controlValue)) {
                                isValid = false;
                                TextViewEmail.setError(getString(R.string.err_invalid_email));
                            }
                        }
                        break;
                }
                if (!hasFocus && isValid) {
                    preferences.edit().putString(controlKey, controlValue).commit();
                }
            }
        };
        //endregion

        //region TextChange Lisetner
        int count = linearLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = linearLayout.getChildAt(i);
            if (v instanceof EditText) {
                final EditText txt = (EditText) v;
                txt.setOnFocusChangeListener(txtFocusChangeListener);
            }
        }
        int count_address = llmultiaddressView.getChildCount();
        for(int i =0 ;i<count_address;i++){
            View v = llmultiaddressView.getChildAt(i);
            if(v instanceof EditText){
                final  EditText edt = (EditText) v;
                edt.setOnFocusChangeListener(txtFocusChangeListener);
            }
        }

        Textviewpostalcode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                img_postal_search.setVisibility(View.VISIBLE);
                String controlKey = getString(R.string.pref_signup_postalcode);
                String controlValue = Textviewpostalcode.getText().toString();
                preferences.edit().putString(controlKey, controlValue).commit();
            }
        });

    }

    //region Postal Code API
    public void getAddress() {
        String pcode_str = Textviewpostalcode.getText().toString();
        if (pcode_str.length() == 6) {
            img_postal_search.setVisibility(View.VISIBLE);
            RequestPcodejson pcode_json = new RequestPcodejson(pcode_str);
            JSONObject json = pcode_json.StringtoJsonObject(pcode_json.objecttoJson());
            if (Utils.hasInternetConnection(getContext())) {
                VolleyPCodeAddress v_pcode = new VolleyPCodeAddress(getActivity());
                v_pcode.GetPCodeAddressRequest(json, new VolleyPCodeAddress.VolleyCallback() {
                    @Override
                    public void onSuccess(String blockno, String street, String building) {
                        editTextBlockNo.setText(blockno);
                        editTextStreet.setText(street);
                        editTextBuildingName.setText(building);
                        editTextBlockNo.setError(null);
                        editTextStreet.setError(null);
                        editTextBuildingName.setError(null);
                        preferences.edit().putString(getString(R.string.pref_signup_blockno), blockno)
                                .putString(getString(R.string.pref_signup_street), street)
                                .putString(getString(R.string.pref_signup_building), building)
                                .apply();
                    }
                    @Override
                    public void onFail(String errorcode, String errormessage) {
                        Utils.showAlertDialog(getContext(), errorcode, errormessage);
                    }
                });
            } else {
                Utils.showInternetRequiredDialog(getContext(), getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                return;
            }
        } else {
            Textviewpostalcode.setError("Required");
            img_postal_search.setVisibility(View.GONE);
        }
    }
    //endregion

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.img_fragtwo_left_arrow:
                FragSingupOne fragSingupOne = (FragSingupOne) getFragmentManager().findFragmentByTag("FragSingupOne");
                if (fragSingupOne == null)
                {
                    fragSingupOne = new FragSingupOne();
                }
                getFragmentManager().beginTransaction().replace(R.id.frag_signup_holder, fragSingupOne, "FragSingupOne").commit();
                break;

            case R.id.img_fragtwo_right_arrow:
                callFragSingupThree();
                /*if (!validateallData())
                {
                    callFragSingupThree();
                }*/
                break;

            case R.id.btn_fragtwo_continue2:
                callFragSingupThree();
                /*if (!validateallData())
                {
                    callFragSingupThree();
                }*/
                break;
        }
    }

    /*private Boolean validateallData()
    {
        Boolean hasError = false;

        String telph = preferences.getString(getString(R.string.pref_signup_mobile), "");
        Log.d("telphtest", telph + "no" + et_fragtwo_mobileno.length() + "sec");
        if (telph.equals("") || et_fragtwo_mobileno.length() == 0)
        {
            hasError = true;
            et_fragtwo_mobileno.setError("Required");
        }
        return hasError;
    }*/



    /*public void DialogCountryCode(){
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogcountrycode);

        lvcountrycode = (ListView) dialog.findViewById(R.id.lvcountrycode);

        LoadCountryCode();

        lvcountrycode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CountryCode code = lst_countrycode.get(position);
                btn_dropdown_code.setText(code.getISOCode() + " +" +code.getCountryCode());
                //btn_countrycode.setText("+" +code.getCountryCode());

                if(code.getISOCode().equals(getResources().getString(R.string.default_countryisocode))){
                    linearLayout.setVisibility(View.VISIBLE);
                    rlpostalcode.setVisibility(View.VISIBLE);
                    llmultiaddressView.setVisibility(View.GONE);
                    *//*spinnerNricTypeLoad(R.array.array_nric_type);
                    spinner_nric.setEnabled(true);
                    editText_nric.setHint(getResources().getString(R.string.input_nric));
                    spinner_nric.setSelection(1);*//*

                    preferences.edit()
                            .putInt(getString(R.string.pref_signup_nric_type),0)
                            .apply();
                }
                else{
                    *//*spinnerNricTypeLoad(R.array.array_nric_type_passport);
                    spinner_nric.setSelection(5);*//*
                    llmultiaddressView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    rlpostalcode.setVisibility(View.GONE);
                    *//*spinner_nric.setEnabled(false);
                    editText_nric.setHint(getResources().getString(R.string.passportno));*//*
                    preferences.edit()
                            .putInt(getString(R.string.pref_signup_nric_type),4)
                            .apply();
                }
                preferences.edit()
                        .putString(getString(R.string.pref_signup_mobile_iso),code.getISOCode())
                        .putInt(getString(R.string.pref_signup_mobile_code),code.getCountryCode())
                        .putString(getString(R.string.pref_signup_nationalty),code.getCountryName())
                        .apply();

                *//*if(!code.getCountryName().equals("")){
                    int spinnerPosition = adapterNationality.getPosition(code.getCountryName());
                    spinner_nationalty.setSelection(spinnerPosition+1);
                }
                Log.e("isocode_code",code.getISOCode() + "," + code.getCountryCode());*//*

                dialog.dismiss();
            }
        });
        dialog.show();
    }*/

    /*public void spinnerNricTypeLoad(int nric_type){
        adapterNricType = ArrayAdapter.createFromResource(this, nric_type, R.layout.custom_spinner_style);
        adapterNricType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_nric.setAdapter(new NothingSelectedSpinnerAdapter(adapterNricType, R.layout.signup_spinner_row_nothing_selected, this));
    }*/

    /*public void callvolleyCountryCode(){
        if (Utils.hasInternetConnection(getActivity())) {
            VolleyCountryCode v_countryCode = new VolleyCountryCode(getActivity());
            v_countryCode.GetCountryCodeJsonData(new VolleyCountryCode.VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<CountryCode> success) {
                    lst_countrycode = success;
                    if(lst_countrycode.size()>0){
                        String isocode = preferences.getString(getString(R.string.pref_signup_mobile_iso), "");
                        int phone_code = preferences.getInt(getString(R.string.pref_signup_mobile_code),0);
                        if(isocode.equals("")){
                            btn_dropdown_code.setText(getResources().getString(R.string.default_countryisocode) + " +"+getResources().getString(R.string.default_countrycode));
                            //btn_countrycode.setText("+"+getResources().getString(R.string.default_countrycode));
                        }else{
                            if (isocode.equals("SG +65"))
                            {
                                Toast.makeText(getContext(), "SG+65", Toast.LENGTH_SHORT).show();
                                btn_dropdown_code.setText(isocode);
                            }
                            else {
                                btn_dropdown_code.setText(isocode + " +"+phone_code);
                            }
                            //btn_countrycode.setText("+"+phone_code);
                        }
                        String code = btn_dropdown_code.getText().toString().substring(3);
                        Log.d("finaltest", isocode + "=iscode  "+code);
                        preferences.edit()
                                .putString(getString(R.string.pref_signup_mobile_iso),isocode)
                                .putInt(getString(R.string.pref_signup_mobile_code),Integer.parseInt(code)).apply();

                        Log.e("call api",btn_dropdown_code.getText().toString()+ "," + code);
                    }
                    if (btn_dropdown_code.getText().toString().equals("SG+65") || btn_dropdown_code.getText().toString().equals("SG +65"))
                    {
                        linearLayout.setVisibility(View.VISIBLE);
                        llmultiaddressView.setVisibility(View.GONE);
                    }
                    else {
                        linearLayout.setVisibility(View.GONE);
                        llmultiaddressView.setVisibility(View.VISIBLE);
                    }

                }

                @Override
                public void onFail(String errorcode, String errormessage) {
                    Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
                    btn_dropdown_code.setText(getResources().getString(R.string.default_countryisocode) + " +"+getResources().getString(R.string.default_countrycode));
                    //btn_countrycode.setText("+"+getResources().getString(R.string.default_countrycode));
                }
            });
        }else{
            Toast.makeText(getContext(), "No Internet", Toast.LENGTH_SHORT).show();
            btn_dropdown_code.setText(getResources().getString(R.string.default_countryisocode) + " +"+getResources().getString(R.string.default_countrycode));
            //btn_countrycode.setText("+"+getResources().getString(R.string.default_countrycode));
        }
    }*/

    public void LoadCountryCode(){
        adpt_countrycode = new CountryCodeAdapter(getContext(), lst_countrycode);
        adpt_countrycode.notifyDataSetChanged();
        lvcountrycode.setAdapter(adpt_countrycode);
    }

    private void callFragSingupThree() {
        FragSingupThree fragSingupThree = (FragSingupThree) getFragmentManager().findFragmentByTag("FragSingupThree");
        if (fragSingupThree == null)
        {
            fragSingupThree = new FragSingupThree();
        }
        getFragmentManager().beginTransaction().replace(R.id.frag_signup_holder, fragSingupThree, "FragSingupThree").commit();
    }
}
