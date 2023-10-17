package innowise.khorsun.carorderservice.mapper;

import innowise.khorsun.carorderservice.dto.CustomerDto;
import innowise.khorsun.carorderservice.entity.Customer;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public interface CustomerMapper {
    Customer customerDtoToCustomer(CustomerDto customerDto);
    CustomerDto customerToCustomerDto(Customer customer);
}
