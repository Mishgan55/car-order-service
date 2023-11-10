package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.BookingDto;
import innowise.khorsun.carorderservice.entity.Booking;
import innowise.khorsun.carorderservice.model.BookingRequestModel;
import innowise.khorsun.carorderservice.util.enums.Status;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BookingService {
    void addBooking(BookingRequestModel bookingRequestModel);

    BookingDto getBookingById(Integer id);

    void returnBooking(Integer id);

    Booking getBookingByUserIdAndStatus(Integer userId, Status status);

    List<BookingDto> getBookingsByUserId(Integer userId);
}
