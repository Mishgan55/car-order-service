package innowise.khorsun.carorderservice.service;

import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsRoute;
import innowise.khorsun.carorderservice.model.RouteRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public interface RouteService {
    DirectionsRoute[] calculateRoute(RouteRequest request) throws IOException, InterruptedException, ApiException;
}
