package innowise.khorsun.carorderservice.dto;

import lombok.Data;

@Data
public class CarDto {
    private String brand;
    private String model;
    private Integer yearOfProduction;
    private String plateNumber;
    private Boolean isAvailable;
}
