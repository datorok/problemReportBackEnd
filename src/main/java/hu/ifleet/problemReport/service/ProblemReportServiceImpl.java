package hu.ifleet.problemReport.service;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Date;

import hu.ifleet.problemReport.entity.ErrorType;
import hu.ifleet.problemReport.entity.ProblemReport;
import hu.ifleet.problemReport.entity.ProblemReportChange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Author: torokdaniel
 * Date: 2019. 03. 01. 9:42
 * Desciption:
 */

@Service
public class ProblemReportServiceImpl implements ProblemReportService {
    private Logger logger = LoggerFactory.getLogger(ProblemReportServiceImpl.class);

    private Connection getConnection() {

        Connection connection = null;
        Properties property = new Properties();
        try {
            property.put("user", "GEZA");
            property.put("password", "password");
            property.put("lc_ctype", "WIN1250");
            connection = DriverManager.getConnection("jdbc:firebirdsql://localhost:3050/D:/databases/IGOR.GDB?defaultHoldable=true", property);
        } catch (SQLException e) {
            e.printStackTrace();
            logger.error("DB-hez történő csatlakozás sikertelen", e);
            logger.debug("DB-hez történő csatlakozás sikertelen. Csatlakozási adatok: "+property);
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

    public List<ProblemReportChange> getProblemReportChangeList(int problemReportId) {
        List<ProblemReportChange> result = new ArrayList<>();
        Connection connection = getConnection();
        PreparedStatement pstmt = null;
        try {
        pstmt = connection.prepareStatement("select prc.ID as prcID, prc.T as prcTimestamp, prc.CONTACT_NAME as prcReporter, prc.BUG_MESSAGE as prcMessage,\n" +
                "prc.PR_ID as prcPrid, prc.STATE_ID as prcStateId, prs.NAME as prcStateName\n" +
                "from problem_report_changes prc \n" +
                "join problem_report_states prs on prs.id = prc.state_id\n" +
                "where prc.pr_id = ?\n" +
                "order by prc.ID asc;");
            pstmt.setInt(1, problemReportId);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("PRCID");
                ProblemReportChange tmp = result.stream().filter(prc -> prc.getProblemReportChangeId() == id).findAny().orElse(null);
                if (tmp == null) {
                    tmp = new ProblemReportChange(id, resultSet.getInt("PRCPRID"),
                            resultSet.getTimestamp("PRCTIMESTAMP").toLocalDateTime(), resultSet.getString("PRCSTATENAME"),
                            resultSet.getInt("PRCSTATEID"), resultSet.getString("PRCMESSAGE")
                    );
                    result.add(tmp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection(connection);
//        System.out.println("result.size: " + result.size());
        return result;
    }

    @Override
    public List<ProblemReport> getProblemReportsList(int compId) {
        List<ProblemReport> result = new ArrayList<>();

        Connection connection = getConnection();

        PreparedStatement pstmt = null;
        try {
//          pstmtGetProblemReports = connection.prepareStatement("SELECT * FROM PROBLEM_REPORTS WHERE STATE_ID <> 4 and COMP_ID = ?");
//          pstmtGetProblemReports = connection.prepareStatement("SELECT * FROM PROBLEM_REPORTS WHERE COMP_ID = ?");
//          pstmtGetProblemReportChanges = connection.prepareStatement("SELECT * FROM PROBLEM_REPORT_CHANGES WHERE PR_ID = ?");
//          pstmtGetReportState = connection.prepareStatement("SELECT name FROM PROBLEM_REPORT_STATES WHERE ID = ?");
            pstmt = connection.prepareStatement("select pr.ID as prId, \n" +
                    "pr.PR_ID as prPrid, \n" +
                    "pr.T_CREATE as prCreationTime, \n" +
                    "pr.COMP_ID as prCompanyId,\n" +
                    "pr.ERROR_TYPE as prErrorType, \n" +
                    "pr.VEHICLE_ID as prVehicleId, \n" +
                    "pr.LICENSE_NO as prLicencePlate, \n" +
                    "pr.STATE_ID as prActualStateId, \n" +
                    "prs.NAME as prStateName,  \n" +
                    "pr.PARAMS as prParams, \n" +
                    "prs.COLOR_HTML as prStateColor, \n" +
                    "prc.bug_message as prActualState \n" +
                    "from problem_reports pr\n" +
                    "join problem_report_changes prc on prc.pr_id = pr.id\n" +
                    "join problem_report_states prs on prs.id = pr.state_id and prc.state_id = 0\n" +
                    "where pr.comp_id = ?;");

            pstmt.setInt(1, compId);
            ResultSet resultSet = pstmt.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("PRID");
                ProblemReport tmp = result.stream().filter(pr -> pr.getId() == id).findAny().orElse(null);
                if (tmp == null) {
//                    tmp = new ProblemReport(id, resultSet.getTimestamp("PRCREATIONTIME").toLocalDateTime(),
//                            resultSet.getString("PARAMS"), resultSet.getInt("PRCOMPANYID"),
//                            resultSet.getObject("PRLICENCEPLATE")==null ? "no data" : resultSet.getString("PRLICENCEPLATE"), resultSet.getInt("PRVEHICLEID"),
//                            resultSet.getInt("PRERRORTYPE"), ErrorType.valueOfIntErrorType(resultSet.getInt("PRERRORTYPE")),
//                            resultSet.getInt("PRACTUALSTATEID"), resultSet.getString("PRSTATENAME"),
//                            resultSet.getString("PRSTATECOLOR"), resultSet.getString("PRCMESSAGE")
//                    );
//                    result.add(tmp);
//                }
//                tmp.getProblemReportChangeList().add(new ProblemReportChange(resultSet.getInt("PRCID"), resultSet.getInt("PRCPRID"),
//                        resultSet.getTimestamp("PRCTIMESTAMP").toLocalDateTime(), resultSet.getString("PRCSTATENAME"),
//                        resultSet.getInt("PRCSTATEID"), resultSet.getString("PRCDESCRIPTION")));
//            }

                    tmp = new ProblemReport(id, resultSet.getString("PRPRID"), resultSet.getTimestamp("PRCREATIONTIME").toLocalDateTime(),
                             resultSet.getInt("PRCOMPANYID"), resultSet.getInt("PRERRORTYPE"),
                            ErrorType.valueOfIntErrorType(resultSet.getInt("PRERRORTYPE")),
                            resultSet.getInt("PRVEHICLEID"),
                            resultSet.getObject("PRLICENCEPLATE") == null ? "no data" : resultSet.getString("PRLICENCEPLATE"),
                            resultSet.getInt("PRACTUALSTATEID"), resultSet.getString("PRSTATENAME"),
                            resultSet.getString("PARAMS"),
                            resultSet.getString("PRSTATECOLOR"), resultSet.getString("PRACTUALSTATE")
                    );
                    result.add(tmp);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        closeConnection(connection);
//        result.stream()
//                .filter(problemReport ->problemReport.getLicencePlateNumber() == null || problemReport.getLicencePlateNumber().equals(""))
//                .forEach(problemReport -> {
//                    problemReport.setLicencePlateNumber("no data");
//                });
//        for(int i = 0; i<resultFromDB.size(); i++){
//            if (resultFromDB.get(i).getLicencePlateNumber() == null || resultFromDB.get(i).getLicencePlateNumber().equals("")){
//                resultFromDB.get(i).setLicencePlateNumber("no data");
//            }
//        }
        return result;
    }

    @Override
    public boolean saveNewProblemReport(ProblemReport problemReport){
        System.out.println("problemReport:");
        System.out.println(problemReport);
        Connection connection = getConnection();
        PreparedStatement saveNewReport = null;
        PreparedStatement saveNewReportChange = null;
        //*** PROBLEM_REPORTS TABLE ***                                         *** PROBLEM_REPORT_CHANGES ***
        // ID: INTEGER   NOT NULL!!!!!                                              ID: INTEGER         NOT NULL!!!!!
        // PR_ID: VARCHAR(32)  NOT NULL!!!!!                                        PR_ID: VARCHAR(32)  NOT NULL!!!!!
        // T_CREATE: TIMESTAMP NOT NULL!!!!!                                        T: TIMESTAMP
        // COMP_ID: INTEGER  NOT NULL!!!!!                                          STATE_ID: INTEGER
        // DISP_ID: INTEGER  NOT NULL!!!!!                                          CONTACT_NAME: VARCHAR(255)       +++
        // VEHICLE_ID: INTEGER  +++                                                 STATE_CHANGER_NAME: VARCHAR(255) +++
        // LICENSE_NO: VARCHAR(64)  +++                                             BUG_MESSAGE: VARCHAR(4000)
        // BOX_ID: INTEGER                                                          INSIDE_MESSAGE: VARCHAR(255)
        // UNIT_ID: INTEGER                                                         V_SYNC: INTEGER      NOT NULL!!!!!
        // NODE_ID: INTEGER +++                                                     V_MODIFIED: INTEGER  NOT NULL!!!!!
        // ERROR_TYPE: SMALLINT  +++
        // T_INVESTIGATION: TIMESTAMP
        // PERSON_ID: INTEGER
        // CLOSED: SMALLINT
        // PARAMS: VARCHAR(8192) +++
        // T_CONFIRMATION_SENT: TIMESTAMP
        // STATE_ID: INTEGER  NOT NULL!!!!!
        // AGREE_STATUS: INTEGER  NOT NULL!!!!!
        // LAST_SENT_AGREE_STATUS: INTEGER
        // WS_ID: INTEGER
        // V_SYNC: INTEGER   NOT NULL!!!!!
        // V_MODIFIED: INTEGER  NOT NULL!!!!!
        // T_SERVICE_CONFIRMATION_SENT: TIMESTAMP
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        String params = "contact_name=\"" +  problemReport.getReporterName() + " contact_data=" + problemReport.getReporterEmail() +
                ", phone_no=" + problemReport.getReporterPhoneNumber() + " problem_desc=" +
                problemReport.getProblemDescription()+"\"";
        String prid;
        try {
            if( problemReport.getId() < 1){
                int generatedID =0;
                PreparedStatement pst = connection.prepareStatement("select first 1 gen_id(gen_problem_reports,1) " +
                        "from rdb$database;");
                ResultSet rs = pst.executeQuery();
                if( rs.next() ) {

                    generatedID = rs.getInt(1);
                    problemReport.setId(generatedID);
                    problemReport.setReportCreationTime(LocalDateTime.now());
                }
                prid = "pr-" + sdf.format(today) + "-"+(generatedID+1);
                problemReport.setPrid(prid);
                rs.close();
                pst.close();
            }
            System.out.println("OBJECT BEFORE SAVING: ");
            System.out.println(problemReport);
            saveNewReport = connection.prepareStatement("UPDATE OR INSERT INTO PROBLEM_REPORTS (ID, PR_ID, " +
                    "T_CREATE, COMP_ID, DISP_ID, VEHICLE_ID, LICENSE_NO, ERROR_TYPE, PARAMS, STATE_ID, V_MODIFIED) " +
                    "VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 1 ) matching(id);");
            saveNewReport.setInt(1, problemReport.getId());                                           //ID
            saveNewReport.setString(2, problemReport.getPrid());                                                         //PR_ID
            saveNewReport.setTimestamp(3, Timestamp.valueOf(problemReport.getReportCreationTime()));  //T_CREATE
            saveNewReport.setInt(4, problemReport.getCompId());                                       //COMP_ID
            saveNewReport.setInt(5, problemReport.getDispId());                                       //DISP_ID
            saveNewReport.setInt(6, problemReport.getVehicleId());                                    //VEHICLE_ID
            saveNewReport.setString(7, problemReport.getLicencePlateNumber());                        //LICENSE_NO
            saveNewReport.setInt(8, problemReport.getErrorTypeId());                                  //ERROR_TYPE
            saveNewReport.setString(9, params);                                                       //PARAMS
            saveNewReport.setInt(10, problemReport.getActualStatusId());                              //STATE_ID
            saveNewReport.execute();
            int problemReportChangeId = 0;
            PreparedStatement pst = connection.prepareStatement("select first 1 gen_id(gen_problem_report_changes,1) " +
                    "from rdb$database;");
            ResultSet generatedPRC = pst.executeQuery();
            if( generatedPRC.next() ) {
                problemReportChangeId = generatedPRC.getInt(1);
            }
            pst.close();
            saveNewReportChange = connection.prepareStatement("INSERT INTO PROBLEM_REPORT_CHANGES (ID, PR_ID, T, CONTACT_NAME, " +
                    "BUG_MESSAGE, V_MODIFIED) " +
                    "VALUES (?, ?, ?, ?, ?, 1);");
            saveNewReportChange.setInt(1, problemReportChangeId);                                         //ID
            saveNewReportChange.setInt(2, problemReport.getId());                                         //PR_ID
            saveNewReportChange.setTimestamp(3, Timestamp.valueOf(problemReport.getReportCreationTime()));//T
            saveNewReportChange.setString(4, problemReport.getReporterName());
            saveNewReportChange.setString(5,problemReport.getProblemDescription());
            saveNewReportChange.execute();
            PreparedStatement pstGen = connection.prepareStatement("select first 1 gen_id(gen_v_id,1) from rdb$database;");
            pstGen.execute();
            pstGen.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                closeConnection(connection);
                saveNewReport.close();
                saveNewReportChange.close();
            } catch (SQLException ex) {
                System.err.println(ex);
            }
        }
        return true;
    }
}