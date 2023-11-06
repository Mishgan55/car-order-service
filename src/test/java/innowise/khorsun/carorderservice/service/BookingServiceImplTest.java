package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.BookingDto;
import innowise.khorsun.carorderservice.dto.UserDto;
import innowise.khorsun.carorderservice.entity.Booking;
import innowise.khorsun.carorderservice.entity.Car;
import innowise.khorsun.carorderservice.mapper.BookingMapper;
import innowise.khorsun.carorderservice.model.BookingRequestModel;
import innowise.khorsun.carorderservice.repositorie.BookingRepository;
import innowise.khorsun.carorderservice.service.impl.BookingServiceImpl;
import innowise.khorsun.carorderservice.util.enums.Status;
import innowise.khorsun.carorderservice.util.error.booking.BookingExistingException;
import innowise.khorsun.carorderservice.util.error.booking.BookingNotFoundException;
import innowise.khorsun.carorderservice.util.error.user.UserNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
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
    void testGetBookingById() {

        Integer bookingId = 1;
        BookingDto bookingDto = new BookingDto();
        Booking booking = new Booking();
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingMapper.bookingToBookingDto(booking)).thenReturn(bookingDto);

        BookingDto result = bookingService.getBookingById(bookingId);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(bookingDto, result);
    }

    @Test
    void testGetBookingById_BookingNotFound() {
        Integer bookingId = 1;
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingById(bookingId));
    }

    @Test
    void addBooking_Success() {

        BookingRequestModel bookingRequestModel = new BookingRequestModel();
        bookingRequestModel.setUserId(1);
        bookingRequestModel.setCarId(1);

        Booking booking = mock(Booking.class);
        when(bookingMapper.bookingRequestModelToBooking(bookingRequestModel)).thenReturn(booking);

        bookingService.addBooking(bookingRequestModel);

        verify(carService).setCarAvailability(bookingRequestModel, false);
        verify(booking).setStartDateTime(any(LocalDateTime.class));
        verify(booking).setStatus(Status.IN_PROGRESS);
        verify(bookingRepository).save(booking);
    }

    @Test
    void testAddBooking_ActiveBookingExists() {
        BookingRequestModel bookingRequestModel = new BookingRequestModel();
        int userId = 1;
        bookingRequestModel.setUserId(userId);

        when(bookingRepository.findBookingByUserIdAndStatus(userId, Status.PENDING)).thenReturn(Optional.of(new Booking()));


        assertThrows(BookingExistingException.class, () -> bookingService.addBooking(bookingRequestModel));
    }

    @Test
    void testReturnBooking_Success() {
        Integer bookingId = 1;
        Booking booking = new Booking();
        booking.setId(bookingId);
        booking.setStatus(Status.IN_PROGRESS);
        booking.setCar(new Car());

        when(bookingRepository.findBookingByIdAndStatus(bookingId, Status.PENDING)).thenReturn(Optional.empty());
        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        doNothing().when(carService).setCarAvailability(booking.getCar().getId(), true);

        bookingService.returnBooking(bookingId);

        Assertions.assertEquals(Status.PENDING, booking.getStatus());
        verify(carService).setCarAvailability(booking.getCar().getId(), true);
    }

    @Test
    void testReturnBooking_BookingNotFound() {
        Integer bookingId = 1;

        when(bookingRepository.findBookingByIdAndStatus(bookingId, Status.PENDING)).thenReturn(Optional.of(new Booking()));

        assertThrows(BookingExistingException.class, () -> bookingService.returnBooking(bookingId));
    }

    @Test
    void testGetBookingByUserIdAndStatus() {
        Integer userId = 1;
        Status status = Status.PENDING;

        Booking booking = new Booking();
        booking.setStatus(status);
        when(bookingRepository.findBookingByUserIdAndStatus(any(), any(Status.class))).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingByUserIdAndStatus(userId, status);

        assertNotNull(result);
        assertEquals(result, booking);
    }

    @Test
    void testGetBookingByUserIdAndStatus_BookingNotFound() {
        Integer userId = 1;
        Status status = Status.PENDING;

        when(bookingRepository.findBookingByUserIdAndStatus(userId, status)).thenReturn(Optional.empty());

        assertThrows(BookingNotFoundException.class, () -> bookingService.getBookingByUserIdAndStatus(userId, status));
    }

    @Test
    void testGetBookingsByUserId() {

        Integer userId = 1;
        UserDto userDto = new UserDto();
        List<Booking> bookings = List.of(new Booking(), new Booking());
        List<BookingDto> bookingDto = List.of(new BookingDto(), new BookingDto());
        when(userService.getUserById(userId)).thenReturn(userDto);

        when(bookingRepository.findBookingByUserId(userId)).thenReturn(bookings);
        when(bookingMapper.bookingToBookingDto(any())).thenReturn(bookingDto.get(0), bookingDto.get(1));

        List<BookingDto> result = bookingService.getBookingsByUserId(userId);

        assertNotNull(result);
        verify(bookingRepository, times(1)).findBookingByUserId(userId);
        assertEquals(bookingDto, result);
    }

    @Test
    void testGetBookingsByUserId_UserNotFound() {
        Integer userId = 1;

        when(userService.getUserById(userId)).thenThrow(UserNotFoundException.class);

        assertThrows(UserNotFoundException.class, () -> bookingService.getBookingsByUserId(userId));
    }
}
