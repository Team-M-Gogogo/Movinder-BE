package com.movinder.be;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movinder.be.controller.dto.AddChatRequest;
import com.movinder.be.entity.Customer;
import com.movinder.be.entity.Movie;
import com.movinder.be.entity.Room;
import com.movinder.be.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;

@SpringBootTest
@AutoConfigureMockMvc
public class ForumControllerTest {
    @Autowired
    MockMvc client;

    @Autowired
    RoomRepository roomRepository;
    @Autowired
    MessageRepository messageRepository;
    @Autowired
    CustomerRepository customerRepository;


    @BeforeEach
    public void clearDB() {
        roomRepository.deleteAll();
        messageRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    public void should_get_message_when_perform_add_message_given_add_chat_request_obj() throws Exception {
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

        Customer savedCustomer = customerRepository.save(customer);

        String roomId = "63a00a4955506136f35be596";
        Room room = new Room(roomId, new ArrayList<>(), new ArrayList<>(), "63a00a4955506136f35be599");
        Room savedRoom = roomRepository.save(room);

        AddChatRequest addChatRequest = new AddChatRequest(savedCustomer.getCustomerId(), "test message", savedRoom.getRoomId());

        String chatRequestJson = new ObjectMapper().writeValueAsString(addChatRequest);


        //when & then
        client.perform(MockMvcRequestBuilders.put("/forum/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(chatRequestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.messageId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$.customerId").value(savedCustomer.getCustomerId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.message").value("test message"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdTime").exists());
    }


}
