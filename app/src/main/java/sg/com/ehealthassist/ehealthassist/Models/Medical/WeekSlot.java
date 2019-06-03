package sg.com.ehealthassist.ehealthassist.Models.Medical;

/**
 * Created by User on 8/4/2017.
 */

public class WeekSlot {
    String slotname;
    int slotid ;
    Boolean checked;

    public WeekSlot(){

    }
    public WeekSlot(String slotname, int slotid, Boolean checked) {
        this.slotname = slotname;
        this.slotid = slotid;
        this.checked = checked;
    }

    public String getSlotname() {
        return slotname;
    }

    public void setSlotname(String slotname) {
        this.slotname = slotname;
    }

    public Boolean getChecked() {
        return checked;
    }

    public void setChecked(Boolean checked) {
        this.checked = checked;
    }

    public int getSlotid() {
        return slotid;
    }

    public void setSlotid(int slotid) {
        this.slotid = slotid;
    }
}
