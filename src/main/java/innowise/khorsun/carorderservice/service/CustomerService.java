package innowise.khorsun.carorderservice.service;

import innowise.khorsun.carorderservice.dto.CustomerDto;
import innowise.khorsun.carorderservice.model.CustomerUpdateModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CustomerService {
    CustomerDto getCustomerDtoById(Integer id);
    List<CustomerDto> getAllCustomers();
    void addCustomer(CustomerDto customerDto);
    void removeCustomer(Integer id);
    void editCustomer(Integer id, CustomerUpdateModel customerUpdateModel);
}
