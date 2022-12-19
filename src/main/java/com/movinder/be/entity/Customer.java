package com.movinder.be.entity;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
public class Customer {
    @MongoId(FieldType.OBJECT_ID)
    private String customerId;

    @Indexed(unique = true)
    private String customerName;


    public Customer(String customerId, String customerName){
        this.customerId = customerId;
        this.customerName = customerName;
    }

    public Customer(){

    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
