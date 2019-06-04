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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
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
        FxSessionResult sessionfx = null;

        sessionfx = new FxSession(/*"problem_report_fx"*/ "log_sheet_generator").checkSessionBlocking(sessionId,"", new ArrayList<>(), null);

        System.out.println("sessionfx of hibalista: "+sessionfx);
        return new ResponseEntity<>(problemReportServiceImpl.getProblemReportsList(sessionfx.getComp_id()), HttpStatus.OK);
    }

    @GetMapping("/valtozasjson")
    public List<ProblemReportChange> getProblemReportChangeList(@RequestParam(value = "sessionId",required = true) String sessionId, @RequestParam(name = "problemReportId", required = true) int problemReportId)
            throws InterruptedException, ExecutionException, URISyntaxException, IOException, FxSessionException {

        FxSessionResult sessionfx = null;

        sessionfx = new FxSession(/*"problem_report_fx"*/ "log_sheet_generator").checkSessionBlocking(sessionId,"", new ArrayList<>(), null);

        System.out.println("sessionfx of valtozasok: "+sessionfx);
        return new ArrayList<>(problemReportServiceImpl.getProblemReportChangeList(problemReportId));
    }

    @GetMapping("/jarmuvekjson")
    public ResponseEntity<List<Object>> getJarmuvekList(@RequestParam(value = "sessionId",required = true) String sessionId) throws InterruptedException, ExecutionException, URISyntaxException, IOException, FxSessionException {

        FxSessionResult sessionfx = null;

        sessionfx = new FxSession(/*"problem_report_fx"*/ "log_sheet_generator").datavehicleSessionBlocking(sessionId,"", "fleet_state", true);

        System.out.println("sessionfx of jarmuvek: "+sessionfx);
//        return new ResponseEntity(sessionfx.getVehicles().stream().map(vehicle-> new Vehicle(vehicle.getId(), vehicle.getLicense_no())).collect(Collectors.toList()), HttpStatus.OK);
        List<Vehicle> vehicle_list = sessionfx.getVehicles().stream()
                .map(vehicle -> new Vehicle(vehicle.getId(), vehicle.getLicense_no()))
                .collect(Collectors.toList());
        return new ResponseEntity(vehicle_list, HttpStatus.OK);
    }

//    @GetMapping("/hibalistajson")
//    public List<ProblemReport> getproblemReportList() {
//        return problemReportServiceImpl.getProblemReportsList(1071);
//   }
}
