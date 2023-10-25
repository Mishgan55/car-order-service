package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.entity.Booking;
import innowise.khorsun.carorderservice.entity.Car;
import innowise.khorsun.carorderservice.entity.Payment;
import innowise.khorsun.carorderservice.repositorie.BookingRepository;
import innowise.khorsun.carorderservice.repositorie.CarRepository;
import innowise.khorsun.carorderservice.repositorie.PaymentRepository;
import innowise.khorsun.carorderservice.service.PaymentService;
import innowise.khorsun.carorderservice.util.enums.Status;
import innowise.khorsun.carorderservice.util.error.car.CarNotFoundException;
import innowise.khorsun.carorderservice.util.error.reservation.BookingNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.Date;

@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;


    private final CarRepository carRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, BookingRepository bookingRepository, CarRepository carRepository) {
        this.paymentRepository = paymentRepository;
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
    }

    @Override
    @Transactional
    public Payment save(Payment payment) {
        return paymentRepository.save(payment);
    }

    @Override
    public BigDecimal calculatePaymentAmount(Integer userId) {
        Booking booking = bookingRepository
                .findBookingByUserIdAndStatus(userId, Status.PENDING)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found", new Date()));
        Car car = carRepository.findById(booking.getCar().getId())
                .orElseThrow(() -> new CarNotFoundException("Car not found", new Date()));
        long minutes = Duration.between(booking.getStartDateTime(), booking.getEndDateTime()).toMinutes();
        BigDecimal dailyFee = car.getDailyFee();
        return dailyFee.multiply(BigDecimal.valueOf(minutes)).multiply(BigDecimal.valueOf(100));
    }

    @Override
    public boolean isSessionPaid(String sessionId) {
        return paymentRepository.findBySessionId(sessionId)
                .getStatus()
                .equals(Status.PAYED);
    }

    @Override
    public Payment findBySessionId(String sessionId) {
        return paymentRepository.findBySessionId(sessionId);
    }

    @Transactional
    public String checkSuccess(Payment payment, String sessionId) {
        if (payment == null) {
            return "payment not found";
        }
        if (isSessionPaid(sessionId)) {
            return "invalid payment";
        }
        Booking booking = bookingRepository.findBookingByUserIdAndStatus(payment.getUser().getId(), Status.PENDING)
                .orElseThrow(() -> new BookingNotFoundException("Booking not found", new Date()));
        booking.setStatus(Status.PAYED);
        payment.setStatus(Status.PAYED);
        paymentRepository.save(payment);
        return "Your payment was successful!";
    }
}
