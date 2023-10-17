package innowise.khorsun.carorderservice.dto;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CarDto {
    @Column(name = "brand")
    @Size(min = 1,max = 50, message = "Brand's characters should be between 1 and 50")
    @NotEmpty(message = "Brand shouldn't be empty")
    private String brand;
    @Column(name = "model")
    @Size(min = 1,max = 50, message = "Model's characters should be between 1 and 50")
    @NotEmpty(message = "Model shouldn't be empty")
    private String model;
    @Column(name = "year_of_production")
    @Min(value = 2000,message = "Year should be greater then 2000")
    private Integer yearOfProduction;
    @Column(name = "plate_number")
    @NotEmpty(message = "Plate number should not be empty")
    private String plateNumber;
    @Column(name = "is_available")
    private Boolean isAvailable;
}
