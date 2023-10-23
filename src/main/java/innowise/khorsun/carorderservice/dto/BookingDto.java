package innowise.khorsun.carorderservice.dto;

import innowise.khorsun.carorderservice.util.enums.Status;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
public class BookingDto {
    private Integer userId;
    private Integer carId;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    @Enumerated(EnumType.STRING)
    private Status status;
}
