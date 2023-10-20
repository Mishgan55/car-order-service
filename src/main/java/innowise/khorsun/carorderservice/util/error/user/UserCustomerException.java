package innowise.khorsun.carorderservice.util.error.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import lombok.Setter;
import lombok.ToString;

import java.util.Date;
@AllArgsConstructor
@Getter
@Setter
@ToString
public class UserCustomerException extends RuntimeException{
    private final String message;
    private final Date date;
}
