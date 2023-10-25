package innowise.khorsun.carorderservice.controller;

import innowise.khorsun.carorderservice.dto.PaymentDto;
import innowise.khorsun.carorderservice.model.PaymentRequestModel;
import innowise.khorsun.carorderservice.service.PaymentService;
import innowise.khorsun.carorderservice.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final StripeService stripeService;
    private final PaymentService paymentService;


    @Autowired
    public PaymentController(StripeService stripeService, PaymentService paymentService) {
        this.stripeService = stripeService;
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<String> createStripeSession(@RequestBody PaymentRequestModel paymentRequestModel) {
        PaymentDto paymentFromSession = stripeService.getPaymentFromSession(stripeService.createPaymentSession(
                paymentRequestModel.getUserId(), paymentRequestModel.getType()), paymentRequestModel);
        return ResponseEntity.ok(paymentFromSession.getUrl().toString());
    }

    @GetMapping("/success")
    public String success(@RequestParam("session_id") String sessionId) {
        return  paymentService.checkSuccess(paymentService.findBySessionId(sessionId),sessionId);
    }

    @GetMapping("/cancel")
    public ResponseEntity<String> returnPaymentPausedMessage() {
        return ResponseEntity.ok("Payment can be made later. "
                + "The session is available for 23 hours.");
    }
}
