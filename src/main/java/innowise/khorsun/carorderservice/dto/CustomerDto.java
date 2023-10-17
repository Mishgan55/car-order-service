package innowise.khorsun.carorderservice.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CustomerDto {
    @NotEmpty(message = "Firstname should not be empty!")
    @Size(min = 1,max = 100,message = "Name's characters should be between 1 and 100")
    private String firstName;
    @NotEmpty(message = "Lastname should not be empty!")
    @Size(min = 1,max = 100,message = "Name's characters should be between 1 and 100")
    private String lastName;
    @Email(message = "Enter a valid Email")
    private String email;
    @NotEmpty(message = "Phone number should not be empty!")
    private String phoneNumber;
}
