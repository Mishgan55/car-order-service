package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.dto.PlaceDto;
import innowise.khorsun.carorderservice.entity.Car;
import innowise.khorsun.carorderservice.mapper.CarMapper;
import innowise.khorsun.carorderservice.model.CarUpdateDto;
import innowise.khorsun.carorderservice.repositorie.CarRepository;
import innowise.khorsun.carorderservice.service.impl.CarServiceImpl;
import innowise.khorsun.carorderservice.util.error.car.CarNotFoundException;
import innowise.khorsun.carorderservice.util.error.car.DuplicateCarPlateNumberException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CarServiceImplTest {
    @InjectMocks
    private CarServiceImpl carService;
    @Mock
    private PlaceService placeService;
    @Mock
    private CarRepository carRepository;
    @Spy
    private CarMapper carMapper;

    @BeforeEach
    public void setUp() {
        carService = new CarServiceImpl(placeService, carRepository, carMapper);
    }

    @Test
    void testAddCar_CarAddedSuccessfully() {
        // Arrange
        CarDto carDto = new CarDto();
        carDto.setPlateNumber("ABC123");
        carDto.setPlaceId(1);
        PlaceDto placeDto = new PlaceDto();
        Car car = new Car();

        when(placeService.getPlaceById(carDto.getPlaceId())).thenReturn(placeDto);
        when(carRepository.findByPlateNumber(carDto.getPlateNumber())).thenReturn(Optional.empty());
        when(carMapper.carDtoToCar(carDto)).thenReturn(car);

        // Act
        carService.addCar(carDto);

        // Assert
        verify(placeService).getPlaceById(carDto.getPlaceId());
        verify(carRepository).findByPlateNumber(carDto.getPlateNumber());
        verify(carRepository).save(car);
    }

    @Test
    void testAddCar_PlateNumberNotUnique() {
        // Arrange
        CarDto carDto = new CarDto();
        carDto.setPlateNumber("ABC123");
        carDto.setPlaceId(1);

        when(carRepository.findByPlateNumber(carDto.getPlateNumber())).thenReturn(Optional.of(new Car()));

        // Act and Assert
        assertThrows(DuplicateCarPlateNumberException.class, () -> carService.addCar(carDto));
    }

    @Test
    void testGetCarDtoById_success() {

        Integer carId = 1;
        Car car = new Car();
        car.setId(carId);

        CarDto carDto = new CarDto();

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(carMapper.carToCarDto(car)).thenReturn(carDto);

        CarDto result = carService.getCarById(carId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(carDto, result);
    }

    @Test
    void testGetCarDtoById_CarNotFound() {
        when(carRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> carService.getCarById(1));
    }

    @Test
    void testGetAllCars_Success() {

        List<CarDto> carDto = List.of(new CarDto(), new CarDto());
        List<Car> cars = List.of(new Car(), new Car());
        when(carRepository.findAll()).thenReturn(cars);
        when(carMapper.carToCarDto(any())).thenReturn(carDto.get(0), carDto.get(1));

        List<CarDto> allCars = carService.getAllCars();

        Assertions.assertNotNull(allCars);
        Assertions.assertEquals(2, allCars.size());
    }

    @Test
    void testGetAvailableCars_Success() {
        List<CarDto> carDto = List.of(new CarDto(), new CarDto());
        List<Car> cars = List.of(new Car(), new Car());

        when(carRepository.findCarsByIsAvailableTrue()).thenReturn(Optional.of(cars));
        when(carMapper.carToCarDto(any())).thenReturn(carDto.get(0), carDto.get(1));

        List<CarDto> result = carService.getAvailableCars();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(carDto, result);
    }

    @Test
    void testGetAvailableCars_NotFoundCars() {
        when(carRepository.findCarsByIsAvailableTrue()).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> carService.getAvailableCars());
    }

    @Test
    void testEditCar_Success() {

        Integer carId = 1;
        String testWord = "Test";

        CarUpdateDto carUpdateDto = new CarUpdateDto();
        carUpdateDto.setBrand(testWord);
        carUpdateDto.setModel(testWord);
        carUpdateDto.setIsAvailable(false);

        Car car = new Car();

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        carService.editCar(carId, carUpdateDto);

        verify(carRepository, times(1)).findById(carId);
        Assertions.assertEquals(car.getModel(), testWord);
        Assertions.assertEquals(car.getBrand(), testWord);
    }

    @Test
    void testEditCar_CarNotFound() {
        Integer carId = 1;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        CarUpdateDto carUpdateDto = new CarUpdateDto();

        assertThrows(CarNotFoundException.class, () -> carService.editCar(carId, carUpdateDto));
    }

    @Test
    void testChangeAvailability_Success() {
        Integer carId = 1;
        Car car = new Car();
        car.setId(carId);
        car.setIsAvailable(true);
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        carService.changeAvailability(carId);

        verify(carRepository, times(2)).findById(1);
        assertFalse(car.getIsAvailable());
    }

    @Test
    void testChangeAvailability_CarNotFound(){
        Integer carId=1;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class,()->carService.changeAvailability(carId));
    }


    @Test
    void testRemoveCar_Success() {
        Integer carId = 1;
        doNothing().when(carRepository).deleteById(carId);

        carService.removeCar(carId);

        verify(carRepository, times(1)).deleteById(carId);
    }
}
