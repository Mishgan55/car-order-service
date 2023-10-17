package innowise.khorsun.carorderservice.controller;

import innowise.khorsun.carorderservice.dto.CustomerDto;
import innowise.khorsun.carorderservice.model.CustomerUpdateModel;
import innowise.khorsun.carorderservice.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerService customerService;

    @Autowired
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDto> getCustomerDtoById(@PathVariable("id") Integer id) {
        return ResponseEntity.ok(customerService.getCustomerDtoById(id));
    }

    @GetMapping()
    public ResponseEntity<List<CustomerDto>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @PostMapping("/add")
    public void addCustomer(@RequestBody @Valid CustomerDto customerDto) {
        customerService.addCustomer(customerDto);
    }

    @DeleteMapping("/{id}")
    public void removeCustomer(@PathVariable("id") Integer id) {
        customerService.removeCustomer(id);
    }

    @PatchMapping("/{id}/edit")
    public void editCustomer(@PathVariable("id") Integer id, @RequestBody @Valid CustomerUpdateModel customerUpdateModel) {
        customerService.editCustomer(id, customerUpdateModel);
    }
}
