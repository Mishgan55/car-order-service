package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.BookingDto;
import innowise.khorsun.carorderservice.entity.Booking;
import innowise.khorsun.carorderservice.entity.Car;
import innowise.khorsun.carorderservice.mapper.BookingMapper;
import innowise.khorsun.carorderservice.model.BookingRequestModel;
import innowise.khorsun.carorderservice.repositorie.CarRepository;
import innowise.khorsun.carorderservice.repositorie.BookingRepository;
import innowise.khorsun.carorderservice.service.BookingService;
import innowise.khorsun.carorderservice.util.enums.Status;
import innowise.khorsun.carorderservice.util.error.car.CarNotFoundException;
import innowise.khorsun.carorderservice.util.error.reservation.BookingNotFoundException;
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

    private final CarRepository carRepository;

    @Autowired
    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper, CarRepository carRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.carRepository = carRepository;
    }

    @Override
    @Transactional
    public void addBooking(BookingRequestModel bookingRequestModel) {
        Car car = carRepository.findById(bookingRequestModel.getCarId())
                .orElseThrow(() -> new CarNotFoundException("Car not found", new Date()));
        car.setIsAvailable(false);
        Booking booking = bookingMapper.bookingRequestModelToBooking(bookingRequestModel);
        booking.setStartDateTime(LocalDateTime.now());
        booking.setStatus(Status.IN_PROGRESS);
        bookingRepository.save(booking);
    }

    @Override
    public BookingDto getBookingById(Integer id) {
        return bookingRepository.findById(id).map(bookingMapper::bookingToBookingDto)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found", new Date()));
    }

    @Override
    @Transactional
    public void returnBooking(Integer id) {
        Optional<Booking> optionalBooking = bookingRepository.findById(id);
        optionalBooking.ifPresent(reservation -> {
            reservation.setEndDateTime(LocalDateTime.now());
            reservation.setStatus(Status.PENDING);
            Car car = reservation.getCar();
            if (car == null) {
                throw new CarNotFoundException("Car not found for reservation ID: " + id, new Date());
            }
            car.setIsAvailable(true);
        });
    }
}
