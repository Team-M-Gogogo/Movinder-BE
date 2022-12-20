package com.movinder.be;

import com.fasterxml.jackson.databind.ObjectMapper;
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
}
