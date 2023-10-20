package innowise.khorsun.carorderservice.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class CarDto {
    @Size(min = 1,max = 50, message = "Brand's characters should be between 1 and 50")
    @NotEmpty(message = "Brand shouldn't be empty")
    private String brand;
    @Size(min = 1,max = 50, message = "Model's characters should be between 1 and 50")
    @NotEmpty(message = "Model shouldn't be empty")
    private String model;
    @Min(value = 2000,message = "Year should be greater then 2000")
    private Integer yearOfProduction;
    @NotEmpty(message = "Plate number should not be empty")
    private String plateNumber;
    private Boolean isAvailable;
    @NotEmpty(message = "Daily fee should not be empty")
    private BigDecimal dailyFee;
}
