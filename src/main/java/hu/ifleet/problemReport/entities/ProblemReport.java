package hu.ifleet.problemReport.entities;

import java.sql.Timestamp;
import java.util.List;

/**
 * Author: torokdaniel
 * Date: 2019. 02. 25. 10:50
 * Desciption:
 */
public class ProblemReport {

    private int reportId;
    private Timestamp reportCreationTime;
    private String params;
    private String reporterName;
    private String reporterEmail;
    private String reporterPhoneNumber;
    private String licencePlateNumber;
    private List<ProblemReportChange> problemReportChangeList;

    public ProblemReport() {
    }

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public Timestamp getReportCreationTime() {
        return reportCreationTime;
    }

    public void setReportCreationTime(Timestamp reportCreationTime) {
        this.reportCreationTime = reportCreationTime;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterEmail() {
        return reporterEmail;
    }

    public void setReporterEmail(String reporterEmail) {
        this.reporterEmail = reporterEmail;
    }

    public String getReporterPhoneNumber() {
        return reporterPhoneNumber;
    }

    public void setReporterPhoneNumber(String reporterPhoneNumber) {
        this.reporterPhoneNumber = reporterPhoneNumber;
    }

    public String getLicencePlateNumber() {
        return licencePlateNumber;
    }

    public void setLicencePlateNumber(String licencePlateNumber) {
        this.licencePlateNumber = licencePlateNumber;
    }

    public List<ProblemReportChange> getProblemReportChangeList() {
        return problemReportChangeList;
    }

    public void setProblemReportChangeList(List<ProblemReportChange> problemReportChangeList) {
        this.problemReportChangeList = problemReportChangeList;
    }
    public void addToProblemReportChangeList(ProblemReportChange prc){
        problemReportChangeList.add(prc);
    }
}
