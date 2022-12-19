package com.movinder.be.controller;

import com.movinder.be.entity.Customer;
import com.movinder.be.service.CustomerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/customer")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/{customerID}")
    @ResponseStatus(code = HttpStatus.OK)
    public Customer getCustomer(@PathVariable String customerID) {
        return customerService.findByCustomerId(customerID);
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public Customer create(@RequestBody Customer customer) {
        return customerService.createCustomerAccount(customer);
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public String test() {
        return "SUCCESS: Server is running";
    }

}
