package innowise.khorsun.carorderservice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "places")
@Getter
@Setter
@RequiredArgsConstructor
public class Place {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Size(min = 1,max = 50, message = "Name's characters should be between 1 and 50")
    @NotEmpty(message = "Name shouldn't be empty")
    @Column(name = "name")
    private String name;
    @NotEmpty(message = "Address shouldn't be empty")
    @Column(name = "address")
    private String address;
    @NotEmpty(message = "Work hours shouldn't be empty")
    @Column(name = "work_hours")
    private String workHours;
    @Column(name = "contact_information")
    private String contactInformation;
    @OneToMany(mappedBy = "place")
    private List<Car> car;
}


