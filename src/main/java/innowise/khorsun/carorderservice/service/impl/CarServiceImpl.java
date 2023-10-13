package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.entity.Car;
import innowise.khorsun.carorderservice.repositorie.CarRepository;
import innowise.khorsun.carorderservice.service.CarService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Service
public class CarServiceImpl implements CarService<CarDto> {
    private final CarRepository carRepository;
    private final ModelMapper modelMapper;


    @Autowired
    public CarServiceImpl(ModelMapper modelMapper,CarRepository carRepository) {
        this.modelMapper = modelMapper;
        this.carRepository = carRepository;
    }

    public Optional<CarDto> showOne(int id){
        return carRepository.findById(id).map(this::convertToCarDto);
    }

    public List<CarDto> findAll(){
        return carRepository.findAll().stream().map(this::convertToCarDto).toList();
    }
    @Transactional
    public void create(Car car){
        enrichCar(car);
        carRepository.save(car);
    }
    @Transactional
    public void delete(int id){
        carRepository.deleteById(id);
    }

    @Transactional
    public void update(Integer id, CarDto carDto){
        Optional<CarDto> updatedCarDto = showOne(id);
        if (updatedCarDto.isPresent()){
            CarDto updatedCar = updatedCarDto.get();
            Car car = convertToCar(updatedCar);
            updatingCarFields(car,id,carDto);
            carRepository.save(car);
        }
    }


    private void updatingCarFields(Car car, Integer id, CarDto carDto){
        car.setId(id);
        enrichCar(car);
        car.setBrand(carDto.getBrand());
        car.setModel(carDto.getModel());
        car.setYearOfProduction(carDto.getYearOfProduction());
        car.setPlateNumber(carDto.getPlateNumber());
        car.setIsAvailable(carDto.getIsAvailable());
    }
    private void enrichCar(Car car) {
        car.setBranchId(1);
    }

    public CarDto convertToCarDto(Car car){
        return modelMapper.map(car, CarDto.class);}

    public Car convertToCar(CarDto carDto){
        return modelMapper.map(carDto, Car.class);
    }

}
