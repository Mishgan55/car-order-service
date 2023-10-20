package innowise.khorsun.carorderservice.repositorie;

import innowise.khorsun.carorderservice.entity.Place;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends JpaRepository<Place,Integer> {
}
