package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.entity.Payment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface PaymentService {
    Payment save(Payment payment);
    BigDecimal calculatePaymentAmount(Integer rentalId);

    boolean isSessionPaid(String sessionId);

    Payment findBySessionId(String sessionId);
    String checkSuccess(Payment payment,String sessionId);
}
