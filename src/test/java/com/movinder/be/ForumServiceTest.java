package com.movinder.be;

import com.movinder.be.controller.dto.AddChatRequest;
import com.movinder.be.entity.Customer;
import com.movinder.be.entity.Message;
import com.movinder.be.entity.Room;
import com.movinder.be.repository.MessageRepository;
import com.movinder.be.repository.RoomRepository;
import com.movinder.be.service.CustomerService;
import com.movinder.be.service.ForumService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
public class ForumServiceTest {

    @InjectMocks
    ForumService forumService;

    @Mock
    CustomerService customerService;

    @Mock
    MessageRepository messageRepository;

    @Mock
    RoomRepository roomRepository;

    @Test
    public void should_return_a_message_when_add_message_given_an_add_chat_request_obj(){
        // given

        String customerId = "63a00a4955506136f35be595";
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
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
        given(customerService.findByCustomerId(customerId)).willReturn(customer);

        String messageId = "63a00a4955506136f35be597";
        Message mockMessage = new Message(customerId, "test");
        mockMessage.setMessageId(messageId);
        String start = "1970-01-01T00:00:00";
        LocalDateTime now = LocalDateTime.parse(start);
        mockMessage.setCreatedTime(now);

        given(messageRepository.save(Mockito.any(Message.class))).willReturn(mockMessage);

        String roomId = "63a00a4955506136f35be596";
        Room room = new Room(roomId, new ArrayList<>(), new ArrayList<>(), "63a00a4955506136f35be599");
        given(roomRepository.findById(roomId)).willReturn(java.util.Optional.of(room));

        // when
        Message savedMessage = forumService.addMessage(new AddChatRequest(customerId, "test", roomId));

        // then
        assertThat(savedMessage.getMessage(), equalTo("test"));
        assertThat(savedMessage.getCustomerId(), equalTo(customerId));
        assertThat(savedMessage.getMessageId(), equalTo(messageId));
        assertEquals(savedMessage.getCreatedTime(), now);

        verify(customerService).findByCustomerId(customerId);
        verify(messageRepository).save(Mockito.any(Message.class));
        verify(roomRepository).findById(roomId);

    }

    @Test
    public void should_return_a_list_of_room_when_get_room_by_customer_id_given_customer_id(){
        // given
        String customerId = "63a00a4955506136f35be595";
        String roomId = "63a00a4955506136f35be596";
        Room room = new Room(roomId, new ArrayList<>(), new ArrayList<>(), "63a00a4955506136f35be599");
        given(roomRepository.findByCustomerIdsContaining(customerId)).willReturn(Arrays.asList(room));

        // when
        List<Room> fetchRoom = forumService.getRoomByCustomerId(customerId);

        // then
        assertThat(fetchRoom, equalTo(Arrays.asList(room)));


        verify(roomRepository).findByCustomerIdsContaining(customerId);
    }


}
