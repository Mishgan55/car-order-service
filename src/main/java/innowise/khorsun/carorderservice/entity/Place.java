package innowise.khorsun.carorderservice.entity;

import lombok.Getter;
import lombok.Setter;


import org.locationtech.jts.geom.Geometry;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "places")
@Getter
@Setter
public class Place{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;
    @Column(name = "location")
    private Geometry location;
    @OneToMany(mappedBy = "place")
    private List<Car> car;
}


