package innowise.khorsun.carorderservice.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import innowise.khorsun.carorderservice.dto.PaymentDto;
import innowise.khorsun.carorderservice.mapper.PaymentMapper;
import innowise.khorsun.carorderservice.model.PaymentRequestModel;
import innowise.khorsun.carorderservice.service.impl.StripeServiceImpl;
import innowise.khorsun.carorderservice.util.PropertyUtil;
import innowise.khorsun.carorderservice.util.enums.Type;
import innowise.khorsun.carorderservice.util.error.payment.PaymentSessionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StripeServiceImplTest {

    @InjectMocks
    private StripeServiceImpl stripeService;
    @Mock
    private PaymentService paymentService;
    @Mock
    private PaymentMapper paymentMapper;
    private SessionCreateParams params;
    private PaymentRequestModel paymentRequestInfoDto;
    private static final String STRIPE_API_KEY=
            "sk_test_51O4gF9Hwob1or3tw2JsMNgK1YdfDQLYQtCpxcyHVM0rnsFbwZyxFb4b1SfiITQ6FzIiPL2sYYCON7RTHzQNdPZvn007FpNxPVh";

    @BeforeEach
    void createSessionInformation() {
        paymentRequestInfoDto = new PaymentRequestModel();
        paymentRequestInfoDto.setUserId(1);
        paymentRequestInfoDto.setType(Type.PAYMENT);

        Stripe.apiKey = STRIPE_API_KEY;

        params =
                SessionCreateParams.builder()
                        .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setPriceData(
                                                SessionCreateParams.LineItem.PriceData.builder()
                                                        .setCurrency("gbp")
                                                        .setUnitAmount(500L)
                                                        .setProductData(
                                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                        .setName("Product")
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .setQuantity(1L)
                                        .build()
                        )
                        .setClientReferenceId("1")
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl(PropertyUtil.PAYMENT_URL + "/success" + "?session_id={CHECKOUT_SESSION_ID}")
                        .setCancelUrl(PropertyUtil.PAYMENT_URL + "/cancel")
                        .build();

    }

    @Test
    void testCreatePaymentSession() {
        Integer userId = 1;
        Type type = Type.PAYMENT;
        when(paymentService.calculatePaymentAmount(userId)).thenReturn(500L);

        SessionCreateParams actualParams = stripeService.createPaymentSession(userId, type);

        assertEquals(params.getMode(), actualParams.getMode());
        assertEquals(params.getSuccessUrl(), actualParams.getSuccessUrl());
        assertEquals(params.getCancelUrl(), actualParams.getCancelUrl());
        assertEquals(params.getClientReferenceId(), actualParams.getClientReferenceId());
        assertNotNull(actualParams);
    }

    @Test
    void testGetPaymentFromSession() {
        when(paymentService.calculatePaymentAmount(anyInt())).thenReturn(500L);
        when(paymentMapper.paymentToPaymentDto(any())).thenReturn(new PaymentDto());
        // Act
        PaymentDto paymentDto = stripeService.getPaymentFromSession(paymentRequestInfoDto);
        // Assert
        assertNotNull(paymentDto);
        assertEquals(SessionCreateParams.Mode.PAYMENT, params.getMode());
        assertEquals(PropertyUtil.PAYMENT_URL
                + "/success" + "?session_id={CHECKOUT_SESSION_ID}", params.getSuccessUrl());
        verify(paymentService, times(1)).calculatePaymentAmount(anyInt());
        verify(paymentMapper, times(1)).paymentToPaymentDto(any());
    }

    @Test
    void testGetPaymentFromSession_InvalidSession() throws StripeException {
        Integer userId = 1;
        Type type = Type.PAYMENT;
        when(paymentService.calculatePaymentAmount(userId)).thenReturn(500L);

        SessionCreateParams actualParams = stripeService.createPaymentSession(userId, type);

        when(Session.create(actualParams)).thenThrow(new PaymentSessionException("TEST"));

        assertThrows(PaymentSessionException.class, () -> stripeService.getPaymentFromSession(paymentRequestInfoDto));
    }
    @Test
    void testGetPaymentFromSession_MalformedUrl() throws StripeException {
        Integer userId = 1;
        Type type = Type.PAYMENT;
        when(paymentService.calculatePaymentAmount(userId)).thenReturn(500L);

        SessionCreateParams actualParams = stripeService.createPaymentSession(userId, type);

        when(Session.create(actualParams).getUrl()).thenReturn(null);

        assertThrows(PaymentSessionException.class, () -> stripeService.getPaymentFromSession(paymentRequestInfoDto));
    }
}