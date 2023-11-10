package innowise.khorsun.carorderservice.util.error.payment;

import lombok.Getter;

@Getter
public class PaymentSessionException extends RuntimeException{
    public PaymentSessionException(String message) {
        super(message);
    }
}
