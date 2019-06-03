package sg.com.ehealthassist.ehealthassist.Models.EDocument;

/**
 * Created by User on 9/7/2017.
 */

public class RADCapture {
    int id;
    String memberid;
    String title;
    String createdate;
    String desc;
    boolean isreview;

    public RADCapture(){}
    public RADCapture(int id, String memberid, String title, String createdate, String desc ,boolean isreview) {
        this.id = id;
        this.memberid = memberid;
        this.title = title;
        this.createdate = createdate;
        this.desc = desc;
        this.isreview = isreview;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMemberid() {
        return memberid;
    }

    public void setMemberid(String memberid) {
        this.memberid = memberid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isreview() {
        return isreview;
    }

    public void setIsreview(boolean isreview) {
        this.isreview = isreview;
    }
}
