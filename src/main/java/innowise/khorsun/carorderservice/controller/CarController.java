package innowise.khorsun.carorderservice.controller;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.service.impl.CarServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public Optional<CarDto> showOneCar(@PathVariable("id") Integer id) {
        return carService.showOne(id);
    }

    @GetMapping
    public List<CarDto> index() {
        return carService.findAll();
    }

    @PostMapping("/add")
    public ResponseEntity<HttpStatus> createCar(@RequestBody @Valid CarDto carDTO) {
        carService.create(carDTO);
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Integer id) {
        carService.delete(id);
    }

    @PatchMapping("/{id}/edit")
    public void update(@PathVariable("id") Integer id, @RequestBody @Valid CarDto carDto) {
        carService.update(id, carDto);
    }
}
