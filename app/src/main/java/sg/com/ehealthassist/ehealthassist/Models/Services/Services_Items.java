package sg.com.ehealthassist.ehealthassist.Models.Services;

/**
 * Created by User on 6/29/2017.
 */

public class Services_Items {
    int id;
    String services_name;

    public Services_Items (){}
    public Services_Items(int id, String services_name) {
        this.id = id;
        this.services_name = services_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServices_name() {
        return services_name;
    }

    public void setServices_name(String services_name) {
        this.services_name = services_name;
    }
}
