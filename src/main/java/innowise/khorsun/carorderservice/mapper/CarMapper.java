package innowise.khorsun.carorderservice.mapper;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.entity.Car;
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

}
