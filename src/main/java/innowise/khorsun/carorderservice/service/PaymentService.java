package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.PaymentDto;
import innowise.khorsun.carorderservice.entity.Payment;
import org.springframework.stereotype.Service;


@Service
public interface PaymentService {
    Payment addPayment(PaymentDto paymentDto);

    Long calculatePaymentAmount(Integer rentalId);

    boolean isSessionPaid(String sessionId);

    Payment findBySessionId(String sessionId);

    String checkSuccess(Payment payment, String sessionId);
}
