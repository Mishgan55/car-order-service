package innowise.khorsun.carorderservice.service;

import com.stripe.param.checkout.SessionCreateParams;
import innowise.khorsun.carorderservice.dto.PaymentDto;
import innowise.khorsun.carorderservice.model.PaymentRequestModel;
import innowise.khorsun.carorderservice.util.enums.Type;
import org.springframework.stereotype.Service;

@Service
public interface StripeService {
    SessionCreateParams createPaymentSession(Integer userId, Type type);

    PaymentDto getPaymentFromSession(PaymentRequestModel paymentRequestInfoDto);
}
