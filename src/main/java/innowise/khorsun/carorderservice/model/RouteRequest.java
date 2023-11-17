package innowise.khorsun.carorderservice.model;

import lombok.Data;

@Data
public class RouteRequest {
    private Double startLatitude;
    private Double startLongitude;
    private Double endLatitude;
    private Double endLongitude;
}
