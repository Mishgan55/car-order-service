package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.BookingDto;
import innowise.khorsun.carorderservice.entity.Booking;
import innowise.khorsun.carorderservice.mapper.BookingMapper;
import innowise.khorsun.carorderservice.repositorie.BookingRepository;
import innowise.khorsun.carorderservice.service.impl.BookingServiceImpl;
import innowise.khorsun.carorderservice.util.error.booking.BookingNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingMapper bookingMapper;
    @Mock
    private CarService carService;
    @Mock
    private UserService userService;

    @Test
    void testGetBookingById(){

        Integer bookingId=1;
        BookingDto bookingDto = new BookingDto();
        Booking booking = new Booking();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingMapper.bookingToBookingDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.getBookingById(bookingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookingDto,result);
    }
    @Test
    void testGetBookingById_BookingNotFound(){
        Integer bookingId=1;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        Assertions.assertThrows(BookingNotFoundException.class,()->bookingService.getBookingById(bookingId));
    }
}
