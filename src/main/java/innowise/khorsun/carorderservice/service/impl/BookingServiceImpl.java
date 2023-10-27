package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.BookingDto;
import innowise.khorsun.carorderservice.entity.Booking;
import innowise.khorsun.carorderservice.mapper.BookingMapper;
import innowise.khorsun.carorderservice.model.BookingRequestModel;
import innowise.khorsun.carorderservice.repositorie.BookingRepository;
import innowise.khorsun.carorderservice.service.BookingService;
import innowise.khorsun.carorderservice.service.CarService;
import innowise.khorsun.carorderservice.util.PropertyUtil;
import innowise.khorsun.carorderservice.util.enums.Status;
import innowise.khorsun.carorderservice.util.error.booking.BookingExistingException;
import innowise.khorsun.carorderservice.util.error.booking.BookingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CarService carService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper,
                              CarService carService) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.carService = carService;
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
                .orElseThrow(() -> new BookingNotFoundException(PropertyUtil.BOOKING_NOT_FOUND,
                        new Date()));
    }

    @Override
    @Transactional
    public void returnBooking(Integer id) {
        if(bookingRepository.findBookingByIdAndStatus(id,Status.PENDING).isPresent()){
            throw new BookingExistingException(PropertyUtil.RETURN_BOOKING_ERROR_MESSAGE ,new Date());
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
                .orElseThrow(() -> new BookingNotFoundException(PropertyUtil.BOOKING_NOT_FOUND,
                        new Date()));
    }

    private void checkIfActiveBookingExists(Integer userId) {
        if (bookingRepository.findBookingByUserIdAndStatus(userId, Status.PENDING).isPresent()
                || bookingRepository.findBookingByUserIdAndStatus(userId, Status.IN_PROGRESS).isPresent()) {
            throw new BookingExistingException(PropertyUtil.MESSAGE_ERROR_BOOKING_EXISTING,
                    new Date());
        }
    }
}
