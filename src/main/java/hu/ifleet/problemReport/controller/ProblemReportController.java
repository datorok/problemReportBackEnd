package hu.ifleet.problemReport.controller;

import hu.ifleet.problemReport.entity.ProblemReport;
import hu.ifleet.problemReport.entity.ProblemReportChange;
import hu.ifleet.problemReport.service.ProblemReportServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * Author: torokdaniel
 * Date: 2019. 02. 27. 15:47
 * Desciption:
 */
@Controller
public class ProblemReportController {

    @Autowired
    ProblemReportServiceImpl problemReportServiceImpl;

    @GetMapping("/hibalista")
    public String hibalista(Model model) {

        List<ProblemReport> problemReportList = problemReportServiceImpl.getProblemReportsList(1055);
        model.addAttribute("problemReportList", problemReportList);
        return "hibalista";
    }
}
