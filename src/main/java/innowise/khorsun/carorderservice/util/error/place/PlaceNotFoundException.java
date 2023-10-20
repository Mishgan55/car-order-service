package innowise.khorsun.carorderservice.util.error.place;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Date;
@Getter
@Setter
@RequiredArgsConstructor
public class PlaceNotFoundException extends RuntimeException{
    private final String message;
    private final Date date;
}