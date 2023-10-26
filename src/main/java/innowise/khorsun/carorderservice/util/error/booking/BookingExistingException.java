package innowise.khorsun.carorderservice.util.error.booking;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@RequiredArgsConstructor
public class BookingExistingException extends RuntimeException{
    private final String message;
    private final Date date;
}
