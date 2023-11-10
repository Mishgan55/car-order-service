package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.entity.Car;
import innowise.khorsun.carorderservice.mapper.CarMapper;
import innowise.khorsun.carorderservice.model.CarUpdateDto;
import innowise.khorsun.carorderservice.repositorie.CarRepository;
import innowise.khorsun.carorderservice.service.CarService;
import innowise.khorsun.carorderservice.service.PlaceService;
import innowise.khorsun.carorderservice.util.PropertyUtil;
import innowise.khorsun.carorderservice.util.error.car.CarNotFoundException;
import innowise.khorsun.carorderservice.util.error.car.DuplicateCarPlateNumberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {
    private final PlaceService placeService;
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Autowired
    public CarServiceImpl(PlaceService placeService, CarRepository carRepository,
                          CarMapper carMapper) {
        this.placeService = placeService;
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    public CarDto getCarById(Integer id) {
        return carRepository.findById(id)
                .map(carMapper::carToCarDto)
                .orElseThrow(() -> new CarNotFoundException(PropertyUtil.CAR_NOT_FOUND,
                        new Date()));
    }

    public List<CarDto> getAllCars() {
      return carRepository.findAll()
                .stream()
                .map(carMapper::carToCarDto)
                .toList();
    }

    @Transactional
    public void addCar(CarDto carDto) {
        checkIfDuplicateCarPlateNumberExist(carDto.getPlateNumber());
        checkIfPlaceExist(carDto.getPlaceId());
        Car car = carMapper.carDtoToCar(carDto);
        carRepository.save(car);
    }

    private void checkIfPlaceExist(Integer placeId) {
        placeService.getPlaceById(placeId);
    }

    private void checkIfDuplicateCarPlateNumberExist(String plateNumber) {
        if (!isCarPlateNumberUnique(plateNumber)) {
            throw new DuplicateCarPlateNumberException(PropertyUtil.DUPLICATE_PLATE_NUMBER, new Date());
        }
    }

    private boolean isCarPlateNumberUnique(String plateNumber) {
        return carRepository.findByPlateNumber(plateNumber).isEmpty();
    }
    @Transactional
    public void removeCar(Integer id) {
        carRepository.deleteById(id);
    }

    @Transactional
    public void editCar(Integer carId, CarUpdateDto carDto) {
        Optional<Car> car = carRepository.findById(carId);
        if (car.isPresent()) {
            Car updatedCar = car.get();
            updatedCar.setBrand(carDto.getBrand());
            updatedCar.setModel(carDto.getModel());
            updatedCar.setIsAvailable(carDto.getIsAvailable());
        } else {
            throw new CarNotFoundException(PropertyUtil.CAR_NOT_FOUND, new Date());
        }
    }

    @Override
    public List<CarDto> getAvailableCars() {
        return carRepository.findCarsByIsAvailableTrue()
                .orElseThrow(()->new CarNotFoundException(PropertyUtil.CAR_NOT_FOUND, new Date()))
                .stream()
                .map(carMapper::carToCarDto).toList();
    }

    @Override
    @Transactional
    public void changeAvailability(Integer carId) {
        Car car = carRepository
                .findById(carId)
                .orElseThrow(() -> new CarNotFoundException(PropertyUtil.CAR_NOT_FOUND, new Date()));
        car.setIsAvailable(!isCarAvailable(carId));
        carRepository.save(car);
    }
    private boolean isCarAvailable(Integer carId){
        return carRepository
                .findById(carId)
                .orElseThrow(() -> new CarNotFoundException(PropertyUtil.CAR_NOT_FOUND, new Date()))
                .getIsAvailable();
    }
}
