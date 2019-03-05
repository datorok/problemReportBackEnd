package hu.ifleet.problemReport.service;

import hu.ifleet.problemReport.entity.ProblemReport;
import hu.ifleet.problemReport.entity.ProblemReportChange;
import org.springframework.stereotype.Service;


import java.sql.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Author: torokdaniel
 * Date: 2019. 03. 01. 9:42
 * Desciption:
 */

@Service
public class ProblemReportServiceImpl implements ProblemReportService {

    private Connection getConnection() {

        Connection connection = null;
        try {
            connection = DriverManager.getConnection("jdbc:firebirdsql://localhost:3050/D:/databases/IGOR.GDB?defaultHoldable=true", "geza", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    private void closeConnection(Connection connection) {

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<ProblemReport> getProblemReportsList(int compId) {

        Connection connection = getConnection();

        List<ProblemReport> problemReportList = new ArrayList<>();
        List<ProblemReportChange> problemReportChangeList = new ArrayList<>();

        PreparedStatement pstmtGetProblemReports = null;
        PreparedStatement pstmtGetProblemReportChanges = null;
        PreparedStatement pstmtGetReportState = null;


        try {
            pstmtGetProblemReports = connection.prepareStatement("SELECT * FROM PROBLEM_REPORTS WHERE COMP_ID = ?");
            pstmtGetProblemReportChanges = connection.prepareStatement("SELECT * FROM PROBLEM_REPORT_CHANGES WHERE PR_ID = ?");
            pstmtGetReportState = connection.prepareStatement("SELECT name FROM PROBLEM_REPORT_STATES WHERE ID = ?");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            pstmtGetProblemReports.setInt(1, compId);
            ResultSet problemReportResultSet = pstmtGetProblemReports.executeQuery();

            while (problemReportResultSet.next()) {

                String reporterName = "";
                String reporterEmail = "";
                String reporterPhoneNumber = "";

                int reportId = problemReportResultSet.getInt("ID");
                Timestamp reportCreationTime = problemReportResultSet.getTimestamp("T_CREATE");
                String reportCompId = problemReportResultSet.getString("COMP_ID");
                String params = problemReportResultSet.getString("PARAMS");
                String licencePlateNumber = problemReportResultSet.getString("LICENSE_NO");

                String[] paramValues = params.split("\\n");

                for (int i = 0; i < paramValues.length; i++) {

                    if (paramValues[i].length() > 5 && paramValues[i].substring(0, 5).equals("error")) {
                        break;
                    } else if (paramValues[i].length() > 8 && paramValues[i].substring(0, 9).equals("contact_n")) {
                        String[] nameArr = paramValues[i].split("=");
                        if (nameArr[1].length() > 3) {
                            reporterName = nameArr[1];
                        } else {
                            reporterName = "no data";
                        }

                    } else if (paramValues[i].length() > 8 && paramValues[i].substring(0, 9).equals("contact_d")) {
                        String[] contactArr = paramValues[i].split("=");
                        if (contactArr.length == 2) {
                            if (contactArr[1].contains("@")) {
                                reporterEmail = contactArr[1];
                                reporterPhoneNumber = "no data";
                            } else if (contactArr[1].trim().length() > 6) {
                                reporterPhoneNumber = contactArr[1];
                                reporterEmail = "no data";
                            } else {
                                reporterPhoneNumber = "no data";
                                reporterEmail = "no data";
                            }
                        } else if (contactArr.length == 3) {
                            if (contactArr[1].substring(0, 6).equals("email:")) {
                                String[] contactArrColon = contactArr[1].split(":");
                                String[] contactArrColonAndComa = contactArrColon[1].split(",");
                                reporterEmail = contactArrColonAndComa[0];

                                if (contactArrColonAndComa[1].trim().equals("phone_no")) {
                                    reporterPhoneNumber = contactArr[2];
                                }
                            }
                        } else {
                            for (int j = 1; j < contactArr.length - 1; j++) {
                                if (contactArr[j].equals("email")) {
                                    String[] emailArr = contactArr[j + 1].split(",");
                                    if (emailArr[0].contains("@")) {
                                        reporterEmail = emailArr[0].trim();
                                        if (emailArr[1].contains("@")) {
                                            reporterEmail += " és " + emailArr[1].trim();
                                        }
                                    } else {
                                        reporterEmail = "no data";
                                    }
                                    if (emailArr[emailArr.length - 1].trim().equals("phone_no")) {
                                        if (contactArr[j + 2].length() > 6) {
                                            reporterPhoneNumber = contactArr[j + 2];
                                        } else {
                                            reporterPhoneNumber = "no data";
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
                pr.setCompId(reportCompId);
                pr.setLicencePlateNumber(licencePlateNumber);
                pr.setReporterName(reporterName);
                pr.setReporterEmail(reporterEmail);
                pr.setReporterPhoneNumber(reporterPhoneNumber);
                pr.setProblemReportChangeList(new ArrayList<ProblemReportChange>());

                try {
                    pstmtGetProblemReportChanges.setInt(1, reportId);
                    ResultSet problemReportChangesResultSet = pstmtGetProblemReportChanges.executeQuery();


                    while (problemReportChangesResultSet.next()) {
                        int problemReportId = problemReportChangesResultSet.getInt("PR_ID");
                        Timestamp stateChangeTime = problemReportChangesResultSet.getTimestamp("T");
                        int stateChangeActualState = problemReportChangesResultSet.getInt("STATE_ID");
                        String stateChangeMessage = problemReportChangesResultSet.getString("BUG_MESSAGE");
                        ProblemReportChange prc = new ProblemReportChange();
                        prc.setProblemReportId(problemReportId);
                        prc.setStateChangeTime(stateChangeTime);
                        prc.setStateChangeMessage(stateChangeMessage);

                        pstmtGetReportState.setInt(1, stateChangeActualState);
                        ResultSet reportStateResultSet = pstmtGetReportState.executeQuery();
                        String stateString = "";

                        while (reportStateResultSet.next()) {
                            stateString = reportStateResultSet.getString("NAME");
                        }

                        prc.setStateChangeActualState(stateString);
                        problemReportChangeList.add(prc);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                problemReportList.add(pr);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                pstmtGetProblemReports.close();
                pstmtGetProblemReportChanges.close();
                pstmtGetReportState.close();
            } catch (SQLException e) {
                System.err.println(e);
            }
        }
        closeConnection(connection);

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

