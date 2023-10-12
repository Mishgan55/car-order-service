package innowise.khorsun.carorderservice.repositories;

import innowise.khorsun.carorderservice.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car,Integer> {
}
