package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.CarDto;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface CarService {
    Optional<CarDto> findCarDtoById(Integer id);
    List<CarDto> findAllCar();
    void createCar(CarDto carDto);
    void deleteCar(Integer id);
    void updateCar(Integer id, CarDto carDto);

}
