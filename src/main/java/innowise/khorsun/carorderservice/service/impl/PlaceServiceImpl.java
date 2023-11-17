package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.PlaceDto;
import innowise.khorsun.carorderservice.mapper.PlaceMapper;
import innowise.khorsun.carorderservice.repositorie.PlaceRepository;
import innowise.khorsun.carorderservice.service.PlaceService;
import innowise.khorsun.carorderservice.util.PropertyUtil;
import innowise.khorsun.carorderservice.util.error.place.PlaceNotFoundException;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final PlaceMapper placeMapper;

    @Autowired
    public PlaceServiceImpl(PlaceRepository placeRepository, PlaceMapper placeMapper) {
        this.placeRepository = placeRepository;
        this.placeMapper = placeMapper;
    }

    @Override
    public PlaceDto getPlaceDtoById(Integer id) {
        return placeRepository.findById(id)
                .map(placeMapper::placeToPlaceDto)
                .orElseThrow(() -> new PlaceNotFoundException(PropertyUtil.PLACE_NOT_FOUND, new Date()));
    }

    @Override
    public List<PlaceDto> getAllPlaces() {
        return placeRepository
                .findAll()
                .stream()
                .map(placeMapper::placeToPlaceDto).toList();
    }

    @Override
    @Transactional
    public void addPlace(PlaceDto placeDto) {
        placeRepository.save(placeMapper.placeDtoToPlace(placeDto));
    }

    @Override
    @Transactional
    public void removePlace(Integer id) {
        placeRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void editPlace(Integer id, PlaceDto updatedPlaceDto) {
        placeRepository
                .findById(id)
                .ifPresentOrElse(
                        existingPlace -> {
                            GeometryFactory geometryFactory = new GeometryFactory();
                            existingPlace
                                    .setLocation(geometryFactory
                                            .createPoint(new Coordinate(updatedPlaceDto.getLatitude(),
                                                    updatedPlaceDto.getLongitude())));
                        },
                        () -> {
                            throw new PlaceNotFoundException(PropertyUtil.PLACE_NOT_FOUND, new Date());
                        }
                );
    }
}
