package innowise.khorsun.carorderservice.mapper;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.entity.Car;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CarMapper {
    Car carDtoToCar(CarDto carDto);
    CarDto carToCarDto(Car car);

}
