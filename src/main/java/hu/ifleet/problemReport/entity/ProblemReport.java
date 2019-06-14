package hu.ifleet.problemReport.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: torokdaniel
 * Date: 2019. 02. 25. 10:50
 * Desciption:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemReport {
    private int id;
    private String prid;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime reportCreationTime;
    private String params;
    private Integer compId;
    private String reporterName;
    private String reporterEmail;
    private String reporterPhoneNumber;
    private String licencePlateNumber = "";
    private Integer vehicleId;
    private Integer errorTypeId;
    private ErrorType errorType;
    private Integer actualStatusId;
    private String actualStatusName;
    private String actualStatusColor;
    private String problemDescription;
    private Integer dispId;
    private List<ProblemReportChange> problemReportChangeList = new ArrayList<ProblemReportChange>();

    public ProblemReport(int id, String prid, LocalDateTime reportCreationTime, Integer compId, Integer errorTypeId, ErrorType errorType,
                         int vehicleId, String licencePlateNumber, Integer actualStatusId, String actualStatusName,
                         String params, String actualStatusColor, String problemDescription) {
        this.prid = prid;
        this.id = id;
        this.reportCreationTime = reportCreationTime;
        this.compId = compId;
        this.errorTypeId = errorTypeId;
        this.errorType = errorType;
        this.vehicleId = vehicleId;
        this.licencePlateNumber = licencePlateNumber;
        this.actualStatusId = actualStatusId;
        this.actualStatusName = actualStatusName;
        this.params = params;
        this.actualStatusColor = actualStatusColor;
        this.problemDescription = problemDescription;
        getDataFromParams();
    }

    @JsonIgnore
    private void getDataFromParams(){
        String reporterName = "";
        String reporterEmail = "";
        String reporterPhoneNumber = "";
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
        this.setReporterName(reporterName);
        this.setReporterEmail(reporterEmail);
        this.setReporterPhoneNumber(reporterPhoneNumber);
    }
}
