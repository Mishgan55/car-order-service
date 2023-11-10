package innowise.khorsun.carorderservice.controller;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.model.CarUpdateDto;
import innowise.khorsun.carorderservice.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;


@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCar(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(carService.getCarById(id));
    }
    @GetMapping("/get-available-cars")
    public ResponseEntity<List<CarDto>> getAvailableCars(){
        return ResponseEntity.ok(carService.getAvailableCars());
    }

    @GetMapping
    public ResponseEntity<List<CarDto>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @PostMapping("/add")
    public void addCar(@RequestBody @Valid CarDto carDTO) {
        carService.addCar(carDTO);
    }

    @DeleteMapping("/{id}")
    public void removeCar(@PathVariable("id") Integer id) {
        carService.removeCar(id);
    }

    @PatchMapping("/{id}/edit")
    public void editCar(@PathVariable("id") Integer id,
                        @RequestBody @Valid CarUpdateDto carUpdateDto) {
        carService.editCar(id, carUpdateDto);
    }
}
