package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.PlaceDto;
import innowise.khorsun.carorderservice.entity.Place;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface PlaceService {
    PlaceDto getPlaceDtoById(Integer id);
    List<PlaceDto> getAllPlaces();
    void addPlace(PlaceDto placeDto);
    void removePlace(Integer id);
    void editPlace(Integer id, PlaceDto placeDtoDto);
    Optional<Place> getPlaceById(Integer placeId);
}
