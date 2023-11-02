package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.BookingDto;
import innowise.khorsun.carorderservice.model.BookingRequestModel;
import org.springframework.stereotype.Service;


@Service
public interface BookingService {
    void addBooking(BookingRequestModel bookingRequestModel);

    BookingDto getBookingById(Integer id);

    void returnBooking(Integer id);
}
