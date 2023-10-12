package innowise.khorsun.carorderservice.services;

import innowise.khorsun.carorderservice.models.Car;
import innowise.khorsun.carorderservice.repositories.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CarService {
private final CarRepository carRepository;
    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public Optional<Car> showOne(int id){
        return carRepository.findById(id);
    }
    public List<Car> findAll(){
        return carRepository.findAll();
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
    private void enrichCar(Car car) {
        car.setBranchId(1);
    }

}
