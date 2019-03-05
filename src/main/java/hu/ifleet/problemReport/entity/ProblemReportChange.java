package hu.ifleet.problemReport.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Author: torokdaniel
 * Date: 2019. 02. 26. 14:11
 * Desciption:
 */

public class ProblemReportChange {

    private int id;

    private int problemReportId;
    private Timestamp stateChangeTime;
    private String stateChangeActualState;
    private String stateChangeMessage;

    public ProblemReportChange() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProblemReportId() {
        return problemReportId;
    }

    public void setProblemReportId(int problemReportId) {
        this.problemReportId = problemReportId;
    }

    public Timestamp getStateChangeTime() {
        return stateChangeTime;
    }

    public void setStateChangeTime(Timestamp stateChangeTime) {
        this.stateChangeTime = stateChangeTime;
    }

    public String getStateChangeMessage() {
        return stateChangeMessage;
    }

    public void setStateChangeMessage(String stateChangeMessage) {
        this.stateChangeMessage = stateChangeMessage;
    }

    public String getStateChangeActualState() {
        return stateChangeActualState;
    }

    public void setStateChangeActualState(String stateChangeActualState) {
        this.stateChangeActualState = stateChangeActualState;
    }

    @Override
    public String toString() {
        return "problemReportId: "+problemReportId + "; státuszváltás időpontja: " + stateChangeTime + "; státuszkód: " + stateChangeActualState + "\n" + "üzenet: " + stateChangeMessage;
    }

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
