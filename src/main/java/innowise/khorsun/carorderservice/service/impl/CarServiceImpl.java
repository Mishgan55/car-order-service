package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.CarDto;
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
public class CarServiceImpl implements CarService<CarDto> {
    private final CarRepository carRepository;


    @Autowired
    public CarServiceImpl(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Optional<CarDto> showOne(int id) {
        if(carRepository.findById(id).isEmpty()){
            throw new CarNotFoundException("Not found");
        }
        return carRepository.findById(id).map(CarMapper.INSTANCE::carToCarDto);
    }

    public List<CarDto> findAll() {
        return carRepository.findAll().stream().map(CarMapper.INSTANCE::carToCarDto).toList();
    }

    @Transactional
    public void create(CarDto carDto) {
        if (!isCarPlateNumberUnique(carDto.getPlateNumber())) {
            throw new DuplicateCarPlateNumberException("Car with this plate number already exists.");
        }
        Car car = CarMapper.INSTANCE.carDtoToCar(carDto);
        enrichCar(car);
        carRepository.save(car);
    }

    @Transactional
    public void delete(int id) {
        carRepository.deleteById(id);
    }

    @Transactional
    public void update(Integer id, CarDto carDto) {

        Optional<CarDto> updatedCarDto = showOne(id);
        if (updatedCarDto.isPresent()) {
            CarDto updatedCar = updatedCarDto.get();
            Car car = CarMapper.INSTANCE.carDtoToCar(updatedCar);
            updatingCarFields(car, id, carDto);
            carRepository.save(car);
        } else {
            throw new CarNotFoundException("Car not found");
        }
    }


    private void updatingCarFields(Car car, Integer id, CarDto carDto) {
        car.setId(id);
        enrichCar(car);
        car.setBrand(carDto.getBrand());
        car.setModel(carDto.getModel());
        car.setYearOfProduction(carDto.getYearOfProduction());
        car.setPlateNumber(car.getPlateNumber());
        car.setIsAvailable(carDto.getIsAvailable());
    }

    private void enrichCar(Car car) {
        car.setBranchId(1);
    }

    public boolean isCarPlateNumberUnique(String plateNumber) {
        Optional<Car> existingCar = carRepository.findByPlateNumber(plateNumber);
        return existingCar.isEmpty();
    }


}
