package innowise.khorsun.carorderservice.mapper;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.entity.Car;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CarMapper {
    CarMapper INSTANCE = Mappers.getMapper( CarMapper.class );
    Car carDtoToCar(CarDto carDto);
    CarDto carToCarDto(Car car);
}
