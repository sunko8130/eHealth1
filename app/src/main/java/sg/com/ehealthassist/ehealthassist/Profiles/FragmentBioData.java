package sg.com.ehealthassist.ehealthassist.Profiles;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import sg.com.ehealthassist.ehealthassist.API.Request.RequestPcodejson;
import sg.com.ehealthassist.ehealthassist.API.Request.Requestdeleteprofilejson;
import sg.com.ehealthassist.ehealthassist.API.Request.Requestinsertprofilejson;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyCountryCode;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyInsertMemberListProfile;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyPCodeAddress;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleyUpdateMemberListProfile;
import sg.com.ehealthassist.ehealthassist.API.Volley.VolleydeleteMemberListProfile;
import sg.com.ehealthassist.ehealthassist.CountryCode.CountryCodeAdapter;
import sg.com.ehealthassist.ehealthassist.CustomUI.FixedHoloDatePickerDialog;
import sg.com.ehealthassist.ehealthassist.CustomUI.NothingSelectedSpinnerAdapter;
import sg.com.ehealthassist.ehealthassist.DB.DBMedProfile;
import sg.com.ehealthassist.ehealthassist.Models.Profile.CountryCode;
import sg.com.ehealthassist.ehealthassist.Models.Profile.MedicalProfile;
import sg.com.ehealthassist.ehealthassist.Other.Utils;
import sg.com.ehealthassist.ehealthassist.R;

public class FragmentBioData extends Fragment {

    SharedPreferences pref = null;
    EditText edtdob, txtBlockNo, txtStreet, txtBuildingName, txtNRIC, txtName,
            txtPhoneHome, txtEmail, edt_address1, edt_address2, edt_address3, edt_address4;
    public static EditText txtPhoneHp, txtPostalCode, txtUnitNo;
    TextView text_error_msg;
    Spinner spinnerNricType, spinnerelation, spinner_married, spinner_gender, spinner_nationalty, spinner_language;
    Button btnUploadProfile, buttonMedProfileLoacalProfile, buttonMedProfileDelete,
            btn_mobiledropdownccode, btn_mobilecountrycode;
    ImageButton imgbtn_pcodesearch;
    ImageView imgdobright_arrow;
    RelativeLayout rldoblayout, rlpostalcode;
    LinearLayout llrelation, lllanguage, llmultiaddressView, llContactView;
    ListView lvcountrycode;
    CountryCodeAdapter adpt_countrycode;
    String memberid, from, usertoken;
    String[] nric_type,nric_type_passport, marital_status, arr_nationality;
    int from_clinicid, member, from_id;
    String oldpatientname = "",olddob ="";
    DBMedProfile dbmedprofile;
    ArrayAdapter<CharSequence> adapterNricType, adapterMarried, adapterRelation,
            adapterGender, adapterNationality, adapter_language;
    boolean backup_drive_detect = false, update_profile_flag = false;
    ArrayList<CountryCode> lst_countrycode = new ArrayList<CountryCode>();
    String mp_countryname = "";
    Context themedContext;
    public FragmentBioData() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater INFLATER, ViewGroup container, Bundle savedInstanceState) {
        View content = INFLATER.inflate(R.layout.fragment_profilebiodata, container, false);
        pref = getActivity().getSharedPreferences(getString(R.string.preference_name), getActivity().MODE_PRIVATE);
        themedContext = new ContextThemeWrapper(getActivity(), android.R.style.Theme_Holo_Light_Dialog);
        memberid = pref.getString(getString(R.string.pref_login_memberId), "");
        usertoken = pref.getString(getString(R.string.pref_login_Access_token), "");
        backup_drive_detect = pref.getBoolean(getString(R.string.pref_backup_drive_checked), true);
        update_profile_flag = pref.getBoolean(getString(R.string.pref_update_profile_flag), false);
        dbmedprofile = new DBMedProfile(getActivity());
        from = getArguments().getString("from");
        from_clinicid = getArguments().getInt("clinicid", 0);
        member = getArguments().getInt("member", 0);
        from_id = getArguments().getInt("id", 0);
        findViewById(content);

        callvolleyCountryCode();

        if (from.equals("list")) {
            checkEnableEditable();
            loadData();
            loadContactData();
        } else {
            edtdob.setEnabled(true);
            txtNRIC.setEnabled(true);
            imgdobright_arrow.setEnabled(true);
            rldoblayout.setEnabled(true);
            buttonMedProfileDelete.setVisibility(View.GONE);
            String phone_number = pref.getString(getString(R.string.pref_main_user_data_hp), "");
            int country_code = pref.getInt(getString(R.string.pref_main_user_data_hp_code), 0);
            String isocdoe = pref.getString(getString(R.string.pref_main_user_data_hp_iso), "");
            txtPhoneHp.setText(phone_number);
            layoutcheckeanble(isocdoe);

            if (isocdoe.equals(getResources().getString(R.string.default_countryisocode)) || isocdoe.equals("")) {
                spinnderNricLoad(R.array.array_nric_type);
                spinnerNricType.setEnabled(true);
                spinnerNricType.setSelection(1);
                pref.edit().putInt(getString(R.string.pref_mp_integer_nric_type), 0)
                        .apply();
            } else {
                spinnderNricLoad(R.array.array_nric_type_passport);
                spinnerNricType.setSelection(5);
                spinnerNricType.setEnabled(false);
                pref.edit().putInt(getString(R.string.pref_mp_integer_nric_type), 4)
                        .apply();
            }
            editmp_mobile(getString(R.string.pref_mp_mobile), phone_number);
            editmp_mobile_code(getString(R.string.pref_mp_mobile_code), country_code);
            editmp_mobile_iso(getString(R.string.pref_mp_mobile_iso), isocdoe);

            String nation = getActivity().getResources().getString(R.string.defaultnational);
            int spinnerPosition = adapterNationality.getPosition(nation);
                spinner_nationalty.setSelection(spinnerPosition+1);
            pref.edit().putString(getString(R.string.pref_signup_nationalty),arr_nationality[spinnerPosition+1])
                    .putInt(getString(R.string.pref_signup_integer_nationalty), -1)
                    .apply();
            spinner_language.setSelection(3);
            spinner_gender.setSelection(2);
            pref.edit().putInt(getString(R.string.pref_mp_sex), 2)
                    .apply();
            //String email = pref.getString(getString(R.string.pref_main_user_data_email), "");
            //  txtEmail.setText(email);
          /*  getcountryname(btn_mobiledropdownccode.getText().toString());
            Log.e("countryname" ,mp_countryname);*/

        }
        setEvents();
        return content;
    }

    //region findviewbyid()
    public void findViewById(View _vcontent) {
        text_error_msg = (TextView) _vcontent.findViewById(R.id.text_error_msg);
        //region editText
        txtName = (EditText) _vcontent.findViewById(R.id.editText_name);
        txtNRIC = (EditText) _vcontent.findViewById(R.id.editText_nricbio);
        txtName.setFilters(new InputFilter[]{new InputFilter.LengthFilter(66), new InputFilter.AllCaps()});
        txtNRIC.setFilters(new InputFilter[]
                {new InputFilter.LengthFilter(15), new InputFilter.AllCaps()});
        edtdob = (EditText) _vcontent.findViewById(R.id.textViewDOB);
        txtBlockNo = (EditText) _vcontent.findViewById(R.id.editTextBlockNo);
        txtStreet = (EditText) _vcontent.findViewById(R.id.editTextStreet);
        txtBuildingName = (EditText) _vcontent.findViewById(R.id.editTextBuildingName);
        txtUnitNo = (EditText) _vcontent.findViewById(R.id.editTextUnitNo);
        txtPostalCode = (EditText) _vcontent.findViewById(R.id.editTexttelPostalCode);
        txtPhoneHome = (EditText) _vcontent.findViewById(R.id.editText_HomeMobile);
        txtPhoneHp = (EditText) _vcontent.findViewById(R.id.editText_Mobile);
        txtEmail = (EditText) _vcontent.findViewById(R.id.editTextLogInEmail);
        edt_address1 = (EditText) _vcontent.findViewById(R.id.edt_address1);
        edt_address2 = (EditText) _vcontent.findViewById(R.id.edt_address2);
        edt_address3 = (EditText) _vcontent.findViewById(R.id.edt_address3);
        edt_address4 = (EditText) _vcontent.findViewById(R.id.edt_address4);
        //endregion
        //region spinner
        spinnerNricType = (Spinner) _vcontent.findViewById(R.id.spinner_nric);
        spinnerelation = (Spinner) _vcontent.findViewById(R.id.spinner_relation);
        spinner_married = (Spinner) _vcontent.findViewById(R.id.spinner_married);
        spinner_gender = (Spinner) _vcontent.findViewById(R.id.spinner_gender);
        spinner_nationalty = (Spinner) _vcontent.findViewById(R.id.spinner_nationalty);
        spinner_language = (Spinner) _vcontent.findViewById(R.id.spinner_language);
        //endregion
        //region button
        btnUploadProfile = (Button) _vcontent.findViewById(R.id.buttonMedProfileUploadProfile);
        buttonMedProfileLoacalProfile = (Button) _vcontent.findViewById(R.id.buttonMedProfileLoacalProfile);
        buttonMedProfileDelete = (Button) _vcontent.findViewById(R.id.buttonMedProfileDelete);
        btn_mobiledropdownccode = (Button) _vcontent.findViewById(R.id.btn_mobiledropdownccode);
        btn_mobilecountrycode = (Button) _vcontent.findViewById(R.id.btn_mobilecountrycode);
        //endregion
        //region Imagebutton
        imgbtn_pcodesearch = (ImageButton) _vcontent.findViewById(R.id.btn_pcodesearch);
        //endregion
        //region ImageView
        imgdobright_arrow = (ImageView) _vcontent.findViewById(R.id.imgdobright_arrow);
        //endregion
        //region  Relativelayout
        rldoblayout = (RelativeLayout) _vcontent.findViewById(R.id.rldoblayout);
        rlpostalcode = (RelativeLayout) _vcontent.findViewById(R.id.rlpostalcode);
        //endregion
        //region Linearlayout
        llrelation = (LinearLayout) _vcontent.findViewById(R.id.llrelation);
        lllanguage = (LinearLayout) _vcontent.findViewById(R.id.lllanguage);
        llmultiaddressView = (LinearLayout) _vcontent.findViewById(R.id.llmultiaddressView);
        llContactView = (LinearLayout) _vcontent.findViewById(R.id.llContactView);
        //endregion
        //region spinner Load
        nric_type = getResources().getStringArray(R.array.array_nric_type);
        nric_type_passport = getResources().getStringArray(R.array.array_nric_type_passport);
        spinnderNricLoad(R.array.array_nric_type);

        adapterMarried = ArrayAdapter.createFromResource(getActivity(), R.array.array_marriedStaus, R.layout.custom_spinner_style);
        adapterMarried.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        marital_status = getResources().getStringArray(R.array.array_marriedStaus);
        spinner_married.setAdapter(new NothingSelectedSpinnerAdapter(adapterMarried, R.layout.signup_spinner_row_nothing_selected, getActivity()));

        adapterGender = ArrayAdapter.createFromResource(getActivity(), R.array.array_gender, R.layout.custom_spinner_style);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_gender.setAdapter(new NothingSelectedSpinnerAdapter(adapterGender, R.layout.signup_spinner_row_nothing_selected, getActivity()));

        arr_nationality = getResources().getStringArray(R.array.country_code);
        adapterNationality = ArrayAdapter.createFromResource(getActivity(), R.array.country_code, R.layout.custom_spinner_style);
        adapterNationality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_nationalty.setAdapter(new NothingSelectedSpinnerAdapter(adapterNationality, R.layout.signup_spinner_row_nothing_selected, getActivity()));

        if (member > 0) {
            adapterRelation = ArrayAdapter.createFromResource(getActivity(), R.array.array_myself_relation, R.layout.custom_spinner_style);
            txtPhoneHp.setFocusable(false);
            txtEmail.setFocusable(false);

        } else {
            adapterRelation = ArrayAdapter.createFromResource(getActivity(), R.array.array_relation, R.layout.custom_spinner_style);
            txtPhoneHp.setFocusable(true);
            txtEmail.setFocusable(true);
        }
        adapterRelation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerelation.setAdapter(new NothingSelectedSpinnerAdapter(adapterRelation, R.layout.signup_spinner_row_nothing_selected, getActivity()));

        adapter_language = ArrayAdapter.createFromResource(getActivity(), R.array.arrays_language, R.layout.custom_spinner_style);
        adapter_language.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_language.setAdapter(new NothingSelectedSpinnerAdapter(adapter_language, R.layout.signup_spinner_row_nothing_selected, getActivity()));
        //endregion
    }

    //endregion
    //region LoadData()
    private void loadData() {

        checkEditText();

        int nricType = pref.getInt(getString(R.string.pref_mp_integer_nric_type), -1);
        txtNRIC.setText(pref.getString(getString(R.string.pref_mp_nric), ""));
        txtName.setText(pref.getString(getString(R.string.pref_mp_name), ""));
        oldpatientname = txtName.getText().toString();

        int sex = pref.getInt(getString(R.string.pref_mp_sex), -1);
        int language = pref.getInt(getString(R.string.pref_mp_language), -1);
        String nation = pref.getString(getString(R.string.pref_mp_nationality_name), "");
        String pdob = pref.getString(getString(R.string.pref_mp_dob), "");
        int relation = pref.getInt(getString(R.string.pref_mp_integer_relation), -1);
        int married_status = pref.getInt(getString(R.string.pref_mp_integer_married_status), -1);
        int mp_member = pref.getInt(getString(R.string.pref_mp_member), 0);

        if (mp_member > 0) {
            buttonMedProfileDelete.setVisibility(View.GONE);
            spinnerelation.setEnabled(false);
            btn_mobiledropdownccode.setEnabled(false);
        } else {
            spinnerelation.setEnabled(true);
            buttonMedProfileDelete.setVisibility(View.VISIBLE);
            btn_mobiledropdownccode.setEnabled(false);
        }
        if (nricType != -1) {
            int position = nricType;
            if(position == 4){
                spinnderNricLoad(R.array.array_nric_type_passport);
                spinnerNricType.setSelection(position + 1);

            }else{
                spinnderNricLoad(R.array.array_nric_type);
                spinnerNricType.setSelection(position + 1);
            }

        }
        if (relation != -1) {
            Log.e("fragment relation", relation + "");
            int position = relation;
            spinnerelation.setSelection(position);
        }
        if (married_status != -1) {
            int position = married_status;
            spinner_married.setSelection(position + 1);
        }
        if (sex != -1) {
            int position = sex;
            spinner_gender.setSelection(position + 1);
        }

        if (language != -1) {
            int position = language;
            spinner_language.setSelection(position+1);
        }else{
            spinner_language.setSelection(3);
        }
        if (!nation.equals("")) {
            int spinnerPosition = adapterNationality.getPosition(nation);
            spinner_nationalty.setSelection(spinnerPosition + 1);
        } else {
            int spinnerPosition = adapterNationality.getPosition(getActivity().getResources().getString(R.string.defaultnational));
            spinner_nationalty.setSelection(spinnerPosition + 1);

            spinner_nationalty.setSelection(spinnerPosition + 1);
            pref.edit().putString(getString(R.string.pref_mp_nationality_name), arr_nationality[spinnerPosition + 1])
                    .putInt(getString(R.string.pref_mp_nationality_integer), -1)
                    .apply();
        }
        if (!pdob.equals("")) {
            edtdob.setText(pdob);
            olddob= edtdob.getText().toString();
            edtdob.setTextColor(getResources().getColor(R.color.colorlightblue));
        }
    }

    private void loadContactData() {
        int nricType = pref.getInt(getString(R.string.pref_mp_integer_nric_type), -1);
        txtPhoneHome.setText(pref.getString(getString(R.string.pref_mp_tel), ""));
        String phonehp = pref.getString(getString(R.string.pref_mp_mobile), "");
        txtPhoneHp.setText(phonehp);
        int mobile_code = pref.getInt(getString(R.string.pref_mp_mobile_code), 0);
        String mobile_iso = pref.getString(getString(R.string.pref_mp_mobile_iso), "");
        btn_mobiledropdownccode.setText(mobile_iso);
        btn_mobilecountrycode.setText("+" + mobile_code);
        txtEmail.setText(pref.getString(getString(R.string.pref_mp_email), ""));
        txtPostalCode.setText(pref.getString(getString(R.string.pref_mp_postal_code), ""));
        txtBlockNo.setText(pref.getString(getString(R.string.pref_mp_block_no), ""));
        txtStreet.setText(pref.getString(getString(R.string.pref_mp_street), ""));
        txtBuildingName.setText(pref.getString(getString(R.string.pref_mp_building), ""));
        txtUnitNo.setText(pref.getString(getString(R.string.pref_mp_unit_no), ""));
        edt_address1.setText(pref.getString(getString(R.string.pref_mp_address1), ""));
        edt_address2.setText(pref.getString(getString(R.string.pref_mp_address2), ""));
        edt_address3.setText(pref.getString(getString(R.string.pref_mp_address3), ""));
        edt_address4.setText(pref.getString(getString(R.string.pref_mp_address4), ""));
        layoutcheckeanble(mobile_iso);

        if (mobile_iso.equals(getResources().getString(R.string.default_countryisocode)) || mobile_iso.equals("")) {
            if(nricType==4){
                spinnderNricLoad(R.array.array_nric_type_passport);
            }else{
                spinnderNricLoad(R.array.array_nric_type);
            }
            spinnerNricType.setSelection(nricType + 1);
            spinnerNricType.setEnabled(false);
        } /*else {
            spinnderNricLoad(R.array.array_nric_type_passport);
            spinnerNricType.setSelection(5);
            spinnerNricType.setEnabled(false);

            pref.edit().putInt(getString(R.string.pref_mp_integer_nric_type), 4)
                    .apply();
        }*/
    }

    public void editmp_mobile(String controlkey, String phonenumber) {
        pref.edit().putString(controlkey, phonenumber).commit();
    }

    public void editmp_mobile_code(String controlkey, int phonenumber) {
        pref.edit().putInt(controlkey, phonenumber).commit();
    }

    public void editmp_mobile_iso(String controlkey, String isocode) {
        pref.edit().putString(controlkey, isocode).commit();
    }

    public void spinnderNricLoad(int nric_types){
        adapterNricType = ArrayAdapter.createFromResource(getActivity(),nric_types, R.layout.custom_spinner_style);
        adapterNricType.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerNricType.setAdapter(new NothingSelectedSpinnerAdapter(adapterNricType, R.layout.signup_spinner_row_nothing_selected, getActivity()));
    }

    //region DateTime Picker
    View.OnClickListener datetimepickerOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String dob = pref.getString(getString(R.string.pref_mp_dob), "");
            Calendar newCalendar = Calendar.getInstance();
            if (dob.equals("")) {
                newCalendar.set(Calendar.YEAR, 1980);
            } else {
                Date date = Utils.reminderTimeFormat(dob, "dd-MMM-yyyy", "dd-MMM-yyyy");
                newCalendar.set(Calendar.YEAR, date.getYear() + 1900);
                newCalendar.set(Calendar.MONTH, date.getMonth());
                newCalendar.set(Calendar.DATE, date.getDate());
            }
          /*  DatePickerDialog dialog = new DatePickerDialog(getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();

                            newDate.set(year, monthOfYear, dayOfMonth);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

                            edtdob.setText(simpleDateFormat.format(newDate.getTime()));
                            edtdob.setTextColor(getResources().getColor(R.color.colorlightblue));
                            pref.edit().putString(getString(R.string.pref_mp_dob), edtdob.getText().toString()).apply();
                        }
                    },
                    newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setTitle("Date of Birth");
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog.show();*/

            final DatePickerDialog dialog = new FixedHoloDatePickerDialog(
                   themedContext,
                    new DatePickerDialog.OnDateSetListener(){
                        @Override
                        public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                            Calendar newDate = Calendar.getInstance();

                            newDate.set(year, monthOfYear, dayOfMonth);
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

                            edtdob.setText(simpleDateFormat.format(newDate.getTime()));
                            edtdob.setTextColor(getResources().getColor(R.color.colorlightblue));
                            pref.edit().putString(getString(R.string.pref_mp_dob), edtdob.getText().toString()).apply();
                        }
                    },
                    newCalendar.get(java.util.Calendar.YEAR),
                    newCalendar.get(java.util.Calendar.MONTH),
                    newCalendar.get(java.util.Calendar.DAY_OF_MONTH)
            );

            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setTitle("Date of Birth");
            dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
            dialog.show();


        }
    };
    //endregion
    //endregion
    //region Valid Data
    //region checkeditext()
    public boolean checkEditText() {
        pref = getActivity().getSharedPreferences(getString(R.string.preference_name), getActivity().MODE_PRIVATE);
        Boolean hasError = false;
        String dob = pref.getString(getString(R.string.pref_mp_dob), "");
        String nricType = pref.getString(getString(R.string.pref_mp_nric_type), "");
        String nric = pref.getString(getString(R.string.pref_mp_nric), "");
        int integernricType = pref.getInt(getString(R.string.pref_mp_integer_nric_type), -1);
        String name = pref.getString(getString(R.string.pref_mp_name), "");
        String telph = pref.getString(getString(R.string.pref_mp_mobile), "");

        if (nricType.equals("")) {
            hasError = true;
        }
        if (nric.equals("")) {
            txtNRIC.setError("Required");
            hasError = true;
        }
        if (pref.getInt(getString(R.string.pref_mp_integer_nric_type), 0) > 0 &&
                pref.getInt(getString(R.string.pref_mp_integer_nric_type), 0) < 4) {

            String nstrtype = nric_type[pref.getInt(getString(R.string.pref_mp_integer_nric_type), 0)];

            if (!Utils.isValidNRIC(nric, nstrtype)) {
                txtNRIC.setError("Invalid");
                hasError = true;
            }
        }
        if (!from.equals("list")) {
            if (dbmedprofile.existNRIC(nric, integernricType, dob, memberid) > 0) {
                txtNRIC.setError("Duplicate profile found");
                hasError = true;
            }
        }
        if (name.equals("")) {
            txtName.setError("Required");
            hasError = true;
        }
        if (dob.equals("")) {
            edtdob.setError(null);
            hasError = true;
        }

        if (telph.length() < 19) {
            String phone_number = pref.getString(getString(R.string.pref_mp_mobile), "");
            pref.edit().putString(getActivity().getString(R.string.pref_mp_mobile), phone_number).apply();
            txtPhoneHp.setText(phone_number);
        }
        if(telph.length()<8){
            hasError = true;
        }
        return hasError;
    }
    //endregion

    //region checkEnable Editext
    public void checkEnableEditable() {
        String dob = pref.getString(getString(R.string.pref_mp_dob), "");
        int nricType = pref.getInt(getString(R.string.pref_mp_integer_nric_type), -1);
        String nric = pref.getString(getString(R.string.pref_mp_nric), "");

        if (nric.equals("")) {
            txtNRIC.setEnabled(true);
        } else {
            txtNRIC.setEnabled(false);
        }
        if (dob.equals("")) {
            edtdob.setEnabled(true);
            imgdobright_arrow.setEnabled(true);
            rldoblayout.setEnabled(true);
        } else {
            edtdob.setEnabled(true);
            imgdobright_arrow.setEnabled(true);
            rldoblayout.setEnabled(true);
        }
        if (nricType > -1) {
            spinnerNricType.setEnabled(false);
        } else {
            spinnerNricType.setEnabled(true);
        }
    }
    //endregion
    //endregion
    //region setEvent()
    public void setEvents() {
        txtName.setOnFocusChangeListener(txtFocusChangeListener);
        edtdob.setOnClickListener(datetimepickerOnClickListener);
        rldoblayout.setOnClickListener(datetimepickerOnClickListener);
        imgdobright_arrow.setOnClickListener(datetimepickerOnClickListener);
        btnUploadProfile.setOnClickListener(btnUploadProfileOnClickListener);
        buttonMedProfileLoacalProfile.setOnClickListener(btnLocalMedProfileOnClickListener);
        buttonMedProfileDelete.setOnClickListener(buttonMedProfileDeleteOnClickListener);
        //region spinnerNricType selected()
        spinnerNricType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String controlKey = getString(R.string.pref_mp_nric_type);
                if (position > 0) {
                    String result = parent.getItemAtPosition(position).toString();
                    pref.edit().putString(controlKey, result)
                            .putInt(getString(R.string.pref_mp_integer_nric_type), position - 1)
                            .apply();

                    if (position > 1 && position != 5) {
                        if (!txtNRIC.getText().toString().equals("")) {

                            if (!Utils.isValidNRIC(txtNRIC.getText().toString(), nric_type[position - 1])) {
                                txtNRIC.setError(getString(R.string.err_invalid_nric));
                            } else {
                                txtNRIC.setError(null);
                            }
                        }
                    } else {
                        txtNRIC.setError(null);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pref.edit().putString(getString(R.string.pref_mp_nric_type), "")
                        .putInt(getString(R.string.pref_mp_integer_nric_type), -1)
                        .apply();
            }
        });
        //endregion
        //region spinnerelation selected()
        spinnerelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String controlKey = getString(R.string.pref_mp_relation);
                if (position > 0) {
                    int member = 0;
                    String result = parent.getItemAtPosition(position).toString();
                    if (result.equals("MYSELF")) {
                        member = 1;
                    }
                    pref.edit().putString(controlKey, result)
                            .putInt(getString(R.string.pref_mp_integer_relation), position)
                            .putInt(getString(R.string.pref_mp_member), member)
                            .apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pref.edit().putString(getString(R.string.pref_mp_relation), "").apply();
            }
        });
        //endregion
        //region spinnerMarried selected()
        spinner_married.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String controlKey = getString(R.string.pref_mp_marriedstatus);
                if (position > 0) {
                    pref.edit()
                            .putInt(getString(R.string.pref_mp_integer_married_status), position - 1)
                            .apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pref.edit().putInt(getString(R.string.pref_mp_integer_married_status), -1).apply();
            }
        });
        //endregion
        //region spinner Gender ()
        spinner_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    pref.edit().putInt(getString(R.string.pref_mp_sex), position - 1)
                            .apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pref.edit().putInt(getString(R.string.pref_mp_sex), -1).apply();

            }
        });
        //endregion
        //region spinnerNationality selected()
        spinner_nationalty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    pref.edit().putString(getString(R.string.pref_mp_nationality_name), arr_nationality[position - 1])
                            .putInt(getString(R.string.pref_mp_nationality_integer), position - 1).apply();
                    Log.e("Spinner nationality", arr_nationality[position - 1] + "");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pref.edit()
                        .putInt(getString(R.string.pref_mp_nationality_integer), -1)
                        .apply();
            }
        });
        //endregion
        //region spinner language()
        spinner_language.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    pref.edit().putInt(getString(R.string.pref_mp_language), position-1)
                            .apply();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pref.edit().putInt(getString(R.string.pref_mp_language), -1).apply();
            }
        });
        //endregion
        //region txtName textchange()
        txtName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String controlKey = getString(R.string.pref_mp_name);
                String controlValue = txtName.getText().toString();
                pref.edit().putString(controlKey, controlValue).apply();
                txtName.setError(null);
            }
        });
        //endregion
        //region txtnric textchange()
        txtNRIC.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String controlValue = txtNRIC.getText().toString();
                controlValue = txtNRIC.getText().toString();
                String controlKey = getString(R.string.pref_mp_nric);
                pref.edit().putString(controlKey, controlValue).apply();
            }
        });
        //endregion
        //region PostalCode Event
        txtPostalCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
                if (arg1 == EditorInfo.IME_ACTION_GO) {
                    getAddress();
                }
                return false;
            }

        });
        //endregion
        //region PostalSearch Image Button
        imgbtn_pcodesearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAddress();
            }
        });
        //endregion
        //region PostCode Textchange()
        txtPostalCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                imgbtn_pcodesearch.setVisibility(View.VISIBLE);
                String controlKey = getString(R.string.pref_signup_postalcode);
                String controlValue = txtPostalCode.getText().toString();
                pref.edit().putString(controlKey, controlValue).commit();
            }
        });
        //endregion
        //region TextChanged Event Mobile
        txtPhoneHp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (member > 0) {
                    Utils.showAlertDialog(getActivity(), "Info", getResources().getString(R.string.account_info_update));
                }
            }
        });
        txtEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (member > 0) {
                    Utils.showAlertDialog(getActivity(), "Info", getResources().getString(R.string.account_info_update));
                }
            }
        });

        txtPhoneHp.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String controlValue = txtPhoneHp.getText().toString();
                String controlkey = getString(R.string.pref_mp_mobile);

                editmp_mobile(controlkey, controlValue);
              /*  if (!Utils.isValidSGMobile(controlValue) && controlValue.length() > 0) {
                    txtPhoneHp.setError(getString(R.string.err_invalid_mobile));
                } else {
                    controlValue =   controlValue;
                    editmp_mobile(controlkey, controlValue);
                }*/
            }
        });
        txtPhoneHome.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String controlKey = getString(R.string.pref_mp_tel);
                String controlValue = txtPhoneHome.getText().toString();
                editmp_mobile(controlKey, controlValue);
               /* if (!Utils.isValidSGNumber(controlValue) && controlValue.length() > 0) {
                    txtPhoneHome.setError(getString(R.string.err_invalid_contact_number));
                } else {

                    editmp_mobile(controlKey, controlValue);
                }*/
            }
        });
        //endregion

        //region TextChangeEvent Email
        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String controlKey = getString(R.string.pref_mp_email);
                String controlValue = txtEmail.getText().toString();
                if (!controlValue.equals("")) {
                    if (!Utils.isValidEmail(controlValue)) {
                        txtEmail.setError(getString(R.string.err_invalid_email));
                    } else {
                        pref.edit().putString(controlKey, controlValue).commit();
                    }
                }
            }
        });

        //endregion
        //region mobile countrycode
        btn_mobiledropdownccode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCountryCode("mobile");
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
                    case R.id.editTextBlockNo:
                        controlKey = getString(R.string.pref_mp_block_no);
                        controlValue = txtBlockNo.getText().toString();
                        break;
                    case R.id.editTextStreet:
                        controlKey = getString(R.string.pref_mp_street);
                        controlValue = txtStreet.getText().toString();
                        break;
                    case R.id.editTextBuildingName:
                        controlKey = getString(R.string.pref_mp_building);
                        controlValue = txtBuildingName.getText().toString();
                        break;
                    case R.id.editTextUnitNo:
                        controlKey = getString(R.string.pref_mp_unit_no);
                        controlValue = txtUnitNo.getText().toString();
                        break;
                    case R.id.edt_address1:
                        controlKey = getString(R.string.pref_mp_address1);
                        controlValue = edt_address1.getText().toString();
                        break;
                    case R.id.edt_address2:
                        controlKey = getString(R.string.pref_mp_address2);
                        controlValue = edt_address2.getText().toString();
                        break;
                    case R.id.edt_address3:
                        controlKey = getString(R.string.pref_mp_address3);
                        controlValue = edt_address3.getText().toString();
                        break;
                    case R.id.edt_address4:
                        controlKey = getString(R.string.pref_mp_address4);
                        controlValue = edt_address4.getText().toString();
                        break;
                }
                if (!hasFocus && isValid) {
                    pref.edit().putString(controlKey, controlValue).commit();
                }
            }
        };
        //endregion
        //region Linearlayout textchange
        int count = llContactView.getChildCount();
        for (int i = 0; i < count; i++) {
            View v = llContactView.getChildAt(i);
            if (v instanceof EditText) {
                final EditText txt = (EditText) v;
                txt.setOnFocusChangeListener(txtFocusChangeListener);
            }
        }
        int count_address = llmultiaddressView.getChildCount();
        for (int i = 0; i < count_address; i++) {
            View v = llmultiaddressView.getChildAt(i);
            if (v instanceof EditText) {
                final EditText edt = (EditText) v;
                edt.setOnFocusChangeListener(txtFocusChangeListener);
            }
        }
        //endregion
    }

    //region txtFocusChangeListener()
    View.OnFocusChangeListener txtFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            String controlKey = null;
            String controlValue = null;
            switch (v.getId()) {
                case R.id.editText_nricbio:
                    controlKey = getString(R.string.pref_mp_nric);
                    controlValue = txtNRIC.getText().toString();
                    break;
            }
            if (!hasFocus) {
                pref.edit().putString(controlKey, controlValue).apply();
                Log.e("Pref Name>>>", controlKey + ">> " + controlValue);
            }
        }
    };

    //endregion
    //region btnUploadProfileOnClickListener()
    View.OnClickListener btnUploadProfileOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!checkEditText()) {
                if ((pref.getBoolean(getString(R.string.pref_is_account_verified), false)) && (pref.getString(getString(R.string.pref_login_Access_token), "").length() > 0)) {
                    if (from.equals("list")) {
                        if (checkchangesprofile()) {
                            callvolleyupdateprofile("upload");
                        } else {
                            callvolleyinsertMemberprofile(memberid, usertoken, "upload");
                        }
                    } else {
                        callvolleyinsertMemberprofile(memberid, usertoken, "upload");
                    }

                } else {
                    Utils.showAlertDialog(getActivity(), "Upload Fail", getString(R.string.error_response_upload_profile_status_Failed), "ActivityMedicalProfileList");
                }
            } else {
                Utils.showAlertDialog(getActivity(), "Alerts", getString(R.string.alerts_profile));
            }
        }
    };
    //endregion
    //region btnLocalMedProfileOnClickListener()
    View.OnClickListener btnLocalMedProfileOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!checkEditText()) {
                text_error_msg.setVisibility(View.GONE);

                if ((pref.getBoolean(getString(R.string.pref_is_account_verified), false)) && (pref.getString(getString(R.string.pref_login_Access_token), "").length() > 0)) {
                    if (from.equals("list")) {
                        if (checkchangesprofile()) {
                            callvolleyupdateprofile("save");
                        } else {
                            callvolleyinsertMemberprofile(memberid, usertoken, "save");
                        }

                    } else {
                        callvolleyinsertMemberprofile(memberid, usertoken, "save");
                    }

                } else {
                    Utils.showAlertDialog(getActivity(), "Upload Fail", getString(R.string.error_response_upload_profile_status_Failed), "ActivityMedicalProfileList");
                }
            } else {
                Utils.showAlertDialog(getActivity(), "Alerts", getString(R.string.alerts_profile));

            }
        }
    };

    public boolean checkchangesprofile() {
        boolean change = false;
        String textname = txtName.getText().toString();
        String txtdob = edtdob.getText().toString();
        Log.e("checkchange", oldpatientname + "," + textname);
        if (oldpatientname.equals(textname) && olddob.equals(txtdob)) {
            change = false;
        }
        else{
            change = true;
        }
        Log.e("right",change+"");
        return change;
    }

    //region create object()
    public MedicalProfile createMedicalprofile() {
        MedicalProfile medical_profile = new MedicalProfile();
        medical_profile.setId(pref.getInt(getString(R.string.pref_mp_profile_id), 0));
        medical_profile.setNric(pref.getString(getString(R.string.pref_mp_nric), ""));
        medical_profile.setNric_type(pref.getInt(getString(R.string.pref_mp_integer_nric_type), -1));
        medical_profile.setNric_name(pref.getString(getString(R.string.pref_mp_name), ""));
        medical_profile.setGender(pref.getInt(getString(R.string.pref_mp_sex), -1));
        medical_profile.setNationality(pref.getString(getString(R.string.pref_mp_nationality_name), ""));
        medical_profile.setDob(pref.getString(getString(R.string.pref_mp_dob), ""));
        medical_profile.setBlock_no(pref.getString(getString(R.string.pref_mp_block_no), ""));
        medical_profile.setBuilding_name(pref.getString(getString(R.string.pref_mp_building), ""));
        medical_profile.setStreet(pref.getString(getString(R.string.pref_mp_street), ""));
        medical_profile.setUnit_no(pref.getString(getString(R.string.pref_mp_unit_no), ""));
        medical_profile.setPostal_code(pref.getString(getString(R.string.pref_mp_postal_code), ""));
        medical_profile.setTel1(pref.getString(getString(R.string.pref_mp_mobile), ""));
        medical_profile.setTel1_code(pref.getInt(getString(R.string.pref_mp_mobile_code), 0));
        medical_profile.setTel1_iso(pref.getString(getString(R.string.pref_mp_mobile_iso), ""));
        medical_profile.setTel2(pref.getString(getString(R.string.pref_mp_tel), ""));
        medical_profile.setEmail(pref.getString(getString(R.string.pref_mp_email), ""));
        medical_profile.setAllergy(pref.getString(getString(R.string.pref_mp_allergy), ""));
        medical_profile.setMemberid(pref.getString(getString(R.string.pref_login_memberId), ""));
        medical_profile.setRelation(pref.getInt(getString(R.string.pref_mp_integer_relation), -1));
        medical_profile.setMarried_staus(pref.getInt(getString(R.string.pref_mp_integer_married_status), -1));
        medical_profile.setLanguage(pref.getInt(getString(R.string.pref_mp_language), -1));
        medical_profile.setMember(pref.getInt(getString(R.string.pref_mp_member), 0));
        medical_profile.setAddress1(pref.getString(getString(R.string.pref_mp_address1), ""));
        medical_profile.setAddress2(pref.getString(getString(R.string.pref_mp_address2), ""));
        medical_profile.setAddress3(pref.getString(getString(R.string.pref_mp_address3), ""));
        medical_profile.setAddress4(pref.getString(getString(R.string.pref_mp_address4), ""));

        if (!from.equals("list")) {
            medical_profile.setClinic_id(0);

        } else {
            medical_profile.setClinic_id(from_clinicid);
        }

        return medical_profile;
    }

    //endregion
    //endregion
    //region buttonMedProfileDeleteOnClickListener()
    View.OnClickListener buttonMedProfileDeleteOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            alertBuilder.setMessage(getString(R.string.msgbox_delete_profile))
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    })
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            callvolleydeleteprofile();
                            return;

                        }
                    });
            alertBuilder.create().show();

        }
    };

    //endregion
    //endregion
    //region volley api
    //region volley delete memberprofile
    public void callvolleydeleteprofile() {
        if (Utils.hasInternetConnection(getActivity())) {
            String nric = pref.getString(getString(R.string.pref_mp_nric), "");
            int nric_type = pref.getInt(getString(R.string.pref_mp_integer_nric_type), -1);
            String dob = edtdob.getText().toString();
            Requestdeleteprofilejson param_obj = new Requestdeleteprofilejson(memberid, nric, nric_type + "", dob);
            JSONObject param_json = param_obj.StringtoJsonObject(param_obj.objecttoJson());
            Log.e("deleteparam", param_json.toString());
            VolleydeleteMemberListProfile _vdelete = new VolleydeleteMemberListProfile(getActivity());
            _vdelete.GetDeleteMemberprofileRequest(param_json, usertoken, new VolleydeleteMemberListProfile.VolleyCallback() {
                @Override
                public void onSuccess(String message) {
                    Log.e("delete profile", message);
                    dbmedprofile.deleteMedProfile(from_id);
                    pref.edit().putBoolean(getString(R.string.pref_update_profile_flag), true).apply();
                    returnback();
                }

                @Override
                public void onFail(String errorcode, String errormessage) {
                    Log.e("delete profile", errorcode + " " + errormessage);
                    if (errorcode.equals("NotFound")) {
                        dbmedprofile.deleteMedProfile(from_id);
                        pref.edit().putBoolean(getString(R.string.pref_update_profile_flag), true).apply();
                        returnback();
                    } else {
                        Utils.showAlertDialog(getActivity(), "Alert", errormessage);
                    }
                }
            });
        }
    }

    //endregion
    //region volley update memberprofile
    public void callvolleyupdateprofile(final String click) {
        if (Utils.hasInternetConnection(getActivity())) {
            String name = txtName.getText().toString();
            String nric = txtNRIC.getText().toString();
            String dob = edtdob.getText().toString();
            int nricType = pref.getInt(getString(R.string.pref_mp_integer_nric_type), -1);
            int mpmember = pref.getInt(getString(R.string.pref_mp_member), 0);
            String mainaccont = "N";
            if (mpmember > 0) {
                mainaccont = "Y";
            }
            Requestinsertprofilejson param_obj = new Requestinsertprofilejson(memberid, nric, nricType + "", name, dob, mainaccont);
            JSONObject param_json = param_obj.StringtoJsonObject(param_obj.objecttoJson());
            VolleyUpdateMemberListProfile _vupdateprofile = new VolleyUpdateMemberListProfile(getActivity());
            _vupdateprofile.GetUpdateMemberprofileRequest(param_json, usertoken, new VolleyUpdateMemberListProfile.VolleyCallback() {
                @Override
                public void onSuccess(String message) {
                    switch (click) {
                        case "save":
                            Log.e("update profile", message);
                            MedicalProfile obj_medical = createMedicalprofile();
                            dbmedprofile.updateMedicalProfile(obj_medical);
                            Utils.showAlertDialog(getActivity(), "Upload Local Success", getString(R.string.response_upload_profile_status_local_success), "ActivityMedicalProfileList");
                            pref.edit().putBoolean(getString(R.string.pref_update_profile_flag), true).apply();
                            break;
                        case "upload":
                            callQRCodeScreen();
                            break;
                        default:
                            break;
                    }
                }

                @Override
                public void onFail(String errorcode, String errormessage) {
                    Log.e("update profile", errorcode + " " + errormessage);
                    if (errorcode.equals("NotFound")) {
                        callvolleyinsertMemberprofile(memberid, usertoken, click);
                    } else {
                        Utils.showAlertDialog(getActivity(), "Alert", errormessage);
                    }
                }
            });
        }
    }

    //endregion
    //region volley insert memberprofile
    public void callvolleyinsertMemberprofile(final String memberid, String accesstoken, final String click) {
        String name = txtName.getText().toString();
        String nric = txtNRIC.getText().toString();
        String dob = edtdob.getText().toString();
        int nricType = pref.getInt(getString(R.string.pref_mp_integer_nric_type), -1);
        int mpmember = pref.getInt(getString(R.string.pref_mp_member), 0);
        String mainaccont = "N";
        if (mpmember > 0) {
            mainaccont = "Y";
        }
        if (Utils.hasInternetConnection(getActivity())) {
            Requestinsertprofilejson param_obj = new Requestinsertprofilejson(memberid, nric, nricType + "", name, dob, mainaccont);
            JSONObject param_json = param_obj.StringtoJsonObject(param_obj.objecttoJson());
            Log.e("insertparam", param_json.toString());
            VolleyInsertMemberListProfile _vinsertprofile = new VolleyInsertMemberListProfile(getActivity());
            _vinsertprofile.GetInsertMemberprofileRequest(param_json, accesstoken, new VolleyInsertMemberListProfile.VolleyCallback() {
                @Override
                public void onSuccess(String message) {
                    switch (click) {
                        case "save":
                            Log.e("insertProfile", message);
                            MedicalProfile obj_medical = createMedicalprofile();
                            dbmedprofile.updateMedicalProfile(obj_medical);
                            Utils.showAlertDialog(getActivity(), "Upload Local Success", getString(R.string.response_upload_profile_status_local_success), "ActivityMedicalProfileList");
                            pref.edit().putBoolean(getString(R.string.pref_update_profile_flag), true).apply();
                            break;
                        case "upload":
                            callQRCodeScreen();
                            break;
                    }
                }

                @Override
                public void onFail(String errorcode, String errormessage) {
                    Log.e("insertPrfole", errorcode + " " + errormessage);
                    Utils.showAlertDialog(getActivity(), "Alert", errormessage);
                }
            });
        }
    }

    //endregion
    //endregion
    //region QRCodeScreen
    public void callQRCodeScreen() {
        text_error_msg.setVisibility(View.GONE);
        IntentIntegrator integrator = new IntentIntegrator(getActivity());
        integrator.setCaptureLayout(R.layout.custom_capture_layout);
        integrator.setOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        integrator.setPrompt(getString(R.string.scan_title));
        integrator.initiateScan();
    }

    //endregion
    //region GetAddres
    public void getAddress() {
        final String pcode_str = txtPostalCode.getText().toString();
        Log.e("pcode_str", pcode_str);
        if (pcode_str.length() == 6) {
            imgbtn_pcodesearch.setVisibility(View.VISIBLE);
            RequestPcodejson pcode_json = new RequestPcodejson(pcode_str);
            JSONObject json = pcode_json.StringtoJsonObject(pcode_json.objecttoJson());
            if (Utils.hasInternetConnection(getActivity())) {
                VolleyPCodeAddress v_pcode = new VolleyPCodeAddress(getActivity());
                v_pcode.GetPCodeAddressRequest(json, new VolleyPCodeAddress.VolleyCallback() {
                    @Override
                    public void onSuccess(String blockno, String street, String building) {
                        txtBlockNo.setText(blockno);
                        txtStreet.setText(street);
                        txtBuildingName.setText(building);
                        pref.edit().putString(getString(R.string.pref_mp_block_no), blockno)
                                .putString(getString(R.string.pref_mp_street), street)
                                .putString(getString(R.string.pref_mp_building), building)
                                .putString(getString(R.string.pref_mp_postal_code), pcode_str)
                                .apply();
                    }

                    @Override
                    public void onFail(String errorcode, String errormessage) {
                        Utils.showAlertDialog(getActivity(), errorcode, errormessage);
                    }
                });

            } else {
                Utils.showInternetRequiredDialog(getActivity(), getString(R.string.title_internet_require), getString(R.string.msg_no_internet_connection_setup));
                return;
            }

        } else {
            txtPostalCode.setError("Required");
            imgbtn_pcodesearch.setVisibility(View.GONE);
        }
    }
    //endregion
    //region CountryCode

    public void callvolleyCountryCode() {
        if (Utils.hasInternetConnection(getActivity())) {
            VolleyCountryCode v_countryCode = new VolleyCountryCode(getActivity());
            v_countryCode.GetCountryCodeJsonData(new VolleyCountryCode.VolleyCallback() {
                @Override
                public void onSuccess(ArrayList<CountryCode> success) {
                    lst_countrycode = success;
                    if (lst_countrycode.size() > 0) {
                        String isocode = pref.getString(getString(R.string.pref_mp_mobile_iso), "");
                        int phone_code = pref.getInt(getString(R.string.pref_mp_mobile_code), 0);
                        if (isocode.equals("")) {
                            btn_mobiledropdownccode.setText(getResources().getString(R.string.default_countryisocode));
                            btn_mobilecountrycode.setText("+" + getResources().getString(R.string.default_countrycode));

                        } else {
                            btn_mobiledropdownccode.setText(isocode);
                            btn_mobilecountrycode.setText("+" + phone_code);

                        }
                       // getcountryname(isocode);
                        String code = btn_mobilecountrycode.getText().toString().substring(1);
                        pref.edit()
                                .putString(getString(R.string.pref_mp_mobile_iso), btn_mobiledropdownccode.getText().toString())
                                .putInt(getString(R.string.pref_mp_mobile_code), Integer.parseInt(code)).apply();
                    }
                }

                @Override
                public void onFail(String errorcode, String errormessage) {
                    btn_mobiledropdownccode.setText(getResources().getString(R.string.default_countryisocode));
                    btn_mobilecountrycode.setText("+" + getResources().getString(R.string.default_countrycode));
                }
            });
        } else {
            btn_mobiledropdownccode.setText(getResources().getString(R.string.default_countryisocode));
            btn_mobilecountrycode.setText("+" + getResources().getString(R.string.default_countrycode));
        }
    }

    public void LoadCountryCode() {
        adpt_countrycode = new CountryCodeAdapter(getActivity(), lst_countrycode);
        adpt_countrycode.notifyDataSetChanged();
        lvcountrycode.setAdapter(adpt_countrycode);
    }

    public void DialogCountryCode(final String sentfrom) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogcountrycode);

        lvcountrycode = (ListView) dialog.findViewById(R.id.lvcountrycode);
        LoadCountryCode();
        lvcountrycode.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CountryCode code = lst_countrycode.get(position);
                if (sentfrom.equals("mobile")) {

                    btn_mobiledropdownccode.setText(code.getISOCode());
                    btn_mobilecountrycode.setText("+" + code.getCountryCode());

                    layoutcheckeanble(code.getISOCode());

                    if(from.equals("list")){
                        spinnerNricType.setEnabled(false);
                    }else{
                        if(code.getISOCode().equals("SG")){
                            spinnderNricLoad(R.array.array_nric_type);
                            spinnerNricType.setEnabled(true);
                            spinnerNricType.setSelection(1);
                            pref.edit()
                                    .putInt(getString(R.string.pref_mp_integer_nric_type), 0)
                                    .apply();
                        }else{
                            spinnderNricLoad(R.array.array_nric_type_passport);
                            spinnerNricType.setSelection(5);
                            spinnerNricType.setEnabled(false);
                            pref.edit().putInt(getString(R.string.pref_mp_integer_nric_type), 4)
                                    .apply();
                        }
                    }

                    editmp_mobile_code(getString(R.string.pref_mp_mobile_code), code.getCountryCode());
                    editmp_mobile_iso(getString(R.string.pref_mp_mobile_iso), code.getISOCode());
                } else {
                    editmp_mobile_code(getString(R.string.pref_mp_mobile_code), code.getCountryCode());
                    editmp_mobile_iso(getString(R.string.pref_mp_mobile_iso), code.getISOCode());
                }


                dialog.dismiss();
            }
        });
        dialog.show();
    }

    //endregion
    //region layout enable()
    public void layoutcheckeanble(String isocde) {
        if (isocde.equals(getResources().getString(R.string.default_countryisocode)) || isocde.equals("")) {
            llContactView.setVisibility(View.VISIBLE);
            rlpostalcode.setVisibility(View.VISIBLE);
            llmultiaddressView.setVisibility(View.GONE);
            txtNRIC.setHint(getResources().getString(R.string.input_nric));
        } else {
            llmultiaddressView.setVisibility(View.VISIBLE);
            llContactView.setVisibility(View.GONE);
            rlpostalcode.setVisibility(View.GONE);
            txtNRIC.setHint(getResources().getString(R.string.passportno));
        }
    }

    //endregion
    //region onAttach()
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /* private void hideKeyboard(View view) {
         InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
         imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
     }*/
    public void returnback() {
        Intent intent = new Intent(getActivity(), ActivityMedProfileList.class);
        startActivity(intent);
        getActivity().finish();
    }
    //endregion
}
