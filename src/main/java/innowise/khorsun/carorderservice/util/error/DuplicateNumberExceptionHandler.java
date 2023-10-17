package innowise.khorsun.carorderservice.util.error;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class DuplicateNumberExceptionHandler {


    @ExceptionHandler({DuplicateCarPlateNumberException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleDuplicateCarPlateNumber(DuplicateCarPlateNumberException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());

    }
}
