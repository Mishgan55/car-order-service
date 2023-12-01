package innowise.khorsun.carorderservice.mapper;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.entity.Car;
import innowise.khorsun.carorderservice.model.GeoCarResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CarMapper {
    @Mapping(target = "placeId", source = "place.id")
    CarDto carToCarDto(Car car);

    @Mapping(target = "place.id", source = "placeId")
    Car carDtoToCar(CarDto carDto);
    @Mapping(target = "placeDto.latitude", source = "place.location.coordinate.x")
    @Mapping(target = "placeDto.longitude", source = "place.location.coordinate.y")
    GeoCarResponse carToGeoCar(Car car);
}
