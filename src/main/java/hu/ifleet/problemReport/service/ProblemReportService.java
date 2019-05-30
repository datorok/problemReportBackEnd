package hu.ifleet.problemReport.service;

import hu.ifleet.problemReport.entity.ProblemReport;
import hu.ifleet.problemReport.entity.ProblemReportChange;
import java.util.List;

/**
 * Author: torokdaniel
 * Date: 2019. 03. 01. 9:33
 * Desciption:
 */

public interface ProblemReportService {

//    void addAProblemReport(int compId, String licencePlateNumber,String reporterName, String reporterEmail, String reporterPhoneNumber, List<ProblemReportChange> problemReportChangeList);
//    void modifyAProblemReportChanges(ProblemReport problemReport);
//    void deleteProblemReport(int reportId);
    List<ProblemReport> getProblemReportsList(int compId);
    boolean saveNewProblemReport(ProblemReport problemReport);
    //List<ProblemReportChange> getProblemReportChangeListForAReportId(int reportId);
}
