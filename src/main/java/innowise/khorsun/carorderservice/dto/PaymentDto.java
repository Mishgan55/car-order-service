package innowise.khorsun.carorderservice.dto;

import innowise.khorsun.carorderservice.util.enums.Status;
import innowise.khorsun.carorderservice.util.enums.Type;
import lombok.Data;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDateTime;

@Data
public class PaymentDto {
    private Integer id;
    private Integer userId;
    private String sessionId;
    private Status status;
    private URL url;
    private BigDecimal paymentAmount;
    private Type type;
    private LocalDateTime paymentDate;
}
