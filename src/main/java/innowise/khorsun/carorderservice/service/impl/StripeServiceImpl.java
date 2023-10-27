package innowise.khorsun.carorderservice.service.impl;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import innowise.khorsun.carorderservice.dto.PaymentDto;
import innowise.khorsun.carorderservice.mapper.PaymentMapper;
import innowise.khorsun.carorderservice.model.PaymentRequestModel;
import innowise.khorsun.carorderservice.service.PaymentService;
import innowise.khorsun.carorderservice.service.StripeService;
import innowise.khorsun.carorderservice.util.enums.Status;
import innowise.khorsun.carorderservice.util.enums.Type;
import innowise.khorsun.carorderservice.util.error.payment.PaymentSessionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

@Service
public class StripeServiceImpl implements StripeService {

    private static final String PAYMENT_URL = "http://localhost:8082/e-car-order/payments";
    private static final String INVALID_SESSION_MESSAGE = "Invalid session data received from Stripe";
    private static final String STRIPE_ERROR_MESSAGE = "Stripe error: ";
    private static final String MALFORMED_URL_MESSAGE = "Malformed URL: ";
    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;
    private final ResourceBundle resourceBundle;

    @Value("${STRIPE_API_KEY}")
    private String stripeApiKey;

    @Autowired
    public StripeServiceImpl(PaymentService paymentService, PaymentMapper paymentMapper, ResourceBundle resourceBundle) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
        this.resourceBundle = resourceBundle;
    }

    @Override
    public SessionCreateParams createPaymentSession(Integer userId, Type type) {
        Stripe.apiKey = stripeApiKey;
        SessionCreateParams.Builder builder = new SessionCreateParams.Builder();
        builder.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD);
        builder.setMode(SessionCreateParams.Mode.PAYMENT);
        builder.setSuccessUrl(PAYMENT_URL + "/success" + "?session_id={CHECKOUT_SESSION_ID}");
        builder.setCancelUrl(PAYMENT_URL + "/cancel");
        builder.setClientReferenceId(userId.toString());
        builder.addLineItem(
                new SessionCreateParams.LineItem.Builder()
                        .setPriceData(
                                new SessionCreateParams.LineItem.PriceData.Builder()
                                        .setCurrency("usd")
                                        .setProductData(
                                                new SessionCreateParams.LineItem
                                                        .PriceData.ProductData.Builder()
                                                        .setName("Car User Payment")
                                                        .build()
                                        )
                                        .setUnitAmount(
                                                paymentService
                                                        .calculatePaymentAmount(userId)
                                                        .longValue())
                                        .build()
                        )
                        .setQuantity(1L)
                        .build()
        );
        return builder.build();
    }

    @Override
    public PaymentDto getPaymentFromSession(PaymentRequestModel paymentRequestInfoDto) {
        SessionCreateParams params = createPaymentSession(
                paymentRequestInfoDto.getUserId(), paymentRequestInfoDto.getType());
        try {
            Session session = Session.create(params);
            String sessionUrl = session.getUrl();
            String sessionId = session.getId();
            Integer userId = Integer.valueOf(session.getClientReferenceId());

            if (sessionUrl == null || sessionId == null) {
                throw new PaymentSessionException(resourceBundle.getString(INVALID_SESSION_MESSAGE));
            }
            BigDecimal amountToPay = new BigDecimal(session.getAmountTotal());
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setUserId(userId);
            paymentDto.setSessionId(sessionId);
            paymentDto.setUrl(new URL(sessionUrl));
            paymentDto.setType(paymentRequestInfoDto.getType());
            paymentDto.setStatus(Status.PENDING);
            paymentDto.setPaymentAmount(amountToPay.divide(BigDecimal.valueOf(100), RoundingMode.HALF_UP));
            return paymentMapper.paymentToPaymentDto(paymentService.addPayment(paymentDto));
        } catch (StripeException e) {
            throw new PaymentSessionException(resourceBundle.getString(STRIPE_ERROR_MESSAGE) + e.getMessage());
        } catch (MalformedURLException e) {
            throw new PaymentSessionException(resourceBundle.getString(MALFORMED_URL_MESSAGE) + e.getMessage());
        }
    }
}
