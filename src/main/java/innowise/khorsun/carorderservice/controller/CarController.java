package innowise.khorsun.carorderservice.controller;

import innowise.khorsun.carorderservice.DTO.CarDTO;
import innowise.khorsun.carorderservice.models.Car;
import innowise.khorsun.carorderservice.services.CarService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/car")
public class CarController {

    private final CarService carService;
    private final ModelMapper modelMapper;

    @Autowired
    public CarController(CarService carService, ModelMapper modelMapper) {
        this.carService = carService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public Optional<CarDTO> showOneCar(@PathVariable("id")Integer id){
        return carService.showOne(id).map(this::convertToCar);
    }

    @GetMapping
    public List<CarDTO> index(){
        return carService.findAll().stream().map(this::convertToCar).collect(Collectors.toList());
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> createCar(@RequestBody CarDTO carDTO){
        Car car = convertToCarDTO(carDTO);
        carService.create(car);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("id") Integer id){
        carService.delete(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }


    private CarDTO convertToCar(Car car){
        return modelMapper.map(car, CarDTO.class);
    }
    private Car convertToCarDTO(CarDTO carDTO){
        return modelMapper.map(carDTO, Car.class);
    }
}
