package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.model.CarUpdateDto;
import innowise.khorsun.carorderservice.entity.Car;
import innowise.khorsun.carorderservice.mapper.CarMapper;
import innowise.khorsun.carorderservice.repositorie.CarRepository;
import innowise.khorsun.carorderservice.service.CarService;
import innowise.khorsun.carorderservice.util.error.CarNotFoundException;
import innowise.khorsun.carorderservice.util.error.DuplicateCarPlateNumberException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;


    @Autowired
    public CarServiceImpl(CarRepository carRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    public Optional<CarDto> getCarDtoById(Integer id) {
        if (carRepository.findById(id).isEmpty()) {
            throw new CarNotFoundException("Not found");
        }
        return carRepository.findById(id).map(carMapper::carToCarDto);
    }

    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(carMapper::carToCarDto).toList();
    }

    @Transactional
    public void createCar(CarDto carDto) {
        if (!isCarPlateNumberUnique(carDto.getPlateNumber())) {
            throw new DuplicateCarPlateNumberException("Car with this plate number already exists.");
        }
        Car car = carMapper.carDtoToCar(carDto);
        enrichCarForCreate(car);
        carRepository.save(car);
    }

    @Transactional
    public void deleteCar(Integer id) {
        carRepository.deleteById(id);
    }

    @Transactional
    public void updateCar(Integer id, CarUpdateDto carDto) {
        Optional<Car> car = carRepository.findById(id);
        if(car.isPresent()){
            Car updatedCar = car.get();
            updatedCar.setBrand(carDto.getBrand());
            updatedCar.setModel(carDto.getModel());
            updatedCar.setIsAvailable(carDto.getIsAvailable());
        } else {
            throw new CarNotFoundException("Car not found");
        }
    }

    private void enrichCarForCreate(Car car) {
        car.setBranchId(1);
    }

    public boolean isCarPlateNumberUnique(String plateNumber) {
        Optional<Car> existingCar = carRepository.findByPlateNumber(plateNumber);
        return existingCar.isEmpty();
    }


}
