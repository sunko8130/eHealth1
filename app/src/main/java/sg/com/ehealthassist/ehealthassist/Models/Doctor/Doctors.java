package sg.com.ehealthassist.ehealthassist.Models.Doctor;

import java.util.List;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class Doctors{
    private int dr_id ;
    private String dr_name;
    private List<String> dr_session;
    // private String dr_session;

    public Doctors(){}
    public Doctors(int dr_id,String name,List<String> session){
        this.dr_id=dr_id;
        this.dr_name=name;
        this.dr_session=session;
    }

    public void setDr_id(int id){
        this.dr_id=id;
    }
    public void setDr_name(String name){
        this.dr_name=name;
    }
    public String getDr_name(){
        return this.dr_name;
    }

    public void setDr_session(List<String> dr_session){
        this.dr_session=dr_session;
    }
    public List<String> getDr_session(){
        return this.dr_session;
    }
    public int getDr_id(){
        return this.dr_id;
    }
}
