package hu.ifleet.problemReport.controller;

import hu.ifleet.problemReport.entity.Vehicle;
import hu.ifleet.problemReport.service.ProblemReportServiceImpl;
import hu.ifleet.problemReport.entity.ProblemReport;
import hu.ifleet.problemReport.entity.ProblemReportChange;
import hu.ifleet.sessionmicroservice.logic.FxSession;
import hu.ifleet.sessionmicroservice.model.exception.FxSessionException;
import hu.ifleet.sessionmicroservice.model.response.FxSessionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * Author: torokdaniel
 * Date: 2019. 05. 06. 11:37
 * Desciption:
 */
@RestController
public class JsonController {
    @Autowired
    ProblemReportServiceImpl problemReportServiceImpl;

    @GetMapping("/hibalistajson")
    public ResponseEntity<List<ProblemReport>> getProblemReportList(@RequestParam(value = "sessionId",required = true) String sessionId) throws InterruptedException, ExecutionException, URISyntaxException, IOException, FxSessionException {
        System.out.println("sessionId: " + sessionId);
        FxSessionResult sessionfx = FxSession.getInstance("problem_report_fx").checkSessionBlocking(sessionId);
        System.out.println("COMPID: "+ sessionfx.getComp_id());
        System.out.println("sessionfx of hibalista: "+sessionfx);
        return new ResponseEntity<>(problemReportServiceImpl.getProblemReportsList(sessionfx.getComp_id()), HttpStatus.OK);
    }

    @GetMapping("/valtozasjson")
    public ResponseEntity<List<ProblemReportChange>> getProblemReportChangeList(@RequestParam(value = "sessionId",required = true) String sessionId, @RequestParam(name = "problemReportId", required = true) int problemReportId)
            throws InterruptedException, ExecutionException, URISyntaxException, IOException, FxSessionException {

        FxSessionResult sessionfx =  FxSession.getInstance("problem_report_fx").checkSessionBlocking(sessionId);

        System.out.println("sessionfx of valtozasok: "+sessionfx);
        List<ProblemReportChange> result = problemReportServiceImpl.getProblemReportChangeList(problemReportId);
        System.out.println(result.toString());
        return new ResponseEntity(result,HttpStatus.OK);
    }

    @GetMapping("/jarmuvekjson")
    public ResponseEntity<List<Object>> getJarmuvekList(@RequestParam(value = "sessionId",required = true) String sessionId) throws InterruptedException, ExecutionException, URISyntaxException, IOException, FxSessionException {
    System.out.println("session: "+ sessionId);

        FxSessionResult sessionfx = FxSession.getInstance("problem_report_fx").datavehicleSessionBlocking(sessionId,"fleet_state"); //fleet_state helyett a hibabejelentő mostani funkciókódját (action) kell majd beírni

        System.out.println("sessionfx of jarmuvek: "+sessionfx);
//        return new ResponseEntity(sessionfx.getVehicles().stream().map(vehicle-> new Vehicle(vehicle.getId(), vehicle.getLicense_no())).collect(Collectors.toList()), HttpStatus.OK);
        List<Vehicle> vehicle_list = sessionfx.getVehicles().stream()
                .map(vehicle -> new Vehicle(vehicle.getId(), vehicle.getLicense_no()))
                .collect(Collectors.toList());
        return new ResponseEntity(vehicle_list, HttpStatus.OK);
    }

    @PostMapping("/problemreportpersist")
    public void saveNewProblemReport(@RequestBody ProblemReport problemreport, @RequestParam String sessionId)
            throws InterruptedException, ExecutionException, URISyntaxException, IOException, FxSessionException{
        System.out.println(sessionId);
        System.out.println("PROBLEMREPORT: ");
        System.out.println(problemreport);
        FxSessionResult sessionfx =  FxSession.getInstance("problem_report_fx").checkSessionBlocking(sessionId);
        problemreport.setCompId(sessionfx.getComp_id());
        problemreport.setDispId(sessionfx.getDisp_id());
        problemReportServiceImpl.saveNewProblemReport(problemreport);

    }

//    @GetMapping("/hibalistajson")
//    public List<ProblemReport> getproblemReportList() {
//        return problemReportServiceImpl.getProblemReportsList(1071);
//   }
}
