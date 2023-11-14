package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.CarDto;
import innowise.khorsun.carorderservice.dto.PaymentDto;
import innowise.khorsun.carorderservice.entity.Booking;

import innowise.khorsun.carorderservice.entity.Car;
import innowise.khorsun.carorderservice.entity.Payment;
import innowise.khorsun.carorderservice.entity.User;
import innowise.khorsun.carorderservice.mapper.PaymentMapper;
import innowise.khorsun.carorderservice.repositorie.PaymentRepository;
import innowise.khorsun.carorderservice.service.impl.CarServiceImpl;
import innowise.khorsun.carorderservice.service.impl.PaymentServiceImpl;
import innowise.khorsun.carorderservice.util.PropertyUtil;
import innowise.khorsun.carorderservice.util.enums.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;


@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private BookingService bookingService;

    @Spy
    private PaymentMapper paymentMapper;
    @Mock
    CarServiceImpl carService;


    @Test
    void testAddPayment_Success() {
        PaymentDto paymentDto = new PaymentDto();

        Payment payment = new Payment();
        payment.setId(1);

        when(paymentMapper.paymentDtoToPayment(paymentDto)).thenReturn(payment);
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment result = paymentService.addPayment(paymentDto);

        assertEquals(payment, result);
    }

    @Test
    void testIsSessionPaid_WhenSessionIsPaid() {
        String sessionId = "your_session_id";

        Payment payment = new Payment();
        payment.setStatus(Status.PAYED);

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(payment);

        boolean result = paymentService.isSessionPaid(sessionId);

        assertTrue(result);
    }

    @Test
    void testIsSessionPaid_WhenSessionIsNotPaid() {
        String sessionId = "your_session_id";

        Payment payment = new Payment();
        payment.setStatus(Status.PENDING);

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(payment);

        boolean result = paymentService.isSessionPaid(sessionId);

        assertFalse(result);
    }

    @Test
    void testCheckSuccess_PaymentNotFound() {
        String sessionId = "your_session_id";

        String result = paymentService.checkSuccess(null, sessionId);

        assertEquals(PropertyUtil.PAYMENT_NOT_FOUND, result);
    }

    @Test
    void testCheckSuccess_InvalidKeyMessage() {
        String sessionId = "your_session_id";
        Payment payment = new Payment();
        payment.setStatus(Status.PAYED);

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(payment);

        String result = paymentService.checkSuccess(payment, sessionId);

        assertEquals(PropertyUtil.INVALID_KEY_MESSAGE, result);
    }

    @Test
    void testCheckSuccess_SuccessfullyPaymentMessage() {
        String sessionId = "your_session_id";

        Payment payment = mock(Payment.class);
        User user = mock(User.class);
        Booking booking = mock(Booking.class);

        when(payment.getStatus()).thenReturn(Status.PENDING);
        when(paymentRepository.findBySessionId(sessionId)).thenReturn(payment);
        when(payment.getUser()).thenReturn(user);
        when(user.getId()).thenReturn(1); // Mock the user's ID
        when(bookingService.getBookingByUserIdAndStatus(1, Status.PENDING)).thenReturn(booking);

        String result = paymentService.checkSuccess(payment, sessionId);

        assertEquals(PropertyUtil.SUCCESSFULLY_PAYMENT_MESSAGE, result);
        verify(payment, times(1)).setStatus(Status.PAYED);
        verify(booking, times(1)).setStatus(Status.PAYED);
    }

    @Test
    void testCalculatePaymentAmount() {
        Integer userId = 1;
        Booking booking = new Booking();
        booking.setStartDateTime(LocalDateTime.now().minusHours(2));
        booking.setEndDateTime(LocalDateTime.now());
        booking.setStatus(Status.PENDING);


        Car car = new Car();
        car.setId(1);
        booking.setCar(car);

        when(bookingService.getBookingByUserIdAndStatus(userId, Status.PENDING)).thenReturn(booking);

        CarDto carDto = new CarDto();
        carDto.setDailyFee(new BigDecimal("10.00"));

        when(carService.getCarById(booking.getCar().getId())).thenReturn(carDto);

        Long paymentAmount = paymentService.calculatePaymentAmount(userId);

        long minutes = Duration.between(booking.getStartDateTime(), booking.getEndDateTime()).toMinutes();
        Long expectedAmount = new BigDecimal("10.00").multiply(BigDecimal.valueOf(minutes)).longValue();

        assertEquals(expectedAmount, paymentAmount);
    }

    @Test
    void testFindBySessionId() {

        String sessionId = "Test id";
        Payment payment = new Payment();
        payment.setSessionId(sessionId);

        when(paymentRepository.findBySessionId(sessionId)).thenReturn(payment);

        Payment result = paymentService.findBySessionId(sessionId);

        verify(paymentRepository, times(1)).findBySessionId(sessionId);
        assertNotNull(result);
        assertEquals(sessionId, result.getSessionId());
    }
}
