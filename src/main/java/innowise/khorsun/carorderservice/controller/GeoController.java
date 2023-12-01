package innowise.khorsun.carorderservice.controller;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsRoute;
import innowise.khorsun.carorderservice.model.GeoCarResponse;
import innowise.khorsun.carorderservice.model.GeoRequestModel;
import innowise.khorsun.carorderservice.model.RouteRequest;
import innowise.khorsun.carorderservice.service.CarService;
import innowise.khorsun.carorderservice.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;


@Controller
public class GeoController {
    @Value("${google.maps.api.key}")
    private String apiKey;
    private final CarService carService;
    private final RouteService routeService;
    @Autowired
    public GeoController(CarService carService, RouteService routeService) {
        this.carService = carService;
        this.routeService = routeService;
    }

    @GetMapping("/get-location")
    public String getMyLocation(Model model){
        model.addAttribute("apiKey", apiKey);
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