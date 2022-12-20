package com.movinder.be;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movinder.be.controller.dto.CustomerAuthenticateRequest;
import com.movinder.be.entity.Customer;
import com.movinder.be.entity.Movie;
import com.movinder.be.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerControllerTest {
    @Autowired
    MockMvc client;

    @Autowired
    CustomerRepository customerRepository;

    @BeforeEach
    public void clearDB() {
        customerRepository.deleteAll();
    }

    @Test
    public void should_create_customer_when_perform_create_given_customer_obj() throws Exception {
        //given
        Customer customer = new Customer();
        customer.setCustomerName("name");
        customer.setPassword("pass");
        customer.setGender("Male");
        customer.setStatus("available");
        customer.setSelfIntro("intro");
        customer.setAge(20);
        customer.setShowName(false);
        customer.setShowGender(true);
        customer.setShowAge(true);
        customer.setShowStatus(true);

        String customerJson = new ObjectMapper().writeValueAsString(customer);


        //when & then
        client.perform(MockMvcRequestBuilders.post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("pass"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("available"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.selfIntro").value("intro"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.showName").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.showGender").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.showAge").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.showStatus").value(true));
    }

    @Test
    public void should_get_customer_when_perform_authenticate_csutomer_given_name_and_password() throws Exception {
        //given
        CustomerAuthenticateRequest authenticateRequest = new CustomerAuthenticateRequest("name", "pass");

        String customerAuthenticationJson = new ObjectMapper().writeValueAsString(authenticateRequest);

        Customer customer = new Customer();
        customer.setCustomerName("name");
        customer.setPassword("pass");
        customer.setGender("Male");
        customer.setStatus("available");
        customer.setSelfIntro("intro");
        customer.setAge(20);
        customer.setShowName(false);
        customer.setShowGender(true);
        customer.setShowAge(true);
        customer.setShowStatus(true);

        customerRepository.save(customer);

        //when & then
        client.perform(MockMvcRequestBuilders.post("/customers/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerAuthenticationJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("name"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("pass"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(20))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("available"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.selfIntro").value("intro"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.showName").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.showGender").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.showAge").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.showStatus").value(true));
    }

    @Test
    public void should_update_customer_when_perform_update_customer_given_customer() throws Exception {
        //given

        Customer customer = new Customer();
        customer.setCustomerName("name");
        customer.setPassword("pass");
        customer.setGender("Male");
        customer.setStatus("available");
        customer.setSelfIntro("intro");
        customer.setAge(20);
        customer.setShowName(false);
        customer.setShowGender(true);
        customer.setShowAge(true);
        customer.setShowStatus(true);

        Customer customerSaved = customerRepository.save(customer);

        Customer updatedCustomer = new Customer();
        updatedCustomer.setCustomerId(customerSaved.getCustomerId());
        updatedCustomer.setCustomerName("name2");
        updatedCustomer.setPassword("pass");
        updatedCustomer.setGender("Male");
        updatedCustomer.setStatus("available");
        updatedCustomer.setSelfIntro("intro");
        updatedCustomer.setAge(200);
        updatedCustomer.setShowName(true);
        updatedCustomer.setShowGender(true);
        updatedCustomer.setShowAge(true);
        updatedCustomer.setShowStatus(true);

        String customerJson = new ObjectMapper().writeValueAsString(updatedCustomer);



        //when & then
        client.perform(MockMvcRequestBuilders.put("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerName").value("name2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("pass"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.gender").value("Male"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.age").value(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("available"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.selfIntro").value("intro"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.showName").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.showGender").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.showAge").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.showStatus").value(true));
    }
}
