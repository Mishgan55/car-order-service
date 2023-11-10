package innowise.khorsun.carorderservice.model;


import innowise.khorsun.carorderservice.util.enums.Type;
import lombok.Data;

@Data
public class PaymentRequestModel {
     private Integer userId;
     private Type type;
}
