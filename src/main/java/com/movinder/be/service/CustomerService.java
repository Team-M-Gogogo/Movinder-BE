package com.movinder.be.service;

import com.movinder.be.entity.Customer;
import com.movinder.be.repository.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {
    private final CustomerRepository customerMongoRepository;

    public CustomerService(CustomerRepository customerMongoRepository) {
        this.customerMongoRepository = customerMongoRepository;
    }

    public Customer findByCustomerId(String customerID){
        return customerMongoRepository.findById(customerID).get();
    }

}
