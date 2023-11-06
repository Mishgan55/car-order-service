package innowise.khorsun.carorderservice.service;

import static org.mockito.Mockito.*;

import com.stripe.param.checkout.SessionCreateParams;
import innowise.khorsun.carorderservice.dto.PaymentDto;
import innowise.khorsun.carorderservice.entity.Payment;
import innowise.khorsun.carorderservice.mapper.PaymentMapper;
import innowise.khorsun.carorderservice.model.PaymentRequestModel;
import innowise.khorsun.carorderservice.service.impl.StripeServiceImpl;
import innowise.khorsun.carorderservice.util.enums.Status;
import innowise.khorsun.carorderservice.util.enums.Type;
import innowise.khorsun.carorderservice.util.error.payment.PaymentSessionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.springframework.beans.factory.annotation.Value;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class StripeServiceImplTest {

    @InjectMocks
    private StripeServiceImpl stripeService;

    @Mock
    private PaymentService paymentService;
    @Mock
    private PaymentMapper paymentMapper;

    @Value("${STRIPE_API_KEY}")
    private String stripeApiKey;
    @BeforeEach
    void setUp() {
        stripeService = new StripeServiceImpl(paymentService, paymentMapper);
    }

    @Test
    void testCreatePaymentSession() {
        // Создаем данные для теста
        Integer userId = 1;
        Type type = Type.PAYMENT;

        // Задаем ожидаемые значения, которые вернут моки
        when(paymentService.calculatePaymentAmount(userId)).thenReturn(100L);

        // Вызываем метод, который мы хотим протестировать
        SessionCreateParams result = stripeService.createPaymentSession(userId, type);

        // Проверяем, что результат не равен null и соответствует ожидаемым значениям
        assertNotNull(result);
        assertEquals(stripeApiKey, Stripe.apiKey);
        // Дополнительные проверки на ожидаемые значения
    }

//    @Test
//    void testGetPaymentFromSession() throws StripeException, MalformedURLException {
//        // Arrange
//        PaymentRequestModel paymentRequestInfoDto = new PaymentRequestModel();
//        paymentRequestInfoDto.setUserId(1);
//        paymentRequestInfoDto.setType(Type.PAYMENT);
//
//        SessionCreateParams params = new SessionCreateParams.Builder().build();
//        Session session = new Session();
//        session.setUrl("https://example.com");
//        session.setId("session123");
//        session.setClientReferenceId("1");
//        session.setAmountTotal(1000L);
//
//
//        doReturn(100L).when(spy(paymentService)).calculatePaymentAmount(any());
//        when(stripeService.createPaymentSession(paymentRequestInfoDto.getUserId(), paymentRequestInfoDto.getType())).thenReturn(params);
//        when(Session.create(params)).thenReturn(session);
//
//        PaymentDto expectedPaymentDto = new PaymentDto();
//        expectedPaymentDto.setUserId(1);
//        expectedPaymentDto.setSessionId("session123");
//        expectedPaymentDto.setUrl(new URL("https://example.com"));
//        expectedPaymentDto.setType(Type.PAYMENT);
//        expectedPaymentDto.setStatus(Status.PENDING);
//        expectedPaymentDto.setPaymentAmount(BigDecimal.valueOf(100L));
//
//        when(paymentService.addPayment(expectedPaymentDto)).thenReturn(new Payment());
//
//        PaymentDto paymentDto = stripeService.getPaymentFromSession(paymentRequestInfoDto);
//
//        assertEquals(expectedPaymentDto, paymentDto);
//    }
//
//    @Test
//    void testGetPaymentFromSessionStripeException() {
//        // Arrange
//        PaymentRequestModel paymentRequestInfoDto = new PaymentRequestModel();
//        paymentRequestInfoDto.setUserId(1);
//        paymentRequestInfoDto.setType(Type.PAYMENT);
//
//        SessionCreateParams params = new SessionCreateParams.Builder().build();
//
//        when(stripeService.createPaymentSession(paymentRequestInfoDto.getUserId(), paymentRequestInfoDto.getType())).thenReturn(params);
//
//        assertThrows(PaymentSessionException.class, () -> {
//            stripeService.getPaymentFromSession(paymentRequestInfoDto);
//        });
//    }
//
//    @Test
//    void testGetPaymentFromSessionMalformedURLException() throws StripeException {
//        PaymentRequestModel paymentRequestInfoDto = new PaymentRequestModel();
//        paymentRequestInfoDto.setUserId(1);
//        paymentRequestInfoDto.setType(Type.PAYMENT);
//
//        SessionCreateParams params = new SessionCreateParams.Builder().build();
//        Session session = new Session();
//        session.setUrl("https://example.com");
//        session.setId("session123");
//        session.setClientReferenceId("1");
//        session.setAmountTotal(1000L);
//
//        when(stripeService.createPaymentSession(paymentRequestInfoDto.getUserId(), paymentRequestInfoDto.getType())).thenReturn(params);
//        when(Session.create(params)).thenReturn(session);
//        when(paymentMapper.paymentToPaymentDto(Mockito.any())).thenThrow(new MalformedURLException("Malformed URL"));
//
//        assertThrows(PaymentSessionException.class, () -> {
//            stripeService.getPaymentFromSession(paymentRequestInfoDto);
//        });
//    }

}
