package innowise.khorsun.carorderservice.model;

import innowise.khorsun.carorderservice.dto.PlaceDto;
import lombok.Data;

@Data
public class GeoCarResponse {
    private String brand;
    private String model;
    private Integer yearOfProduction;
    private String plateNumber;
    private PlaceDto placeDto;
}
