package innowise.khorsun.carorderservice.controller;

import innowise.khorsun.carorderservice.dto.PlaceDto;
import innowise.khorsun.carorderservice.service.PlaceService;
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
@RequestMapping("/places")
public class PlaceController {
    private final PlaceService placeService;
    @Autowired
    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }
    @GetMapping("/{id}")
    public ResponseEntity<PlaceDto> getPlaceDto(@PathVariable("id") Integer id){
        return ResponseEntity.ok(placeService.getPlaceById(id));
    }
    @GetMapping()
    public ResponseEntity<List<PlaceDto>> getAllPlaces(){
        return ResponseEntity.ok(placeService.getAllPlaces());
    }
    @PostMapping("/add")
    public void addPlace(@RequestBody @Valid PlaceDto placeDto){
        placeService.addPlace(placeDto);
    }
    @DeleteMapping("/{id}")
    public void removePlace(@PathVariable("id")Integer id){
        placeService.removePlace(id);
    }
    @PatchMapping("/{id}/edit")
    public void editPlace(@PathVariable("id")Integer id,
                          @RequestBody @Valid PlaceDto placeDto){
        placeService.editPlace(id,placeDto);
    }
}
