package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.entity.Car;
import innowise.khorsun.carorderservice.mapper.CarMapper;
import innowise.khorsun.carorderservice.model.CarUpdateDto;
import innowise.khorsun.carorderservice.repositorie.CarRepository;
import innowise.khorsun.carorderservice.repositorie.PlaceRepository;
import innowise.khorsun.carorderservice.service.CarService;
import innowise.khorsun.carorderservice.util.error.car.CarNotFoundException;
import innowise.khorsun.carorderservice.util.error.car.DuplicateCarPlateNumberException;
import innowise.khorsun.carorderservice.util.error.place.PlaceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CarServiceImpl implements CarService {
    private final PlaceRepository placeRepository;
    private final CarRepository carRepository;
    private final CarMapper carMapper;


    @Autowired
    public CarServiceImpl(PlaceRepository placeRepository, CarRepository carRepository, CarMapper carMapper) {
        this.placeRepository = placeRepository;
        this.carRepository = carRepository;
        this.carMapper = carMapper;
    }

    public CarDto getCarDtoById(Integer id) {
        return carRepository.findById(id)
                .map(carMapper::carToCarDto)
                .orElseThrow(() -> new CarNotFoundException("Not found",new Date()));
    }

    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(carMapper::carToCarDto).toList();
    }

    @Transactional
    public void addCar(CarDto carDto) {
        if (!isCarPlateNumberUnique(carDto.getPlateNumber())) {
            throw new DuplicateCarPlateNumberException("Car with this plate number already exists.",new Date());
        }
        if (placeRepository.findById(carDto.getPlaceId()).isEmpty()){
            throw new PlaceNotFoundException("Place not found",new Date());
        }
        Car car = carMapper.carDtoToCar(carDto);
        carRepository.save(car);
    }

    @Transactional
    public void removeCar(Integer id) {
        carRepository.deleteById(id);
    }

    @Transactional
    public void editCar(Integer id, CarUpdateDto carDto) {
        Optional<Car> car = carRepository.findById(id);
        if (car.isPresent()) {
            Car updatedCar = car.get();
            updatedCar.setBrand(carDto.getBrand());
            updatedCar.setModel(carDto.getModel());
            updatedCar.setIsAvailable(carDto.getIsAvailable());
        } else {
            throw new CarNotFoundException("Car not found",new Date());
        }
    }

    public boolean isCarPlateNumberUnique(String plateNumber) {
        return carRepository.findByPlateNumber(plateNumber).isEmpty();
    }


}
