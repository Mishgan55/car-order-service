package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.PlaceDto;
import innowise.khorsun.carorderservice.entity.Place;
import innowise.khorsun.carorderservice.mapper.PlaceMapper;
import innowise.khorsun.carorderservice.repositorie.PlaceRepository;
import innowise.khorsun.carorderservice.service.impl.PlaceServiceImpl;
import innowise.khorsun.carorderservice.util.error.place.PlaceNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlaceServiceImplTest {
    @InjectMocks
    private PlaceServiceImpl placeService;
    @Mock
    private PlaceRepository placeRepository;
    @Mock
    private PlaceMapper placeMapper;

    @Test
    void testGetPlaceDtoById(){
        Integer placeId=1;
        Place place = new Place();
        place.setId(placeId);

        PlaceDto placeDto = new PlaceDto();

        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));
        when(placeMapper.placeToPlaceDto(place)).thenReturn(placeDto);

        PlaceDto placeDtoById = placeService.getPlaceDtoById(placeId);

        Assertions.assertNotNull(placeDtoById);
        Assertions.assertEquals(placeDto,placeDtoById);
    }
    @Test
    void testGetPlaceDtoById_PlaceNotFound(){
        when(placeRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(PlaceNotFoundException.class,()->placeService.getPlaceDtoById(1));
    }
    @Test
    void testGetAllPlaces(){
        List<PlaceDto> placeDto = List.of(new PlaceDto(), new PlaceDto());
        when(placeRepository.findAll()).thenReturn(List.of(new Place(),new Place()));
        when(placeMapper.placeToPlaceDto(any())).thenReturn(placeDto.get(0),placeDto.get(1));

        List<PlaceDto> allPlaces = placeService.getAllPlaces();

        Assertions.assertNotNull(allPlaces);
        Assertions.assertEquals(2,allPlaces.size());
    }
    @Test
    void testAddPlace(){
        PlaceDto placeDto = new PlaceDto();
        placeDto.setName("Test");
        placeDto.setAddress("Test");
        placeDto.setWorkHours("Test");

        Place place = new Place();

        when(placeMapper.placeDtoToPlace(placeDto)).thenReturn(place);

        placeService.addPlace(placeDto);

        verify(placeRepository,times(1)).save(any(Place.class));
    }
    @Test
    void testRemovePlace(){
        doNothing().when(placeRepository).deleteById(1);

        placeService.removePlace(1);

        verify(placeRepository,times(1)).deleteById(1);
    }
    @Test
    void testEditPlace(){
        Integer placeId=1;
        String test="Test";
        PlaceDto updatedPlace = new PlaceDto();
        updatedPlace.setName(test);
        updatedPlace.setAddress(test);
        updatedPlace.setWorkHours(test);

        Place place = new Place();
        when(placeRepository.findById(placeId)).thenReturn(Optional.of(place));

        placeService.editPlace(placeId,updatedPlace);

        verify(placeRepository,times(1)).findById(placeId);
        Assertions.assertEquals(test,place.getName());
        Assertions.assertEquals(test,place.getAddress());
        Assertions.assertEquals(test,place.getWorkHours());
    }
    @Test
    void testEditPlace_PlaceNotFound(){
        when(placeRepository.findById(1)).thenReturn(Optional.empty());

        PlaceDto updatedPlace = new PlaceDto();

        assertThrows(PlaceNotFoundException.class,()->placeService.editPlace(1,updatedPlace));
    }

    @Test
    void testGetPlaceById(){
        Integer placeId=1;
        Place expectedPlace = new Place();
        expectedPlace.setId(1);
        expectedPlace.setName("Test");
        when(placeRepository.findById(1)).thenReturn(Optional.of(expectedPlace));

        Optional<Place> result = placeService.getPlaceById(placeId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(expectedPlace, result.get());
    }
    @Test
    void testGetPlaceById_WhenPlaceDoesNotExist() {
        Integer placeId = 1;

        when(placeRepository.findById(placeId)).thenReturn(Optional.empty());

        Optional<Place> result = placeService.getPlaceById(placeId);

        Assertions.assertFalse(result.isPresent());
    }

}
