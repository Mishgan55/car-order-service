package innowise.khorsun.carorderservice.mapper;

import innowise.khorsun.carorderservice.dto.PlaceDto;
import innowise.khorsun.carorderservice.entity.Place;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface PlaceMapper {


    @Mapping(source = "location", target = "latitude", qualifiedByName = "toLatitude")
    @Mapping(source = "location", target = "longitude", qualifiedByName = "toLongitude")
    PlaceDto placeToPlaceDto(Place place);

    // Добавьте квалификаторы для преобразования координат
    @Named("toLatitude")
    default double toLatitude(Geometry location) {
        return location.getCoordinate().getX();
    }
    @Named("toLongitude")
    default double toLongitude(Geometry location) {
        return location.getCoordinate().getY();
    }
    @Mapping(target = "location", expression = "java(createGeo(placeDto.getLatitude(), placeDto.getLongitude()))")
    Place placeDtoToPlace(PlaceDto placeDto);
    default Geometry createGeo(double latitude, double longitude) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate = new Coordinate(longitude, latitude);
        return geometryFactory.createPoint(coordinate);
    }
}
