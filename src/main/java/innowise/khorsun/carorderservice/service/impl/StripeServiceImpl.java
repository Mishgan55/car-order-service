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
import innowise.khorsun.carorderservice.util.PropertyUtil;
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


@Service
public class StripeServiceImpl implements StripeService {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @Value("${STRIPE_API_KEY}")
    private String stripeApiKey;

    @Autowired
    public StripeServiceImpl(PaymentService paymentService, PaymentMapper paymentMapper) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public SessionCreateParams createPaymentSession(Integer userId, Type type) {
        Stripe.apiKey = stripeApiKey;
        SessionCreateParams.Builder builder = new SessionCreateParams.Builder();
        builder.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD);
        builder.setMode(SessionCreateParams.Mode.PAYMENT);
        builder.setSuccessUrl(PropertyUtil.PAYMENT_URL + "/success" + "?session_id={CHECKOUT_SESSION_ID}");
        builder.setCancelUrl(PropertyUtil.PAYMENT_URL + "/cancel");
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
                throw new PaymentSessionException(PropertyUtil.INVALID_SESSION_MESSAGE);
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
            throw new PaymentSessionException(PropertyUtil.STRIPE_ERROR_MESSAGE + e.getMessage());
        } catch (MalformedURLException e) {
            throw new PaymentSessionException(PropertyUtil.MALFORMED_URL_MESSAGE + e.getMessage());
        }
    }
}
