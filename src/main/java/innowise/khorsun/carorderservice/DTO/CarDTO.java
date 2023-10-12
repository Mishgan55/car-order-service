package innowise.khorsun.carorderservice.DTO;

import lombok.Data;

@Data
public class CarDTO {
    String brand;
    String model;
    Integer yearOfProduction;
    String plateNumber;
    Boolean isAvailable;
}
