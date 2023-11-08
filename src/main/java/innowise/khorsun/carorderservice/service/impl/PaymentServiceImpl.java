package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.dto.PaymentDto;
import innowise.khorsun.carorderservice.entity.Booking;
import innowise.khorsun.carorderservice.entity.Payment;
import innowise.khorsun.carorderservice.mapper.PaymentMapper;
import innowise.khorsun.carorderservice.repositorie.PaymentRepository;
import innowise.khorsun.carorderservice.service.BookingService;
import innowise.khorsun.carorderservice.service.CarService;
import innowise.khorsun.carorderservice.service.PaymentService;
import innowise.khorsun.carorderservice.util.PropertyUtil;
import innowise.khorsun.carorderservice.util.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
@Transactional(readOnly = true)
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final BookingService bookingService;
    private final PaymentMapper paymentMapper;
    private final CarService carService;
    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository,
                              BookingService bookingService, PaymentMapper paymentMapper, CarService carService) {
        this.paymentRepository = paymentRepository;
        this.bookingService = bookingService;
        this.paymentMapper = paymentMapper;
        this.carService = carService;

    }

    @Override
    @Transactional
    public Payment addPayment(PaymentDto paymentDto) {
        return paymentRepository.save(paymentMapper.paymentDtoToPayment(paymentDto));
    }

    @Override
    public Long calculatePaymentAmount(Integer userId) {
        Booking booking = bookingService.getBookingByUserIdAndStatus(userId, Status.PENDING);
        CarDto carDto = carService.getCarById(booking.getCar().getId());
        long minutes = Duration.between(booking.getStartDateTime(), booking.getEndDateTime()).toMinutes();
        BigDecimal dailyFee = carDto.getDailyFee();
        return dailyFee.multiply(BigDecimal.valueOf(minutes)).longValue();
    }

    @Override
    public boolean isSessionPaid(String sessionId) {
        return paymentRepository.findBySessionId(sessionId)
                .getStatus()
                .equals(Status.PAYED);
    }

    @Override
    public Payment findBySessionId(String sessionId) {
        return paymentRepository
                .findBySessionId(sessionId);
    }

    @Transactional
    public String checkSuccess(Payment payment, String sessionId) {
        if (payment == null) {
            return PropertyUtil.PAYMENT_NOT_FOUND;
        }
        if (isSessionPaid(sessionId)) {
            return PropertyUtil.INVALID_KEY_MESSAGE;
        }
        Booking booking = bookingService.getBookingByUserIdAndStatus(payment.getUser().getId(), Status.PENDING);
        booking.setStatus(Status.PAYED);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(Status.PAYED);
        paymentRepository.save(payment);
        return PropertyUtil.SUCCESSFULLY_PAYMENT_MESSAGE;
    }
}