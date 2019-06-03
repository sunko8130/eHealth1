package sg.com.ehealthassist.ehealthassist.Models.Clinic;

/**
 * Created by User on 6/29/2017.
 */

public class Address {
    String blockno = "", building = "", nearestmrt = "", placetown = "", postal = "", region = "", street = "",
            unitno = "";

    public Address() {
    }

    public Address(String BlockNo, String Street, String UnitNo, String Building, String Postal, String Region, String PlaceTown, String NearestMrt) {
        this.blockno = BlockNo;
        this.building = Building;
        this.nearestmrt = NearestMrt;
        this.placetown = PlaceTown;
        this.postal = Postal;
        this.region = Region;
        this.street = Street;
        this.unitno = UnitNo;
    }

    public String getBlockno() {
        return blockno;
    }

    public void setBlockno(String blockno) {
        this.blockno = blockno;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getNearestmrt() {
        return nearestmrt;
    }

    public void setNearestmrt(String nearestmrt) {
        this.nearestmrt = nearestmrt;
    }

    public String getPlacetown() {
        return placetown;
    }

    public void setPlacetown(String placetown) {
        this.placetown = placetown;
    }

    public String getPostal() {
        return postal;
    }

    public void setPostal(String postal) {
        this.postal = postal;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getUnitno() {
        return unitno;
    }

    public void setUnitno(String unitno) {
        this.unitno = unitno;
    }
}
