package innowise.khorsun.carorderservice.util.error.customer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DuplicateCustomerExceptionHandler {
    @ExceptionHandler({DuplicateCustomerException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleDuplicateCustomerNumber(DuplicateCustomerException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage()+", "+ex.getDate());

    }
}
