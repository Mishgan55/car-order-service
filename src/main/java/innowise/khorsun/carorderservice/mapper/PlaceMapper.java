package innowise.khorsun.carorderservice.mapper;

import innowise.khorsun.carorderservice.dto.PlaceDto;
import innowise.khorsun.carorderservice.entity.Place;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface PlaceMapper {
    Place placeDtoToPlace(PlaceDto placeDto);
    PlaceDto placeToPlaceDto(Place place);
}
