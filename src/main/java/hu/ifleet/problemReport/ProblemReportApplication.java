package hu.ifleet.problemReport;

import hu.ifleet.problemReport.entity.ProblemReport;
import hu.ifleet.problemReport.service.ProblemReportServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

@SpringBootApplication
public class ProblemReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProblemReportApplication.class, args);

//        ProblemReportServiceImpl prsi = new ProblemReportServiceImpl();
//
//        List<ProblemReport> prl;
//
//        prl = prsi.getProblemReportsList();
//
//        System.out.println("prl.size()= "+prl.size());
//        try {
//            BufferedWriter bw = new BufferedWriter(new FileWriter("d:\\projects\\output\\output.txt"));
//        for (int i = 0; i < prl.size(); i++) {
//            StringBuilder output = new StringBuilder();
//
//            output.append("problemReport id: " + prl.get(i).getReportId()+ "\n");
//            output.append("létrehozás dátuma: " + prl.get(i).getReportCreationTime()+ "\n");
//            output.append("cég id: " + prl.get(i).getCompId()+ "\n");
//            output.append("bejelentő neve: " + prl.get(i).getReporterName() + "\n");
//            output.append("bejelentő e-mail címe: " + prl.get(i).getReporterEmail() + "\n");
//            output.append("bejelentő telefonszáma: " + prl.get(i).getReporterPhoneNumber() + "\n");
//            output.append("érintett rendszám: " + prl.get(i).getLicencePlateNumber()+ "\n");
//            output.append("\n");
//            for (int j = 0; j < prl.get(i).getProblemReportChangeList().size(); j++) {
//                output.append(prl.get(i).getProblemReportChangeList().get(j).toString() + "\n");
//                output.append("\n");
//            }
//            output.append("\n");
//            output.append("*****************************************************************************************"+ "\n");
//            bw.append(output.toString());
//        }
//        bw.flush();
//        bw.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

    }
}
