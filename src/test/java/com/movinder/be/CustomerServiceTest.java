package com.movinder.be;

import com.movinder.be.entity.Customer;
import com.movinder.be.entity.Movie;
import com.movinder.be.repository.CinemaRepository;
import com.movinder.be.repository.CustomerRepository;
import com.movinder.be.repository.MovieRepository;
import com.movinder.be.repository.MovieSessionRepository;
import com.movinder.be.service.CustomerService;
import com.movinder.be.service.MovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class CustomerServiceTest {
    @Mock
    CustomerRepository customerRepository;

    @InjectMocks
    CustomerService customerService;

    @Test
    public void should_return_a_customer_when_add_customer_given_a_customer(){
        // given
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

        given(customerRepository.save(customer)).willReturn(customer);

        // when
        Customer saveCustomer = customerService.createCustomerAccount(customer);

        // then
        assertThat(saveCustomer.getCustomerName(), equalTo("name"));
        assertThat(saveCustomer.getPassword(), equalTo("pass"));
        assertThat(saveCustomer.getGender(), equalTo("Male"));
        assertThat(saveCustomer.getAge(), equalTo(20));
        assertThat(saveCustomer.getStatus(), equalTo("available"));
        assertThat(saveCustomer.getSelfIntro(), equalTo("intro"));
        assertFalse(saveCustomer.getShowName());
        assertTrue(saveCustomer.getShowGender());
        assertTrue(saveCustomer.getShowAge());
        assertTrue(saveCustomer.getShowStatus());

        verify(customerRepository).save(customer);
    }
}
