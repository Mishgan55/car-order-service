package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.PlaceDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface PlaceService {
    PlaceDto getPlaceById(Integer id);
    List<PlaceDto> getAllPlaces();
    void addPlace(PlaceDto placeDto);
    void removePlace(Integer id);
    void editPlace(Integer id, PlaceDto placeDtoDto);
}
