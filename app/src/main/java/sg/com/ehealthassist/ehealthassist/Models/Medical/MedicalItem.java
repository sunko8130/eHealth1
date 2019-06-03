package sg.com.ehealthassist.ehealthassist.Models.Medical;

/**
 * Created by thazinhlaing on 2/7/17.
 */
public class MedicalItem {
    String visitno="";
    String clinicid="";
    String id="";
    String itemno="";
    String medicalType="";
    String medicalCode="";
    String medicalName="";
    String medicalUsage="";
    String medicalDosage="";
    String medicalDosageUnit="";
    String medicalFreq="";
    String medicalFreqCode="";
    String medicalTotalQty="";
    String medicalTotalQtyUnit="";
    String PreCaution1="";
    String PreCaution2="";
    String PreCaution3="";
    String downloaded="";
    Integer is_reminder = 0;
    Integer startdate = 0;
    Integer enddate = 0;
    Long nextdate = 0L;
    Integer numberofdays = 0;
    String slotinterval="";

    public MedicalItem(){

    }
    public MedicalItem(String visitno, String clinicid, String id, String itemno, String medicalType,
                       String medicalCode, String medicalName, String medicalUsage, String medicalDosage,
                       String medicalDosageUnit, String medicalFreq, String medicalFreqCode,
                       String medicalTotalQty, String medicalTotalQtyUnit, String preCaution1,
                       String preCaution2, String preCaution3, String downloaded,Integer is_reminder,Integer startdate,
                       Integer enddate,Long nextdate,Integer numofdays ,String slotinterval) {
        this.visitno = visitno;
        this.clinicid = clinicid;
        this.id = id;
        this.itemno = itemno;
        this.medicalType = medicalType;
        this.medicalCode = medicalCode;
        this.medicalName = medicalName;
        this.medicalUsage = medicalUsage;
        this.medicalDosage = medicalDosage;
        this.medicalDosageUnit = medicalDosageUnit;
        this.medicalFreq = medicalFreq;
        this.medicalFreqCode = medicalFreqCode;
        this.medicalTotalQty = medicalTotalQty;
        this.medicalTotalQtyUnit = medicalTotalQtyUnit;
        this.PreCaution1 = preCaution1;
        this.PreCaution2 = preCaution2;
        this.PreCaution3 = preCaution3;
        this.downloaded = downloaded;
        this.is_reminder = is_reminder;
        this.startdate = startdate;
        this.enddate = enddate;
        this.nextdate = nextdate;
        this.numberofdays = numofdays;
        this.slotinterval = slotinterval;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItemno() {
        return itemno;
    }

    public void setItemno(String itemno) {
        this.itemno = itemno;
    }

    public String getMedicalType() {
        return medicalType;
    }

    public void setMedicalType(String medicalType) {
        this.medicalType = medicalType;
    }

    public String getMedicalCode() {
        return medicalCode;
    }

    public void setMedicalCode(String medicalCode) {
        this.medicalCode = medicalCode;
    }

    public String getMedicalName() {
        return medicalName;
    }

    public void setMedicalName(String medicalName) {
        this.medicalName = medicalName;
    }

    public String getMedicalUsage() {
        return medicalUsage;
    }

    public void setMedicalUsage(String medicalUsage) {
        this.medicalUsage = medicalUsage;
    }

    public String getMedicalDosage() {
        return medicalDosage;
    }

    public void setMedicalDosage(String medicalDosage) {
        this.medicalDosage = medicalDosage;
    }

    public String getMedicalDosageUnit() {
        return medicalDosageUnit;
    }

    public void setMedicalDosageUnit(String medicalDosageUnit) {
        this.medicalDosageUnit = medicalDosageUnit;
    }

    public String getMedicalFreq() {
        return medicalFreq;
    }

    public void setMedicalFreq(String medicalFreq) {
        this.medicalFreq = medicalFreq;
    }

    public String getMedicalFreqCode() {
        return medicalFreqCode;
    }

    public void setMedicalFreqCode(String medicalFreqCode) {
        this.medicalFreqCode = medicalFreqCode;
    }

    public String getMedicalTotalQty() {
        return medicalTotalQty;
    }

    public void setMedicalTotalQty(String medicalTotalQty) {
        this.medicalTotalQty = medicalTotalQty;
    }

    public String getMedicalTotalQtyUnit() {
        return medicalTotalQtyUnit;
    }

    public void setMedicalTotalQtyUnit(String medicalTotalQtyUnit) {
        this.medicalTotalQtyUnit = medicalTotalQtyUnit;
    }

    public String getPreCaution1() {
        return PreCaution1;
    }

    public void setPreCaution1(String preCaution1) {
        PreCaution1 = preCaution1;
    }

    public String getPreCaution2() {
        return PreCaution2;
    }

    public void setPreCaution2(String preCaution2) {
        PreCaution2 = preCaution2;
    }

    public String getPreCaution3() {
        return PreCaution3;
    }

    public void setPreCaution3(String preCaution3) {
        PreCaution3 = preCaution3;
    }

    public String getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(String downloaded) {
        this.downloaded = downloaded;
    }

    public String getVisitno() {
        return visitno;
    }

    public void setVisitno(String visitno) {
        this.visitno = visitno;
    }

    public String getClinicid() {
        return clinicid;
    }

    public void setClinicid(String clinicid) {
        this.clinicid = clinicid;
    }

    public Integer getIs_reminder() {
        return is_reminder;
    }

    public void setIs_reminder(Integer is_reminder) {
        this.is_reminder = is_reminder;
    }

    public Integer getStartdate() {
        return startdate;
    }

    public void setStartdate(Integer startdate) {
        this.startdate = startdate;
    }

    public Integer getEnddate() {
        return enddate;
    }

    public void setEnddate(Integer enddate) {
        this.enddate = enddate;
    }

    public Long getNextdate() {
        return nextdate;
    }

    public void setNextdate(Long nextdate) {
        this.nextdate = nextdate;
    }

    public Integer getNumberofdays() {
        return numberofdays;
    }

    public void setNumberofdays(Integer numberofdays) {
        this.numberofdays = numberofdays;
    }

    public String getSlotinterval() {
        return slotinterval;
    }

    public void setSlotinterval(String slotinterval) {
        this.slotinterval = slotinterval;
    }
}


