package innowise.khorsun.carorderservice.service.impl;

import innowise.khorsun.carorderservice.dto.CustomerDto;
import innowise.khorsun.carorderservice.entity.Customer;
import innowise.khorsun.carorderservice.mapper.CustomerMapper;
import innowise.khorsun.carorderservice.model.CustomerUpdateModel;
import innowise.khorsun.carorderservice.repositorie.CustomerRepository;
import innowise.khorsun.carorderservice.service.CustomerService;
import innowise.khorsun.carorderservice.util.error.customer.CustomerNotFoundException;
import innowise.khorsun.carorderservice.util.error.customer.DuplicateCustomerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDto getCustomerDtoById(Integer id) {
        return customerRepository
                .findById(id)
                .map(customerMapper::customerToCustomerDto)
                .orElseThrow(() -> new CustomerNotFoundException("Customer not found", new Date()));
    }

    @Override
    public List<CustomerDto> getAllCustomers() {
        return customerRepository
                .findAll().stream()
                .map(customerMapper::customerToCustomerDto).toList();
    }

    @Override
    @Transactional
    public void addCustomer(CustomerDto customerDto) {
        if (!isCustomerEmailUnique(customerDto.getEmail())) {
            throw new DuplicateCustomerException("Customer with this email is already exist", new Date());
        }
        if (!isCustomerPhoneNumberUnique(customerDto.getPhoneNumber())) {
            throw new DuplicateCustomerException("Customer with this phone number is already exist", new Date());
        }
        Customer customer = customerMapper.customerDtoToCustomer(customerDto);
        customer.setCreationDate(new Date());
        customerRepository.save(customer);
    }

    @Override
    public void removeCustomer(Integer id) {
        customerRepository.deleteById(id);

    }

    @Override
    @Transactional
    public void editCustomer(Integer id, CustomerUpdateModel customerUpdateModel) {
        Optional<Customer> customer = customerRepository.findById(id);
        if (customer.isPresent()) {
            Customer updatedCustomer = customer.get();
            updatedCustomer.setFirstName(customerUpdateModel.getFirstName());
            updatedCustomer.setLastName(customerUpdateModel.getLastName());
        } else {
            throw new CustomerNotFoundException("Customer not found", new Date());
        }
    }

    public boolean isCustomerEmailUnique(String email) {
        return customerRepository.findCustomerByEmail(email).isEmpty();
    }

    public boolean isCustomerPhoneNumberUnique(String phoneNumber) {
        return customerRepository.findCustomerByPhoneNumber(phoneNumber).isEmpty();
    }
}
