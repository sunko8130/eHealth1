package sg.com.ehealthassist.ehealthassist.Fragment;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.UnicodeSetSpanner;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyCountryCode;
import sg.com.ehealthassist.ehealthassist.Account.ActivitySingup;
import sg.com.ehealthassist.ehealthassist.CountryCode.CountryCodeAdapter;
import sg.com.ehealthassist.ehealthassist.CustomUI.FixedHoloDatePickerDialog;
import sg.com.ehealthassist.ehealthassist.CustomUI.NothingSelectedSpinnerAdapter;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Profile.CountryCode;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

import static android.content.Context.MODE_PRIVATE;
import static sg.com.ehealthassist.ehealthassist.Other.Utils.arr_nationality;

/**
 * Created by User on 12/1/2017.
 */

public class FragSingupOne extends Fragment {

    Button btn_frag_signup_one, btn_dropdown_code;
    //Spinner spinner_nric, spinner_gender, spinner_nationality, spinner_married;
    Button spinner_nric, spinner_gender, spinner_nationality, spinner_married;
    EditText editText_nric, editText_name, edtdob, et_fragtwo_mobileno, TextViewEmail;
    String[] nric_type,nric_type_passport, marrital_status,arr_nationality;
    RelativeLayout rldoblayout;
    ImageView imgdobright_arrow;
    ListView lvcountrycode;
    CountryCodeAdapter adpt_countrycode;
    ArrayList<CountryCode> lst_countrycode = new ArrayList<>();
    ArrayAdapter<CharSequence> adapterMarried, adapterNricType, adapterGender,adapterNationality;
    String[] arr_nric_type = {"OTHER", "PINK IC", "BLUE IC", "FIN"};
    String[] arr_nric_type_passport = {"OTHER", "PINK IC", "BLUE IC", "FIN", "Passport"};
    String[] arr_marrial = {"UNKNOWN", "SEPARATED", "DIVORCED", "MARRIED", "SINGLE", "WIDOWED", "OTHERS"};
    String[] arr_nation = {"AMERICAN,UNITED STATES", "AUSTRALIAN", "AUSTRIAN", "BANGLADESHI", "BELGIAN", "BRAZILIAN", "BRITISH", "BRUNEIAN", "CAMBODIAN", "CANADIAN", "CHINESE", "CHINESE, MACAO", "DANISH", "EAST TIMORESE", "FILIPINO", "FINNISH", "FRENCH", "GERMAN", "GREEK", "HONGKONGER"
                            , "ICELANDER", "INDIAN", "INDONESIAN", "IRANIAN", "IRAQI", "IRISH", "ISRAELI", "ITALIAN", "JAPANESE", "KOREAN, NORTH KOREA", "KOREAN, SOUTH KOREA", "KUWAITI", "MALAYSIAN", "MALDIVIAN", "MEXICAN", "MONGOLIAN", "MYANMAR", "NATIONALITY UNKNOWN", "NEPALESE", "NETHERLANDS"
                            , "NEW ZEALANDER", "ON-SINGAPORE CITIZEN", "NORWEGIAN", "PAKISTANI", "PAPUA NEW GUINEAN", "PORTUGUESE", "ROMANIAN", "RUSSIAN", "SINGAPOREAN", "SOUTH AFRICAN", "SWEDISH", "SWISS", "TAIWANESE", "THAI", "VIETNAMESE"};
    String[] arr_gender = {"FEMALE", "MALE"};
    SharedPreferences preferences = null;
    DBMedProfile dbmedprofile;
    Context themedContext;

    View view;
    @SuppressLint("RestrictedApi")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag_singup_one, container, false);
        preferences = getActivity().getSharedPreferences(getString(R.string.preference_name), MODE_PRIVATE);
        dbmedprofile = new DBMedProfile(getContext());
        themedContext = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog);
        findViewById();
        callvolleyCountryCode();
        LoadData();
        setEvent();
        return view;
    }

    private void findViewById() {
       // et_fragtwo_mobileno = view.findViewById(R.id.et_fragtwo_mobileno2);
        TextViewEmail = view.findViewById(R.id.TextViewEmail);
        //btn_dropdown_code = view.findViewById(R.id.btn_fragtwo_dropdown_code);
        spinner_nric = view.findViewById(R.id.spinner_nric);
        spinner_gender = view.findViewById(R.id.spinner_gender);
        spinner_nationality = view.findViewById(R.id.spinner_nationality);
        spinner_married = view.findViewById(R.id.spinner_married);
        editText_nric = view.findViewById(R.id.editText_nric);
        editText_name = view.findViewById(R.id.editText_name);
        edtdob = view.findViewById(R.id.edt_frag_dob);
        btn_frag_signup_one = view.findViewById(R.id.btn_frag_singup_one);
        rldoblayout = view.findViewById(R.id.rldob_frag_layout);
        imgdobright_arrow = view.findViewById(R.id.img_frag_dobright_arrow);

        nric_type = getResources().getStringArray(R.array.array_nric_type);
        arr_nationality = getResources().getStringArray( R.array.country_code);

        preferences.edit().putInt(getString(R.string.pref_signup_nric_type), -1).apply();
        preferences.edit().putInt(getString(R.string.pref_signup_marrital_status), -1).apply();
        preferences.edit().putInt(getString(R.string.pref_signup_gender), -1).apply();

    }

    private void setEvent() {

        btn_dropdown_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogCountryCode();
            }
        });

        //save mobile no
        et_fragtwo_mobileno.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String controlKey = getString(R.string.pref_signup_mobile);
                String controlValue = et_fragtwo_mobileno.getText().toString();
                preferences.edit().putString(controlKey, controlValue).apply();
            }
        });

        //save email
        TextViewEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String controlKey = getString(R.string.pref_signup_email);
                String controlValue = TextViewEmail.getText().toString();
                // if (!controlValue.equals("")) {
                if (!Utils.isValidEmail(controlValue)) {

                } else {
                    preferences.edit().putString(controlKey, controlValue).commit();
                }
                //}
            }
        });

        //save dob
        edtdob.setOnClickListener(datetimepickerOnClickListener);
        rldoblayout.setOnClickListener(datetimepickerOnClickListener);
        imgdobright_arrow.setOnClickListener(datetimepickerOnClickListener);

        spinner_nric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNricDialog();
            }
        });

        spinner_married.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callMarriedDialog();
            }
        });

        spinner_nationality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callNationalityDialog();
            }
        });

        spinner_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callGenderDialog();
            }
        });

        //save spinner_nric
        /*spinner_nric.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String controlKey = getString(R.string.pref_signup_nric_type);
                if (position > 0) {
                    preferences.edit().putInt(controlKey, position - 1)
                            .apply();
                    if (position > 1 && position!=5) {
                        if (!editText_nric.getText().toString().equals("")) {
                            if (!Utils.isValidNRIC(editText_nric.getText().toString(), nric_type[position - 1])) {
                                editText_nric.setError(getString(R.string.err_invalid_nric));
                            } else {
                                editText_nric.setError(null);
                            }
                        }
                    } else {
                        editText_nric.setError(null);
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                preferences.edit().putInt(getString(R.string.pref_signup_nric_type), -1).apply();
            }
        });*/

        //save nric
        editText_nric.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String controlValue = editText_nric.getText().toString();
                String controlKey = getString(R.string.pref_signup_nric_number);
                preferences.edit().putString(controlKey, controlValue).apply();
            }
        });

        //save name
        editText_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String controlKey = getString(R.string.pref_signup_name);
                String controlValue = editText_name.getText().toString();
                preferences.edit().putString(controlKey, controlValue).apply();
                editText_name.setError(null);
            }
        });

        //save spinner gender
        /*spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    preferences.edit().putInt(getString(R.string.pref_signup_gender), position - 1)
                            .apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                preferences.edit().putInt(getString(R.string.pref_signup_gender), -1).apply();
            }
        });

        //save spinner nationality
        spinner_nationality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                *//*if (arr_nationality[position].equals("SINGAPOREAN"))
                {
                    spinnerNricTypeLoad(R.array.array_nric_type);
                    spinner_nric.setEnabled(true);
                    editText_nric.setHint(getResources().getString(R.string.input_nric));
                    spinner_nric.setSelection(1);

                }*//*
                if (position > 0) {
                    preferences.edit().putString(getString(R.string.pref_signup_nationalty),arr_nationality[position-1])
                            .putInt(getString(R.string.pref_signup_integer_nationalty),position-1).apply();
                }
                *//*else {
                    spinnerNricTypeLoad(R.array.array_nric_type_passport);
                    spinner_nric.setEnabled(true);
                    editText_nric.setHint(getResources().getString(R.string.input_nric));
                    spinner_nric.setSelection(1);
                    if (position > 0) {
                        preferences.edit().putString(getString(R.string.pref_signup_nationalty),arr_nationality[position-1])
                                .putInt(getString(R.string.pref_signup_integer_nationalty),position-1).apply();
                    }
                }*//*

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(getContext(), "test",Toast.LENGTH_SHORT).show();
                preferences.edit()
                        .putInt(getString(R.string.pref_signup_integer_nationalty), -1)
                        .apply();
            }
        });

        //save spinner marrial status
        spinner_married.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    preferences.edit().putInt(getString(R.string.pref_signup_marrital_status), position - 1)
                            .apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                preferences.edit().putInt(getString(R.string.pref_signup_marrital_status), -1).apply();
            }
        });*/

    }

    private void callGenderDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_spinner_gender);
        ListView lv_gender = dialog.findViewById(R.id.lv_spinner_gender);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arr_gender);
        adapter.setNotifyOnChange(true);
        lv_gender.setAdapter(adapter);
        lv_gender.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                spinner_gender.setText(adapterView.getItemAtPosition(position).toString());
                if (position > 0) {
                    preferences.edit().putInt(getString(R.string.pref_signup_gender), position - 1)
                            .apply();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void callNationalityDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_spinner_nation);
        ListView lv_nation = dialog.findViewById(R.id.lv_spinner_nation);
        final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arr_nation);
        lv_nation.setAdapter(adapter);
        lv_nation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                spinner_nationality.setText(adapterView.getItemAtPosition(position).toString());
                if (position > 0) {
                    preferences.edit().putString(getString(R.string.pref_signup_nationalty),arr_nationality[position-1])
                            .putInt(getString(R.string.pref_signup_integer_nationalty),position-1).apply();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void callMarriedDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_spinner_married);
        ListView lv_marrial = dialog.findViewById(R.id.lv_spinner_married);
        final ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arr_marrial);
        lv_marrial.setAdapter(adapter);
        lv_marrial.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                spinner_married.setText(adapterView.getItemAtPosition(position).toString());
                if (position > 0) {
                    preferences.edit().putInt(getString(R.string.pref_signup_marrital_status), position - 1)
                            .apply();
                }
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void callNricDialog() {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_spinner_nric);
        ListView lv_nrictype = dialog.findViewById(R.id.lv_spinner_nrictype);
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arr_nric_type);
        adapter.setNotifyOnChange(true);
        lv_nrictype.setAdapter(adapter);
        lv_nrictype.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String controlKey = getString(R.string.pref_signup_nric_type);
                spinner_nric.setText(adapterView.getItemAtPosition(position).toString());
                spinner_nric.setTextColor(Color.BLUE);
                dialog.dismiss();

                if (position > 0) {
                    preferences.edit().putInt(controlKey, position - 1)
                            .apply();
                    if (position > 1 && position!=5) {
                        if (!editText_nric.getText().toString().equals("")) {
                            if (!Utils.isValidNRIC(editText_nric.getText().toString(), nric_type[position - 1])) {
                                editText_nric.setError(getString(R.string.err_invalid_nric));
                            } else {
                                editText_nric.setError(null);
                            }
                        }
                    } else {
                        editText_nric.setError(null);
                    }
                }
            }
        });
        dialog.show();
    }

    public void LoadData()
    {
        String nation = preferences.getString(getString(R.string.pref_signup_nationalty),"");
        String phone_iso = preferences.getString(getString(R.string.pref_signup_mobile_iso),"");
        if(phone_iso.equals(getResources().getString(R.string.default_countryisocode)) || phone_iso.equals("")){
            /*linearLayout.setVisibility(View.VISIBLE);
            rlpostalcode.setVisibility(View.VISIBLE);
            llmultiaddressView.setVisibility(View.GONE);*/
            spinner_nric.setEnabled(true);
            editText_nric.setHint(getResources().getString(R.string.input_nric));
            spinner_nric.setText("OTHER");
            spinner_nric.setTextColor(Color.BLUE);
        }
        else{
            /*llmultiaddressView.setVisibility(View.VISIBLE);
            linearLayout.setVisibility(View.GONE);
            rlpostalcode.setVisibility(View.GONE);*/
            spinnerNricTypeLoad(R.array.array_nric_type_passport);
            //spinner_nric.setSelection(5);
            spinner_nric.setEnabled(false);
            editText_nric.setHint(getResources().getString(R.string.passportno));
            //spinner_nric.setHint("Passport");
            spinner_nric.setText("Passport");
            spinner_nric.setTextColor(Color.BLUE);
        }

        /*if(!nation.equals("")){
            int spinnerPosition = adapterNationality.getPosition(nation);
            spinner_nationality.setSelection(spinnerPosition+1);
        }
        else{
            int spinnerPosition = adapterNationality.getPosition(getContext().getResources().getString(R.string.defaultnational));
            spinner_nationality.setSelection(spinnerPosition+1);

            // spinner_nationalty.setSelection(177);
            preferences.edit().putString(getString(R.string.pref_signup_nationalty),arr_nationality[spinnerPosition+1])
                    .putInt(getString(R.string.pref_signup_integer_nationalty), spinnerPosition)
                    .apply();
        }*/
    }

    public void spinnerNricTypeLoad(int nric_type){
        adapterNricType = ArrayAdapter.createFromResource(getContext(), nric_type, R.layout.custom_spinner_style);
        adapterNricType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //spinner_nric.setAdapter(new NothingSelectedSpinnerAdapter(adapterNricType, R.layout.signup_spinner_row_nothing_selected, getContext()));
    }

    View.OnClickListener datetimepickerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String dob = preferences.getString(getString(R.string.pref_signup_dob), "");
            Calendar newCalendar = Calendar.getInstance();
            if (dob.equals("")) {
                newCalendar.set(Calendar.YEAR, 1980);
            } else {
                Date date = Utils.reminderTimeFormat(dob, "dd-MMM-yyyy", "dd-MMM-yyyy");
                newCalendar.set(Calendar.YEAR, date.getYear() + 1900);
                newCalendar.set(Calendar.MONTH, date.getMonth());
                newCalendar.set(Calendar.DATE, date.getDate());
            }

            final DatePickerDialog dialog1 = new FixedHoloDatePickerDialog(
                    themedContext,
                    new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();
                            newDate.set(year, monthOfYear, dayOfMonth);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                            edtdob.setText(simpleDateFormat.format(newDate.getTime()));
                            edtdob.setTextColor(getResources().getColor(R.color.colorlightblue));
                            //  rderror_dob.setVisibility(View.GONE);
                            preferences.edit().putString(getString(R.string.pref_signup_dob), edtdob.getText().toString()).apply();
                        }
                    },
                    newCalendar.get(java.util.Calendar.YEAR),
                    newCalendar.get(java.util.Calendar.MONTH),
                    newCalendar.get(java.util.Calendar.DAY_OF_MONTH)
            );

            dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog1.setTitle("Date of Birth");
            dialog1.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog1.show();

        }
    };

    public void callvolleyCountryCode(){
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
                    /*if (btn_dropdown_code.getText().toString().equals("SG+65") || btn_dropdown_code.getText().toString().equals("SG +65"))
                    {
                        linearLayout.setVisibility(View.VISIBLE);
                        llmultiaddressView.setVisibility(View.GONE);
                    }
                    else {
                        linearLayout.setVisibility(View.GONE);
                        llmultiaddressView.setVisibility(View.VISIBLE);
                    }*/

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
    }

    public void DialogCountryCode(){
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

                    /*linearLayout.setVisibility(View.VISIBLE);
                    rlpostalcode.setVisibility(View.VISIBLE);
                    llmultiaddressView.setVisibility(View.GONE);*/

                    spinnerNricTypeLoad(R.array.array_nric_type);
                    spinner_nric.setEnabled(true);
                    spinner_nric.setText("OTHER");
                    spinner_nric.setTextColor(Color.BLUE);
                    editText_nric.setHint(getResources().getString(R.string.input_nric));
                    //spinner_nric.setSelection(1);

                    preferences.edit()
                            .putInt(getString(R.string.pref_signup_nric_type),0)
                            .apply();

                    /*if(!code.getCountryName().equals("")){
                        int spinnerPosition = adapterNationality.getPosition(code.getCountryName());
                        spinner_nationality.setSelection(spinnerPosition+1);
                    }*/
                }
                else{
                    spinnerNricTypeLoad(R.array.array_nric_type_passport);
                    //spinner_nric.setSelection(5);

                    /*llmultiaddressView.setVisibility(View.VISIBLE);
                    linearLayout.setVisibility(View.GONE);
                    rlpostalcode.setVisibility(View.GONE);*/

                    spinner_nric.setEnabled(false);
                    editText_nric.setHint(getResources().getString(R.string.passportno));
                    spinner_nric.setText("Passport");
                    spinner_nric.setTextColor(Color.BLUE);
                    preferences.edit()
                            .putInt(getString(R.string.pref_signup_nric_type),4)
                            .apply();

                    /*if(!code.getCountryName().equals("")){
                        int spinnerPosition = adapterNationality.getPosition(code.getCountryName());
                        spinner_nationality.setSelection(spinnerPosition+1);
                    }*/
                }
                preferences.edit()
                        .putString(getString(R.string.pref_signup_mobile_iso),code.getISOCode())
                        .putInt(getString(R.string.pref_signup_mobile_code),code.getCountryCode())
                        .putString(getString(R.string.pref_signup_nationalty),code.getCountryName())
                        .apply();
                Log.d("eeeee", preferences.getString(getString(R.string.pref_signup_mobile_iso), ""));

                /*if(!code.getCountryName().equals("")){
                    int spinnerPosition = adapterNationality.getPosition(code.getCountryName());
                    spinner_nationality.setSelection(spinnerPosition+1);
                }
                Log.e("isocode_code",code.getISOCode() + "," + code.getCountryCode());*/

                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void LoadCountryCode(){
        adpt_countrycode = new CountryCodeAdapter(getContext(), lst_countrycode);
        adpt_countrycode.notifyDataSetChanged();
        lvcountrycode.setAdapter(adpt_countrycode);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btn_frag_signup_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateallData())
                {
                    FragSingupTwo fragSingupTwo = (FragSingupTwo) getFragmentManager().findFragmentByTag("FragSingupTwo");
                    if (fragSingupTwo == null)
                    {
                        fragSingupTwo = new FragSingupTwo();
                    }
                    getFragmentManager().beginTransaction().replace(R.id.frag_signup_holder, fragSingupTwo, "FragSingupTwo").commit();
                }
                else {
                    Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }



                Log.d("testpreferences", preferences.getInt("signup_nrictype", 0) +"");
                Log.d("testpreferences", preferences.getString("signup_nricnumber", null)+"");
                Log.d("testpreferences", preferences.getString("signup_name", null)+"");
                Log.d("testpreferences", preferences.getInt("signup_gender", 0)+"");
                Log.d("testpreferences", preferences.getString("signup_mp__nationalty", null)+"");
                Log.d("testpreferences", preferences.getInt("signup_integer_nationalty", 0) + "integer");
                Log.d("testpreferences", preferences.getInt("signup_marrital_status", 0) +"");
                Log.d("testpreferences", preferences.getString("signup_dob", null)+"");
            }
        });
    }

    private boolean validateallData() {
        Boolean hasError = false;
        String telph = preferences.getString(getString(R.string.pref_signup_mobile), "");
        Log.d("dddddd", telph + "no");
        String nric = preferences.getString(getString(R.string.pref_signup_nric_number), "");
        String name = preferences.getString(getString(R.string.pref_signup_name), "");
        String dob = preferences.getString(getString(R.string.pref_signup_dob), "");
        int integernricType = preferences.getInt(getString(R.string.pref_signup_nric_type), -1);
        Log.d("nrictype_check", integernricType + " -1");
        //   String email = preferences.getString(getString(R.string.pref_signup_email), "");

        if (telph.equals("") || et_fragtwo_mobileno.length() == 0)
        {
            hasError = true;
            et_fragtwo_mobileno.setError("Required");
        }

        if (nric.equals("") || editText_nric.length() == 0) {
            editText_nric.setError("Required");
            hasError = true;
        }
        if (integernricType > 0 && integernricType <4) {
            String nstrtype = nric_type[integernricType];
            if (!Utils.isValidNRIC(nric, nstrtype)) {
                editText_nric.setError("Invalid NRIC");
                hasError = true;
            }
        }
        if (integernricType < 0) {
            hasError = true;
        }

        if (preferences.getInt(getString(R.string.pref_signup_nric_type), 0) > 0 &&
                preferences.getInt(getString(R.string.pref_signup_nric_type),0)<4) {
            String nstrtype = nric_type[preferences.getInt(getString(R.string.pref_signup_nric_type),0)];
            if (!Utils.isValidNRIC(nric,nstrtype)) {
                editText_nric.setError("Invalid");
                hasError = true;
            }
        }
        if (name.equals("") || editText_name.length() == 0) {
            editText_name.setError("Required");
            hasError = true;
        }
        if (dob == "" || edtdob.length() == 0) {
            hasError = true;
        }
        return hasError;
    }
}
