package sg.com.ehealthassist.ehealthassist.Medication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import sg.com.ehealthassist.ehealthassist.R;

public class FragmentMedCondition extends Fragment {
    SharedPreferences preferences = null;
    EditText txtAllergy;
    String from = "";
    int from_clinicid=0;

    public FragmentMedCondition() {
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
        View content = INFLATER.inflate(R.layout.fragment_medcondition, container, false);
        preferences = getActivity().getSharedPreferences(getString(R.string.preference_name), getActivity().MODE_PRIVATE);

        findViewsById(content);
        from = getArguments().getString("from");
        from_clinicid = getArguments().getInt("clinicid", 0);
        if (from.equals("list")) {
            loadData();
        }
        setControlsEvent();
        return content;
    }

    //region findViewsById()
    private void findViewsById(View content) {
        txtAllergy = (EditText) content.findViewById(R.id.editTextAllergy);
    }

    //endregion
    //region setControlsEvent()
    private void setControlsEvent() {
        View.OnFocusChangeListener txtFocusChanged = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                String controlKey = null;
                String controlValue = null;
                boolean isValid = true;
                switch (v.getId()) {
                    case R.id.editTextAllergy:
                        controlKey = getString(R.string.pref_mp_allergy);
                        controlValue = txtAllergy.getText().toString();
                        break;
                }
                if (!hasFocus && isValid) {
                    preferences.edit().putString(controlKey, controlValue).commit();
                }
            }
        };
        txtAllergy.setOnFocusChangeListener(txtFocusChanged);
    }

    //endregion
    //region LoadData()
    private void loadData() {
        txtAllergy.setText(preferences.getString(getString(R.string.pref_mp_allergy), ""));
    }

    //endregion
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
}
