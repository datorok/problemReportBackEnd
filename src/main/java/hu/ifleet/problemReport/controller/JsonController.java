package hu.ifleet.problemReport.controller;

import hu.ifleet.problemReport.entity.ProblemReport;
import hu.ifleet.problemReport.service.ProblemReportServiceImpl;
import hu.ifleet.sessionmicroservice.logic.FxSession;
import hu.ifleet.sessionmicroservice.model.response.FxSessionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Author: torokdaniel
 * Date: 2019. 05. 06. 11:37
 * Desciption:
 */
@RestController
public class JsonController {
    @Autowired
    ProblemReportServiceImpl problemReportServiceImpl;

//    @GetMapping("/hibalistajson")
//    public ResponseEntity<List<ProblemReport>> getproblemReportList(@RequestParam(value = "sessionId",required = true) String sessionId) {
//        FxSessionResult sessionfx = new FxSession("problem_report_fx").checkSessionBlocking();
//        return new ResponseEntity<>(problemReportServiceImpl.getProblemReportsList(1071), HttpStatus.OK);
//    }

    @GetMapping("/hibalistajson")
    public List<ProblemReport> getproblemReportList() {
        return problemReportServiceImpl.getProblemReportsList(1071);
    }

}
