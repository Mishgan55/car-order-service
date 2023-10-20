package innowise.khorsun.carorderservice.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;


@Entity
@Table(name = "cars")
@Getter
@Setter
@RequiredArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "brand")
    @Size(min = 1,max = 50, message = "Brand's characters should be between 1 and 50")
    @NotEmpty(message = "Brand shouldn't be empty")
    private String brand;
    @Column(name = "model")
    @Size(min = 2,max = 50, message = "Model's characters should be between 2 and 50")
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
    @Column(name = "daily_fee")
    private BigDecimal dailyFee;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id",referencedColumnName ="id" )
    private Place place;
}
