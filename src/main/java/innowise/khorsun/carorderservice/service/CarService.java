package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.model.CarUpdateDto;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public interface CarService {
    Optional<CarDto> getCarDtoById(Integer id);
    List<CarDto> getAllCars();
    void addCar(CarDto carDto);
    void removeCar(Integer id);
    void editCar(Integer id, CarUpdateDto carUpdateDto);

}
