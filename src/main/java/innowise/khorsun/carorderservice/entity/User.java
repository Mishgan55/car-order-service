package innowise.khorsun.carorderservice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "users")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "firstname")
    @NotEmpty(message = "Firstname should not be empty!")
    @Size(min = 1,max = 100,message = "Name's characters should be between 1 and 100")
    private String firstName;
    @Column(name = "lastname")
    @NotEmpty(message = "Lastname should not be empty!")
    @Size(min = 1,max = 100,message = "Name's characters should be between 1 and 100")
    private String lastName;
    @Column(name = "email")
    @Email(message = "Enter a valid Email")
    private String email;
    @Column(name = "phone_number")
    @NotEmpty(message = "Phone number should not be empty!")
    @Pattern(regexp = "^\\+\\d{12}$", message ="Invalid phone number," +
            " write down your number using this pattern +375001234567")
    private String phoneNumber;
    @Column(name = "password")
    private String password;
    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;
    @Column(name = "creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    public enum Role {
        MANAGER("MANAGER"),
        CUSTOMER("CUSTOMER");
        private final String val;

        Role(String val) {
            this.val = val;
        }

        public String getVal() {
            return val;
        }
    }
}
