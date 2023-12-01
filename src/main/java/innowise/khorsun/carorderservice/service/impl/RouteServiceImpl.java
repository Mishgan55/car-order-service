package innowise.khorsun.carorderservice.service.impl;

import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.LatLng;
import com.google.maps.model.TravelMode;
import innowise.khorsun.carorderservice.model.RouteRequest;
import innowise.khorsun.carorderservice.service.RouteService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Transactional(readOnly = true)
public class RouteServiceImpl implements RouteService {
    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    public String getGoogleMapsApiKey() {
        return googleMapsApiKey;
    }

    public DirectionsRoute[] calculateRoute(RouteRequest request) throws IOException, InterruptedException, ApiException {
        GeoApiContext.Builder builder = new GeoApiContext.Builder().apiKey(getGoogleMapsApiKey());
        DirectionsResult result = DirectionsApi.newRequest(builder.build())
                .origin(new LatLng(request.getStartLatitude(), request.getStartLongitude()))
                .destination(new LatLng(request.getEndLatitude(), request.getEndLongitude()))
                .mode(TravelMode.DRIVING)
                .await();

        return result.routes;

    }
}
