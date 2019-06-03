package sg.com.ehealthassist.ehealthassist.eDocument;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sg.com.ehealthassist.ehealthassist.DB.DBClinicInfo;
import sg.com.ehealthassist.ehealthassist.Models.Clinic.ClinicInfo;
import sg.com.ehealthassist.ehealthassist.R;

public class ActivityRatingClinic extends AppCompatActivity implements android.widget.SearchView.OnQueryTextListener{

    ListView lv_searchclinic;
    SearchView _searchclinicview;
    DBClinicInfo dbcliniclist;
    Context _mcontext;
    public static List<ClinicInfo> arrCliniclist = new ArrayList<ClinicInfo>();
    ClinicSearchAdapter clinicadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        setContentView(R.layout.activity_rating_clinic);
        _mcontext = this;
        dbcliniclist = new DBClinicInfo(_mcontext);
        lv_searchclinic = (ListView)findViewById(R.id.lvclinicsearch);
        _searchclinicview =(SearchView)findViewById(R.id.clinicsearchview);
        _searchclinicview.setOnQueryTextListener(this);

        loadclinicinfo();

        lv_searchclinic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ActivityRating.txtclinicname.setText(arrCliniclist.get(i).get_name());
                ActivityRating.clinic_code= arrCliniclist.get(i).getHecode();
                finish();
            }
        });
    }

    public void loadclinicinfo(){
        arrCliniclist = dbcliniclist.getAllClinicList();
        clinicadapter = new ClinicSearchAdapter(_mcontext,arrCliniclist);
        lv_searchclinic.setAdapter(clinicadapter);
        clinicadapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        clinicadapter.filter(text);
        return false;
    }
}
