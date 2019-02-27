package hu.ifleet.problemReport;

import hu.ifleet.problemReport.entities.ProblemReport;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

@SpringBootApplication
public class ProblemReportApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProblemReportApplication.class, args);

        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:firebirdsql://localhost:3050/D:/databases/IGOR.GDB", "geza", "password");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        DBModel dbModel = null;
        try {
            dbModel = new DBModel(conn);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String output = "";
        List<ProblemReport> prl = dbModel.getProblemReports();
        System.out.println("prl.size()= "+prl.size());
        for (int i = 0; i < prl.size(); i++) {
//            for (int j = 0; j < prl.get(i).getProblemReportChangeList().size(); j++) {
//                System.out.println(prl.get(i).getProblemReportChangeList().get(j).toString());
//            }
            output += "name: " + prl.get(i).getReporterName() + "\n";
            output += "e-mail: " + prl.get(i).getReporterEmail() + "\n";
            output += "phone: " + prl.get(i).getReporterPhoneNumber() + "\n";
            output += "*****************************************************************************************"+ "\n";
        }
        System.out.println("output.length = " + output.length());
        try {
            BufferedWriter br = new BufferedWriter(new FileWriter("d:\\projects\\output\\output.txt"));
            br.write(output);
            br.flush();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
