package innowise.khorsun.carorderservice.model;

import lombok.Data;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
@Data
public class CarUpdateDto {
    @Column(name = "brand")
    @Size(min = 1,max = 50, message = "Brand's characters should be between 1 and 50")
    @NotEmpty(message = "Brand shouldn't be empty")
    private String brand;
    @Column(name = "model")
    @Size(min = 1,max = 50, message = "Model's characters should be between 1 and 50")
    @NotEmpty(message = "Model shouldn't be empty")
    private String model;
    @Column(name = "is_available")
    private Boolean isAvailable;
}
