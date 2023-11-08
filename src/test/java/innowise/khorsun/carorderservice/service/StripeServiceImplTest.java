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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mock;

@ExtendWith(MockitoExtension.class)
class StripeServiceImplTest {

    @InjectMocks
    private StripeServiceImpl stripeService;

    @Mock
    private PaymentService paymentService;

    @Mock
    private PaymentMapper paymentMapper;

    private SessionCreateParams sessionParams;
    private PaymentRequestModel paymentRequestInfoDto;

    @Value("${STRIPE_API_KEY}")
    private static String stripeApiKey;

    @BeforeEach
    void createSessionInformation() {
        paymentRequestInfoDto = new PaymentRequestModel();
        paymentRequestInfoDto.setUserId(1);
        paymentRequestInfoDto.setType(Type.PAYMENT);

        Stripe.apiKey = stripeApiKey;

        SessionCreateParams.Builder sessionParamsBuilder = new SessionCreateParams.Builder();
        sessionParamsBuilder.setMode(SessionCreateParams.Mode.PAYMENT);
        sessionParamsBuilder.setSuccessUrl(PropertyUtil.PAYMENT_URL + "/success" + "?session_id={CHECKOUT_SESSION_ID}");
        sessionParamsBuilder.setCancelUrl(PropertyUtil.PAYMENT_URL + "/cancel");
        sessionParamsBuilder.setClientReferenceId("1");
        List<SessionCreateParams.LineItem> lineItems = new ArrayList<>();
        SessionCreateParams.LineItem lineItem = new SessionCreateParams.LineItem.Builder()
                .setPrice("price_1O9naBHwob1or3twuhQLxLNk")
                .setQuantity(1L)
                .build();
        lineItems.add(lineItem);

        sessionParamsBuilder.addAllLineItem(lineItems);
        sessionParams = sessionParamsBuilder.build();
    }

    @Test
    void testCreatePaymentSession() {
        Integer userId = 1;
        Type type = Type.PAYMENT;

        when(paymentService.calculatePaymentAmount(userId)).thenReturn(42L);


        SessionCreateParams actualParams = stripeService.createPaymentSession(userId, type);

        assertEquals(sessionParams.getMode(), actualParams.getMode());
        assertEquals(sessionParams.getSuccessUrl(), actualParams.getSuccessUrl());
        assertEquals(sessionParams.getCancelUrl(), actualParams.getCancelUrl());
        assertEquals(sessionParams.getClientReferenceId(), actualParams.getClientReferenceId());
        assertNotNull(actualParams);
    }

    @Test
    void testGetPaymentFromSession() throws StripeException {

        when(paymentService.calculatePaymentAmount(anyInt())).thenReturn(100L);

        Session session = Mockito.mock(Session.class);

        Session mock = mock(Session.create(sessionParams).getClass());
        doReturn(session).when(mock);
        when(paymentMapper.paymentToPaymentDto(any())).thenReturn(new PaymentDto());

        // Act
        PaymentDto paymentDto = stripeService.getPaymentFromSession(paymentRequestInfoDto);

        // Assert
        assertNotNull(paymentDto);
        assertEquals(SessionCreateParams.Mode.PAYMENT, sessionParams.getMode());
        assertEquals(PropertyUtil.PAYMENT_URL
                + "/success" + "?session_id={CHECKOUT_SESSION_ID}", sessionParams.getSuccessUrl());
        verify(paymentService, times(1)).calculatePaymentAmount(anyInt());
        verify(paymentMapper, times(1)).paymentToPaymentDto(any());
    }

    @Test
    void testGetPaymentFromSessionWithInvalidSession() throws StripeException {

        Session session = Mockito.mock(Session.class);
        when(session.getUrl()).thenReturn(sessionParams.getSuccessUrl());
        when(paymentService.calculatePaymentAmount(anyInt())).thenReturn(100L);
        Session mock = mock(Session.create(sessionParams).getClass());
        doReturn(session).when(mock);

        assertEquals(sessionParams.getSuccessUrl(), session.getUrl());
        assertDoesNotThrow(() -> stripeService.getPaymentFromSession(paymentRequestInfoDto));
    }
}