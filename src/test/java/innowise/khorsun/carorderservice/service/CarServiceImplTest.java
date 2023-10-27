package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.entity.Car;
import innowise.khorsun.carorderservice.mapper.CarMapper;
import innowise.khorsun.carorderservice.repositorie.CarRepository;
import innowise.khorsun.carorderservice.service.impl.CarServiceImpl;
import innowise.khorsun.carorderservice.util.error.car.CarNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    @InjectMocks
    private CarServiceImpl carService;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarMapper carMapper;

    @Test
    void testGetCarDtoById(){

        Integer carId=1;
        Car car = new Car();
        car.setId(carId);

        CarDto carDto = new CarDto();

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(carMapper.carToCarDto(car)).thenReturn(carDto);

        CarDto result = carService.getCarDtoById(carId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(carDto,result);
    }
    @Test
    void testGetCarDtoById_CarNotFound(){
        when(carRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class,()->carService.getCarDtoById(1));
    }
    @Test
    void testGetAllCars(){

        List<CarDto> carDto = List.of(new CarDto(), new CarDto());
        List<Car> cars = List.of(new Car(), new Car());
        when(carRepository.findAll()).thenReturn(cars);
        when(carMapper.carToCarDto(any())).thenReturn(carDto.get(0),carDto.get(1));

        List<CarDto> allCars = carService.getAllCars();

        Assertions.assertNotNull(allCars);
        Assertions.assertEquals(2,allCars.size());

    }
}
