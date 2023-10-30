package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.entity.Car;
import innowise.khorsun.carorderservice.entity.Place;
import innowise.khorsun.carorderservice.mapper.CarMapper;
import innowise.khorsun.carorderservice.model.BookingRequestModel;
import innowise.khorsun.carorderservice.model.CarUpdateDto;
import innowise.khorsun.carorderservice.repositorie.CarRepository;
import innowise.khorsun.carorderservice.service.impl.CarServiceImpl;
import innowise.khorsun.carorderservice.service.impl.PlaceServiceImpl;
import innowise.khorsun.carorderservice.util.error.car.CarNotFoundException;
import innowise.khorsun.carorderservice.util.error.car.DuplicateCarPlateNumberException;
import innowise.khorsun.carorderservice.util.error.place.PlaceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
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
    private PlaceServiceImpl placeService;
    @Mock
    private CarRepository carRepository;
    @Mock
    private CarMapper carMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this); // Инициализация моков

        carService = new CarServiceImpl(placeService, carRepository, carMapper);
    }

    @Test
    void testAddCar_Success() {
        CarDto carDto = new CarDto();
        carDto.setBrand("Test");
        carDto.setModel("Test");
        carDto.setYearOfProduction(1900);
        carDto.setPlateNumber("Test");
        carDto.setIsAvailable(true);
        carDto.setDailyFee(BigDecimal.valueOf(0.23));
        carDto.setPlaceId(1);

        when(carRepository.findByPlateNumber("Test")).thenReturn(Optional.empty());
        when(placeService.getPlaceById(1)).thenReturn(Optional.of(new Place()));
        when(carMapper.carDtoToCar(carDto)).thenReturn(new Car());

        assertDoesNotThrow(() -> carService.addCar(carDto));
        verify(carRepository,times(1)).save(any(Car.class));
    }

    @Test
    void testAddCar_DuplicatePlateNumber() {
        CarDto carDto = new CarDto();
        carDto.setPlateNumber("Test");

        when(carRepository.findByPlateNumber("Test")).thenReturn(Optional.of(new Car()));

        assertThrows(DuplicateCarPlateNumberException.class, () -> carService.addCar(carDto));
    }
    @Test
    void testAddCar_PlaceNotFound() {
        CarDto carDto = new CarDto();
        carDto.setPlaceId(1);

        when(placeService.getPlaceById(1)).thenReturn(Optional.empty());

        assertThrows(PlaceNotFoundException.class, () -> carService.addCar(carDto));
    }
    @Test
    void testGetCarDtoById_success(){

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
    void testGetAllCars_Success(){

        List<CarDto> carDto = List.of(new CarDto(), new CarDto());
        List<Car> cars = List.of(new Car(), new Car());
        when(carRepository.findAll()).thenReturn(cars);
        when(carMapper.carToCarDto(any())).thenReturn(carDto.get(0),carDto.get(1));

        List<CarDto> allCars = carService.getAllCars();

        Assertions.assertNotNull(allCars);
        Assertions.assertEquals(2,allCars.size());
    }
    @Test
    void testGetAvailableCars_Success(){
        List<CarDto> carDto = List.of(new CarDto(), new CarDto());
        List<Car> cars = List.of(new Car(), new Car());

        when(carRepository.findCarsByIsAvailableTrue()).thenReturn(Optional.of(cars));
        when(carMapper.carToCarDto(any())).thenReturn(carDto.get(0),carDto.get(1));

        List<CarDto> result = carService.getAvailableCars();

        Assertions.assertNotNull(result);
        Assertions.assertEquals(carDto,result);
    }
    @Test
    void testGetAvailableCars_NotFoundCars(){
        when(carRepository.findCarsByIsAvailableTrue()).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class,()->carService.getAvailableCars());
    }

    @Test
    void testIsCarPlateNumberUnique_UniqueNumber(){
        when(carRepository.findByPlateNumber(any())).thenReturn(Optional.empty());

        Assertions.assertTrue(carService.isCarPlateNumberUnique(any()));
    }
    @Test
    void testIsCarPlateNumberUnique_NotUniqueNumber(){
        when(carRepository.findByPlateNumber(any())).thenReturn(Optional.of(new Car()));

        Assertions.assertFalse(carService.isCarPlateNumberUnique(any()));
    }
    @Test
    void testEditCar_Success(){

        Integer carId=1;
        String testWord="Test";

        CarUpdateDto carUpdateDto = new CarUpdateDto();
        carUpdateDto.setBrand(testWord);
        carUpdateDto.setModel(testWord);
        carUpdateDto.setIsAvailable(false);

        Car car = new Car();

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        carService.editCar(carId,carUpdateDto);

        verify(carRepository,times(1)).findById(carId);
        Assertions.assertEquals(car.getModel(),testWord);
        Assertions.assertEquals(car.getBrand(),testWord);
    }
    @Test
    void testEditCar_CarNotFound(){
        Integer carId=1;
        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        CarUpdateDto carUpdateDto = new CarUpdateDto();

        assertThrows(CarNotFoundException.class,()->carService.editCar(carId,carUpdateDto));
    }
    @Test
    void testSetCarAvailability_Success_WithBookingRequestModel() {
        Integer carId = 1;
        BookingRequestModel bookingRequestModel = new BookingRequestModel();
        bookingRequestModel.setCarId(carId);

        Car car = new Car();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        assertDoesNotThrow(() -> carService.setCarAvailability(bookingRequestModel, true));

        verify(carRepository, times(1)).save(car);
    }
    @Test
    void testSetCarAvailability_CarNotFound_WithBookingRequestModel() {
        BookingRequestModel bookingRequestModel = new BookingRequestModel();
        bookingRequestModel.setCarId(1);

        when(carRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> carService.setCarAvailability(bookingRequestModel, true));
    }

    @Test
    void testSetCarAvailability_Success_WithCarId() {
        Integer carId = 2;
        Car car = new Car();
        when(carRepository.findById(carId)).thenReturn(Optional.of(car));

        assertDoesNotThrow(() -> carService.setCarAvailability(carId, true));

        verify(carRepository, times(1)).save(car);
    }

    @Test
    void testSetCarAvailability_CarNotFound_WithCarId() {
        when(carRepository.findById(3)).thenReturn(Optional.empty()); // Указываем несуществующий carId

        assertThrows(CarNotFoundException.class, () -> carService.setCarAvailability(3, true));
    }
    @Test
    void testRemoveCar_Success(){
        Integer carId=1;
        doNothing().when(carRepository).deleteById(carId);

        carService.removeCar(carId);

        verify(carRepository,times(1)).deleteById(carId);
    }
}
