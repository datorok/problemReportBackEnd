package hu.ifleet.problemReport;

import hu.ifleet.problemReport.entities.ProblemReport;
import hu.ifleet.problemReport.entities.ProblemReportChange;

import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Author: torokdaniel
 * Date: 2019. 02. 25. 11:13
 * Desciption:
 */
public class DBModel {

    private Connection connection;
    private PreparedStatement pstmtGetReports;
    private PreparedStatement pstmtGetReportChanges;

    public DBModel(Connection connection) throws SQLException {
        this.connection = connection;
//        this.pstmtGetReports = connection.prepareStatement("SELECT * FROM PROBLEM_REPORTS where id > 17000");
        this.pstmtGetReports = connection.prepareStatement("SELECT * FROM PROBLEM_REPORTS");
//        this.pstmtGetReportChanges = connection.prepareStatement("SELECT * FROM PROBLEM_REPORT_CHANGES where PR_ID > 17000");
        this.pstmtGetReportChanges = connection.prepareStatement("SELECT * FROM PROBLEM_REPORT_CHANGES");
    }

    public List<ProblemReport> getProblemReports() {
        List<ProblemReport> problemReportList = new ArrayList<>();
        List<ProblemReportChange> problemReportChangeList = new ArrayList<>();
        ResultSet resultSet = null;
        try {
            resultSet = pstmtGetReports.executeQuery();
            while (resultSet.next()) {

                String reporterName = "";
                String reporterEmail = "";
                String reporterPhoneNumber = "";

                int reportId = resultSet.getInt("id");

                Timestamp reportCreationTime = resultSet.getTimestamp("T_CREATE");

                String params = resultSet.getString("params");

                String licencePlateNumber = resultSet.getString("LICENSE_NO");

                String[] paramValues = params.split("\\n");

                for (int i = 0; i < paramValues.length; i++) {

                    if (paramValues[i].length() > 5 && paramValues[i].substring(0, 5).equals("error")) {
                        break;
                    } else if (paramValues[i].length() > 8 && paramValues[i].substring(0, 9).equals("contact_n")) {
                        String[] nameArr = paramValues[i].split("=");
                        if (nameArr[1].length() > 3) {
                            reporterName = nameArr[1];
                        } else {
                            reporterName = "nincs elérhető tárolt adat";
                        }

                    } else if (paramValues[i].length() > 8 && paramValues[i].substring(0, 9).equals("contact_d")) {
                        String[] contactArr = paramValues[i].split("=");
                        if (contactArr.length==2) {
                            if (contactArr[1].contains("@")) {
                                reporterEmail = contactArr[1];
                                reporterPhoneNumber = "nincs elérhető tárolt adat";
                            } else if (contactArr[1].trim().length() > 6) {
                                reporterPhoneNumber = contactArr[1];
                                reporterEmail = "nincs elérhető tárolt adat";
                            } else {
                                reporterPhoneNumber = "nincs elérhető tárolt adat";
                                reporterEmail = "nincs elérhető tárolt adat";
                            }
                        } else {
                            for (int j = 1; j < contactArr.length - 1; j++) {
                                if (contactArr[j].equals("email")) {
                                    String[] emailArr = contactArr[j + 1].split(",");
                                    if (emailArr[0].contains("@")) {
                                        reporterEmail = emailArr[0];
                                    } else {
                                        reporterEmail = "nincs elérhető tárolt adat";
                                    }
                                    if (emailArr[1].trim().equals("phone_no")) {
                                        if (contactArr[j + 2].length() > 6) {
                                            reporterPhoneNumber = contactArr[j + 2];
                                        } else {
                                            reporterPhoneNumber = "nincs elérhető tárolt adat";
                                        }
                                    }
                                } else if (contactArr[j].equals("phone_no ")) {
                                    reporterPhoneNumber = contactArr[j + 1];
                                }
                            }
                        }
                    }
                }
                ProblemReport pr = new ProblemReport();
                pr.setReportId(reportId);
                pr.setReportCreationTime(reportCreationTime);
                pr.setLicencePlateNumber(licencePlateNumber);
                pr.setReporterName(reporterName);
                pr.setReporterEmail(reporterEmail);
                pr.setReporterPhoneNumber(reporterPhoneNumber);
                pr.setProblemReportChangeList(new ArrayList<ProblemReportChange>());
                problemReportList.add(pr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            resultSet = pstmtGetReportChanges.executeQuery();
            while (resultSet.next()) {
                int problemReportId = resultSet.getInt("PR_ID");
                Timestamp stateChangeTime = resultSet.getTimestamp("T");
                int stateChangeActualState = resultSet.getInt("STATE_ID");
                String stateChangeMessage = resultSet.getString("BUG_MESSAGE");

                ProblemReportChange prc = new ProblemReportChange();
                prc.setProblemReportId(problemReportId);
                prc.setStateChangeTime(stateChangeTime);
                prc.setStateChangeActualState(stateChangeActualState);
                prc.setStateChangeMessage(stateChangeMessage);
                problemReportChangeList.add(prc);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        problemReportChangeList.sort(Comparator.comparingInt(ProblemReportChange::getProblemReportId));

        for (int i = 0; i < problemReportChangeList.size(); i++) {

            int tempProblemReportId = problemReportChangeList.get(i).getProblemReportId();
            for (ProblemReport pr : problemReportList) {
                if (pr.getReportId() == tempProblemReportId) {
                    pr.addToProblemReportChangeList(problemReportChangeList.get(i));
                }
            }
        }

        return problemReportList;
    }
}
