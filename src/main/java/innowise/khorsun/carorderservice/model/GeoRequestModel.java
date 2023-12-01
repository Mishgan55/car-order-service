package innowise.khorsun.carorderservice.model;

import lombok.Data;

@Data
public class GeoRequestModel {
    private Double latitude;
    private Double longitude;
    private Double radius;
}
