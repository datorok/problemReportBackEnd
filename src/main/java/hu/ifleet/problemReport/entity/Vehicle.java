package hu.ifleet.problemReport.entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * Author: torokdaniel
 * Date: 2019. 05. 31. 17:00
 * Desciption:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    private Integer vehicleId;
    private String vehicleLicencePlate;
}
