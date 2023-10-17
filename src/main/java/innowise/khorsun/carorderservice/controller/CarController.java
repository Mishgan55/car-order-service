package innowise.khorsun.carorderservice.controller;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.model.CarUpdateDto;
import innowise.khorsun.carorderservice.service.impl.CarServiceImpl;
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
import java.util.Optional;


@RestController
@RequestMapping("/car")
public class CarController {

    private final CarServiceImpl carService;

    @Autowired
    public CarController(CarServiceImpl carService) {
        this.carService = carService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<CarDto>> getOneCar(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(carService.getCarDtoById(id));
    }

    @GetMapping
    public ResponseEntity<List<CarDto>> getAllCars() {
        return ResponseEntity.ok(carService.getAllCars());
    }

    @PostMapping("/add")
    public void createCar(@RequestBody @Valid CarDto carDTO) {
        carService.createCar(carDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(@PathVariable("id") Integer id) {
        carService.deleteCar(id);
    }

    @PatchMapping("/{id}/edit")
    public void updateCar(@PathVariable("id") Integer id, @RequestBody @Valid CarUpdateDto carUpdateDto) {
        carService.updateCar(id, carUpdateDto);
    }
}
