package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.BookingDto;
import innowise.khorsun.carorderservice.entity.Booking;
import innowise.khorsun.carorderservice.mapper.BookingMapper;
import innowise.khorsun.carorderservice.model.BookingRequestModel;
import innowise.khorsun.carorderservice.repositorie.BookingRepository;
import innowise.khorsun.carorderservice.service.BookingService;
import innowise.khorsun.carorderservice.service.CarService;
import innowise.khorsun.carorderservice.util.enums.Status;
import innowise.khorsun.carorderservice.util.error.booking.BookingExistingException;
import innowise.khorsun.carorderservice.util.error.booking.BookingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CarService carService;
    private final ResourceBundle resourceBundle;
    private static final String BOOKING_NOT_FOUND="message.error.booking_not_found";
    private static final String MESSAGE_ERROR_BOOKING_EXISTING ="message.error.booking_existing";
    private static final String RETURN_BOOKING_ERROR_MESSAGE = "You have already return the car";

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper,
                              CarService carService, ResourceBundle bundle) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.carService = carService;
        this.resourceBundle = bundle;
    }

    @Override
    @Transactional
    public void addBooking(BookingRequestModel bookingRequestModel) {
        checkIfActiveBookingExists(bookingRequestModel.getUserId());
        carService.setCarAvailability(bookingRequestModel, false);
        Booking booking = bookingMapper.bookingRequestModelToBooking(bookingRequestModel);
        booking.setStartDateTime(LocalDateTime.now());
        booking.setStatus(Status.IN_PROGRESS);
        bookingRepository.save(booking);

    }

    @Override
    public BookingDto getBookingById(Integer id) {
        return bookingRepository
                .findById(id)
                .map(bookingMapper::bookingToBookingDto)
                .orElseThrow(() -> new BookingNotFoundException(resourceBundle.getString(BOOKING_NOT_FOUND),
                        new Date()));
    }

    @Override
    @Transactional
    public void returnBooking(Integer id) {
        if(bookingRepository.findBookingByIdAndStatus(id,Status.PENDING).isPresent()){
            throw new BookingExistingException(RETURN_BOOKING_ERROR_MESSAGE ,new Date());
        }
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        optionalBooking.ifPresent(reservation -> {
            reservation.setEndDateTime(LocalDateTime.now());
            reservation.setStatus(Status.PENDING);
            carService.setCarAvailability(reservation.getCar().getId(), true);
        });
    }

    @Override
    public Booking getBookingByUserIdAndStatus(Integer userId, Status status) {
        return bookingRepository
                .findBookingByUserIdAndStatus(userId, Status.PENDING)
                .orElseThrow(() -> new BookingNotFoundException(resourceBundle.getString(BOOKING_NOT_FOUND),
                        new Date()));
    }

    private void checkIfActiveBookingExists(Integer userId) {
        if (bookingRepository.findBookingByUserIdAndStatus(userId, Status.PENDING).isPresent()
                || bookingRepository.findBookingByUserIdAndStatus(userId, Status.IN_PROGRESS).isPresent()) {
            throw new BookingExistingException(resourceBundle.getString(MESSAGE_ERROR_BOOKING_EXISTING),
                    new Date());
        }
    }
}
