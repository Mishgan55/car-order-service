package innowise.khorsun.carorderservice.controller;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsRoute;
import innowise.khorsun.carorderservice.model.GeoCarResponse;
import innowise.khorsun.carorderservice.model.GeoRequestModel;
import innowise.khorsun.carorderservice.model.RouteRequest;
import innowise.khorsun.carorderservice.service.CarService;
import innowise.khorsun.carorderservice.service.impl.RouteServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;


@Controller
public class GeoController {
    private final CarService carService;
    private final RouteServiceImpl routeService;
    @Autowired
    public GeoController(CarService carService, RouteServiceImpl routeService) {
        this.carService = carService;
        this.routeService = routeService;
    }

    @GetMapping("/get-location")
    public String getMyLocation(){
       return "geo/get-location";
   }

    @ResponseBody
    @PostMapping("/get-car-places")
    public ResponseEntity<List<GeoCarResponse>> getAvailableCarWithPlaceByRadius(@RequestBody GeoRequestModel geoRequestModel){
        return ResponseEntity.ok(carService.getAvailableCarWithPlaceByRadius(geoRequestModel));
    }

    @ResponseBody
    @PostMapping("/calculate-route")
    public ResponseEntity<DirectionsRoute[]> calculateRoute(@RequestBody RouteRequest request) throws IOException, InterruptedException, ApiException {
        return ResponseEntity.ok(routeService.calculateRoute(request));
    }

}
