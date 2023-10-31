package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.model.BookingRequestModel;
import innowise.khorsun.carorderservice.model.CarUpdateDto;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public interface CarService {
    CarDto getCarById(Integer id);
    List<CarDto> getAllCars();
    void addCar(CarDto carDto);
    void removeCar(Integer id);
    void editCar(Integer id, CarUpdateDto carUpdateDto);
    List<CarDto> getAvailableCars();
    void setCarAvailability(BookingRequestModel bookingRequestModel,Boolean status);
    void setCarAvailability(Integer id,Boolean status);
}
