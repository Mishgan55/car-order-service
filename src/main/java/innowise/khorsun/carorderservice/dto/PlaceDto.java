package innowise.khorsun.carorderservice.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class PlaceDto {
    @Size(min = 1,max = 50, message = "Name's characters should be between 1 and 50")
    @NotEmpty(message = "Name shouldn't be empty")
    private String name;
    @NotEmpty(message = "Address shouldn't be empty")
    private String address;
    @NotEmpty(message = "Work hours shouldn't be empty")
    private String workHours;
}
