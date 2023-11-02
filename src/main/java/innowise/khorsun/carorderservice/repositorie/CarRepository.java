package innowise.khorsun.carorderservice.repositorie;

import innowise.khorsun.carorderservice.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car,Integer> {
    Optional<Car> findByPlateNumber(String plateNumber);

    List<Car> findCarsByIsAvailableTrue();
}
