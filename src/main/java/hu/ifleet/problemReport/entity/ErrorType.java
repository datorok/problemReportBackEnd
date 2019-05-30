package hu.ifleet.problemReport.entity;

import java.util.HashMap;
import java.util.Map;
/**
 * Author: torokdaniel
 * Date: 2019. 03. 05. 14:22
 * Desciption:
 */
public enum ErrorType {
    egyik_sem(0),
    diszpécserközpont(1),
    járműegység(2),
    egyéb(3);

    private int value;
    private static Map map = new HashMap<>();

    ErrorType(int value) {
        this.value = value;
    }

    static {
        for (ErrorType errorType : ErrorType.values()) {
            map.put(errorType.value, errorType);
        }
    }

    public static ErrorType valueOfIntErrorType(int errorType) {
        return (ErrorType) map.get(errorType);
    }

    public int getValue() {
        return value;
    }

}
