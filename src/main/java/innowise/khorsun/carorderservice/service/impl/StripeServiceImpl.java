package innowise.khorsun.carorderservice.service.impl;

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
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static com.stripe.Stripe.*;


@Service
public class StripeServiceImpl implements StripeService {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;
    private final UserService userService;
    private final KafkaTemplate<String, Map<String, String>> kafkaTemplate;
    @Value("${spring.kafka.topic.notification.link-for-payment}")
    private String linkForPaymentTopic;

    @Value("${STRIPE_API_KEY}")
    private String stripeApiKey;

    @Autowired
    public StripeServiceImpl(PaymentService paymentService, PaymentMapper paymentMapper,
                             UserService userService, KafkaTemplate<String, Map<String, String>> kafkaTemplate) {
        this.paymentService = paymentService;
        this.paymentMapper = paymentMapper;
        this.userService = userService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public SessionCreateParams createPaymentSession(Integer userId, Type type) {
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
                                                        .calculatePaymentAmount(userId))
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

            //send kafka topic for notification service to sendNotificationWithPaymentLink() method
            sendNotificationWithLinkAndEmail(userService.getUserById(paymentRequestInfoDto.getUserId()).getEmail(),sessionUrl);

            if (sessionUrl == null || sessionId == null) {
                throw new PaymentSessionException(PropertyUtil.INVALID_SESSION_MESSAGE);
            }
            Long amountTotal = session.getAmountTotal();
            PaymentDto paymentDto = new PaymentDto();
            paymentDto.setUserId(userId);
            paymentDto.setSessionId(sessionId);
            paymentDto.setUrl(new URL(sessionUrl));
            paymentDto.setType(paymentRequestInfoDto.getType());
            paymentDto.setStatus(Status.PENDING);
            paymentDto.setPaymentAmount(BigDecimal.valueOf(amountTotal));
            return paymentMapper.paymentToPaymentDto(paymentService.addPayment(paymentDto));
        } catch (StripeException e) {
            throw new PaymentSessionException(PropertyUtil.STRIPE_ERROR_MESSAGE + e.getMessage());
        } catch (MalformedURLException e) {
            throw new PaymentSessionException(PropertyUtil.MALFORMED_URL_MESSAGE + e.getMessage());
        }
    }
    /**
     * Create json from map and send it to kafka
     */
    private void sendNotificationWithLinkAndEmail(String email,String link){
        //send kafka topic for notification service for sendNotificationWithPaymentLink() method
        Map<String, String> map = new HashMap<>();
        map.put("email", email);
        map.put("link", link);
        //Create json record for kafka
        ProducerRecord<String, Map<String, String>> recordMessage = new ProducerRecord<>(linkForPaymentTopic, map);
        kafkaTemplate.send(recordMessage);
        kafkaTemplate.flush();
    }
}
