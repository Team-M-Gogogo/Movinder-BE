package com.movinder.be.service;


import com.movinder.be.entity.Customer;
import com.movinder.be.exception.MalformedRequestException;
import com.movinder.be.exception.ProvidedKeyAlreadyExistException;
import com.movinder.be.exception.RequestDataNotCompleteException;
import com.movinder.be.repository.CustomerRepository;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Stream;

@Service
public class CustomerService {
    private final CustomerRepository customerMongoRepository;

    public CustomerService(CustomerRepository customerMongoRepository) {
        this.customerMongoRepository = customerMongoRepository;
    }


    public Customer createCustomerAccount(Customer customer) {

        if (customer.getCustomerId() != null){
            throw new MalformedRequestException("Create customer request should not contain ID");
        }
        validateCustomerAttributes(customer);

        System.out.println(customer.getCustomerName());

        try {
            return customerMongoRepository.save(customer);
        } catch (DuplicateKeyException customerExist) {
            throw new ProvidedKeyAlreadyExistException("Customer name");
        }
    }

    // checks if object contains null attributes
    private void validateCustomerAttributes(Customer customer) {

        boolean containsNull = Stream
                .of(customer.getCustomerName(), customer.getPassword(), customer.getGender(), customer.getAge(),
                        customer.getStatus(), customer.getSelfIntro(), customer.getShowName(),
                        customer.getShowGender(), customer.getShowAge(), customer.getShowStatus())
                .anyMatch(Objects::isNull);

        if (containsNull){
            throw new RequestDataNotCompleteException("Customer");
        }
    }

}
