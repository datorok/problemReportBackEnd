package hu.ifleet.problemReport.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Author: torokdaniel
 * Date: 2019. 02. 26. 14:11
 * Desciption:
 */

@Data
@NoArgsConstructor
//@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemReportChange {
    private int id;
    private int problemReportId;
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime stateChangeTime;
    private String stateChangeActualStateString;
    private int stateChangeActualStateInt;
    private String stateChangeMessage;

    public ProblemReportChange(int id, int problemReportId, LocalDateTime stateChangeTime, String stateChangeActualStateString, int stateChangeActualStateInt, String stateChangeMessage) {
        this.id = id;
        this.problemReportId = problemReportId;
        this.stateChangeTime = stateChangeTime;
        this.stateChangeActualStateString = stateChangeActualStateString;
        this.stateChangeActualStateInt = stateChangeActualStateInt;
        this.stateChangeMessage = stateChangeMessage;
    }

    public ProblemReportChange(int id, LocalDateTime stateChangeTime, int stateChangeActualStateInt) {
        this.id = id;
        this.stateChangeTime = stateChangeTime;
        this.stateChangeActualStateInt = stateChangeActualStateInt;
    }
    //    @Override
//    public String toString() {
//        return "problemReportId: "+problemReportId + "; státuszváltás időpontja: " + stateChangeTime + "; státuszkód: " + stateChangeActualStateString + "\n" + "üzenet: " + stateChangeMessage;
//    }
//
    public int compare(Object obj1, Object obj2) {
        Integer prid1 = ((ProblemReportChange) obj1).getProblemReportId();
        Integer prid2 = ((ProblemReportChange) obj2).getProblemReportId();

        if (prid1 > prid2) {
            return 1;
        } else if (prid1 < prid2){
            return -1;
        } else {
            return 0;
        }
    }
}
