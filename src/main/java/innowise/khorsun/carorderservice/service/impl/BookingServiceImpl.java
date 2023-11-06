package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.BookingDto;
import innowise.khorsun.carorderservice.entity.Booking;
import innowise.khorsun.carorderservice.mapper.BookingMapper;
import innowise.khorsun.carorderservice.model.BookingRequestModel;
import innowise.khorsun.carorderservice.repositorie.BookingRepository;
import innowise.khorsun.carorderservice.service.BookingService;
import innowise.khorsun.carorderservice.service.CarService;
import innowise.khorsun.carorderservice.service.UserService;
import innowise.khorsun.carorderservice.util.PropertyUtil;
import innowise.khorsun.carorderservice.util.enums.Status;
import innowise.khorsun.carorderservice.util.error.booking.BookingExistingException;
import innowise.khorsun.carorderservice.util.error.booking.BookingNotFoundException;
import innowise.khorsun.carorderservice.util.error.user.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final CarService carService;
    private final UserService userService;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper,
                              CarService carService, UserService userService) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.carService = carService;
        this.userService = userService;
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

    @Override
    public List<BookingDto> getBookingsByUserId(Integer userId) throws UserNotFoundException {
        checkIfUserExist(userId);
        return bookingRepository
                .findBookingByUserId(userId)
                .stream()
                .map(bookingMapper::bookingToBookingDto)
                .toList();
    }

    private void checkIfActiveBookingExists(Integer userId) {
        if (bookingRepository.findBookingByUserIdAndStatus(userId, Status.PENDING).isPresent()
                || bookingRepository.findBookingByUserIdAndStatus(userId, Status.IN_PROGRESS).isPresent()) {
            throw new BookingExistingException(PropertyUtil.MESSAGE_ERROR_BOOKING_EXISTING,
                    new Date());
        }
    }
    private void checkIfUserExist(Integer userId){
        userService.getUserById(userId);
    }
}
