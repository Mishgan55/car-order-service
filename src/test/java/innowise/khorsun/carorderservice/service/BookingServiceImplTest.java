package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.BookingDto;
import innowise.khorsun.carorderservice.entity.Booking;
import innowise.khorsun.carorderservice.mapper.BookingMapper;
import innowise.khorsun.carorderservice.model.BookingRequestModel;
import innowise.khorsun.carorderservice.repositorie.BookingRepository;
import innowise.khorsun.carorderservice.service.impl.BookingServiceImpl;
import innowise.khorsun.carorderservice.util.enums.Status;
import innowise.khorsun.carorderservice.util.error.booking.BookingExistingException;
import innowise.khorsun.carorderservice.util.error.booking.BookingNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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

        assertThrows(BookingNotFoundException.class,()->bookingService.getBookingById(bookingId));
    }
    @Test
    void addBooking_Success(){

        BookingRequestModel bookingRequestModel = new BookingRequestModel();
        bookingRequestModel.setUserId(1);
        bookingRequestModel.setCarId(1);

        Booking booking = mock(Booking.class);
        when(bookingMapper.bookingRequestModelToBooking(bookingRequestModel)).thenReturn(booking);

        bookingService.addBooking(bookingRequestModel);

        verify(carService).setCarAvailability(bookingRequestModel,false);
        verify(booking).setStartDateTime(any(LocalDateTime.class));
        verify(booking).setStatus(Status.IN_PROGRESS);
        verify(bookingRepository).save(booking);
    }
    @Test
    void testAddBooking_ActiveBookingExists() {
        BookingRequestModel bookingRequestModel = new BookingRequestModel();
        int userId = 1;
        bookingRequestModel.setUserId(userId);

        // Моки
        when(bookingRepository.findBookingByUserIdAndStatus(userId, Status.PENDING)).thenReturn(Optional.of(new Booking()));


        assertThrows(BookingExistingException.class, () -> bookingService.addBooking(bookingRequestModel));
    }
    @Test
    void testReturnBooking_Success(){
        Integer bookingId=1;
        Booking booking = mock(Booking.class);
        booking.setId(bookingId);
        booking.setStatus(Status.PENDING);

        when(bookingRepository.findBookingByIdAndStatus(bookingId,Status.PENDING)).thenReturn(Optional.empty());
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        doNothing().when(carService).setCarAvailability(booking.getCar().getId(), true);

        bookingService.returnBooking(bookingId);

        Assertions.assertEquals(Status.PENDING, booking.getStatus());
        verify(booking).setEndDateTime(any(LocalDateTime.class));
        verify(booking).setStatus(Status.PENDING);
        verify(carService).setCarAvailability(bookingId,eq(true));
        verify(bookingRepository).save(booking);
    }
}
