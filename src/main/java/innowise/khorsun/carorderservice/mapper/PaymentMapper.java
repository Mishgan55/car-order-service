package innowise.khorsun.carorderservice.mapper;

import innowise.khorsun.carorderservice.dto.PaymentDto;
import innowise.khorsun.carorderservice.entity.Payment;
import innowise.khorsun.carorderservice.model.PaymentRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface PaymentMapper {
    @Mapping(target = "user.id", source = "userId")
    Payment paymentDtoToPayment(PaymentDto paymentDto);

    PaymentDto paymentToPaymentDto(Payment payment);
    @Mapping(target = "userId", source = "user.id")
    PaymentRequestModel paymentToPaymentRequestModel(Payment payment);
}
