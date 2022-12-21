package com.movinder.be;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movinder.be.controller.dto.AddChatRequest;
import com.movinder.be.entity.Customer;
import com.movinder.be.entity.Message;
import com.movinder.be.entity.Room;
import com.movinder.be.repository.CustomerRepository;
import com.movinder.be.repository.MessageRepository;
import com.movinder.be.repository.RoomRepository;
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

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;

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

        AddChatRequest addChatRequest = new AddChatRequest(savedCustomer.getCustomerId(), "test message", "63a00a4955506136f35be599");

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

    @Test
    public void should_get_rooms_when_perform_find_room_by_customer_id_given_customer_id() throws Exception {
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

        Room room = new Room(new ArrayList<>(), new ArrayList<String>(){{add(savedCustomer.getCustomerId()); add("123123");}}, "63a00a4955506136f35be599");
        Room savedRoom = roomRepository.save(room);
        Room room2 = new Room(new ArrayList<>(), new ArrayList<String>(){{add(savedCustomer.getCustomerId());}}, "63a00a4955506136f35be560");
        Room savedRoom2 = roomRepository.save(room2);


        //when & then
        client.perform(MockMvcRequestBuilders.get("/forum/rooms/{customerID}", savedCustomer.getCustomerId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].roomId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].roomId").isString())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].messageIds", empty()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].messageIds", empty()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].customerIds", containsInAnyOrder(new ArrayList<String>(){{add(savedCustomer.getCustomerId());}}, new ArrayList<String>(){{add(savedCustomer.getCustomerId()); add("123123");}})))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].movieId", containsInAnyOrder("63a00a4955506136f35be560", "63a00a4955506136f35be599")));

    }

    @Test
    public void should_get_messages_when_perform_get_message_by_id_given_movie_id() throws Exception {
        //given


        String movieId = "63a00a4955506136f35be596";

        Message message = new Message("63a00a4955506136f35be597", "test");
        Message savedMessage = messageRepository.save(message);
        Message message2 = new Message("63a00a4955506136f35be597", "test2");
        Message savedMessage2 = messageRepository.save(message2);

        Room room = new Room(new ArrayList<String>(){{add(savedMessage.getMessageId()); add(savedMessage2.getMessageId());}}, new ArrayList<>(), movieId);
        Room savedRoom = roomRepository.save(room);

        //when & then
        client.perform(MockMvcRequestBuilders.get("/forum/rooms?movieID={movieID}", movieId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].messageId").value(savedMessage.getMessageId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].messageId").value(savedMessage2.getMessageId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].customerId").value("63a00a4955506136f35be597"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].customerId").value("63a00a4955506136f35be597"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].message", containsInAnyOrder("test", "test2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[*].createdTime").exists());

    }


}
